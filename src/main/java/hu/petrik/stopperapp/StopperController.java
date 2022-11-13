package hu.petrik.stopperapp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public class StopperController {

    @FXML
    private Button startStopBtn;
    @FXML
    private Button resetTimeBtn;
    @FXML
    private VBox timerHbox;
    @FXML
    private Label timerLabel;

    private boolean isStopperRun = false;

    private LocalTime startTime = null;
    private LocalTime stopTime = null;


    private String time;
    @FXML
    public void startStopAction(ActionEvent actionEvent) {
        if(isStopperRun) {
            stopTime = LocalTime.now();
            startStopBtn.setText("Start");
            isStopperRun = false;
            resetTimeBtn.setText("Reset");
        } else {
            resetTimeBtn.setText("Részidő");
            startStopBtn.setText("Stop");
            isStopperRun = true;

            Duration stopDuration = null;
            if (stopTime != null) {
                stopDuration = Duration.between(stopTime, LocalTime.now());
            }

            if (startTime == null) {
                startTime = LocalTime.now();
            }
            Timer timer = new Timer();
            Duration finalStopDuration = stopDuration;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(()-> {
                        if (isStopperRun) {
                            Duration stopperTime = Duration.between(startTime, LocalTime.now());
                            if (finalStopDuration != null) {
                                stopperTime.minus(finalStopDuration);
                            }
                            long hours = stopperTime.toHours();
                            long min = stopperTime.toMinutes() - (hours * 60);
                            long sec = stopperTime.toSeconds() -  (min * 60) - (hours * 60 * 60);
                            String milliText = String.valueOf ((double) stopperTime.toMillis() / 1000);
                            int milli = Integer.parseInt(milliText.split("\\.")[1]);

                            String[] texts = new String[4];
                            if (hours < 10) {
                                texts[0] = String.format("0%d", hours);
                            } else {
                                texts[0] = String.valueOf(hours);
                            }
                            if (min < 10) {
                                texts[1] = String.format("0%d", min);
                            } else {
                                texts[1] = String.valueOf(min);
                            }
                            if (sec < 10) {
                                texts[2] = String.format("0%d", sec);
                            } else {
                                texts[2] = String.valueOf(sec);
                            }
                            if (milli < 100) {
                                texts[3] = String.format("0%d", milli);
                            } else {
                                texts[3] = String.valueOf(milli);
                            }
                            time = String.format(texts[0] + ":" + texts[1] + ":" + texts[2] + "." + texts[3]);
                            timerLabel.setText(time);
                        } else {
                            timer.cancel();
                        }
                    });
                }
            }, 0, 1);
        }

    }

    @FXML
    public void resetTimeAction(ActionEvent actionEvent) {
        if (isStopperRun) {
            timerHbox.getChildren().add(new Label(time));
        } else {
            timerHbox.getChildren().clear();
            timerLabel.setText("00:00:00.000");
            startTime = null;
            stopTime = null;
        }
    }
}