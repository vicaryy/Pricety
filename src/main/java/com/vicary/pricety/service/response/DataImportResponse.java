package com.vicary.pricety.service.response;

import com.vicary.pricety.api_telegram.service.QuickSender;
import com.vicary.pricety.entity.DataImportEntity;
import com.vicary.pricety.exception.IllegalInputException;
import com.vicary.pricety.messages.Messages;
import com.vicary.pricety.thread_local.ActiveUser;

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

        if (dataImport.getMethod().equalsIgnoreCase("get")) {
            responseFacade.updateWebUserByTelegram(dataImport.getEmail(), user.getTelegramId());
            quickSender.message(user.getChatId(), Messages.other("importDataGet"), false);
        }
        if (dataImport.getMethod().equalsIgnoreCase("send")) {
            responseFacade.updateTelegramByWebUser(dataImport.getEmail(), user.getTelegramId());
            quickSender.message(user.getChatId(), Messages.other("importDataSend"), false);
        }

        responseFacade.deleteAllDataImportByEmail(dataImport.getEmail());
    }
}
















