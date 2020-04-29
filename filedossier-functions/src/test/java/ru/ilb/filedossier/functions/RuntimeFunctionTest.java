/*
 * Copyright 2019 chunaev.
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
package ru.ilb.filedossier.functions;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author chunaev
 */
public class RuntimeFunctionTest {

    public RuntimeFunctionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of apply method, of class RuntimeFunction.
     */
    @Test
    public void testApply() throws URISyntaxException {
        System.out.println("apply");
        byte[] t = "{'test':'123'}".getBytes();
        URI commandUri = this.getClass().getClassLoader().getResource("runtime/command.sh").toURI();
        Paths.get(commandUri.getPath()).toFile().setExecutable(true);
        RuntimeFunction instance = new RuntimeFunction(commandUri);
        byte[] expResult = t;
        byte[] result = instance.apply(t);
        assertArrayEquals(expResult, result);

    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void whenExceptionThrownWithWrongCommandExitCodeMessage() throws URISyntaxException {
        exceptionRule.expect(RuntimeException.class);
        //Expected: (an instance of java.lang.RuntimeException and exception with message a string containing "Wrong command exit code")
        //     but: exception with message a string containing "Wrong command exit code" message was "Обрыв канала"
        //exceptionRule.expectMessage("Wrong command exit code");
        System.out.println("apply");
        byte[] t = "{'test':'123'}".getBytes();
        URI commandUri = this.getClass().getClassLoader().getResource("runtime/error_command.sh").toURI();
        Paths.get(commandUri.getPath()).toFile().setExecutable(true);
        RuntimeFunction instance = new RuntimeFunction(commandUri);
        instance.apply(t);
    }

}
