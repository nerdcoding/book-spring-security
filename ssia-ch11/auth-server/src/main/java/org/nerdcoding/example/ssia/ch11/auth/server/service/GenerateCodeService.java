/*
 * GenerateCodeService.java
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

import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class GenerateCodeService {

    public String generateCode() {
        try {
            final SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            int c = secureRandom.nextInt(9000) + 1000;

            return String.valueOf(c);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem when generating the random OTP code");
        }
    }

}
