package io.dplusic.stardust.ai;

import android.util.Pair;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;

import io.dplusic.cbes.EntityManager;
import io.dplusic.stardust.entity.Coordinate;
import io.dplusic.stardust.entity.Dust;
import io.dplusic.stardust.entity.Player;
import io.dplusic.stardust.entity.Star;

public enum AIUtils {
    ;

    static class StarGroupsByOnwer {
        public List<Star> com;
        public List<Star> user;
        public List<Star> nobody;
    }

    static StarGroupsByOnwer groupStars(List<Star> stars) {
        StarGroupsByOnwer starGroupsByOnwer = new StarGroupsByOnwer();
        starGroupsByOnwer.com  = new ArrayList<Star>(stars.size());
        starGroupsByOnwer.user = new ArrayList<Star>(stars.size());
        starGroupsByOnwer.nobody = new ArrayList<Star>(stars.size());

        for (Star star : stars) {
            Optional<Player> ownerOptional = star.getOwnerOptional();
            if (ownerOptional.isPresent()) {
                Player owner = ownerOptional.get();
                if (owner.getPlayerType() == Player.PLAYER_TYPE_COM) {
                    starGroupsByOnwer.com.add(star);
                } else if (owner.getPlayerType() == Player.PLAYER_TYPE_USER) {
                    starGroupsByOnwer.user.add(star);
                }
            } else {
                starGroupsByOnwer.nobody.add(star);
            }
        }

        return starGroupsByOnwer;
    }

    static void createDust(EntityManager entityManager, Star from, Star to) {
        int halfInfectivity = from.getInfectivity() / 2;

        Dust dust = new Dust(entityManager, from, to);

        from.setInfectivity(halfInfectivity);
        dust.setInfectivity(halfInfectivity);
    }

    static Pair<Star, Float> findNearest(Star from, List<Star> stars) {
        Star minStar = null;
        float minDistance = Float.MAX_VALUE;
        for (Star star : stars) {
            float distance = Coordinate.calculateDistance(from.coordinate, star.coordinate);
            if (distance < minDistance) {
                minDistance = distance;
                minStar = star;
            }
        }

        if (minStar == null) {
            return null;
        } else {
            return Pair.create(minStar, minDistance);
        }
    }
}
