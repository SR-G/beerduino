package org.tensin.beerduino.notifications;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.tensin.beerduino.TemperatureResults;
import org.tensin.common.CoreException;
import org.tensin.common.tools.documentation.updater.Description;

/**
 * The Class SMSNotification.
 */
@Root(name = "sms")
@Description("Notification by sending an SMS. Not done yet. Where are the free SMS services by the way ?")
public class SMSNotification implements INotification {

    /**
     * Number.
     */
    @Element
    @Description("SMS number to send the notification")
    private String number;

    /*
     * (non-Javadoc)
     * 
     * @see org.tensin.beerduino.notifications.INotification#execute(org.tensin.beerduino.TemperatureResults)
     */
    @Override
    public void execute(final TemperatureResults results) throws CoreException {

    }

    /**
     * Gets the number.
     * 
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the number.
     * 
     * @param number
     *            the new number
     */
    public void setNumber(String number) {
        this.number = number;
    }
}
