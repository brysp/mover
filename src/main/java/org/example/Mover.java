package org.example;

import java.awt.*;

public class Mover {

    private static Mover mover;
    private Direction direction;

    private Robot robot;

    private Point lastPosition;

    private Point mousePos;


    public static Mover getInstance() {

        if(mover == null) {
            initMover();
        }
        return mover;
    }

    public static void initMover() {
        mover = new Mover();

    }

    public boolean move() {
        boolean moved = false;

        if(mover == null) {
            initMover();
        }

        if(lastPosition == null) {
            lastPosition = MousePosition.getMousePosition();
        }
        mousePos = MousePosition.getMousePosition();

        if(lastPosition.equals(mousePos)) {
            Point newMousePos = mover.getNewMove(mousePos);
            robot.mouseMove(newMousePos.x, newMousePos.y);
            moved = true;
        }

        lastPosition = MousePosition.getMousePosition();
        return moved;
    }

    private Mover() {
        direction = Direction.UP;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    private void nextDirection() {
        switch (direction) {
            case UP:
                direction = Direction.RIGHT;
                break;
            case LEFT:
                direction = Direction.UP;
                break;
            case DOWN:
                direction = Direction.LEFT;
                break;
            case RIGHT:
                direction = Direction.DOWN;
                break;
        }
    }

    public Point getNewMove(Point currentPoint) {

        nextDirection();

        switch (direction) {
            case UP:
                return new Point((int)currentPoint.getX(), (int)currentPoint.getY() + 15);
            case LEFT:
                return new Point((int)currentPoint.getX() + 15, (int)currentPoint.getY());
            case DOWN:
                return new Point((int)currentPoint.getX(), (int)currentPoint.getY() - 15);
            case RIGHT:
                return new Point((int)currentPoint.getX() - 15, (int)currentPoint.getY());
        }
        return new Point((int)currentPoint.getX(), (int)currentPoint.getY());
    }
}
