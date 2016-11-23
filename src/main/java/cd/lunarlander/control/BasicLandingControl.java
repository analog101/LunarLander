package cd.lunarlander.control;

import cd.lunarlander.Lander;
import cd.lunarlander.LanderController;

public class BasicLandingControl implements LanderController{

    @Override
    public void control(Lander lander) {



        if (lander.getAltitude() < 100){
            lander.descentThrusterOn(2);
        }
        else if (lander.getAltitude() < 200){
            lander.descentThrusterOn(3);
        }
        else if (lander.getAltitude() < 300){
            lander.descentThrusterOn(4);
        }
        else{
            lander.descentThrusterOff();
        }

    }
}
