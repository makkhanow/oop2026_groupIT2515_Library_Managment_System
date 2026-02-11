package edu.aitu.oop3.config;

public final class  LibraryConfig {
    private static volatile LibraryConfig instance;

    private final int defaultLoanDays;
    private final double finePerDay;

    private LibraryConfig(int defaultLoanDays, double finePerDay) {
        this.defaultLoanDays = defaultLoanDays;
        this.finePerDay = finePerDay;
    }

    public static LibraryConfig getInstance() {
        if (instance == null) {
            synchronized (LibraryConfig.class) {
                if (instance == null) {
                    instance = new LibraryConfig(14, 500.0);
                }
            }
        }
        return instance;
    }

    public int getDefaultLoanDays() { return defaultLoanDays; }
    public double getFinePerDay() { return finePerDay; }
}
