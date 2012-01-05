package org.tensin.beerduino.run;

import org.tensin.beerduino.boot.BootConstants;
import org.tensin.beerduino.boot.ClasspathBooter;
import org.tensin.common.CoreException;

/**
 * The Class Beerduino.
 */
public class Beerduino {

    /** The Constant BEERDUINO_MAIN_CLASS. */
    private static final String BEERDUINO_MAIN_CLASS = org.tensin.beerduino.Beerduino.class.getName();

    /** The Constant BEERDUINO_BOOT_JAR. */
    private static final String BEERDUINO_BOOT_JAR = "beerduino.jar";

    /**
     * Gestion du classpath.
     * 
     * @param args
     *            the arguments
     */
    public static void main(final String args[]) {
        try {
            final ClasspathBooter cb = new ClasspathBooter(BEERDUINO_BOOT_JAR, "Beerduino");
            final String libDirectory = cb.getLibraryPathName(BEERDUINO_BOOT_JAR);
            cb.addJarsToClasspath(libDirectory, BootConstants.LIBS);
            System.out.println(cb.displayClasspath("\n"));
            cb.execute(BEERDUINO_MAIN_CLASS, "main", new Class[] { args.getClass() }, new Object[] { args });
        } catch (final CoreException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Affichage du manifest.
     */
    /*
     * private void displayManifest() {
     * if (mainArgs.getFirst().equals("manifest")) {
     * PyramideLauncherUtil.sysoutln();
     * String specificJar = null;
     * if (mainArgs.size() > 1) {
     * specificJar = mainArgs.get(1);
     * }
     * final File[] jars = libDirectoryFile.listFiles();
     * for (int i = 0; i < allLib.length; i++) {
     * final String jarName = allLib[i];
     * if ((specificJar != null) && !jarName.startsWith(specificJar)) {
     * continue;
     * }
     * try {
     * JarFile jarFile = new JarFile(jars[i]);
     * final JarEntry entry = jarFile.getJarEntry("META-INF/MANIFEST.MF");
     * final InputStream is = jarFile.getInputStream(entry);
     * final BufferedReader br = new BufferedReader(new InputStreamReader(is));
     * String line;
     * PyramideLauncherUtil.sysoutln("\n########## " + jarName + " ##########\n");
     * while ((line = br.readLine()) != null) {
     * PyramideLauncherUtil.sysoutln(line);
     * }
     * if (jarFile != null) {
     * jarFile.close();
     * jarFile = null;
     * }
     * } catch (final Exception e) {
     * PyramideLauncherUtil.sysoutln("Lecture MANIFEST " + jarName + " impossible");
     * }
     * }
     * PyramideLauncherUtil.sysexit(0);
     * }
     * }
     */

}
