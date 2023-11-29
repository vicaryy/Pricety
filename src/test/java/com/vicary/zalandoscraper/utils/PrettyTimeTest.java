package com.vicary.zalandoscraper.utils;

import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrettyTimeTest {

    private static InstantTime instantTime;

    private final long mockedTime = 1286701200; // 10.10.2010 10:00   -   dd.mm.yyyy hh:ss

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
    }

    @BeforeEach
    void beforeEach() {
        instantTime = mock(InstantTime.class);
    }

    @Test
    void get_expectEquals_MomentAgo() {
        //given
        LocalDateTime givenTime = LocalDateTime.of(2010, 10, 10, 9, 59, 50);

        String expectedPrettyTime = Messages.pretty("moment") + Messages.pretty("ago");

        //when
        when(instantTime.nowInSeconds()).thenReturn(mockedTime);
        String actualPrettyTime = PrettyTime.get(givenTime, instantTime);

        //then
        assertEquals(expectedPrettyTime, actualPrettyTime);
    }

    @Test
    void get_expectEquals_OneMinuteAgo() {
        //given
        LocalDateTime givenTime = LocalDateTime.of(2010, 10, 10, 9, 58, 45);

        String expectedPrettyTime = "1 " + Messages.pretty("minute") + Messages.pretty("ago");

        //when
        when(instantTime.nowInSeconds()).thenReturn(mockedTime);
        String actualPrettyTime = PrettyTime.get(givenTime, instantTime);

        //then
        assertEquals(expectedPrettyTime, actualPrettyTime);
    }

    @Test
    void get_expectEquals_FiveMinutesAgo() {
        //given
        LocalDateTime givenTime = LocalDateTime.of(2010, 10, 10, 9, 54, 45);

        String expectedPrettyTime = "5 " + Messages.pretty("minutes") + Messages.pretty("ago");

        //when
        when(instantTime.nowInSeconds()).thenReturn(mockedTime);
        String actualPrettyTime = PrettyTime.get(givenTime, instantTime);

        //then
        assertEquals(expectedPrettyTime, actualPrettyTime);
    }

    @Test
    void get_expectEquals_OneHourAgo() {
        //given
        LocalDateTime givenTime = LocalDateTime.of(2010, 10, 10, 8, 54, 45);

        String expectedPrettyTime = "1 " + Messages.pretty("hour") + Messages.pretty("ago");

        //when
        when(instantTime.nowInSeconds()).thenReturn(mockedTime);
        String actualPrettyTime = PrettyTime.get(givenTime, instantTime);

        //then
        assertEquals(expectedPrettyTime, actualPrettyTime);
    }

    @Test
    void get_expectEquals_FiveHoursAgo() {
        //given
        LocalDateTime givenTime = LocalDateTime.of(2010, 10, 10, 4, 54, 45);

        String expectedPrettyTime = "5 " + Messages.pretty("hours") + Messages.pretty("ago");

        //when
        when(instantTime.nowInSeconds()).thenReturn(mockedTime);
        String actualPrettyTime = PrettyTime.get(givenTime, instantTime);

        //then
        assertEquals(expectedPrettyTime, actualPrettyTime);
    }

    @Test
    void get_expectEquals_OneDayAgo() {
        //given
        LocalDateTime givenTime = LocalDateTime.of(2010, 10, 9, 4, 54, 45);

        String expectedPrettyTime = "1 " + Messages.pretty("day") + Messages.pretty("ago");

        //when
        when(instantTime.nowInSeconds()).thenReturn(mockedTime);
        String actualPrettyTime = PrettyTime.get(givenTime, instantTime);

        //then
        assertEquals(expectedPrettyTime, actualPrettyTime);
    }

    @Test
    void get_expectEquals_FiveDaysAgo() {
        //given
        LocalDateTime givenTime = LocalDateTime.of(2010, 10, 5, 4, 54, 45);

        String expectedPrettyTime = "5 " + Messages.pretty("days") + Messages.pretty("ago");

        //when
        when(instantTime.nowInSeconds()).thenReturn(mockedTime);
        String actualPrettyTime = PrettyTime.get(givenTime, instantTime);

        //then
        assertEquals(expectedPrettyTime, actualPrettyTime);
    }

    @Test
    void get_expectEquals_ThreeThousandAndTwoHundredNinetyTwoDays() {
        //given
        LocalDateTime givenTime = LocalDateTime.of(2001, 10, 5, 4, 54, 45);

        String expectedPrettyTime = "3292 " + Messages.pretty("days") + Messages.pretty("ago");

        //when
        when(instantTime.nowInSeconds()).thenReturn(mockedTime);
        String actualPrettyTime = PrettyTime.get(givenTime, instantTime);

        //then
        assertEquals(expectedPrettyTime, actualPrettyTime);
    }

    @Test
    void get_expectEquals_SameTime() {
        //given
        LocalDateTime givenTime = LocalDateTime.of(2010, 10, 10, 10, 00, 00);

        String expectedPrettyTime = Messages.pretty("moment") + Messages.pretty("ago");

        //when
        when(instantTime.nowInSeconds()).thenReturn(mockedTime);
        String actualPrettyTime = PrettyTime.get(givenTime, instantTime);

        //then
        assertEquals(expectedPrettyTime, actualPrettyTime);
    }

    @Test
    void get_expectEquals_FutureTime() {
        //given
        LocalDateTime givenTime = LocalDateTime.of(2011, 10, 10, 10, 00, 00);

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> PrettyTime.get(givenTime, instantTime));
    }
}










