import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;

public class Hexagon implements Shape
{
    private double x;
    private double y;
    private double width;
    private double height;
    private String text = "";
    /**
     * rectangle constructor
     */
    public Hexagon(double x1, double y1, double x2, double y2, String s)
    {
        text = s;
        x = x1;
        y = y1;
        width = x2 - x1;
        height = y2 - y1;
        draw();
    }

    /**
     * draws the rectangle on the screen
     */
    public void draw(){
        double[] xPoints = new double[]{x, x + width/4, x + 3*width/4, x+ width, x + 3*width/4, x + width/4};
        double[] yPoints = new double[]{y + height/2, y + 0, y + 0, y + height/2, y + height, y + height};
        UI.erasePolygon(xPoints, yPoints, 6);
        UI.drawPolygon(xPoints, yPoints, 6);
        UI.drawString(text, x + 3, y + 15);
    }

    /**
     * returns the position of the shape using a method
     */
    public double[] getPosition(){
        double[] array = new double[4];
        array[0] = x;
        array[1] = y;
        array[2] = width;
        array[3] = height;
        return array;
    }

    /**
     * a boolean that checks if the click is in the area of the
     * rectangle.
     */
    public boolean checkPos(double xPos, double yPos){
        if (xPos > x && xPos < x + width && yPos > y && yPos < y + height){ //within bounds of the shape
            return true;
        }
        return false;
    }

    /**
     * moves the rectangle to a new destination
     */
    public void move(double xPos, double yPos){
        x = xPos - width/2;
        y = yPos - height/2;
    }

    /**
     * adjusts the height of the shape
     */
    public void adjustHeight(double value){
        height = value;
    }

    /**
     * adjusts the width of the shape
     */
    public void adjustWidth(double value){
        width = value;
    }

    /**
     * adjusts the text on the shape
     */
    public void adjustText(String value){
        text = value;
    }

    /**
     * takes all of the relevant variables of the shape and converts it
     * into string format for saving into a file.
     */
    public String toString(){
        String s = "Hexagon " + String.valueOf(x) + " " + String.valueOf(y) + " " + 
            String.valueOf(width+x) + " " + String.valueOf(height+y) + " " + text + " \"";
        return s;
    }
}
