package org.tensin.beerduino.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class CloseHelper {

    public static void close(final InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

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
