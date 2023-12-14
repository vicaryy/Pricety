package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class StopStateTest {

    @MockBean
    private AutoUpdater autoUpdater;

    @MockBean
    private UpdateReceiverService updateReceiverService;

    private UpdaterState state;


    @BeforeEach
    void beforeEach() {
        state = new StopState(autoUpdater);
    }

    @Test
    void start_JustStart() {
        //given
        //when
        state.start();

        //then
        verify(autoUpdater, times(1)).startRunningThread(any(Runnable.class));
    }

    @Test
    void startOnce_JustStartOnce() {
        //given
        //when
        state.start();

        //then
        verify(autoUpdater, times(1)).startRunningThread(any(Runnable.class));
    }

    @Test
    void stop_expectThrow() {
        //given
        //when
        //then
        assertThrows(ZalandoScraperBotException.class, () -> state.stop());
    }

    @Test
    void isRunning_expectFalse() {
        //given
        //when
        //then
        assertFalse(state.isRunning());
    }

    @Test
    void getState_expectEquals() {
        //given
        //when
        //then
        assertEquals("Stopped", state.getState());
    }
}