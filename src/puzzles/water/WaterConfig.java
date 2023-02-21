package puzzles.water;

import solver.Configuration;
import util.Grid;

import java.util.*;

public class WaterConfig implements Configuration {
    List<Bucket> buckets;
    int goal;

    public WaterConfig(int goal) {
        buckets = new LinkedList<>();
        this.goal = goal;

    }

    public void addBucket(Bucket a) {
        buckets.add((a));
    }

    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
    }

    @Override
    public Collection<Configuration> getSuccessors() {
        List<Configuration> list = new ArrayList<>();
        WaterConfig a;
        WaterConfig b;
        WaterConfig c;
        List <Bucket> copy;


        for(Bucket i:buckets){

            for(Bucket j:buckets){
                copy = Copy();
                if(!(i.equals(j))) {
                    c = transfer(copy.indexOf(i), copy.indexOf(j),copy);
                    if (c != null) {
                        list.add(c);
                    }
                }
            }
            copy = Copy();
            a= fillConfig(copy.indexOf(i),copy);
            if(a !=null){
                list.add(a);
            }
            copy = Copy();
            b = emptyConfig(copy.indexOf(i),copy);
            if(b !=null){
                list.add(b);
            }

        }
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaterConfig)) return false;
        WaterConfig that = (WaterConfig) o;
        return goal == that.goal && Objects.equals(buckets, that.buckets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buckets, goal);
    }
    /**
     * checks if configuration has correct answer
     * @return copy of intial list
     */
    private List<Bucket> Copy(){
        List<Bucket> g = new ArrayList<>();
        for (Bucket i:this.buckets){
            g.add(new Bucket(i));
        }
        return g;
    }
    /**
     * makes possible of move filling bucket
     * @param x : index of bucket getting changed
     * @param copy : list of buckets
     * @return new config
     */
    private WaterConfig fillConfig(int x,List<Bucket> copy){
        WaterConfig c = new WaterConfig(goal);

            Bucket z = copy.get(x);
        if (!z.isfull()){
            z.fill();
            copy.set(x,z);

            c.setBuckets(copy);
            return  c;
        }
        else return null;

        }

    /**
     * makes possible of move emptying buckets
     * @param x : index of bucket getting changed
     * @param copy : list of buckets
     * @return new config
     */
    private WaterConfig emptyConfig(int x, List<Bucket> copy){
        WaterConfig c = new WaterConfig(goal);
        Bucket z = copy.get(x);

        if(z.getCurrent() >=1){
            z.drain();
            copy.set(x,z);
            c.setBuckets(copy);
            return c;
        }
        return  null;
    }
    /**
     * makes possible of move tranfer water
     * @param x : index of bucket getting water
     * @param y : index of bucked giving water
     * @param copy : list of buckets
     * @return new config
     */
    private WaterConfig transfer(int x, int y,List<Bucket> copy){
        WaterConfig c = new WaterConfig(goal);
        Bucket z = copy.get(x);
        Bucket a = copy.get(y);
        if (a.getCurrent() >1 && !z.isfull()){
            z.fillFrom(a);
            copy.set(x,z);
            copy.set(y,a);
            c.setBuckets(copy);
            return c;
        }
        else return null;


    }



    @Override
    public boolean isSolution() {

            for(Bucket i:buckets){
                if (i.getCurrent() == goal){
                    return true;}
            }
        return false;
    }

    @Override
    public boolean isValid() {
            int sum =0;
        for(Bucket i:buckets){
            sum += i.getMax();
        }
        return goal <= sum;
    }

    @Override
    public Grid<String> getGrid() {
        return null;
    }

    @Override
    public String toString() {
        return  String.valueOf(buckets);
    }
}
