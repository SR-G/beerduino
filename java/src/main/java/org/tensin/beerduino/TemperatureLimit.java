package org.tensin.beerduino;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class TemperatureLimit {

    @Attribute(name = "capteur")
    private String sensorId;

    @Attribute(name = "temperature")
    private double limit;

    public TemperatureLimit() {
        super();
    }

    public double getLimit() {
        return limit;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setLimit(final double limit) {
        this.limit = limit;
    }

    public void setLimit(final String limit) {
        this.limit = Double.valueOf(limit);
    }

    public void setSensorId(final String sensorId) {
        this.sensorId = sensorId;
    }

}
