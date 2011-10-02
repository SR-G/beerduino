package org.tensin.beerduino;

import org.apache.log4j.BasicConfigurator;

abstract public class AbstractTestCase {

    public AbstractTestCase() {
        super();
        BasicConfigurator.configure();
    }
}
