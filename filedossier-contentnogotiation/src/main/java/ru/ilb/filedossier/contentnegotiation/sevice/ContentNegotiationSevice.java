/*
 * Copyright 2020 slavb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.ilb.filedossier.contentnegotiation.sevice;

import java.util.List;
import java.util.Optional;

/**
 * Service to negotiate content based on accept header
 *
 * @author slavb
 */
public interface ContentNegotiationSevice {

    // TODO: fix all usages of MediaType.toString(), as result can be ambiguous
    /**
     * get acceptable media type based on accept header
     *
     * @param acceptableMediaTypes client's accept header value, e.g. text/html,application/xhtml+xml,application/xml;q=0.9
     * @param allowedMediaTypes list of allowed media types to serve
     * @return
     */
    Optional<String> getAcceptableMediaType(String acceptableMediaTypes, List<String> allowedMediaTypes);

}
