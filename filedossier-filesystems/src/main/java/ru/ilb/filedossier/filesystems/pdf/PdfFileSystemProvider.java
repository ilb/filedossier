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
import java.net.URISyntaxException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The Pdf FileSystemProvider based on Sardine.
 */
public class PdfFileSystemProvider extends FileSystemProvider {

    private static final int DEFAULT_PORT = 80;
    private final Map<URI, PdfFileSystem> hosts = new HashMap<>();

    @Override
    public void copy(Path fileFrom, Path fileTo, CopyOption... options) throws IOException {

        throw new UnsupportedOperationException();
    }

    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {

        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Path dir) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * The default implementation in FileSystemProvider will simply call
     * delete() in deleteIfExists() and silently ignore any NoSuchFileException.
     * In case of Nexus, trying to delete() will result in 503 (Not allowed)
     * even if the path points to nowhere.
     */
    @Override
    public boolean deleteIfExists(Path path) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileSystem getFileSystem(URI uri) {
        try {
            return getPdfFs(uri, true);
        } catch (URISyntaxException ex) {
            throw new FileSystemNotFoundException(uri.toString());
        }
    }

    @Override
    public Path getPath(URI uri) {
        try {
            PdfFileSystem host = getPdfFs(uri, true);
            return new PdfPath(host, uri.getPath());
        } catch (URISyntaxException e) {
            throw new FileSystemNotFoundException(uri.toString());
        }
    }

    private PdfFileSystem getPdfFs(URI uri, boolean create) throws URISyntaxException {

        synchronized (hosts) {
            PdfFileSystem fs = hosts.get(uri);
            if (fs == null && create) {
                fs = new PdfFileSystem(this, uri);
                hosts.put(uri, fs);
            }
            return fs;
        }
    }

    @Override
    public String getScheme() {
        return "pdffs";
    }

    /**
     * Unsupported
     */
    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported
     */
    @Override
    public FileStore getFileStore(Path path) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        PdfFileSystem pfs = (PdfFileSystem) path.getFileSystem();
        Path contentsPath = pfs.getContents().resolve(path.toString().substring(1));
        contentsPath.getFileSystem().provider().checkAccess(contentsPath, modes);

    }

    /**
     * Unsupported
     */
    @Override
    public boolean isHidden(Path path) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported
     */
    @Override
    public boolean isSameFile(Path path, Path path2) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported
     */
    @Override
    public void move(Path source, Path target, CopyOption... options) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs)
            throws IOException {
        PdfFileSystem pfs = (PdfFileSystem) path.getFileSystem();
        Path contentsPath = pfs.getContents().resolve(path.toString().substring(1));
        return Files.newByteChannel(contentsPath, StandardOpenOption.READ);
    }

    /**
     * Unsupported
     */
    @Override
    public DirectoryStream<Path> newDirectoryStream(Path arg0, Filter<? super Path> arg1) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
        try {
            return getPdfFs(uri, true);
        } catch (URISyntaxException e) {
            throw new FileSystemException(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
        PdfFileSystem pfs = (PdfFileSystem) path.getFileSystem();
        Path contentsPath = pfs.getContents().resolve(path.toString().substring(1));
        return contentsPath.getFileSystem().provider().readAttributes(contentsPath, type, options);
    }

    /**
     * Unsupported
     */
    @Override
    public Map<String, Object> readAttributes(Path arg0, String arg1, LinkOption... arg2) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported
     */
    @Override
    public void setAttribute(Path arg0, String arg1, Object arg2, LinkOption... arg3) throws IOException {
        throw new UnsupportedOperationException();
    }
}
