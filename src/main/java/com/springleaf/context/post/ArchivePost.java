package com.springleaf.context.post;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.context.user.UserContext;
import com.springleaf.object.entity.Comment;
import com.springleaf.object.entity.Post;
import com.springleaf.object.entity.Vote;
import io.ebean.Ebean;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RequestMapping(path = "/post/archive", type = RequestType.DELETE)
public class ArchivePost extends UserContext {

    private final String POST_ID = "post_id";

    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        long post_id = Long.parseLong(map.get(POST_ID) + "");
        if ($.isEmpty(post_id)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        Post post = Ebean.find(Post.class).where().eq("id", post_id).and().eq("active", true).findOne();
        if (post == null) {
            return error(ErrorCode.POST_NOT_FOUND);
        }
        post.setActive(false);
        post.save();
        return result();
    }
}
