package cd.lunarlander;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Lander {

    private double descentThrust;
    private double velocityX;
    private double velocityY;
    private double positionX = 500;
    private double positionY;
    private boolean rightThrusterOn;
    private boolean leftThrusterOn;
    private Environment environment = new MoonEnvironment();
    private Image image = new Image( "lunarlander.png" );
    private boolean landed = false;
    private boolean crashed = false;
    double lastUpdatedTime = 0;

    private static final double HORIZONTAL_THRUST_LEVEL = 0.001;

    public void update(double time){
        positionX += velocityX * (time-lastUpdatedTime);
        positionY += velocityY * (time-lastUpdatedTime);
        if (positionY < 200){ descentThrust = 0; }
        else if (positionY > 200){ descentThrust = 200; }
        if (positionY >= environment.getGroundPositionY(positionX) - image.getHeight()){
            positionY = environment.getGroundPositionY(positionX) - image.getHeight();
            velocityX = 0;
            velocityY = 0;
            descentThrust = 0;
            rightThrusterOn = false;
            leftThrusterOn = false;
            landed = true;
            System.out.println("Landed");
        }
        else {
            velocityX += getAccelerationX() * (time-lastUpdatedTime);
            velocityY += getAccelerationY() * (time-lastUpdatedTime);
        }
        System.out.println(velocityY);
        lastUpdatedTime = time;
    }

    public void render(GraphicsContext gc){
        gc.drawImage( image, positionX, positionY );
    }

    public double getAccelerationX(){
        if (landed || crashed) return 0.0;
        return getHorizontalNetThrust();
    }

    public double getAccelerationY(){
        if (landed || crashed) return 0.0;
        return environment.getGravitationalConstant() - descentThrust;
    }

    public double getDescentThrust(){
        return descentThrust;
    }

    private double getHorizontalNetThrust(){
        if (rightThrusterOn && leftThrusterOn) return 0.0;
        if (!(rightThrusterOn || leftThrusterOn)) return 0.0;
        return leftThrusterOn ? HORIZONTAL_THRUST_LEVEL : -HORIZONTAL_THRUST_LEVEL;
    }

    public double getVelocityX(){
        return velocityX;
    }

    public double getVelocityY(){
        return velocityY;
    }

    public void descentThrusterOn(){
        descentThrust = 0.01;
    }

    public void descentThrusterOn(int thrust){
        if (thrust < 0) descentThrust = 0.0;
        if (thrust > 5) descentThrust = 5.0;
        descentThrust = (double) thrust / 100.0;
    }

    public void descentThrusterOff(){
        descentThrust = 0.0;
    }

    public void rightThrusterOn(){
        rightThrusterOn = true;
    }

    public void rightThrusterOff(){
        rightThrusterOn = false;
    }

    public void leftThrusterOn(){
        leftThrusterOn = true;
    }

    public void leftThrusterOff(){
        leftThrusterOn = false;
    }



}
