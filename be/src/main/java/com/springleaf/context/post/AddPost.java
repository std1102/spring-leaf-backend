package com.springleaf.context.post;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.context.user.UserContext;
import io.ebean.Ebean;
import io.ebean.annotation.Transactional;
import com.springleaf.object.entity.Post;
import com.springleaf.object.entity.Subject;
import com.springleaf.object.entity.Topic;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@RequestMapping(type = RequestType.POST, path = "/post/create-post")
public class AddPost extends UserContext {

    private String ID_SUBJECT = "id_subject";
    private String ID_TOPIC = "id_topic";
    private String CONTENT = "content";

    @Transactional
    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        long id_subject = (int) map.get(ID_SUBJECT);
        long id_topic = (int) map.get(ID_TOPIC);
        String content = (String) map.get(CONTENT);
        if ($.isMultipleEmpty(id_subject, id_topic, content)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        Subject subject = Ebean.find(Subject.class)
                .where()
                .eq("id", id_subject).findOne();
        if (subject == null || !subject.getActive()) {
            return error(ErrorCode.SUBJECT_NOT_FOUND);
        }
        boolean find = false;
        Topic topic = null;
        for (Topic _topic : subject.getTopics()) {
            if (_topic.getId().equals(id_topic) || !_topic.getActive()) {
                topic = _topic;
            }
        }
        if (topic == null) {
            return error(ErrorCode.TOPIC_NOT_FOUND);
        }
        Post post = new Post();
        post.setActive(true);
        post.setCreate_date(new Date());
        post.setUser(userRecord);
        post.setTopic(topic);
        post.setContent(content);
        post.save();
        return result();
    }
}
