package cd.lunarlander;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Created by Steve on 18/11/2016.
 */
public class LunarSurface {

    private Image tile = new Image( "cratertile.png" );

    private double offsetX;

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public void render(GraphicsContext gc){
        double positionY = gc.getCanvas().getHeight() - tile.getHeight();
        double positionX = ( offsetX % tile.getWidth() ) - tile.getWidth();
        while (positionX < gc.getCanvas().getWidth()){
            gc.drawImage( tile, positionX, positionY );
            positionX += tile.getWidth();
        }
    }


}
