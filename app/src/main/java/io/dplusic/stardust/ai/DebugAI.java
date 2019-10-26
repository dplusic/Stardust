package io.dplusic.stardust.ai;

import java.util.List;

import io.dplusic.stardust.entity.Player;
import io.dplusic.stardust.entity.Star;

public class DebugAI implements AI {

    private List<Star> stars;

    private int elapsed = 0;

    public DebugAI(List<Star> stars) {
        this.stars = stars;
    }

    public void update() {

        List<List<Star>> groupedStars = AIUtils.groupStars(stars);
        List<Star> comStars = groupedStars.get(Player.PLAYER_TYPE_COM);
        if (elapsed > 100) {
            for (Star comStar : comStars) {
                comStar.setInfectivity(0);
            }
        }

        elapsed++;
    }
}
