module ru.nsu.voroben.paint {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;


    opens ru.nsu.vorobev.paint.gui to javafx.fxml;
    exports ru.nsu.vorobev.paint.gui;
    exports ru.nsu.vorobev.paint;
    opens ru.nsu.vorobev.paint to javafx.fxml;
    exports ru.nsu.vorobev.paint.canvasFX;
    opens ru.nsu.vorobev.paint.canvasFX to javafx.fxml;
}