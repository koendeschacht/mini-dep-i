/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-4-1. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

public class PropertyNotFoundException extends ApplicationContextException {
    public PropertyNotFoundException(String message) {
        super(message);
    }

    public PropertyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
