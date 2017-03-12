/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-12. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.testbeans.*;
import org.junit.Assert;
import org.junit.Test;

public class TestQualifiers {

    @Test
    public void testQualifiedField_DeclareClass() {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.declareBean(BeanWithQualifiers.class);
        QualifiersWiringBean bean = applicationContext.getBean(QualifiersWiringBean.class);
        Assert.assertNotNull(bean.getBeanWithQualifiers());
        Assert.assertEquals(BeanWithQualifiers.class, bean.getBeanWithQualifiers().getClass());
    }

    @Test
    public void testQualifiedField_DeclareClassObjects() {
        ApplicationContext applicationContext = new ApplicationContext();
        BeanWithQualifiers beanWithQualifiers = new BeanWithQualifiers();
        applicationContext.declareBean(beanWithQualifiers);
        QualifiersWiringBean bean = applicationContext.getBean(QualifiersWiringBean.class);
        Assert.assertNotNull(bean.getBeanWithQualifiers());
        Assert.assertEquals(beanWithQualifiers, bean.getBeanWithQualifiers());
    }

    @Test
    public void testQualifiedConstructor() {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.declareBean(BeanWithQualifiers.class);
        QualifiersWiringBeanConstructor bean = applicationContext.getBean(QualifiersWiringBeanConstructor.class);
        Assert.assertNotNull(bean.getBeanWithQualifiers());
        Assert.assertEquals(BeanWithQualifiers.class, bean.getBeanWithQualifiers().getClass());
    }

    @Test
    public void testProgrammaticDeclaration_Classes() {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.declareBean(Dao1.class);
        applicationContext.declareBean(Dao1.class, "first_dao");
        QualifiersWiringBeanDao bean = applicationContext.getBean(QualifiersWiringBeanDao.class);
        Assert.assertNotNull(bean);
        Assert.assertNotNull(bean.getDao1());
    }

    @Test
    public void testProgrammaticDeclaration_Objects() {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.declareBean(new Dao1());
        Dao1 first_dao = new Dao1();
        applicationContext.declareBean(first_dao, "first_dao");
        QualifiersWiringBeanDao bean = applicationContext.getBean(QualifiersWiringBeanDao.class);
        Assert.assertNotNull(bean);
        Assert.assertNotNull(bean.getDao1());
        Assert.assertEquals(first_dao, bean.getDao1());
    }

}
