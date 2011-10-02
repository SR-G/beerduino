package org.tensin.beerduino;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class AbstractThread.
 */
public class AbstractThread extends Thread {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractThread.class);

	/** The Constant SECONDS. */
	protected final static int SECONDS = 1000;
	
	/** The alive. */
	protected boolean alive = true;

	/**
	 * Sleep seconds.
	 *
	 * @param seconds the seconds
	 */
	protected void sleepSeconds(final long seconds) {
		sleepMilliSeconds(seconds * SECONDS);
	}
	
	/**
	 * Sleep milli seconds.
	 *
	 * @param milliseconds the milliseconds
	 */
	protected void sleepMilliSeconds(final long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			LOGGER.error("Thread interrupted", e);
		}
	}
	
}
