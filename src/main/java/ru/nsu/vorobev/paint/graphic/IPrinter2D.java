package ru.nsu.vorobev.paint.graphic;

import java.awt.Color;

public interface IPrinter2D {
    void printPoint(int x, int y, Color color);
    void printLine(int x1, int y1, int x2, int y2, int width, Color color);
    int[] getGridARGB();
    void redrawCanvas(int[] pixels);
}
