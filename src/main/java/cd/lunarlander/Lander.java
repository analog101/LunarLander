package cd.lunarlander;

import cd.lunarlander.control.BasicLandingControl;
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
    private Image landerImage = new Image( "lunarlander.png" );
    private boolean landed = false;
    private boolean crashed = false;
    private double lastUpdatedTime = 0;
    private double lastUpdatedFlameTime = 0;
    private LanderController controller = new BasicLandingControl();
    private Image[] flameImages = { new Image("flame_1.png"), new Image("flame_2.png"), new Image("flame_3.png"),
            new Image("flame_4.png"), new Image("flame_5.png") };
    private int latestFlameImageIndex = 0;

    private static final double HORIZONTAL_THRUST_LEVEL = 10;

    public double getPositionX() {
        return positionX;
    }

    public double getAltitude(){
        return environment.getGroundPositionY(positionX) - landerImage.getHeight() - positionY;
    }

    private void processLanding(){
        if (Math.abs(velocityY) > 50.0 || Math.abs(velocityX) > 20.0) { crashed = true; }
        else { landed = true; }
        positionY = environment.getGroundPositionY(positionX) - landerImage.getHeight();
        velocityX = 0; velocityY = 0; descentThrust = 0;
        rightThrusterOn = false; leftThrusterOn = false;
    }

    public void update(double time){
        positionX += velocityX * (time-lastUpdatedTime);
        positionY += velocityY * (time-lastUpdatedTime);
        if (crashed){ return; }
        if (landed) return;
        if (this.getAltitude() <= 0){ processLanding(); }
        else {
            velocityX += getAccelerationX() * (time-lastUpdatedTime);
            velocityY += getAccelerationY() * (time-lastUpdatedTime);
        }
        controller.control(this);
        lastUpdatedTime = time;
        if (time - lastUpdatedFlameTime > 0.1){
            latestFlameImageIndex = (latestFlameImageIndex + 1) % flameImages.length;
            lastUpdatedFlameTime = time;
            System.out.println(latestFlameImageIndex);
        }
    }

    public void render(GraphicsContext gc){
        gc.drawImage( landerImage, 500, positionY );
        renderFlame( gc );
    }

    private void renderFlame(GraphicsContext gc){
        if (descentThrust == 0.0) return;
        double flamePositionY = positionY + landerImage.getHeight() + 5.0;
        double maxFlameWidth = landerImage.getWidth() * 0.8;
        double flameWidth = descentThrust >= 500 ? maxFlameWidth : maxFlameWidth * (descentThrust / 500.0);
        double flameHeight = flameImages[latestFlameImageIndex].getHeight()
                * (flameWidth / flameImages[latestFlameImageIndex].getWidth());
        double flamePositionX = 500 + landerImage.getWidth() * 0.5 - flameWidth / 2.0;
        gc.drawImage(flameImages[latestFlameImageIndex], flamePositionX, flamePositionY, flameWidth, flameHeight);
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
        descentThrust = thrust * 100.0;
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
