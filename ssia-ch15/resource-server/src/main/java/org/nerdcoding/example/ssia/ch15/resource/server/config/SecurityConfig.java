/*
 * SecurityConfig.java
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

package org.nerdcoding.example.ssia.ch15.resource.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;

//@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String jwtKey;

    public SecurityConfig(@Value("${jwt.key}") final String jwtKey) {
        this.jwtKey = jwtKey;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer(
                        configurer -> configurer.jwt(
                                jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                        )
                );
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        final byte [] key = jwtKey.getBytes();
        return NimbusJwtDecoder
                .withSecretKey(
                        new SecretKeySpec(key, 0, key.length, "AES")
                )
                .build();
    }

}
