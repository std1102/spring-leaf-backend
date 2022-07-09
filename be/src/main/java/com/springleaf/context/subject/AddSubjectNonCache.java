package com.springleaf.context.subject;

import com.springleaf.annotation.RequestMapping;
import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.ErrorCode;
import com.springleaf.common.RequestType;
import com.springleaf.context.user.UserContext;
import com.springleaf.database.DataCache;
import com.springleaf.database.DataSourceHandle;
import com.springleaf.object.entity.Subject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RequestMapping(path = "/subject/create", type = RequestType.POST)
public class AddSubjectNonCache extends UserContext {
    @Override
    protected Object _process(Map<String, Object> map) throws IOException, ParseException {
        String name = (String) map.get("name");
        if ($.isEmpty(name)) {
            return error(ErrorCode.MISSING_REQUIRE_PROPERTIES);
        }
        Subject subject = new Subject();
        subject.setName(name);
        subject.setActive(true);
        subject.save();
        return result();
    }
}
