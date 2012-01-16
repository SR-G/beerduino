package org.tensin.common.tools.documentation.updater;

import java.util.TreeSet;

/**
 * The Class SimpleXMLDocumentationOutputWiki.
 */
public class SimpleXMLDocumentationOutputMarkdown extends SimpleXMLDocumentationOutputAPT implements ISimpleXMLDocumentationOutput {

    /*
     * (non-Javadoc)
     * 
     * @see org.tensin.common.tools.documentation.updater.ISimpleXMLDocumentationOutput#generate(java.lang.Class, org.tensin.common.tools.documentation.updater.SimpleXMLDocumentationEntity)
     */
    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.SimpleXMLDocumentationOutputAPT#generate(java.lang.Class, org.tensin.common.tools.documentation.updater.SimpleXMLDocumentationEntity)
      */
    @Override
    public String generate(final Class<?> racine, final SimpleXMLDocumentationEntity entity) throws SimpleXMLDocumentationException {
        setSyntaxer(new SyntaxerMarkdown());
        StringBuilder sb = new StringBuilder();
        // sb.append("(" + getCurrentDateWithDateFormat("yyyy-MM-dd HH:mm:ss") + ")\n\n");
        // sb.append(summaryInAPT(1,entity,1));
        sb.append(format(entity, new TreeSet<String>()));
        return sb.toString();
    }
}