package org.tensin.beerduino;

import java.util.concurrent.LinkedBlockingQueue;

import org.rrd4j.core.Sample;
import org.rrd4j.core.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.common.CoreException;

/**
 * The Class ThreadRRDStorage.
 */
public class ThreadRRDStorage extends AbstractThread {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadRRDStorage.class);

    /** The queue. */
    private final LinkedBlockingQueue<TemperatureResults> queue;

    /** The rrd. */
    private RRDTemperature rrd;

    /**
     * Instantiates a new thread rrd storage.
     */
    private ThreadRRDStorage() {
        rrd = null;
        queue = null;
    }

    /**
     * Instantiates a new thread rrd storage.
     * 
     * @param queue
     *            the queue
     */
    public ThreadRRDStorage(final LinkedBlockingQueue<TemperatureResults> queue) {
        super();
        this.queue = queue;
        rrd = null;
    }

    /**
     * Gets the rrd.
     * 
     * @return the rrd
     */
    public RRDTemperature getRrd() {
        return rrd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        setName("THREAD-RRD-STORAGE");
        TemperatureResults results;
        Sample s;
        while (alive) {
            try {
                results = queue.take();
                if (rrd == null) {
                    rrd = new RRDTemperature();
                    rrd.setRrdFileName(Beerduino.getInstance().getPreferences().getWorkDir() + "temperatures.rrd");
                    rrd.setStartTime(Util.getTime());
                    for (TemperatureResult result : results.getResults()) {
                        rrd.addDatasourceSensorsName(RRDTemperature.DATASOURCE_TEMPERATURE + "-" + result.getSensorId());
                    }
                }

                s = rrd.acquireSample();
                s.setTime(Util.getTime());

                for (TemperatureResult result : results.getResults()) {
                    rrd.update(s, RRDTemperature.DATASOURCE_TEMPERATURE + "-" + result.getSensorId(), result.getTemperature());
                }
                rrd.commitSample(s);
            } catch (CoreException e) {
                LOGGER.error("Error while storing in RRD file", e);
            } catch (InterruptedException e) {
                LOGGER.error("Error while storing in RRD file", e);
            }
        }
    }
}
