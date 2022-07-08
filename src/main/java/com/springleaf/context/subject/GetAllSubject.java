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
import java.util.Set;

@RequestMapping(path = "/subject/all", type = RequestType.GET)
public class GetAllSubject extends Context {
    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException, MessagingException {
        Set<Subject> subjects = Ebean.find(Subject.class).findSet();
        for (Subject subject : subjects) {
            if (subject.getTopics() != null) {
                for (Topic topic : subject.getTopics()) {
                    topic.setSubject(null);
                    topic.setPosts(null);
                }
            }
        }
        result.put("subjects", subjects);
        return result();
    }
}
