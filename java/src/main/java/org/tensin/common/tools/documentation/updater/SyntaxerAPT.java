package org.tensin.common.tools.documentation.updater;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class SyntaxAPTHelper.
 */
public class SyntaxerAPT implements ISyntaxer {

    /** Line delimiter. */
    private static final char LINE_DELIMITER = '-';

    /** Line delimiter. */
    private static final String LINE_SEPARATOR = "\n";

    /** Column separator. */
    private static final String COLUMN_SEPARATOR = "|";

    /** Pad size pour la mise au centre. */
    private static final int CENTER_PAD_SIZE = 3;

    /**
     * Fonction decentrage d'une chaine.
     * 
     * @param value
     *            String
     * @param rightPadSize
     *            int
     * @return String
     */
    private static String center(final String value, final int rightPadSize) {
        String res = value;
        if (res == null) {
            res = "";
        }
        int ecart = rightPadSize - res.length();
        if (ecart > 1) {
            int offset = ecart / 2;
            res = StringUtils.leftPad(res, res.length() + offset);
        }
        return StringUtils.rightPad(res, rightPadSize);
    }

    /** The current column counts. */
    private int currentColumnCounts;

    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.ISyntaxer#buildCommentEnd(java.lang.String)
      */
    @Override
    public String buildCommentEnd(final String comment) {
        return "~~BEGIN_" + (StringUtils.isNotEmpty(comment) ? comment : "");
    }

    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.ISyntaxer#buildCommentStart(java.lang.String)
      */
    @Override
    public String buildCommentStart(final String comment) {
        return "~~END_" + (StringUtils.isNotEmpty(comment) ? comment : "");
    }

    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.ISyntaxer#buildFileHeader(java.lang.String, java.lang.String)
      */
    @Override
    public String buildFileHeader(final String title, final String author) {
        StringBuilder sb = new StringBuilder();
        sb.append("                                ------").append(LINE_SEPARATOR);
        sb.append("                                ").append(title).append(".").append(LINE_SEPARATOR);
        sb.append("                                ------").append(LINE_SEPARATOR);
        sb.append("                                ").append(author).append(".").append(LINE_SEPARATOR);
        sb.append("                                ------").append(LINE_SEPARATOR);
        sb.append("                                ").append(new Date()).append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        return sb.toString();
    }

    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.ISyntaxer#buildLink(java.lang.String)
      */
    @Override
    public String buildLink(final String value) {
        return "{" + value + "}";
    }

    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.ISyntaxer#buildLink(java.lang.String, java.lang.String)
      */
    @Override
    public String buildLink(final String text, final String anchor) {
        return "{{{" + anchor + "}" + text + "}}";
    }

    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.ISyntaxer#buildQuoteEnd()
      */
    @Override
    public String buildQuoteEnd() {
        return "+--------------------------------------------------------------------------------+" + LINE_SEPARATOR;
    }

    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.ISyntaxer#buildQuoteStart()
      */
    @Override
    public String buildQuoteStart() {
        return "+--------------------------------------------------------------------------------+" + LINE_SEPARATOR;
    }

    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.ISyntaxer#buildTableFooter()
      */
    @Override
    public String buildTableFooter() {
        return "";
    }

    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.ISyntaxer#buildTableHeader(int)
      */
    @Override
    public String buildTableHeader(final int columnCount) {
        return buildTableRowApt(2);
    }

    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.ISyntaxer#buildTableHeader(java.lang.String[])
      */
    @Override
    public String buildTableHeader(final String[] values) {
        // sbEnum.append("*-----------------+\n");
        // sbEnum.append("| <<Valeur>>      |\n");
        // sbEnum.append("*-----------------+\n");

        currentColumnCounts = 0;

        String row = buildTableRowApt(values.length);

        String result = row;
        if (values != null && values.length > 0) {
            result += "|";
            for (String value : values) {
                result += value + "|";
                currentColumnCounts++;
            }
            result += LINE_SEPARATOR;
            result += row;
        }
        return result;
    }

    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.ISyntaxer#buildTableRow(java.lang.String[])
      */
    @Override
    public String buildTableRow(final String[] values) {
        // sbEnum.append("| " + enumeration + "|\n");
        // sbEnum.append("*-----------------+\n");
        String result = "| ";
        for (String value : values) {
            result += value + "|";
        }
        result += LINE_SEPARATOR;
        result += buildTableRowApt(values.length);
        return result;
    }

    /**
     * Method.
     *
     * @param columnCount the column count
     * @return the string
     */
    @Override
    public String buildTableRowApt(final int columnCount) {
        String row = "*";
        for (int i = 0; i < columnCount; i++) {
            row += "---------------------------+";
        }
        row += LINE_SEPARATOR;
        return row;
    }

    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.ISyntaxer#buildUnorderedItem(java.lang.String)
      */
    @Override
    public String buildUnorderedItem(final String value) {
        return "* " + value + LINE_SEPARATOR + LINE_SEPARATOR;
    }

    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.ISyntaxer#fontBold(java.lang.String)
      */
    @Override
    public String fontBold(final String value) {
        return "<<" + value + ">>";
    }

    /**
      * {@inheritDoc}
      * 
      * @see org.tensin.common.tools.documentation.updater.ISyntaxer#fontItalic(java.lang.String)
      */
    @Override
    public String fontItalic(final String value) {
        return "__" + value + "__";
    }

}
