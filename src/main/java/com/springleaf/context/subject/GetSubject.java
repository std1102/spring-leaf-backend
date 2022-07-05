package com.springleaf.context.subject;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.context.Context;
import com.springleaf.object.entity.Subject;
import com.springleaf.object.entity.Topic;
import io.ebean.Ebean;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RequestMapping(path = "/subject/get", type = RequestType.GET)
public class GetSubject extends Context {

    private final String SUBJECT_ID = "subject_id";

    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException, MessagingException {
        long subject_id = Long.parseLong(map.get(SUBJECT_ID) + "");
        Subject subject = Ebean.find(Subject.class).where().eq("id", subject_id).findOne();
        if (subject == null) {
            return error(ErrorCode.SUBJECT_NOT_FOUND);
        }
        if (subject.getTopics() != null) {
            for (Topic topic : subject.getTopics()) {
                topic.setSubject(null);
                topic.setPosts(null);
            }
        }
        result.put("subject", subject);
        return result();
    }
}
