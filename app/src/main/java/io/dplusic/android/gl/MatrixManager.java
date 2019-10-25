package io.dplusic.android.gl;

import java.util.Stack;

import android.opengl.Matrix;

public class MatrixManager implements Cloneable {

	private Stack<float[]> matrixStack;
	private float[] currentMatrix;

	public MatrixManager() {
		matrixStack = new Stack<float[]>();
		currentMatrix = new float[16];
		Matrix.setIdentityM(currentMatrix, 0);
	}

	public float[] getMatrix() {
		return currentMatrix.clone();
	}

	public void pushMatrix() {
		matrixStack.push(currentMatrix.clone());
	}

	public void popMatrix() {
		currentMatrix = matrixStack.pop();
	}

	public void translate(float x, float y, float z) {
		Matrix.translateM(currentMatrix, 0, x, y, z);
	}

	public void rotate(float a, float x, float y, float z) {
		Matrix.rotateM(currentMatrix, 0, a, x, y, z);
	}

	public void rotateRelated(float a, float x, float y, float z) {
		float[] relatedCoordinates = getRelatedCoordinates(x, y, z);
		Matrix.rotateM(currentMatrix, 0, a, relatedCoordinates[0],
				relatedCoordinates[1], relatedCoordinates[2]);
	}

	public void translateRelated(float a, float x, float y, float z) {
		float[] relatedCoordinates = getRelatedCoordinates(x, y, z);
		Matrix.translateM(currentMatrix, 0, a * relatedCoordinates[0], a
				* relatedCoordinates[1], a * relatedCoordinates[2]);
	}

	public void multiplyMatrix(float[] m) {
		float[] result = new float[16];
		Matrix.multiplyMM(result, 0, currentMatrix, 0, m, 0);
		currentMatrix = result;
	}

	public void multiplyMatrixRelated(float[] m) {
		float[] result = new float[16];
		Matrix.multiplyMM(result, 0, m, 0, currentMatrix, 0);
		currentMatrix = result;
	}

	public void multiplyVector(float[] result, float[] vector) {
		Matrix.multiplyMV(result, 0, currentMatrix, 0, vector, 0);
	}

	private float[] getRelatedCoordinates(float x, float y, float z) {
		float[] result = new float[16];
		float[] relatedMatrix = new float[] { //
		x, 0, 0, 0, //
				y, 0, 0, 0, //
				z, 0, 0, 0, //
				0, 0, 0, 0, //
		};
		Matrix.multiplyMM(result, 0, relatedMatrix, 0, currentMatrix, 0);
		return new float[] { result[0], result[4], result[8] };
	}

	@Override
	public MatrixManager clone() {
		MatrixManager matrixManager = new MatrixManager();
		matrixManager.currentMatrix = currentMatrix;
		return matrixManager;
	}
}