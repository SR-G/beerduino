package org.tensin.beerduino.tools;

import org.apache.log4j.BasicConfigurator;
import org.tensin.beerduino.Preferences;
import org.tensin.beerduino.TemperatureResults;

public class DocumentationUpdater {

	/**
	 * Method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		BasicConfigurator.configure();
		SimpleXMLDocumentation.convert(TemperatureResults.class, "src/main/java/org/tensin/beerduino/xsd/temperatureresults.xsd", new SimpleXMLDocumentationOutputXSD());
		SimpleXMLDocumentation.convert(Preferences.class, "src/main/java/org/tensin/beerduino/xsd/preferences.xsd", new SimpleXMLDocumentationOutputXSD());

		SimpleXMLDocumentation.convert(Preferences.class, "preferences.apt", new SimpleXMLDocumentationOutputAPT());
		SimpleXMLDocumentation.convert(TemperatureResults.class, "temperatureresults.apt", new SimpleXMLDocumentationOutputAPT());
	}
}
