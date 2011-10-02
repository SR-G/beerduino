package org.tensin.beerduino.notifications;

import org.simpleframework.xml.Root;
import org.tensin.beerduino.CoreException;
import org.tensin.beerduino.TemperatureResults;


/**
 * The Class SMSNotification.
 */
@Root
public class SMSNotification implements INotification {

    /* (non-Javadoc)
     * @see org.tensin.beerduino.notifications.INotification#execute(org.tensin.beerduino.TemperatureResults)
     */
    @Override
    public void execute(final TemperatureResults results) throws CoreException {

    }
}
