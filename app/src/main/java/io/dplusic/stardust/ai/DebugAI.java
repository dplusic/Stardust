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

        AIUtils.StarGroupsByOnwer starGroupsByOnwer = AIUtils.groupStars(stars);
        if (elapsed > 100) {
            for (Star comStar : starGroupsByOnwer.com) {
                comStar.setInfectivity(0);
            }
        }

        elapsed++;
    }
}
