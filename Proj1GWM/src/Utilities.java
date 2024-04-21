public class Utilities {

    public static WeatherReading parseWeatherReading(String[] parts) {

        String region = parts[0];
        String country = parts[1];
        String state = parts[2];
        String city = parts[3];
        int year = Integer.parseInt(parts[4]);
        int month = Integer.parseInt(parts[5]);
        int day = Integer.parseInt(parts[6]);
        double avgTemperature = Double.parseDouble(parts[7]);


        return new WeatherReading(region, country, state, city, year, month, day, avgTemperature);
    }

}
