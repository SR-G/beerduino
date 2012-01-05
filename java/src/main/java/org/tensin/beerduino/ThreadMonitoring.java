package org.tensin.beerduino;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.common.helpers.CloseHelper;

/**
 * The Class ThreadMonitoring.
 */
public class ThreadMonitoring extends AbstractThread {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadMonitoring.class);

    /** The notifications. */
    private final LinkedBlockingQueue<TemperatureResults> notifications;

    /** The temperatures. */
    private final LinkedBlockingQueue<TemperatureResults> temperatures;

    /**
     * Instantiates a new thread monitoring.
     * 
     * @param temperatures
     *            the temperatures
     * @param notifications
     *            the notifications
     */
    public ThreadMonitoring(final LinkedBlockingQueue<TemperatureResults> temperatures, final LinkedBlockingQueue<TemperatureResults> notifications) {
        super();
        this.notifications = notifications;
        this.temperatures = temperatures;
    }

    /**
     * Are temperatures back to normal.
     * 
     * @param results
     *            the results
     * @return true, if successful
     */
    private boolean areTemperaturesBackToNormal(final TemperatureResults results) {
        int temperaturesBelowCount = 0;
        int checkedLimitsCount = 0;
        for (TemperatureResult result : results.getResults()) {
            checkedLimitsCount++;
            if (result.getTemperature() < result.getLimit()) {
                temperaturesBelowCount++;
            }
        }
        if (temperaturesBelowCount == checkedLimitsCount) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Are temperatures overheat.
     * 
     * @param results
     *            the results
     * @return true, if successful
     */
    private boolean areTemperaturesOverheat(final TemperatureResults results) {
        for (TemperatureResult result : results.getResults()) {
            if (result.getTemperature() >= result.getLimit()) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        setName("THREAD-MONITORING");
        Serializer serializer = new Persister();
        URL url = null;
        InputStream is = null;
        TemperatureResults results = null;
        boolean overheat = false;
        while (alive) {
            sleepMilliSeconds(Beerduino.getInstance().getPreferences().getArduinoCheckFrequency());
            LOGGER.info("now acquiring");
            try {
                url = new URL(Beerduino.getInstance().getPreferences().getArduinoUrl());
                is = url.openStream();
                results = serializer.read(TemperatureResults.class, is);
                for (TemperatureResult result : results.getResults()) {
                    TemperatureLimit limit = Beerduino.getInstance().getPreferences().getLimitForSensor(result.getSensorId());
                    if (limit != null) {
                        result.setLimit(limit.getLimit());
                    }
                }

                temperatures.offer(results);
                if (!overheat) {
                    if (areTemperaturesOverheat(results)) {
                        overheat = true;
                        results.setState(TemperatureState.OVERHEAT);
                        notifications.offer(results);
                    }
                } else {
                    if (areTemperaturesBackToNormal(results)) {
                        overheat = false;
                        results.setState(TemperatureState.NORMAL);
                        notifications.offer(results);
                    }
                }
                LOGGER.info("Results : \n" + results.toString());
            } catch (MalformedURLException e) {
                LOGGER.error("Error while getting results from [" + Beerduino.getInstance().getPreferences().getArduinoUrl() + "]", e);
            } catch (IOException e) {
                LOGGER.error("Error while getting results from [" + Beerduino.getInstance().getPreferences().getArduinoUrl() + "]", e);
            } catch (Exception e) {
                LOGGER.error("Error while getting results from [" + Beerduino.getInstance().getPreferences().getArduinoUrl() + "]", e);
            } finally {
                CloseHelper.close(is);
            }
        }
    }

}
