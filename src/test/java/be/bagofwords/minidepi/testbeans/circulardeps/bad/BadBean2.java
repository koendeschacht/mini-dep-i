/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2018-11-6. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans.circulardeps.bad;

import be.bagofwords.minidepi.annotations.Inject;

public class BadBean2 {

    @Inject
    private BadBean3 badBean3;

    @Override
    public String toString() {
        return "bean2";
    }
}
