package com.vicary.zalandoscraper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PrettyTime {

    public static String get(LocalDateTime localDateTime) {

        Long updateTime = localDateTime.toEpochSecond(ZoneOffset.ofHours(1));

        Long currentTime = Instant.now().getEpochSecond();


        int ago = (int) (currentTime - updateTime);
        int minutes = ago / 60;
        int hours = ago / 3600;
        int days = ago / 86400;

        String when = "";

        if (minutes == 0)
            when = "a moment";

        else if (hours == 0 && days == 0)
            when = String.format("%d %s", minutes, minutes < 2 ? "minute" : "minutes");

        else if (hours != 0 && days == 0)
            when = String.format("%d %s", hours, hours < 2 ? "hour" : "hours");

        else
            when = String.format("%d %s", days, days < 2 ? "day" : "days");

        return when + " ago";
    }
}
