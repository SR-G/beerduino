package org.tensin.beerduino.notifications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.simpleframework.xml.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.beerduino.CoreException;
import org.tensin.beerduino.TemperatureResult;
import org.tensin.beerduino.TemperatureResults;
import org.tensin.beerduino.TemperatureState;
import org.tensin.beerduino.helpers.CloseHelper;

public class PushToNotification extends URLNotification implements INotification {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushToNotification.class);

    @Attribute(name = "pushto-url", required = false)
    private String pushtoUrl = "http://pushme.to/";

    @Attribute(name = "id")
    private String pushtoId;

    @Attribute(name = "signature", required = false)
    private String pushtoSignature = "beerduino";

    @Override
    public void execute(final TemperatureResults results) throws CoreException {

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
            if (returnCode == HttpStatus.NOT_IMPLEMENTED_501) {
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

    public String getPushtoId() {
        return pushtoId;
    }

    public String getPushtoSignature() {
        return pushtoSignature;
    }

    public String getPushtoUrl() {
        return pushtoUrl;
    }

    private String getUrl() {
        return getPushtoUrl() + getPushtoId() + "/";
    }

    public void setPushtoId(final String pushtoId) {
        this.pushtoId = pushtoId;
    }

    public void setPushtoSignature(final String pushtoSignature) {
        this.pushtoSignature = pushtoSignature;
    }

    public void setPushtoUrl(final String pushtoUrl) {
        this.pushtoUrl = pushtoUrl;
    }

}
