package com.example.session;

import org.junit.jupiter.api.Test;

import org.springframework.aot.smoketest.support.junit.AotSmokeTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@AotSmokeTest
public class SessionJdbcApplicationAotTests {

	@Test
	void getHomeWithNoCredentialsThenUnauthorized(WebTestClient client) {
		client.get().uri("/").exchange().expectStatus().isUnauthorized();
	}

	@Test
	void getHomeThenReturnExpectedTextWithCredentials(WebTestClient client) {
		client.get().uri("/").headers((header) -> header.setBasicAuth("user", "password")).exchange().expectStatus().isOk()
				.expectBody().consumeWith((result) -> assertThat(new String(result.getResponseBodyContent()))
						.contains("This page is secured"));
	}

	@Test
	void getHomeThenReturnExpectedCookie(WebTestClient client) {
		client.get().uri("/").headers((header) -> header.setBasicAuth("user", "password")).exchange().expectStatus().isOk()
				.expectCookie().exists("SESSIONCOOKIE");
	}

}
