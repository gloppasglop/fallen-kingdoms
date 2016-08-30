package com.gloppasglop.fk.utils;

/**
 * Created by christopheroux on 27/08/16.
 */
public class GameTime {

    private int time;
    private boolean isRunning;
    private int pvpDay;
    private int assaultDay;
    private int durationDay;

    public GameTime() {
        time = 0;
        isRunning = false;
        pvpDay = 2;
        assaultDay = 5;
        durationDay = 20;
    }

    public void setTime(int time) {
        if (time >0 ) {
            this.time = time;
        }
    }

    public boolean isRunning(){
        return this.isRunning;
    }

    public int days() {
        return 1 + (int) (time/60.0/durationDay);
    }

    public int seconds() {
       return time % 60;
    }

    public int minutes() {
       return (time/60) % durationDay;
    }

    public void inc() {
        if (isRunning) time++;
    }

    public void stop() {
        isRunning = false;
    }

    public void start() {
        isRunning = true;
        time = 0;
    }

    public void restart() {
        isRunning = true;
    }

    public boolean isPvp() {
        return this.days() >= pvpDay;
    }

    public boolean isAssault() {
        return this.days() >= assaultDay;
    }


}
