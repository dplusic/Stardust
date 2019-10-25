package io.dplusic.stardust;

import java.util.ArrayList;
import java.util.List;

import io.dplusic.stardust.entity.Coordinate;
import io.dplusic.stardust.entity.Dust;
import io.dplusic.stardust.entity.Player;
import io.dplusic.stardust.entity.Star;

public class AI {

    private List<Star> stars;

    public AI(List<Star> stars) {
        this.stars = stars;
    }

    public void update() {
        List<List<Star>> groupedStars = groupStars(stars);
        List<Star> nobodyStars = groupedStars.get(Player.PLAYER_TYPE_NOBODY);
        List<Star> userStars = groupedStars.get(Player.PLAYER_TYPE_USER);
        List<Star> comStars = groupedStars.get(Player.PLAYER_TYPE_COM);

        List<Star> otherStars = new ArrayList<>(stars.size());
        otherStars.addAll(nobodyStars);
        otherStars.addAll(userStars);

        for (Star comStar : comStars) {
            if (comStar.getInfectivity() > 50) {
                Star targetStar = findNearest(comStar, otherStars);
                if (targetStar != null) {
                    otherStars.remove(targetStar);
                    createDust(comStar, targetStar);
                }
            }
        }
    }

    private static void createDust(Star from, Star to) {
        int halfInfectivity = from.getInfectivity() / 2;

        Dust dust = new Dust(from, to);

        from.setInfectivity(halfInfectivity);
        dust.setInfectivity(halfInfectivity);
    }

    private static Star findNearest(Star from, List<Star> stars) {
        Star minStar = null;
        float minDistance = Float.MAX_VALUE;
        for (Star star : stars) {
            float distance = Coordinate.calculateDistance(from.coordinate, star.coordinate);
            if (distance < minDistance) {
                minDistance = distance;
                minStar = star;
            }
        }
        return minStar;
    }

    private static List<List<Star>> groupStars(List<Star> stars) {
        List<List<Star>> groupedStars = new ArrayList<>(3);
        groupedStars.add(new ArrayList<Star>(stars.size()));
        groupedStars.add(new ArrayList<Star>(stars.size()));
        groupedStars.add(new ArrayList<Star>(stars.size()));

        for (Star star : stars) {
            groupedStars.get(star.getOwner().getPlayerType()).add(star);
        }

        return groupedStars;
    }
}
