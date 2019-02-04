/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.annotations.Inject;

public class Dao2 extends TestBean {

    private DatabaseService databaseService;

    @Inject
    public Dao2(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

}
