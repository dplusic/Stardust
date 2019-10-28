package io.dplusic.stardust.component;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import io.dplusic.stardust.Selector;
import io.dplusic.stardust.entity.StardustEntity;
import io.dplusic.stardust.mesh.Mesh;

public class Selectable extends Renderable {

	private static final int ID_INTERVAL = 16;

	private static Map<Integer, Selectable> idMap = new HashMap<Integer, Selectable>();
	private static int nextId = 1;

	private int id;
	private float[] uniqueColor;

	private Mesh selectingMesh;

	public Selectable(Mesh selectingMesh) {

		this.selectingMesh = selectingMesh;

		id = nextId++;
		idMap.put(id, this);

		uniqueColor = new float[4];
		int colorGenerator = id * ID_INTERVAL;
		for (int i = 0; i < 3; i++) {
			uniqueColor[i] = (float) (colorGenerator & 0xFF) / 0x100;
			colorGenerator >>= 8;
			colorGenerator *= ID_INTERVAL;
		}
		uniqueColor[3] = 0;

	}

	public void picking(GL10 gl) {
		getEntity().getMesh().picking(gl);
	}

	@Override
	public void draw(GL10 gl) {
		selectingMesh.draw(gl);
	}

	@Override
	public void update() {

		StardustEntity entity = getEntity();

		entity.getMesh().setUniqueColor(uniqueColor);
		updateMeshLocation(selectingMesh, entity.coordinate);

		super.update();
	}

	public static void selectByColor(Selector selector, byte red, byte green, byte blue) {

		int idRed = getColorId(red);
		int idGreen = getColorId(green);
		int idBlue = getColorId(blue);
		int id = idRed + idGreen * ID_INTERVAL + idBlue * ID_INTERVAL
				* ID_INTERVAL;

		System.out.println(red + " " + green + " " + blue + " " + id);

		Selectable selected;
		if (id == 0) {
			selected = null;
		} else {
			selected = idMap.get(id);
		}
		selector.touchOver(selected);
	}

	private static int getColorId(byte color) {
		if (color == 0) {
			return 0;
		}
		int intColor = (int) color & 0xFF;
		
		if(intColor > 130) {
			intColor +=8;
		}
		
		int id = intColor / ID_INTERVAL;
		return id;
	}
	
	public int getId(){
		return this.id;
	}

}
