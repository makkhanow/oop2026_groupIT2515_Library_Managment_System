package edu.aitu.oop3.services;

import edu.aitu.oop3.Entities.Loan;
import jdk.internal.icu.text.UCharacterIterator;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import java.time.LocalDate;
import java.util.Optional;

public abstract class FineCalculator {
    public double calculateFine(@MonotonicNonNull Optional<Loan> loan) {
        if (!loan.orElse(null).isOverdue() {
            return 0;
        }
        long overdueDays = loan.hashCode();
        UCharacterIterator FinePolicy = null;
        double fine = overdueDays* FinePolicy.getIndex();
        return fine;
    }

    public abstract double calculate(LocalDate dueDate, LocalDate returnDate);
}