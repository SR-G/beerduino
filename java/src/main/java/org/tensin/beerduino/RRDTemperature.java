package org.tensin.beerduino;

import java.awt.Color;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.rrd4j.graph.RrdGraph;
import org.rrd4j.graph.RrdGraphDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.common.CoreException;

/**
 * The Class RRDTemperature.
 */
public class RRDTemperature {

    /** GRAPH_IMAGE_HEIGHT. */
    public static final int GRAPH_IMAGE_HEIGHT = 600;

    /** GRAPH_IMAGE_WIDTH. */
    public static final int GRAPH_IMAGE_WIDTH = 1024;

    /** startTime. */
    private long startTime;

    /** rrdFileName. */
    private String rrdFileName;

    /** rrdDb. */
    private RrdDb rrdDb;

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RRDTemperature.class);

    /** The Constant DATASOURCE_TEMPERATURE. */
    public static final String DATASOURCE_TEMPERATURE = "temperature";

    /**
     * Method.
     * 
     * @param l
     *            the l
     * @return the string
     */
    public static String convertTimestamp(final long l) {
        Date date = new Date();
        date.setTime(Long.valueOf(l));
        SimpleDateFormat sdf = new SimpleDateFormat("H:m:s:S");
        return sdf.format(date);
    }

    /** The sensors name. */
    private final Collection<String> sensorsName = new ArrayList<String>();

    /**
     * Method.
     * 
     * @return the sample
     * @throws CoreException
     *             the core exception
     */
    public Sample acquireSample() throws CoreException {
        try {
            return getRrdDb().createSample();
        } catch (IOException e) {
            throw new CoreException(e);
        }
    }

    /**
     * Adds the datasource sensors name.
     * 
     * @param name
     *            the name
     */
    public void addDatasourceSensorsName(final String name) {
        sensorsName.add(name);
    }

    /**
     * Method.
     * 
     */
    public void close() {
        try {
            getRrdDb().close();
        } catch (IOException e) {
            LOGGER.error("Erreur à la fermeture de la base RRD", e);
        }
    }

    /**
     * Method.
     * 
     * @param s
     *            the s
     * @throws CoreException
     *             the core exception
     */
    public void commitSample(final Sample s) throws CoreException {
        try {
            s.update();
        } catch (IOException e) {
            throw new CoreException(e);
        }
    }

    /**
     * Method.
     * 
     * @param start
     *            the start
     * @param end
     *            the end
     */
    public void dump(final long start, final long end) {
        try {
            RrdDb rrdDbReader = new RrdDb(rrdFileName, true);
            FetchRequest request = rrdDbReader.createFetchRequest(ConsolFun.AVERAGE, start, end);
            LOGGER.info(request.dump());
            FetchData fetchData = request.fetchData();
            LOGGER.info("== Data fetched. " + fetchData.getRowCount() + " points obtained");
            LOGGER.info(fetchData.toString());
            // log.info("== Dumping fetched data to XML format");
            // log.info(fetchData.exportXml());
        } catch (IOException e) {
            LOGGER.error("Error while dumping RRD datas", e);
        }
    }

    /**
     * Gets the colors.
     * 
     * @return the colors
     */
    public Collection<Color> getColors() {
        Collection<Color> colors = new ArrayList<Color>();
        colors.add(Color.GREEN);
        colors.add(Color.ORANGE);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.BLACK);
        colors.add(Color.PINK);
        return colors;
    }

    /**
     * Getter for the attribute rrdDb.
     * 
     * @return Returns the attribute rrdDb.
     */
    public RrdDb getRrdDb() {
        if (rrdDb == null) {
            rrdDb = init();
        }
        return rrdDb;
    }

    /**
     * Getter for the attribute rrdFileName.
     * 
     * @return Returns the attribute rrdFileName.
     */
    public String getRrdFileName() {
        return rrdFileName;
    }

    /**
     * Getter for the attribute startTime.
     * 
     * @return Returns the attribute startTime.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Graph.
     * 
     * @param filename
     *            the filename
     * @param startTime
     *            the start time
     * @param endTime
     *            the end time
     * @param title
     *            the title
     * @return the string
     * @throws CoreException
     *             the core exception
     */
    public String graph(final String filename, final long startTime, final long endTime, final String title) throws CoreException {
        String generatedGraphFilename = filename + ".png";

        // Création premier graph
        LOGGER.info("Rendering graph to base filenames [" + filename + "] from [" + convertTimestamp(startTime) + "] to [" + convertTimestamp(endTime) + "]");
        RrdGraphDef gDef = new RrdGraphDef();
        gDef.setWidth(GRAPH_IMAGE_WIDTH);
        gDef.setHeight(GRAPH_IMAGE_HEIGHT);
        gDef.setFilename(generatedGraphFilename);
        gDef.setStartTime(startTime);
        gDef.setEndTime(endTime);
        gDef.setTitle(title);
        gDef.setVerticalLabel("temp");
        // gDef.hrule(2568, Color.GREEN, "hrule");
        gDef.setImageFormat("png");
        // gDef.setPoolUsed(false);
        // gDef.vrule((start + 2 * end) / 3, Color.MAGENTA, "vrule\\c");
        // gDef.comment(label + "\\r");

        Iterator<Color> itr = getColors().iterator();

        for (String sensorName : sensorsName) {
            gDef.datasource(sensorName, getRrdFileName(), sensorName, ConsolFun.AVERAGE);
            gDef.line(sensorName, itr.next(), sensorName);
            gDef.gprint(sensorName, ConsolFun.AVERAGE, "avgTotal = %.3f%S\\c");
        }

        try {
            // Génère chaque graph
            new RrdGraph(gDef);
        } catch (IOException e) {
            throw new CoreException(e);
        }
        return generatedGraphFilename;
    }

    /**
     * Inits the.
     * 
     * @return the rrd db
     */
    public RrdDb init() {
        try {
            int step = 1; // On stocke une mesure à chaque "1 seconde"
            double min = -Double.NaN;
            double max = Double.NaN;
            RrdDef rrdDef = new RrdDef(getRrdFileName(), getStartTime() - 1, step);
            // rrdDef.setVersion(2);
            for (String sensorName : sensorsName) {
                rrdDef.addDatasource(sensorName, DsType.GAUGE, step * 7200, min, max);
            }

            // On garde tous les relevés pendant 1 heure (avec 1 prise toutes les secondes).
            // = on stocke 60 secondes * 60 minutes = 1 heure de donnnées
            rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 1, (60 * 60 * 1));

            // On fait la moyenne par 5 minutes (avec 1 prise toutes les secondes) en on conserve sur la journée.
            // Comme on aggrège 60 * 5 = 300 secondes = 5 minutes, on conserve un nombre d'enregistrements
            // de 12 (* 5 minutes d'écart = 1 heure) * 24 = 1 journée * 5 = 5 J
            rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, (60 * 5), (12 * 24 * 5));

            // On fait la moyenne toutes les heures et on les gardes pendant 1 mois.
            // Aggrégation sur 60 secondes * 60 minutes = 1 heure
            // Et on conserve 24 heures * 31 jours = 1 mois d'entrées de type heures aggrégées
            rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, (60 * 60), (24 * 31));

            // On fait la moyenne tous les jours et on les gardes pendant 10 ans.
            // Aggrégation sur 60 secondes * 60 minutes * 24 = 1 jour
            // Et on conserve 1 journée * 365J = 1 année * 10
            rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, (60 * 60 * 24), (365 * 10));

            // rrdDb = new RrdDb(rrdDef, backendFactory);
            return new RrdDb(rrdDef);
        } catch (IOException e) {
            LOGGER.error("Error while initializing RRD storage", e);
            return null;
        }
    }

    /**
     * Setter for the attribute rrdDb.
     * 
     * @param rrdDb
     *            The attribute rrdDb.
     */
    public void setRrdDb(final RrdDb rrdDb) {
        this.rrdDb = rrdDb;
    }

    /**
     * Setter for the attribute rrdFileName.
     * 
     * @param rrdFileName
     *            The attribute rrdFileName.
     */
    public void setRrdFileName(final String rrdFileName) {
        this.rrdFileName = rrdFileName;
    }

    /**
     * Sets the start time.
     * 
     * @param startTime
     *            the new start time
     */
    public void setStartTime(final long startTime) {
        this.startTime = startTime;
    }

    /**
     * Method.
     * 
     * @param s
     *            the s
     * @param key
     *            the key
     * @param value
     *            the value
     */
    public void update(final Sample s, final String key, final double value) {
        s.setValue(key, value);
    }

    /**
     * Method.
     * 
     * @param s
     *            the s
     * @param key
     *            the key
     * @param values
     *            the values
     */
    public void update(final Sample s, final String key, final long[] values) {
        for (long value : values) {
            s.setValue(key, value);
        }
    }
}
