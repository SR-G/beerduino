package org.tensin.beerduino;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.beerduino.notifications.INotification;


/**
 * The Class ThreadNotify.
 */
public class ThreadNotify extends AbstractThread {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadNotify.class);

    /** The queue. */
    private final LinkedBlockingQueue<TemperatureResults> queue;

    /**
     * Instantiates a new thread notify.
     *
     * @param notifications the notifications
     */
    public ThreadNotify(final LinkedBlockingQueue<TemperatureResults> notifications) {
        queue = notifications;
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        setName("THREAD-NOTIFY");
        while (alive) {
            try {
                final TemperatureResults results = queue.take();

                LOGGER.info("Notification emited due to new state [" + results.getState() + "]");
                Collection<INotification> notifiers = Beerduino.getInstance().getPreferences().getNotifiers();
                for (final INotification notifier : notifiers) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                notifier.execute(results);
                            } catch (CoreException e) {
                                LOGGER.error("Error while notifying", e);
                            }
                        }
                    }.start();
                }
            } catch (InterruptedException e) {
                LOGGER.error("Error while notifying", e);
            }
        }
    }
}
