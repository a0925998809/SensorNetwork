import java.util.ArrayList;

public class Path {
    private ArrayList<Integer> path;
    private double cost;

    public Path(ArrayList<Integer> path, double cost){
        this.path = path;
        this.cost = cost;
    }

    public ArrayList<Integer> getPath() {
        return path;
    }

    public double getCost() {
        return cost;
    }

    public void setPath(ArrayList<Integer> path) {
        this.path = path;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }


    @Override
    public String toString(){
  
        return this.path.toString() + " " + cost;
    }
}
