package com.hsbc.hbar.teller.broker.security;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.preauth.j2ee.J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource;

public class JWTAuthenticationDetailsSource extends J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource {

	@Override
	protected Collection<String> getUserRoles(HttpServletRequest request) {
		return Collections.emptyList();
	}
	
}
