package com.demo.controllers;

/**
 * @author Digiex Group
 */
public interface ApiPath {
    // Base URL
    String BASE_API_URL = "/api";
    String ID = "/{id}";
    String ALL = "/all";

    // Authenticate APIs
    String AUTHENTICATE_API = BASE_API_URL + "/auth";
    String AUTHENTICATE_RESET_PASSWORD = "/reset-password";
    String AUTHENTICATE_SOCIAL = "/social";


    // User APIs
    String USER_API = BASE_API_URL + "/user";
    String SIGN_UP = "/sign-up";
    String AUTH_INFO = "/info";
    String MEMBER = "/member";

    String SKILL = "/skill";
    String VERIFY_MAIL = "/verify-account";

    String ACTIVATE_CODE = "/{activate_code}";

    // Referral APIs
    String REFERRAL_API = BASE_API_URL + "/referral";
    String REFERRAL_REWARD_PAYMENT = "/reward-payment";
    String PAGE_REFERRAL = "/page-referral";
    String PAGE_REFERRAL_USER = "/page-referral-user";

    // Category APIs
    String CATEGORY_API = BASE_API_URL + "/category";
    String PAGE_CATEGORY = "/page-category";


    // Skill APIs
    String SKILL_API = BASE_API_URL + "/skill";
    String PAGE_SKILL = "/page-skill";
    String SKILL_PUBLIC = "/skill-public";
    String UPLOAD_FILE = "/upload-file";


    String PUBLIC = "/public";


    String CHANGE_DISPLAY = "/change-display";

    String ANSWER_API = BASE_API_URL + "/answer";

    String QUESTION_API = BASE_API_URL + "/question";

    String SKILL_TEST_API = BASE_API_URL + "/skill-test";

    String DEACTIVATE_SKILL = "/deactivate";

    // Company APIs

    String COMPANY_API = BASE_API_URL + "/company";
    String PAGE_COMPANY = "/page/company";

    String COMPANY_LIST = "/company/list";


    // Member Company APIs
    String MEMBER_COMPANY_API =  "/member-company";

    String MEMBER_API_GET =  "/member/{id}";

    String MEMBER_API_PAGE = "/memberCompany";

    String MEMBER_API_DELETE = "/member/{id}";

    String MEMBER_API_LIST = "/members" ;

    String MEMBER_API_UPDATE = "/updateMember/{id}";


    // Job APIs

    String JOB_API = BASE_API_URL + "/job";

}
