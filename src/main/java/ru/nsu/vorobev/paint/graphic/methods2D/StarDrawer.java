package ru.nsu.vorobev.paint.graphic.methods2D;

import ru.nsu.vorobev.paint.graphic.IPrinter2D;

import java.awt.*;

public class StarDrawer {
    public static void drawStar(IPrinter2D printer2D, int x, int y, int internalRadius, int externalRadius, int width, int countOfPoints, double angleInDeg, Color color) {
        double step = 360.0 / countOfPoints;
        double internalOffset = step / 2;

        int cx = x + (int)(externalRadius * Math.cos(Math.toRadians(-angleInDeg)));
        int cy = y + (int)(externalRadius * Math.sin(Math.toRadians(-angleInDeg)));
        for (int i = 1; i <= countOfPoints*2; i++){
            int nx;
            int ny;
            if(i % 2 == 1){
                double angle = internalOffset - angleInDeg + (i/2) * step;
                nx = x + (int)(internalRadius * Math.cos(Math.toRadians(angle)));
                ny = y + (int)(internalRadius * Math.sin(Math.toRadians(angle)));
            } else{
                double angle = - angleInDeg + (i/2) * step;
                nx = x + (int)(externalRadius * Math.cos(Math.toRadians( angle)));
                ny = y + (int)(externalRadius * Math.sin(Math.toRadians( angle)));
            }
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
