package org.tensin.beerduino.notifications;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.rrd4j.core.Util;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.beerduino.Beerduino;
import org.tensin.beerduino.CoreException;
import org.tensin.beerduino.RRDTemperature;
import org.tensin.beerduino.Recipient;
import org.tensin.beerduino.TemperatureResults;
import org.tensin.beerduino.TemperatureState;
import org.tensin.beerduino.TemplatedGraph;
import org.tensin.beerduino.VelociMail;

/**
 * The Class MailNotification.
 */
@Root
public class MailNotification implements INotification {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MailNotification.class);

    /** The recipients. */
    @ElementList(name = "destinataires", required = true)
    public Collection<Recipient> recipients = new ArrayList<Recipient>();

    /** The smtp hostname. */
    @Attribute(required = false)
    public String smtpHostname = "smtp.gmail.com";

    /** The smtp login. */
    @Attribute(required = false)
    public String smtpLogin = "beerduino@gmail.com";

    /** The smtp password. */
    @Attribute(required = false)
    public String smtpPassword = "duino1234"; // should be put in configuration file

    /** The smtp port. */
    @Attribute(required = false)
    public int smtpPort = 587;

    /**
     * Instantiates a new mail notification.
     */
    public MailNotification() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.tensin.beerduino.notifications.INotification#execute(org.tensin.beerduino.TemperatureResults)
     */
    @Override
    public void execute(final TemperatureResults results) throws CoreException {
        LOGGER.info("Sending mail notification");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpHostname);
        props.put("mail.smtp.starttls.enable", "true");

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

        RRDTemperature rrd = Beerduino.getInstance().getRrd();

        try {

            if (StringUtils.isEmpty(smtpPassword)) {
                LOGGER.warn("Password is blank !");
            }

            HtmlEmail email = new HtmlEmail();
            email.setSmtpPort(smtpPort);
            email.setAuthenticator(new DefaultAuthenticator(smtpLogin, smtpPassword));
            // email.setDebug(true);
            email.setHostName(smtpHostname);
            email.setCharset("UTF-8");
            email.getMailSession().getProperties().put("mail.smtps.auth", "true");
            email.getMailSession().getProperties().put("mail.debug", "true");
            email.getMailSession().getProperties().put("mail.smtps.port", String.valueOf(smtpPort));
            email.getMailSession().getProperties().put("mail.smtps.socketFactory.port", String.valueOf(smtpPort));
            email.getMailSession().getProperties().put("mail.smtps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            email.getMailSession().getProperties().put("mail.smtps.socketFactory.fallback", "false");
            email.getMailSession().getProperties().put("mail.smtp.starttls.enable", "true");
            email.setFrom(smtpLogin, "Mr. Beer Duino");
            email.setSubject("Beerduino mail report [" + sdf.format(d) + "]");

            String graphFilename = rrd.graph(Beerduino.getInstance().getPreferences().getWorkDir() + "temperatures", rrd.getRrdDb().getArchive(0)
                    .getStartTime(), Util.getTime(), "Temperatures");

            TemplatedGraph graph = new TemplatedGraph();
            if (graphFilename.startsWith("/")) {
                graphFilename = "file://" + graphFilename; // cas où les fichiers générés ont un path complet
            } else {
                graphFilename = "file:///" + System.getProperty("user.dir") + File.separator + graphFilename; // cas où les fichiers générés ont un path
                                                                                                              // relatifs
            }
            String cid = email.embed(new URL(graphFilename), "Temperatures"); // graphFileName est supposé avoir un path complet
            graph.setCid(cid);
            graph.setLabel("Temperatures");
            graph.setHeight(String.valueOf((int) (RRDTemperature.GRAPH_IMAGE_HEIGHT * 0.75)));
            graph.setWidth(String.valueOf((int) (RRDTemperature.GRAPH_IMAGE_WIDTH * 0.75)));

            VelociMail layout = new VelociMail();
            layout.setTemplate("org/tensin/beerduino/templates/mail/mail-notification.vm");
            layout.setLayout("org/tensin/beerduino/templates/mail/");
            layout.addContext("charset", "UTF-8");
            layout.addContext("results", results.getResults());
            layout.addContext("graph", graph);
            if (results.getState().compareTo(TemperatureState.OVERHEAT) == 0) {
                layout.addContext("explanation", "Une ou plusieurs sondes dépassent la température définie en seuil !");
            } else {
                layout.addContext("explanation", "Toutes les sondes sont à présent revenues sous les limites.");
            }

            email.setMsg(layout.render(email));
            for (Recipient recipient : recipients) {
                email.addTo(recipient.getEmail(), recipient.getName());
            }
            email.setTLS(true);
            email.send();
        } catch (EmailException e) {
            throw new CoreException(e);
        } catch (MalformedURLException e) {
            throw new CoreException(e);
        } catch (IOException e) {
            throw new CoreException(e);
        }

        /*
         * try {
         * Session session = Session.getDefaultInstance(props);
         * // session.setDebug(true);
         * 
         * // Needs javaee-api.jar
         * MimeMessage message = new MimeMessage(session);
         * message.setFrom(new InternetAddress(smtpLogin));
         * message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
         * message.setSubject("Beerduino");
         * message.setText("Test");
         * 
         * Transport transport = session.getTransport("smtp");
         * transport.connect(smtpHostname, smtpPort, smtpLogin, smtpPassword);
         * message.saveChanges();
         * 
         * transport.sendMessage(message, message.getAllRecipients());
         * transport.close();
         * } catch (MessagingException e) {
         * throw new CoreException(e);
         * }
         */
    }
}
