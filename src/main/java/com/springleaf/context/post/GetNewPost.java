package com.springleaf.context.post;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.RequestType;
import com.springleaf.context.Context;
import com.springleaf.database.DataCache;
import com.springleaf.database.DataSourceHandle;
import com.springleaf.object.entity.Comment;
import com.springleaf.object.entity.Vote;
import io.ebean.Ebean;
import lombok.extern.slf4j.Slf4j;
import com.springleaf.object.entity.Post;
import com.springleaf.object.entity.types.VoteType;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Slf4j
@RequestMapping(path = "/post/get-posts", type = RequestType.GET)
public class GetNewPost extends Context {

    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException, MessagingException {
        // Caching
        Set<Post> posts = new HashSet<>();
        DataCache dataCache = DataSourceHandle.getDataCache();
        String cachedData = dataCache.get("posts_cached");
        if (!$.isEmpty(cachedData)) {
            posts = $.parse(dataCache.get("posts_cached"), Set.class);
            result.put("posts", posts);
            return result();
        }
        // TODO
        posts = Ebean.find(Post.class)
                        .select("*")
                        .where().eq("active", true)
                        .order().desc("create_date")
                        .findSet();
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
        dataCache.delete("posts_cached");
        dataCache.put("posts_cached", $.toString(posts), DefaultValues.CACHE_TIME_ALIVE);
        result.put("posts", posts);
        return result();
    }
}
