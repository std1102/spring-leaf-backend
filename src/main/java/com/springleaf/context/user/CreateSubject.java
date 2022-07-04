package com.springleaf.context.user;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.database.DataCache;
import com.springleaf.database.DataSourceHandle;
import com.springleaf.object.entity.Subject;
import com.springleaf.object.entity.types.SubjectType;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RequestMapping(path = "/subject/create", type = RequestType.POST)
public class CreateSubject extends UserContext{
    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        String name = (String) map.get("name");
        if ($.isEmpty(name)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        Subject subject = new Subject();
        subject.setName(name);
        DataCache dataCache = DataSourceHandle.getDataCache();
        dataCache.putCategory(DefaultValues.SUBJECT_CATEGORY, request_id, $.toString(subject), DefaultValues.CACHE_TIME_ALIVE);
        return result();
    }
}
