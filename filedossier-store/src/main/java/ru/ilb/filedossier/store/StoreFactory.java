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

    private final URI storageRoot;

    private StoreFactory(URI storageRoot) {
	this.storageRoot = storageRoot;
    }

    public static StoreFactory newInstance(URI storageRoot) {
	return new StoreFactory(storageRoot);
    }

    /**
     * Returns store with basic path - storageRoot/storeKey/
     */
    public Store getFileStorage(String storeKey) {
	return new FileStore(storageRoot, storeKey);
    }

    /**
     * Returns store with nested path - storageRoot/storeKey1.../storeKeyN/
     */
    public Store getNestedFileStorage(String... storeKeys) {
	return new FileStore(storageRoot, storeKeys);
    }

}
