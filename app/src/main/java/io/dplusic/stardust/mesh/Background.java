package io.dplusic.stardust.mesh;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import io.dplusic.stardust.R;

public class Background extends Mesh {

	public Background(Resources resources) {

		float textureCoordinates[] = { 0, 1, 0, //
				0.6f, 1, 0, //
				0.6f, 0, 0, //
				0, 0, 0, //
		};

		float[] vertices = new float[] { 0, 0, 0, //
				1, 0, 0, //
				1, 1, 0, //
				0, 1, 0, //
		};

		short[] indices = new short[] { 0, 1, 2, //
				0, 2, 3 //
		};

		setIndices(indices);
		setVertices(vertices);
		setTextureCoordinates(textureCoordinates);

		loadBitmap(BitmapFactory.decodeResource(resources,
				R.drawable.space));
	}
}
