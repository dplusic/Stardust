package io.dplusic.android.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class GLHelper {

	public static FloatBuffer fromArrayToBuffer(float[] floatArray) {
		FloatBuffer floatBuffer;
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(floatArray.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		floatBuffer = byteBuf.asFloatBuffer();
		floatBuffer.put(floatArray);
		floatBuffer.position(0);
		return floatBuffer;
	}

	public static ShortBuffer fromArrayToBuffer(short[] shortArray) {
		ShortBuffer shortBuffer;
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(shortArray.length * 2);
		byteBuf.order(ByteOrder.nativeOrder());
		shortBuffer = byteBuf.asShortBuffer();
		shortBuffer.put(shortArray);
		shortBuffer.position(0);
		return shortBuffer;
	}
}
