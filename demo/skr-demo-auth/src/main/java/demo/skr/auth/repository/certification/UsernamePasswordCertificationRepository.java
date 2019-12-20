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
package demo.skr.auth.repository.certification;

import demo.skr.auth.model.Account;
import demo.skr.auth.model.certification.UsernamePasswordCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="https://github.com/hank-cp">Hank CP</a>
 */
@Repository
public interface UsernamePasswordCertificationRepository
        extends JpaRepository<UsernamePasswordCertification, Long> {

    UsernamePasswordCertification findByAccount(Account account);

    UsernamePasswordCertification findByUsername(String username);

}