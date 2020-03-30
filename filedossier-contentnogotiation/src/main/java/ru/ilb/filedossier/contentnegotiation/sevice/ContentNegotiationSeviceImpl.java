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
import java.util.stream.Collectors;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;
import ru.ilb.filedossier.contentnegotiation.domain.MediaTypeAcceptor;
import ru.ilb.filedossier.contentnegotiation.domain.MediaTypeResolver;

@Named
public class ContentNegotiationSeviceImpl implements ContentNegotiationSevice {

    @Override
    public Optional<String> getAcceptableMediaType(String acceptableMediaTypes, List<String> allowedMediaTypes) {
        MediaTypeAcceptor mta = new MediaTypeAcceptor(acceptableMediaTypes);
        MediaTypeResolver mtr = new MediaTypeResolver(mta);
        List<MediaType> amt = allowedMediaTypes.stream().map(mt -> MediaType.valueOf(mt)).collect(Collectors.toList());
        return mtr.getAcceptableMediaType(amt).map(mt -> mt.toString());
    }

}
