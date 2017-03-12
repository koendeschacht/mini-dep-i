/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.testbeans.BeanThatWiresContext;
import org.junit.Assert;
import org.junit.Test;

public class AutowiringContextTest {

    @Test
    public void testAutoWiringContext() {
        ApplicationContext applicationContext = new ApplicationContext();
        BeanThatWiresContext bean = applicationContext.getBean(BeanThatWiresContext.class);
        ApplicationContext contextFromBean = bean.getApplicationContext();
        Assert.assertNotNull(contextFromBean);
        Assert.assertEquals(applicationContext, contextFromBean);
    }

}
