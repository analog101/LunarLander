package cd.lunarlander;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by Steve on 17/11/2016.
 */
public class LunarLanderApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle( "Lunar Lander" );

        Group root = new Group();
        Scene theScene = new Scene( root );
        primaryStage.setScene( theScene );

        Canvas canvas = new Canvas( 800, 600 );
        root.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image space = new Image( "sky.png" );

        Environment environment = new MoonEnvironment(canvas, new LunarSurface());

        Lander lander = new Lander(environment);

        new AnimationTimer()
        {
            final long startNanoTime = System.nanoTime();
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                gc.drawImage( space, 0, 0 );
                lander.render( gc );
                lander.update( t );
                environment.getSurface().setOffsetX(-lander.getPositionX());
                environment.getSurface().render( gc );

            }
        }.start();

        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
