package ru.nsu.vorobev.paint.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.nsu.vorobev.paint.Model;

public class LineOptionsController {
    private Stage stage;
    private Model model;
    @FXML
    protected Slider widthSlider;
    @FXML
    protected TextField widthTextField;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setModel(Model model) {
        this.model = model;
    }


    public void init(){
        widthSlider.setValue(model.getLineWidth());
        widthSlider.setMin(1);
        widthSlider.setMax(20);
        widthTextField.setText("" + model.getLineWidth());
        widthSlider.valueProperty().addListener((observableValue, number, t1) -> widthTextField.setText("" + t1.intValue()));
        widthTextField.setOnKeyPressed(keyEvent -> Utils.onIntTextFieldReact(keyEvent,widthSlider,widthTextField,1,20));
    }

    @FXML
    protected void onSelectBtnClick(){
        model.setLineWidth((int)widthSlider.getValue());
        stage.close();
    }
    @FXML
    protected void onCancelBtnClick(){
        stage.close();
    }
}
