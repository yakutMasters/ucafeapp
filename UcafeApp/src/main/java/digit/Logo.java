package digit;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Scale;

import java.net.URISyntaxException;

public class Logo extends Parent {

    private Color logoColor;

    Polygon first = new Polygon(295, 155, 365, 225, 155, 440, -55, 225, 15, 155, 155, 300);
    public ImageView poridge;

    {
        try {
            poridge = new ImageView(new Image(getClass().getResource("/verh.png").toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Logo(){
        logoColor = Color.GHOSTWHITE;

        first.setFill(logoColor);
        first.setLayoutX(400);

        poridge.scaleXProperty().set(0.29);
        poridge.scaleYProperty().set(0.29);
        poridge.setLayoutY(50);
        poridge.setLayoutX(204);

        getTransforms().add(new Scale(0.83f, 0.83f, 0, 0));

        getChildren().addAll(first, poridge);
    }
}
