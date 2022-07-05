package com.springleaf.context.user;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.object.entity.Comment;
import com.springleaf.object.entity.Post;
import io.ebean.Ebean;
import javafx.geometry.Pos;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RequestMapping(path = "/user/comment", type = RequestType.POST)
public class CommentPost extends UserContext{

    private final String POST_ID = "post_id";
    private final String PARRENT_ID = "parrent_id";
    private final String CONTENT = "content";

    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        long post_id = (int) map.get(POST_ID);
        String content = (String) map.get(CONTENT);
        if ($.isMultipleEmpty(post_id, content)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        Post post = Ebean.find(Post.class).where().eq("id", post_id).findOne();
        if (post == null) {
            return error(ErrorCode.POST_NOT_FOUND);
        }
        Comment comment = new Comment();
        comment.setActive(true);
        comment.setUser(userRecord);
        comment.setContent(content);
        comment.setPost(post);
        if (map.containsKey(PARRENT_ID)) {
            long parrent_id = (int) map.get(PARRENT_ID);
            Comment parrent = Ebean.find(Comment.class).where().eq("id", parrent_id).findOne();
            if (parrent != null) {
                comment.setParent(parrent);
            }
            else return error(ErrorCode.ACTION_DENIED);
        }
        comment.save();
        return result();
    }
}
