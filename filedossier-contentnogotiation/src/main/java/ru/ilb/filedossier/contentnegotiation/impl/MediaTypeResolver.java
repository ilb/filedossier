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
package ru.ilb.filedossier.contentnegotiation.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author slavb
 */
public class MediaTypeResolver {

    /**
     * acceptable media types
     */
    private final MediaTypeAcceptor mediaTypeAcceptor;


    public MediaTypeResolver(MediaTypeAcceptor mediaTypeAcceptor) {
        this.mediaTypeAcceptor = mediaTypeAcceptor;
    }

    /**
     * get acceptable media type. assumes first one is default
     *
     * @param allowedMediaTypes
     * @return
     */
    public MediaType getAcceptableMediaType(List<MediaType> allowedMediaTypes) {
        if (mediaTypeAcceptor.isAcceptDefaultRepresentation()) {
            return allowedMediaTypes.get(0);
        }

        //allowedMediaTypes.stream().map(arg0)
        return null;
    }

}
