/*
 * Copyright (C) 2019-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.skr.security;

/**
 * Certification to identify a user. e.g.
 * <ul>
 *     <li>username + password</li>
 *     <li>phone + sms captcha</li>
 *     <li>email + captcha</li>
 *     <li>OAuth</li>
 * </ul>
 *
 * @author <a href="https://github.com/hank-cp">Hank CP</a>
 */
public interface Certification {

    /** identity for this Certification, e.g. username, phone number, email address, etc. */
    String getIdentity();

    /** identity for the user principle that this certification is binding to, e.g. account.id / user.id */
    String getUserPrincipalIdentity();

}
