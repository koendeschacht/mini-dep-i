/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-4-9. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.testbeans.BeanStopAfter;
import be.bagofwords.minidepi.testbeans.BeanStopBefore;
import org.junit.Assert;
import org.junit.Test;

public class TestStopBefore {

    @Test
    public void testLifeCycle() {
        ApplicationContext applicationContext = new ApplicationContext();
        BeanStopAfter stopAfter = applicationContext.getBean(BeanStopAfter.class);
        BeanStopBefore stopBefore = applicationContext.getBean(BeanStopBefore.class);
        Assert.assertFalse(stopBefore.stopped);
        Assert.assertFalse(stopAfter.stopped);
        applicationContext.terminate();
        Assert.assertTrue(stopBefore.stopped);
        Assert.assertTrue(stopAfter.stopped);

    }

}
