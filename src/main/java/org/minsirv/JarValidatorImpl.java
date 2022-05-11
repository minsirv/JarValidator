package org.minsirv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minsirv.enumerators.ResponseCodes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

class JarValidatorImpl implements JarValidator {

    private static final Logger logger = LogManager.getLogger(JarValidatorImpl.class);

    @Override
    public boolean validate(String jarCode) {
        boolean isValid;
        List<Integer> digits = new ArrayList<>();
        if (!validInput(jarCode)) {
            return false;
        }

        digits.addAll(jarCode.chars().mapToObj(e -> (char)e).map(Character::getNumericValue).collect(Collectors.toList()));
        isValid = validateGenderCode(digits) //check if the first digit indicating sex is valid
                && validateBirthDate(digits) //check if birth date numbers match date pattern
                && validateControl(digits)
        ;

        return isValid;
    }

    public List<String> validateVerbose(String jarCode) {
        List<String> validationResults = new ArrayList<>();
        List<Integer> digits = new ArrayList<>();

        if(isNull(jarCode))
            validationResults.add(ResponseCodes.INPUT_NULL.toString());
        if(!isElevenCharacters(jarCode))
            validationResults.add(ResponseCodes.INPUT_WRONG_LENGTH.toString());
        if(!isOnlyDigits(jarCode))
            validationResults.add(ResponseCodes.INAPPROPRIATE_CHARACTERS.toString());

        if (!validationResults.isEmpty())
            return validationResults;

        digits.addAll(jarCode.chars().mapToObj(e -> (char)e).map(Character::getNumericValue).collect(Collectors.toList()));
        if(!validateGenderCode(digits))
            validationResults.add(ResponseCodes.GENDER_CODE_INVALID.toString());
        if(!validateBirthDate(digits))
            validationResults.add(ResponseCodes.INVALID_BIRTH_DATE.toString());
        if(!validateControl(digits))
            validationResults.add(ResponseCodes.INVALID_CONTROL_CODE.toString());
        return validationResults;
    }

    @Override
    public Date toDate(String jarCode) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            switch (jarCode.substring(0,1)) {
                case "3":
                case "4":
                    return format.parse("19".concat(jarCode.substring(1,7)));
                case "5":
                case "6":
                    return format.parse("20".concat(jarCode.substring(1,7)));
            }
        } catch (ParseException e) {
            logger.error("Failed to parse to date!");
        }
        return null;
    }

    public boolean isMale(String jarCode) {

        return !isNull(jarCode) && jarCode.startsWith("3") || jarCode.startsWith("5");
    }
//  should an invalid code be counted as male/female?
    public boolean isFemale(String jarCode) {
        return !isNull(jarCode) && jarCode.startsWith("4") || jarCode.startsWith("6");
    }

    private boolean validateControl(List<Integer> jarCode) {
        Integer control = (jarCode.get(0) +
                jarCode.get(1) * 2 +
                jarCode.get(2) * 3 +
                jarCode.get(3) * 4 +
                jarCode.get(4) * 5 +
                jarCode.get(5) * 6 +
                jarCode.get(6) * 7 +
                jarCode.get(7) * 8 +
                jarCode.get(8) * 9 +
                jarCode.get(9)) / 11;

        if (control < 10) {
            return jarCode.get(10).equals(control);
        }

        control = (jarCode.get(0) * 3 +
                jarCode.get(1) * 4 +
                jarCode.get(2) * 5 +
                jarCode.get(3) * 6 +
                jarCode.get(4) * 7 +
                jarCode.get(5) * 8 +
                jarCode.get(6) * 9 +
                jarCode.get(7) +
                jarCode.get(8) * 2 +
                jarCode.get(9) * 3) / 11;

        if (control < 10) {
            return jarCode.get(10).equals(control);
        }

        return jarCode.get(10).equals(0);
    }

    private boolean validateBirthDate(List<Integer> jarCode) {
        Integer month = jarCode.get(3) * 10 + jarCode.get(4);
        Integer day = jarCode.get(5) * 10 + jarCode.get(6);
        return month > 0 && month <= 12
                && day > 0 && day <= 31
                || jarCode.get(3) == 0 && jarCode.get(4) == 0 && jarCode.get(5) == 0 && jarCode.get(6) == 0;
    }

    private boolean validateGenderCode(List<Integer> jarCode) {
        return jarCode.get(0) >= 3 && jarCode.get(0) <= 6;
    }

    private boolean validInput(String jarCode) {
        return !isNull(jarCode) && isElevenCharacters(jarCode) && isOnlyDigits(jarCode);
    }

    private boolean isNull(String input) {
        return input == null;
    }

    private boolean isElevenCharacters(String input) {
        return input.length() == 11;
    }

    private boolean isOnlyDigits(String input) {
        return input.chars().allMatch(Character::isDigit);
    }
}
