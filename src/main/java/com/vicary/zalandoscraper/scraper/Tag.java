package com.vicary.zalandoscraper.scraper;

class Tag {
    static class Zalando {
        static final String ONE_VARIANT_TAB = "span.sDq_FX._2kjxJ6.dgII7d.Yb63TQ";
        static final String NAME = "span._ZDS_REF_SCOPE_._5FHGm_";
        static final String DESCRIPTION = "span.EKabf7.R_QwOV";
        static final String PRICE = "span.sDq_FX._4sa1cA";
        static final String MAIN_TAB = "div._5qdMrS.VHXqc_.rceRmQ._4NtqZU.mIlIve";
//        static final String SOLD_OUT_TAB = "Artykuł wyprzedany";
        static final String SOLD_OUT_TAB = "h2#pdp_size-reminder-dialog_title";
        static final String VARIANT_BUTTON = "pdp-size-picker-trigger";
        static final String COOKIES_BUTTON = "button.uc-btn";
        static final String ALL_VARIANTS = "span.hDNRPv.r9BRio.qXofat.ZkIJC-.pMa0tB.JCuRr_ > div._0xLoFW.FCIprz > span.sDq_FX._2kjxJ6.dgII7d";
        static final String AVAILABLE_VARIANTS = "span.hDNRPv.r9BRio.qXofat.ZkIJC-.pMa0tB.JCuRr_ > div._0xLoFW.FCIprz > span.sDq_FX._2kjxJ6.dgII7d.HlZ_Tf";
        static final String NON_AVAILABLE_VARIANTS = "span.hDNRPv.r9BRio.qXofat.ZkIJC-.pMa0tB.JCuRr_ > div._0xLoFW.FCIprz > span.sDq_FX._2kjxJ6.dgII7d.Yb63TQ";
    }

    static class Hebe {
        static final String MULTI_VARIANT_TAB = "div.swatch__items.swatch__items";
        static final String ALL_VARIANTS = "div.swatch__item.swatch__item";
        static final String WAIT_FOR_CONTENT = "div.product-summary.js-product-summary";
        static final String MAIN_TAB = "div.product-summary__main-box";
        static final String SOLD_OUT_TAB = "Produkt niedostępny online";
        static final String NAME = "p.product-content__brand";
        static final String DESCRIPTION = "p.js-product-short-description";
        static final String PRICE = "div.price-product__wrapper";
        static final String AVAILABLE_VARIANTS = "div.swatch__item--selectable";
        static final String NON_AVAILABLE_VARIANTS = "div.swatch__item--unselectable";
    }

    static class Nike {
        static final String MULTI_VARIANT_TAB = "fieldset.mt5-sm.mb3-sm.body-2.css-1pj6y87";
        static final String SOLD_OUT_TAB = "div.sold-out";
        static final String ONE_VARIANT_TAB = "span.prl0-sm.ta-sm-l.bg-transparent.sizeHeader";
        static final String NOT_AVAILABLE_TAB = "h1.headline-2.not-found";
        static final String MAIN_TAB = "div#RightRail";
        static final String NAME = "h1#pdp_product_title";
        static final String DESCRIPTION = "h2.headline-5.pb1-sm.d-sm-ib";
        static final String PRICE = "div.product-price.is--current-price";
        static final String VARIANT_TAB = "label.css-xf3ahq";
    }
}












