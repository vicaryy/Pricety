package com.vicary.zalandoscraper.api_request.bot_info;

import lombok.Data;
import com.vicary.zalandoscraper.api_object.User;
import com.vicary.zalandoscraper.api_request.ApiRequest;
import com.vicary.zalandoscraper.end_point.EndPoint;

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
