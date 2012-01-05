/*
 * 
 */
package org.tensin.beerduino;

import org.apache.log4j.BasicConfigurator;

/**
 * The Class AbstractTestCase.
 */
abstract public class AbstractTestCase {

    /**
     * Instantiates a new abstract test case.
     */
    public AbstractTestCase() {
        super();
        BasicConfigurator.configure();
    }
}
