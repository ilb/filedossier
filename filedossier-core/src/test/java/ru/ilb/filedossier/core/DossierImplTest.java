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
package ru.ilb.filedossier.core;

import ru.ilb.filedossier.core.DossierFactory;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.Dossier;
import java.util.List;
import javax.naming.Context;
import javax.naming.NamingException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.ClassRule;
import static ru.ilb.filedossier.core.DossierFactoryTest.getDossierFactory;
import ru.ilb.filedossier.jndi.JndiRule;

/**
 *
 * @author slavb
 */
public class DossierImplTest {

    private final DossierFactory dossierFactory;

    private final Dossier dossier;

    @ClassRule
    public static JndiRule jndi = new JndiRule() {
        @Override
        protected void bind(Context context) throws NamingException {
            context.bind("ru.bystrobank.apps.meta.url", "https://devel.net.ilb.ru/meta");
        }

    };

    public DossierImplTest() throws NamingException {
        dossierFactory = getDossierFactory();
        dossier = dossierFactory.getDossier("teststorekey", "testmodel", "TEST", "mode1");
    }

    /**
     * Test of getCode method, of class DossierImpl.
     */
    @Test
    public void testGetCode() {
        System.out.println("getCode");
        String expResult = "TEST";
        String result = dossier.getCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class DossierImpl.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        String expResult = "Тестовое досье";
        String result = dossier.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDossierFiles method, of class DossierImpl.
     */
    @Test
    public void testGetDossierFiles() {
        System.out.println("getDossierFiles");
        List<DossierFile> result = dossier.getDossierFiles();
        assertEquals(2, result.size());
    }

    /**
     * Test of getDossierFile method, of class DossierImpl.
     */
    @Test
    public void testGetDossierFile() {
        System.out.println("getDossierFile");
        String fileCode = "fairpricecalc";
        DossierFile result = dossier.getDossierFile(fileCode);
        assertEquals("Тест имя", result.getName());
    }

}
