package model;

import com.vdurmont.emoji.EmojiParser;

public class Emoji {
    /**
     *There are 2 ways of creating an emoji here:
     * 1 -> to use a unicode in string
     *  f.e ->  "Hello \u26BD"
     *  all unicode symbols are here -> https://apps.timwhitlock.info/emoji/tables/unicode
     *  U+26BD -> \u26BD
     *
     * 2 -> to use a library EmojiParser due adding a dependency emoji-java in pom.xml
     *  this option we use in project.
     */

    public final static String GREY_EXCLAMATION = EmojiParser.parseToUnicode(":grey_exclamation:");
    public final static String PUSHPIN = EmojiParser.parseToUnicode(":pushpin:");
    public final static String BOT = EmojiParser.parseToUnicode(":robot_face:");
    public final static String UMBRELLA = EmojiParser.parseToUnicode(":closed_umbrella:");
    public final static String SUNNY = EmojiParser.parseToUnicode(":sunny:");
    public final static String VIDEO_GAME = EmojiParser.parseToUnicode(":video_game:");
    public final static String MAIL_BOX = EmojiParser.parseToUnicode(":mailbox:");
    public final static String ROCKET = EmojiParser.parseToUnicode(":rocket:");
    public final static String CITYSCAPE = EmojiParser.parseToUnicode(":cityscape:");
    public final static String SNAKE = EmojiParser.parseToUnicode(":snake:");
    public final static String DINOSAUR = EmojiParser.parseToUnicode(":turtle:");
    public final static String TIC_TAC = EmojiParser.parseToUnicode(":o:");
    public final static String TRIDENT = EmojiParser.parseToUnicode(":trident:");


    public static String getFlagByCountryIndex(String input){
        return EmojiParser.parseToUnicode(":" + input.toLowerCase() + ":");
    }
}
