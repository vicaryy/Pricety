package com.vicary.zalandoscraper.tag;

public enum Tag {
    ONE_SIZE("span.sDq_FX._2kjxJ6.dgII7d.Yb63TQ"),
    NAME("span._ZDS_REF_SCOPE_._5FHGm_"),
    DESCRIPTION("span.EKabf7.R_QwOV"),
    PRICE("span.sDq_FX._4sa1cA"),
    ALL_SIZES("span.sDq_FX._2kjxJ6.dgII7d"),
    ALL_AVAILABLE_SIZES("span[class='sDq_FX _2kjxJ6 dgII7d HlZ_Tf'"),
    ALL_SIZES_ELEMENT("div[class='F8If-J mZoZK2 KLaowZ hj1pfK _8n7CyI JCuRr_'"),
    COOKIES_BUTTON("button[class='uc-btn uc-btn-default btn-deny'"),
    SIZE_BUTTON("//*[@id=\"picker-trigger\"]"),
    LINK_VALIDATION("div.L5YdXz._0xLoFW._7ckuOK.mROyo1.s8As-K"),
    SOLD_OUT("h2[class='sDq_FX MnJKTe FxZV-M HlZ_Tf'");

    private final String tag;

    Tag(String tag) {
        this.tag = tag;
    }

    public String get() {
        return tag;
    }
}

