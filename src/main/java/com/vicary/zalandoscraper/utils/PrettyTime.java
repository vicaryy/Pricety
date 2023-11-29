package com.vicary.zalandoscraper.utils;

import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.messages.Messages;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PrettyTime {

    public static String get(LocalDateTime localDateTime) {
        Long updateTime = localDateTime.toEpochSecond(ZoneOffset.ofHours(1));

        Long currentTime = Instant.now().getEpochSecond();

        if (currentTime - updateTime < 0)
            throw new IllegalArgumentException("Operates only on past timestamps.");

        int ago = (int) (currentTime - updateTime);
        int minutes = ago / 60;
        int hours = ago / 3600;
        int days = ago / 86400;

        String when = "";

        if (minutes == 0)
            when = Messages.pretty("moment");

        else if (hours == 0 && days == 0)
            when = String.format("%d %s", minutes, minutes < 2 ? Messages.pretty("minute") : Messages.pretty("minutes"));

        else if (hours != 0 && days == 0)
            when = String.format("%d %s", hours, hours < 2 ? Messages.pretty("hour") : Messages.pretty("hours"));

        else
            when = String.format("%d %s", days, days < 2 ? Messages.pretty("day") : Messages.pretty("days"));

        return when + Messages.pretty("ago");
    }

    static String get(LocalDateTime localDateTime, InstantTime instantTime) {
        Long updateTime = localDateTime.toEpochSecond(ZoneOffset.ofHours(1));

        Long currentTime = instantTime.nowInSeconds();

        if (currentTime - updateTime < 0)
            throw new IllegalArgumentException("Operates only on past timestamps.");

        int ago = (int) (currentTime - updateTime);
        int minutes = ago / 60;
        int hours = ago / 3600;
        int days = ago / 86400;

        String when = "";

        if (minutes == 0)
            when = Messages.pretty("moment");

        else if (hours == 0 && days == 0)
            when = String.format("%d %s", minutes, minutes < 2 ? Messages.pretty("minute") : Messages.pretty("minutes"));

        else if (hours != 0 && days == 0)
            when = String.format("%d %s", hours, hours < 2 ? Messages.pretty("hour") : Messages.pretty("hours"));

        else
            when = String.format("%d %s", days, days < 2 ? Messages.pretty("day") : Messages.pretty("days"));

        return when + Messages.pretty("ago");
    }
}
