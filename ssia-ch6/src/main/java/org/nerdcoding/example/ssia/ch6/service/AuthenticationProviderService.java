/*
 * AuthenticationProviderService.java
 *
 * Copyright (c) 2020, Tobias Koltsch. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 and
 * only version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-2.0.html>.
 */

package org.nerdcoding.example.ssia.ch6.service;


import org.nerdcoding.example.ssia.ch6.service.model.CustomerUserDateils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationProviderService implements AuthenticationProvider {

    private final JpaUserDetailsService jpaUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SCryptPasswordEncoder sCryptPasswordEncoder;

    @Autowired
    public AuthenticationProviderService(
            final JpaUserDetailsService jpaUserDetailsService,
            final BCryptPasswordEncoder bCryptPasswordEncoder,
            final SCryptPasswordEncoder sCryptPasswordEncoder) {
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sCryptPasswordEncoder = sCryptPasswordEncoder;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final CustomerUserDateils userDetails = (CustomerUserDateils)
                jpaUserDetailsService.loadUserByUsername(authentication.getName());

        switch (userDetails.getUser().getAlgorithm()) {
            case BCRYPT:
                return checkPassword(
                        userDetails,
                        authentication.getCredentials().toString(),
                        bCryptPasswordEncoder
                );
            case SCRYPT:
                return checkPassword(
                        userDetails,
                        authentication.getCredentials().toString(),
                        sCryptPasswordEncoder
                );
        }

        throw new BadCredentialsException("Bad credentials");
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication);
    }

    private Authentication checkPassword(
            final UserDetails user,
            final String password,
            final PasswordEncoder passwordEncoder) {

        if (passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities()
            );
        }

        throw new BadCredentialsException("Bad credentials");
    }
}
