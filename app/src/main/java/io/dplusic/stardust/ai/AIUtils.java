package io.dplusic.stardust.ai;

import java.util.ArrayList;
import java.util.List;

import io.dplusic.stardust.entity.Coordinate;
import io.dplusic.stardust.entity.Dust;
import io.dplusic.stardust.entity.Star;

public enum AIUtils {
    ;


    static List<List<Star>> groupStars(List<Star> stars) {
        List<List<Star>> groupedStars = new ArrayList<>(3);
        groupedStars.add(new ArrayList<Star>(stars.size()));
        groupedStars.add(new ArrayList<Star>(stars.size()));
        groupedStars.add(new ArrayList<Star>(stars.size()));

        for (Star star : stars) {
            groupedStars.get(star.getOwner().getPlayerType()).add(star);
        }

        return groupedStars;
    }

    static void createDust(Star from, Star to) {
        int halfInfectivity = from.getInfectivity() / 2;

        Dust dust = new Dust(from, to);

        from.setInfectivity(halfInfectivity);
        dust.setInfectivity(halfInfectivity);
    }

    static Star findNearest(Star from, List<Star> stars) {
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
}
