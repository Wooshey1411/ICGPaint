package ru.nsu.vorobev.paint.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Utils {
    public static void onIntTextFieldReact(KeyEvent keyEvent, Slider slider, TextField textField, int from, int to){
            if (keyEvent.getCode() == KeyCode.ENTER) {
                int prevValue = (int)slider.getValue();
                int caretPosition = textField.getCaretPosition();
                String text = textField.getText();
                int color;
                try {
                    color = Integer.parseInt(text);
                    if (color < from || color > to) {
                        throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Значение должно быть целым от " + from + " до " + to + ".", ButtonType.CLOSE);
                    alert.showAndWait();
                    textField.setText(prevValue + "");
                    textField.positionCaret(caretPosition);
                    return;
                }
                slider.setValue(color);
                textField.positionCaret(caretPosition);
            }
    }
}
