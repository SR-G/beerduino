/*
 * 
 */
package org.tensin.beerduino;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.common.CoreException;


/**
 * The Class XMLTemperatureReadingTestCase.
 */
public class XMLTemperatureReadingTestCase extends AbstractTestCase {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(XMLTemperatureReadingTestCase.class);

    /**
     * Test xml temperature reading.
     *
     * @throws CoreException the core exception
     */
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
