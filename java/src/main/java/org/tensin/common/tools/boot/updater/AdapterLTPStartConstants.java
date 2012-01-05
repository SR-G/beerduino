package org.tensin.common.tools.boot.updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.common.CoreException;

/**
 * AdapterLTPStartConstants.
 * 
 * @author u248663
 * @version $Revision: 1.5 $
 * @since 19 mars 2010 11:19:46
 */
public class AdapterLTPStartConstants implements IAdapterInput, IAdapterOutput {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdapterLTPStartConstants.class);

    /** LIB_COLUMNS. */
    private static final int LIB_COLUMNS = 5;
    
    /** constantName. */
    private String constantName = "ltpLib";
    
    /** excludedJars. */
    private List<String> excludedJars = new ArrayList<String>();

    /**
     * Méthode getExcludedJars.
     *
     * @return the excluded jars
     * List
     */
    public List<String> getExcludedJars() {
		return excludedJars;
	}

	/**
	 * Méthode setExcludedJars.
	 *
	 * @param excludedJars
	 * void
	 */
	public void setExcludedJars(List<String> excludedJars) {
		this.excludedJars = excludedJars;
	}

	/**
	 * Gets the constant name.
	 *
	 * @return the constant name
	 */
    public String getConstantName() {
		return constantName;
	}

	/**
	 * Sets the constant name.
	 *
	 * @param constantName the new constant name
	 */
	public void setConstantName(String constantName) {
		this.constantName = constantName;
	}

	/** START_JAR_ENTRY_PATTERN. */
    public static final String START_JAR_ENTRY_PATTERN = ".*String (JAR_[^ ]*).*=.*\"(.*\\.jar)\".*";

    /**
     * Méthode buildAdapter.
     *
     * @param destFileName String
     * @param constantName String
     * @return AdapterLTPStartConstants
     */
    public static AdapterLTPStartConstants buildAdapter(final String destFileName, String constantName) {
        AdapterLTPStartConstants adapter = new AdapterLTPStartConstants();
        adapter.setDestFileName(destFileName);
        adapter.setConstantName(constantName);
        return adapter;
    }

    /**
     * Builds the adapter.
     *
     * @param destFileName the dest file name
     * @return the adapter ltp start constants
     */
    public static AdapterLTPStartConstants buildAdapter(final String destFileName) {
        AdapterLTPStartConstants adapter = new AdapterLTPStartConstants();
        adapter.setDestFileName(destFileName);
        return adapter;
    }

    /**
     * Méthode buildAdapter.
     *
     * @param destFileName String
     * @param constantName String
     * @param excludedJars List
     * @return AdapterLTPStartConstants
     */
    public static AdapterLTPStartConstants buildAdapter(final String destFileName, String constantName, List<String> excludedJars) {
        AdapterLTPStartConstants adapter = new AdapterLTPStartConstants();
        adapter.setDestFileName(destFileName);
        adapter.setConstantName(constantName);
        adapter.setExcludedJars(excludedJars);
        return adapter;
    }

    /** mode. */
    private String mode;

    /** destFileName. */
    private String destFileName;

    /**
     * Method.
     * 
     * @param expectedBaseJars
     *            List
     * @return String
     */
    private String buildAllLibs(final Collection<JarContainer> expectedBaseJars) {
        return buildAllLibs(expectedBaseJars, null);
    }

    /**
     * Construit la liste de toutes les librairies, sauf jars exclus.
     *
     * @param expectedJars the expected jars
     * @param excludedJars List
     * @return String
     */
    private String buildAllLibs(final Collection<JarContainer> expectedJars, final List<String> excludedJars) {
        StringBuffer sb = new StringBuffer();
        Iterator<JarContainer> itr = expectedJars.iterator();
        int count = 0;
        JarContainer item;
        while (itr.hasNext()) {
            item = itr.next();

            if (!isExcludedName(item.getStartConstantsKey(), excludedJars)) {
            	if ( item.hasOrigin(AdapterLTPStartConstants.class)) {
	                if (count > 0) {
	                    sb.append(", ");
	                }
	                if (count % LIB_COLUMNS == 0) {
	                    sb.append("\n\t\t\t");
	                }
	                sb.append(item.getStartConstantsKey());
	                count++;
            	}
            }
        }
        return sb.toString();
    }

    /**
     * Remplacement d'une chaîne par une autre dans un fichier.
     *
     * @param filePathName Le fichier dans lequel faire le remplacement
     * @param oldString L'ancienne chaine
     * @param newString La nouvelle chaîne
     * @throws CoreException the core exception
     */
    public static void replace(final String filePathName, final String oldString, final String newString) throws CoreException {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Remplacement dans " + filePathName).append("\n");
    	sb.append(" > Ancienne chaine : " + oldString).append("\n");
    	sb.append(" > Nouvelle chaine : " + newString).append("\n");
        final File file = new File(filePathName);
        BufferedReader br = null;
        InputStreamReader isr = null;
        OutputStreamWriter osw = null;
        try {
            isr = new InputStreamReader(new FileInputStream(file), guessEncoding());

            br = new BufferedReader(isr);
            final StringBuffer buffer = new StringBuffer();
            String line = br.readLine();
            while (line != null) {
                buffer.append(line + "\r\n");
                line = br.readLine();
            }
            String fileStr = null;
            try {
                fileStr = buffer.toString().replaceAll(oldString, newString);
                // CHECKSTYLE:OFF exceptions
            } catch (final Exception e) {
            	sb.append("Erreur de remplacement dans '" + oldString + "' : " + e.getMessage()).append("\n");
                // CHECKSTYLE:ON
            }
            osw = new OutputStreamWriter(new FileOutputStream(file), guessEncoding());
            osw.write(fileStr);
        } catch (final IOException e) {
            throw new CoreException("Erreur remplacement chaine", e);
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                    isr = null;
                }
                if (br != null) {
                    br.close();
                    br = null;
                }
                if (osw != null) {
                    osw.close();
                    osw = null;
                }
            } catch (final IOException ex) {
                LOGGER.error(ex.getMessage());
            }
        }
    }

    /**
     * Guess encoding.
     *
     * @return the string
     */
    public static String guessEncoding() {
        String foundEncoding = System.getProperty("file.encoding", "UTF-8").toUpperCase().trim();
        return foundEncoding;
    }


    
    /*
     * (non-Javadoc)
     * @see com.inetpsa.ltp.tools.excluded.IAdapterOutput#generate()
     */
    public void generate(final Collection<JarContainer> jars) throws DependencyException {
		if (!new File(destFileName).exists()) {
			LOGGER.info("Erreur, le fichier : " + destFileName + " n'existe pas.");
		} else {
			try {
				LOGGER.info("");
		    	LOGGER.info("--------------------------------------------------------------------------------------------");
		    	LOGGER.info("Mise à jour du contenu du StartConstants.java ... \n");
				// Mise à jour des jars indidividuels
				for ( JarContainer jar : jars) {
	            	if ( jar.hasOrigin(AdapterLTPStartConstants.class)) {
	            		String oldJarDefinition = "public static final String " + jar.getStartConstantsKey() + " = \"[^\"]*\";";
	            		String newJarDefinition = "public static final String " + jar.getStartConstantsKey() + " = \"" + jar.getJarName() + "\";";
	            		replace(destFileName, oldJarDefinition, newJarDefinition);
	            	}
				}
				
				// Construction de la liste de tous les jars
				String allLibs = buildAllLibs(jars, getExcludedJars());
				String allLibsDeclaration = "public static final String[] "
						+ constantName + " = { " + allLibs + " };";
				LOGGER.info("");
				LOGGER.info(allLibsDeclaration);
				replace(destFileName,
						"public static final String\\[\\] " + constantName
								+ " = \\{[^\\}]*\\};", allLibsDeclaration);

				// Cas particulier : on cherche ltp-java et on met à jour la version 
				String ltpVersion = null;
				for (JarContainer jar : jars) {
					if ( jar.getGroupId().equals("com.inetpsa.ltp00") && jar.getArtifactId().equals("ltp-java")) {
						ltpVersion = jar.getVersion();
						break;
					}
				}
				if ( StringUtils.isNotEmpty(ltpVersion)) {
					
					LOGGER.info("");
			    	LOGGER.info("--------------------------------------------------------------------------------------------");
					if ( StringUtils.isNotEmpty(System.getProperty("ltp.release"))) {
						LOGGER.info("Version release detectee (paramètre systeme 'ltp.release', on retire le -SNAPSHOT [" + ltpVersion + "]");
						ltpVersion = ltpVersion.replaceAll("-SNAPSHOT", "");
					} else {
						LOGGER.info("Pas de release en cours (paramètre systeme 'ltp.release' non positionné), on prend [" + ltpVersion + "]");
					}
					LOGGER.info("");
					
					LOGGER.info("Mise à jour de la version des jars LTP avec [" + ltpVersion + "]\n");
            		String oldJarDefinition = "public static final String JAR_LTP = \"[^\"]*\";";
            		String newJarDefinition = "public static final String JAR_LTP = \"ltp-java-" + ltpVersion + ".jar\";";
            		replace(destFileName, oldJarDefinition, newJarDefinition);
            		oldJarDefinition = "public static final String JAR_LTP_STRUTS_V1 = \"[^\"]*\";";
            		newJarDefinition = "public static final String JAR_LTP_STRUTS_V1 = \"ltp-strutsv1-" + ltpVersion + ".jar\";";
            		replace(destFileName, oldJarDefinition, newJarDefinition);
            		oldJarDefinition = "public static final String JAR_JBI_LTP = \"[^\"]*\";";
            		newJarDefinition = "public static final String JAR_JBI_LTP = \"ltp-jbi-" + ltpVersion + ".jar\";";
            		replace(destFileName, oldJarDefinition, newJarDefinition);
				} else {
					LOGGER.info("");
			    	LOGGER.info("--------------------------------------------------------------------------------------------");
					LOGGER.info("Impossible de retrouver la version LTP, les versions LTP ne seront pas à jour dans le StartConstants.java !");
				}
			} catch (CoreException e) {
				throw new DependencyException(e);
			}
		}
    }

    /**
     * Récupération d'un reader sur un item éventuellement interne ou sinon fichier.
     *
     * @param path Chemin.
     * @return BufferedReader item
     * @throws IOException Problème.
     */
    private BufferedReader getBufferedReader(final String path) throws IOException {
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream(path);
        if (is == null) {
            is = ClassLoader.getSystemResourceAsStream(path);
        }
        if (is == null) {
            is = new FileInputStream(path);
        }
        return new BufferedReader(new InputStreamReader(is, guessEncoding()));
    }

    /**
     * Getter de l'attribut destFileName.
     * 
     * @return Returns L'attribut destFileName.
     */
    public String getDestFileName() {
        return destFileName;
    }

    /**
     * Getter de l'attribut mode.
     * @return Returns L'attribut mode.
     */
    public String getMode() {
        return mode;
    }

    /* (non-Javadoc)
     * @see com.inetpsa.ltp.tools.excluded.IAdapterInput#getName()
     */
    public String getName() {
        return "Load and save from and to LTP java StartConstants file";
    }

    /**
     * Jar exclu ?.
     *
     * @param item String
     * @param namesToCheck Liste des jars exclus
     * @return boolean
     */
    private boolean isExcludedName(final String item, final List<String> namesToCheck) {
        boolean result = false;
        Iterator<String> itr = namesToCheck.iterator();
        String pattern;
        while (itr.hasNext() && !result) {
            pattern = (String) itr.next();
            if (item.startsWith(pattern)) {
                result = true;
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.inetpsa.ltp.tools.excluded.IAdapterInput#load()
     */
    public Collection<JarContainer> load() throws DependencyException {
        return loadStartJavaContent(getDestFileName());
    }

    /**
     * Chargement des jars contenus dans le start.java
     *
     * @param javaFileToUpdate String
     * @return Collection
     * @throws DependencyException the dependency exception
     */
    private Collection<JarContainer> loadStartJavaContent(final String javaFileToUpdate) throws DependencyException {
        return loadStartJavaContent(javaFileToUpdate, START_JAR_ENTRY_PATTERN);
    }

    /**
     * Chargement des jars contenus dans le start.java
     *
     * @param javaFileToUpdate String
     * @param jarEntryConstantPattern String
     * @return Collection
     * @throws DependencyException the dependency exception
     */
    private Collection<JarContainer> loadStartJavaContent(final String javaFileToUpdate, final String jarEntryConstantPattern) throws DependencyException {
        Collection<JarContainer> startJavaLibs = new ArrayList<JarContainer>();
        BufferedReader br;
        try {
            br = getBufferedReader(javaFileToUpdate);
            String lineStartJava = null;
            Pattern pattern = Pattern.compile(jarEntryConstantPattern);
            Matcher doubleMatcher;
            String key = null;
            String value = null;
            JarContainer jar;
            while ((lineStartJava = br.readLine()) != null) {
                if (Pattern.matches(jarEntryConstantPattern, lineStartJava)) {
                    doubleMatcher = pattern.matcher(lineStartJava);
                    if (doubleMatcher.find()) {
                        key = doubleMatcher.group(1);
                        value = doubleMatcher.group(2);
                    }
                    jar = new JarContainer(AdapterLTPStartConstants.class);
                    jar.setStartConstantsKey(key);
                    jar.setStartConstantsRegExp(value);
                    jar.setJarName(value);
                    startJavaLibs.add(jar);
                }
            }
        } catch (IOException e) {
            throw new DependencyException(e);
        }
        return startJavaLibs;
    }

    /**
     * Setter de l'attribut destFileName.
     * 
     * @param destFileName
     *            L'attribut destFileName.
     */
    public void setDestFileName(final String destFileName) {
        this.destFileName = destFileName;
    }

    /**
     * Setter de l'attribut mode.
     * @param mode L'attribut mode.
     */
    public void setMode(final String mode) {
        this.mode = mode;
    }

	/**
	 * Adds the excluded jar.
	 *
	 * @param jar the jar
	 */
	public void addExcludedJar(String jar) {
		excludedJars.add(jar);
	}
}
