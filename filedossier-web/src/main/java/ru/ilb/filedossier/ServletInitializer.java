package ru.ilb.filedossier;

import java.nio.file.spi.FileSystemProvider;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        FileSystemProviderInitializer.customLoadInstalledProviders();
        FileSystemProvider.installedProviders();
        return application.sources(Application.class);
    }

}
