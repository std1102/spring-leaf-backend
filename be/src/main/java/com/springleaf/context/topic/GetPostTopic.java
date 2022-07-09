package com.springleaf.context.topic;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.context.Context;
import com.springleaf.object.entity.Post;
import com.springleaf.object.entity.Subject;
import com.springleaf.object.entity.Topic;
import com.springleaf.object.entity.User;
import io.ebean.Ebean;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RequestMapping(path = "/topic/get-post", type = RequestType.GET)
public class GetPostTopic extends Context {

    private final String TOPIC_ID = "topic_id";

    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException, MessagingException {
        long topic_id = Long.parseLong(map.get(TOPIC_ID) + "");
        Topic topic = Ebean.find(Topic.class).where().eq("id", topic_id).findOne();
        if (topic == null) {
            return error(ErrorCode.SUBJECT_NOT_FOUND);
        }
        if (topic.getPosts() != null) {
            for (Post post : topic.getPosts()) {
                User user = post.getUser();
                post.setUser(new User());
                post.getUser().setId(user.getId());
                post.getUser().setFirst_name(user.getFirst_name());
                post.getUser().setLast_name(user.getLast_name());
                post.setComments(null);
                post.setLanguage(null);
                post.setTopic(null);
                post.setVotes(null);
            }
        }
        result.put("post", topic.getPosts());
        return result();
    }
}
