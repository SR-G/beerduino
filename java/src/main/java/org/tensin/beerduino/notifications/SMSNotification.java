package org.tensin.beerduino.notifications;

import org.simpleframework.xml.Root;
import org.tensin.beerduino.CoreException;
import org.tensin.beerduino.TemperatureResults;
import org.tensin.beerduino.tools.Description;


/**
 * The Class SMSNotification.
 */
@Root(name = "sms")
@Description("Notification by sending an SMS. Not done yet. Where are the free SMS services by the way ?")
public class SMSNotification implements INotification {

    /* (non-Javadoc)
     * @see org.tensin.beerduino.notifications.INotification#execute(org.tensin.beerduino.TemperatureResults)
     */
    @Override
    public void execute(final TemperatureResults results) throws CoreException {

    }
}
