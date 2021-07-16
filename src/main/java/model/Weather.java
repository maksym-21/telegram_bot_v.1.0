package model;

import model.Emoji;
import model.Model;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

public class Weather {
    public static String getWeather(String city, Model model) throws IOException {
        // secure -> weather api
        // example -> https://samples.openweathermap.org/data/2.5/weather?q=London&appid=secure_weather_api
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\config.properties");
        properties.load(fileInputStream);

        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + properties.get("my_weather_api"));

        Scanner scanner = new Scanner((InputStream)url.getContent());

        StringBuilder outputJsonFormat = new StringBuilder();
        while (scanner.hasNextLine()){
            outputJsonFormat.append(scanner.nextLine());
        }

        // System.out.println(outputJsonFormat);

        JSONObject object = new JSONObject(outputJsonFormat.toString());

        // index of country
        JSONObject index = object.getJSONObject("sys");
        String var = (String) index.get("country");

        // setting a city name
        model.setCityName(city + " , " +  Emoji.getFlagByCountryIndex(var) + " ");

        // getting from whole main json object a smaller json object
        JSONObject object1 = object.getJSONObject("main");

        model.setHumidity((Number) object1.get("humidity"));
        model.setPressure((Number) object1.get("pressure"));

        Number temperature = (Number) object1.get("temp");
        Number min = (Number) object1.get("temp_min");
        Number max = (Number) object1.get("temp_max");

        model.setTemperature(temperature);
        model.setMinmax(min,max);

        // getting another smaller part of json object and setting a main forecast and description to it
        JSONArray array = object.getJSONArray("weather");
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);

            model.setMainForecast((String) jsonObject.get("main"));
            model.setDescription((String) jsonObject.get("description"));
        }

        // returning whole info about weather forecast by method toString of our model
        return model.toString();
    }
}
