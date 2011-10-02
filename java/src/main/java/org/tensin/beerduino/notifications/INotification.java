package org.tensin.beerduino.notifications;

import org.tensin.beerduino.CoreException;
import org.tensin.beerduino.TemperatureResults;


/**
 * The Interface INotification.
 */
public interface INotification {

    /**
     * Execute.
     *
     * @param results the results
     * @throws CoreException the core exception
     */
    void execute(final TemperatureResults results) throws CoreException;

}
