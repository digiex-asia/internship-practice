package com.demo.controllers.helper;

import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.Constant;
import com.demo.common.utilities.RestAPIStatus;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateHelper {
    public void checkDate(String Dob) {
        SimpleDateFormat format = new SimpleDateFormat(Constant.API_FORMAT_DATE);
        try {
            format.parse(Dob);


        } catch (ParseException e) {

            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Date format must be MM/dd/yy");
        }
    }
}
