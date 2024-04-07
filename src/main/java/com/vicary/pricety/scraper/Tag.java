package com.vicary.pricety.scraper;

class Tag {
    static class Zalando {
        static final String ONE_VARIANT_TAB = "span.sDq_FX._2kjxJ6.dgII7d.Yb63TQ";
        static final String NAME = "span._ZDS_REF_SCOPE_._5FHGm_";
        static final String DESCRIPTION = "span.EKabf7.R_QwOV";
        static final String PHOTO_URL = ".LiPgRT.DlJ4rT.S3xARh:first-of-type img";
        static final String PRICE_SPAN = "span.sDq_FX._4sa1cA";
        static final String PRICE_P = "div.hD5J5m > div > div > p.sDq_FX._4sa1cA";
        static final String PRICE_P_SECOND_VARIANT = "div.hD5J5m > div > div > div > p.sDq_FX._4sa1cA";
        static final String MAIN_TAB = "div._5qdMrS.VHXqc_.rceRmQ._4NtqZU.mIlIve";
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
        static final String PHOTO_URL = ".product-images__main picture source";
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
        static final String PHOTO_URL = ".css-jpr23i img:first-of-type";
        static final String PRICE = "div.product-price.is--current-price";
        static final String VARIANT_TAB = "label.css-xf3ahq";
    }

    static class House {
        static final String SOLD_OUT_TAB = "product-unavailable";
        static final String PRICE = "div.basic-pricestyled__StyledBasicPrice-sc-1tz47jj-0.hfTNOq.basic-price";
        static final String CURRENCY = "span.currencycomponent__Currency-sc-1bzking-0.bdfSnQ.currency";
        static final String PHOTO_URL = ".image-wrapperstyled__ImageWrapper-vn5fec-0:first-of-type img";
        static final String NAME = "div.titlestyled__StyledTitle-urmrll-1.cnhsCB";
        static final String ONE_VARIANT = "size-picker-size-name";
        static final String COOKIES_BUTTON = "button#cookiebotDialogOkButton";
        static final String ALL_VARIANTS_TAB = "li.itemstyled__ItemStyled-sc-1p6n2ae-0.zwcmQ";
        static final String AVAILABLE_VARIANTS_TAB = "size";
        static final String NON_AVAILABLE_VARIANTS_TAB = "size-inactive";
        static final String PRODUCT_TAB = "div.desktop__RightSection-sc-19wvx4y-1.llWjxx";
    }

    static class Zara {
        static final String ADD_TO_CART_BUTTON = "button.zds-button.product-cart-buttons__button.product-cart-buttons__add-to-cart.zds-bt-none";
        static final String PRICE = "span.price-current__amount";
        static final String NAME = "h1.product-detail-info__header-name";
        static final String PHOTO_URL = ".product-detail-images__image-wrapper:first-of-type img";
        static final String PRODUCT_TAB = "div.product-detail-info";
        static final String INTERNATIONAL_EXIT_BUTTON = "svg.zds-dialog-icon-button__icon.zds-dialog-close-button__icon";
        static final String CURRENT_COLOR = "p.product-color-extended-name.product-detail-color-selector__selected-color-name";
        static final String ALL_VARIANTS_TEXT_TAB = "div.product-size-info__main-label";
        static final String ALL_VARIANTS_TAB = "li.size-selector-list__item";
        static final String NON_AVAILABLE_VARIANTS_TAB = "li.size-selector-list__item--is-disabled";
        static final String ALL_COLORS_TAB = "li.product-detail-color-selector__color";
    }
}