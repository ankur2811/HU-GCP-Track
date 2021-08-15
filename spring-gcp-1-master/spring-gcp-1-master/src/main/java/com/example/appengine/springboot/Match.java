package com.example.appengine.springboot;

public class Match {
    private int over;
    private int ball;
    private int run;
    private int batsman;
    private int bowler;
    public int getOver() {
        return over;
    }
    public void setOver(int over) {
        this.over = over;
    }
    public int getBall() {
        return ball;
    }
    public void setBall(int ball) {
        this.ball = ball;
    }
    public int getRun() {
        return run;
    }
    public void setRun(int run) {
        this.run = run;
    }
    public int getBatsman() {
        return batsman;
    }
    public void setBatsman(int batsman) {
        this.batsman = batsman;
    }
    public int getBowler() {
        return bowler;
    }
    public void setBowler(int bowler) {
        this.bowler = bowler;
    }
    public Match() {
        super();

    }

}
