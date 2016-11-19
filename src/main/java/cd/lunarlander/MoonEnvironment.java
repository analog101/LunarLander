package cd.lunarlander;

public class MoonEnvironment implements Environment {

    public double getGravitationalConstant() { return 98; }

    @Override
    public double getGroundPositionY(double positionX) {
        return 838.0;
    }

}
