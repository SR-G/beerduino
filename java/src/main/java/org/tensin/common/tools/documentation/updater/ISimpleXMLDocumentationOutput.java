package org.tensin.common.tools.documentation.updater;

/**
 * The Interface ISimpleXMLDocumentation.
 */
public interface ISimpleXMLDocumentationOutput {

    /**
     * Generate.
     * 
     * @param racine
     *            the racine
     * @param entity
     *            the entity
     * @return the string
     * @throws SimpleXMLDocumentationException
     *             the simple xml documentation exception
     */
    String generate(final Class<?> racine, final SimpleXMLDocumentationEntity entity) throws SimpleXMLDocumentationException;

}
