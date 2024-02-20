package ru.nsu.vorobev.paint.graphic.methods2D;

import java.awt.Color;
import ru.nsu.vorobev.paint.graphic.IPrinter2D;

public class LineDrawer{
    public static void drawSimpleLine(IPrinter2D printer2D, int x1, int y1, int x2, int y2, Color color){
        int dx = x2-x1;
        int dy = y2-y1;
        int x = x1;
        int y = y1;
        int xWalker = dx != 0 ? (dx > 0 ? 1 : -1) : 0;
        int yWalker = dy != 0 ? (dy > 0 ? 1 : -1) : 0;
        dx = Math.abs(dx);
        dy = Math.abs(dy);
        int d2x = dx << 1;
        int d2y = dy << 1;
        printer2D.printPoint(x1,y1,color);
        printer2D.printPoint(x2,y2,color);
        if(Math.abs(dx) >= Math.abs(dy)) {
            int err = x1-x2;
            for (int i = 0; i < dx; i++) {
                x+= xWalker;
                err += d2y;
                if (err > 0) {
                    err -= d2x;
                    y+= yWalker;
                }
                printer2D.printPoint(x, y, color);
            }
        } else{
            int err = y1-y2;
            for (int i = 0; i < dy; i++) {
                y+= yWalker;
                err += d2x;
                if (err > 0) {
                    err -= d2y;
                    x+= xWalker;
                }
                printer2D.printPoint(x, y, color);
            }
        }
    }
}
