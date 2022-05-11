package org.minsirv;

import java.util.Date;
import java.util.List;

public interface JarValidator {

    boolean validate(String jarCode);

    boolean isMale(String jarCode);

    boolean isFemale(String jarCode);

    Date toDate(String jarCode);

    List<String> validateVerbose(String jarCode);
}
