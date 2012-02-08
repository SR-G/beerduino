package org.tensin.beerduino;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.beerduino.notifications.INotification;
import org.tensin.common.CoreException;

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
     * @param notifications
     *            the notifications
     */
    public ThreadNotify(final LinkedBlockingQueue<TemperatureResults> notifications) {
        queue = notifications;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        setName("THREAD-NOTIFY");
        while (alive) {
            try {
                final TemperatureResults results = queue.take();

                Collection<INotification> notifiers = Beerduino.getInstance().getPreferences().getNotifiers();
                for (final INotification notifier : notifiers) {
                    // Each notifier determines if he's concerned about current results set
                    // If yes, the nofitication is done
                    final TemperatureResults clonedResults = results.clone();
                    if (notifier.isNotifierEligibleToResults(clonedResults)) {
                        new Thread() {
                            @Override
                            public void run() {
                                setName("THREAD-NOTIFY-" + notifier.getClass().getSimpleName().toUpperCase());
                                try {
                                    LOGGER.info("Notification emited with state [" + results.getState() + "] to notifier [" + notifier.getClass().getSimpleName() + "]");
                                    notifier.execute(clonedResults);
                                } catch (CoreException e) {
                                    LOGGER.error("Error while notifying with notifier [" + notifier.getClass().getSimpleName() + "]", e);
                                }
                            }
                        }.start();
                    }
                }
            } catch (InterruptedException e) {
                LOGGER.error("Global error while notifying", e);
            } catch (CoreException e) {
                LOGGER.error("Global error while notifying", e);
            } catch (CloneNotSupportedException e) {
                LOGGER.error("Global error while notifying", e);
            }
        }
    }
}
