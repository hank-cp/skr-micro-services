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
package demo.skr.aio.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.skr.aio.AioApp;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author <a href="https://github.com/hank-cp">Hank CP</a>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AioApp.class)
@AutoConfigureMockMvc
@Transactional
@Rollback
public class PermissionCheckTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testPermissionGranted() throws Exception {
        JsonNode response = objectMapper.readTree(mvc.perform(post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("username", "dev"),
                        new BasicNameValuePair("password", "dev"),
                        new BasicNameValuePair("tenentCode", "org")
                ))))).andReturn().getResponse().getContentAsByteArray());
        String accessToken = response.get("access-token").asText();
        assertThat(accessToken, allOf(notNullValue(), not(emptyString())));

        mvc.perform(get("/task/list")
                .header("access-token", accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        mvc.perform(get("/task/secret-list")
            .header("access-token", accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testPermissionDenied() throws Exception {
        JsonNode response = objectMapper.readTree(mvc.perform(post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("username", "test"),
                        new BasicNameValuePair("password", "test"),
                        new BasicNameValuePair("tenentCode", "org")
                ))))).andReturn().getResponse().getContentAsByteArray());
        String accessToken = response.get("access-token").asText();
        assertThat(accessToken, allOf(notNullValue(), not(emptyString())));

        mvc.perform(get("/task/list")
                .header("access-token", accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());

        mvc.perform(get("/task/secret-list")
            .header("access-token", accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isForbidden());
    }
}