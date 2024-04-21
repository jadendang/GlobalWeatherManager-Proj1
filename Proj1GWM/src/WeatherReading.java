import java.util.Objects;

public record WeatherReading(
        String region,
        String country,
        String state,
        String city,
        Integer year,
        Integer month,
        Integer day,
        Double avgTemperature
) implements Comparable<WeatherReading> {


    public int compareTo(WeatherReading other) {
        if (!this.country.equals(other.country)) {
            return this.country.compareTo(other.country);
        } else if (!this.state.equals(other.state)) {
            return this.state.compareTo(other.state);
        } else {
            return this.city.compareTo(other.city);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WeatherReading other = (WeatherReading) obj;
        return Objects.equals(region, other.region) &&
                Objects.equals(country, other.country) &&
                Objects.equals(state, other.state) &&
                Objects.equals(city, other.city) &&
                Objects.equals(year, other.year) &&
                Objects.equals(month, other.month) &&
                Objects.equals(day, other.day) &&
                Objects.equals(avgTemperature, other.avgTemperature);
    }


    @Override
    public String toString() {
        return "WeatherReading[" +
                "region=" + region + ", " +
                "country=" + country + ", " +
                "state=" + state + ", " +
                "city=" + city + ", " +
                "year=" + year + ", " +
                "month=" + month + ", " +
                "day=" + day + ", " +
                "avgTemperature=" + avgTemperature + ']';
    }


}
