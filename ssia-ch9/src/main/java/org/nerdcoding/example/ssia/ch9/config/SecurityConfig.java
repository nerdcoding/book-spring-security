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

package org.nerdcoding.example.ssia.ch9.config;

import org.nerdcoding.example.ssia.ch9.config.filter.AuthenticationLoggingFilter;
import org.nerdcoding.example.ssia.ch9.config.filter.RequestValidationFilter;
import org.nerdcoding.example.ssia.ch9.config.filter.StaticKeyAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final StaticKeyAuthenticationFilter staticKeyAuthenticationFilter;

    @Autowired
    public SecurityConfig(final StaticKeyAuthenticationFilter staticKeyAuthenticationFilter) {
        this.staticKeyAuthenticationFilter = staticKeyAuthenticationFilter;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.addFilterBefore(
                new RequestValidationFilter(),
                BasicAuthenticationFilter.class
        ).addFilterAfter(
                new AuthenticationLoggingFilter(),
                BasicAuthenticationFilter.class
        ).addFilterAt(
                staticKeyAuthenticationFilter,
                BasicAuthenticationFilter.class
        ).authorizeRequests().anyRequest().permitAll();
    }

}
