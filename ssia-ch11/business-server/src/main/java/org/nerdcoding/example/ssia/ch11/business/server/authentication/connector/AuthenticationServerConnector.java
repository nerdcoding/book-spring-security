/*
 * AuthenticationServerConnector.java
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

package org.nerdcoding.example.ssia.ch11.business.server.authentication.connector;

import org.nerdcoding.example.ssia.ch11.business.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationServerConnector {

    private final RestTemplate restTemplate;
    private final String authServerBaseUrl;

    public AuthenticationServerConnector(
            @Autowired final RestTemplate restTemplate,
            @Value("${auth.server.base.url}") final String authServerBaseUrl) {
        this.restTemplate = restTemplate;
        this.authServerBaseUrl = authServerBaseUrl;
    }

    public void authWithUsernamePassword(final String username, final String password) {
        restTemplate.postForEntity(
                authServerBaseUrl + "/user/auth",
                new HttpEntity<>(
                        new User(username, password, null)
                ),
                Void.class
        );
    }

    public boolean authWithOtp(final String username, final String code) {
        final ResponseEntity<Void> response = restTemplate.postForEntity(
                authServerBaseUrl + "/otp/check",
                new HttpEntity<>(
                        new User(username, null, code)
                ),
                Void.class
        );

        return HttpStatus.OK.equals(response.getStatusCode());
    }

}
