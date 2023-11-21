package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;

public class CommandResponse implements Responser {
    private final ResponseFacade responseFacade;
    private final ActiveUser user;

    public CommandResponse(ResponseFacade responseFacade, ActiveUser user) {
        this.responseFacade = responseFacade;
        this.user = user;
    }


    @Override
    public void response() {
        String text = user.getText();
        if (text.equals("/start"))
            sendStart();

        else if (text.equals("/menu"))
            sendMenuBlocks();

        else if (text.equals("/update"))
            sendUpdate();

        else if (text.equals("/limits"))
            sendLimits();

        else if (text.equals("/help"))
            sendHelp();

        else if (text.equals("/tip"))
            sendTip();
    }

    public void sendStart() {
        String displayedNick = user.getNick();
        if (displayedNick == null)
            displayedNick = "";

        displayedNick = " " + displayedNick;

        QuickSender.message(user.getChatId(), Messages.command("start").formatted(displayedNick), true);
    }

    public void sendMenuBlocks() {
        QuickSender.message(InlineBlock.getMenu());
    }

    private void sendUpdate() {
        String message = Messages.command("update").formatted(responseFacade.getLastUpdateTime());
        QuickSender.message(user.getChatId(), message, true);
    }

    private void sendLimits() {
        QuickSender.message(user.getChatId(), Messages.command("limits"), true);
    }

    private void sendHelp() {
        QuickSender.message(user.getChatId(), Messages.command("help"), true);
    }

    private void sendTip() {
        QuickSender.message(user.getChatId(), Messages.command("tip"), true);
    }
}





















