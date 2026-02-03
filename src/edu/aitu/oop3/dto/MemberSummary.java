package edu.aitu.oop3.dto;

import edu.aitu.oop3.Entities.Loan;
import edu.aitu.oop3.Entities.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemberSummary {
    private final Member member;
    private final List<Loan> activeLoans;
    private final double totalFine;

    private MemberSummary(Builder b) {
        this.member = b.member;
        this.activeLoans = Collections.unmodifiableList(new ArrayList<>(b.activeLoans));
        this.totalFine = b.totalFine;
    }

    public Member getMember() { return member; }
    public List<Loan> getActiveLoans() { return activeLoans; }
    public double getTotalFine() { return totalFine; }

    public static class Builder {
        private Member member;
        private List<Loan> activeLoans = new ArrayList<>();
        private double totalFine;

        public Builder member(Member member) {
            this.member = member;
            return this;
        }

        public Builder activeLoans(List<Loan> loans) {
            this.activeLoans = loans;
            return this;
        }

        public Builder totalFine(double fine) {
            this.totalFine = fine;
            return this;
        }

        public MemberSummary build() {
            if (member == null) {
                throw new IllegalStateException("member is required");
            }
            return new MemberSummary(this);
        }
    }
}
