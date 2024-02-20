package ru.nsu.vorobev.paint.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.nsu.vorobev.paint.Model;

public class CanvasOptionsController {
    private Stage stage;
    private Model model;

    @FXML
    protected TextField widthTextField;
    @FXML
    protected TextField heigthTextField;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setModel(Model model) {
        this.model = model;
    }


    public void init(){
        widthTextField.setText("" + model.getCanvasWidth());
        heigthTextField.setText("" + model.getCanvasHeight());
    }

    int getIntFromTextField(TextField textField, int from, int to){
        try{
            int value = Integer.parseInt(textField.getText());
            if(value < from || value > to){
                return -1;
            }
            return value;
        } catch (NumberFormatException ex){
            return -1;
        }
    }

    @FXML
    protected void onSelectBtnClick(){
        int newWidth;
        int newHeight;
        newHeight = getIntFromTextField(heigthTextField,1,2000);
        if(newHeight == -1){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Длина должна быть целым числом от 1 до 2000.", ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }
        newWidth = getIntFromTextField(widthTextField,1,2000);
        if(newWidth == -1){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ширина должна быть целым числом от 1 до 2000.", ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }
        model.setCanvasSize(newWidth,newHeight);
        stage.close();
    }
    @FXML
    protected void onCancelBtnClick(){
        stage.close();
    }
}
