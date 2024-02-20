package ru.nsu.vorobev.paint.gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.nsu.vorobev.paint.Model;
import ru.nsu.vorobev.paint.canvasFX.DragResizerXY;
import ru.nsu.vorobev.paint.canvasFX.ResizableCanvas;
import ru.nsu.vorobev.paint.graphic.IPrinter2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class MainWindowController implements ModelListener {
    @FXML
    protected ScrollPane mainScrollPane;
    @FXML
    protected StackPane canvasHolder;
    @FXML
    protected ToggleButton lineDrawBtn;
    @FXML
    protected ToggleButton fillDrawBtn;
    @FXML
    protected ToggleButton polygonDrawBtn;
    @FXML
    protected ToggleButton starDrawBtn;
    @FXML
    protected Button openFileBtn;
    @FXML
    protected Button optionsBtn;
    @FXML
    protected Button chooseColorBtn;
    @FXML
    protected Button clearBtn;
    @FXML
    protected Button infoBtn;
    @FXML
    protected RadioMenuItem radioLineDrawBtn;
    @FXML
    protected RadioMenuItem radioStarDrawBtn;
    @FXML
    protected RadioMenuItem radioPolygonDrawBtn;
    @FXML
    protected RadioMenuItem radioFillDrawBtn;
    @FXML
    protected MenuItem menuOpenFile;
    @FXML
    protected MenuItem menuSaveFile;
    @FXML
    protected MenuItem menuAbout;
    private IPrinter2D printer2D;
    private ResizableCanvas canvas;
    private Model model;
    private void setButtonImage(ButtonBase button, String imageName, int opacity){
        try {
            button.setGraphic(new ImageView(new Image(Objects.requireNonNull(MainWindowController.class.getResourceAsStream(imageName)),button.getPrefWidth() - opacity,button.getPrefHeight() - opacity,false,false)));
        } catch (NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, "File not found " + imageName, ButtonType.CLOSE);
            alert.showAndWait();
            System.exit(0); // ??
        }
    }

    @FXML
    protected void initialize(){
        model = new Model();
        model.setModelListener(this);
        ResizableCanvas resizableCanvas = new ResizableCanvas();
        canvas = resizableCanvas;
        resizableCanvas.setWidth(canvasHolder.getPrefWidth());
        resizableCanvas.setHeight(canvasHolder.getPrefHeight());
        model.setCanvasSize((int)canvas.getWidth(),(int)canvas.getHeight());
        GraphicsContext gc = resizableCanvas.getGraphicsContext2D();
        PixelWriter pixelWriter = gc.getPixelWriter();
        printer2D = new IPrinter2D() {
            @Override
            public void printPoint(int x, int y, Color color) {
                pixelWriter.setColor(x,y,new javafx.scene.paint.Color(color.getRed() / 255.0, color.getGreen() / 255.0,color.getBlue() / 255.0,1.0));
            }

            @Override
            public void printLine(int x1, int y1, int x2, int y2,int width, Color color) {
                gc.setStroke(new javafx.scene.paint.Color(color.getRed() / 255.0, color.getGreen() / 255.0,color.getBlue() / 255.0,1));
                gc.setLineWidth(width);
                gc.strokeLine(x1,y1,x2,y2);
            }

            @Override
            public int[] getGridARGB() {
                WritableImage writableImage = new WritableImage((int) resizableCanvas.getWidth(), (int) resizableCanvas.getHeight());
                resizableCanvas.snapshot(null, writableImage);

                PixelReader pixelReader = writableImage.getPixelReader();

                int width = (int) writableImage.getWidth();
                int height = (int) writableImage.getHeight();

                int[] pixels = new int[width * height];

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int argb = pixelReader.getArgb(x, y);
                        pixels[y * width + x] = (argb);
                    }
                }
                return pixels;
            }

            @Override
            public void redrawCanvas(int[] pixels) {
                pixelWriter.setPixels(0,0,(int)canvas.getWidth(),(int)canvas.getHeight(),PixelFormat.getIntArgbInstance(),pixels,0, (int)canvas.getWidth());
            }
        };
        model.setPrinter2D(printer2D);
        resizableCanvas.setOnMouseClicked(mouseEvent -> {
            if(!model.getIsToolSelected() || model.getOnMouseClickReaction() == null || !mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                return;
            }
            model.getOnMouseClickReaction().accept(mouseEvent);
        });
        resizableCanvas.setOnMouseDragged(mouseEvent -> {
            if(!model.getIsToolSelected() || model.getOnMouseDraggedReaction() == null || !mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                return;
            }
            model.getOnMouseDraggedReaction().accept(mouseEvent);
        });
        resizableCanvas.setOnMousePressed(mouseEvent -> {
            if(!model.getIsToolSelected() || model.getOnMousePressedReaction() == null || !mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                return;
            }
            model.getOnMousePressedReaction().accept(mouseEvent);
        });
        resizableCanvas.setOnMouseReleased(mouseEvent -> {
            if(!model.getIsToolSelected() || model.getOnMouseRealisedReaction() == null || !mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                return;
            }
            model.getOnMouseRealisedReaction().accept(mouseEvent);
        });

        canvasHolder.getChildren().add(resizableCanvas);
        canvasHolder.minWidthProperty().addListener((observableValue, number, t1) -> {
            canvas.setWidth(t1.intValue());
            model.setCanvasSize(t1.intValue(),model.getCanvasHeight());
        });
        canvasHolder.minHeightProperty().addListener((observableValue, number, t1) -> {
            canvas.setHeight(t1.intValue());
            model.setCanvasSize(model.getCanvasWidth(),t1.intValue());
        });
        DragResizerXY.makeResizable(canvasHolder);
        setButtonImage(lineDrawBtn,"line-icon.png",0);
        setButtonImage(fillDrawBtn,"fill-icon.png",9);
        setButtonImage(polygonDrawBtn,"polygon-icon.png",9);
        setButtonImage(starDrawBtn,"star-icon.png", 9);
        setButtonImage(openFileBtn,"open-icon.png", 9);
        setButtonImage(optionsBtn,"options-icon.png",9);
        setButtonImage(chooseColorBtn,"palette-icon.png",9);
        setButtonImage(clearBtn, "eraser-icon.png",4);
        setButtonImage(infoBtn,"info-icon.png",9);

        lineDrawBtn.setOnMouseClicked(mouseEvent -> {
            boolean iWasSelected = lineDrawBtn.isSelected();
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                unselectAllToolButtons();
                selectLineTool(!iWasSelected,lineDrawBtn);
                radioLineDrawBtn.setSelected(iWasSelected);
            }
            if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){
                openLineOptions();
            }
        });
        polygonDrawBtn.setOnMouseClicked(mouseEvent -> {
            boolean iWasSelected = polygonDrawBtn.isSelected();
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                unselectAllToolButtons();
                selectPolygonTool(!iWasSelected,polygonDrawBtn);
                radioPolygonDrawBtn.setSelected(iWasSelected);
            }
            if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){
                openPolygonOptions();
            }
        });
        starDrawBtn.setOnMouseClicked(mouseEvent -> {
            boolean iWasSelected = starDrawBtn.isSelected();
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                unselectAllToolButtons();
                selectStarTool(!iWasSelected,starDrawBtn);
                radioStarDrawBtn.setSelected(iWasSelected);
            }
            if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){
                openStarOptions();
            }
        });
        fillDrawBtn.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                boolean iWasSelected = fillDrawBtn.isSelected();
                unselectAllToolButtons();
                selectFillTool(!iWasSelected, fillDrawBtn);
                radioFillDrawBtn.setSelected(iWasSelected);
            }
        });
        radioLineDrawBtn.setOnAction(actionEvent -> {
            boolean iWasSelected = radioLineDrawBtn.isSelected();
            unselectAllToolButtons();
            selectLineTool(!iWasSelected,radioLineDrawBtn);
            lineDrawBtn.setSelected(iWasSelected);
        });
        radioPolygonDrawBtn.setOnAction(actionEvent -> {
            boolean iWasSelected = radioPolygonDrawBtn.isSelected();
            unselectAllToolButtons();
            selectPolygonTool(!iWasSelected,radioPolygonDrawBtn);
            polygonDrawBtn.setSelected(iWasSelected);
        });
        radioStarDrawBtn.setOnAction(actionEvent -> {
            boolean iWasSelected = radioStarDrawBtn.isSelected();
            unselectAllToolButtons();
            selectStarTool(!iWasSelected,radioStarDrawBtn);
            starDrawBtn.setSelected(iWasSelected);
        });
        radioFillDrawBtn.setOnAction(actionEvent -> {
            boolean iWasSelected = radioFillDrawBtn.isSelected();
            unselectAllToolButtons();
            selectFillTool(!iWasSelected,radioFillDrawBtn);
            fillDrawBtn.setSelected(iWasSelected);
        });
        openFileBtn.setOnMouseClicked(mouseEvent -> openFile());
        menuOpenFile.setOnAction(actionEvent -> openFile());
        menuSaveFile.setOnAction(actionEvent -> saveFile());
        openFileBtn.setTooltip(new Tooltip("Open file"));
        optionsBtn.setTooltip(new Tooltip("Canvas options"));
        lineDrawBtn.setTooltip(new Tooltip("Line tool"));
        starDrawBtn.setTooltip(new Tooltip("Star tool"));
        polygonDrawBtn.setTooltip(new Tooltip("Polygon tool"));
        fillDrawBtn.setTooltip(new Tooltip("Fill tool"));
        chooseColorBtn.setTooltip(new Tooltip("Palette"));
        clearBtn.setTooltip(new Tooltip("Clear canvas"));
        infoBtn.setTooltip(new Tooltip("Tools info"));
        infoBtn.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                openToolsInfo();
            }
        });
        menuAbout.setOnAction(actionEvent -> openInfo());
        optionsBtn.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                openCanvasOptions();
            }
        });
    }

    private void unselectAllToolButtons(){
        lineDrawBtn.setSelected(false);
        fillDrawBtn.setSelected(false);
        starDrawBtn.setSelected(false);
        polygonDrawBtn.setSelected(false);
        radioFillDrawBtn.setSelected(false);
        radioLineDrawBtn.setSelected(false);
        radioStarDrawBtn.setSelected(false);
        radioPolygonDrawBtn.setSelected(false);
    }

    private void openLineOptions(){
        Stage newStage = new Stage();
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(mainScrollPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindowController.class.getResource("line-options.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 260, 80);
            newStage.setTitle("line options");
            newStage.setScene(scene);
            newStage.setResizable(false);
            LineOptionsController controller = fxmlLoader.getController();
            controller.setModel(model);
            controller.setStage(newStage);
            controller.init();
            newStage.showAndWait();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void openPolygonOptions(){
        Stage newStage = new Stage();
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(mainScrollPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindowController.class.getResource("polygon-options.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 260, 160);
            newStage.setTitle("polygon options");
            newStage.setScene(scene);
            newStage.setResizable(false);
            PolygonWindowController controller = fxmlLoader.getController();
            controller.setModel(model);
            controller.setStage(newStage);
            controller.init();
            newStage.showAndWait();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void openStarOptions(){
        Stage newStage = new Stage();
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(mainScrollPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindowController.class.getResource("star-options.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 260, 160);
            newStage.setTitle("star options");
            newStage.setScene(scene);
            newStage.setResizable(false);
            StarOptionsController controller = fxmlLoader.getController();
            controller.setModel(model);
            controller.setStage(newStage);
            controller.init();
            newStage.showAndWait();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
    private void selectLineTool(boolean iWasSelected, Toggle btn){
        if(iWasSelected){
            model.unSelectTool();
            DragResizerXY.allowResize();
        } else{
            btn.setSelected(true);
            DragResizerXY.banResize();
            model.selectLineTool();
        }
    }
    private void selectFillTool(boolean iWasSelected, Toggle btn){
        if(iWasSelected){
            model.unSelectTool();
            DragResizerXY.allowResize();
        } else{
            btn.setSelected(true);
            model.selectFillTool();
            DragResizerXY.banResize();
        }
    }
    private void selectStarTool(boolean iWasSelected, Toggle btn){
        if(iWasSelected){
            model.unSelectTool();
            DragResizerXY.allowResize();
        } else{
            btn.setSelected(true);
            model.selectStarTool();
            DragResizerXY.banResize();
        }
    }

    private void selectPolygonTool(boolean iWasSelected, Toggle btn){
        if(iWasSelected){
            model.unSelectTool();
            DragResizerXY.allowResize();
        } else{
            btn.setSelected(true);
            model.selectPolygonTool();
            DragResizerXY.banResize();
        }
    }

    @FXML
    protected void onRedBtnClick(){
        model.setColor(Color.RED);
    }
    @FXML
    protected void onGreenBtnClick(){
        model.setColor(Color.GREEN);
    }
    @FXML
    protected void onBlueBtnClick(){
        model.setColor(Color.BLUE);
    }
    @FXML
    protected void onBlackBtnClick(){
        model.setColor(Color.BLACK);
    }
    @FXML
    protected void onWhiteBtnClick(){
        model.setColor(Color.WHITE);
    }
    @FXML
    protected void onCyanBtnClick(){
        model.setColor(Color.CYAN);
    }
    @FXML
    protected void onYellowBtnClick(){
        model.setColor(Color.YELLOW);
    }
    @FXML
    protected void onMagentaBtnClick(){
        model.setColor(Color.MAGENTA);
    }
    @FXML
    protected void onPaletteBtnClick(){
        Stage newStage = new Stage();
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(chooseColorBtn.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindowController.class.getResource("palette-window.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 400, 140);
            newStage.setTitle("Palette");
            newStage.setScene(scene);
            newStage.setResizable(false);
            PaletteWindowController controller = fxmlLoader.getController();
            controller.setModel(model);
            controller.setMyStage(newStage);
            newStage.showAndWait();
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    protected void onCleanBtnClick(){
        int[] grid = printer2D.getGridARGB();
        for (int i = 0; i < canvas.getWidth(); i++){
            for (int j = 0; j < canvas.getHeight(); j++){
                grid[j*(int)canvas.getWidth() + i] = -1;
            }
        }
        printer2D.redrawCanvas(grid);
    }

    private void openFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open picture");
        File picture = fileChooser.showOpenDialog(openFileBtn.getScene().getWindow());
        if (picture == null){
            return;
        }
        Image image;
        try {
             image = new Image(new FileInputStream(picture));
        } catch (FileNotFoundException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, "No such file", ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }
        if(image.isError()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unsupportable format", ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }
        model.setCanvasSize((int)image.getWidth(),(int)image.getHeight());
        canvas.setWidth(image.getWidth());
        canvas.setHeight(image.getHeight());
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.drawImage(image,0,0);
    }
    private void saveFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save picture");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        File picture = fileChooser.showSaveDialog(openFileBtn.getScene().getWindow());
        if(picture == null){
            return;
        }
        try{
            if(!picture.createNewFile()){
                throw new IOException();
            }
        } catch (IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error during creating file", ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }
        WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
        canvas.snapshot(null, writableImage);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        try {
            ImageIO.write(renderedImage, "png", picture);
        } catch (IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error during saving file", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    private void addTab(String text,String name, double wrappingWidth, TabPane tabPane){
        Tab lineTab = new Tab(name);
        ScrollPane lineScroll = new ScrollPane();
        Text txt = new Text(text);
        txt.setFont(new Font("Times New Roman",16));
        lineScroll.setPadding(new Insets(5,5,5,5));
        txt.setWrappingWidth(wrappingWidth);
        lineScroll.setContent(txt);
        lineTab.setContent(lineScroll);
        lineTab.setClosable(false);
        tabPane.getTabs().add(lineTab);
    }
    private void openToolsInfo(){
        Stage window = new Stage();
        window.initModality(Modality.WINDOW_MODAL);
        window.initOwner(mainScrollPane.getScene().getWindow());
        TabPane tabPane = new TabPane();
        addTab("""
                Инструмент для рисования линий

                Чтобы рисовать - зажмите ЛКМ на холсте, проведите до конца линии и отожмите кнопку

                Нажмите ПКМ для открытия окна настроек

                Настройки:
                Ширина линии - задает ширину нарисованной линии""","Line",290,tabPane);

        addTab("""
                Инструмент для заливки

                Чтобы сделать заливку - нажмите ЛКМ на холсте

                Цвет заливки - это текущий выбранный цвет""","Fill",290,tabPane);

        addTab("""
                Инструмент для рисования многоугольников

                Чтобы нарисовать многоугольник - нажмите ЛКМ на холсте

                Чтобы открыть окно настроек нажмите ПКМ\s

                Настройки:
                Ширина - задаёт ширину линий многоугольника
                Радиус - задаёт расстояние от места клика до вершнин
                Количество - количество вершин многоугольника
                Угол - угол, под которым относительно горизонтали расположена правая точка многоугольника""", "Polygon", 290, tabPane);

        addTab("""
                Инструмент для рисования звезд

                Чтобы нарисовать звезду - нажмите ЛКМ на хосте

                Чтобы открыть окно настроек нажмите ПКМ

                Настройки:
                Ширина - задаёт ширину линий звезды
                Радиус - задаёт расстояние от места клика до дальних вершин звезды
                Количество - количество внешних вершин звезды (равно так же и внутреннему)
                Угол - угол, под которым относительно горизонтали расположена правая точка звезды""", "Star", 290, tabPane);

        addTab("""
                Палитра для выбора цвета

                Чтобы откыть окно для выбора цвета нажмите ЛКМ

                Настройки:
                Red - интенсивность красного цвета
                Green - интенсивность зеленого цвета
                Blue - интенсивность синего цвета""","Palette",290,tabPane);

        Scene scene = new Scene(tabPane,300,400);
        window.setScene(scene);
        window.setResizable(false);
        window.setTitle("Info");
        window.showAndWait();
    }
    private void openInfo(){
        Stage newStage = new Stage();
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(mainScrollPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindowController.class.getResource("about-window.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 250, 300);
            newStage.setTitle("about");
            newStage.setScene(scene);
            newStage.setResizable(false);
            newStage.showAndWait();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void openCanvasOptions(){
        Stage newStage = new Stage();
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(mainScrollPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindowController.class.getResource("canvas-options.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 160, 100);
            newStage.setTitle("Canvas options");
            newStage.setScene(scene);
            newStage.setResizable(false);
            CanvasOptionsController controller = fxmlLoader.getController();
            controller.setModel(model);
            controller.setStage(newStage);
            controller.init();
            newStage.showAndWait();
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void onCanvasResize() {
        canvasHolder.setPrefWidth(model.getCanvasWidth());
        canvasHolder.setMinWidth(model.getCanvasWidth());
        canvasHolder.setPrefHeight(model.getCanvasHeight());
        canvasHolder.setMinHeight(model.getCanvasHeight());
        canvas.setHeight(model.getCanvasHeight());
        canvas.setWidth(model.getCanvasWidth());
    }
}

