package model;

public class Model {
    private String cityName;
    private String description;
    private String minmax;
    private String mainForecast;
    private Number temperature;
    private Number humidity;
    private Number pressure;

    private final String push = Emoji.PUSHPIN;

    public void setPressure(Number pressure) {
        this.pressure = pressure;
    }

    public void setTemperature(Number temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(Number humidity) {
        this.humidity = humidity;
    }


    public void setMainForecast(String mainForecast) {
        this.mainForecast = mainForecast.toLowerCase();
    }

    public void setDescription(String description) {
        this.description = description.toLowerCase();
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setMinmax(Number min, Number max) {
        this.minmax = "\n(min = " + min + " C*, max = " + max + " C*)";
    }

    @Override
    public String toString() {
        return "Weather forecast {" + "\n" +
                push + "\tCity -> " + cityName.toUpperCase() + "\n"+
                push + "\tMain forecast -> " + mainForecast + "\n"+
                push + "\tDescription -> " + description + "\n"+
                push + "\tTemperature -> " + temperature + " C* " + minmax + "\n"+
                push + "\tHumidity -> " + humidity + "\n" +
                push + "\tPressure -> " + pressure + "\n" +
                '}';
    }
}
