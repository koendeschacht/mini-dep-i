/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2018-11-28. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi; import be.bagofwords.logging.Log;
import be.bagofwords.minidepi.annotations.Property;

public class MainToTestUserInput implements Runnable {

    public static void main(String[] args) {
        ApplicationManager.run(MainToTestUserInput.class);
    }

    @Property("my.input.value")
    private int input;

    @Override
    public void run() {
        Log.i("Test succeeded!");
    }
}
