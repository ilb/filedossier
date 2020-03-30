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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author slavb
 */
public class MediaTypeAcceptor {

    /**
     * acceptable media types
     */
    private final List<MediaType> acceptableMediaTypes;

    /**
     * should accept default representation
     */
    private final boolean acceptDefaultRepresentation;

    /**
     * construct from "accept" header
     *
     * @param accept
     */
    public MediaTypeAcceptor(String accept) {
        this(Stream.of(accept.split(","))
                .map(mt -> MediaType.valueOf(mt))
                .collect(Collectors.toList()));
    }

    /**
     * construct from list of acceptable mime types
     *
     * @param acceptableMediaTypes
     */
    public MediaTypeAcceptor(List<MediaType> acceptableMediaTypes) {
        this.acceptableMediaTypes = acceptableMediaTypes;
        this.acceptDefaultRepresentation = acceptableMediaTypes
                .stream()
                .anyMatch(mt -> mt.toString().equals(MediaType.TEXT_HTML));
    }

    public Optional<MediaType> getCompatibleMediaType(MediaType other) {
        return acceptableMediaTypes.stream()
                .filter(mt -> mt.isCompatible(other))
                .sorted(MediaTypeQComparator.INSTANCE.reversed())
                .findFirst();
    }

    public boolean isAcceptDefaultRepresentation() {
        return acceptDefaultRepresentation;
    }
}
