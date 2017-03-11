/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.annotations.Inject;

public class Plugin1 extends TestBean implements Plugin {

    @Inject
    private Dao1 dao1;

    public Dao1 getDao1() {
        return dao1;
    }
}
