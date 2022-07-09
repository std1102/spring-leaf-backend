package com.springleaf.context.subject;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.RequestType;
import com.springleaf.context.Context;
import com.springleaf.database.DataCache;
import com.springleaf.database.DataSourceHandle;
import com.springleaf.database.imcache.InternalCache;
import com.springleaf.object.entity.Subject;
import com.springleaf.object.entity.types.VoteType;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RequestMapping(path = "/subject-cached", type = RequestType.GET)
public class GetCurrentSubjectCache extends Context {
    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException, MessagingException {
        DataCache dataCache = DataSourceHandle.getDataCache();
        List<Subject> subjects = new LinkedList<>();
        Map<String, String> cached = dataCache.getAllCategory(DefaultValues.SUBJECT_CATEGORY);
        for (String key : cached.keySet()) {
            Subject subject = $.parse(cached.get(key), Subject.class);
            subjects.add(subject);
        }
        result.put("subjects", subjects);
        return result();
    }
}
