/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.wisc.cae.springbug;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Steve Riesenberg
 */
@Controller
public class PageController {
    // Note: This is not exposed as a bean, as doing so would require us
    // to re-define the requestMatcher to exclude XHR requests
    private final RequestCache requestCache = new HttpSessionRequestCache();

    @GetMapping("/demo/index.html")
    public String information(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication == null) {
            System.out.printf("Saving request: %s %s%n", request.getMethod(), request.getRequestURI());
            requestCache.saveRequest(request, response);
        }

        return "information";
    }
}
