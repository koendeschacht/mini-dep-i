/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-12. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.annotations.Bean;
import be.bagofwords.minidepi.annotations.Inject;

public class QualifiersWiringBeanConstructor {

    private TestBean beanWithQualifiers;

    @Inject
    public QualifiersWiringBeanConstructor(@Bean("bean1") TestBean testBean) {
        this.beanWithQualifiers = testBean;
    }

    public TestBean getBeanWithQualifiers() {
        return beanWithQualifiers;
    }
}
