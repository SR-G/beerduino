package org.tensin.beerduino;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
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

/**
 * @author u248663
 *
 */
@Root
public class Preferences {

    private static final Logger LOGGER = LoggerFactory.getLogger(Preferences.class);

    public static Preferences loadConfiguration(final String configurationFilename) throws CoreException {
        LOGGER.info("Loading configuration from file [" + configurationFilename + "]");
        /*
        MailNotification mn = new MailNotification();
        mn.recipients.add(new Recipient("serge.simon@gmail.com", "Serge SIMON"));
        notifiers.add(mn);
        */
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

    @ElementListUnion({ @ElementList(entry = "mail", inline = true, type = MailNotification.class),
            @ElementList(entry = "sms", inline = true, type = SMSNotification.class),
            @ElementList(entry = "push", inline = true, type = PushToNotification.class),
            @ElementList(entry = "url", inline = true, type = URLNotification.class) })
    private Collection<INotification> notifiers = new ArrayList<INotification>();

    @ElementList(name = "seuils", required = false)
    private Collection<TemperatureLimit> limits = new ArrayList<TemperatureLimit>();

    @Element
    private String arduinoIp = "127.0.0.1";

    @Element(required = false)
    private String workDir = System.getProperty("user.dir") + File.separator + "work" + File.separator;

    @Element
    private int arduinoPort = 8080;

    public void add(final INotification notifier) {
        notifiers.add(notifier);
    }

    public String getArduinoIp() {
        return arduinoIp;
    }

    public int getArduinoPort() {
        return arduinoPort;
    }

    public String getArduinoUrl() {
        return "http://" + arduinoIp + ":" + arduinoPort + "/";
    }

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

    public Collection<TemperatureLimit> getLimits() {
        return limits;
    }

    public Collection<INotification> getNotifiers() {
        return notifiers;
    }

    public String getWorkDir() {
        return workDir;
    }

    public void saveConfiguration(final String configurationFilename) {
        Serializer serializer = new Persister();
        File dest = new File(configurationFilename);

        try {
            serializer.write(this, dest);
        } catch (Exception e) {
            LOGGER.error("Error while writing preferences to [" + configurationFilename + "]", e);
        }
    }

    public void setArduinoIp(final String arduinoIp) {
        this.arduinoIp = arduinoIp;
    }

    public void setArduinoPort(final int arduinoPort) {
        this.arduinoPort = arduinoPort;
    }

    public void setLimits(final Collection<TemperatureLimit> limits) {
        this.limits = limits;
    }

    public void setNotifiers(final Collection<INotification> notifiers) {
        this.notifiers = notifiers;
    }

    public void setWorkDir(final String workDir) {
        this.workDir = workDir;
    }

}
