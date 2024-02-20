package ru.nsu.vorobev.paint.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.nsu.vorobev.paint.Model;

public class StarOptionsController {
    private Model model;
    private Stage stage;

    @FXML
    protected Slider widthSlider;
    @FXML
    protected Slider pointsSlider;
    @FXML
    protected Slider radiusSlider;
    @FXML
    protected Slider angleSlider;
    @FXML
    protected TextField widthTextField;
    @FXML
    protected TextField pointsTextField;
    @FXML
    protected TextField radiusTextField;
    @FXML
    protected TextField angleTextField;

    public void setModel(Model model) {
        this.model = model;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init(){
        widthSlider.setMin(1);
        widthSlider.setMax(12);
        widthSlider.setValue(model.getStarWidth());
        widthTextField.setText(model.getStarWidth() + "");
        pointsSlider.setMin(3);
        pointsSlider.setMax(20);
        pointsSlider.setValue(model.getStarCountOfPoints());
        pointsTextField.setText(model.getStarCountOfPoints() + "");
        radiusSlider.setMin(3);
        radiusSlider.setMax(200);
        radiusSlider.setValue(model.getStarExternalRadius());
        radiusTextField.setText("" + model.getStarExternalRadius());
        angleSlider.setMin(0);
        angleSlider.setMax(360);
        angleSlider.setValue(model.getStarAngle());
        angleTextField.setText("" + model.getStarAngle());
        widthSlider.valueProperty().addListener((observableValue, number, t1) -> widthTextField.setText("" + t1.intValue()));
        pointsSlider.valueProperty().addListener((observableValue, number, t1) -> pointsTextField.setText("" + t1.intValue()));
        radiusSlider.valueProperty().addListener((observableValue, number, t1) -> radiusTextField.setText("" + t1.intValue()));
        angleSlider.valueProperty().addListener((observableValue, number, t1) -> angleTextField.setText("" + t1.intValue()));
        widthTextField.setOnKeyPressed(keyEvent -> Utils.onIntTextFieldReact(keyEvent,widthSlider,widthTextField,1,12));
        pointsTextField.setOnKeyPressed(keyEvent -> Utils.onIntTextFieldReact(keyEvent,pointsSlider,pointsTextField,3,20));
        radiusTextField.setOnKeyPressed(keyEvent -> Utils.onIntTextFieldReact(keyEvent,radiusSlider,radiusTextField,3,200));
        angleTextField.setOnKeyPressed(keyEvent -> Utils.onIntTextFieldReact(keyEvent,angleSlider,angleTextField,0,360));
    }
    @FXML
    protected void onSelectBtnClick(){
        model.setStarWidth((int)widthSlider.getValue());
        model.setStarAngle((int)angleSlider.getValue());
        model.setStarExternalRadius((int)radiusSlider.getValue());
        model.setStarCountOfPoints((int)pointsSlider.getValue());
        model.setStarInternalRadius((int)radiusSlider.getValue()/2);
        stage.close();
    }
    @FXML
    protected void onCancelBtnClick(){
        stage.close();
    }
}
