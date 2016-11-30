package cd.lunarlander.exercise;

/**
 * Created by Steve on 30/11/2016.
 */
public interface Lander {

    double getVelocityX();

    double getVelocityY();

    double getAltitude();

    void descentThrusterOn();

    void descentThrusterOn(int thrust);

    void descentThrusterOff();


}
