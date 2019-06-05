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
package ru.ilb.filedossier.repositories;

import javax.transaction.Transactional;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ilb.filedossier.Application;
import ru.ilb.filedossier.model.DossierContext;

/**
 *
 * @author slavb
 */
@RunWith(SpringRunner.class)
@Transactional
@Commit // https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/testing.html#testcontext-tx-rollback-and-commit-behavior
@ContextConfiguration(classes = Application.class)
@SpringBootTest
public class DossierContextRepositoryTest {

    @Autowired
    DossierContextRepository repository;

    public DossierContextRepositoryTest() {
    }

    @Test
    public void testDossierContextWithDataSaved() {
        DossierContext dossierContext = new DossierContext();
        dossierContext.setDossierKey("testDossierKey");
        dossierContext.addDossierContextData("testDataKey", "testValue");
        dossierContext.addDossierContextData("testDataKey1", "testValue1");
        
        dossierContext = repository.save(dossierContext);
        assertEquals(1, repository.countItemsInDossierContext());
        assertEquals(2, repository.countItemsInDossierContextData());
    }

}
