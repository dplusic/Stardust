package io.dplusic.stardust.mesh;

public class Cube extends Mesh {

	public Cube(float width, float depth, float height) {

		width /= 2;
		depth /= 2;
		height /= 2;

		float vertices[] = { -width, -depth, height, //
				width, -depth, height, //
				-width, depth, height, //
				width, depth, height, //

				width, -depth, height, //
				width, -depth, -height, //
				width, depth, height, //
				width, depth, -height, //

				width, -depth, -height, //
				-width, -depth, -height, //
				width, depth, -height, //
				-width, depth, -height, //

				-width, -depth, -height, //
				-width, -depth, height, //
				-width, depth, -height, //
				-width, depth, height, //

				-width, -depth, -height, //
				width, -depth, -height, //
				-width, -depth, height, //
				width, -depth, height, //

				-width, depth, height, //
				width, depth, height, //
				-width, depth, -height, //
				width, depth, -height, //
		};

		float normalVectors[] = { 0, 0, 1, //
				0, 0, 1, //
				0, 0, 1, //
				0, 0, 1, //

				1, 0, 0, //
				1, 0, 0, //
				1, 0, 0, //
				1, 0, 0, //

				0, 0, -1, //
				0, 0, -1, //
				0, 0, -1, //
				0, 0, -1, //

				-1, 0, 0, //
				-1, 0, 0, //
				-1, 0, 0, //
				-1, 0, 0, //

				0, -1, 0, //
				0, -1, 0, //
				0, -1, 0, //
				0, -1, 0, //

				0, 1, 0, //
				0, 1, 0, //
				0, 1, 0, //
				0, 1, 0, //
		};

		short indices[] = { 0, 1, 3, //
				0, 3, 2, //

				4, 5, 7, //
				4, 7, 6, //

				8, 9, 11, //
				8, 11, 10, //

				12, 13, 15, //
				12, 15, 14, //

				16, 17, 19, //
				16, 19, 18, //

				20, 21, 23, //
				20, 23, 22, //
		};

		setVertices(vertices);
		setIndices(indices);
		setNormalVectors(normalVectors);
	}

	public Cube() {
		this(20, 20, 20);
	}
}
