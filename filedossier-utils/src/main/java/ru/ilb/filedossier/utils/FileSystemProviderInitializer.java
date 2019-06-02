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
package ru.ilb.filedossier.utils;

import java.lang.reflect.Field;
import java.nio.file.FileSystems;
import java.nio.file.spi.FileSystemProvider;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

/**
 *
 * @author slavb
 */
class FileSystemProviderInitializer {

    /*
* This is a very dirty hack to get around the fact that FileSystemProvider uses
* ClassLoader.getSystemClassLoader() to discover FileSystemProvider in the classpath.
* Tomcat hijacks the SystemClassLoader causing SPI not to function in tomcat.
     */
    @SuppressWarnings("unused")
    public static void customLoadInstalledProviders() {
        try {
            Field loadingProvidersField = FileSystemProvider.class.getDeclaredField("loadingProviders");
            Field installedProvidersField = FileSystemProvider.class.getDeclaredField("installedProviders");
            loadingProvidersField.setAccessible(true);
            installedProvidersField.setAccessible(true);

            loadingProvidersField.set(null, true);

            List<FileSystemProvider> installedProviders = AccessController
                    .doPrivileged(new PrivilegedAction<List<FileSystemProvider>>() {
                        @Override
                        public List<FileSystemProvider> run() {
                            return threadClassLoaderInstalledProviders();
                        }
                    });

            installedProvidersField.set(null, installedProviders);  // overwrite the current list of installed file system providers

            loadingProvidersField.set(null, false);

            loadingProvidersField.setAccessible(false);
            installedProvidersField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("The hack didn't work.", e);
        }
    }

    /*
    Load FileSystem Providers from Thread context class loader and from system class loader
     */
    private static List<FileSystemProvider> threadClassLoaderInstalledProviders() {

        List<FileSystemProvider> list = new ArrayList<>();

        FileSystemProvider defaultProvider = FileSystems.getDefault().provider();

        ServiceLoader<FileSystemProvider> sl = ServiceLoader.load(FileSystemProvider.class, ClassLoader.getSystemClassLoader());
        for (FileSystemProvider provider : sl) {
            addProviderToList(list, provider);
        }

        ServiceLoader<FileSystemProvider> tsl = ServiceLoader.load(FileSystemProvider.class, Thread.currentThread().getContextClassLoader());
        for (FileSystemProvider provider : tsl) {
            addProviderToList(list, provider);
        }

        list.add(0, defaultProvider);

        return Collections.unmodifiableList(list);
    }

    private static void addProviderToList(List<FileSystemProvider> list, FileSystemProvider provider) {

        String scheme = provider.getScheme();

        // add to list if the provider is not "file" and isn't a duplicate
        if (!scheme.equalsIgnoreCase("file")) {
            boolean found = false;
            for (FileSystemProvider p : list) {
                if (p.getScheme().equalsIgnoreCase(scheme)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                list.add(provider);
            }
        }

    }

}
