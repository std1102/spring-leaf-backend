package com.springleaf.common;


import java.util.regex.Pattern;

public interface DefaultValues {
    public static final String[] SYSTEM_CHANNEL = new String[] {"hihihi", "hehehe"};
    public static final int RANDOM_STR_LENGTH = 12;
    // TODO expire time
    public static final int SESSION_EXPIRE = 9999999;
    public static final long CACHE_TIME_ALIVE = 3000;
    public static final long CACHE_RESULT_ALIVE = 10;
    public static final String SEPARATOR_STR = ";";
    public static final String MAIL_PROTOCOL= "smtp";
    public static final String MAIL_SMTP_HOST= "smtp.gmail.com";
    public static final String MAIL_SMTP_PORT= "587";
    public static final String MAIL_REGEX= "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String VN_PHONENUMBER_REGEX = "^(0|\\+84|84?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$";
    public static final String IP_REGEX=  "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
    public static final String USER_ID_REGEX=  "^(?=.{6,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
    public static final String DATE_UPLOAD = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
    public static final Pattern DATE_UPLOAD_PATTERN= Pattern.compile(DATE_UPLOAD);
    public static final Pattern MAIL_PATTERN = Pattern.compile(MAIL_REGEX);
    public static final Pattern VN_PHONENUMBER_PATTERN = Pattern.compile(VN_PHONENUMBER_REGEX);
    public static final Pattern IP_PATTERN = Pattern.compile(IP_REGEX);
    public static final Pattern USER_ID_PATTERN = Pattern.compile(USER_ID_REGEX);
    public static final String CONFIG_FILE = "spring-leaf.cfg";
    public static final String DEFAULT_FOLDER = "dms";
    public static final long LIMIT_FILE_SIZE = 5;
    public static final int SALT = 12;
    public static final String AES_KEY = "ktyAUWGmXcHdNtiI0daV+w==";
    public static final String REDIS_HOST= "localhost";
    public static final int REDIS_PORT= 1111;
    public static final int REDIS_POOL_SIZE= 20;
    public static final boolean USE_IM_CACHE = true;
    public static final int THREAD_POOL = 50;
    public static final String SUBJECT_CATEGORY = "subject";
    public static final String SUBJECT_SEPERATOR = "[VOTE]";

}
