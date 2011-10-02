/*
 * 
 */
package org.tensin.beerduino;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.springframework.core.io.Resource;


/**
 * Very simple {@link Server} to mock http traffic.
 */
public class MockHttpServer extends AbstractHandler {

    /*
     * The Jetty Server responsible for opening the Socket
     */
    /** The server. */
    private Server server;

    /*
     * The resource of the (XML, JSON etc) file that should be returned. It is
     * accessible through the server instance and must be accessible from the
     * classpath
     */
    /** The response resource. */
    private Resource responseResource;

    /**
     * Construct MockHtppServer.
     *
     * @param port the port for starting up the HTTP server
     */
    public MockHttpServer(final int port) {
        super();
        initServer(port);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jetty.server.Handler#handle(java.lang.String, org.eclipse.jetty.server.Request, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void handle(final String target, final Request baseRequest, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
        invariant();
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(responseResource.getInputStream(), outputStream);
        outputStream.flush();
    }

    /*
     * Initialize the server
     */
    /**
     * Inits the server.
     *
     * @param port the port
     */
    private void initServer(final int port) {
        server = new Server();
        SocketConnector connector = new SocketConnector();
        connector.setPort(port);
        connector.setMaxIdleTime(1000 * 60 * 60 * 4); // 4 heures
        server.addConnector(connector);
        server.setHandler(this);
    }

    /*
     * Check the response to render back
     */
    /**
     * Invariant.
     */
    protected void invariant() {
        if (responseResource == null) {
            throw new RuntimeException("No responseResource set");
        }
    }

    /**
     * Sets the response resource.
     *
     * @param responseResource the responseResource to set
     */
    public void setResponseResource(final Resource responseResource) {
        this.responseResource = responseResource;
    }

    /**
     * Start the server in a non-blocking mode. The separate thread will be
     * killed when the test class finishes
     */
    public void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.start();
                    // just wait until the server gets killed
                    // System.in.read();
                } catch (Exception e) {
                    stopServer();
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Stop the server.
     */
    public void stopServer() {
        try {
            server.stop();
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
