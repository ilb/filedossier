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
package ru.ilb.filedossier.functions;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author kuznetsov_me
 */
public class WebResourceFunctionTest {

    HttpServer httpServer;

    public WebResourceFunctionTest() {
    }

    @Before
    public void setup() throws IOException {
	httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
	httpServer.createContext("/api/endpoint", new HttpHandler() {
	    public void handle(HttpExchange exchange) throws IOException {
		byte[] response = "test response data".getBytes();
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
		exchange.getResponseBody().write(response);
		exchange.close();
	    }

	});
	httpServer.start();
    }

    @After
    public void cleanup() {
	httpServer.stop(0);
    }

    /**
     * Test of apply method, of class WebResourceFunction.
     */
    @Test
    public void testApply() {
	System.out.println("apply");
	byte[] template = "test request data".getBytes();
	URI resourceUri = URI.create("http://localhost:8000/api/endpoint");
	WebResourceFunction instance = new WebResourceFunction(resourceUri);
	byte[] result = instance.apply(template);
	Logger.getGlobal().log(Level.INFO, "server response: " + new String(result));
	Assert.assertNotNull(result);
    }

}
