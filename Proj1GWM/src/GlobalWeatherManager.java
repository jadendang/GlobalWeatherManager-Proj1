import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.util.Collection.*;

public class GlobalWeatherManager implements GlobalWeatherManagerInterface, Iterable<WeatherReading> {

    private ArrayList<WeatherReading> weatherData;

    public Iterator<WeatherReading> iterator() {
        return weatherData.iterator();
    }

    public GlobalWeatherManager(ArrayList<WeatherReading> weatherData)   {
        this.weatherData = weatherData;
    }

    public ArrayList<WeatherReading> getWeatherData() {
        return weatherData;
    }

    public GlobalWeatherManager(File weatherFile) throws FileNotFoundException {
        weatherData = new ArrayList<>();

        // TODO: look into scanner method
        try (Scanner weatherScan = new Scanner(weatherFile)) {
            weatherScan.useDelimiter(",|\\R");

            while (weatherScan.hasNext()) {
                String region = weatherScan.next();
                String country = weatherScan.next();
                String state = weatherScan.next();
                String city = weatherScan.next();
                int year = Integer.parseInt(weatherScan.next());
                int month = Integer.parseInt(weatherScan.next());
                int day = Integer.parseInt(weatherScan.next());
                double avgTemperature = Double.parseDouble(weatherScan.next());

                WeatherReading reading = new WeatherReading(region, country, state, city, year, month, day, avgTemperature);
                weatherData.add(reading);

            }
        }
    }

    @Override
    public int getReadingCount() {
        return weatherData.size();
    }

    @Override
    public WeatherReading getReading(int index) {
        if (index < 0 || index >= weatherData.size()) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
        return weatherData.get(index);
    }

    @Override
    public WeatherReading[] getReadings(int index, int count) {
        if (index < 0 || count < 1 || index + count > weatherData.size()) {
            throw new IllegalArgumentException("Invalid count or index");
        }
        WeatherReading[] readings = new WeatherReading[count];
        for (int i = 0; i < count; i++) {
            readings[i] = weatherData.get(index + i);
        }
        return readings;
    }

    @Override
    public WeatherReading[] getReadings(int index, int count, int month, int day) {
        if (index < 0 || count < 1 || index + count > weatherData.size()) {
            throw new IllegalArgumentException("Invalid count or index");
        }
        ArrayList<WeatherReading> readingsFiltered = new ArrayList<>();
        for (int i = index; i < index + count; i++) {
            WeatherReading reading = weatherData.get(i);
            if (reading.month() == month && reading.day() == day) {
                readingsFiltered.add(reading);
            }
        }
        return readingsFiltered.toArray(new WeatherReading[0]);
    }

    @Override
    public CityListStats getCityListStats(String country, String state, String city) {
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country must not be null or blank");
        }
        if (state == null) {
            throw new IllegalArgumentException("State must not be null");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City must not be null or blank");
        }

        Collections.sort(weatherData);
        int startIndex = binarySearchCity(city);
        if (startIndex == -1) {
            return null;
        }
        while (startIndex > 0 && weatherData.get(startIndex -1).city().equals(city)) {
            startIndex--;
        }
        int count = 0;
        List<Integer> yearsList = new ArrayList<>();
        while (startIndex < weatherData.size() && weatherData.get(startIndex).city().equals(city)) {
            WeatherReading reading = weatherData.get(startIndex);
            System.out.println("Processing WR: " + reading);
            System.out.println("actual state of WR: " + reading.state());
            System.out.println("requested country: " + country + ", Requested state: " + state + ", Requested city: " + city);
            System.out.println("Actual country: " + reading.country() + ", Actual state: " + reading.state() + ", Actual city: " + reading.city());
            if (reading.country().equals(country) && reading.state().equals(state)) {
                count++;
                if (!yearsList.contains(reading.year())) {
                    yearsList.add(reading.year());
                }
            } else {
                System.out.println("no match found for country: " + country + ", state: " + state);
            }
            startIndex++;
        }
        return new CityListStats(startIndex, count, yearsList.toArray(new Integer[0]));
    }

    @Override
    public double getTemperatureLinearRegressionSlope(WeatherReading[] readings) {
        List<Integer> years = new ArrayList<>();
        List<Double> temperatures = new ArrayList<>();
        for (WeatherReading reading : readings) {
            if (reading.avgTemperature() != -99.0) {
                years.add(reading.year());
                temperatures.add(reading.avgTemperature());
            }
        }
        if (years.size() < 2) {
            throw new IllegalArgumentException("Need more readings for regression analysis");
        }
        double sumXY = 0;
        double sumX = 0;
        double sumY = 0;
        double sumXSquare = 0;
        for (int i = 0; i < years.size(); i++) {
            sumXY += years.get(i) * temperatures.get(i);
            sumX += years.get(i);
            sumY += temperatures.get(i);
            sumXSquare += years.get(i) * years.get(i);
        }
        double n = years.size();
        double slope = (n * sumXY - sumX * sumY) / (n * sumXSquare - sumX * sumX);

        return slope;
    }

    @Override
    public double calcLinearRegressionSlope(Integer[] x, Double[] y) {
        if (x == null || y == null || x.length < 2 || y.length <2 || x.length != y.length) {
            throw new IllegalArgumentException("Invalid input arrays");
        }
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumXSquare = 0;

        for (int i = 0; i < x.length; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
            sumXSquare += x[i] * x[i];
        }
        double n = x.length;
        double slope = (n * sumXY - sumX * sumY) / (n * sumXSquare - sumX * sumX);

        return slope;
    }


    // chapter 13 go to authors code for binary search
    public int binarySearchCity(String city) {
        int low = 0;
        int high = weatherData.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            String midCity = weatherData.get(mid).city();

            if (midCity.compareTo(city) < 0) {
                low = mid + 1;
            } else if (midCity.compareTo(city) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }

        return -1;
    }
}


