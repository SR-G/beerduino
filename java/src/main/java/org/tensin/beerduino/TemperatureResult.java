package org.tensin.beerduino;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 *  <sensor id="1" value="27" type="C" seuil="30" />
 * 
 * @author u248663
 *
 */
@Root(name = "sensor")
public class TemperatureResult {

    @Attribute(name = "value")
    private double temperature;

    @Attribute(name = "id")
    private String sensorId;

    @Attribute(required = false)
    private String type = "C";

    @Attribute(name = "seuil", required = false)
    private double limit = Double.NaN;

    public TemperatureResult() {
        super();
    }

    public TemperatureResult(final String sensorId, final double temperature) {
        super();
        this.temperature = temperature;
        this.sensorId = sensorId;
    }

    public double getLimit() {
        return limit;
    }

    public String getSensorId() {
        return sensorId;
    }

    public TemperatureState getState() {
        if (temperature >= limit) {
            return TemperatureState.OVERHEAT;
        } else {
            return TemperatureState.NORMAL;
        }
    }

    public double getTemperature() {
        return temperature;
    }

    public String getType() {
        return type;
    }

    public void setLimit(final double limit) {
        this.limit = limit;
    }

    public void setSensorId(final String sensorId) {
        this.sensorId = sensorId;
    }

    public void setTemperature(final double temperature) {
        this.temperature = temperature;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Temp [").append(temperature).append("], sensorId [").append(sensorId).append("], seuil [").append(limit).append("]");
        return sb.toString();
    }
}
