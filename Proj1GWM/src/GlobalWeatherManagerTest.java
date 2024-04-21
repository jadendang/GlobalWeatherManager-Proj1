import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class   GlobalWeatherManagerTest {

    private ArrayList<WeatherReading> weatherData;
    private GlobalWeatherManager manager;

    @Test
    public void constructorTest() {
        ArrayList<WeatherReading> testData = new ArrayList<>();
        testData.add(new WeatherReading("Africa", "Algeria", "", "Algiers", 1995, 1, 1, 64.2));
        testData.add(new WeatherReading("Africa", "Algeria", "", "Algiers", 1995, 1, 2, 49.4));
        testData.add(new WeatherReading("Africa", "Algeria", "", "Algiers", 1995, 1, 3, 48.8));
        testData.add(new WeatherReading("North America", "US", "California", "Fresno", 1995, 1, 2, 42.8));

        GlobalWeatherManager manager = new GlobalWeatherManager(testData);
        assertNotNull(manager.getWeatherData());
        assertEquals(testData.size(), manager.getWeatherData().size());
    }

    @BeforeEach
    public void setUp() {
        weatherData = new ArrayList<>();
        weatherData.add(new WeatherReading("Region", "CountryA", "StateA", "CityA", 1995, 1, 1, 25.0));
        weatherData.add(new WeatherReading("Region", "CountryA", "StateA", "CityA", 1996, 1, 2, 25.0));
        weatherData.add(new WeatherReading("Region", "CountryA", "StateA", "CityA", 1995, 1, 3, 26.0));
        weatherData.add(new WeatherReading("Region", "CountryA", "StateA", "CityA", 1997, 1, 4, 25.0));
        weatherData.add(new WeatherReading("Region", "CountryA", "StateB", "CityA", 1995, 1, 5, 27.0));
        weatherData.add(new WeatherReading("Region", "CountryA", "StateB", "CityA", 1997, 1, 5, 27.0));
        weatherData.add(new WeatherReading("Region", "CountryA", "StateA", "CityB", 1996, 1, 6, 25.0));
        Collections.sort(weatherData);

        manager = new GlobalWeatherManager(weatherData);
    }

    @Test
    public void testGetCityListStats_FirstOccurrence() {
        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        CityListStats stats = manager.getCityListStats("CountryA", "StateA", "CityA");
        assertNotNull(stats);
        assertEquals(4, stats.getCount());
        assertArrayEquals(new Integer[]{1995, 1996, 1997}, stats.getYears());
    }

    @Test
    public void testGetCityListStats_LastOccurrence() {
        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        CityListStats stats = manager.getCityListStats("CountryA", "StateA", "CityB");
        assertNotNull(stats);
        assertEquals(1, stats.getCount());
        assertArrayEquals(new Integer[]{1996}, stats.getYears());
    }

    @Test
    public void testGetCityListStats_MiddleOccurrence() {
        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        CityListStats stats = manager.getCityListStats("CountryA", "StateB", "CityA");
        assertNotNull(stats);
        assertEquals(1, stats.getCount());
        assertArrayEquals(new Integer[]{1995}, stats.getYears());
    }

    @Test
    public void testGetCityListStats_CityNotFound() {
        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        CityListStats stats = manager.getCityListStats("CountryB", "StateB", "CityC");
        assertNull(stats);
    }

    @Test
    public void testGetCityListStats_NullCountry() {
        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        assertThrows(IllegalArgumentException.class, () -> {
            manager.getCityListStats(null, "StateA", "CityA");
        });
    }

    @Test
    public void testGetCityListStats_NullState() {
        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        assertThrows(IllegalArgumentException.class, () -> {
            manager.getCityListStats("CountryA", null, "CityA");
        });
    }

    @Test
    public void testGetCityListStats_NullCity() {
        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        assertThrows(IllegalArgumentException.class, () -> {
            manager.getCityListStats("CountryA", "StateA", null);
        });
    }

    @Test
    public void testGetCityListStats_BlankCountry() {
        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        assertThrows(IllegalArgumentException.class, () -> {
            manager.getCityListStats("", "StateA", "CityA");
        });
    }

    @Test
    public void testGetCityListStats_BlankCity() {
        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        assertThrows(IllegalArgumentException.class, () -> {
            manager.getCityListStats("CountryA", "StateA", "");
        });
    }




//    @Test
//    void CityListStatsTest() {
//        int startingIndex = 0;
//        int count = 10;
//        Integer[] years = {2019, 2020, 2021};
//
//        CityListStats cityStats = new CityListStats(startingIndex, count, years);
//
//        assertNotNull(cityStats, "CityListStats object should not be null");
//        assertEquals(startingIndex, cityStats.startingIndex(), "Starting index should match");
//        assertEquals(count, cityStats.getCount(), "Count should match");
//
//        Integer[] actualYears = cityStats.getYears();
//        assertNotNull(actualYears, "Years array should not be null");
//        assertEquals(years.length, actualYears.length, "Years array length should match");
//
//        for (int i = 0; i < years.length; i++) {
//            assertEquals(years[i], actualYears[i], "Year at index " + i + " should match");
//        }
//    }

    @Test
    void LinearRegressionSlopeTest() {
        ArrayList<WeatherReading> readings = new ArrayList<>();
                readings.add(new WeatherReading("Africa", "Algeria", "", "Algiers", 1995, 1, 1, 20.0));
                readings.add(new WeatherReading("Africa", "Algeria", "", "Algiers", 1996, 1, 1, 21.0));
                readings.add(new WeatherReading("Africa", "Algeria", "", "Algiers", 1997, 1, 1, 22.0));
                readings.add(new WeatherReading("Africa", "Algeria", "", "Algiers", 1998, 1, 1, 23.0));
                readings.add(new WeatherReading("Africa", "Algeria", "", "Algiers", 1999, 1, 1, 24.0));

        GlobalWeatherManager manager = new GlobalWeatherManager(readings);
        WeatherReading[] readingsArray = readings.toArray(new WeatherReading[0]);
        double slope = manager.getTemperatureLinearRegressionSlope(readingsArray);

        double expectedSlope = 1.0;

        assertEquals(expectedSlope, slope, 0.01);

    }

    @Test
    void calcLinearRegressionSlope() {

        ArrayList<WeatherReading> weatherData = new ArrayList<>();

        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        Integer[] years = {1995, 1996, 1997, 1998, 1999};
        Double[] temperatures = {10.0, 11.0, 12.0, 13.0, 14.0};

        double expectedSlope = 1.0;

        double actualSlope = manager.calcLinearRegressionSlope(years, temperatures);

        assertEquals(expectedSlope, actualSlope, 0.0001, "Slope calculation is incorrect");
    }

    @Test
    void getReadingCountTest() {
        ArrayList<WeatherReading> weatherData = new ArrayList<>();
        weatherData.add(new WeatherReading("Region", "Country", "State", "City1", 2022, 1, 1, 25.0));
        weatherData.add(new WeatherReading("Region", "Country", "State", "City2", 2023, 1, 1, 26.0));
        weatherData.add(new WeatherReading("Region", "Country", "State", "City3", 2024, 1, 1, 27.0));

        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        assertEquals(3, manager.getReadingCount(), "Reading count should be 3");
    }

    @Test
    void indexCountReadingTest() {
        ArrayList<WeatherReading> weatherData = new ArrayList<>();
        weatherData.add(new WeatherReading("Region", "Country", "State", "City1", 1995, 1, 1, 25.0));
        weatherData.add(new WeatherReading("Region", "Country", "State", "City2", 1996, 1, 1, 27.0));

        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        WeatherReading[] readings = manager.getReadings(0, 2);
        assertNotNull(readings, "Array should not be null");
        assertEquals(2, readings.length, "Array length should be 2");
        assertEquals("City1", readings[0].city(), "City should match");
        assertEquals("City2", readings[1].city(), "City should match");
    }

    @Test
    void getReadingsTest() {
        ArrayList<WeatherReading> weatherData = new ArrayList<>();
        weatherData.add(new WeatherReading("Region", "Country", "State", "City1", 1995, 1, 1, 25.0));
        weatherData.add(new WeatherReading("Region", "Country", "State", "City2", 1996, 1, 1, 26.0));
        weatherData.add(new WeatherReading("Region", "Country", "State", "City3", 1997, 1, 1, 27.0));

        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        WeatherReading[] readings = manager.getReadings(0,3,1,1);

        assertNotNull(readings, "Readings should not be null");
        assertEquals(3, readings.length, "ArrayLength should be 3");
        assertEquals(1995, readings[0].year(), "First Reading Year should be 1995");
        assertEquals(1996, readings[1].year(), "First Reading Year should be 1996");
        assertEquals(1997, readings[2].year(), "First Reading Year should be 1997");
    }

    @Test
    void indexReadingTest() {
        ArrayList<WeatherReading> weatherData = new ArrayList<>();
        weatherData.add(new WeatherReading("Region", "Country", "State", "City1", 2022, 1, 1, 25.0));
        weatherData.add(new WeatherReading("Region", "Country", "State", "City2", 2023, 1, 1, 26.0));
        weatherData.add(new WeatherReading("Region", "Country", "State", "City3", 2024, 1, 1, 27.0));

        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        WeatherReading reading = manager.getReading(1);
        assertEquals("City2", reading.city(), "City name should match");
        assertEquals(2023, reading.year(), "Year should match");
        assertEquals(1, reading.month(), "Month should match");
        assertEquals(1, reading.day(), "Day should match");
        assertEquals(26.0, reading.avgTemperature(), 0.01, "Average temperature should match");
    }

    @Test
    void getCityListStatsTest() {
        ArrayList<WeatherReading> weatherData = new ArrayList<>();
        weatherData.add(new WeatherReading("Region", "Country", "State", "City", 1995, 1, 1, 25.0));
        weatherData.add(new WeatherReading("Region", "Country", "State", "City", 1996, 1, 1, 26.0));

        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);
        CityListStats stats = manager.getCityListStats("Country", "State", "City");

        assertNotNull(stats, "CityListStats should not be null");
        assertEquals(2, stats.getCount(), "Count should be 2");
        assertArrayEquals(new Integer[]{1995, 1996}, stats.getYears(), "Years should contain [1995, 1996]");
    }

//    void getCityListStatsTest() {
//        ArrayList<WeatherReading> weatherData = new ArrayList<>();
//        weatherData.add(new WeatherReading("Region1", "Country1", "State1", "City1", 2020, 1, 1, 20.0));
//        weatherData.add(new WeatherReading("Region1", "Country1", "State2", "City1", 2020, 1, 1, 22.0));
//        weatherData.add(new WeatherReading("Region1", "Country1", "State1", "City1", 2021, 1, 1, 25.0));
//        weatherData.add(new WeatherReading("Region1", "Country1", "", "City2", 2020, 1, 1, 18.0)); // Testing empty state
//
//        // Create a GlobalWeatherManager instance
//        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);
//
//        // Test for a valid city with state and country
//        CityListStats stats1 = manager.getCityListStats("Country1", "State1", "City1");
//        assertNotNull(stats1);
//        assertEquals(2, stats1.getCount());
//        assertArrayEquals(new Integer[]{2020, 2021}, stats1.getYears());
//
//        // Test for a city with empty state and country
//        CityListStats stats2 = manager.getCityListStats("Country1", "", "City2");
//        assertNotNull(stats2);
//        assertEquals(1, stats2.getCount());
//        assertArrayEquals(new Integer[]{2020}, stats2.getYears());
//
//        // Test for a non-existent city
//        CityListStats stats3 = manager.getCityListStats("Country1", "State1", "City3");
//        assertNull(stats3);
//    }

    @Test
    void iterator() {
    }


    @Test
    void binarySearchCity() {
        ArrayList<WeatherReading> weatherData = new ArrayList<>();
        weatherData.add(new WeatherReading("Region", "Country", "State", "City1", 2022, 1, 1, 25.0));
        weatherData.add(new WeatherReading("Region", "Country", "State", "City2", 2023, 1, 1, 26.0));
        weatherData.add(new WeatherReading("Region", "Country", "State", "City3", 2024, 1, 1, 27.0));

        GlobalWeatherManager manager = new GlobalWeatherManager(weatherData);

        int indexCity1 = manager.binarySearchCity("City1");
        assertEquals(0, indexCity1, "Index of City1 should be 0");

        int indexCity4 = manager.binarySearchCity("City4");
        assertEquals(-1, indexCity4, "Index of City4 should be -1");
    }

    @Test
    public void testGetCityListStats() {
        // Assuming you have initialized your GlobalWeatherManager and weatherData appropriately
        CityListStats stats = manager.getCityListStats("CountryA", "StateB", "CityA");
        assertNotNull(stats);
        System.out.println("Count: " + stats.getCount());
        System.out.println("Years: " + Arrays.toString(stats.getYears()));
    }

}


