/*
 * HelloController.java
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

package org.nerdcoding.example.ssia.ch11.auth.server.controller;

import org.nerdcoding.example.ssia.ch11.auth.server.peristence.model.Otp;
import org.nerdcoding.example.ssia.ch11.auth.server.peristence.model.User;
import org.nerdcoding.example.ssia.ch11.auth.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/add")
    public HttpEntity<Void> addUser(@RequestBody final User user) {
        userService.addUser(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/user/auth")
    public HttpEntity<Void> auth(@RequestBody final User user) {
        userService.auth(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/otp/check")
    public HttpEntity<Void> check(@RequestBody final Otp otp) {
        return userService.checkOtp(otp)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
