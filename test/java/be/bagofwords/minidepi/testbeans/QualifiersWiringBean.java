/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-12. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.annotations.Inject;

public class QualifiersWiringBean {

    @Inject("bean1")
    private TestBean beanWithQualifiers;

    public TestBean getBeanWithQualifiers() {
        return beanWithQualifiers;
    }
}
