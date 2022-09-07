package example.myapp;

import digit.Logo;
import digit.Tablo;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController {

    int mainBank;
    long lostMinutes;

    private Label countLabel = new Label("");

    @FXML
    private Logo logo = new Logo();

    @FXML
    private Tablo tablo = new Tablo();

    ConnectToMongo connect = new ConnectToMongo();

    @FXML
    private Button button;

    @FXML
    protected void onHelloButtonClick() {

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               Date currentDate = new Date();

                               mainBank = connect.getNumber();
                               countLabel.setText("" + mainBank);

                               Client rsltHndl = new Client(currentDate.getTime() - currentDate.getTime() % 1000 - 120000, currentDate.getTime() - currentDate.getTime() % 1000 - 60000);
                               String result = rsltHndl.getContent().toString();
                               /* проверяем связь и создаем нового клиента за промежуток равный продолжительности пустых ответов
                               нужно проверить не будет ли он дублировать предыдущие продажи тк время может оказать слишком большим */
                               if (!rsltHndl.getContent().isEmpty()) {
                                   System.out.println("Продажи: " + result);
                                   rsltHndl.checkSales(rsltHndl.getContent());
                                   mainBank += rsltHndl.getCount() * 10;

                                   if (lostMinutes != 0) {
                                       Client checkLost = new Client((currentDate.getTime() - currentDate.getTime() % 1000 + 10680000) - lostMinutes, currentDate.getTime() - currentDate.getTime() % 1000 + 10680000);
                                       if (!checkLost.getContent().isEmpty()) {
                                           System.out.println("До этого я обнаружил подозрительные долгие пустые ответы,\nи решил проверить еще раз этот период.\nВот продажи за этот период: " + checkLost.getContent());
                                           checkLost.checkSales(checkLost.getContent());
                                           mainBank += checkLost.getCount() * 10;
                                       }
                                       lostMinutes = 0;
                                   }
                               } else {
                                   lostMinutes += 60000;
                                   System.out.println("+" + lostMinutes + " миллисекунд");
                               }

                               connect.setNumber(mainBank);

                               System.out.println("" + mainBank + " was sent to MongoDB");

                               Platform.runLater(() -> {
                                   tablo.setBank(mainBank);
                                   tablo.refreshClocks();
                               });
                           }
                       },
                new Date(),
                60000);

        button.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            button.setVisible(false);
        });
        countLabel.textProperty().addListener((observableValue, s, t1) -> playAnimation());
    }

    public void playAnimation()  {
        Glow firstGlow = new Glow();
        firstGlow.setLevel(0.0);
        Timeline timeline1 = new Timeline();
        timeline1.setCycleCount(6);
        timeline1.setAutoReverse(true);
        KeyValue kv1 = new KeyValue(firstGlow.levelProperty(), 1.0);
        KeyFrame kf1 = new KeyFrame(Duration.millis(1000), kv1);
        timeline1.getKeyFrames().add(kf1);
        timeline1.play();

        Glow secondGlow = new Glow();
        secondGlow.setInput(firstGlow);
        secondGlow.setLevel(0.0);
        Timeline timeline2 = new Timeline();
        timeline2.setCycleCount(6);
        timeline2.setAutoReverse(true);
        KeyValue kv2 = new KeyValue(secondGlow.levelProperty(), 1.0);
        KeyFrame kf2 = new KeyFrame(Duration.millis(1000), kv2);
        timeline2.getKeyFrames().add(kf2);
        timeline2.play();

        Glow thirdGlow = new Glow();
        thirdGlow.setLevel(0.0);
        thirdGlow.setInput(secondGlow);
        Timeline timeline3 = new Timeline();
        timeline3.setCycleCount(6);
        timeline3.setAutoReverse(true);
        KeyValue kv3 = new KeyValue(thirdGlow.levelProperty(), 1.0);
        KeyFrame kf3 = new KeyFrame(Duration.millis(1000), kv3);
        timeline3.getKeyFrames().add(kf3);
        timeline3.play();

        Glow forthGlow = new Glow();
        forthGlow.setLevel(0.0);
        forthGlow.setInput(thirdGlow);
        Timeline timeline4 = new Timeline();
        timeline4.setCycleCount(6);
        timeline4.setAutoReverse(true);
        KeyValue kv4 = new KeyValue(forthGlow.levelProperty(), 1.0);
        KeyFrame kf4 = new KeyFrame(Duration.millis(1000), kv4);
        timeline4.getKeyFrames().add(kf4);
        timeline4.play();

        Glow fifthGlow = new Glow();
        fifthGlow.setLevel(0.0);
        fifthGlow.setInput(forthGlow);
        Timeline timeline5 = new Timeline();
        timeline5.setCycleCount(6);
        timeline5.setAutoReverse(true);
        KeyValue kv5 = new KeyValue(fifthGlow.levelProperty(), 1.0);
        KeyFrame kf5 = new KeyFrame(Duration.millis(1000), kv5);
        timeline5.getKeyFrames().add(kf5);
        timeline5.play();

        Glow sixthGlow = new Glow();
        sixthGlow.setLevel(0.0);
        sixthGlow.setInput(fifthGlow);
        Timeline timeline6 = new Timeline();
        timeline6.setCycleCount(6);
        timeline6.setAutoReverse(true);
        KeyValue kv6 = new KeyValue(sixthGlow.levelProperty(), 1.0);
        KeyFrame kf6 = new KeyFrame(Duration.millis(1000), kv6);
        timeline6.getKeyFrames().add(kf6);
        timeline6.play();

        logo.poridge.setEffect(sixthGlow);
    }
}

