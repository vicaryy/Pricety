package com.vicary.pricety.service.response;

import com.vicary.pricety.api_telegram.service.QuickSender;
import com.vicary.pricety.messages.Messages;
import com.vicary.pricety.service.response.inline_markup.InlineKeyboardMarkupFactory;
import com.vicary.pricety.thread_local.ActiveUser;

public class CommandResponse implements Responser {
    private final ResponseFacade responseFacade;
    private final ActiveUser user;
    private final QuickSender quickSender;

    public CommandResponse(ResponseFacade responseFacade, ActiveUser user) {
        this.responseFacade = responseFacade;
        this.user = user;
        this.quickSender = new QuickSender();
    }

    public CommandResponse(ResponseFacade responseFacade, ActiveUser user, QuickSender quickSender) {
        this.responseFacade = responseFacade;
        this.user = user;
        this.quickSender = quickSender;
    }


    @Override
    public void response() {
        String text = user.getText();
        if (text.equals("/start"))
            sendStart();

        else if (text.equals("/menu"))
            sendMenu();

        else if (text.equals("/update"))
            sendUpdate();

        else if (text.equals("/limits"))
            sendLimits();

        else if (text.equals("/help"))
            sendHelp();

        else if (text.equals("/tip"))
            sendTip();
    }

    private void sendStart() {
        String displayedNick = user.getNick();
        if (displayedNick == null)
            displayedNick = "";

        displayedNick = " " + displayedNick;

        quickSender.message(user.getChatId(), Messages.command("start").formatted(displayedNick), true);
    }

    private void sendMenu() {
        quickSender.inlineMarkup(
                user.getChatId(),
                Messages.menu("welcome"),
                InlineKeyboardMarkupFactory.getMenu(),
                true);
    }

    private void sendUpdate() {
        String message = Messages.command("update").formatted(responseFacade.getLastUpdateTime());
        quickSender.message(user.getChatId(), message, true);
    }

    private void sendLimits() {
        quickSender.message(user.getChatId(), Messages.command("limits"), true);
    }

    private void sendHelp() {
        quickSender.message(user.getChatId(), Messages.command("help"), true);
    }

    private void sendTip() {
        quickSender.message(user.getChatId(), Messages.command("tip"), true);
    }
}





















