package org.tensin.beerduino.notifications;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.beerduino.TemperatureResult;
import org.tensin.beerduino.TemperatureResults;
import org.tensin.beerduino.TemperatureState;
import org.tensin.common.CoreException;
import org.tensin.common.tools.documentation.updater.Description;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * The Class TwitterNotification. Use : https://notifrier.appspot.com (Android
 * only)
 * 
 */
@Root(name = "twitter")
@Description("Notification by sending a Tweet.")
public class TwitterNotification implements INotification {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TwitterNotification.class);

	/** The pushto url. */
	@Attribute(name = "dest", required = true)
	@Description("Recipient ID that will receive the tweets")
	private String recipientId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.tensin.beerduino.notifications.URLNotification#execute(org.tensin
	 * .beerduino.TemperatureResults)
	 */
	@Override
	public void execute(final TemperatureResults results) throws CoreException {
		LOGGER.info("Sending Twitter To notification to [" + recipientId + "]");

		try {
			StringBuilder sb = new StringBuilder();
			if (results.getState().compareTo(TemperatureState.OVERHEAT) == 0) {
				sb.append("Dépassement de température !");
			} else {
				sb.append("Températures back to normal");
			}
			sb.append(" (");
			int cnt = 0;
			for (TemperatureResult result : results.getResults()) {
				if (cnt > 0) {
					sb.append(",");
				}
				sb.append(result.getTemperature());
				cnt++;
			}
			sb.append(")");

			Twitter sender = new TwitterFactory().getInstance();
			DirectMessage message = sender.sendDirectMessage(recipientId,
					sb.toString());
			LOGGER.info("Sent: " + sb.toString() + " to @"
					+ message.getRecipientScreenName());
		} catch (TwitterException e) {
			LOGGER.error("Error while twitting to [" + recipientId + "]", e);
		} finally {
		}
	}

}
