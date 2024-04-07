package com.vicary.pricety.api_telegram.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vicary.pricety.api_telegram.api_object.ApiObject;
import com.vicary.pricety.api_telegram.api_object.User;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "status"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ChatMemberOwner.class, name = "creator"),
        @JsonSubTypes.Type(value = ChatMemberAdministrator.class, name = "administrator"),
        @JsonSubTypes.Type(value = ChatMemberMember.class, name = "member"),
        @JsonSubTypes.Type(value = ChatMemberRestricted.class, name = "restricted"),
        @JsonSubTypes.Type(value = ChatMemberLeft.class, name = "left"),
        @JsonSubTypes.Type(value = ChatMemberBanned.class, name = "kicked")
})
public interface ChatMember extends ApiObject {
    String getStatus();

    User getUser();
}
