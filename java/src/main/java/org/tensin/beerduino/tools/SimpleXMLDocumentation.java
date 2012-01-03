package org.tensin.beerduino.tools;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.tensin.beerduino.CoreException;
import org.tensin.beerduino.Preferences;


/**
 * The Class SimpleXMLDocumentation.
 *
 * @author j385649
 * @since 26 oct. 2011 14:40:52
 */
public class SimpleXMLDocumentation {
	
	/** Types. */
	private static LinkedHashMap<String,SimpleXMLDocumentationType> types;
	
	/** root. */
	private SimpleXMLDocumentationEntity root;
	
	/**
	 * Gets the root.
	 *
	 * @return the root
	 */
	public SimpleXMLDocumentationEntity getRoot() {
		return root;
	}

	/**
	 * Method.
	 *
	 */
	private static void initSimpleXMLDocumentation() {
		types = new LinkedHashMap<String,SimpleXMLDocumentationType>();
	}
	
	/**
	 * Constructor.
	 *
	 * @param clazz the clazz
	 */
	private SimpleXMLDocumentation(Class<?> clazz) {
		this.root = new SimpleXMLDocumentationEntity(clazz);
		registerType(clazz);
		root.parse();
	}
	
	/**
	 * Method.
	 *
	 * @param clazz the clazz
	 * @return the type
	 */
	static SimpleXMLDocumentationType getType(Class<?> clazz) {
		return SimpleXMLDocumentation.types.get(clazz.getName());
	}

	/**
	 * Method.
	 *
	 * @param pathName the path name
	 * @return the type
	 */
	static SimpleXMLDocumentationType getType(String pathName) {
		return SimpleXMLDocumentation.types.get(pathName);
	}
	
	/**
	 * Method.
	 *
	 * @param clazz the clazz
	 */
	static void registerType(Class<?> clazz) {
		SimpleXMLDocumentationType type = new SimpleXMLDocumentationType(clazz);
		SimpleXMLDocumentation.types.put(type.getName(),type);
	}
	
	/**
	 * Method.
	 *
	 * @param name the name
	 */
	static void registerType(String name) {
		SimpleXMLDocumentationType type = new SimpleXMLDocumentationType(name);
		SimpleXMLDocumentation.types.put(type.getName(),type);
	}
	
	/**
	 * Method.
	 *
	 */
	static void showAllTypes() {
		System.out.println("Liste des types");
		Iterator<String> itTypes = types.keySet().iterator();
		while(itTypes.hasNext()) {
			String typeName = itTypes.next();
			SimpleXMLDocumentationType type = types.get(typeName);
			System.out.println("TYPE ["+type.isPrimitive()+"] : "+typeName);
		}
	
	}
	
	/**
	 * Convert.
	 *
	 * @param racine the racine
	 * @param destination the destination
	 * @param converter the converter
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	public static void convert(final Class<?> racine, final String destination, final ISimpleXMLDocumentation converter) throws IOException, CoreException {
		initSimpleXMLDocumentation();
		SimpleXMLDocumentation sxmldoc = new SimpleXMLDocumentation(racine);
		affecteIndices();
		org.apache.commons.io.FileUtils.writeStringToFile(new File(destination), converter.generate(sxmldoc.getRoot()));
	}
	
	/**
	 * Method.
	 *
	 */
	private static void affecteIndices() {
		int indice = 0;
		Iterator<String> itTypes = types.keySet().iterator();
		while(itTypes.hasNext()) {
			SimpleXMLDocumentationType type = types.get(itTypes.next());
			if (!type.isPrimitive()) {
				indice++;
				type.setIndice(indice);
			}
		}
	}
	

	/**
	 * Method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		//SimpleXMLDocumentation sxmldoc = new SimpleXMLDocumentation(Usine.class);
		//System.out.println(sxmldoc.toString());
		//System.out.println(sxmldoc.toXML());
		//System.out.println(sxmldoc.toXSD());
		
		SimpleXMLDocumentation.convert(Preferences.class, "preferences.apt", new SimpleXMLDocumentationOutputAPT());
		SimpleXMLDocumentation.convert(Preferences.class, "preferences.xsd", new SimpleXMLDocumentationOutputXSD());
		// SimpleXMLDocumentation.toXSD(Usine.class, "../CDN-JAVA/site/apt/SimpleXML.xsd");
		
		//SimpleXMLDocumentation.toXSD(OccurrenceConfig.class, "../TEST/data/efl.xsd");
		//SimpleXMLDocumentation.toAPT(OccurrenceConfig.class, "../CDN-JAVA/site/apt/SimpleXML.apt");
	}
}
