package org.tensin.beerduino.tools;

/**
 * The Interface ISimpleXMLDocumentation.
 */
public interface ISimpleXMLDocumentationOutput {
	
	/**
	 * Generate.
	 *
	 * @param racine the racine
	 * @param entity the entity
	 * @return the string
	 * @throws CoreException the core exception
	 */
	String generate(final Class<?> racine, final SimpleXMLDocumentationEntity entity) throws SimpleXMLDocumentationException;

}
