package org.tensin.beerduino;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.beerduino.notifications.INotification;
import org.tensin.beerduino.notifications.MailNotification;
import org.tensin.beerduino.notifications.PushToNotification;
import org.tensin.beerduino.notifications.SMSNotification;
import org.tensin.beerduino.notifications.URLNotification;
import org.tensin.beerduino.tools.Description;

/**
 * The Class Preferences.
 * 
 * @author u248663
 */
@Root(name = "preferences")
@Description("Beerduino global preferences.")
public class Preferences {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Preferences.class);

    /**
     * Load configuration.
     * 
     * @param configurationFilename
     *            the configuration filename
     * @return the preferences
     * @throws CoreException
     *             the core exception
     */
    public static Preferences loadConfiguration(final String configurationFilename) throws CoreException {
        LOGGER.info("Loading configuration from file [" + configurationFilename + "]");
        Serializer serializer = new Persister();
        File source = new File(configurationFilename);

        try {
            Preferences p = serializer.read(Preferences.class, source);
            new File(p.getWorkDir()).mkdirs();
            return p;
        } catch (Exception e) {
            throw new CoreException(e);
        }
    }

    /**
     * 
     */
    @Attribute(required = false)
    private String noNamespaceSchemaLocation;
    
    /**
     * @return
     */
    public String getNoNamespaceSchemaLocation() {
		return noNamespaceSchemaLocation;
	}

	/**
	 * @param noNamespaceSchemaLocation
	 */
	public void setNoNamespaceSchemaLocation(String noNamespaceSchemaLocation) {
		this.noNamespaceSchemaLocation = noNamespaceSchemaLocation;
	}

	/** The arduino ip. */
    @Element
    @Description("IP address of the arduino board")
    private String arduinoIp = "127.0.0.1";

    /** The arduino port. */
    @Element
    @Description("Port of the arduino board. Default to 80.")
    private int arduinoPort = 80;

    /** The arduino port. */
    @Element(required = false)
    @Description("Frequency of the arduino values checkout. In milliseconds. Default to 1000.")
    private int arduinoCheckFrequency = 1000;

    public int getArduinoCheckFrequency() {
		return arduinoCheckFrequency;
	}

	public void setArduinoCheckFrequency(int arduinoCheckFrequency) {
		this.arduinoCheckFrequency = arduinoCheckFrequency;
	}

	/** The limits. */
    @ElementList(name = "seuils", required = false)
    @Description("Temperatures warnings (after / below whom a notification will be sent)")
    private Collection<TemperatureLimit> limits = new ArrayList<TemperatureLimit>();

    /** The notifiers. */
    @ElementListUnion({ @ElementList(entry = "mail", inline = true, type = MailNotification.class),
            @ElementList(entry = "sms", inline = true, type = SMSNotification.class),
            @ElementList(entry = "push", inline = true, type = PushToNotification.class),
            @ElementList(entry = "url", inline = true, type = URLNotification.class) })
    @Description("Notifications mechanism. Multiple notifications may be defined at the same time.")
    private Collection<INotification> notifiers = new ArrayList<INotification>();

    /** The work dir. */
    @Element(required = false)
    @Description("Temp directory.")
    private String workDir = System.getProperty("user.dir") + File.separator + "work" + File.separator;

    /**
     * Adds the.
     * 
     * @param notifier
     *            the notifier
     */
    public void add(final INotification notifier) {
        notifiers.add(notifier);
    }

    /**
     * Gets the arduino ip.
     * 
     * @return the arduino ip
     */
    public String getArduinoIp() {
        return arduinoIp;
    }

    /**
     * Gets the arduino port.
     * 
     * @return the arduino port
     */
    public int getArduinoPort() {
        return arduinoPort;
    }

    /**
     * Gets the arduino url.
     * 
     * @return the arduino url
     */
    public String getArduinoUrl() {
        return "http://" + arduinoIp + ":" + arduinoPort + "/";
    }

    /**
     * Gets the limit for sensor.
     * 
     * @param sensorId
     *            the sensor id
     * @return the limit for sensor
     */
    public TemperatureLimit getLimitForSensor(final String sensorId) {
        if (StringUtils.isNotEmpty(sensorId)) {
            for (TemperatureLimit limit : limits) {
                if (sensorId.equalsIgnoreCase(limit.getSensorId())) {
                    return limit;
                }
            }
        }
        return null;
    }

    /**
     * Gets the limits.
     * 
     * @return the limits
     */
    public Collection<TemperatureLimit> getLimits() {
        return limits;
    }

    /**
     * Gets the notifiers.
     * 
     * @return the notifiers
     */
    public Collection<INotification> getNotifiers() {
        return notifiers;
    }

    /**
     * Gets the work dir.
     * 
     * @return the work dir
     */
    public String getWorkDir() {
        return workDir;
    }

    /**
     * Save configuration.
     * 
     * @param configurationFilename
     *            the configuration filename
     */
    public void saveConfiguration(final String configurationFilename) {
        Serializer serializer = new Persister();
        File dest = new File(configurationFilename);

        try {
            serializer.write(this, dest);
        } catch (Exception e) {
            LOGGER.error("Error while writing preferences to [" + configurationFilename + "]", e);
        }
    }

    /**
     * Sets the arduino ip.
     * 
     * @param arduinoIp
     *            the new arduino ip
     */
    public void setArduinoIp(final String arduinoIp) {
        this.arduinoIp = arduinoIp;
    }

    /**
     * Sets the arduino port.
     * 
     * @param arduinoPort
     *            the new arduino port
     */
    public void setArduinoPort(final int arduinoPort) {
        this.arduinoPort = arduinoPort;
    }

    /**
     * Sets the limits.
     * 
     * @param limits
     *            the new limits
     */
    public void setLimits(final Collection<TemperatureLimit> limits) {
        this.limits = limits;
    }

    /**
     * Sets the notifiers.
     * 
     * @param notifiers
     *            the new notifiers
     */
    public void setNotifiers(final Collection<INotification> notifiers) {
        this.notifiers = notifiers;
    }

    /**
     * Sets the work dir.
     * 
     * @param workDir
     *            the new work dir
     */
    public void setWorkDir(final String workDir) {
        this.workDir = workDir;
    }

}
