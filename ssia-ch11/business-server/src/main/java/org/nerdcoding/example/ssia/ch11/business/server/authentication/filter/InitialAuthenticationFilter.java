/*
 * InitialAuthenticationFilter.java
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

package org.nerdcoding.example.ssia.ch11.business.server.authentication.filter;


import org.nerdcoding.example.ssia.ch11.business.server.authentication.OtpAuthentication;
import org.nerdcoding.example.ssia.ch11.business.server.authentication.UsernamePasswordAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class InitialAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Value("${jwt.signing.key}")
    private String jwtSigningKey;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse,
            final FilterChain filterChain) throws ServletException, IOException {

        final String username = httpServletRequest.getHeader("username");
        final String password = httpServletRequest.getHeader("password");
        final String code = httpServletRequest.getHeader("code");

        if (code == null) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthentication(username, password)
            );
        } else {
            final Authentication authentication = authenticationManager.authenticate(
                    new OtpAuthentication(username, code)
            );

            httpServletResponse.setHeader("Authorization", createJwt(authentication));
        }
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/login");
    }

    private String createJwt(final Authentication authentication) {
        final SecretKey secretKey = Keys.hmacShaKeyFor(
                jwtSigningKey.getBytes(StandardCharsets.UTF_8)
        );

        return Jwts.builder()
                .setClaims(Map.of("username", authentication.getName()))
                .signWith(secretKey)
                .compact();
    }
}
