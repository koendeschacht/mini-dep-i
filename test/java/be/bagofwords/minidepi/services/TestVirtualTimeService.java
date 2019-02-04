/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-6-24. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.services;

import be.bagofwords.minidepi.ApplicationContext;
import org.junit.Assert;
import org.junit.Test;

public class TestVirtualTimeService {

    @Test
    public void test() throws InterruptedException {
        ApplicationContext applicationContextWithVirtualTime = new ApplicationContext();
        applicationContextWithVirtualTime.registerBean(VirtualTimeService.class);
        sleepShouldTake(applicationContextWithVirtualTime, 0);
        ApplicationContext applicationContextWithRealTime = new ApplicationContext();
        applicationContextWithVirtualTime.registerBean(TimeService.class);
        sleepShouldTake(applicationContextWithRealTime, 500);
    }

    private void sleepShouldTake(ApplicationContext applicationContext, int expectedTime) throws InterruptedException {
        TimeService timeService = applicationContext.getBean(TimeService.class);
        long start = System.currentTimeMillis();
        timeService.sleep(500);
        long end = System.currentTimeMillis();
        long actualTime = end - start;
        Assert.assertTrue(expectedTime - 50 < actualTime && actualTime < expectedTime + 50);
    }

}