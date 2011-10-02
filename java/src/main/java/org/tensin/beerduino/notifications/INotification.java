package org.tensin.beerduino.notifications;

import org.tensin.beerduino.CoreException;
import org.tensin.beerduino.TemperatureResults;

public interface INotification {

    void execute(final TemperatureResults results) throws CoreException;

}
