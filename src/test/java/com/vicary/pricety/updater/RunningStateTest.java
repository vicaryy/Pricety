package com.vicary.pricety.updater;

import com.vicary.pricety.exception.ScraperBotException;
import com.vicary.pricety.service.UpdateReceiverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RunningStateTest {

    @MockBean
    private AutoUpdater autoUpdater;
    @MockBean
    private UpdateReceiverService updateReceiverService;

    private UpdaterState state;


    @BeforeEach
    void beforeEach() {
        state = new RunningState(autoUpdater);
    }

    @Test
    void start_expectThrow() {
        //given
        //when
        //then
        assertThrows(ScraperBotException.class, () -> state.start());
    }

    @Test
    void startOnce_expectThrow() {
        //given
        //when
        //then
        assertThrows(ScraperBotException.class, () -> state.start());
    }

    @Test
    void stop_JustStop() {
        //given
        Thread givenRunningThread = mock(Thread.class);

        //when
        when(autoUpdater.getRunningThread()).thenReturn(givenRunningThread);
        state.stop();

        //then
        verify(autoUpdater, times(1)).getRunningThread();
        verify(givenRunningThread, times(1)).interrupt();
        verify(autoUpdater, times(1)).setState(any(StopState.class));
    }

    @Test
    void isRunning() {
        //given
        //when
        //then
        assertFalse(state.isUpdating());
    }

    @Test
    void getState_expectEquals() {
        //given
        //when
        //then
        assertEquals("Running", state.getState());
    }
}