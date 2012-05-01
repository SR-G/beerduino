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

                            /*
                             * public static void main(String arsg[]) throws InterruptedException{
                             * try {
                             * //Creates a Pachube object authenicated using the provided API KEY
                             * 
                             * Pachube p = new Pachube(API KEY HERE);
                             * Feed f = new Feed();
                             * f.setTitle("JAVA API TEST FEED");
                             * Data a = new Data();
                             * a.setId(0);
                             * a.setMaxValue(100d);
                             * a.setMinValue(0d);
                             * a.setTag("Test");
                             * a.setValue(30d);
                             * f.addData(a);
                             * Feed g = p.createFeed(f);
                             * //The Feed 'f' is does not represent the feed on pachube any
                             * // Changes made to this object will not alter the online feed.
                             * System.out.println("The id of the new feed is:");
                             * System.out.println(g.getId());
                             * 
                             * } catch (PachubeException e) {
                             * //If an exception occurs it will print the error message from the failed
                             * // HTTP command
                             * System.out.println(e.errorMessage);
                             * }
                             * }
                             * 
                             * }
                             * To update an existing feed's datastream the example code is shown below; This code will update the feed with id 2993
                             * 
                             * try {
                             * Pachube p = new Pachube(API KEY HERE);
                             * Feed f = p.getFeed(2993);
                             * f.updateDatastream(0, 90d);
                             * } catch (PachubeException e) {
                             * System.out.println(e.errorMessage);
                             * }
                             * In this API Triggers are modeled using the Trigger class which is capable of forming the correct HTTP headers. The code below create a Trigger on the feed 2993 datastream 0 and will post to the specified URL
                             * if the streams value is greater than 10.
                             * 
                             * try {
                             * Pachube p = new Pachube(APU_KEY);
                             * Trigger t = new Trigger();
                             * t.setEnv_id(2993);
                             * t.setStream_id(0);
                             * t.setThreshold(10d);
                             * t.setType(TriggerType.gt);
                             * 
                             * t.setUrl(new URL("http://google.com"));
                             * 
                             * p.createTrigger(t);
                             * } catch (PachubeException e) {
                             * // TODO Auto-generated catch block
                             * e.printStackTrace();
                             * } catch (MalformedURLException e) {
                             * // TODO Auto-generated catch block
                             * e.printStackTrace();
                             * }
                             */

                        }
                    }
                }
            }
        } finally {

        }
    }
}
