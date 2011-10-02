/*
 * 
 */
package org.tensin.beerduino;

import org.springframework.core.io.Resource;


/**
 * Tests subclassing {@link AbstractMockHttpServerTest} can communicate with a
 * mock Http endpoint that behavious like a real server.
 * 
 * The idea behind this class is that http responses can be tested (regardless
 * of the input request).
 * 
 */
public abstract class AbstractMockHttpServerTest extends AbstractTestCase {

    /*
     * The Server that handles the Soap calls
     */
    /** The server. */
    private MockHttpServer server;

    /**
     * Set the expected response on the Handler.
     *
     * @param responseResource the resource of the file containing the xml response
     */
    protected void setResponseResource(final Resource responseResource) {
        server.setResponseResource(responseResource);
    }

    /**
     * Start server.
     */
    public void startServer() {
        server = new MockHttpServer(8080);
        server.startServer();
    }

    /**
     * Stop server.
     */
    public void stopServer() {
        server.stopServer();
    }
}
