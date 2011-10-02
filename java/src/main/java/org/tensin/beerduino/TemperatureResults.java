package org.tensin.beerduino;

import java.util.ArrayList;
import java.util.Collection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


/**
 * The Class TemperatureResults.
 */
@Root(name = "sensors")
public class TemperatureResults {

    /** The state. */
    private TemperatureState state;

    /** The results. */
    @ElementList(inline = true)
    private Collection<TemperatureResult> results = new ArrayList<TemperatureResult>();

    /** The time. */
    @Attribute
    private String time = "";

    /**
     * Gets the results.
     *
     * @return the results
     */
    public Collection<TemperatureResult> getResults() {
        return results;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public TemperatureState getState() {
        return state;
    }

    /**
     * Gets the time.
     *
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the results.
     *
     * @param results the new results
     */
    public void setResults(final Collection<TemperatureResult> results) {
        this.results = results;
    }

    /**
     * Sets the state.
     *
     * @param state the new state
     */
    public void setState(final TemperatureState state) {
        this.state = state;
    }

    /**
     * Sets the time.
     *
     * @param time the new time
     */
    public void setTime(final String time) {
        this.time = time;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (TemperatureResult result : results) {
            sb.append(" - ").append(result.toString()).append("\n");
        }
        return sb.toString();
    }
}
