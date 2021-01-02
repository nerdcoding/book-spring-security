/*
 * AuthServerConfig.java
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

package org.nerdcoding.example.ssia.ch15.authorization.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServerConfig(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager);
    }

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        /*final BaseClientDetails baseClientDetails = new BaseClientDetails();
        baseClientDetails.setClientId("client");
        baseClientDetails.setClientSecret("secret");
        baseClientDetails.setScope(List.of("read"));
        baseClientDetails.setAuthorizedGrantTypes(List.of("password"));

        final InMemoryClientDetailsService inMemoryClientDetailsService = new InMemoryClientDetailsService();
        inMemoryClientDetailsService.setClientDetailsStore(
                Map.of("client", baseClientDetails)
        );

        clients.withClientDetails(inMemoryClientDetailsService);*/

        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .scopes("read")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .redirectUris("http://127.0.0.1:9090/home")
                .autoApprove(true)
            .and()
                .withClient("resourceserver")
                .secret("resourceserversecret");
    }
}

