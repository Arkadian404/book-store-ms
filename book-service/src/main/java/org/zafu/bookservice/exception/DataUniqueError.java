package org.zafu.bookservice.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public enum DataUniqueError {
    DUPLICATE_ISBN("isbn_uq", "ISBN is already taken"),
    DUPLICATE_PUBLISHER_NAME("publisher_name_uq", "Publisher name is already taken"),
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