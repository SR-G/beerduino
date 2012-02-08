package org.tensin.beerduino.notifications;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.beerduino.Beerduino;
import org.tensin.beerduino.TemperatureLimit;
import org.tensin.beerduino.TemperatureResult;
import org.tensin.beerduino.TemperatureResults;
import org.tensin.beerduino.TemperatureState;
import org.tensin.common.CoreException;
import org.tensin.common.helpers.CloseHelper;
import org.tensin.common.tools.documentation.updater.Description;

/**
 * The Class AbstractNotification.
 */
public abstract class AbstractNotification {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNotification.class);

    /** The Constant NOT_IMPLEMENTED_501. */
    public static final int NOT_IMPLEMENTED_501 = 501;

    /** The id. */
    @Attribute(required = false)
    @Description("Internal ID defining the notification. Can be reused below (on the temperature limits for example)")
    private String id;

    /** The overheat. */
    private boolean overheat = false;

    /**
     * Activate url.
     * 
     * @param address
     *            the address
     */
    protected void activateUrl(final String address) {
        try {
            URL u = new URL(address);
            InputStream is = u.openStream();
            CloseHelper.close(is);
        } catch (MalformedURLException e) {
            LOGGER.error("Error while activating to [" + address + "]", e);
        } catch (IOException e) {
            LOGGER.error("Error while activating to [" + address + "]", e);
        }
    }

    /**
     * Are temperatures back to normal.
     * 
     * @param results
     *            the results
     * @return true, if successful
     */
    private boolean areTemperaturesBackToNormal(final TemperatureResults results) {
        int temperaturesBelowCount = 0;
        int checkedLimitsCount = 0;
        for (TemperatureResult result : results.getResults()) {
            checkedLimitsCount++;
            if (result.getTemperature() < result.getLimit()) {
                temperaturesBelowCount++;
            }
        }
        if (temperaturesBelowCount == checkedLimitsCount) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Are temperatures overheat.
     * 
     * @param results
     *            the results
     * @return true, if successful
     */
    private boolean areTemperaturesOverheat(final TemperatureResults results) {
        for (TemperatureResult result : results.getResults()) {
            if (result.getTemperature() >= result.getLimit()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extract items from list.
     * 
     * @param notifiersDefined
     *            the notifiers defined
     * @return the collection
     */
    private Collection<String> extractItemsFromList(final String notifiersDefined) {
        Collection<String> results = new ArrayList<String>();
        for (String item : notifiersDefined.split(",")) {
            results.add(item.trim());
        }
        return results;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Checks if is notifier eligible to limit.
     * 
     * @param limit
     *            the limit
     * @return true, if is notifier eligible to limit
     */
    protected boolean isNotifierEligibleToLimit(final TemperatureLimit limit) {
        String notifiersDefined = limit.getNotifiers();
        if (StringUtils.isEmpty(notifiersDefined)) {
            return true;
        } else {
            if (StringUtils.isEmpty(getId())) {
                return false;
            }
            for (String notifierIDDefined : extractItemsFromList(notifiersDefined)) {
                if (getId().equalsIgnoreCase(notifierIDDefined)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Checks if is notifier eligible to results.
     * 
     * @param results
     *            the results
     * @return true, if is notifier eligible to results
     * @throws CoreException
     *             the core exception
     */
    public boolean isNotifierEligibleToResults(final TemperatureResults results) throws CoreException {
        boolean notify = false;
        Iterator<TemperatureResult> itr = results.getResults().iterator();
        while (itr.hasNext()) {
            TemperatureResult result = itr.next();
            Collection<TemperatureLimit> limits = Beerduino.getInstance().getPreferences().getLimitForSensor(result.getSensorId());
            if (limits.size() > 0) {
                for (TemperatureLimit limit : limits) {
                    if (isNotifierEligibleToLimit(limit)) {
                        if (Double.isNaN(limit.getLimit())) {
                            // Always notify <= unlimited value
                            notify = true;
                        } else {
                            // Notify only if overheat (up or down)
                            result.setLimit(limit.getLimit());
                        }
                    }
                }
            } else {
                // No limit = no notification
            }
        }

        if (!notify) {
            if (!overheat) {
                if (areTemperaturesOverheat(results)) {
                    overheat = true;
                    results.setState(TemperatureState.OVERHEAT);
                    notify = true;
                }
            } else {
                if (areTemperaturesBackToNormal(results)) {
                    overheat = false;
                    results.setState(TemperatureState.NORMAL);
                    notify = true;
                }
            }
        }
        return notify;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the new id
     */
    public void setId(final String id) {
        this.id = id;
    }

}
