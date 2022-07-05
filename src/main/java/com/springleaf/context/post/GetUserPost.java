package com.springleaf.context.post;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.context.user.UserContext;
import com.springleaf.object.entity.Comment;
import com.springleaf.object.entity.Post;
import com.springleaf.object.entity.Vote;
import io.ebean.Ebean;
import javafx.geometry.Pos;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Set;

@RequestMapping(type = RequestType.GET, path = "/post/user-post")
public class GetUserPost extends UserContext {
    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        Set<Post> posts = Ebean.find(Post.class).where().eq("user_id", userRecord.getId()).findSet();
        if (posts.isEmpty()) {
            return error(ErrorCode.POST_NOT_FOUND);
        }
        for (Post post : posts) {
            if (post.getContent().length() >= 100) {
                post.setContent(post.getContent().substring(0, 100));
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
        }
        result.put("posts", posts);
        return result();
    }
}
