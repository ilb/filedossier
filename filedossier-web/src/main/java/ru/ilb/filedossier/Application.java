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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.provider.XSLTJaxbProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.util.Assert;
import ru.ilb.common.jaxrs.jaxb.JaxbContextResolver;
import ru.ilb.common.jaxrs.xml.transform.ServletContextURIResolver;
import ru.ilb.filedossier.entities.DossierContext;
import ru.ilb.filedossier.context.DossierContextBuilder;
import ru.ilb.filedossier.context.DossierContextImpl;
import ru.ilb.filedossier.core.DossierFactory;
import ru.ilb.filedossier.ddl.DossierDefinitionRepository;
import ru.ilb.filedossier.ddl.FileDossierDefinitionRepository;
import ru.ilb.filedossier.scripting.SubstitutorTemplateEvaluator;
import ru.ilb.filedossier.scripting.TemplateEvaluator;
import ru.ilb.filedossier.store.StoreFactory;

/**
 *
 * @author slavb
 */
@SpringBootApplication
@ComponentScan(basePackages = { "ru.ilb.filedossier.components", "ru.ilb.filedossier.mappers" })
public class Application { // extends JpaBaseConfiguration

    public static void main(String[] args) {
	SpringApplication.run(Application.class, args);
    }

    @Bean
    public ru.ilb.common.jaxrs.json.MOXyJsonProvider jsonProvider() {
	// lacks @Provider annotation
	// return new org.eclipse.persistence.jaxb.rs.MOXyJsonProvider();
	return new ru.ilb.common.jaxrs.json.MOXyJsonProvider();
    }

    @Bean
    public JaxbContextResolver jaxbContextResolver() {
	return new JaxbContextResolver();
    }

    @Bean
    public DossierFactory dossierFactory() {
	DossierDefinitionRepository dossierModelRepository;
	StoreFactory storeFactory;
	try {
	    dossierModelRepository = new FileDossierDefinitionRepository(
		    getClass().getClassLoader().getResource("models").toURI());
	    storeFactory = StoreFactory.newInstance(getClass().getClassLoader().getResource("teststoreroot").toURI());
	} catch (URISyntaxException ex) {
	    throw new RuntimeException(ex);
	}

	DossierContextBuilder dossierContextBuilder = (String dossierKey, String dossierPackage,
		String dossierCode) -> {
	    DossierContext dc = new DossierContextImpl("contextKey");
	    dc.setProperty("name", "Тест имя");
	    dc.setProperty("prop", false);
	    return dc;
	};
	TemplateEvaluator templateEvaluator;
	try {
	    templateEvaluator = new SubstitutorTemplateEvaluator(new InitialContext());
	} catch (NamingException ex) {
	    throw new RuntimeException(ex);
	}
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

    @Bean
    public LoggingFeature loggingFeature() {
	LoggingFeature lf = new LoggingFeature();
	lf.addBinaryContentMediaTypes("application/vnd.oasis.opendocument.spreadsheet");
	;
	return lf;
    }

    /**
     * JPA-like NamingStrategy
     * 
     * @return
     */
    @Bean
    public NamingStrategy namingStrategy() {
	return new NamingStrategy() {
	    @Override
	    public String getReverseColumnName(RelationalPersistentProperty property) {
		return property.getOwner().getTableName() + "_ID";
	    }

	    @Override
	    public String getColumnName(RelationalPersistentProperty property) {
		Assert.notNull(property, "Property must not be null.");
		return property.getName().toUpperCase();
	    }

	    @Override
	    public String getTableName(Class<?> type) {
		Assert.notNull(type, "Type must not be null.");
		return type.getSimpleName().toUpperCase();
	    }

	};
    }
}
