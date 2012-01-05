package org.tensin.beerduino.run;

import org.apache.log4j.BasicConfigurator;
import org.tensin.beerduino.Preferences;
import org.tensin.beerduino.TemperatureResults;
import org.tensin.common.tools.documentation.updater.SimpleXMLDocumentation;
import org.tensin.common.tools.documentation.updater.SimpleXMLDocumentationOutputAPT;
import org.tensin.common.tools.documentation.updater.SimpleXMLDocumentationOutputXSD;

/**
 * The Class UpdateDocumentation.
 */
public class UpdateDocumentation {

    /**
     * Method.
     * 
     * @param args
     *            the arguments
     * @throws Exception
     *             the exception
     */
    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        SimpleXMLDocumentation.convert(TemperatureResults.class, "src/main/java/org/tensin/beerduino/xsd/temperatureresults.xsd", new SimpleXMLDocumentationOutputXSD());
        SimpleXMLDocumentation.convert(Preferences.class, "src/main/java/org/tensin/beerduino/xsd/preferences.xsd", new SimpleXMLDocumentationOutputXSD());

        SimpleXMLDocumentation.convert(Preferences.class, "preferences.apt", new SimpleXMLDocumentationOutputAPT());
        SimpleXMLDocumentation.convert(TemperatureResults.class, "temperatureresults.apt", new SimpleXMLDocumentationOutputAPT());
    }
}
