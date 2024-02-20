package ru.nsu.vorobev.paint;

import javafx.scene.input.MouseEvent;
import ru.nsu.vorobev.paint.graphic.IPrinter2D;
import ru.nsu.vorobev.paint.graphic.methods2D.FillDrawer;
import ru.nsu.vorobev.paint.graphic.methods2D.LineDrawer;
import ru.nsu.vorobev.paint.graphic.methods2D.PolygonDrawer;
import ru.nsu.vorobev.paint.graphic.methods2D.StarDrawer;
import ru.nsu.vorobev.paint.gui.ModelListener;

import java.awt.*;
import java.util.function.Consumer;

public class Model {
    private Color color = Color.RED;
    private boolean isToolSelected = false;
    private Consumer<MouseEvent> onMouseClickReaction;
    private Consumer<MouseEvent> onMousePressedReaction;
    private Consumer<MouseEvent> onMouseRealisedReaction;
    private Consumer<MouseEvent> onMouseDraggedReaction;
    private ModelListener modelListener;
    private int lineWidth = 1;
    private int starWidth = 1;
    private int polygonWidth = 1;
    private int canvasWidth = 600;
    private int canvasHeight = 400;
    private int polygonRadius = 100;
    private int starInternalRadius = 60;
    private int starExternalRadius = 100;
    private int starCountOfPoints = 6;
    private int polygonCountOfPoints = 7;
    private int starAngle = 0;
    private int polygonAngle = 0;
    private int lineX1;
    private int lineY1;

    private IPrinter2D printer2D;

    public void unSelectTool(){
        isToolSelected = false;
    }
    public boolean getIsToolSelected(){
        return isToolSelected;
    }

    public void setPrinter2D(IPrinter2D printer2D) {
        this.printer2D = printer2D;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setStarWidth(int starWidth) {
        this.starWidth = starWidth;
    }

    public void setPolygonWidth(int polygonWidth) {
        this.polygonWidth = polygonWidth;
    }

    public void setCanvasSize(int width, int height){
        canvasWidth = width;
        canvasHeight = height;
        modelListener.onCanvasResize();
    }

    public void setPolygonRadius(int polygonRadius) {
        this.polygonRadius = polygonRadius;
    }

    public void setStarInternalRadius(int starInternalRadius) {
        this.starInternalRadius = starInternalRadius;
    }

    public void setStarExternalRadius(int starExternalRadius) {
        this.starExternalRadius = starExternalRadius;
    }

    public void setStarCountOfPoints(int starCountOfPoints) {
        this.starCountOfPoints = starCountOfPoints;
    }

    public void setPolygonCountOfPoints(int polygonCountOfPoints) {
        this.polygonCountOfPoints = polygonCountOfPoints;
    }

    public void setStarAngle(int starAngle) {
        this.starAngle = starAngle;
    }

    public void setPolygonAngle(int polygonAngle) {
        this.polygonAngle = polygonAngle;
    }

    public Consumer<MouseEvent> getOnMouseClickReaction() {
        return onMouseClickReaction;
    }

    public Consumer<MouseEvent> getOnMousePressedReaction() {
        return onMousePressedReaction;
    }

    public Consumer<MouseEvent> getOnMouseRealisedReaction() {
        return onMouseRealisedReaction;
    }

    public Consumer<MouseEvent> getOnMouseDraggedReaction() {
        return onMouseDraggedReaction;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public int getStarWidth() {
        return starWidth;
    }

    public int getPolygonWidth() {
        return polygonWidth;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public int getPolygonRadius() {
        return polygonRadius;
    }

    public int getStarInternalRadius() {
        return starInternalRadius;
    }

    public int getStarExternalRadius() {
        return starExternalRadius;
    }

    public int getStarCountOfPoints() {
        return starCountOfPoints;
    }

    public int getPolygonCountOfPoints() {
        return polygonCountOfPoints;
    }

    public int getStarAngle() {
        return starAngle;
    }

    public int getPolygonAngle() {
        return polygonAngle;
    }

    private void resetMouseReactors(){
        onMousePressedReaction = null;
        onMouseDraggedReaction = null;
        onMouseClickReaction = null;
        onMouseRealisedReaction = null;
    }
    public void selectLineTool(){
        resetMouseReactors();
        onMouseRealisedReaction = mouseEvent -> {
            if(lineWidth == 1){
                LineDrawer.drawSimpleLine(printer2D,lineX1,lineY1,(int)mouseEvent.getX(), (int)mouseEvent.getY(),color);
            } else{
                printer2D.printLine(lineX1,lineY1,(int)mouseEvent.getX(), (int)mouseEvent.getY(), lineWidth, color);
            }
        };
        onMousePressedReaction = mouseEvent -> {
            lineX1 = (int)mouseEvent.getX();
            lineY1 = (int)mouseEvent.getY();
        };
        isToolSelected = true;
    }
    public void selectFillTool(){
        resetMouseReactors();
        onMouseClickReaction = mouseEvent -> FillDrawer.fill(printer2D,canvasWidth,canvasHeight,(int)mouseEvent.getX(),(int)mouseEvent.getY(),color);
        isToolSelected = true;
    }
    public void selectStarTool(){
        resetMouseReactors();
        onMouseClickReaction = mouseEvent -> StarDrawer.drawStar(printer2D,(int)mouseEvent.getX(),(int)mouseEvent.getY(),starInternalRadius,starExternalRadius,
                starWidth,starCountOfPoints,starAngle,color);
        isToolSelected = true;
    }
    public void selectPolygonTool(){
        resetMouseReactors();
        onMouseClickReaction = mouseEvent -> PolygonDrawer.drawPolygon(printer2D,(int)mouseEvent.getX(),(int)mouseEvent.getY(),polygonRadius,polygonAngle,polygonWidth,polygonCountOfPoints,color);
        isToolSelected = true;
    }
    public void setModelListener(ModelListener modelListener){
        this.modelListener = modelListener;
    }
}
