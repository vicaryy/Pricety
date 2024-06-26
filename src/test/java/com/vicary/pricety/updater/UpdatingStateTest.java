package com.vicary.pricety.updater;

import com.vicary.pricety.exception.ScraperBotException;
import com.vicary.pricety.service.UpdateReceiverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UpdatingStateTest {

    @MockBean
    private AutoUpdater autoUpdater;
    @MockBean
    private UpdateReceiverService updateReceiverService;

    private UpdaterState state;


    @BeforeEach
    void beforeEach() {
        state = new UpdatingState(autoUpdater);
    }

    @Test
    void start_expectThrow() {
        //when
        //then
        assertThrows(ScraperBotException.class, () -> state.start());
    }

    @Test
    void startOnce_expectThrow() {
        //when
        //then
        assertThrows(ScraperBotException.class, () -> state.start());
    }

    @Test
    void stop_expectThrow() {
        //when
        //then
        assertThrows(ScraperBotException.class, () -> state.start());
    }

    @Test
    void isRunning_expectTrue() {
        //when
        //then
        assertTrue(state.isUpdating());
    }

    @Test
    void getState_expectEquals() {
        //when
        //then
        assertEquals("Updating Products", state.getState());
    }

}