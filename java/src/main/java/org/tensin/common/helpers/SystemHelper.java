package org.tensin.common.helpers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public final class SystemHelper {

    /** READ_ARRAY_SIZE */
    private static final int READ_ARRAY_SIZE = 16384;

    /** bufferedReader */
    private static BufferedReader bufferedReader;

    /**
     * Do Nothing.
     */
    public static void doNothing() {
        // CHECKSTYLE:OFF Do nothing on te dit ...
    }

    // CHECKSTYLE:ON Do nothing on te dit ...

    /**
     * Method.
     * 
     * @return BufferedReader
     */
    private static BufferedReader getBufferedReader() {
        if (bufferedReader == null) {
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        }
        return bufferedReader;
    }

    /**
     * Dump d'un message sur un flux
     * 
     * @param out
     *            Le flux sur lequel envoyer le message
     * @param msg
     *            Le message à envoyer
     */
    private static void output(final PrintStream out, final String msg) {
        // CHECKSTYLE:OFF Affichage sur la console
        out.print(msg);
        // CHECKSTYLE:ON
    }

    /**
     * Dump d'un message sur un flux
     * 
     * @param out
     *            Le flux sur lequel envoyer le message
     * @param msg
     *            Le message à envoyer
     */
    private static void outputln(final PrintStream out, final String msg) {
        // CHECKSTYLE:OFF Affichage sur la console
        out.println(msg);
        // CHECKSTYLE:ON
    }

    /**
     * Lecture entrée standard.
     * 
     * @return Chaîne saisie.
     * @throws SecurityException
     *             Problème.
     */
    public static String read() throws SecurityException {
        String result = null;
        byte[] bytes = new byte[READ_ARRAY_SIZE];
        int read;
        try {
            read = System.in.read(bytes);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(bytes, 0, read);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toString()
                    .getBytes());
            BufferedReader br = new BufferedReader(new InputStreamReader(bis));
            result = br.readLine();
        } catch (IOException e) {
            throw new SecurityException("Probleme lecture", e);
        }

        return result;
    }

    /**
     * Read a line from the a buffer.
     * 
     * @return String
     */
    public static String readLine() {
        String ch = null;
        try {
            ch = getBufferedReader().readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ch;
    }

    /**
     * Affichage console.
     * 
     */
    public static void syserrln() {
        syserrln("");
    }

    /**
     * Affichage console.
     * 
     * @param o
     *            Objet.
     */
    public static void syserrln(final Object o) {
        syserrln(String.valueOf(o));
    }

    /**
     * Affichage console.
     * 
     * @param msg
     *            Message.
     */
    public static void syserrln(final String msg) {
        outputln(System.err, msg);
    }

    /**
     * Arrêt du processus.
     * 
     * @param code
     *            Code.
     */
    // CHECKSTYLE:OFF Arrêt du processus
    public static void sysexit(final int code) {
        System.exit(code);
    }

    // CHECKSTYLE:ON

    /**
     * Affichage console.
     * 
     * @param msg
     *            Message.
     */
    public static void sysout(final String msg) {
        output(System.out, msg);
    }

    /**
     * Affichage console.
     */
    public static void sysoutln() {
        sysoutln("");
    }

    /**
     * Affichage console.
     * 
     * @param msg
     *            Message.
     */
    // CHECKSTYLE:OFF Affichage sur la console
    public static void sysoutln(final String msg) {
        outputln(System.out, msg);
    }

}
