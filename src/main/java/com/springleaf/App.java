package com.springleaf;

import com.springleaf.common.$;
import com.springleaf.common.Config;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.MultiThreadSystem;
import com.springleaf.database.DataCache;
import com.springleaf.database.DataSourceHandle;
import com.springleaf.object.entity.Post;
import com.springleaf.object.entity.Subject;
import com.springleaf.object.entity.types.VoteType;
import com.springleaf.rest.Router;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Spring leaf
 *
 */
public class App 
{
    public static void main( String[] args ) throws InstantiationException, IllegalAccessException {
        System.out.println( "\n" +
                "░██████╗██████╗░██████╗░██╗███╗░░██╗░██████╗░  ██╗░░░░░███████╗░█████╗░███████╗\n" +
                "██╔════╝██╔══██╗██╔══██╗██║████╗░██║██╔════╝░  ██║░░░░░██╔════╝██╔══██╗██╔════╝\n" +
                "╚█████╗░██████╔╝██████╔╝██║██╔██╗██║██║░░██╗░  ██║░░░░░█████╗░░███████║█████╗░░\n" +
                "░╚═══██╗██╔═══╝░██╔══██╗██║██║╚████║██║░░╚██╗  ██║░░░░░██╔══╝░░██╔══██║██╔══╝░░\n" +
                "██████╔╝██║░░░░░██║░░██║██║██║░╚███║╚██████╔╝  ███████╗███████╗██║░░██║██║░░░░░\n" +
                "╚═════╝░╚═╝░░░░░╚═╝░░╚═╝╚═╝╚═╝░░╚══╝░╚═════╝░  ╚══════╝╚══════╝╚═╝░░╚═╝╚═╝░░░░░" );
        Config.init();
        MultiThreadSystem.init();
        DataSourceHandle.init();
        Router.init();
        // Interval check subject cache
        Thread thread = new Thread(() -> {
            CacheService.run();
        });
        thread.start();
    }

    @Slf4j
    private static class CacheService{
        public static synchronized void run() {
            DataCache dataCache = DataSourceHandle.getDataCache();
            Map<String, String> cached;
            while (true) {
                cached = dataCache.getAllCategory(DefaultValues.SUBJECT_CATEGORY);
                for (String key : cached.keySet()) {
                    Subject subject = $.parse(cached.get(key), Subject.class);
                    if (subject == null || subject.getVotes() == null) {
                        continue;
                    }
                    long upvote = subject.getVotes().stream().filter(
                            vote -> {
                                return vote.getType().equals(VoteType.UPVOTE);
                            }
                    ).count();
                    long downvote = subject.getVotes().stream().filter(
                            vote -> {
                                return vote.getType().equals(VoteType.DOWNVOTE);
                            }
                    ).count();
                    if (upvote == 0) {
                        upvote = -1;
                    }
                    if (downvote == 0) {
                        downvote = -1;
                    }
//                    if ((upvote / downvote) >= 1.5) {
//                        subject.save();
//                    }
                    subject.save();
                    log.debug("Subject " + subject.getName() + " saved");
                }
                try {
                    // saving memory
                    Thread.sleep(DefaultValues.CACHE_TIME_ALIVE - 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
