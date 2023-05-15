package com.demo.common.utilities;

import com.demo.common.exceptions.ApplicationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author DigiEx Group
 */
public class Validator {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

    /**
     * @param message
     * @throws ApplicationException if {@code obj} is NOT null
     */

    public static void mustNull(Object obj, RestAPIStatus RestAPIStatus, String message) {

        if (obj != null) {
            throw new ApplicationException(RestAPIStatus, message);
        }
    }


    public static void notNull(Object obj, RestAPIStatus RestAPIStatus, String message) {
        if (obj == null) {
            throw new ApplicationException(RestAPIStatus, message);
        }
    }

    /**
     * Validate list object not null & not empty
     *
     * @param obj
     * @param message
     */

    public static void notNullAndNotEmpty(List<?> obj, RestAPIStatus RestAPIStatus, String message) {

        if (obj == null || obj.isEmpty()) {
            throw new ApplicationException(RestAPIStatus, message);
        }
    }

    /**
     * Validate object not null & not empty
     *
     * @param obj
     * @param message
     */

    public static void notNullAndNotEmpty(Object obj, RestAPIStatus RestAPIStatus, String message) {

        if (obj == null || "".equals(obj)) {
            throw new ApplicationException(RestAPIStatus, message);
        }
    }


    /**
     * Validate list object must null & must empty
     *
     * @param obj
     * @param apiStatus
     * @param message
     */
    public static void mustNullAndMustEmpty(List<?> obj, RestAPIStatus apiStatus, String message) {
        if (obj != null && !obj.isEmpty()) {
            throw new ApplicationException(apiStatus, message);
        }
    }


    public static void mustEquals(String str1, String str2, RestAPIStatus RestAPIStatus, String message) {
        if (!str1.equals(str2)) {
            throw new ApplicationException(RestAPIStatus, message);

        }
    }

    /**
     * validate email format
     *
     * @param emailAddress
     */
    public static void validateEmail(String emailAddress) {
        boolean isEmailFormat = isEmailFormat(emailAddress);
        if (!isEmailFormat) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Invalid email format");
        }
    }

    private static boolean isEmailFormat(String valueToValidate) {
        // Regex
        String regexExpression = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b";
        Pattern regexPattern = Pattern.compile(regexExpression);

        if (valueToValidate != null) {
            if (valueToValidate.indexOf("@") <= 0) {
                return false;
            }
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(valueToValidate);
            return matcher.matches();
        } else { // The case of empty Regex expression must be accepted
            Matcher matcher = regexPattern.matcher("");
            return matcher.matches();
        }
    }

    public static void checkFileExtensionType(MultipartFile file, String[] extensions) {
        // get extension
        String extensionType = AppUtil.getFileExtension(file);
        if (!Arrays.asList(extensions).contains(extensionType)) {
            throw new ApplicationException(RestAPIStatus.INVALID_FILE);
        }
    }

}
