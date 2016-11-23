package cd.lunarlander;

import javafx.scene.canvas.Canvas;

public class MoonEnvironment implements Environment {

    private Canvas canvas;
    private LunarSurface lunarSurface;

    public MoonEnvironment(Canvas canvas, LunarSurface lunarSurface) {
        this.canvas = canvas;
        this.lunarSurface = lunarSurface;
    }

    public double getGravitationalConstant() { return 98; }

    @Override
    public double getGroundPositionY(double positionX) {
        return canvas.getHeight() - lunarSurface.getImageHeight();
    }

    public LunarSurface getSurface() {
        return lunarSurface;
    }
}
