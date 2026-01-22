package edu.aitu.oop3.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SimpleFineCalculator implements FineCalculator {
    private final double finePerDay;

    public SimpleFineCalculator(double finePerDay) {
        this.finePerDay = finePerDay;
    }

    @Override
    public double calculate(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate == null || !returnDate.isAfter(dueDate)) return 0.0;
        long days = ChronoUnit.DAYS.between(dueDate, returnDate);
        return days * finePerDay;
    }
}
