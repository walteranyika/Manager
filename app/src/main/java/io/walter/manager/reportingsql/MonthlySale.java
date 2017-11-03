package io.walter.manager.reportingsql;

/**
 * Created by walter on 11/3/17.
 */
public class MonthlySale {
    private double totalSales;
    private int numberSales;
    private String month;
    private String year;

    public MonthlySale() {
    }

    public MonthlySale(double totalSales, int numberSales, String month, String year) {
        this.totalSales = totalSales;
        this.numberSales = numberSales;
        this.month = month;
        this.year=year;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public int getNumberSales() {
        return numberSales;
    }

    public void setNumberSales(int numberSales) {
        this.numberSales = numberSales;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
