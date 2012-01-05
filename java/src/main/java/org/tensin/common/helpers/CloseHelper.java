package org.tensin.common.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;


/**
 * The Class CloseHelper.
 */
public class CloseHelper {

    /**
     * Close.
     *
     * @param is the is
     */
    public static void close(final InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Close.
     *
     * @param br the br
     */
    public static void close(final Reader br) {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
