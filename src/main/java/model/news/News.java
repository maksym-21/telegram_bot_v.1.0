package model.news;

import handlers.PropertiesHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;


// +? algorithm for better showing news
public class News {

    private static final Map<String,NewsModel> storage = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(News.class);


    public static ArrayList<NewsModel> getNews(String keyWord,String dateFrom,NewsLanguage language) throws IOException {
        String api = PropertiesHandler.getStringFromProperty("my_news_api");
        // 2021-07-27
        // https://newsapi.org/v2/everything?q={key_word}&from={date}&language={lang}&sortBy=publishedAt&apiKey={api_key}

        URL url = new URL("https://newsapi.org/v2/everything?q=" + keyWord.toLowerCase() +
                "&from=" + dateFrom + "&language=" + language + "&sortBy=publishedAt&apiKey="+api);

        LOG.info("Getting info from news api");

        Scanner scanner = new Scanner((InputStream)url.getContent());

        StringBuilder outputJsonFormat = new StringBuilder();
        while (scanner.hasNextLine()){
            outputJsonFormat.append(scanner.nextLine());
        }

        LOG.info("Successfully read from api by url {}",url.toString());
        JSONObject mainObject = new JSONObject(outputJsonFormat.toString());

        int size = Integer.parseInt(String.valueOf(mainObject.get("totalResults")));
        String status = String.valueOf(mainObject.get("status"));

        System.out.println(mainObject.toString());

        if (size==0 || !status.equals("ok")) {
            LOG.warn("Failed with reading json object with size - {} , status - {}",size,status);
            return new ArrayList<>();
        }

        LOG.info("Start of handling received main json object size of {}",size);

        return handle(mainObject,size);
    }

    private static ArrayList<NewsModel> handle(JSONObject object,int size){
        /*
         *  1 -> доповнити список якшо новина вже бyла
         *  2 -> треба висвітлити скільки новин найшло
         */
        ArrayList< NewsModel> list = new ArrayList<>();

        JSONArray articles = object.getJSONArray("articles") ;
        int index = (size > 6 ) ? 5 : articles.length();

        for (int i = 0; i < index; i++) {
            JSONObject jsonObject = articles.getJSONObject(++i);

            NewsModel model = new NewsModel(i);

            if(jsonObject.get("author") == null || jsonObject.get("author").equals("null")){
                model.setAuthor(" ? ");
            }
            else model.setAuthor(String.valueOf(jsonObject.get("author")));

            LOG.info("author -> success");

            if (jsonObject.has("title")) model.setTitle(String.valueOf(jsonObject.get("title")));
            else model.setTitle(" ? ");

            LOG.info("title -> success");

            model.setUrlToNovelty(String.valueOf(jsonObject.get("url")));

            LOG.info("UrlToNovelty -> success");

            model.setUrlToImage(String.valueOf(jsonObject.get("urlToImage")));

            LOG.info("UrlToImage -> success");

            // if it's fresh news (there wasn't such news with such article in history of bot)
            if (!storage.containsKey(model.getTitle())) list.add(model);

            storage.put(model.getTitle(),model);
        }

        return list;
    }
}
