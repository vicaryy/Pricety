package com.vicary.pricety.format;

import com.vicary.pricety.exception.IllegalInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarkdownV2Test {

    @Test
    void applyWithOtherFormats_expectEquals_TextWithBold() {
        //given
        String givenText = "*siema*";
        String expectedText = "*siema*";

        //when
        //then
        assertEquals(expectedText, MarkdownV2.applyWithManualBoldAndItalic(givenText));
    }

    @Test
    void applyWithOtherFormats_expectEquals_TextWithoutMarkdownCharacters() {
        //given
        String givenText = "siema";
        String expectedText = "siema";

        //when
        //then
        assertEquals(expectedText, MarkdownV2.applyWithManualBoldAndItalic(givenText));
    }

    @Test
    void applyWithOtherFormats_expectEquals_TextWithBoldAndOtherMarkdownCharacters() {
        //given
        String givenText = "*si.ema*";
        String expectedText = "*si\\.ema*";

        //when
        //then
        assertEquals(expectedText, MarkdownV2.applyWithManualBoldAndItalic(givenText));
    }

    @Test
    void applyWithOtherFormats_expectEquals_TextWithBoldAndItalicAndOtherMarkdownCharacters() {
        //given
        String givenText = "*si.ema* co tam słychać _hmm_!";
        String expectedText = "*si\\.ema* co tam słychać _hmm_\\!";

        //when
        //then
        assertEquals(expectedText, MarkdownV2.applyWithManualBoldAndItalic(givenText));
    }

    @Test
    void applyWithOtherFormats_expectThrow_TextWithBoldButTheyAreNotEven() {
        //given
        String givenText = "*si.ema* co t*am słychać _hmm_!";

        //when
        //then
        assertThrows(IllegalInputException.class, () -> MarkdownV2.applyWithManualBoldAndItalic(givenText));
    }

    @Test
    void applyWithOtherFormats_expectThrow_TextWithItalicButTheyAreNotEven() {
        //given
        String givenText = "*si.ema* co tam słychać _hm_m_!";

        //when
        //then
        assertThrows(IllegalInputException.class, () -> MarkdownV2.applyWithManualBoldAndItalic(givenText));
    }
}