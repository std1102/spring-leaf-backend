package com.springleaf.common;

public enum ErrorCode {
    OK (0, "OKE"),
    FILE_NOT_FOUND (1, "File not found"),
    MISSING_REQUIRE_PROPERTIES (2, "Invalid input"),
    INTERNAL_ERROR (3, "Internal error"),
    EMPTY_FOLDER (4, "Folder is empty"),
    FOLDER_NOT_FOUND(5, "Folder not found"),
    OWNER_NOT_EXIST(6, "Owner not exist"),
    ACCESS_DENIED(7, "Access denied"),
    FOLDER_EXIST(8, "Folder exist"),
    FILE_REQUIRED(9, "Require a file"),
    FOLDER_REQUIRED(10, "Require a folder"),
    FILE_TEMPORARY_DELETED(11, "File is temporary deleted"),
    FILE_CORRUPTED(12, "File is corrupted"),
    USERNAME_DUPLICATED(13, "Username is duplicated"),
    TOKEN_EXPIRED(14, "Token expired"),
    OLD_PASSWORD_NOT_MATCH(15, "Password not match"),
    SAME_PASSWORD(16, "Old password and new password is the same"),
    SUBJECT_NOT_FOUND(17, "Subject not found"),
    TOPIC_NOT_FOUND(18, "Topic not found");

    public final int code;
    public final String desc;

    ErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static ErrorCode value(int code) {
        for (ErrorCode errorCode: ErrorCode.values()) {
            if (errorCode.code == code) return errorCode;
        }
        return null;
    }
}
