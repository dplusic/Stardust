package io.dplusic.stardust.ai;

import java.util.ArrayList;
import java.util.List;

import io.dplusic.stardust.entity.Player;
import io.dplusic.stardust.entity.Star;

public class NormalAI implements AI {

    private List<Star> stars;

    public NormalAI(List<Star> stars) {
        this.stars = stars;
    }

    public void update() {
        List<List<Star>> groupedStars = AIUtils.groupStars(stars);
        List<Star> nobodyStars = groupedStars.get(Player.PLAYER_TYPE_NOBODY);
        List<Star> userStars = groupedStars.get(Player.PLAYER_TYPE_USER);
        List<Star> comStars = groupedStars.get(Player.PLAYER_TYPE_COM);

        List<Star> otherStars = new ArrayList<>(stars.size());
        otherStars.addAll(nobodyStars);
        otherStars.addAll(userStars);

        for (Star comStar : comStars) {
            if (comStar.getInfectivity() > 50) {
                Star targetStar = AIUtils.findNearest(comStar, otherStars);
                if (targetStar != null) {
                    otherStars.remove(targetStar);
                    AIUtils.createDust(comStar, targetStar);
                }
            }
        }
    }
}
