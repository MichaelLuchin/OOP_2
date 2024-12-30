package functions;

import java.io.Serializable;

public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable
{
    private static class FunctionNode {


        //NODE's VARS
        private FunctionNode prevAddress;
        private final FunctionPoint object;
        private FunctionNode nextAddress;


        //NODE's CONSTRUCTORS
        private FunctionNode() {
            object = new FunctionPoint();
            prevAddress = this;
            nextAddress = this;
        }

        private FunctionNode(FunctionNode leftNode, FunctionPoint object, FunctionNode rightNode) {
            this.prevAddress = leftNode;
            this.object = object;
            this.nextAddress = rightNode;
        }


        /*Class's getters:
        private FunctionNode getLeftAddress() {return prevAddress;}
        private FunctionPoint getObject() {return object;}
        private FunctionNode getRightAddress() {return nextAddress;}

        Class's setters:
        private void setLeftAddress(FunctionNode LeftNode) {prevAddress = LeftNode;}
        private void setObject(FunctionPoint object) {this.object = object;}
        private void setRightAddress(FunctionNode RightNode) {prevAddress = RightNode;}*/
    }


    //LIST's vars:
    private final FunctionNode head;
    private FunctionNode iterator;
    private int pointsCount;


    //LIST's constructors:
    public LinkedListTabulatedFunction()
    {
        throw new IllegalStateException("Cannot create TabulatedFunction: Too many points");
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        this(leftX,rightX,new double[pointsCount]);
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if(leftX >= rightX)
        {
            throw new IllegalStateException("Cannot create TabulatedFunction: Left x should be greater than right x");
        }
        if (values.length < 2)
        {
            throw new IllegalStateException("Cannot create TabulatedFunction: Number of values should be at least 2");
        }
        head = new FunctionNode();
        iterator = head;
        pointsCount = values.length;
        double stepX = (rightX-leftX) / (pointsCount - 1);
        double curX;
        for (int i=0; i < pointsCount; i++)
        {
            curX = leftX + stepX * i;
            iterator = addNodeToTail(null);
            iterator.object.setX(curX);
            iterator.object.setY(values[i]);
        }
    }
    public LinkedListTabulatedFunction(FunctionPoint[] MassPoints){
        int i = MassPoints.length-1;
        while(i > 0 && MassPoints[i].getX() < MassPoints[--i].getX());
        if(i == 0)
        {
            throw new IllegalStateException("Cannot create TabulatedFunction: Left x should be greater than right x or number of values should be at least 2");
        }
        head = new FunctionNode();
        iterator = head;
        pointsCount = MassPoints.length;
        for(int j=0; j < pointsCount; j++)
        {
            addNodeToTail(MassPoints[j]);
        }
    }



    //TF's METHODS (ArrayTabulatedFunction's methods)
    public double getLeftDomainBorder()
    {
        return head.nextAddress.object.getX();
    }
    public double getRightDomainBorder()
    {
        return head.prevAddress.object.getX();
    }
    public double getFunctionValue(double x) {
        if(x > getRightDomainBorder() || x < getLeftDomainBorder())
        {
            return Double.NaN;
        }
        getNodeByX(x);
        return ((iterator.prevAddress.object.getY()-iterator.object.getY())/(iterator.prevAddress.object.getX()-iterator.object.getX()))*(x - iterator.prevAddress.object.getX()) + iterator.prevAddress.object.getY();
    }
    public int getPointsCount() {
        return pointsCount;
    }
    public FunctionPoint getPoint(int index) {
        if( index < 0 || index > pointsCount-1) {
            throw new FunctionPointIndexOutOfBoundsException("Cannot get: Index out of bounds");
        }
        return getNodeByIndex(index).object;
    }
    public double getPointX(int index) {
        if( index < 0 || index > pointsCount - 1) {
            throw new FunctionPointIndexOutOfBoundsException("Cannot get: Index out of bounds");
        }
        return getNodeByIndex(index).object.getX();
    }
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if(index < 0 || index > pointsCount - 1)
        {
            throw new FunctionPointIndexOutOfBoundsException("Cannot set: Index out of bounds");
        }
        getNodeByIndex(index);
        if((index != pointsCount - 1 && iterator.nextAddress.object.getX() < x) || (index != 0 && iterator.prevAddress.object.getX() > x))
        {
            throw new InappropriateFunctionPointException("Cannot set: Incorrect function point");
        }
        iterator.object.setX(x);
    }
    public double getPointY(int index) {
        if(index < 0 || index > pointsCount-1){
            throw new FunctionPointIndexOutOfBoundsException("Cannot get: Index out of bounds");
        }
        return getNodeByIndex(index).object.getY();
    }
    public void setPointY(int index, double y) {
        if(index < 0 || index > pointsCount-1) {
            throw new FunctionPointIndexOutOfBoundsException("Cannot set: Index out of bounds");
        }
        getNodeByIndex(index).object.setY(y);
    }
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        iterator = head.nextAddress;
        for(int i=0; i < pointsCount; i++, iterator = iterator.nextAddress){
            if (iterator.object.getX()==point.getX()){
                throw new InappropriateFunctionPointException("Cannot add: Found duplicate point");
            }
        }
        getNodeByX(point.getX());
        FunctionNode newNode = new FunctionNode(iterator.prevAddress, point, iterator);
        iterator.prevAddress.nextAddress = newNode;
        iterator.prevAddress = newNode;
        pointsCount++;
    }
    public void deletePoint(int index) {
        if(index < 0 || index > pointsCount-1) {
            throw new FunctionPointIndexOutOfBoundsException("Cannot delete: Index out of bounds");
        }
        if(pointsCount<3){
            throw new IllegalStateException("Cannot delete: Too many points");
        }
        deleteNodeByIndex(index);
    }


    //LIST METHODS
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {throw new FunctionPointIndexOutOfBoundsException("Index out of bounds");} //ERROR
        iterator = head;
        if (index < ((pointsCount - 1) >> 1))
        {
            for (int i = 0; i <= index; i++)
            {
                iterator = iterator.nextAddress;
            }
        } else{
            for (int i = 0; i <= pointsCount-index-1; i++)
            {
                iterator = iterator.prevAddress;
            }
        }
        return iterator;
    }
    private FunctionNode getNodeByX(double x) {
        iterator = head;
        if (x < ((this.getRightDomainBorder() + this.getLeftDomainBorder()) / 2)) {
            do {
                iterator = iterator.nextAddress;
            } while(x > iterator.object.getX());
        } else{
            do {
                iterator = iterator.prevAddress;
            } while(x < iterator.prevAddress.object.getX());
        }
        return iterator;
    }
    private FunctionNode addNodeToTail(FunctionPoint obj)
    {
        if(obj==null)
            obj = new FunctionPoint();
        FunctionNode newNode = new FunctionNode(head.prevAddress, obj, head);
        head.prevAddress.nextAddress = newNode;
        head.prevAddress = newNode;
        if (head.nextAddress == head)
        {
            head.nextAddress = newNode;
        }
        return newNode;
    }
    private FunctionNode addNodeByIndex(int index) {
        getNodeByIndex(index);
        FunctionPoint newPoint = new FunctionPoint();
        FunctionNode newNode = new FunctionNode(iterator, newPoint, iterator.nextAddress);
        iterator.nextAddress = newNode;
        return newNode;
    }
    private FunctionNode deleteNodeByIndex(int index) {
        getNodeByIndex(index);
        iterator.prevAddress.nextAddress = iterator.nextAddress;
        iterator.nextAddress.prevAddress = iterator.prevAddress;
        iterator.prevAddress = null;
        iterator.nextAddress = null;
        pointsCount--;
        return iterator;
    }
    public String toString() {
        StringBuilder str = new StringBuilder();
        iterator = head.nextAddress;
        str.append("{").append(iterator.object.toString());

        while (iterator.nextAddress != head) {
            iterator = iterator.nextAddress;
            str.append(", ").append(iterator.object.toString());
        }
        str.append("}");

        return str.toString();
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof LinkedListTabulatedFunction object)
        {
            if(object.pointsCount!=pointsCount)return false;
            iterator=head.nextAddress;
            object.iterator=object.head.nextAddress;
            while (iterator != head) {
                if(!iterator.object.equals(object.iterator.object)) return false;
                iterator = iterator.nextAddress;
                object.iterator = object.iterator.nextAddress;
            }
            return true;
        }
        else if(o instanceof ArrayTabulatedFunction) {
            if(((TabulatedFunction)o).getPointsCount()!=pointsCount)return false;
            iterator=head.nextAddress;
            for(int j=0; j < pointsCount; j++, iterator=iterator.nextAddress)
                if(!iterator.object.equals(((TabulatedFunction) o).getPoint(j))) return false;
            return true;
        }
        return false;
    }
    public int hashCode() {
        int hash = pointsCount;
        iterator=head.nextAddress;
        while (iterator != head) {
            hash +=iterator.object.hashCode();
            hash *= 907;
            iterator = iterator.nextAddress;
        }
        return hash;
    }
    public Object clone() throws CloneNotSupportedException {
        FunctionPoint arr[] =new FunctionPoint[pointsCount];
        iterator=head.nextAddress;
        for(int i=0; i < pointsCount; i++, iterator=iterator.nextAddress)
            arr[i]=iterator.object;
        try
        {
            return new LinkedListTabulatedFunction(arr);
        }
        catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
        return super.clone();
    }
}