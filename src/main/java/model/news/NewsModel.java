package model.news;

import lombok.Getter;
import lombok.Setter;
import model.Emoji;

@Setter
@Getter
public class NewsModel {
    private final int hashId;

    private String author;
    private String title;
    private String urlToImage;
    private String urlToNovelty;

    private final String push = Emoji.PUSHPIN;

    public NewsModel(int hashId) {
        this.hashId = hashId;
    }

    @Override
    public String toString() {
        return "News #" + hashId + " {\n" +
                push + " Author -> " + author + '\n' +
                push + " Title -> " + title + '\n' +
                push + " Link -> " + urlToNovelty + '\n' +
                '}';
    }
}
