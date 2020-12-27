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


import org.nerdcoding.example.ssia.ch11.business.server.authentication.UsernamePasswordAuthentication;
import org.nerdcoding.example.ssia.ch11.business.server.authentication.connector.AuthenticationServerConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationServerConnector authenticationServerConnector;

    @Autowired
    public UsernamePasswordAuthenticationProvider(final AuthenticationServerConnector authenticationServerConnector) {
        this.authenticationServerConnector = authenticationServerConnector;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        authenticationServerConnector.authWithUsernamePassword(
                authentication.getName(),
                String.valueOf(authentication.getCredentials())
        );

        return new UsernamePasswordAuthenticationToken(
                authentication.getName(),
                String.valueOf(authentication.getCredentials())
        );
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return UsernamePasswordAuthentication.class
                .isAssignableFrom(authentication);
    }
}
