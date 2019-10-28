package io.dplusic.stardust.ai;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.dplusic.cbes.EntityManager;
import io.dplusic.stardust.Selector;
import io.dplusic.stardust.entity.Player;
import io.dplusic.stardust.entity.Star;

public class NormalAI implements AI {

    private static final int TICKS_BETWEEN_ACTIONS = 60 * 2;
    private static final int MAX_DUST_IN_ONE_ACTION = 4;

    private EntityManager entityManager;
    private List<Star> stars;

    private ActionWithInterval moveActionWithInterval = new ActionWithInterval(TICKS_BETWEEN_ACTIONS, new Runnable() {
        @Override
        public void run() {
            AIUtils.StarGroupsByOnwer starGroupsByOnwer = AIUtils.groupStars(stars);

            List<Star> otherStars = new ArrayList<>(stars.size());
            otherStars.addAll(starGroupsByOnwer.nobody);
            otherStars.addAll(starGroupsByOnwer.user);

            List<ActionCandidate> candidates = new ArrayList<>(otherStars.size());

            for (Star comStar : starGroupsByOnwer.com) {
                if (comStar.getInfectivity() > 50) {
                    Pair<Star, Float> nearestPair = AIUtils.findNearest(comStar, otherStars);
                    if (nearestPair != null) {
                        otherStars.remove(nearestPair.first);
                        candidates.add(ActionCandidate.create(comStar, nearestPair.first, nearestPair.second));
                    }
                }
            }

            Collections.sort(candidates, new Comparator<ActionCandidate>() {
                @Override
                public int compare(ActionCandidate o1, ActionCandidate o2) {
                    return (int) (o1.distance - o2.distance);
                }
            });

            List<ActionCandidate> electedList = candidates.subList(0, Math.min(candidates.size(), MAX_DUST_IN_ONE_ACTION));
            for (ActionCandidate elected : electedList) {
                AIUtils.createDust(entityManager, elected.comStar, elected.targetStar);
            }
        }
    });

    public NormalAI(EntityManager entityManager, List<Star> stars) {
        this.entityManager = entityManager;
        this.stars = stars;
    }

    public void update() {
        moveActionWithInterval.update();
    }

    private static class ActionCandidate {
        public Star comStar;
        public Star targetStar;
        public Float distance;

        public static ActionCandidate create(Star comStar, Star targetStar, Float distance) {
            ActionCandidate actionCandidate = new ActionCandidate();
            actionCandidate.comStar = comStar;
            actionCandidate.targetStar = targetStar;
            actionCandidate.distance = distance;
            return actionCandidate;
        }
    }
}
