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
package ru.ilb.filedossier.contentnegotiation.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
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
    public Optional<MediaType> getAcceptableMediaType(List<MediaType> allowedMediaTypes) {
        if (mediaTypeAcceptor.isAcceptDefaultRepresentation()) {
            return Optional.ofNullable(allowedMediaTypes.get(0));
        }

        return allowedMediaTypes.stream().flatMap(mt -> streamopt(mediaTypeAcceptor.getCompatibleMediaType(mt)))
                .sorted(MediaTypeQComparator.INSTANCE.reversed())
                .findFirst();
    }

    /**
     * convert Optional to Stream
     *
     * @see https://stackoverflow.com/questions/22725537/using-java-8s-optional-with-streamflatmap
     * @param <T>
     * @param opt
     * @return
     */
    private static <T> Stream<T> streamopt(Optional<T> opt) {
        return opt.map(Stream::of).orElse(Stream.empty());
    }
}
