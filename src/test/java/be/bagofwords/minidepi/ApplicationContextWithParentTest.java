/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2019-1-21. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.testbeans.Dao1;
import be.bagofwords.minidepi.testbeans.DatabaseService;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationContextWithParentTest {

    @Test
    public void testUsingSameBean() {
        ApplicationContext parent = new ApplicationContext();
        ApplicationContext child = new ApplicationContext(parent);
        DatabaseService databaseServiceFromParent = parent.getBean(DatabaseService.class);
        DatabaseService databaseServiceFromChild = child.getBean(DatabaseService.class);
        Assert.assertSame(databaseServiceFromParent, databaseServiceFromChild);
    }

    @Test
    public void testUsingChildBean() {
        ApplicationContext parent = new ApplicationContext();
        ApplicationContext child = new ApplicationContext(parent);
        DatabaseService databaseServiceFromParent = parent.getBean(DatabaseService.class);
        Dao1 daoFromChild = child.getBean(Dao1.class);
        Assert.assertSame(databaseServiceFromParent, daoFromChild.databaseService);
    }

    @Test
    public void testUsingChildBeanMultipleContexts() {
        ApplicationContext parent = new ApplicationContext();
        ApplicationContext child1 = new ApplicationContext(parent);
        ApplicationContext child2 = new ApplicationContext(parent);
        parent.getBean(DatabaseService.class);
        Dao1 daoFromChild1 = child1.getBean(Dao1.class);
        Dao1 daoFromChild2 = child2.getBean(Dao1.class);
        Assert.assertSame(daoFromChild1.databaseService, daoFromChild2.databaseService);
        Assert.assertNotSame(daoFromChild1, daoFromChild2);
    }

}
