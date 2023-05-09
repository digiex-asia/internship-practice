package com.demo.common.utilities;

/**
 * @author DigiEx Group
 */
public interface Constant {
    String HEADER_TOKEN = "Auth-Token";

    //  Date
    String API_FORMAT_DATE_TIME = "MM/dd/yyyy hh:mm:ss";
    String API_FORMAT_TIME = "MM/dd/yyyy HH:mm";
    String API_FORMAT_DATE = "MM/dd/yyyy";
    String FORMAT_TIME = "HH:mm";
    long ONE_DAY_MILLISECOND = 24 * 60 * 60 * 1000;
    // Regex Pattern
    String VALID_XSS = "^((?!<|>)[\\s\\S])*?$";
    String VALID_CURLY_BRACES = "^((?!\\{|\\})[\\s\\S])*?$";
    // Other
    int SALT_LENGTH = 6;

    String SORT_BY_NAME = "name";

    String SORT_BY_PASS_SCORE = "pass_score";

    String SORT_BY_SCORE = "score";

    String SORT_BY_SKILL_TEST = "skill_test_id";
    // Sort field
    String MEMBER_FIRST_NAME = "firstName";
    String MEMBER_LAST_NAME = "lastName";
    String MEMBER_EMAIL = "email";

}
