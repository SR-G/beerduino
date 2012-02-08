package org.tensin.beerduino;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.tensin.common.tools.documentation.updater.Description;

/**
 * The Class TemperatureLimit.
 */
@Root(name = "seuil")
@Description("Definition of a temperature limit for a sensor")
public class TemperatureLimit {

    /** The sensor id. */
    @Attribute(name = "capteur")
    @Description("Sensor ID (usually a number, corresponding to the sensor linked to the arduino board)")
    private String sensorId;

    /** The limit. */
    @Attribute(name = "temperature", required = false)
    @Description("Temperature limit : once reached, the notifications will be activated. Optional, if not set, the notification will always be emitted.")
    private double limit = Double.NaN;

    @Attribute(name = "notifiers", required = false)
    @Description("The ID of the notifiers that are concerned by this limit. If empty, all notifiers will be notified.")
    private String notifiers;

    /**
     * Instantiates a new temperature limit.
     */
    public TemperatureLimit() {
        super();
    }

    /**
     * Gets the limit.
     * 
     * @return the limit
     */
    public double getLimit() {
        return limit;
    }

    /**
     * Gets the notifiers.
     * 
     * @return the notifiers
     */
    public String getNotifiers() {
        return notifiers;
    }

    /**
     * Gets the sensor id.
     * 
     * @return the sensor id
     */
    public String getSensorId() {
        return sensorId;
    }

    /**
     * Sets the limit.
     * 
     * @param limit
     *            the new limit
     */
    public void setLimit(final double limit) {
        this.limit = limit;
    }

    /**
     * Sets the limit.
     * 
     * @param limit
     *            the new limit
     */
    public void setLimit(final String limit) {
        this.limit = Double.valueOf(limit);
    }

    /**
     * Sets the notifiers.
     * 
     * @param notifiers
     *            the new notifiers
     */
    public void setNotifiers(final String notifiers) {
        this.notifiers = notifiers;
    }

    /**
     * Sets the sensor id.
     * 
     * @param sensorId
     *            the new sensor id
     */
    public void setSensorId(final String sensorId) {
        this.sensorId = sensorId;
    }

}
