package io.dplusic.stardust.ai;

public class ActionWithInterval {

    private int interval;
    private Runnable action;

    private int ticks = 0;

    public ActionWithInterval(int interval, Runnable action) {
        this.interval = interval;
        this.action = action;
    }

    public void update() {
        if (ticks == 0) {
            action.run();
        }
        ticks++;
        if (ticks == interval) {
            ticks = 0;
        }
    }
}
