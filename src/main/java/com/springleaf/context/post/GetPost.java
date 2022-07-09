package com.springleaf.context.post;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.context.Context;
import com.springleaf.object.entity.Comment;
import com.springleaf.object.entity.Post;
import com.springleaf.object.entity.User;
import com.springleaf.object.entity.Vote;
import io.ebean.Ebean;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RequestMapping(path = "/post/get", type = RequestType.GET)
public class GetPost extends Context {

    private final String POST_ID = "post_id";

    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException, MessagingException {
        long post_id = Long.parseLong(map.get(POST_ID) + "");
        if ($.isEmpty(post_id)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        Post post = Ebean.find(Post.class).where().eq("id", post_id).and().eq("active", true).findOne();
        if (post == null) {
            return error(ErrorCode.POST_NOT_FOUND);
        }
        for (Vote voz : post.getVotes()) {
            voz.setUser(null);
            voz.setPost(null);
        }
        for (Comment comment : post.getComments()) {
            comment.getUser().setLogin(null);
            comment.getUser().setRoles(null);
            comment.getUser().setBiography(null);
            comment.getUser().setLast_change_password(null);
            comment.getUser().setDob(null);
            comment.getUser().setEmail(null);
            comment.getUser().setActive(null);
            comment.getUser().setStatus(null);
            comment.setPost(null);
        }
        result.put("post", post);
        return result();
    }
}
