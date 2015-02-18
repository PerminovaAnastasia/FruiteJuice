import java.util.*;

/**
 * Created by Anastasia on 11.02.2015.
 */
public class Juice{

    TreeSet<Components> components;
    Juice(){ components = new TreeSet<Components>();}
    Juice(String str){
        components = new TreeSet<Components>();

        StringTokenizer st = new StringTokenizer(str, " ");
        while (st.hasMoreTokens()){
            String temp = st.nextToken();
            components.add(new Components(temp));
        }
    }

    public boolean isSubJuiceOf(Juice obj) {
        return this.components.containsAll(obj.components);
    }
  /*  public static class treeComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            return ((Juice) o2).components.size() - (((Juice) o1).components.size());
        }
    }
    public static treeComparator nc = new treeComparator();
    public static Comparator getNC() {
        return nc;
    }*/
}
