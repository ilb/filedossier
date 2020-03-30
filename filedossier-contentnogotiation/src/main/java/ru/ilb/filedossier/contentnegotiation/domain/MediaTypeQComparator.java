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

import java.util.Comparator;
import java.util.Optional;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author slavb
 */
public class MediaTypeQComparator implements Comparator<MediaType> {

    public static final Comparator<MediaType> INSTANCE = new MediaTypeQComparator();

    private static Double getQ(MediaType mt) {
        return Optional.ofNullable(mt.getParameters().get("q")).map(s -> Double.parseDouble(s)).orElse(1.0);
    }

    @Override
    public int compare(MediaType arg0, MediaType arg1) {
        return Double.compare(getQ(arg0), getQ(arg1));
    }

}
