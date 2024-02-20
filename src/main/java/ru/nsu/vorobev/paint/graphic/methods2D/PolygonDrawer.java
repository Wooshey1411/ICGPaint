package ru.nsu.vorobev.paint.graphic.methods2D;

import ru.nsu.vorobev.paint.graphic.IPrinter2D;

import java.awt.*;

public class PolygonDrawer{
    public static void drawPolygon(IPrinter2D printer2D, int x, int y, int radius, double angleInDeg, int width, int countOfPoints, Color color) {
        double step = 360.0 / countOfPoints;
        int cx = x + (int)(radius * (Math.cos(Math.toRadians(-angleInDeg))));
        int cy = y + (int)(radius * (Math.sin(Math.toRadians(-angleInDeg))));
        for (int i = 1; i <= countOfPoints; i++){
            double angle = -angleInDeg + i*step;
            int nx = x + (int)(radius * (Math.cos(Math.toRadians(angle))));
            int ny = y + (int)(radius * (Math.sin(Math.toRadians(angle))));
            if(width == 1) {
                LineDrawer.drawSimpleLine(printer2D,cx,cy,nx,ny,color);
            } else{
                printer2D.printLine(cx,cy,nx,ny,width,color);
            }
            cx = nx;
            cy = ny;
        }
    }
}
