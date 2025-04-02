package org.zafu.userservice.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public enum DataUniqueError {
    DUPLICATE_USERNAME("username_uq", "Username is already taken"),
    DUPLICATE_USER_EMAIL("email_uq", "Email is already taken"),
    DUPLICATE_USER_PHONE("phone_uq", "Phone is already taken"),
    ;

    private final String dbMessage;
    private final String userMessage;

    public static String getFriendlyMessage(String dbMessage){
        String lowerCaseDbMessage = dbMessage.toLowerCase();
        for(DataUniqueError error: values()){
            if(lowerCaseDbMessage.contains(error.dbMessage)){
                return error.userMessage;
            }
        }
        return "Data is already taken";
    }
}