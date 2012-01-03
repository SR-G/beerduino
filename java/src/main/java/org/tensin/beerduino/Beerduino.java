package org.tensin.beerduino;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class Beerduino.
 */
public class Beerduino {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Beerduino.class);

    /** The preferences. */
    private static Preferences preferences = new Preferences();

    /** The INSTANCE. */
    private static Beerduino INSTANCE;

    /**
     * Gets the single instance of Beerduino.
     *
     * @return single instance of Beerduino
     */
    public static Beerduino getInstance() {
        return INSTANCE;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     * @throws CoreException the core exception
     */
    public static void main(final String[] args) throws CoreException {
        INSTANCE = new Beerduino();
        INSTANCE.initLog();
        INSTANCE.loadConfiguration();
        INSTANCE.startRRDStorage();
        INSTANCE.startNotifier();
        INSTANCE.startMonitoring();
    }

    /** The temperatures. */
    private final LinkedBlockingQueue<TemperatureResults> temperatures = new LinkedBlockingQueue<TemperatureResults>();

    /** The notifications. */
    private final LinkedBlockingQueue<TemperatureResults> notifications = new LinkedBlockingQueue<TemperatureResults>();

    /** The thread monitoring. */
    private ThreadMonitoring threadMonitoring;

    /** The thread notify. */
    private ThreadNotify threadNotify;

    /** The thread rrd storage. */
    private ThreadRRDStorage threadRRDStorage;

    /**
     * Gets the configuration filename.
     *
     * @return the configuration filename
     */
    private String getConfigurationFilename() {
        return System.getProperty("user.dir") + File.separator + BeerduinoConstants.CONFIGURATION_FILENAME;
    }

    /**
     * Gets the preferences.
     *
     * @return the preferences
     */
    public Preferences getPreferences() {
        return preferences;
    }

    /**
     * Gets the rrd.
     *
     * @return the rrd
     */
    public RRDTemperature getRrd() {
        return threadRRDStorage.getRrd();
    }

    /**
     * Inits the log.
     */
    private void initLog() {
        // Set up a simple configuration that logs on the console.
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure();
        org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
        rootLogger.setLevel(org.apache.log4j.Level.INFO);
        LOGGER.info("Starting Beerduino v" + BeerduinoConstants.PROGRAM_VERSION);
    }

    /**
     * Load configuration.
     *
     * @throws CoreException the core exception
     */
    private void loadConfiguration() throws CoreException {
        preferences = Preferences.loadConfiguration(getConfigurationFilename());
        // preferences.saveConfiguration(getConfigurationFilename());
    }

    /**
     * Start monitoring.
     */
    private void startMonitoring() {
        LOGGER.info("Starting monitoring thread, arduino verification will be made each [" + Beerduino.getInstance().getPreferences().getArduinoCheckFrequency() + "ms]");
        threadMonitoring = new ThreadMonitoring(temperatures, notifications);
        threadMonitoring.start();
    }

    /**
     * Start notifier.
     */
    private void startNotifier() {
        LOGGER.info("Starting notification thread");
        threadNotify = new ThreadNotify(notifications);
        threadNotify.start();
    }

    /**
     * Start rrd storage.
     */
    private void startRRDStorage() {
        LOGGER.info("Starting RRD storage thread");
        threadRRDStorage = new ThreadRRDStorage(temperatures);
        threadRRDStorage.start();
    }
}
