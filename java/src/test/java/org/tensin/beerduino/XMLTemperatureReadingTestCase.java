package org.tensin.beerduino;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLTemperatureReadingTestCase extends AbstractTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLTemperatureReadingTestCase.class);

    @Test
    public void testXMLTemperatureReading() throws CoreException {
        try {
            Serializer serializer = new Persister();
            File source = new File("src/test/java/org/tensin/beerduino/temperatures.xml");
            TemperatureResults results = serializer.read(TemperatureResults.class, source);
            LOGGER.info("Read : \n" + results.toString());
            Assert.assertEquals(3, results.getResults().size());
        } catch (Exception e) {
            LOGGER.error("Reading error", e);
        }
    }

}
