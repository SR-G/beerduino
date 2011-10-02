package org.tensin.beerduino;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.jetty.server.Request;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class ArduinoSimulator extends MockHttpServer {

    /**
     * Test the MockHttpServer.
     */
    /*
    @Test
    public void testMockHappyFlow() throws Exception {
        startServer();
        ClassPathResource responseResource = new ClassPathResource("org/tensin/beerduino/temperatures.xml");
        setResponseResource(responseResource);
        Thread.sleep(250);
        InputStream is = new URL("http://localhost:8080/").openStream();
        Assert.assertEquals(IOUtils.toString(responseResource.getInputStream()), IOUtils.toString(is));
        stopServer();
    }
    */

    public static void main(final String[] args) throws CoreException {

        BasicConfigurator.configure();
        ArduinoSimulator simulator = new ArduinoSimulator(8080);
        simulator.startServer();
        // simulator.setResponseResource(new ClassPathResource("org/tensin/beerduino/temperatures.xml"));
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArduinoSimulator(final int port) {
        super(port);
    }

    @Override
    public void handle(final String target, final Request baseRequest, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
        ServletOutputStream outputStream = response.getOutputStream();

        TemperatureResults results = new TemperatureResults();
        results.getResults().add(new TemperatureResult("1", 21 + Math.random()));
        results.getResults().add(new TemperatureResult("2", 22 + Math.random()));
        results.getResults().add(new TemperatureResult("3", 26 + Math.random()));
        results.getResults().add(new TemperatureResult("4", 28 + Math.random()));
        results.getResults().add(new TemperatureResult("5", 30 + Math.random()));

        Serializer serializer = new Persister();
        try {
            serializer.write(results, outputStream);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        outputStream.flush();
    }

}
