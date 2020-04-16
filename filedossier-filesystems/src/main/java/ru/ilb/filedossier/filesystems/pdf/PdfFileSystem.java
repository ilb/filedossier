/*
 Copyright 2012-2013 University of Stavanger, Norway

 Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package ru.ilb.filedossier.filesystems.pdf;

import java.io.IOException;
import java.net.URI;
import java.net.ProxySelector;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Objects;
import java.util.Set;

/**
 * Pdf implementation of a FileSystem.
 */
public class PdfFileSystem extends FileSystem {

    private final FileSystemProvider provider;
    private final URI uri;
    private Path contents;

    /**
     *
     * @param provider an instance of a PdfFileSystemProvided. This can be a shared instance.
     * @param uri URI for the Pdf server, the scheme is ignored.
     */
    public PdfFileSystem(PdfFileSystemProvider provider, URI uri) {
        this.provider = provider;
        this.uri = uri;
    }

    @Override
    public FileSystemProvider provider() {
        return provider;
    }

    /**
     * Not implemented
     */
    @Override
    public void close() throws IOException {
    }

    /**
     * Not implemented
     *
     * @return null
     */
    @Override
    public Iterable<FileStore> getFileStores() {
        return null;
    }

    @Override
    public Path getPath(String first, String... more) {
        String path;
        if (more.length == 0) {
            path = first;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(first);
            for (String segment : more) {
                if (segment.length() > 0) {
                    if (sb.length() > 0) {
                        sb.append(getSeparator());
                    }
                    sb.append(segment);
                }
            }
            path = sb.toString();
        }
        return new PdfPath(this, path);
    }

    /**
     * Not implemented
     *
     * @return null
     */
    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern) {
        return null;
    }

    /**
     * Not implemented
     *
     * @return null
     */
    @Override
    public Iterable<Path> getRootDirectories() {
        return null;
    }

    @Override
    public String getSeparator() {
        return "/";
    }

    /**
     * Not implemented
     *
     * @return null
     */
    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        return null;
    }

    /**
     * Not implemented
     *
     * @return false
     */
    @Override
    public boolean isOpen() {
        return false;
    }

    /**
     * Not implemented
     *
     * @return false
     */
    @Override
    public boolean isReadOnly() {
        return false;
    }

    /**
     * Not implemented
     *
     * @return null
     */
    @Override
    public WatchService newWatchService() throws IOException {
        return null;
    }

    /**
     * Not implemented
     *
     * @return null
     */
    @Override
    public Set<String> supportedFileAttributeViews() {
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.uri);
        return hash;
    }

    /**
     * Check if one filesystem is equal to another.Checks uri
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PdfFileSystem other = (PdfFileSystem) obj;
        if (!Objects.equals(this.uri, other.uri)) {
            return false;
        }
        return true;
    }

    public URI getUri() {
        return uri;
    }

    public Path getContents() throws IOException {
        if (this.contents == null) {
            this.contents = Files.createTempDirectory("pdffilesystem");
            this.contents .toFile().deleteOnExit();

            PdfExtractor pdfExtractor = PdfExtractor.INSTANCE;
            URI pdfUri = URI.create(uri.toString().substring(6));
            pdfExtractor.extract(pdfUri, contents, "page");

            //URI folderUri = URI.create("file:" + tmpFolder.toUri());
            //  URI folderUri = tmpFolder.toUri();
            //URI folderUri = Paths.get("/").toUri();
            //this.delagete = FileSystems.getFileSystem(folderUri);

            //this.delagete = FileSystems.newFileSystem(folderUri, null);
            //this.contents = FileSystems.newFileSystem(tmpFolder, null);
        }
        return contents;
    }

}
