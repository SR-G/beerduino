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

    /**
     * Checks if is mode merge.
     *
     * @return true, if is mode merge
     */
    boolean isModeMerge();

    /**
     * Method.
     *
     * @param racine the racine
     * @param entity the entity
     * @param sourceFileContent the source file content
     * @return the string
     * @throws SimpleXMLDocumentationException the simple xml documentation exception
     */
    String mergeContent(final Class<?> racine, final SimpleXMLDocumentationEntity entity, final String sourceFileContent) throws SimpleXMLDocumentationException;
}
