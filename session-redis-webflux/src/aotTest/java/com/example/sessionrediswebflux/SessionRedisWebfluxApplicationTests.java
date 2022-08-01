package com.example.sessionrediswebflux;

import org.junit.jupiter.api.Test;

import org.springframework.aot.smoketest.support.junit.AotSmokeTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@AotSmokeTest
public class SessionRedisWebfluxApplicationTests {

	@Test
	void requestShouldContainSessionAttributes(WebTestClient client) {
		client.get().uri("/").headers((header) -> header.setBasicAuth("user", "password")).exchange().expectStatus()
				.isOk().expectBody()
				.consumeWith((result) -> assertThat(new String(result.getResponseBodyContent())).contains(
						"This page is secured using Spring Boot, Spring Session Redis, and Spring Security."));
	}

	@Test
	void getShouldBeProtectedWithoutCredentials(WebTestClient client) {
		client.get().uri("/").exchange().expectStatus().isUnauthorized();
	}

}
