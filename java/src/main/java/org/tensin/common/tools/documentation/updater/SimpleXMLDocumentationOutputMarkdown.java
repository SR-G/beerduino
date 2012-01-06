package org.tensin.common.tools.documentation.updater;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

/**
 * The Class SimpleXMLDocumentationOutputWiki.
 */
public class SimpleXMLDocumentationOutputMarkdown implements ISimpleXMLDocumentationOutput {

    /**
     * Méthode getCurrentDateWithDateFormat.
     * 
     * @param pattern
     *            the pattern
     * @return current date with date format
     *         String
     */
    public static String getCurrentDateWithDateFormat(String pattern) {
        return getDateWithDateFormat(pattern, new Date());
    }

    /**
     * Méthode getDateWithDateFormat.
     * 
     * @param pattern
     *            the pattern
     * @param date
     *            the date
     * @return the date with date format
     *         String
     */
    public static String getDateWithDateFormat(String pattern, Date date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.tensin.common.tools.documentation.updater.ISimpleXMLDocumentationOutput#generate(java.lang.Class, org.tensin.common.tools.documentation.updater.SimpleXMLDocumentationEntity)
     */
    @Override
    public String generate(final Class<?> racine, final SimpleXMLDocumentationEntity entity) throws SimpleXMLDocumentationException {
        StringBuilder sb = new StringBuilder("Documentation");
        sb.append("(" + getCurrentDateWithDateFormat("yyyy-MM-dd HH:mm:ss") + ")\n\n");
        // sb.append(summaryInAPT(1,entity,1));
        sb.append(toMarkDown(entity, new TreeSet<String>()));

        return sb.toString();
    }

    /**
     * Method.
     * 
     * @param entity
     *            the entity
     * @return the link
     */
    private String getLink(final SimpleXMLDocumentationEntity entity) {
        if (entity.isPrimitive()) {
            return entity.getType().getSimpleName();
        } else {
            return "{{{" + entity.getType().getAnchor() + "}" + entity.getType().getBaliseName() + "}}";
        }
    }

    /**
     * Method.
     * 
     * @param entity
     *            the entity
     * @return the required
     */
    private String getRequired(final SimpleXMLDocumentationEntity entity) {
        if (entity.isRequired()) {
            return " X ";
        } else {
            return "   ";
        }
    }

    /**
     * Method.
     * 
     * @param currentEntity
     *            the current entity
     * @param alreadyDisplayed
     *            the already displayed
     * @return the string
     */
    public String toMarkDown(final SimpleXMLDocumentationEntity currentEntity, final Set<String> alreadyDisplayed) {
        /* Doublons */
        // currentEntity.checkDoublons(currentEntity.getType().getBaliseName(), currentEntity.getType().getName());

        /* Titre */
        StringBuffer sb = new StringBuffer();
        sb.append(SyntaxMarkdownHelper.buildLink(currentEntity.getType().getAnchor())).append("\n\n");
        sb.append("    " + currentEntity.getType().getDescription() + "\n\n");

        /* Exemple */
        StringBuffer sbExemple = new StringBuffer("* Exemple\n\n");
        sbExemple.append("+--------------------------------------------------------------------------------+\n");
        sbExemple.append("<" + currentEntity.getType().getBaliseName());

        /* Enum */
        if (currentEntity.getType().isEnum()) {
            StringBuffer sbEnum = new StringBuffer("* Enumeration\n\n");
            sbEnum.append("*-----------------+\n");
            sbEnum.append("| <<Valeur>>      |\n");
            sbEnum.append("*-----------------+\n");
            List<String> enumerations = currentEntity.getType().getEnumeration();
            for (String enumeration : enumerations) {
                sbEnum.append("| " + enumeration + "|\n");
                sbEnum.append("*-----------------+\n");
            }
            sb.append(sbEnum + "\n\n");
        }

        /* Attributs */
        int initialAttributeLineSize = ("<" + currentEntity.getType().getBaliseName()).length();
        int currentAttributeLineSize = initialAttributeLineSize;
        String currentAttributeLine = "";
        int nbAttributs = 0;
        StringBuffer sbAttributs = new StringBuffer("* Attributs\n\n");
        sbAttributs.append("*-----------------+-------------------+-------*-------------------+\n");
        sbAttributs.append("| <<Attributs>>   | <<Type>>          |<<Req>>| <<Description>>   |\n");
        sbAttributs.append("*-----------------+-------------------+-------*-------------------+\n");
        Iterator<String> itEntities = currentEntity.getEntities().keySet().iterator();
        while (itEntities.hasNext()) {
            String name = itEntities.next();
            SimpleXMLDocumentationEntity entity = currentEntity.getEntities().get(name);
            if (entity.isAttribute()) {
                nbAttributs++;
                sbAttributs.append("| " + entity.getBaliseName() + " | " + getLink(entity) + " | " + getRequired(entity) + " | " + entity.getDescription() + " |\n");
                sbAttributs.append("*-----------------+-------------------+-------*-------------------+\n");

                String attributeExemple = " " + entity.getBaliseName() + "='...'";
                if (currentAttributeLineSize + attributeExemple.length() > 80) {
                    sbExemple.append(currentAttributeLine + "\n");
                    currentAttributeLine = StringUtils.repeat(" ", initialAttributeLineSize);
                    currentAttributeLineSize = initialAttributeLineSize;
                }
                currentAttributeLine += attributeExemple;
                currentAttributeLineSize += attributeExemple.length();

            }
        }
        sbExemple.append(currentAttributeLine + ">");
        if (nbAttributs > 0) {
            sb.append(sbAttributs + "\n\n");
        }

        /* Element */
        int nbElements = 0;
        StringBuffer sbElements = new StringBuffer("* Elements\n\n");
        sbElements.append("*-----------------+-------------------+-------*-------------------+\n");
        sbElements.append("| <<Elements>>    | <<Type>>          |<<Req>>| <<Description>>   |\n");
        sbElements.append("*-----------------+-------------------+-------*-------------------+\n");
        itEntities = currentEntity.getEntities().keySet().iterator();
        while (itEntities.hasNext()) {
            String name = itEntities.next();
            SimpleXMLDocumentationEntity entity = currentEntity.getEntities().get(name);
            if (entity.isElement()) {
                nbElements++;
                sbElements.append("| " + entity.getBaliseName() + " | " + getLink(entity) + " | " + getRequired(entity) + " | " + entity.getDescription() + " |\n");
                sbElements.append("*-----------------+-------------------+-------*-------------------+\n");
                sbExemple.append("\n   <!-- Description de '" + entity.getBaliseName() + "' -->");
                sbExemple.append("\n   <" + entity.getBaliseName() + ">");
                if (entity.isPrimitive()) {
                    sbExemple.append(". . .");
                } else {
                    sbExemple.append("\n   . . .\n   ");
                }
                sbExemple.append("</" + entity.getBaliseName() + ">\n");
            } else if (entity.isElementList()) {
                nbElements++;
                if (entity.isInline()) {
                    sbElements.append("| Liste de " + getLink(entity) + " | " + getLink(entity) + " | " + getRequired(entity) + " | " + entity.getDescription() + " |\n");
                    sbExemple.append("\n   <!-- Liste de '" + entity.getType().getBaliseName() + "' -->");
                    sbExemple.append("\n   <" + entity.getType().getBaliseName() + ">");
                    sbExemple.append("\n   . . .");
                    sbExemple.append("\n   </" + entity.getType().getBaliseName() + ">");
                    sbExemple.append("\n   .");
                    sbExemple.append("\n   .");
                    sbExemple.append("\n   .");
                    sbExemple.append("\n   <" + entity.getType().getBaliseName() + ">");
                    sbExemple.append("\n   . . .");
                    sbExemple.append("\n   </" + entity.getType().getBaliseName() + ">\n");
                } else {
                    sbElements.append("| " + entity.getBaliseName() + " | Liste de " + getLink(entity) + " | " + getRequired(entity) + " | " + entity.getDescription() + " |\n");
                    sbExemple.append("\n   <!-- Description de '" + entity.getBaliseName() + "' -->");
                    sbExemple.append("\n   <" + entity.getBaliseName() + ">");
                    sbExemple.append("\n   . . .");
                    sbExemple.append("\n   </" + entity.getBaliseName() + ">\n");
                }
                sbElements.append("*-----------------+-------------------+---+-------------------+\n");
            }
        }
        if (nbElements > 0) {
            sb.append(sbElements + "\n\n");
        }

        /* Text */
        int nbTexts = 0;
        StringBuffer sbTexts = new StringBuffer("* Contenu\n\n");
        sbTexts.append("*-------------------+-------*-------------------+\n");
        sbTexts.append("| <<Type>>          |<<Req>>| <<Description>>   |\n");
        sbTexts.append("*-------------------+-------*-------------------+\n");
        itEntities = currentEntity.getEntities().keySet().iterator();
        while (itEntities.hasNext()) {
            String name = itEntities.next();
            SimpleXMLDocumentationEntity entity = currentEntity.getEntities().get(name);
            if (entity.isText()) {
                nbTexts++;
                sbTexts.append("| " + getLink(entity) + " | " + getRequired(entity) + " | " + entity.getDescription() + " |\n");
                sbTexts.append("*-------------------+-------*-------------------+\n");
                sbExemple.append("...");
            }
        }
        if (nbTexts > 0) {
            sb.append(sbTexts + "\n\n");
        }

        /* Fin de l'exemple */
        if (nbElements == 0 && nbTexts == 0) {
            sbExemple.deleteCharAt(sbExemple.length() - 1).append(" />\n");
        } else {
            sbExemple.append("</" + currentEntity.getType().getBaliseName() + ">\n");
        }
        sbExemple.append("+--------------------------------------------------------------------------------+\n\n");
        if (!currentEntity.getType().isEnum()) {
            sb.append(sbExemple);
        }

        /* Classe d'implementation */
        if (currentEntity.getType().getName().indexOf(".") > -1) {
            String fullName = currentEntity.getType().getName();
            String packageName = fullName.substring(0, fullName.lastIndexOf("."));
            String className = fullName.substring(fullName.lastIndexOf(".") + 1);
            sb.append("* Implémentation\n\n");
            sb.append("*----------+---------------------------------------------------------------+\n");
            sb.append("| Package  | " + packageName + " |\n");
            sb.append("*----------+---------------------------------------------------------------+\n");
            sb.append("| Classe   | " + className + " |\n");
            sb.append("*----------+---------------------------------------------------------------+\n\n");
        }

        /* Documentation */
        String documentation = currentEntity.getType().getDocumentation();
        if (StringUtils.isNotBlank(documentation)) {
            StringBuffer sbDocumentation = new StringBuffer("* Documentation\n\n");
            sbDocumentation.append("    Une documentation complète est disponible {{{" + documentation + "}ici}}\n");
            sb.append(sbDocumentation);
        }

        /* Entités suivantes.... */
        itEntities = currentEntity.getEntities().keySet().iterator();
        while (itEntities.hasNext()) {
            String name = itEntities.next();
            SimpleXMLDocumentationEntity subEntity = currentEntity.getEntities().get(name);
            if (!subEntity.isPrimitive() && !alreadyDisplayed.contains(subEntity.getTypeName())) {
                alreadyDisplayed.add(subEntity.getTypeName());
                sb.append("\n\n\n" + toMarkDown(subEntity, alreadyDisplayed));

            }
        }

        return sb.toString();
    }

}

// https://github.com/mojombo/github-flavored-markdown/issues/1
// http://daringfireball.net/projects/markdown/syntax

/*
 * 
 * GitHub Flavored Markdown
 * ================================
 * 
 * View the [source of this content](http://github.github.com/github-flavored-markdown/sample_content.html).*
 * 
 * Let's get the whole "linebreak" thing out of the way. The next paragraph contains two phrases separated by a single newline character:
 * 
 * Roses are red
 * Violets are blue
 * 
 * The next paragraph has the same phrases, but now they are separated by two spaces and a newline character:
 * 
 * Roses are red
 * Violets are blue
 * 
 * Oh, and one thing I cannot stand is the mangling of words with multiple underscores in them like perform_complicated_task or do_this_and_do_that_and_another_thing.
 * 
 * A bit of the GitHub spice
 * -------------------------
 * 
 * In addition to the changes in the previous section, certain references are auto-linked:
 * 
 * SHA: be6a8cc1c1ecfe9489fb51e4869af15a13fc2cd2
 * User@SHA ref: mojombo@be6a8cc1c1ecfe9489fb51e4869af15a13fc2cd2
 * User/Project@SHA: mojombo/god@be6a8cc1c1ecfe9489fb51e4869af15a13fc2cd2
 * \#Num: #1
 * User/#Num: mojombo#1
 * User/Project#Num: mojombo/god#1
 * 
 * These are dangerous goodies though, and we need to make sure email addresses don't get mangled:
 * 
 * My email addy is tom@github.com.
 * 
 * Math is hard, let's go shopping
 * -------------------------------
 * 
 * In first grade I learned that 5 > 3 and 2 < 7. Maybe some arrows. 1 -> 2 -> 3. 9 <- 8 <- 7.
 * 
 * Triangles man! a^2 + b^2 = c^2
 * 
 * We all like making lists
 * ------------------------
 * 
 * The above header should be an H2 tag. Now, for a list of fruits:
 * 
 * Red Apples
 * Purple Grapes
 * Green Kiwifruits
 * 
 * Let's get crazy:
 * 
 * 1. This is a list item with two paragraphs. Lorem ipsum dolor
 * sit amet, consectetuer adipiscing elit. Aliquam hendrerit
 * mi posuere lectus.
 * 
 * Vestibulum enim wisi, viverra nec, fringilla in, laoreet
 * vitae, risus. Donec sit amet nisl. Aliquam semper ipsum
 * sit amet velit.
 * 
 * 2. Suspendisse id sem consectetuer libero luctus adipiscing.
 * 
 * What about some code **in** a list? That's insane, right?
 * 
 * 1. In Ruby you can map like this:
 * 
 * ['a', 'b'].map { |x| x.uppercase }
 * 
 * 2. In Rails, you can do a shortcut:
 * 
 * ['a', 'b'].map(&:uppercase)
 * 
 * Some people seem to like definition lists
 * 
 * <dl>
 * <dt>Lower cost</dt>
 * <dd>The new version of this product costs significantly less than the previous one!</dd>
 * <dt>Easier to use</dt>
 * <dd>We've changed the product so that it's much easier to use!</dd>
 * </dl>
 * 
 * I am a robot
 * ------------
 * 
 * Maybe you want to print `robot` to the console 1000 times. Why not?
 * 
 * def robot_invasion
 * puts("robot " * 1000)
 * end
 * 
 * You see, that was formatted as code because it's been indented by four spaces.
 * 
 * How about we throw some angle braces and ampersands in there?
 * 
 * <div class="footer">
 * &copy; 2004 Foo Corporation
 * </div>
 * 
 * Set in stone
 * ------------
 * 
 * Preformatted blocks are useful for ASCII art:
 * 
 * <pre>
 * ,-.
 * , ,-. ,-.
 * / \ ( )-( )
 * \ | ,.>-( )-<
 * \|,' ( )-( )
 * Y ___`-' `-'
 * |/__/ `-'
 * |
 * |
 * | -hrr-
 * ___|_____________
 * </pre>
 * 
 * Playing the blame game
 * ----------------------
 * 
 * If you need to blame someone, the best way to do so is by quoting them:
 * 
 * > I, at any rate, am convinced that He does not throw dice.
 * 
 * Or perhaps someone a little less eloquent:
 * 
 * > I wish you'd have given me this written question ahead of time so I
 * > could plan for it... I'm sure something will pop into my head here in
 * > the midst of this press conference, with all the pressure of trying to
 * > come up with answer, but it hadn't yet...
 * >
 * > I don't want to sound like
 * > I have made no mistakes. I'm confident I have. I just haven't - you
 * > just put me under the spot here, and maybe I'm not as quick on my feet
 * > as I should be in coming up with one.
 * 
 * Table for two
 * -------------
 * 
 * <table>
 * <tr>
 * <th>ID</th><th>Name</th><th>Rank</th>
 * </tr>
 * <tr>
 * <td>1</td><td>Tom Preston-Werner</td><td>Awesome</td>
 * </tr>
 * <tr>
 * <td>2</td><td>Albert Einstein</td><td>Nearly as awesome</td>
 * </tr>
 * </table>
 * 
 * Crazy linking action
 * --------------------
 * 
 * I get 10 times more traffic from [Google] [1] than from
 * [Yahoo] [2] or [MSN] [3].
 * 
 * [1]: http://google.com/ "Google"
 * [2]: http://search.yahoo.com/ "Yahoo Search"
 * [3]: http://search.msn.com/ "MSN Search"
 */