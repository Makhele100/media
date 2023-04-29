package com.example.media;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.fxml.Initializable;
import javafx.util.Duration;

import javax.swing.*;

public class HelloController implements Initializable
{
    @FXML
    private MediaView mediaView;

    @FXML
    private Button nextButton;

    @FXML
    private MenuItem openButton;

    @FXML
    private MenuItem closeButton;

    @FXML
    private Button playButton;

    @FXML
    private Button previousButton;

    @FXML
    private Button stopButton;

    @FXML
    private Slider videoSlider;

    @FXML
    private Slider volumeSlider;

    private MediaPlayer mediaPlayer;

    public String filePath;

    private List<String> extensions;

    @FXML
    private void open(ActionEvent event)
    {
        extensions= Arrays.asList("*.mp4","*.3gp","*.mkv","*.MP4","*.MKV","*.3GP","*.flv","*.wmv");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("select file",extensions);

        try
        {
            fileChooser.getExtensionFilters().add(filter);
            fileChooser.setTitle("Select a File to Open");
            File file = fileChooser.showOpenDialog(null);
            filePath = file.toURI().toString();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        if(filePath!= null)
        {
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            DoubleProperty width = mediaView.fitWidthProperty();
            DoubleProperty height = mediaView.fitHeightProperty();

            width.bind(Bindings.selectDouble(mediaView.sceneProperty(),"width"));
            height.bind(Bindings.selectDouble(mediaView.sceneProperty(),"height"));

            volumeSlider.setValue(mediaPlayer.getVolume()*100);
            volumeSlider.valueProperty().addListener(new InvalidationListener()
            {
                @Override
                public void invalidated(Observable obeservable){
                    mediaPlayer.setVolume(volumeSlider.getValue()/100);
                }
            });
            mediaPlayer.setOnReady(new Runnable()
            {
                @Override
                public void run()
                {
                    //System.out.println("Duration: "+ mediaPlayer.getTotalDuration().toSeconds());
                    videoSlider.setMin(0.0);
                    videoSlider.setValue(0.0);
                    videoSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());

                    mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>()
                    {
                        @Override
                        public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue)
                        {
                            videoSlider.setValue(newValue.toSeconds());
                        }
                    });

                    videoSlider.setOnMouseClicked(new EventHandler<MouseEvent>()
                    {
                        @Override
                        public void handle(MouseEvent event)
                        {
                            mediaPlayer.seek(Duration.seconds(videoSlider.getValue()));
                        }

                    });
                    videoSlider.valueProperty().addListener(o ->
                    {
                        if (videoSlider.isValueChanging())
                        {
                            if (mediaPlayer.getMedia().getDuration() != null)
                            {
                                mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(videoSlider.getValue()));
                            }
                       }
                    });

                }
            });

            mediaPlayer.play();
        }
    }

    @FXML
    private void play(ActionEvent event)
    {
        try
        {
            if (mediaPlayer != null)
            {
                mediaPlayer.play();
            }
            else
            {
                mediaPlayer.pause();
            }
        }
        catch(Exception e)
        {
            //System.out.println("Open up a file First");
            JOptionPane.showMessageDialog(null, "Open up a File First");
        }
    }

    @FXML
    private void previous(ActionEvent event)
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(javafx.util.Duration.seconds(10)));
        }
    }

    @FXML
    private void stop(ActionEvent event)
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
        }
    }

    @FXML
    private void next(ActionEvent event)
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(10)));
        }
    }

    @FXML
    private void close(ActionEvent event)
    {
        Platform.exit();
    }
    public void initialize(URL url, ResourceBundle rb)
    {

    }
}
