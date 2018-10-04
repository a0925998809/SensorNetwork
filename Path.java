import java.util.ArrayList;
import java.text.DecimalFormat;

public class Path {
    private ArrayList<Integer> path;
    private double cost;
    private double capacity;

    public Path(ArrayList<Integer> path, double cost, double capacity){
        this.path = path;
        this.cost = cost;
        this.capacity = capacity;
    }

    public ArrayList<Integer> getPath() {
        return path;
    }

    public double getCost() {
        return cost;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setPath(ArrayList<Integer> path) {
        this.path = path;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString(){
    	DecimalFormat    df   = new DecimalFormat("######0.00");
        return this.path.toString() + " " + df.format(cost * Math.pow(10, 7)/1000) + " " + capacity;
    }
}
