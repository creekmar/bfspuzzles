package solver;

import solver.Configuration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class contains a universal algorithm to find a path from a starting
 * configuration to a solution, if one exists
 *
 * @author Lucille Blain
 */
public class Solver {
    List<Configuration> queue;
    Configuration config;
    Map<Configuration, Configuration> map;
    Configuration start;
    Configuration current;
    int total;
    int unique;


    public Solver(Configuration config){
        queue = new LinkedList<>();
        this.config = config;
        map = new HashMap<Configuration, Configuration>();
        map.put(config,config);
        queue.add(config);
        start = config;
        total =0;
        unique =0;

    }

    public int getTotal() {
        return total;
    }

    public int getUnique() {
        return unique;
    }

    public List<Configuration> solve(){
        while(!queue.isEmpty()){

            current = queue.remove(0);

            unique++;
            total++;
             if(current.isSolution()){
                break;
             }
            for(Configuration i: current.getSuccessors()){

                total++;
                if(!map.containsKey(i)){
                    map.put(i,current);
                    queue.add(i);
                }
            }
        }
        if(!current.isValid())
            return null;
        else if(current.isSolution())
            return ConstructPath(map,start,current);
        else return null;
    }

    private List<Configuration> ConstructPath(Map<Configuration, Configuration> m, Configuration s,Configuration e){
        List<Configuration> path = new LinkedList<>();
        if(m.containsKey(e)){
            Configuration c = e;
            while(!c.equals(s)){

                path.add(0,c);
                c = m.get(c);
            }
            path.add(0,s);
        }
        return path;
    }








}
