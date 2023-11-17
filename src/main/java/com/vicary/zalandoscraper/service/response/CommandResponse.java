package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import com.vicary.zalandoscraper.format.MarkdownV2;

@Component
public class CommandResponse {

    private final static String START = """
            *Cześć %s* 👋
                        
            Jestem botem który potrafi obserwować ceny produktów na Zalando, wysyłać alerty cenowe na czacie i podany adres email.
                        
            *Jak mnie użyć?* 🎮
            Wystarczy wkleić link do Zalando i wysłać go.
            Ja się zajmę resztą 😉
                        
                        
            Polecam skorzystać z poniższych komend:
            */menu* - wyświetla tabele menu ⚙️
            */update* - data ostatniej aktualizacji produktów 🔄
            */limits* - informacja na temat limitów ⛔️
            */help* - więcej pomocy 🛟
            */tip* - jeśli chcesz mi dać napiwek ☕️""";


    private final static String HELP = """
            *Help* 🛟
                        
            Jeśli potrzebujesz więcej pomocy, proszę skontaktuj się z administratorem: *@vicary1*""";

    private final static String TIP = """
            *Tip* ☕️
                        
            Jeśli chcesz mi dać napiwek skorzystaj z poniższych adresów:
            Adres BTC: 17PkbNkE1FfCcyWwJLWMkthaHTRfvBbLtT
            Adres ETH: 0x2ac2cc2fc09fcb051a928c7f7dcb6c332a2e73ac
                        
            Dzięki! 💞""";

    private final static String LIMITS = """
            *Limits* ⛔️
                        
            Użytkownik może obserwować maksymalnie 10 przedmiotów.
            Jeśli potrzebujesz obserwować więcej niż 10 przedmiotów skontaktuj się z administratorem w zakładce */help*.""";

    private final static String UPDATE = """
            *Update* ⛔️
                        
            Ostatnia aktualizacja przedmiotów: %s""";

    public void response(String text, String chatId, String nick) {
        String message = null;
        if (text.equals("/start"))
            sendStart(chatId, nick);

        else if (text.equals("/menu"))
            sendMenuBlocks();

        else if (text.equals("/update"))
            sendUpdate(chatId);

        else if (text.equals("/limits"))
            sendLimits(chatId);

        else if (text.equals("/help"))
            sendHelp(chatId);

        else if (text.equals("/tip"))
            sendTip(chatId);
    }

    public void sendStart(String chatId, String nick) {
        if (nick == null)
            nick = "";

        else
            nick = " " + nick;

        QuickSender.message(chatId, Messages.command("start").formatted(nick), true);
    }

    public void sendMenuBlocks() {
        QuickSender.message(InlineBlock.getMenu());
    }

    private void sendUpdate(String chatId) {
        QuickSender.message(chatId, Messages.command("update"), true);
    }

    private void sendLimits(String chatId) {
        QuickSender.message(chatId, Messages.command("limits"), true);
    }

    private void sendHelp(String chatId) {
        QuickSender.message(chatId, Messages.command("help"), true);
    }

    private void sendTip(String chatId) {
        QuickSender.message(chatId, Messages.command("tip"), true);
    }
}





















