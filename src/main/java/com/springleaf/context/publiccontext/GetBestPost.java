package com.springleaf.context.publiccontext;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.RequestType;
import com.springleaf.context.Context;
import com.springleaf.database.DataCache;
import com.springleaf.database.DataSourceHandle;
import com.springleaf.database.imcache.InternalCache;
import com.springleaf.database.redis.RedisDatBase;
import io.ebean.Ebean;
import lombok.extern.slf4j.Slf4j;
import com.springleaf.object.entity.Post;
import com.springleaf.object.entity.Vote;
import com.springleaf.object.entity.types.VoteType;
import redis.clients.jedis.Jedis;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Slf4j
@RequestMapping(path = "/get-posts", type = RequestType.GET)
public class GetBestPost extends Context {

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
                        .fetch("votes")
                        .where().eq("type", VoteType.UPVOTE)
                        .findSet();
        dataCache.delete("posts_cached");
        dataCache.put("posts_cached", $.toString(posts), DefaultValues.CACHE_TIME_ALIVE);
        result.put("posts", posts);
        return result();
    }
}
