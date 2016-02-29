import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;
public class Line implements LineInterface
{    
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private Shape shape1;
    private Shape shape2;
    double[] shape1pos;
    double[] shape2pos;
    String text = "";
    /**
     * Constructor for objects of class Line
     */
    public Line(Shape s1, Shape s2, String t)
    {
        shape1 = s1;
        shape2 = s2;
        text = t;
        shape1pos = s1.getPosition();
        shape2pos = s2.getPosition();
        setLine();
        draw();
    }

    /**
     * set the orientation of the line
     */
    public void setLine(){
        x1 = findCentre(shape1pos)[0];
        y1 = findCentre(shape1pos)[1];
        x2 = findCentre(shape2pos)[0];
        y2 = findCentre(shape2pos)[1];
    }

    /**
     * draws the line object
     */
    public void draw(){
        UI.drawLine(x1, y1, x2, y2);
        double xLow;
        double yLow;
        double xHigh;
        double yHigh;
        //find the lowest coordinates [int terms of x axis] and place them accordingly
        if(x1 < x2){
            xLow = x1;
            yLow = y1;
            xHigh = x2;
            yHigh = y2;
        }else{
            xLow = x2;
            yLow = y2;
            xHigh = x1;
            yHigh = y1;
        }
        UI.drawString(text, xLow + (xHigh-xLow) /2, yLow + (yHigh-yLow)/2); 
    }

    /**
     * find centre of shape it is connected to
     */
    private double[] findCentre (double[] coord){
        double[] arrCent = new double[2];
        arrCent[0] = coord[0] + coord[2]/2;
        arrCent[1] = coord[1] + coord[3]/2;
        return arrCent;
    }

    /**
     * checks the shape that moves with the other shape that the
     * line is attached to. Returns the shape which is not the one
     * that moves
     */
    public Shape otherShape(Shape oldSelectedShape){
        if(oldSelectedShape.equals(shape1)){
            return shape2;
        }
        else{
            return shape1;
        }
    }

    /**
     * checks the shapes on the line if one of them equals the
     * one being checked
     */
    public boolean hasShape(Shape checkShape){
        if (checkShape.equals(shape1) || checkShape.equals(shape2)){
            return true;
        }
        return false;
    }

    /**
     * checks if the click is close enough to the line to warrant it
     * being selected
     */
    public boolean checkPos(double xPos, double yPos){
        double xLow;
        double yLow;
        double xHigh;
        double yHigh;
        //find the lowest coordinates [int terms of x axis] and place them accordingly
        if(x1 < x2){
            xLow = x1;
            yLow = y1;
            xHigh = x2;
            yHigh = y2;
        }else{
            xLow = x2;
            yLow = y2;
            xHigh = x1;
            yHigh = y1;
        }
        double gradient = (yHigh - yLow) / (xHigh - xLow);
        //expected line x and y coordinates at the click coords
        double yExpect = gradient*(xPos - xLow) + yLow;
        double xExpect = (gradient * xLow + yPos - yLow) / gradient;
        //if between the 2 points of coordinates
        if(isBetween(x1,x2,xPos) && isBetween(y1,y2,yPos)){
            //if the clicked xy is within the range of the expected
            //x and y points at that point of the line
            if(isBetween(yExpect - 10, yExpect + 10, yPos) ||
            isBetween(xExpect - 10, xExpect + 10, xPos)){
                return true;
            }
        }
        return false;
    }

    private boolean isBetween(double vLimit1, double vLimit2, double value){
        if (vLimit1 > vLimit2){//if 1 less than 2, swap the values
            double temp = vLimit1;
            vLimit1 = vLimit2;
            vLimit2 = temp;
        }
        if(value < vLimit1 || value > vLimit2){
            return false;
        }
        return true;
    }

    /**
     * takes all of the relevant variables of the line and converts it
     * into string format for saving into a file.
     */
    public String toString(){
        String s = "Line " + text + " \" " + shape1.toString() + " " + shape2.toString();
        return s;
    }

    /**
     * returns the line type
     */
    public String lineType(){
        return "Line";
    }
    /**
     * returns the text variable
     */
    public String getString(){
        return text;
    }
    /**
     * adjusts the text on the line
     */
    public void adjustText(String value){
        text = value;
    }
}
