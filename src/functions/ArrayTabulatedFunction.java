package functions;

import java.io.Serializable;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable
{
    private int pointsCount;
    private FunctionPoint[] point;

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount){
        if(leftX >= rightX)
        {
            throw new IllegalArgumentException("Cannot create TabulatedFunction: Left x should be greater than right x");
        }
        if (pointsCount < 2)
        {
            throw new IllegalArgumentException("Cannot create TabulatedFunction: Number of values should be at least 2");
        }

        this.pointsCount = pointsCount;
        double step = (rightX - leftX)/(pointsCount-1);
        point = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++){
            point[i] = new FunctionPoint(leftX + step * i, 0);
        }
    }
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values){
        this(leftX,rightX,values.length);
        for (int i = 0; i < values.length;i++){
            point[i].setY(values[i]);
        }
    }
    public ArrayTabulatedFunction(FunctionPoint[] MassPoints){
        this(MassPoints[0].getX(), MassPoints[MassPoints.length-1].getX(), MassPoints.length);

        for (int i = 0; i < MassPoints.length - 1; i++){
            if(MassPoints[i].getX() < MassPoints[i+1].getX()){
                throw new IllegalArgumentException("Cannot create TabulatedFunction: Left x should be less than right x");
            }
        }
        for (int i = 0; i < MassPoints.length;i++){
            point[i].setX(MassPoints[i].getX());
        }
    }

    public double getLeftDomainBorder(){
        return point[0].getX();
    }
    public double getRightDomainBorder(){
        return point[pointsCount-1].getX();
    }

    public double getFunctionValue(double x) {
        double value = Double.NaN;
        if (x > getLeftDomainBorder() && x < getRightDomainBorder()) {
            double step = (getRightDomainBorder() - getLeftDomainBorder())/(pointsCount-1);
            int nextl = (int)(x / step);
            int nextr = nextl + 1;
            value = ((x - point[nextl].getX()) / (point[nextr].getX() - point[nextl].getX())) * (point[nextr].getY() - point[nextl].getY()) + point[nextl].getY();
        }
        return value;
    }
    public int getPointsCount(){
        return pointsCount;
    }
    public FunctionPoint getPoint(int index){
        if( index < 0 || index > pointsCount-1) {
            throw new FunctionPointIndexOutOfBoundsException("Cannot get: Index out of bounds");
        }
        return point[index];
    }
    public double getPointX(int index){
        if( index < 0 || index > pointsCount-1) {
            throw new FunctionPointIndexOutOfBoundsException("Cannot get: Index out of bounds");
        }
        return point[index].getX();
    }
    public double getPointY(int index){
        if( index < 0 || index > pointsCount-1) {
            throw new FunctionPointIndexOutOfBoundsException("Cannot get: Index out of bounds");
        }
        return point[index].getY();
    }
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if( index < 0 || index > pointsCount-1){
            throw new FunctionPointIndexOutOfBoundsException("Cannot get: Index out of bounds");
        }
        if(this.point[index+1].getX()<point.getX()||this.point[index - 1].getX()>point.getX()){
            throw new InappropriateFunctionPointException("Cannot set:  Incorrect function point");
        }

        this.point[index] = point;
    }
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if( index < 0 || index > pointsCount-1) {
            throw new FunctionPointIndexOutOfBoundsException("Cannot get: Index out of bounds");
        }

        FunctionPoint fpoint = point[index];
        fpoint.setX(x);
        setPoint(index, fpoint);
    }
    public void setPointY(int index, double y){
        if( index < 0 || index > pointsCount-1) {
            throw new FunctionPointIndexOutOfBoundsException("Cannot get: Index out of bounds");
        }

        if (index < pointsCount-1 && index > 0)
            point[index].setY(y);
    }
    public void deletePoint(int index){
        if( index < 0 || index > pointsCount-1) {
            throw new FunctionPointIndexOutOfBoundsException("Cannot get: Index out of bounds");
        }
        if(pointsCount<3){
            throw new IllegalStateException("Cannot delete: Too many points");
        }

        System.arraycopy(point, index + 1, point, index, point.length - index - 1);
        this.pointsCount--;
    }
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        for(int i=0; i < pointsCount; i++){
            if (this.point[i].getX()==point.getX()){
                throw new InappropriateFunctionPointException("Cannot add: Found duplicate point");
            }
        }

        if (pointsCount == this.point.length) {
            FunctionPoint[] newPoint = new FunctionPoint[this.point.length + 1];
            int nextr = 0;
            for(int i = 0; i < pointsCount; i++){
                if(point.getX() < this.point[i].getX()){
                    nextr = i;
                    break;
                }
            }
            System.arraycopy(this.point, nextr, newPoint, nextr + 1, pointsCount - nextr);
            System.arraycopy(this.point, 0, newPoint, 0, nextr);
            newPoint[nextr] = point;
            this.point = newPoint;
            this.pointsCount++;
        }
        else {
            int nextr = 0;
            for(int i = 0; i < pointsCount; i++){
                if(point.getX() < this.point[i].getX()){
                    nextr = i;
                    break;
                }
            }
            System.arraycopy(this.point, nextr, this.point, nextr + 1, pointsCount - nextr);
            System.arraycopy(this.point, 0, this.point, 0, nextr);
            this.point[nextr] = point;
            this.pointsCount++;
        }
    }
}