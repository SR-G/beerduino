package org.tensin.beerduino;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * VelocityLogger. Mis en place pour s'affranchir des logs parasites de Velocity
 * (fichier velocity.log gênant pour l'indus).
 * 
 * @author David Meunier
 * @version $Revision: 1.3 $
 */
public final class VelocityLogger implements LogChute {

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(VelocityLogger.class);

    /**
     * Initialisation.
     * 
     * @param runtimeServices
     *            RuntimeServices.
     * @throws Exception
     *             Erreur.
     */
    public void init(final RuntimeServices runtimeServices) throws Exception {
        SystemUtil.doNothing();
    }

    /**
     * 
     * Loggin autorisé.
     * 
     * @param level
     *            Niveau.
     * @return true/false.
     */
    public boolean isLevelEnabled(final int level) {
        return true;
    }

    /**
     * Logging.
     * 
     * @param level
     *            Level.
     * @param message
     *            Message.
     */
    public void log(final int level, final String message) {
        LOGGER.debug(message);
    }

    /**
     * Logging.
     * 
     * @param level
     *            Level.
     * @param message
     *            Message.
     * @param throwable
     *            Throwable.
     */
    public void log(final int level, final String message, final Throwable throwable) {
        LOGGER.debug(message);
    }
}
