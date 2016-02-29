public interface Shape
{
    public void draw();
    public double[] getPosition();
    public boolean checkPos(double xPos, double yPos);
    public void move(double xPos, double yPos);
    public void adjustHeight(double value);
    public void adjustWidth(double value);
    public void adjustText(String value);
    public String toString();
}
