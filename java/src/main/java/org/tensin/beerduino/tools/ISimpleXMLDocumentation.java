package org.tensin.beerduino.tools;

import org.tensin.beerduino.CoreException;

/**
 * The Interface ISimpleXMLDocumentation.
 */
public interface ISimpleXMLDocumentation {
	
	/**
	 * Generate.
	 *
	 * @param entity the entity
	 * @return the string
	 * @throws CoreException the core exception
	 */
	String generate(final SimpleXMLDocumentationEntity entity) throws CoreException;

}
