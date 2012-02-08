package org.tensin.beerduino.notifications;

import org.tensin.beerduino.TemperatureResults;
import org.tensin.common.CoreException;

/**
 * The Interface INotification.
 */
public interface INotification {

    /**
     * Execute.
     * 
     * @param results
     *            the results
     * @throws CoreException
     *             the core exception
     */
    void execute(final TemperatureResults results) throws CoreException;

    /**
     * Gets the id.
     * 
     * @return the id
     */
    String getId();

    /**
     * Checks if is notifier eligible to results.
     * 
     * @param results
     *            the results
     * @return true, if is notifier eligible to results
     * @throws CoreException
     *             the core exception
     */
    boolean isNotifierEligibleToResults(final TemperatureResults results) throws CoreException;

}
