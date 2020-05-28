package model;

public class ObstacleModel {
    private double x;
    private double y;
    private double fieldWidth;
    private double fieldHeight;
    
    public ObstacleModel(double x,double y, double fieldWidht,double fieldHeight)
    {
    	this.x = x;
    	this.y = y;
    	this.fieldWidth = fieldWidht;
    	this.fieldHeight = fieldHeight;
    }
    
    public double getX()
    {
    	return x;
    }
    public double getY()
    {
    	return y;
    }
    public void setFieldBorders(double width, double height)
    {
        fieldWidth = width;
        fieldHeight = height;
    }
    public int getMinDistance()
    {
    	return 30;
    }
}
