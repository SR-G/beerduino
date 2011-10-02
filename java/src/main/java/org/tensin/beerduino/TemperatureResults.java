package org.tensin.beerduino;

import java.util.ArrayList;
import java.util.Collection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "sensors")
public class TemperatureResults {

    private TemperatureState state;

    @ElementList(inline = true)
    private Collection<TemperatureResult> results = new ArrayList<TemperatureResult>();

    @Attribute
    private String time = "";

    public Collection<TemperatureResult> getResults() {
        return results;
    }

    public TemperatureState getState() {
        return state;
    }

    public String getTime() {
        return time;
    }

    public void setResults(final Collection<TemperatureResult> results) {
        this.results = results;
    }

    public void setState(final TemperatureState state) {
        this.state = state;
    }

    public void setTime(final String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (TemperatureResult result : results) {
            sb.append(" - ").append(result.toString()).append("\n");
        }
        return sb.toString();
    }
}
