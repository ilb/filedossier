/*
 * Copyright 2019 kuznetsov_me.
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
package ru.ilb.filedossier.context.persistence;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.ilb.filedossier.context.DossierContextImpl;
import ru.ilb.filedossier.context.persistence.repositories.DossierContextRepository;
import ru.ilb.filedossier.entities.DossierContext;

/**
 *
 * @author kuznetsov_me
 */
    
@RunWith(SpringRunner.class)
@Commit // https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/testing.html#testcontext-tx-rollback-and-commit-behavior
@ContextConfiguration(classes = Application.class)
@Transactional
@SpringBootTest
public class DBDossierContextServiceTest {

    @Autowired
    DossierContextRepository dossierContextRepository;

    @Test
    public void testPutDossierContextWithData() {
        
       DBDossierContextService contextService = new DBDossierContextService(dossierContextRepository);
       
       // Create test dossier context
       DossierContext context = new DossierContextImpl();
       context.setProperty("contextKey", "testContextKey");
       context.setProperty("testDataKey", "testDataValue");
       context.setProperty("testDataKey1", "testDataValue1");
       
       contextService.putContext(context);
       DossierContext result = contextService.getContext("testContextKey");
       
       assertEquals("testContextKey", result.getProperty("contextKey"));
       assertEquals("testDataValue", result.getProperty("testDataKey"));
    }
    
    @Test
    public void testGetDossierContextWithData() {
        
       DBDossierContextService contextService = new DBDossierContextService(dossierContextRepository);
       
       DossierContext result = contextService.getContext("testContextKey");
       assertEquals("testContextKey", result.getProperty("contextKey"));
       assertEquals("testDataValue", result.getProperty("testDataKey"));
    }
    
}
