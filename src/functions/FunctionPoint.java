package functions;

public class FunctionPoint {
    private  double x;
    private double y;

    public FunctionPoint(){
        this.x = 0;
        this.y = 0;
    }
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public FunctionPoint(FunctionPoint point){
        this.x = point.x;
        this.y = point.y;
    }
    public double getX() {
        return x;
    }
    public double getY(){
        return y;
    }
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
    public String toString(){
        return "(" + x + "; " + y + ")";
    }
    public boolean equals(Object o) {
        if (this == o) return true; //Проверяем ссылки, чтоб не сравнивать с самим собой
        if (o == null || this.getClass() != o.getClass()) return false; //Проверяем классы
        FunctionPoint object = (FunctionPoint) o; //Приводим к нужному классу
        return (this.x == object.getX() && this.y == object.getY()); //Сравниваем поля
    }
    public int hashCode() {
        int largePartX=(int)(Double.doubleToLongBits(x)>>32)<<1;
        int smallPartX=(int)(Double.doubleToLongBits(x))>>1;
        int largePartY=(int)(Double.doubleToLongBits(y)>>32);
        int smallPartY=(int)(Double.doubleToLongBits(y));
        return 907*(largePartX+smallPartX)^(largePartY+smallPartY);
    }
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
