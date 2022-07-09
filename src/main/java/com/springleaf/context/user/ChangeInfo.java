package com.springleaf.context.user;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.RequestType;
import io.ebean.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RequestMapping(path = "/user/change-info", type = RequestType.POST)
public class ChangeInfo extends UserContext{

    private String EMAIL = "email";
    private String FIRST_NAME = "first_name";
    private String LAST_NAME = "last_name";
    private String DOB = "dob";
    private String BIO = "bio";

    @Override
    @Transactional
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        String email = (String) map.get(EMAIL);
        String first_name = (String) map.get(FIRST_NAME);
        String dob = (String) map.get(DOB);
        String last_name = (String) map.get(LAST_NAME);
        String bio = (String) map.get(BIO);
        if (!$.isEmpty(email)) {
            result.put("New email", email);
            userRecord.setEmail(email);
        }
        if (!$.isEmpty(first_name)) {
            result.put("New frist name", first_name);
            userRecord.setFirst_name(first_name);
        }
        if (!$.isMultipleEmpty(last_name)) {
            result.put("New last name", last_name);
            userRecord.setLast_name(last_name);
        }
        if (!$.isEmpty(dob)) {
            Date actualDob = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
            result.put("New DOB", dob);
            userRecord.setDob(actualDob);
        }
        if (!$.isEmpty(bio)) {
            result.put("New bio", "updated");
            userRecord.setBiography(bio);
        }
        userRecord.save();
        return result();
    }
}
