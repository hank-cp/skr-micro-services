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
package demo.skr.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skr.common.util.JwtUtil;
import org.skr.security.SkrSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.concurrent.Callable;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author <a href="https://github.com/hank-cp">Hank CP</a>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthApp.class)
@AutoConfigureMockMvc
@Rollback
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SkrSecurityProperties skrSecurityProperties;

    @Test
    public void testLogin() throws Exception {
        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("auth_tenentCode", "org1"),
                        new BasicNameValuePair("username", "dev"),
                        new BasicNameValuePair("password", "dev")
                )))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("access-token", notNullValue()))
                .andExpect(jsonPath("refresh-token", notNullValue()))
                .andExpect(jsonPath("loginToken", notNullValue()))
                .andExpect(jsonPath("principal", notNullValue()));
    }

    @Test
    public void testLoginByToken() throws Exception {
        mvc.perform(post("/auth/login-by-token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("auth_tenentCode", "org1"),
                        new BasicNameValuePair("loginToken", JwtUtil.encode("dev",
                                skrSecurityProperties.getLoginToken().getExpiration(),
                                skrSecurityProperties.getLoginToken().getSecret()))
                )))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("access-token", notNullValue()))
                .andExpect(jsonPath("refresh-token", notNullValue()))
                .andExpect(jsonPath("principal", notNullValue()));
    }

    @Test
    public void testLoginFailed() throws Exception {
        mvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                            new BasicNameValuePair("auth_tenentCode", "org1"),
                            new BasicNameValuePair("username", "dev"),
                            new BasicNameValuePair("password", "123")
                    )))))
                    .andExpect(status().isUnauthorized());
    }

    @Test
    public void testRefreshToken() throws Exception {
        JsonNode response = objectMapper.readTree(mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("auth_tenentCode", "org1"),
                        new BasicNameValuePair("username", "dev"),
                        new BasicNameValuePair("password", "dev")
                ))))).andReturn().getResponse().getContentAsByteArray());

        mvc.perform(post("/auth/refresh-token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("auth_tenentCode", "org1"),
                        new BasicNameValuePair("refreshToken", response.get("refresh-token").asText())
                )))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("access-token", notNullValue()));
    }

    public static Throwable exceptionOf(Callable<?> callable) {
        try {
            callable.call();
            return null;
        } catch (Throwable t) {
            return t;
        }
    }
}