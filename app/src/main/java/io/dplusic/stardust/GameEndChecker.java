package io.dplusic.stardust;

import android.app.Activity;
import android.content.Intent;
import android.drm.DrmStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.dplusic.cbes.EntityManager;
import io.dplusic.stardust.entity.Dust;
import io.dplusic.stardust.entity.Player;
import io.dplusic.stardust.entity.Star;

public class GameEndChecker {

    private Activity playingActivity;

    private TextView[] infectivityViews;
    private ViewGroup gameOverView;
    private ViewGroup winView;

    private boolean gameEnd = false;

    public GameEndChecker(
            Activity playingActivity,
            TextView[] infectivityViews,
            ViewGroup gameOverView,
            ViewGroup winView) {

        this.playingActivity = playingActivity;

        this.infectivityViews = infectivityViews;
        this.gameOverView = gameOverView;
        this.winView = winView;
    }

    public void update() {

        if (gameEnd) {
            return;
        }

        Integer losePlayer = null;

        for (TextView infectivityView : infectivityViews) {

            Player player = Player.getInstance((Integer) infectivityView
                    .getTag());

            int infectivityOfStars = 0;
            int infectivityOfDusts = 0;

            List<Star> stars = player.getOwnedList(Star.class);
            for (Star star : stars) {
                infectivityOfStars += star.getInfectivity();
            }

            List<Dust> dusts = player.getOwnedList(Dust.class);
            for (Dust dust : dusts) {
                infectivityOfDusts += dust.getInfectivity();
            }

            infectivityView.setText(String.format("%d / %d",
                    infectivityOfDusts, infectivityOfStars));

            if (infectivityOfDusts + infectivityOfStars == 0) {
                losePlayer = player.getPlayerType();
            }
        }

        if (losePlayer != null) {
            if (losePlayer == Player.PLAYER_TYPE_USER) {
                processGameOver();
            } else if (losePlayer == Player.PLAYER_TYPE_COM) {
                processWin();
            }
            gameEnd = true;
        }
    }

    private void processGameOver() {
        gameOverView.setVisibility(View.VISIBLE);
        gameOverView.findViewById(R.id.button_retry_gameover).setOnClickListener(retry);
    }

    private void processWin() {
        winView.setVisibility(View.VISIBLE);
        winView.findViewById(R.id.button_retry_win).setOnClickListener(retry);
    }

    private View.OnClickListener retry = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = playingActivity.getIntent();
            playingActivity.finish();
            playingActivity.startActivity(intent);

            EntityManager.reset();
            Player.reset();
        }
    };
}
