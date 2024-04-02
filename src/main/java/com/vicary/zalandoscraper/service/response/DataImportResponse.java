package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.entity.DataImportEntity;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.thread_local.ActiveUser;

import java.util.Optional;

public class DataImportResponse implements Responser {
    private final ResponseFacade responseFacade;
    private final ActiveUser user;
    private final QuickSender quickSender;

    public DataImportResponse(ResponseFacade responseFacade, ActiveUser user) {
        this.responseFacade = responseFacade;
        this.user = user;
        this.quickSender = new QuickSender();
    }

    public DataImportResponse(ResponseFacade responseFacade, ActiveUser user, QuickSender quickSender) {
        this.responseFacade = responseFacade;
        this.user = user;
        this.quickSender = quickSender;
    }

    @Override
    public void response() {
        DataImportEntity dataImport = responseFacade.getDataImportByRequest(user.getText()).orElseThrow(() -> new IllegalInputException("", "User insert invalid data import code, user: '%s' - '%s'".formatted(user.getUserId(), user.getNick())));

//        if (dataImport.get().getMethod().equalsIgnoreCase("get"))
    }
}
















