/*
 * 
 */
package org.tensin.beerduino;

import java.io.File;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.beerduino.notifications.NotifryNotification;


/**
 * The Class XMLTemperatureReadingTestCase.
 */
public class NotificationToNotifryTestCase extends AbstractTestCase {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationToNotifryTestCase.class);

    /**
     * Test xml temperature reading.
     *
     * @throws CoreException the core exception
     */
    @Test
    public void testNotifry() throws CoreException {

        try {
            Serializer serializer = new Persister();
            File source = new File("src/test/java/org/tensin/beerduino/temperatures.xml");
            TemperatureResults results = serializer.read(TemperatureResults.class, source);

            NotifryNotification notify = new NotifryNotification();
        	notify.setNotifrySignature("Beerduino");
        	notify.setNotifrySource("297e1f0a3a0420136a7437b8e6b90e77");
        	notify.execute(results);
        } catch (Exception e) {
            LOGGER.error("Notification error", e);
        }
    }

}
