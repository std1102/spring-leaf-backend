package com.springleaf.context.subject;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
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

@RequestMapping(type = RequestType.GET, path = "/subject/find")
public class FindSubject extends Context {

    private final String INPUT = "input";

    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException, MessagingException {
        String input = (String) map.get(INPUT);
        if ($.isEmpty(input)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        Set<Subject> subjects = Ebean.find(Subject.class).where()
                .like("name", "%" + input + "%").findSet();
        if (subjects.size() == 0 || subjects == null) {
            return error(ErrorCode.SUBJECT_NOT_FOUND);
        }
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
