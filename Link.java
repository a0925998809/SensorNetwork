
public class Link{

    Edge edge;
    double distance;
    double cost;
    double capacity;

    public Link(Edge edge, double distance, double cost, double capacity) {
        this.edge = edge;
        this.distance = distance;
        this.cost = cost;
        this.capacity = capacity;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public Edge getEdge() {
        return edge;
    }

    public double getDistance() {
        return distance;
    }

    public double getCost() {
        return cost;
    }

    public double getCapacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this.edge.getTail()==((Link)o).getEdge().getTail()
            && this.edge.getHead()==((Link)o).getEdge().getHead()){
            return true;
        }
        return false;
    }

    /*@Override
    public int hashCode(){
        int hash = 7;
        hash = 31 * hash + edge.getHead();
        hash = 31 * hash + edge.getTail();
        hash = 31 * hash + (int)distance;
        hash = 31 * hash + (int)cost;
        hash = 31 * hash + (int)capacity;
        return hash;

    }*/
    @Override
    public String toString(){

        return "edge: " + edge.toString() +
                ", distance: " + Math.round(distance * 1000.0)/1000.0 +
                ", cost: " + Math.round(cost * Math.pow(10,7))/Math.pow(10,7) + ", capacity: " + capacity;
    }
}
