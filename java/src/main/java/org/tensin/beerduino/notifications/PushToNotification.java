package org.tensin.beerduino.notifications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.PostMethod;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.beerduino.TemperatureResult;
import org.tensin.beerduino.TemperatureResults;
import org.tensin.beerduino.TemperatureState;
import org.tensin.common.CoreException;
import org.tensin.common.helpers.CloseHelper;
import org.tensin.common.tools.documentation.updater.Description;

/**
 * The Class PushToNotification.
 * Use : http://pushme.to (iOS only)
 * 
 */
@Root(name = "push")
@Description("Notification by activating a PushTo notification.")
public class PushToNotification extends URLNotification implements INotification {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PushToNotification.class);

    /** The pushto url. */
    @Attribute(name = "pushto-url", required = false)
    @Description("The push-to URL to use. Something like 'http://pushme.to/'.")
    private String pushtoUrl = "http://pushme.to/";

    /** The pushto id. */
    @Attribute(name = "id")
    @Description("Your pushTo ID (login)")
    private String pushtoId;

    /** The pushto signature. */
    @Attribute(name = "signature", required = false)
    @Description("Signature that will be shown in the pushto notification sent.")
    private String pushtoSignature = "beerduino";

    /*
     * (non-Javadoc)
     * 
     * @see org.tensin.beerduino.notifications.URLNotification#execute(org.tensin.beerduino.TemperatureResults)
     */
    @Override
    public void execute(final TemperatureResults results) throws CoreException {
        LOGGER.info("Sending PushTo notification to [" + pushtoId + "]");

        StringBuilder sb = new StringBuilder();
        if (results.getState().compareTo(TemperatureState.OVERHEAT) == 0) {
            sb.append("Dépassement de température !");
        } else {
            sb.append("Températures back to normal");
        }
        sb.append(" (");
        int cnt = 0;
        for (TemperatureResult result : results.getResults()) {
            if (cnt > 0) {
                sb.append(",");
            }
            sb.append(result.getTemperature());
            cnt++;
        }
        sb.append(")");

        HttpClient client = new HttpClient();
        // client.getParams().setParameter("http.useragent", "Test Client");
        client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        client.getParams().setParameter("http.socket.timeout", new Integer(1000));
        client.getParams().setParameter("http.protocol.content-charset", "UTF-8");

        BufferedReader br = null;

        PostMethod method = new PostMethod(getUrl());
        method.addParameter("message", sb.toString());
        method.addParameter("signature", getPushtoSignature());

        try {
            int returnCode = client.executeMethod(method);
            if (returnCode == NOT_IMPLEMENTED_501) {
                LOGGER.error("The Post method is not implemented by this URI");
                // still consume the response body
                method.getResponseBodyAsString();
            } else {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String readLine;
                sb = new StringBuilder();
                while (((readLine = br.readLine()) != null)) {
                    sb.append(readLine);
                }
                LOGGER.info(sb.toString());
            }
        } catch (HttpException e) {
            LOGGER.error("Error while pushing to [" + getUrl() + "]", e);
        } catch (IOException e) {
            LOGGER.error("Error while pushing to [" + getUrl() + "]", e);
        } finally {
            method.releaseConnection();
            CloseHelper.close(br);
        }

    }

    /**
     * Gets the pushto id.
     * 
     * @return the pushto id
     */
    public String getPushtoId() {
        return pushtoId;
    }

    /**
     * Gets the pushto signature.
     * 
     * @return the pushto signature
     */
    public String getPushtoSignature() {
        return pushtoSignature;
    }

    /**
     * Gets the pushto url.
     * 
     * @return the pushto url
     */
    public String getPushtoUrl() {
        return pushtoUrl;
    }

    /**
     * Gets the url.
     * 
     * @return the url
     */
    private String getUrl() {
        return getPushtoUrl() + getPushtoId() + "/";
    }

    /**
     * Sets the pushto id.
     * 
     * @param pushtoId
     *            the new pushto id
     */
    public void setPushtoId(final String pushtoId) {
        this.pushtoId = pushtoId;
    }

    /**
     * Sets the pushto signature.
     * 
     * @param pushtoSignature
     *            the new pushto signature
     */
    public void setPushtoSignature(final String pushtoSignature) {
        this.pushtoSignature = pushtoSignature;
    }

    /**
     * Sets the pushto url.
     * 
     * @param pushtoUrl
     *            the new pushto url
     */
    public void setPushtoUrl(final String pushtoUrl) {
        this.pushtoUrl = pushtoUrl;
    }

}
