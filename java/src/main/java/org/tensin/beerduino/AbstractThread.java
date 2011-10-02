package org.tensin.beerduino;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractThread extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractThread.class);

	protected final static int SECONDS = 1000;
	
	protected boolean alive = true;

	protected void sleepSeconds(final long seconds) {
		sleepMilliSeconds(seconds * SECONDS);
	}
	
	protected void sleepMilliSeconds(final long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			LOGGER.error("Thread interrupted", e);
		}
	}
	
}
