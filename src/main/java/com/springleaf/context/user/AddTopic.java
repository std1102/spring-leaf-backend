package com.springleaf.context.user;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.object.entity.Post;
import com.springleaf.object.entity.Subject;
import com.springleaf.object.entity.Topic;
import io.ebean.Ebean;
import io.ebean.annotation.Transactional;

import javax.persistence.Transient;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@RequestMapping(path = "/topic/add", type = RequestType.POST)
public class AddTopic extends UserContext{
    private String NAME = "name";
    private String SUBJECT_ID = "subject_id";

    @Override
    @Transactional
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        long id_subject = (int) map.get(SUBJECT_ID);
        String name = (String) map.get(NAME);
        if ($.isMultipleEmpty(id_subject, name)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        Subject subject = Ebean.find(Subject.class)
                .where()
                .eq("id", id_subject).findOne();
        if (subject == null || !subject.getActive()) {
            return error(ErrorCode.SUBJECT_NOT_FOUND);
        }
        for (Topic _topic : subject.getTopics()) {
            if (_topic.getName().equals(name)) {
                return error(ErrorCode.ACTION_DENIED);
            }
        }
        Topic topic = new Topic();
        topic.setName(name);
        topic.setSubject(subject);
        topic.save();
        return result();
    }
}
