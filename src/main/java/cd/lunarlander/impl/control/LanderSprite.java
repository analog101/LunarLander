package cd.lunarlander.impl.control;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class LanderSprite {

    private Image image = new Image( "lunarlander.png" );

    private double positionX = 500;
    private double positionY;

    private double velocityX;
    private double velocityY;

    private double accelerationX;
    private double accelerationY = 0.01;

    public void update(double time){
        positionX += velocityX * time;
        positionY += velocityY * time;
        velocityX += accelerationX * time;
        velocityY += accelerationY * time;
    }

    public void render(GraphicsContext gc){
        gc.drawImage( image, positionX, positionY );
    }

    public Rectangle2D getBoundary(){
        return new Rectangle2D(positionX,positionY,image.getWidth(),image.getHeight());
    }

    public boolean isOnGround(){
        return positionX == 0;
    }

}
