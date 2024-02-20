package ru.nsu.vorobev.paint.gui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.nsu.vorobev.paint.Model;

public class PaletteWindowController {

    @FXML
    protected Canvas colorCanvas;
    @FXML
    protected Slider redSlider;
    @FXML
    protected TextField redTextField;
    @FXML
    protected Slider greenSlider;
    @FXML
    protected TextField greenTextField;
    @FXML
    protected Slider blueSlider;
    @FXML
    protected TextField blueTextField;
    private GraphicsContext gc;
    private Model model;
    private Stage myStage;
    public void setMyStage(Stage stage){
        myStage = stage;
    }
    public void setModel(Model model){
        this.model = model;
    }
    private void paintCanvas(Color color){
        gc.setFill(color);
        gc.fillRect(0,0,colorCanvas.getWidth(),colorCanvas.getHeight());
        System.out.println("Painted");
    }

    void onSliderReact(Slider slider, TextField textField){

        slider.valueProperty().addListener((observableValue, number, t1) -> {
            textField.setText(t1.intValue()+"");
            paintCanvas(new Color(redSlider.getValue() / 255,greenSlider.getValue()/255,blueSlider.getValue()/255,1.0));
        });
    }

    void onTextFieldReact(Slider slider, TextField textField){
        textField.setOnKeyPressed(keyEvent -> {
            Utils.onIntTextFieldReact(keyEvent,slider,textField,0,255);
            paintCanvas(new Color(redSlider.getValue() / 255, greenSlider.getValue() / 255, blueSlider.getValue() / 255, 1.0));

        });
    }

    @FXML
    protected void initialize(){
        gc = colorCanvas.getGraphicsContext2D();
        paintCanvas(Color.BLACK);
        onSliderReact(redSlider,redTextField);
        onSliderReact(blueSlider,blueTextField);
        onSliderReact(greenSlider,greenTextField);
        onTextFieldReact(redSlider,redTextField);
        onTextFieldReact(blueSlider,blueTextField);
        onTextFieldReact(greenSlider,greenTextField);
    }

    @FXML
    protected void onSelectBtnClick(){
        java.awt.Color color = new java.awt.Color((int)redSlider.getValue(),(int)greenSlider.getValue(),(int)blueSlider.getValue());
        model.setColor(color);
        myStage.close();
    }
    @FXML
    protected void onCancelBtnClick(){
        myStage.close();
    }

}
