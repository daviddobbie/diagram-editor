public interface LineInterface
{
    public void setLine();
    public void draw();
    public Shape otherShape(Shape oldSelectedShape);
    public boolean hasShape(Shape checkShape);
    public boolean checkPos(double xPos, double yPos);
    public String toString();
    public String lineType();
    public void adjustText(String value);
    public String getString();
}
