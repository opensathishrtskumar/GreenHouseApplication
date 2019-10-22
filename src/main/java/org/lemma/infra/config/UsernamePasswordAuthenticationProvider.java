package org.lemma.infra.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.lemma.ems.base.dao.UserDetailsDAO;
import org.lemma.ems.base.dao.dto.UserDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author RTS Sathish Kumar
 * 
 *         Custom Authentication Provider
 */
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

	private static final Logger logger = LoggerFactory.getLogger(UsernamePasswordAuthenticationProvider.class);

	private UserDetailsDAO accountRepository;

	@PostConstruct
	public void init() {
		logger.trace("UsernamePasswordAuthentication initialized");
	}

	/**
	 * @param accountRepository
	 */
	@Inject
	public UsernamePasswordAuthenticationProvider(UserDetailsDAO accountRepository) {
		this.accountRepository = accountRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.AuthenticationProvider#
	 * authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		try {
			UserDetailsDTO userDetails = accountRepository.authenticate(token.getName(),
					(String) token.getCredentials(), UserDetailsDAO.Status.ACTIVE.getStatus());
			return authenticatedToken(userDetails, authentication);
		} catch (Exception e) {
			logger.error("{}", e);
			throw new org.springframework.security.core.userdetails.UsernameNotFoundException(token.getName(), e);
		}
	}

	/**
	 * @param authentication
	 * @return
	 */
	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

	/**
	 * @param account
	 * @param original
	 * @return
	 */
	private Authentication authenticatedToken(UserDetailsDTO userDetails, Authentication original) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		UsernamePasswordAuthenticationToken authenticated = new UsernamePasswordAuthenticationToken(userDetails, null,
				authorities);
		authenticated.setDetails(original.getDetails());
		return authenticated;
	}

}