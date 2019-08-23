/*
 * Copyright 2019 slavb.
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
package ru.ilb.filedossier.store;

import ru.ilb.filedossier.entities.Store;
import java.net.URI;

/**
 * Файловое хранилище
 *
 * @author slavb
 */
public class StoreFactory {

    private final URI storeRoot;

    private StoreFactory(URI storeRoot) {
        this.storeRoot = storeRoot;
    }

    public static StoreFactory newInstance(URI storeRoot) {
        return new StoreFactory(storeRoot);
    }

    /**
     * Returns store with basic path - storageRoot/storeKey/
     */
    public Store getStore(String storeKey) {
        return new FileStore(storeRoot, storeKey);
    }

}
