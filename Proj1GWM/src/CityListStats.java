public record CityListStats(int startingIndex, int count, Integer[] years) {

    public int startingIndex() {
        return startingIndex;
    }

    public int getCount() {
        return count;
    }

    public Integer[] getYears() {
        return years;
    }
}
