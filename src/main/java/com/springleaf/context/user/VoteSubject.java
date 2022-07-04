package com.springleaf.context.user;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.database.DataCache;
import com.springleaf.database.DataSourceHandle;
import com.springleaf.object.entity.Subject;
import com.springleaf.object.entity.User;
import com.springleaf.object.entity.Vote;
import com.springleaf.object.entity.types.VoteType;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@RequestMapping(type = RequestType.POST, path = "/subject/vote")
public class VoteSubject extends UserContext{

    private static final String SUBJECT_ID = "subject_id";

    private static final String VOTE_TYPE = "vote_type";

    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        String subject_id = (String) map.get(SUBJECT_ID);
        String vote_type = (String) map.get(VOTE_TYPE);
        if ($.isMultipleEmpty(subject_id, vote_type)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        DataCache dataCache = DataSourceHandle.getDataCache();
        String subjectJson = dataCache.getAllCategory(DefaultValues.SUBJECT_CATEGORY).get(subject_id);
        if ($.isEmpty(subjectJson)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        Subject subject = $.parse(subjectJson, Subject.class);
        if (subject.getVotes() == null) {
            subject.setVotes(new HashSet<>());
        }
        Vote vote = new Vote();
        vote.setType(VoteType.valueOf(vote_type));
        User user = new User();
        user.setId(userRecord.getId());
        vote.setUser(user);
        if (subject.getVotes().size() == 0) {
            subject.getVotes().add(vote);
        }
        else {
            for (Iterator<Vote> iterator = subject.getVotes().iterator(); iterator.hasNext();) {
                Vote voz =  iterator.next();
                if (voz.getUser().getId().equals(user.getId())) {
                    if (voz.getType().equals(VoteType.valueOf(vote_type))) {
                        return error(ErrorCode.ACTION_DENIED);
                    }
                    else {
                        voz.setType(VoteType.valueOf(vote_type));
                        iterator.remove();
                        subject.getVotes().add(voz);
                    };
                }
            }
        }
        dataCache.getAllCategory(DefaultValues.SUBJECT_CATEGORY).replace(subject_id, $.toString(subject));
        return result();
    }
}
