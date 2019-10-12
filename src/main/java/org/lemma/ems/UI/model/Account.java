package org.lemma.ems.UI.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Account implements Serializable {

	private final String email;

	private final String username;

	

	/**
	 * Constructs an Account object. The profielUrl parameter is a UriTemplate to
	 * allow the URL to be re-generated if the Account is updated.
	 */
	public Account(String name, String email, String username) {
		this.email = email;
		this.username = username;
	}
	
	
	
}