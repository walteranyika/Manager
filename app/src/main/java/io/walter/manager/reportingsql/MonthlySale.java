package io.walter.manager.reportingsql;

/**
 * Created by walter on 11/3/17.
 */
public class MonthlySale {
    private double totalSales;
    private int numberSales;
    private String month;

    public MonthlySale() {
    }

    public MonthlySale(double totalSales, int numberSales, String month) {
        this.totalSales = totalSales;
        this.numberSales = numberSales;
        this.month = month;
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
