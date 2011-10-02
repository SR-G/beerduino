package org.tensin.beerduino.notifications;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.simpleframework.xml.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.beerduino.CoreException;
import org.tensin.beerduino.TemperatureResults;
import org.tensin.beerduino.TemperatureState;
import org.tensin.beerduino.helpers.CloseHelper;

public class URLNotification implements INotification {

    private static final Logger LOGGER = LoggerFactory.getLogger(URLNotification.class);

    @Attribute(required = false)
    private String url;

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

    @Override
    public void execute(final TemperatureResults results) throws CoreException {
        if (results.getState().compareTo(TemperatureState.OVERHEAT) == 0) {
            activateUrl(url);
        }
    }

}
