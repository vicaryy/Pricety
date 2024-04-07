package com.vicary.pricety.format;

import com.vicary.pricety.exception.IllegalInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class MarkdownV2 {
    private final static Logger logger = LoggerFactory.getLogger(MarkdownV2.class);
    private final StringBuilder sb;
    private static final List<Character> markdownV2Characters = Arrays.asList('_', '*', '[', ']', '(', ')', '~', '`', '>', '#', '+', '-', '=', '|', '{', '}', '.', '!');
    private static final List<Character> markdownV2CharactersSkips = Arrays.asList('[', ']', '(', ')', '~', '`', '>', '#', '+', '-', '=', '|', '{', '}', '.', '!');

    private MarkdownV2(StringBuilder sb) {
        this.sb = sb;
    }

    public static MarkdownV2 apply(String text) {
        for (char c : markdownV2Characters)
            if (text.contains(String.valueOf(c)))
                text = text.replace(String.valueOf(c), "\\" + c);
        return new MarkdownV2(new StringBuilder().append(text));
    }

    public static String applyWithManualBoldAndItalic(String text) {
        int stars = 0;
        int underscores = 0;
        for (char ch : text.toCharArray()) {
            if (ch == '*')
                stars++;
            if (ch == '_')
                underscores++;
        }

        if (stars % 2 != 0)
            throw new IllegalInputException("Number of stars must be even.", "Admin tries to send message but stars are not even.");
        if (underscores % 2 != 0)
            throw new IllegalInputException("Number of underscores must be even.", "Admin tries to send message but stars are not even.");

        for (char c : markdownV2CharactersSkips)
            if (text.contains(String.valueOf(c)))
                text = text.replace(String.valueOf(c), "\\" + c);
        return text;
    }

    public MarkdownV2 toURL() {
        sb.insert(0, "[link](").insert(sb.length(), ")");
        return this;
    }

    public MarkdownV2 toURL(String serviceName) {
        serviceName = serviceName.replace(".", "․");
        sb.insert(0, "[" + serviceName + "](").insert(sb.length(), ")");
        return this;
    }

    public MarkdownV2 toBold() {
        sb.insert(0, "*").insert(sb.length(), "*");
        return this;
    }

    public MarkdownV2 toItalic() {
        sb.insert(0, "_").insert(sb.length(), "_");
        return this;
    }

    public MarkdownV2 newlineBefore() {
        sb.insert(0, "\n");
        return this;
    }

    public MarkdownV2 newlineBefore(int amountOfLines) {
        for (int i = 0; i < amountOfLines; i++)
            sb.insert(0, "\n");
        return this;
    }

    public MarkdownV2 newlineAfter() {
        sb.insert(sb.length(), "\n");
        return this;
    }

    public MarkdownV2 newlineAfter(int amountOfLines) {
        for (int i = 0; i < amountOfLines; i++)
            sb.insert(sb.length(), "\n");
        return this;
    }

    public String get() {
        return sb.toString();
    }
}
