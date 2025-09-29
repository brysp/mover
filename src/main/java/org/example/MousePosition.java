package org.example;

import java.awt.*;

public class MousePosition {

    public static Point getMousePosition() {

        PointerInfo pointerInfo = MouseInfo.getPointerInfo();

        if(pointerInfo == null) {
            return getScreenCenter();
        }
        return pointerInfo.getLocation();
    }

    public static Point getScreenCenter() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
    }

}
