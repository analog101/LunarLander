package cd.lunarlander.exercise;

import cd.lunarlander.impl.control.*;

/**
 * Created by Steve on 30/11/2016.
 */
public class MyLandingController implements LanderController {

    @Override
    public void control(Lander lander) {

        //Your code goes here!!!
        lander.descentThrusterOn(1);
        System.out.println(lander.getVelocityY());
    }

    public static void main(String[] args) throws Exception{

        LunarLanderApp.main(new String[0]);

    }


}
