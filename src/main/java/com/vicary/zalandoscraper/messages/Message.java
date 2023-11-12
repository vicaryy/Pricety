package com.vicary.zalandoscraper.messages;

import com.vicary.zalandoscraper.format.MarkdownV2;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Message {

    public static class Menu {
        private static String Hello = "Asd";
        private final static List<String> HI_MESSAGES_ENG = List.of(
                "How are you?",
                "What can i do for you?",
                "How's your day going?",
                "How can I help you today?",
                "Are you doing well?",
                "Is there something I can do to assist you?",
                "What's on your mind?",
                "Need assistance?",
                "What's up?",
                "Can I help?");
        private final static List<String> HI_MESSAGES_PL = List.of(
                "Jak się masz?");

        public static String hello(Language lang, String nick) {
            if (lang == Language.PL)
                return """
                        *Hello %s* 👋
                                        
                        %s""".formatted(
                        MarkdownV2.apply(nick).get(),
                        HI_MESSAGES_PL.get(ThreadLocalRandom.current().nextInt(HI_MESSAGES_PL.size())));
        }
    }

    public static class Notification {
        public static String Yo = "yo";
    }
}
