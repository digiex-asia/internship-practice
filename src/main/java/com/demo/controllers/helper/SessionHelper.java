package com.demo.controllers.helper;

import com.demo.common.utilities.Constant;
import com.demo.common.utilities.DateUtil;
import com.demo.common.utilities.UniqueID;
import com.demo.entities.Session;
import com.demo.entities.User;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author DigiEx Group
 */
@Component
public class SessionHelper {

    SimpleDateFormat sdf = new SimpleDateFormat(Constant.API_FORMAT_DATE);

    public Session createSession(User user, boolean keepLogin) {
        //Create new session
        Session session = new Session();
        session.setId(UniqueID.getUUID());
        String userId = user.getId();
        session.setUserId(userId);
        session.setCreatedDate(DateUtil.convertToUTC(new Date()).getTime());
        if(keepLogin){
            try {
                session.setExpiryDate(sdf.parse("12/31/9999 00:00:00"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            // set them 1 day
            session.setExpiryDate(DateUtil.convertToUTC(DateUtil.addDate(new Date(), TimeZone.getDefault(), 1)));
        }
        return session;
    }

}
