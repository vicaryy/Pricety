package com.vicary.zalandoscraper.api_telegram.api_request.bot_info;

import com.vicary.zalandoscraper.api_telegram.api_object.User;
import lombok.Data;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import com.vicary.zalandoscraper.api_telegram.service.EndPoint;

@Data
public class GetMe implements ApiRequest<User> {
    /**
     * A simple method for testing your bot's authentication token. Requires no parameters. Returns basic information about the bot in form of a User object.
     * @return User class.
     */
    @Override
    public User getReturnObject() {
        return new User();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_ME.getPath();
    }

    @Override
    public void checkValidation() {
        /**
         * Nothing to see here.
         */
    }
}
