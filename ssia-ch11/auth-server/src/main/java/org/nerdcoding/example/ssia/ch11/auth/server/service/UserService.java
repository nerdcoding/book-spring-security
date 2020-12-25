/*
 * UserService.java
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

package org.nerdcoding.example.ssia.ch11.auth.server.service;

import org.nerdcoding.example.ssia.ch11.auth.server.peristence.model.Otp;
import org.nerdcoding.example.ssia.ch11.auth.server.peristence.model.User;
import org.nerdcoding.example.ssia.ch11.auth.server.peristence.repository.OtpRepository;
import org.nerdcoding.example.ssia.ch11.auth.server.peristence.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final GenerateCodeService generateCodeService;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;

    public UserService(
            final PasswordEncoder passwordEncoder,
            final GenerateCodeService generateCodeService,
            final UserRepository userRepository,
            final OtpRepository otpRepository) {

        this.passwordEncoder = passwordEncoder;
        this.generateCodeService = generateCodeService;
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
    }

    public boolean checkOtp(final Otp otpToCheck) {
        return otpRepository.findOtpByUsername(otpToCheck.getUsername())
                .map(existingOtp -> existingOtp.getCode().equals(otpToCheck.getCode()))
                .orElse(false);
    }

    public void addUser(final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void auth(final User user) {
        userRepository.findUserByUsername(user.getUsername())
                .ifPresentOrElse(
                        existingUser -> {
                            if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                                renewOtp(existingUser);
                            }  else {
                                throw new BadCredentialsException("Bad credentials");
                            }
                        },
                        () -> {
                            throw new BadCredentialsException("Bad credentials");
                        }
                );

    }

    private void renewOtp(final User user) {
        final String code = generateCodeService.generateCode();

        otpRepository.findOtpByUsername(user.getUsername())
                .ifPresentOrElse(
                        existingOtp -> existingOtp.setCode(code),
                        () -> {
                            final Otp otp = new Otp();
                            otp.setUsername(user.getUsername());
                            otp.setCode(code);

                            otpRepository.save(otp);
                        }
                );
        System.out.println("NEW OTP " + code);
    }
}
