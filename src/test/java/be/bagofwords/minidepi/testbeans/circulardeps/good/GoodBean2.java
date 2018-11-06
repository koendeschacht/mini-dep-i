/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2018-11-6. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans.circulardeps.good;

import be.bagofwords.minidepi.annotations.Inject;

public class GoodBean2 {

    @Inject
    private GoodBean3 goodBean3;

    @Override
    public String toString() {
        return "bean2";
    }
}
