package edu.aitu.oop3.services;

import java.time.LocalDate;

public interface FineCalculator {
    double calculate(LocalDate dueDate, LocalDate returnDate);
}
