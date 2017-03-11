/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.annotations.Inject;

public class CircularDao1 extends TestBean {

    @Inject
    private Dao1 dao1;

    @Inject
    private CircularDao2 circularDao2;

    public Dao1 getDao1() {
        return dao1;
    }

    public CircularDao2 getCircularDao2() {
        return circularDao2;
    }
}
