package ru.nsu.vorobev.paint.graphic.methods2D;

import ru.nsu.vorobev.paint.graphic.IPrinter2D;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.Stack;

public class FillDrawer {

    private static class Pixel{
        private int x;
        private int y;
        public Pixel(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
    public static void fill(IPrinter2D printer2D, int width, int height, int x, int y, Color color){
        long start = System.currentTimeMillis();
        int[] grid = printer2D.getGridARGB();
        int oldColor = grid[y*width + x];
        byte[] argb = new byte[4];
        argb[3] = (byte) color.getBlue();
        argb[2] = (byte)color.getGreen();
        argb[1] = (byte)color.getRed();
        argb[0] = (byte)255;
        int newColor = ByteBuffer.wrap(argb).getInt();
        if(oldColor == newColor){
            return;
        }
        Stack<Pixel> pixels = new Stack<>();
        pixels.push(new Pixel(x,y));
        while(!pixels.isEmpty()){
            Pixel pixel = pixels.pop();
            int lx = pixel.getX();
            while (checkBorders(lx,pixel.getY(), width,height,oldColor,grid)){
                grid[pixel.getY()*width + lx] = newColor;
                lx-=1;
            }
            int rx = pixel.getX() + 1;
            while (checkBorders(rx,pixel.getY(), width,height,oldColor,grid)){
                grid[pixel.getY()*width + rx] = newColor;
                rx+=1;
            }
            rx--;
            lx++;
            findNewSpans(lx,rx, pixel.getY()+1,oldColor,grid,pixels,width,height);
            findNewSpans(lx,rx,pixel.getY()-1,oldColor,grid,pixels,width,height);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        printer2D.redrawCanvas(grid);
    }

    private static boolean checkBorders(int x, int y, int width, int height, int oldColor, int[] grid){
        return  x >= 0 && y >= 0 && x < width && y < height && oldColor == grid[y*width + x];
    }
    private static void findNewSpans(int lx, int rx, int y, int oldColor, int[] grid, Stack<Pixel> pixels, int width, int height){
        boolean isSpan = false;
        for (int i = lx; i < rx; i++){
            if(checkBorders(i,y,width,height,oldColor,grid)){
                if(!isSpan) {
                    pixels.push(new Pixel(i, y));
                    isSpan = true;
                }
            } else if(isSpan){
                isSpan = false;
            }
        }
    }
}
