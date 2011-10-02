package org.tensin.beerduino;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Beerduino {

    private static final Logger LOGGER = LoggerFactory.getLogger(Beerduino.class);

    private static Preferences preferences = new Preferences();

    private static Beerduino INSTANCE;

    public static Beerduino getInstance() {
        return INSTANCE;
    }

    public static void main(final String[] args) throws CoreException {
        INSTANCE = new Beerduino();
        INSTANCE.initLog();
        INSTANCE.loadConfiguration();
        INSTANCE.startRRDStorage();
        INSTANCE.startNotifier();
        INSTANCE.startMonitoring();
    }

    private final LinkedBlockingQueue<TemperatureResults> temperatures = new LinkedBlockingQueue<TemperatureResults>();

    private final LinkedBlockingQueue<TemperatureResults> notifications = new LinkedBlockingQueue<TemperatureResults>();

    private ThreadMonitoring threadMonitoring;

    private ThreadNotify threadNotify;

    private ThreadRRDStorage threadRRDStorage;

    private String getConfigurationFilename() {
        return System.getProperty("user.dir") + File.separator + BeerduinoConstants.CONFIGURATION_FILENAME;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public RRDTemperature getRrd() {
        return threadRRDStorage.getRrd();
    }

    private void initLog() {
        // Set up a simple configuration that logs on the console.
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure();
        org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
        rootLogger.setLevel(org.apache.log4j.Level.INFO);
        LOGGER.info("Starting Beerduino v" + BeerduinoConstants.PROGRAM_VERSION);
    }

    private void loadConfiguration() throws CoreException {
        preferences = Preferences.loadConfiguration(getConfigurationFilename());
        // preferences.saveConfiguration(getConfigurationFilename());
    }

    private void startMonitoring() {
        LOGGER.info("Starting monitoring thread");
        threadMonitoring = new ThreadMonitoring(temperatures, notifications);
        threadMonitoring.start();
    }

    private void startNotifier() {
        LOGGER.info("Starting notification thread");
        threadNotify = new ThreadNotify(notifications);
        threadNotify.start();
    }

    private void startRRDStorage() {
        LOGGER.info("Starting RRD storage thread");
        threadRRDStorage = new ThreadRRDStorage(temperatures);
        threadRRDStorage.start();
    }
}
