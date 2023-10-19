package com.vicary.zalandoscraper.api_request.chat.chat_invite;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_request.ApiRequest;
import lombok.*;
import com.vicary.zalandoscraper.api_object.chat.ChatInviteLink;
import com.vicary.zalandoscraper.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class EditChatInviteLink implements ApiRequest<ChatInviteLink> {
    /**
     * Use this method to edit a non-primary invite link created by the bot.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     *
     * @param chatId              Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param inviteLink          The invite link to edit
     * @param name                Invite link name; 0-32 characters
     * @param expireDate          Point in time (Unix timestamp) when the link will expire
     * @param memberLimit         The maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999
     * @param createsJoinRequest  True, if users joining the chat via the link need to be approved by chat administrators.
     * If True, member_limit can't be specified
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("invite_link")
    private String inviteLink;

    @JsonProperty("name")
    private String name;

    @JsonProperty("expire_date")
    private Integer expireDate;

    @JsonProperty("member_limit")
    private Integer memberLimit;

    @JsonProperty("creates_join_request")
    private Boolean createsJoinRequest;

    @Override
    public ChatInviteLink getReturnObject() {
        return new ChatInviteLink();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.EDIT_CHAT_INVITE_LINK.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
