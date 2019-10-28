package io.dplusic.stardust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.dplusic.android.view.AdvancedMotionEvent;
import io.dplusic.android.view.AdvancedOnTouchListener;
import io.dplusic.cbes.EntityManager;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Optional;

import io.dplusic.stardust.ai.AI;
import io.dplusic.stardust.ai.DebugAI;
import io.dplusic.stardust.ai.NormalAI;
import io.dplusic.stardust.component.Selectable;
import io.dplusic.stardust.entity.Dust;
import io.dplusic.stardust.entity.Player;
import io.dplusic.stardust.entity.Star;

public class PlayingActivity extends Activity {

	private static final int GAME_MILLISECOND_PER_CLOCK = 20; // millisecond

	private static final float SENSITIVITY_ROTATE = 0.5f;
	private static final float SENSITIVITY_ROTATE_ANGLE = 1.3f;

	private static final float MINIMUM_DELTA = 0.2f;
	private static final float MINIMUM_DELTA_ANGLE = 0.5f;
	private static final float MAXIMUM_DELTA = 10;

	private Handler handler = new Handler();

	private EntityManager entityManager;
	private Selector selector;
	private StardustRenderer renderer;
	private boolean confirmTouchScreen = false;

	private AI ai;
	private GameEndChecker gameEndManager;

	private TextView[] infectivityViews;

	private Random random = new Random();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		entityManager = new EntityManager();
		selector = new Selector(onSelected);
		renderer = new StardustRenderer(entityManager, selector, getResources());

		Player user = new Player(entityManager, Player.PLAYER_TYPE_USER);
		Player com = new Player(entityManager, Player.PLAYER_TYPE_COM);

		Map<Integer, Player> players = new HashMap<>();
		players.put(Player.PLAYER_TYPE_USER, user);
		players.put(Player.PLAYER_TYPE_COM, com);

		List<Star> stars = new ArrayList<Star>();

		for (int i = 180; i > -180; i -= 45) {
			for (int j = 60; j > -90; j -= 30) {
				Star star = new Star(entityManager, Optional.<Player>absent(), random.nextInt(), i, j);
				stars.add(star);
			}
		}

		Star namedStar1 = new Star(entityManager, Optional.of(user), random.nextInt(), 0, 90);
		Star namedStar2 = new Star(entityManager, Optional.of(com), random.nextInt(), 0, -90);

		stars.add(namedStar1);
		stars.add(namedStar2);

		ViewGroup playingLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.playing, null);
		setContentView(playingLayout);

		infectivityViews = new TextView[] {
			playingLayout.findViewById(R.id.textView_score1),
			playingLayout.findViewById(R.id.textView_score2)
		};
		initInfectivityView(infectivityViews[0], user);
		initInfectivityView(infectivityViews[1], com);

		GLSurfaceView glSurfaceView = new GLSurfaceView(this);
		glSurfaceView.setRenderer(renderer);
		glSurfaceView.setOnTouchListener(onViewTouchListener);
		((ViewGroup) playingLayout.findViewById(R.id.layout_glSurfaceViewWrapper)).addView(glSurfaceView);

		SensorManager man = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Sensor roc_sensor = man.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		man.registerListener(sListen, roc_sensor,
				SensorManager.SENSOR_DELAY_GAME);

		ai = new NormalAI(entityManager, stars);
		gameEndManager = new GameEndChecker(
				this,
				players,
				infectivityViews,
				(ViewGroup) playingLayout.findViewById(R.id.layout_gameover),
				(ViewGroup) playingLayout.findViewById(R.id.layout_win));
	}

	@Override
	protected void onStart() {
		super.onStart();

		handler.post(updater);
	}

	@Override
	protected void onStop() {
		super.onStop();

		handler.removeCallbacks(updater);
	}

	private Selector.OnSelected onSelected = new Selector.OnSelected() {
		@Override
		public void onSelected(Iterable<Selectable> selectablesFrom, Selectable selectableTo) {
			for (Selectable selectable : selectablesFrom) {

				Star start = (Star) selectable.getEntity();
				int halfInfectivity = start.getInfectivity() / 2;

				Dust dust = new Dust(entityManager, start,
						(Star) selectableTo.getEntity());

				start.setInfectivity(halfInfectivity);
				dust.setInfectivity(halfInfectivity);
			}
		}
	};

	private void initInfectivityView(TextView infectivityView, Player player) {
		float[] playerIdColor = player.getPlayerIdColor();

		infectivityView.setBackgroundColor(Color.argb(
				(int) (playerIdColor[3] * 255),
				(int) (playerIdColor[0] * 255),
				(int) (playerIdColor[1] * 255),
				(int) (playerIdColor[2] * 255)));
		infectivityView.setTag(player.getPlayerType());
	}

	private Runnable updater = new Runnable() {
		@Override
		public void run() {

			entityManager.updateAll();

			ai.update();
			gameEndManager.update();

			handler.postDelayed(updater, GAME_MILLISECOND_PER_CLOCK);
		}
	};

	private OnTouchListener onViewTouchListener = new AdvancedOnTouchListener() {

		@Override
		protected boolean afterPreprocess(View v, AdvancedMotionEvent event) {

			float deltaX = event.getDeltaX();
			float deltaY = event.getDeltaY();
			float deltaAngle = event.getDeltaAngle();

			float absDeltaX = Math.abs(deltaX);
			float absDeltaY = Math.abs(deltaY);
			float absDeltaAngle = Math.abs(deltaAngle);

			if (absDeltaX > MAXIMUM_DELTA) {
				deltaX = deltaX / absDeltaX * MAXIMUM_DELTA;
			} else if (absDeltaX < MINIMUM_DELTA) {
				deltaX = 0;
			}
			if (absDeltaY > MAXIMUM_DELTA) {
				deltaY = deltaY / absDeltaY * MAXIMUM_DELTA;
			} else if (absDeltaY < MINIMUM_DELTA) {
				deltaY = 0;
			}
			if (absDeltaAngle > MAXIMUM_DELTA) {
				deltaAngle = deltaAngle / absDeltaAngle * MAXIMUM_DELTA;
			} else if (absDeltaAngle < MINIMUM_DELTA_ANGLE) {
				deltaAngle = 0;
			}

			if (event.isMultiTouch() || event.isOneClicked()) {
				selector.endSelelecting();
				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					renderer.rotateCameraX(deltaX * SENSITIVITY_ROTATE);
					renderer.rotateCameraY(-deltaY * SENSITIVITY_ROTATE);
					renderer.rotateCameraUp(deltaAngle
							* SENSITIVITY_ROTATE_ANGLE);
					break;
				}
			} else {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					selector.startSelecting();
					PlayingActivity.this.confirmTouchScreen = true;
				case MotionEvent.ACTION_MOVE:
					selector.setSelectingXY(event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_UP:
					selector.endSelelecting();
					PlayingActivity.this.confirmTouchScreen = false;
					break;
				}
			}

			return true;
		}
	};

	private SensorEventListener sListen = new SensorEventListener() {
		private float[] oldSensorValue = new float[3];
		private float[] sensorValue = new float[3];
		private int sensorStateY = 0;
		private int sensorStateX = 0;

		@Override
		public void onSensorChanged(SensorEvent paramSensorEvent) {
			if (PlayingActivity.this.confirmTouchScreen == false)
				return;

			if (paramSensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
				return;
			}
			System.arraycopy(sensorValue, 0, oldSensorValue, 0, 3);
			System.arraycopy(paramSensorEvent.values, 0, sensorValue, 0, 3);
			if (oldSensorValue[1] == 0 || oldSensorValue[0] == 0)
				return;

			float speedY = ((oldSensorValue[1] - sensorValue[1]) * 10000);
			float speedX = ((oldSensorValue[0] - sensorValue[0]) * 10000);

			if (Math.abs(speedX) < Math.abs(speedY) && speedY > 0) {
				sensorStateY++;
			} else if (Math.abs(speedX) < Math.abs(speedY) && speedY < 0) {
				sensorStateY--;
			}

			if (Math.abs(speedX) > Math.abs(speedY) && speedX > 0) {
				sensorStateX++;
			} else if (Math.abs(speedX) > Math.abs(speedY) && speedX < 0) {
				sensorStateX--;
			}

			if (Math.abs(sensorStateY) == 3) {
				sensorStateY = 0;
				renderer.rotateCameraY(speedY / 10);
			}

			if (Math.abs(sensorStateX) == 3) {
				sensorStateX = 0;
				renderer.rotateCameraX(speedX / 10);
			}
		}

		@Override
		public void onAccuracyChanged(Sensor paramSensor, int paramInt) {
		}

	};

}