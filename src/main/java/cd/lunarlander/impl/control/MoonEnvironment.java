package cd.lunarlander.impl.control;

import javafx.scene.canvas.Canvas;

public class MoonEnvironment implements Environment {

    private double height;
    private double width;
    private LunarSurface lunarSurface;

    public MoonEnvironment(double width, double height, LunarSurface lunarSurface) {
        this.height = height;
        this.width = width;
        this.lunarSurface = lunarSurface;
    }

    public double getGravitationalConstant() { return 98; }

    @Override
    public double getGroundPositionY(double positionX) {
        return height - lunarSurface.getImageHeight();
    }

    public LunarSurface getSurface() {
        return lunarSurface;
    }
}
