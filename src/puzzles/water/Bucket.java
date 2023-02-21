package puzzles.water;

import java.util.Objects;

public class Bucket {
    private int max;
    private int empty =0;
    private int current = 0;
        public Bucket(int max){
            this.max = max;
        }
        public Bucket(Bucket i){
            this.max = i.max;
            this.empty = i.empty;
            this.current = i.current;
        }


        public void fill(){
            current = max;
        }
        public void drain(){
            current = 0;
    }

    public int getCurrent() {
        return current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bucket)) return false;
        Bucket bucket = (Bucket) o;
        return max == bucket.max && empty == bucket.empty && current == bucket.current;
    }

    @Override
    public int hashCode() {
        return Objects.hash(max, empty, current);
    }

    public Boolean isfull(){
            if (current == max){
                return true;
            }
            else return false;
    }

    public int getMax() {
        return max;
    }

    public void fillFrom(Bucket o){
            int left= Math.abs(o.current+this.current-this.max);
            if((this.current+o.current) > this.max) {
                this.fill();
                o.current = left;
            }
            else {this.current+=o.current; o.drain();}

        }

    @Override
    public String toString() {
        return String.valueOf(current);
    }
}

