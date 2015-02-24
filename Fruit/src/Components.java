import java.util.Comparator;

/**
 * Created by Anastasia on 11.02.2015.
 */
public class Components implements Comparable<Components>{
    String component;
    Components(){
        component = null;
    }
    Components(String name){ component = name;}
    public String getComponent(){
        return component;
    }

    @Override
    public String toString (){
        return component;
    }
    @Override
    public int compareTo(Components o)
    {
        return component.compareTo(o.component);
    }
    @Override
    public boolean equals(Object o)
    {
        return (component.compareTo(((Components)o).component)==0);
    }

}
