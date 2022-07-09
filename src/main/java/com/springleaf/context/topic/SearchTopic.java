package com.springleaf.context.topic;

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

@RequestMapping(path = "/topic/find", type = RequestType.GET)
public class SearchTopic extends Context {

    private final String INPUT = "input";

    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException, MessagingException {
        String input = (String) map.get(INPUT);
        if ($.isEmpty(input)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        Set<Topic> topics = Ebean.find(Topic.class).where()
                .like("name", "%" + input + "%").findSet();
        if (topics.size() == 0 || topics == null) {
            return error(ErrorCode.TOPIC_NOT_FOUND);
        }
        for (Topic topic : topics) {
            topic.getSubject().setTopics(null);
            topic.setPosts(null);
        }
        result.put("topics", topics);
        return result();
    }
}
