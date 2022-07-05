package com.springleaf.context.user;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.database.DataCache;
import com.springleaf.database.DataSourceHandle;
import com.springleaf.object.entity.Post;
import com.springleaf.object.entity.Subject;
import com.springleaf.object.entity.User;
import com.springleaf.object.entity.Vote;
import com.springleaf.object.entity.types.VoteType;
import io.ebean.Ebean;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;

@RequestMapping(path = "/user/vote-post", type = RequestType.POST)
public class VotePost extends UserContext{

    private final String POST_ID = "post_id";
    private final String VOTE_TYPE = "vote_type";

    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        long post_id = (int) map.get(POST_ID);
        String vote_type = (String) map.get(VOTE_TYPE);
        if ($.isMultipleEmpty(post_id, vote_type)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        Post post = Ebean.find(Post.class).where().eq("id", post_id).and().eq("active", true).findOne();
        if (post == null) {
            return error(ErrorCode.ACTION_DENIED);
        }
        Vote vote = new Vote();
        vote.setType(VoteType.valueOf(vote_type));
        User user = new User();
        user.setId(userRecord.getId());
        vote.setUser(user);
        if (post.getVotes().size() == 0) {
            vote.setPost(post);
            vote.save();
        }
        else {
            for (Iterator<Vote> iterator = post.getVotes().iterator(); iterator.hasNext();) {
                Vote voz = iterator.next();
                if (voz.getUser().getId().equals(user.getId())) {
                    if (voz.getType().equals(VoteType.valueOf(vote_type))) {
                        return error(ErrorCode.ACTION_DENIED);
                    }
                    else {
                        voz.setType(VoteType.valueOf(vote_type));
                        voz.setPost(post);
                        voz.save();
                        iterator.remove();
                        post.getVotes().add(voz);
                        post.save();
                    };
                }
            }
        }
        return result();
    }
}
