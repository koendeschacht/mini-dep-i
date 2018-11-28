/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2018-11-28. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.implementation;

import be.bagofwords.logging.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static be.bagofwords.util.Utils.threadSleep;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class UserInputManager {

    public static final int MAX_TIME_FOR_USER_INPUT = 60_000;

    public String getPropertyFromUserInputIfPossible(String name, List<File> readPropertyFiles) {
        printUserInputWarningHeader(name, readPropertyFiles);
        try {
            long start = System.currentTimeMillis();
            boolean gotInput = false;
            while (System.currentTimeMillis() - start < MAX_TIME_FOR_USER_INPUT && !gotInput) {
                if (System.in.available() > 0) {
                    gotInput = true;
                } else {
                    threadSleep(500);
                }
            }
            if (gotInput) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
                String value = reader.readLine().trim();
                if (!isEmpty(value)) {
                    return value;
                }
            }
        } catch (IOException exp) {
            Log.w("Failed to read user input", exp);
        }
        return null;
    }

    private void printUserInputWarningHeader(String name, List<File> readPropertyFiles) {
        Log.w("**** USER INPUT NEEDED ***** ");
        Log.w("The application needs the value of a property to start successfully. Please provide the value for that property, or press <enter> to terminate the application");
        if (readPropertyFiles.isEmpty()) {
            Log.w("You can provide a property file by passing setting a system property, i.e. -Dproperty.file=/path/to/my_properties.properties");
        } else if (readPropertyFiles.size() == 1) {
            Log.w("You might want to add the properties to the file " + readPropertyFiles.get(0).getAbsolutePath() + " to avoid this warning on the next run");
        } else {
            readPropertyFiles.size();
            Log.w("You might want to any of the following files to avoid this warning on the next run");
            for (File file : readPropertyFiles) {
                Log.w("\t" + file.getAbsolutePath());
            }
        }
        Log.w("You have " + MAX_TIME_FOR_USER_INPUT / 1000 + " seconds to provide this input");
        Log.w("Property \"" + name + "\"=?");
    }

}
