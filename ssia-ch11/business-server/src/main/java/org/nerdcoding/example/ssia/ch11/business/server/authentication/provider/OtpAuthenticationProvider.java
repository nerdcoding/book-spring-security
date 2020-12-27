/*
 * UsernamePasswordAuthenticationProvider.java
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

package org.nerdcoding.example.ssia.ch11.business.server.authentication.provider;


import org.nerdcoding.example.ssia.ch11.business.server.authentication.OtpAuthentication;
import org.nerdcoding.example.ssia.ch11.business.server.authentication.connector.AuthenticationServerConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class OtpAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationServerConnector authenticationServerConnector;

    @Autowired
    public OtpAuthenticationProvider(final AuthenticationServerConnector authenticationServerConnector) {
        this.authenticationServerConnector = authenticationServerConnector;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final boolean authResult = authenticationServerConnector.authWithOtp(
                authentication.getName(),
                String.valueOf(authentication.getCredentials())
        );

        if (authResult) {
            return new OtpAuthentication(
                    authentication.getName(),
                    String.valueOf(authentication.getCredentials())
            );
        } else {
            throw new BadCredentialsException("Bad credentials.");
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return OtpAuthentication.class
                .isAssignableFrom(authentication);
    }
}
