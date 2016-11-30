package cd.lunarlander.impl.control;

import cd.lunarlander.exercise.Lander;
import cd.lunarlander.exercise.MyLandingController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

public class LanderImpl implements Lander {

    private static final LanderImpl instance = new LanderImpl(new MoonEnvironment(800,600,new LunarSurface()));

    private LanderImpl()
    {}

    public static LanderImpl getInstance(){
        return instance;
    }

    private double descentThrust;
    private double velocityX;
    private double velocityY;
    private double positionX = 500;
    private double positionY = 20;
    private boolean rightThrusterOn;
    private boolean leftThrusterOn;
    protected Environment environment;
    private Image landerImage = new Image( "lunarlander.png" );
    private boolean landed = false;
    private boolean crashed = false;
    private double lastUpdatedTime = 0;
    private double lastUpdatedFlameTime = 0;
    private double lastUpdatedCrashTime = 0;
    private LanderController controller = new MyLandingController();
    private Image[] flameImages = { new Image("flame_1.png"), new Image("flame_2.png"), new Image("flame_3.png"),
            new Image("flame_4.png"), new Image("flame_5.png") };
    private Image[] crashImages = { new Image("crash_1.png"), new Image("crash_5.png"), new Image("crash_3.png"), new Image("crash_4.png"), new Image("crash_5.png")};
    private int latestFlameImageIndex = 0;
    private int latestCrashImageIndex = 0;
    Image green = new Image("green.png");
    Image red = new Image("red.png");

    private static final double HORIZONTAL_THRUST_LEVEL = 10;

    public LanderImpl(Environment environment){
        this.environment = environment;
    }

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

        if (positionY < -10) positionY = -10;

        if (landed) return;

        if (time - lastUpdatedFlameTime > 0.1){
            latestFlameImageIndex = (latestFlameImageIndex + 1) % flameImages.length; lastUpdatedFlameTime = time;
        }
        if (time - lastUpdatedCrashTime > 0.1){
            latestCrashImageIndex = (latestCrashImageIndex + 1) % crashImages.length; lastUpdatedCrashTime = time;
        }
        if (crashed){ return; }
        if (this.getAltitude() <= 0){ processLanding(); }
        else {
            velocityX += getAccelerationX() * (time-lastUpdatedTime);
            velocityY += getAccelerationY() * (time-lastUpdatedTime);
        }
        controller.control(this);
        lastUpdatedTime = time;

    }

    public void render(GraphicsContext gc){
        if (crashed){
            renderCrash(gc);
        }
        else {
            gc.drawImage(landerImage, gc.getCanvas().getWidth() / 2 - landerImage.getWidth() * 0.5, positionY);
            renderFlame(gc);
        }
        renderDescentSpeed(gc);
    }

    private void renderCrash(GraphicsContext gc){

        double crashWidth = landerImage.getWidth() * 1.5;
        double crashHeight = crashImages[latestCrashImageIndex].getHeight()
                * (crashWidth / crashImages[latestCrashImageIndex].getWidth());
        double crashPositionX = gc.getCanvas().getWidth()/2 - crashWidth / 2.0;
        double crashPositionY = positionY + landerImage.getHeight()/2 - crashHeight/2;
        gc.drawImage(crashImages[latestCrashImageIndex], crashPositionX, crashPositionY, crashWidth, crashHeight);
    }

    private void renderFlame(GraphicsContext gc){
        if (descentThrust == 0.0) return;
        double flamePositionY = positionY + landerImage.getHeight() + 5.0;
        double maxFlameWidth = landerImage.getWidth() * 0.8;
        double flameWidth = descentThrust >= 500 ? maxFlameWidth : maxFlameWidth * (descentThrust / 500.0);
        double flameHeight = flameImages[latestFlameImageIndex].getHeight()
                * (flameWidth / flameImages[latestFlameImageIndex].getWidth());
        double flamePositionX = gc.getCanvas().getWidth()/2 - flameWidth / 2.0;
        gc.drawImage(flameImages[latestFlameImageIndex], flamePositionX, flamePositionY, flameWidth, flameHeight);
    }

    private void renderDescentSpeed(GraphicsContext gc){

        gc.drawImage(green, 10, 10, getVelocityY() < 0 ? 0 : getVelocityY() > 50 ? 50 : getVelocityY(), 10 );
        gc.drawImage(red, 60, 10, getVelocityY() <= 50 ? 0 : getVelocityY() > 200 ? 200 : getVelocityY(), 10 );

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
        descentThrusterOn(1);
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

    public void setController(LanderController controller) {
        this.controller = controller;
    }
}
