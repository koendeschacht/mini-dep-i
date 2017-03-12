/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-12. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.testbeans.BeanWithJavaXInject;
import be.bagofwords.minidepi.testbeans.Dao1;
import org.junit.Assert;
import org.junit.Test;

public class JavaxInjectTest {

    @Test
    public void testJavaxInject() {
        BeanWithJavaXInject bean = new ApplicationContext().getBean(BeanWithJavaXInject.class);
        Assert.assertNotNull(bean.getDao1());
        Assert.assertEquals(Dao1.class, bean.getDao1().getClass());
    }

}
