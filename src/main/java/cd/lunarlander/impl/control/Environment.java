package cd.lunarlander.impl.control;

public interface Environment {

    double getGravitationalConstant();

    double getGroundPositionY(double positionX);

    LunarSurface getSurface();
}
