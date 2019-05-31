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
package ru.ilb.filedossier;

import java.net.URISyntaxException;
import java.util.Arrays;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxrs.provider.XSLTJaxbProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import ru.ilb.common.jaxrs.jaxb.JaxbContextResolver;
import ru.ilb.common.jaxrs.xml.transform.ServletContextURIResolver;
import ru.ilb.filedossier.context.DossierContext;
import ru.ilb.filedossier.context.DossierContextBuilder;
import ru.ilb.filedossier.lib.DossierFactory;
import ru.ilb.filedossier.model.DossierModelRepository;
import ru.ilb.filedossier.model.FileDossierModelRepository;
import ru.ilb.filedossier.scripting.SubstitutorTemplateEvaluator;
import ru.ilb.filedossier.scripting.TemplateEvaluator;
import ru.ilb.filedossier.store.StoreFactory;

/**
 *
 * @author slavb
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "ru.ilb.filedossier.components",
    "ru.ilb.filedossier.mappers"})
public class Application { // extends JpaBaseConfiguration

    @Autowired
    private Bus bus;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public Server rsServer() {
//        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
//        endpoint.setBus(bus);
//
//        //endpoint.setServiceBeans(Arrays.<Object>asList(new HelloServiceImpl1(), new HelloServiceImpl2()));
//        endpoint.setAddress("/");
//        endpoint.setFeatures(Arrays.asList(new LoggingFeature()));
//        return endpoint.create();
//    }
    @Bean
    public ru.ilb.common.jaxrs.json.MOXyJsonProvider jsonProvider() {
        // lacks @Provider annotation
        //return new org.eclipse.persistence.jaxb.rs.MOXyJsonProvider();
        return new ru.ilb.common.jaxrs.json.MOXyJsonProvider();
    }

    @Bean
    public JaxbContextResolver jaxbContextResolver() {
        return new JaxbContextResolver();
    }

    @Bean
    public DossierFactory dossierFactory() {
        DossierModelRepository dossierModelRepository;
        StoreFactory storeFactory;
        try {
            dossierModelRepository = new FileDossierModelRepository(getClass().getClassLoader().getResource("models").toURI());
            storeFactory = StoreFactory.newInstance(getClass().getClassLoader().getResource("teststoreroot").toURI());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

        DossierContextBuilder dossierContextBuilder = (String dossierKey, String dossierCode) -> {
            DossierContext dc = new DossierContext();
            dc.setProperty("name", "Тест имя");
            dc.setProperty("prop", false);
            return dc;
        };
        TemplateEvaluator templateEvaluator = new SubstitutorTemplateEvaluator();
        return new DossierFactory(dossierModelRepository, storeFactory, dossierContextBuilder, templateEvaluator);

    }


    @Bean
    @ConditionalOnExpression("'${ILB_SYSID}'=='DEVEL'")
    public XSLTRequestFilter xsltRequestFilter() {
        // REFRESH TEMPLATES
        return new XSLTRequestFilter();
    }
    
    @Bean
    public XSLTJaxbProvider xsltJaxbProvider() {
        XSLTJaxbProvider xsltJaxbProvider = new XSLTJaxbProvider();
        xsltJaxbProvider.setResolver(new ServletContextURIResolver());
        xsltJaxbProvider.setRefreshTemplates(true);
        xsltJaxbProvider.setProduceMediaTypes(Arrays.asList("application/xhtml+xml,text/csv,application/pdf"));
        xsltJaxbProvider.setOutTemplate("classpath:/stylesheets/filedossier/dossier.xsl");
        xsltJaxbProvider.setRefreshTemplates(true);
        return xsltJaxbProvider;
    }

}
