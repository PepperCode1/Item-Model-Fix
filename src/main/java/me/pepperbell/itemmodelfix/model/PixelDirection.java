package me.pepperbell.itemmodelfix.model;

import net.minecraft.util.math.Direction;

public enum PixelDirection {
	LEFT(Direction.WEST, -1, 0),
	RIGHT(Direction.EAST, 1, 0),
	UP(Direction.UP, 0, -1),
	DOWN(Direction.DOWN, 0, 1);

	public static final PixelDirection[] VALUES = values();

	private final Direction direction;
	private final int offsetX;
	private final int offsetY;

	PixelDirection(Direction direction, int offsetX, int offsetY) {
		this.direction = direction;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public Direction getDirection() {
		return direction;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public boolean isVertical() {
		return this == DOWN || this == UP;
	}
}
