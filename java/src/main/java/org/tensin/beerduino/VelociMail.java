package org.tensin.beerduino;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.tensin.common.CoreException;
import org.tensin.common.helpers.DumpHelper;

/**
 * The Class VelociMail.
 * 
 * @author u248663
 * @version $Revision: 1.1 $
 * @since 6 mai 2011 15:38:02
 */
public class VelociMail {

    /** loader. */
    private static final ClassLoader loader = Thread.currentThread().getContextClassLoader();

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(VelociMail.class);

    /**
     * Method.
     * 
     * @param resourceName
     *            the resource name
     * @return true, if is image
     */
    public static boolean isImage(final String resourceName) {
        if (StringUtils.isNotEmpty(resourceName)) {
            final String s = resourceName.toLowerCase();
            return s.endsWith(".jpg") || s.endsWith(".gif") || s.endsWith(".png") || s.endsWith(".jpeg") || s.endsWith(".bmp");
        }
        return false;
    }

    /** template. */
    private String template;

    /** layout. */
    private String layout;

    /** velocityContext. */
    private VelocityContext velocityContext;

    /**
     * Constructor.
     */
    public VelociMail() {
        super();
        initVelocity();
    }

    /**
     * Method.
     * 
     * @param key
     *            the key
     * @param object
     *            the object
     */
    public void addContext(String key, Object object) {
        velocityContext.put(key, object);
    }

    /**
     * Method.
     * 
     * @param templatePath
     *            the template path
     * @return the input stream reader
     * @throws CoreException
     *             the core exception
     */
    private Reader getInputStreamReader(String templatePath) throws CoreException {
        InputStream is = loader.getResourceAsStream(templatePath);
        if (is == null) {
            throw new CoreException("La ressource '" + templatePath + "' est introuvable");
        }
        return new InputStreamReader(is);
    }

    /**
     * Method.
     * 
     * @return the layout
     */
    public String getLayout() {
        return layout;
    }

    /**
     * Method.
     * 
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Method.
     * 
     * @return the velocity context
     */
    public VelocityContext getVelocityContext() {
        return velocityContext;
    }

    /**
     * Method.
     * 
     */
    private void initVelocity() {
        try {
            Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM, new VelocityLogger());
            Velocity.init();
            velocityContext = new VelocityContext();
        } catch (Exception e) {
            LOGGER.error("Error while initializing Velocity", e);
        }
    }

    /**
     * Method.
     * 
     * @param email
     *            the email
     * @param listUsedImages
     *            the list used images
     */
    private void loadImagesIntoContext(HtmlEmail email, Set<String> listUsedImages) {
        PathMatchingResourcePatternResolver pmrspr = new PathMatchingResourcePatternResolver();
        List<String> registeredImages = new ArrayList<String>();
        try {
            Resource[] resources = pmrspr.getResources("classpath:" + layout + "**/*");
            String resourceName;
            String baseResourceName;
            String imgResourceName;
            for (Resource resource : resources) {
                resourceName = resource.getFilename();
                if (isImage(resourceName)) {
                    baseResourceName = resourceName.substring(0, resourceName.lastIndexOf("."));
                    imgResourceName = "img" + StringUtils.capitalize(baseResourceName);
                    if (listUsedImages.contains(imgResourceName)) {
                        LOGGER.debug("Merging image [" + imgResourceName + "]");
                        velocityContext.put(imgResourceName, email.embed(resource.getURL(), StringUtils.capitalize(baseResourceName)));
                        registeredImages.add(resourceName + " (cid:" + imgResourceName + ")");
                    } else {
                        LOGGER.debug("Not merging image [" + imgResourceName + "] as it is not used in the current template");
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while assembling images into mail", e);
        } catch (EmailException e) {
            LOGGER.error("Error while assembling images into mail", e);
        }
        LOGGER.info("Registered images : \n" + DumpHelper.dump(registeredImages));
    }

    /**
     * Method.
     * 
     * @return the sets the
     * @throws CoreException
     *             the core exception
     */
    private Set<String> loadUsedImagesFromTemplate() throws CoreException {
        Set<String> listUsedImages = new TreeSet<String>();
        BufferedReader bis = new BufferedReader(getInputStreamReader(template));
        String line;
        String buffer = "";
        try {
            while ((line = bis.readLine()) != null) {
                buffer += line;
            }
            Pattern pattern = Pattern.compile("cid:\\$\\{(img[^}]*)\\}");
            Matcher matcher = pattern.matcher(buffer);
            while (matcher.find()) {
                String s = matcher.group(1);
                listUsedImages.add(s);
            }
        } catch (IOException e) {
            throw new CoreException("Error while parsing template [" + template + "]", e);
        } finally {
            // Do not make any close here
        }
        return listUsedImages;
    }

    /**
     * Render.
     * 
     * @return the string
     */
    public String render() {
        StringWriter w = new StringWriter();
        try {
            // embed the image and get the content id
            // Set<String> listUsedImages = loadUsedImagesFromTemplate();
            // loadImagesIntoContext(email, listUsedImages); // load only images really used in the template
            Velocity.evaluate(velocityContext, w, null, getInputStreamReader(template));
            return w.toString();
        } catch (ParseErrorException e) {
            LOGGER.error("Error while rendering mail", e);
        } catch (MethodInvocationException e) {
            LOGGER.error("Error while rendering mail", e);
        } catch (ResourceNotFoundException e) {
            LOGGER.error("Error while rendering mail", e);
        } catch (CoreException e) {
            LOGGER.error("Error while rendering mail", e);
        } finally {
        }

        return null;
    }

    /**
     * Method.
     * 
     * @param email
     *            the email
     * @return the string
     */
    public String render(HtmlEmail email) {
        StringWriter w = new StringWriter();
        try {
            // embed the image and get the content id
            Set<String> listUsedImages = loadUsedImagesFromTemplate();
            loadImagesIntoContext(email, listUsedImages); // load only images really used in the template
            Velocity.evaluate(velocityContext, w, "VelocityMail", getInputStreamReader(template));
            return w.toString();
        } catch (ParseErrorException e) {
            LOGGER.error("Error while rendering mail", e);
        } catch (MethodInvocationException e) {
            LOGGER.error("Error while rendering mail", e);
        } catch (ResourceNotFoundException e) {
            LOGGER.error("Error while rendering mail", e);
        } catch (Exception e) {
            LOGGER.error("Error while rendering mail", e);
        } finally {
        }

        return null;
    }

    /**
     * Method.
     * 
     * @param layout
     *            the new layout
     */
    public void setLayout(String layout) {
        this.layout = layout;
    }

    /**
     * Method.
     * 
     * @param template
     *            the new template
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Method.
     * 
     * @param velocityContext
     *            the new velocity context
     */
    public void setVelocityContext(VelocityContext velocityContext) {
        this.velocityContext = velocityContext;
    }
}