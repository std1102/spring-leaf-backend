package com.springleaf.context.comment;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.context.user.UserContext;
import com.springleaf.object.entity.Comment;
import io.ebean.Ebean;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RequestMapping(path = "/comment/delete", type = RequestType.DELETE)
public class DeleteComment extends UserContext {

    private final String COMMENT_ID = "comment_id";

    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        long comment_id = (int) map.get(COMMENT_ID);
        if ($.isEmpty(comment_id)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        Comment comment = Ebean.find(Comment.class).where().eq("id", comment_id).findOne();
        if (comment == null) {
            return error(ErrorCode.ACTION_DENIED);
        }
        if (!comment.getUser().getId().equals(userRecord.getId())) {
            return error(ErrorCode.ACCESS_DENIED);
        }
        comment.delete();
        return result();
    }
}
