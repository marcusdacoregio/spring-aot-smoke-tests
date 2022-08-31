package org.example.saml2serviceprovider;

import java.io.IOException;
import java.net.URI;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.aot.smoketest.support.junit.ApplicationTest;
import org.springframework.aot.smoketest.support.junit.ApplicationUrl;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationTest
class Saml2ServiceProviderApplicationAotTests {

	private WebClient webClient;

	@BeforeEach
	void setup(@ApplicationUrl(scheme = ApplicationUrl.Scheme.HTTP) URI applicationUrl) {
		this.webClient = new CustomWebClient(applicationUrl);
		this.webClient.getCookieManager().setCookiesEnabled(true);
		this.webClient.getCookieManager().clearCookies();
	}

	@Test
	void authenticationAttemptWhenValidThenShowsUserEmailAddress() throws Exception {
		performLogin();
		this.webClient.waitForBackgroundJavaScript(20000);
		System.out.println("Before assert " + this.webClient.getCookieManager().getCookies());
		HtmlPage home = (HtmlPage) this.webClient.getCurrentWindow().getEnclosedPage();
		assertThat(home.asNormalizedText()).contains("You're email address is testuser@spring.security.saml");
	}

	@Test
	@Disabled
	void logoutWhenRelyingPartyInitiatedLogoutThenLoginPageWithLogoutParam() throws Exception {
		performLogin();
		HtmlPage home = (HtmlPage) this.webClient.getCurrentWindow().getEnclosedPage();
		HtmlElement rpLogoutButton = home.getHtmlElementById("rp_logout_button");
		HtmlPage loginPage = rpLogoutButton.click();
		assertThat(loginPage.getUrl().getFile()).isEqualTo("/login?logout");
	}

	private void performLogin() throws Exception {
		HtmlPage login = this.webClient.getPage("/");
		System.out.println("Initial " + this.webClient.getCookieManager().getCookies());
		this.webClient.waitForBackgroundJavaScript(20000);
		HtmlForm form = findForm(login);
		HtmlInput username = form.getInputByName("username");
		HtmlPasswordInput password = form.getInputByName("password");
		HtmlSubmitInput submit = login.getHtmlElementById("okta-signin-submit");
		username.type("testuser@spring.security.saml");
		password.type("12345678");
		submit.click();
		System.out.println("After submit " + this.webClient.getCookieManager().getCookies());
		this.webClient.waitForBackgroundJavaScript(20000);
	}

	private HtmlForm findForm(HtmlPage login) {
		for (HtmlForm form : login.getForms()) {
			try {
				if (form.getId().equals("form19")) {
					return form;
				}
			}
			catch (ElementNotFoundException ex) {
				// Continue
			}
		}
		throw new IllegalStateException("Could not resolve login form");
	}

	private static class CustomWebClient extends WebClient {

		private final URI applicationUrl;

		private CustomWebClient(URI applicationUrl) {
			this.applicationUrl = applicationUrl;
		}

		@Override
		public <P extends Page> P getPage(String url) throws IOException, FailingHttpStatusCodeException {
			if (url.startsWith("/")) {
				url = this.applicationUrl + url;
			}
			return super.getPage(url);
		}
	}

}
