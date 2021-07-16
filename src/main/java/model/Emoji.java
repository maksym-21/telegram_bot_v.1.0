package model;

import com.vdurmont.emoji.EmojiParser;

public class Emoji {

    /*
       There are 2 ways of creating an emoji here:
       1 -> to use a unicode in string
            f.e ->  "Hello \u26BD"
            all unicode symbols are here -> https://apps.timwhitlock.info/emoji/tables/unicode
            U+26BD -> \u26BD

       2 -> to use a library EmojiParser due adding a dependency emoji-java in pom.xml
            (used in my project)
     */

    public final static String QUESTION_MARK = EmojiParser.parseToUnicode(":question:");
    public final static String GREY_EXCLAMATION = EmojiParser.parseToUnicode(":grey_exclamation:");
    public final static String PUSHPIN = EmojiParser.parseToUnicode(":pushpin:");
    public final static String BOT = EmojiParser.parseToUnicode(":robot_face:");
    public final static String UMBRELLA = EmojiParser.parseToUnicode(":closed_umbrella:");
    public final static String SUNNY = EmojiParser.parseToUnicode(":sunny:");

    public static String getFlagByCountryIndex(String input){
        return EmojiParser.parseToUnicode(":" + input.toLowerCase() + ":");
    }



}
