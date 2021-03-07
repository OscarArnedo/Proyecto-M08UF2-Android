package com.example.proyectom08uf2android;

public class Bet {

    private User user;
    private int amount;
    private boolean result;
    private String bet;

    public Bet(User user, int amount, boolean result, String bet) {
        this.user = user;
        this.amount = amount;
        this.result = result;
        this.bet = bet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }
}
