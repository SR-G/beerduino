package org.tensin.beerduino.notifications;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.beerduino.TemperatureResults;
import org.tensin.beerduino.TemperatureState;
import org.tensin.common.CoreException;
import org.tensin.common.helpers.CloseHelper;
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

	public static final int NOT_IMPLEMENTED_501 = 501;

	/**
	 * The url. @Todo standard parameters should be available {temp1} =>
	 * substitued by the right value once the URL is called
	 */
	@Attribute(required = false)
	@Description("The single URL that will be activated.")
	private String url;

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
