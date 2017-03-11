/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.testbeans.CircularDao1;
import be.bagofwords.minidepi.testbeans.SelfReferentialDao;
import org.junit.Assert;
import org.junit.Test;

public class CircularWiringTests {

    @Test
    public void wireCircularBeans() {
        ApplicationContext applicationContext = new ApplicationContext();
        CircularDao1 circularDao1 = new CircularDao1();
        applicationContext.declareBean(circularDao1);
        Assert.assertNotNull(circularDao1.getDao1());
        Assert.assertNotNull(circularDao1.getCircularDao2());
    }

    @Test
    public void wireSelfReferentialBean() {
        ApplicationContext applicationContext = new ApplicationContext();
        SelfReferentialDao selfReferentialDao = new SelfReferentialDao();
        applicationContext.declareBean(selfReferentialDao);
        Assert.assertNotNull(selfReferentialDao.getSelf());
    }

}
