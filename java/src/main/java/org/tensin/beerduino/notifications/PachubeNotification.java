package org.tensin.beerduino.notifications;

import java.util.Collection;

import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.beerduino.Beerduino;
import org.tensin.beerduino.TemperatureLimit;
import org.tensin.beerduino.TemperatureResult;
import org.tensin.beerduino.TemperatureResults;
import org.tensin.common.CoreException;
import org.tensin.common.tools.documentation.updater.Description;

/**
 * The Class MailNotification.
 */
@Root(name = "pachube")
@Description("Notification by pushing data to pachube.")
public class PachubeNotification extends AbstractNotification implements
        INotification {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PachubeNotification.class);

    /**
     * Instantiates a new mail notification.
     */
    public PachubeNotification() {
        super();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.tensin.beerduino.notifications.INotification#execute(org.tensin.beerduino.TemperatureResults)
     */
    @Override
    public void execute(final TemperatureResults results) throws CoreException {
        LOGGER.info("Pushing datas to pachube");
        try {
            for (final TemperatureResult result : results.getResults()) {
                Collection<TemperatureLimit> limits = Beerduino.getInstance().getPreferences().getLimitForSensor(result.getSensorId());
                if (limits.size() > 0) {
                    for (TemperatureLimit limit : limits) {
                        if (isNotifierEligibleToLimit(limit)) {
                            // if (limit.getLimit() == Double.NaN) {
                            LOGGER.info("Pushing data to PACHUBE");
                        }
                    }
                }
            }
        } finally {

        }
    }
}
