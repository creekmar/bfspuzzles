package puzzles.clock;

import solver.Configuration;
import util.Grid;

import java.util.*;

public class ClockConfig implements Configuration {
    Map<Integer,Integer> times;
    private int hour;
    private int start;
    private int goal;

    public ClockConfig(int hour, int start,int end){
        this.hour = hour;
        this.start = start;
        this.goal = end;
        this.times= new HashMap<>();
        this.times.put(this.start,start);



    }


    @Override
    public Collection<Configuration> getSuccessors() {
        List<Configuration> successors = new LinkedList<Configuration>();
        int before =this.start -1;
        int after = this.start +1;
        if(before == 0){ before = hour;}
        if(after > this.hour) {after =1;}

            ClockConfig b = new ClockConfig(hour, before, goal);
            successors.add(b);
            ClockConfig c = new ClockConfig(hour,after,goal);
            successors.add(c);


    return successors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClockConfig)) return false;
        ClockConfig that = (ClockConfig) o;
        return hour == that.hour && start == that.start && goal == that.goal && Objects.equals(times, that.times);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start);
    }

    @Override
    public boolean isSolution() {
        if (this.start == goal)
            return true;
        return false;
    }

    @Override
    public boolean isValid() {
        if(goal > hour)
        return false;
        else return true;
    }

    @Override
    public Grid<String> getGrid() {
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(start);
    }
}
