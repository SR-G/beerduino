package org.tensin.beerduino;

import java.util.concurrent.LinkedBlockingQueue;

import org.rrd4j.core.Sample;
import org.rrd4j.core.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadRRDStorage extends AbstractThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadRRDStorage.class);

    private final LinkedBlockingQueue<TemperatureResults> queue;

    private RRDTemperature rrd;

    private ThreadRRDStorage() {
        rrd = null;
        queue = null;
    }

    public ThreadRRDStorage(final LinkedBlockingQueue<TemperatureResults> queue) {
        super();
        this.queue = queue;
        rrd = null;
    }

    public RRDTemperature getRrd() {
        return rrd;
    }

    @Override
    public void run() {
        setName("THREAD-RRD-STORAGE");
        while (alive) {
            try {
                TemperatureResults results = queue.take();
                if (rrd == null) {
                    rrd = new RRDTemperature();
                    rrd.setRrdFileName(Beerduino.getInstance().getPreferences().getWorkDir() + "temperatures.rrd");
                    rrd.setStartTime(Util.getTime());
                    for (TemperatureResult result : results.getResults()) {
                        rrd.addDatasourceSensorsName(RRDTemperature.DATASOURCE_TEMPERATURE + "-" + result.getSensorId());
                    }
                }

                Sample s = rrd.acquireSample();
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
