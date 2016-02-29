import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;
/**
 * Will be the main class where all of the drawing will occur and is done.
 */
public class DiagramEditor
{
    private double xFirst; //first x coord in drawing shape
    private double yFirst; //first y coord in drawing shape
    private double xLast; //second x coord in drawing shape
    private double yLast; //second y coord in drawing shape
    private String shapeDraw = "Rectangle"; //set shape to be drawn
    private String lineDraw = "Line"; //set line to be drawn
    private String doAction = "";
    private boolean doDraw = false;
    private boolean doSelect = false;
    private boolean doMove = false;
    private ArrayList<Shape> shape = new ArrayList<Shape>();
    private Shape selectedShape = null;

    private Shape lineToShape1 = null;
    private Shape lineToShape2 = null;

    private ArrayList<LineInterface> line = new ArrayList<LineInterface>();
    private LineInterface selectedLine = null;
    /**
     * Constructor the Diagram Editor - making
     */
    public DiagramEditor()
    {
        UI.addButton("New Diagram", this::newDiagram);
        UI.addButton("Save Diagram", this::saveDiagram);
        UI.addButton("Load Diagram", this::loadDiagram);
        UI.addButton("Draw Rectangle", this::chooseRectangle);
        UI.addButton("Draw Oval", this::chooseOval);
        UI.addButton("Draw Hexagon", this::chooseHexagon);
        UI.addButton("Draw Line", this::chooseLine);
        UI.addButton("Draw ZigZag Line", this::chooseZigZagLine);
        UI.addButton("Select Mode", this::chooseSelect);
        UI.addButton("Move Mode", this::chooseMove);
        UI.addButton("Delete Selected Item", this::delete);
        UI.addSlider("Adjust Height (in px)", 1, 500, 100, this::changeHeight);
        UI.addSlider("Adjust Width (in px)", 1, 500, 100, this::changeWidth);
        UI.addTextField("Adjust Text", this::changeText);
        UI.setMouseListener(this::doMouse);
    }

    /**
     * make a new diagram, resets all of the arraylists and variables
     */
    public void newDiagram(){
        doDraw = false;
        doSelect = false;
        doMove = false;
        shape = new ArrayList<Shape>();
        selectedShape = null;
        lineToShape1 = null;
        lineToShape2 = null;
        line = new ArrayList<LineInterface>();
        selectedLine = null;
        redraw();
    }

    /**
     * saves the diagram onto a file, using the PrintStream class
     */
    public void saveDiagram(){
        if (selectedShape != null){ //unselects the previous shape
            shape.add(selectedShape);
            selectedShape = null;
        }
        if (selectedLine != null){ //enselects the previous line
            line.add(selectedLine);
            selectedLine = null;
        }
        String fname = UIFileChooser.save();
        try{
            PrintStream st = new PrintStream(fname);
            for(int i = 0; i < shape.size(); i++){
                if(shape.get(i)!=null){
                    st.println(shape.get(i).toString());
                }
            }
            st.println("AssortedLines");
            for(int i = 0; i < line.size(); i++){
                if(line.get(i)!=null){
                    st.println(line.get(i).toString());
                }
            }
            st.close();
            UI.printMessage("Saved Game");
        }catch (Exception e){
            System.out.println("Failed to save file " + e);
        }
        redraw();
    }

    /**
     * loads a new diagram onto the diagram editor
     */
    public void loadDiagram(){
        String fname = UIFileChooser.open();
        newDiagram(); //resets the diagram editor

        boolean shapeVarLoad = true;
        boolean textLoad = false;
        boolean lineNameLoad = false;
        boolean lineLoad = false;
        boolean lineLoadText = false;

        String shapeName = null;
        Double x1 = null;
        Double y1 = null;
        Double x2 = null;
        Double y2 = null;
        String text = null;
        String lineName = null;
        String lineText = null;

        Shape shape1 = null;
        Shape shape2 = null;

        String scannedValue = null;

        try{
            Scanner scan = new Scanner(new File(fname));
            while(scan.hasNext()){
                scannedValue = scan.next();
                if(scannedValue.equals("AssortedLines")){
                    lineLoad = true;
                    lineNameLoad = true;
                }
                else if(lineNameLoad){
                    lineName = scannedValue;
                    lineNameLoad = false;
                    lineLoadText = true;
                }
                else if(shapeVarLoad && !textLoad && !lineLoadText){
                    shapeName = scannedValue;
                    x1 = Double.valueOf(scan.next());
                    y1 = Double.valueOf(scan.next());
                    x2 = Double.valueOf(scan.next());
                    y2 = Double.valueOf(scan.next());
                    shapeVarLoad = false;
                    textLoad = true;
                }
                else if(textLoad && scannedValue != null){
                    if(text == null){
                        text = "";
                    }
                    if (scannedValue.equals("\"")){ //identifies end of textfield
                        textLoad = false;
                        shapeVarLoad = true;
                    }else{
                        text = text + scannedValue + " ";
                    }
                }
                else if(lineLoadText && scannedValue != null){
                    if (scannedValue.equals("\"")){ //identifies end of textfield
                        lineLoadText = false;
                        shapeVarLoad = true;
                    }else{
                        lineText = lineText + scannedValue + " ";
                    }
                }
                if(shapeName != null && x1 != null && y1 != null
                && x2 != null && y2 != null && text!=null && !textLoad && !lineLoad){
                    if(shapeName.equals("Rectangle")){
                        shape.add(new Rectangle(x1,y1,x2,y2,text));
                    }else if(shapeName.equals("Oval")){
                        shape.add(new Oval(x1,y1,x2,y2,text));
                    }else if(shapeName.equals("Hexagon")){
                        shape.add(new Hexagon(x1,y1,x2,y2,text));
                    }
                    shapeName = null;
                    x1 = null;
                    y1 = null;
                    x2 = null;
                    y2 = null;
                    text = null;
                    shapeVarLoad = true;
                }
                if(shapeName != null && x1 != null && y1 != null
                && x2 != null && y2 != null && text!=null && !textLoad && lineLoad){
                    if(shape1 == null){
                        if(shapeName.equals("Rectangle")){
                            shape1 = new Rectangle(x1,y1,x2,y2,text);
                        }else if(shapeName.equals("Oval")){
                            shape1 = new Oval(x1,y1,x2,y2,text);
                        }else if(shapeName.equals("Hexagon")){
                            shape1 = new Hexagon(x1,y1,x2,y2,text);
                        } 
                    }else{
                        if(shapeName.equals("Rectangle")){
                            shape2 = new Rectangle(x1,y1,x2,y2,text);
                        }else if(shapeName.equals("Oval")){
                            shape2 = new Oval(x1,y1,x2,y2,text);
                        }else if(shapeName.equals("Hexagon")){
                            shape2 = new Hexagon(x1,y1,x2,y2,text);
                        } 
                        if(lineName.equals("Line")){
                            line.add(new Line(shape1, shape2, lineText));
                        }else if (lineName.equals("ZigZagLine")){
                            line.add(new ZigZagLine(shape1, shape2, lineText));
                        }
                        shape1 = null;
                        shape2 = null;
                        lineNameLoad = true;
                    }
                    shapeName = null;
                    x1 = null;
                    y1 = null;
                    x2 = null;
                    y2 = null;
                    text = null;
                    shapeVarLoad = true;
                }
            }
            scan.close();
            redraw();
            UI.printMessage("Loaded Diagram"); 
        } catch(IOException e){
            System.out.println("File reading failed " + e);
        }
    }

    /**
     * choose the oval to be drawn
     */
    public void chooseOval(){
        doAction = "Draw";
        shapeDraw = "Oval";
    }

    /**
     * choose the rectangle to be drawn
     */
    public void chooseRectangle(){
        doAction = "Draw";
        shapeDraw = "Rectangle";
    }

    /**
     * choose the hexagon to be drawn
     */
    public void chooseHexagon(){
        doAction = "Draw";
        shapeDraw = "Hexagon";
    }

    /**
     * choose the line to be drawn
     */
    public void chooseLine(){
        doAction = "LineDraw";
        lineDraw = "Line";
    }

    /**
     * choose the zigzag line to be drawn
     */
    public void chooseZigZagLine(){
        doAction = "LineDraw";
        lineDraw = "ZigZagLine";
    }

    /**
     * chooses the select action
     */
    public void chooseSelect(){
        doAction = "Select";
    }

    /**
     * chooses the move action
     */
    public void chooseMove(){
        doAction = "Move";
    }

    /**
     * deletes the selectedShape instance
     */
    public void delete(){
        Shape oldSelectedShape = selectedShape;
        selectedShape = null;
        LineInterface oldSelectedLine = selectedLine;
        selectedLine = null;
        if(oldSelectedShape != null){
            if(checkShapesOnLine(oldSelectedShape) != null){
                //returns the other shape attached to the same line as the selectedShape
                ArrayList<Integer> lineIndex = checkShapesOnLine(oldSelectedShape);
                for(int i = 0; i < lineIndex.size(); i++){
                    Integer lineShareShapes = lineIndex.get(i);
                    line.set(lineShareShapes ,null);
                    line.remove(lineShareShapes);
                }
            }
        }
        redraw();
    }

    /**
     * changes the height of the selected shape
     */
    public void changeHeight(double value){
        if (selectedShape!=null){
            Shape oldSelectedShape = selectedShape;
            selectedShape.adjustHeight(value);
            resetLine(oldSelectedShape);
            redraw();
        }
    }

    /**
     * changes the width of the selected shape
     */
    public void changeWidth(double value){
        if (selectedShape!=null){
            Shape oldSelectedShape = selectedShape;
            selectedShape.adjustWidth(value);
            resetLine(oldSelectedShape);
            redraw();
        }
    }

    /**
     * changes the text field for the shape
     */
    public void changeText(String value){
        if (selectedShape!=null){
            selectedShape.adjustText(value);
            redraw();
        }
        if (selectedLine!=null){
            selectedLine.adjustText(value);
            redraw();
        }
    }

    /**
     * sets up the mouse listener to record the x and y coordinates of the
     * press and release of a drag.
     */
    public void doMouse(String action, double x, double y){
        if (action == "pressed"){//record first x y coords
            xFirst = x;
            yFirst = y;
        }
        if (action == "released"){//record second x y coords
            xLast = x;
            yLast = y;
            if (xLast < xFirst || yLast < yFirst){
                xLast = xFirst;
                xFirst = x;
                yLast = yFirst;
                yFirst = y;
            }
            if (doAction == "Draw"){//if it is set to draw, create new shape
                if(shapeDraw == "Rectangle"){
                    shape.add(new Rectangle(xFirst, yFirst, xLast, yLast, ""));
                    redraw();
                }
                if(shapeDraw == "Oval"){
                    shape.add(new Oval(xFirst, yFirst, xLast, yLast, ""));
                    redraw();
                }
                if(shapeDraw == "Hexagon"){
                    shape.add(new Hexagon(xFirst, yFirst, xLast, yLast, ""));
                    redraw();
                }
            }
            if(doAction == "Select"){
                selectItem(x,y);
            }
            if(doAction == "Move"){
                if (selectedShape != null){ 
                    Shape oldSelectedShape = selectedShape;
                    selectedShape.move(x, y);
                    resetLine(oldSelectedShape);
                    redraw();
                }
            }
            if(doAction == "LineDraw"){
                if (selectedShape != null){ //unselects the previous shape
                    shape.add(selectedShape);
                    selectedShape = null;
                    redraw();
                }
                if (selectedLine != null){
                    line.add(selectedLine);
                    selectedLine = null;
                    redraw();
                }
                if(checkShape(x,y) != null){
                    if(lineToShape1 != null){ //if first shape already chosen
                        lineToShape2 = shape.get(checkShape(x,y));
                    }else{
                        lineToShape1 = shape.get(checkShape(x,y));
                    }
                }
                if(lineToShape1 != null && lineToShape2 != null){
                    // deletes the previous line and draws a new one
                    if(findLineWithBothShapes(lineToShape1, lineToShape2) != null){
                        int deleteLineIndex = findLineWithBothShapes(lineToShape1, lineToShape2);
                        line.set(deleteLineIndex ,null);
                        line.remove(deleteLineIndex);
                    }
                    UI.setColor(Color.black);
                    if(lineDraw == "Line"){
                        line.add(new Line(lineToShape1, lineToShape2, ""));//makes new line
                    }
                    if(lineDraw == "ZigZagLine"){
                        line.add(new ZigZagLine(lineToShape1, lineToShape2, ""));//makes new line
                    }
                    redraw();
                    lineToShape1 = null; //reset back to no shape made for line
                    lineToShape2 = null;
                }
            }
        }
    }

    /**
     * redraws all of the object on the screen, updates the screen
     */
    public void redraw(){
        UI.clearGraphics();
        UI.setColor(Color.black);
        for (int i = 0; i < line.size(); i++){
            if(line.get(i) != null){
                line.get(i).draw();
            }
        }
        if(selectedLine != null){
            UI.setColor(Color.blue);
            selectedLine.draw();
        }
        for (int i = 0; i < shape.size(); i++){
            UI.setColor(Color.black);
            shape.get(i).draw();
        }
        if(selectedShape != null){
            UI.setColor(Color.blue);
            selectedShape.draw();
        }
    }

    /**
     * check for all the shapes if they are at the coordinate that
     * describes their position. Returns the index of the first one in the arraylist
     * that satisfies that condition. Else it returns null.
     */
    public Integer checkShape(double xPos, double yPos){
        for (int i = 0; i < shape.size(); i++){
            if (shape.get(i) != null){//if not null
                if (shape.get(i).checkPos(xPos, yPos)){ //within bounds of the shape
                    return i; //return the index where true
                }
            }
        }
        return null; //return null as there is no shape
    }

    /**
     * check for all the lines if they are at the coordinate that describes
     * their position. Returns the index of the first one in the arraylist
     * that satisfies that condition. Else it returns null
     */
    public Integer checkLine(double xPos, double yPos){
        for (int i = 0; i < line.size(); i++){
            if (line.get(i) != null){//if not null   
                if (line.get(i).checkPos(xPos, yPos)){ //close enough to the line
                    return i; //return the index where true
                }
            }
        }
        return null;
    }

    /**
     * the method that acts on the selection and makes an object become
     * selected
     */
    public void selectItem(double xPos, double yPos){
        if (selectedShape != null){ //unselects the previous shape
            shape.add(selectedShape);
            selectedShape = null;
        }
        if (selectedLine != null){ //unselects the previous shape
            line.add(selectedLine);
            selectedLine = null;
        }
        if(checkShape(xPos,yPos) != null){ //if there is a shape
            selectedShape = shape.get(checkShape(xPos,yPos)); //selects the shape
            shape.set(checkShape(xPos,yPos) , null);//removes it from the arraylist
            shape.remove(checkShape(xPos,yPos));//removes it from the arraylist
        }
        if(checkLine(xPos,yPos) != null){
            selectedLine = line.get(checkLine(xPos,yPos)); //selects the shape
            line.set(checkLine(xPos,yPos) , null);//removes it from the arraylist
            line.remove(checkLine(xPos,yPos));//removes it from the arraylist
        }
        redraw();
    }

    /**
     * acquires the data about the shape and returns it
     */
    public double[] shapePosition (int index){
        double[] array = shape.get(index).getPosition();
        return array;
    }

    /**
     * returns the index of a line that contains the checked shape
     */
    public ArrayList<Integer> checkShapesOnLine(Shape cS){
        ArrayList<Integer> indexLines = new ArrayList<Integer>();
        for(int i = 0; i < line.size(); i++){
            if(line.get(i) != null){
                if(line.get(i).hasShape(cS)){
                    indexLines.add(i);
                }
            }
        }
        return indexLines;
    }

    /**
     * recreates the line dependant on the new position of one of the shapes
     */
    public void resetLine(Shape changedShape){
        if(checkShapesOnLine(changedShape) != null){
            //returns the other shape attached to the same line as the selectedShape
            ArrayList<Integer> lineIndex = checkShapesOnLine(changedShape);
            for(int i = 0; i < lineIndex.size(); i++){
                Integer lineShareShapes = lineIndex.get(i);
                Shape otherShapeOnLine = line.get(lineShareShapes).otherShape(changedShape);
                String lineType = line.get(lineShareShapes).lineType();
                String textValue = line.get(lineShareShapes).getString();
                if (lineType.equals("Line")){
                    line.set(lineShareShapes ,new Line(selectedShape, otherShapeOnLine, textValue));
                }
                if (lineType.equals("ZigZagLine")){
                    line.set(lineShareShapes ,new ZigZagLine(selectedShape, otherShapeOnLine, textValue));
                }
            }
        }
    }

    /**
     * Looks for the line with the pair of shapes on it and returns its index
     */
    public Integer findLineWithBothShapes(Shape checkShape1, Shape checkShape2){
        if(checkShapesOnLine(lineToShape1) != null && checkShapesOnLine(lineToShape2) != null){
            ArrayList<Integer> linesToShape1 = checkShapesOnLine(checkShape1);
            ArrayList<Integer> linesToShape2 = checkShapesOnLine(checkShape2);
            for(int i = 0; i < linesToShape1.size(); i++){
                for(int j = 0; j < linesToShape2.size(); j++){
                    if(linesToShape1.get(i) == linesToShape2.get(j)){
                        return linesToShape1.get(i);
                    }
                }
            }
        }
        return null;
    }
}
