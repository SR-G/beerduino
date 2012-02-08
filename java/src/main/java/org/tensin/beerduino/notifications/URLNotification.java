package org.tensin.beerduino.notifications;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.beerduino.TemperatureResults;
import org.tensin.beerduino.TemperatureState;
import org.tensin.common.CoreException;
import org.tensin.common.tools.documentation.updater.Description;

/**
 * The Class URLNotification.
 */
@Root(name = "url")
@Description("Notification by activating a single URL.")
public class URLNotification extends AbstractNotification implements
        INotification {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(URLNotification.class);

    /**
     * The url. @Todo standard parameters should be available {temp1} =>
     * substitued by the right value once the URL is called
     */
    @Attribute(required = false)
    @Description("The single URL that will be activated.")
    private String url;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.tensin.beerduino.notifications.INotification#execute(org.tensin.beerduino
     * .TemperatureResults)
     */
    @Override
    public void execute(final TemperatureResults results) throws CoreException {
        if (results.getState().compareTo(TemperatureState.OVERHEAT) == 0) {
            activateUrl(url);
        }
    }

}
