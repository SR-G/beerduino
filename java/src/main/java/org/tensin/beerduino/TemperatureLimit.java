package org.tensin.beerduino;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * The Class TemperatureLimit.
 */
@Root(name = "seuil")
public class TemperatureLimit {

    /** The sensor id. */
    @Attribute(name = "capteur")
    private String sensorId;

    /** The limit. */
    @Attribute(name = "temperature")
    private double limit;

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
     * Sets the sensor id.
     * 
     * @param sensorId
     *            the new sensor id
     */
    public void setSensorId(final String sensorId) {
        this.sensorId = sensorId;
    }

}
