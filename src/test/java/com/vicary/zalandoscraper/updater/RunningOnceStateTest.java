package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RunningOnceStateTest {

    @MockBean
    private AutoUpdater autoUpdater;
    @MockBean
    private UpdateReceiverService updateReceiverService;

    private UpdaterState state;

    @BeforeEach
    void beforeEach() {
        state = new RunningOnceState(autoUpdater);
    }


    @Test
    void start_expectThrow() {
        //given
        //when
        //then
        assertThrows(ZalandoScraperBotException.class, () -> state.start());
    }

    @Test
    void startOnce_expectThrow() {
        //given
        //when
        //then
        assertThrows(ZalandoScraperBotException.class, () -> state.startOnce());
    }

    @Test
    void stop_expectThrow() {
        //given
        //when
        //then
        assertThrows(ZalandoScraperBotException.class, () -> state.stop());
    }

    @Test
    void isRunning_expectTrue() {
        //given
        //when
        //then
        assertTrue(state.isRunning());
    }

    @Test
    void getState_expectEquals() {
        //given
        //when
        //then
        assertEquals("Running Once", state.getState());
    }
}












