import java.io.*;
import java.util.*;

/**
 * Created by Anastasia on 11.02.2015.
 */
public class Juicer  implements Runnable {

    ArrayList<Juice> juice;
    TreeMap<Integer, String> notRepeated;

    Juicer(){juice = new ArrayList<Juice>();}

    public static class ComponentComparator implements Comparator {
        public int compare(Object o1, Object o2){
            return ((Components)o1).getComponent().compareTo(((Components)o2).getComponent());
        }
    }
    public static ComponentComparator sc = new ComponentComparator();
    public static Comparator getSC() {return sc;}


    public void run()
    {
        ArrayList<Components> allElements= componentsAll();
        sort(allElements);
        output("juice2.out", allElements);
    }
    public void sort(ArrayList<Components> elements)
    {
        elements.sort(Juicer.getSC());
    }


    public void input(String nameFile) {
        try{
            int countKey = 0;
            notRepeated = new TreeMap<Integer, String>();
            BufferedReader br = new BufferedReader(new FileReader(nameFile));
            while (br.ready()){
                String strLine = br.readLine();
                juice.add(new Juice(strLine));
                countKey = createNotRepeated(strLine, countKey);
            }
        }catch (FileNotFoundException e)
        {
            System.out.println("Failed to open file ");
            System.out.println("Error: " + e);
            return;
        } catch (IOException e)
        {
            System.out.println("I/O error on file ");
            System.out.println("Error: " + e);
            return;
        }
    }
    private ArrayList<Components> componentsAll(){
        ArrayList<Components> allElements = new ArrayList<Components>();

        for(int i : notRepeated.keySet()){
            allElements.add(new Components(notRepeated.get(i)));
        }
        return allElements;
    }
    public int createNotRepeated(String str, int countKey){

        StringTokenizer st = new StringTokenizer(str, " ");
        while (st.hasMoreTokens()){
            String temp = st.nextToken();
            if(!notRepeated.containsValue(temp))
            {
                notRepeated.put(countKey, temp);
                countKey++;
            }
        }
        return countKey;
    }
    private void output(String nameFile, ArrayList<Components> allElements){
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter(nameFile));
            ListIterator iterator= allElements.listIterator();
            while(iterator.hasNext())
            {
                out.println(iterator.next());
            }
            out.close();
        }
        catch (IOException e)
        {
            System.out.println("I/O error on file ");
            System.out.println("Error: " + e);
            return;
        }
    }
    public void outputDif(String nameFile){
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter(nameFile));
            for(int i: notRepeated.keySet())
                out.println(notRepeated.get(i));
            out.close();
        }
        catch (IOException e)
        {
            System.out.println("I/O error on file ");
            System.out.println("Error: " + e);
            return;
        }
    }

    private ArrayList<Integer> mt;
    private ArrayList<Boolean> used;
    private ArrayList<ArrayList<Integer>> matrix;

    public void algorithmKuna(String nameFile){

        int answer = 0;
        int n = juice.size();

        matrix = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < juice.size(); i++) {
            matrix.add(new ArrayList<Integer>());
            for (int j = i + 1; j < juice.size(); j++) {
                if (juice.get(j).isSubJuiceOf(juice.get(i)))
                {
                    matrix.get(i).add(j);
                }
            }
        }

        int num = juice.size();
        mt = new ArrayList<Integer>();
        used = new ArrayList<Boolean>();
        for (int i = 0; i < num; i++)
            mt.add(-1);
        for (int v = 0; v < num; ++v) {
            used.clear();
            for (int i = 0; i < num; i++)
                used.add(false);
            try_kuhn(v);
        }
        int res = 0;
        for (int i = 0; i < num; ++i)
            if (mt.get(i) != -1)
                res++;

        answer = juice.size() - res;
        try {
            PrintWriter out = new PrintWriter(nameFile);
            out.print(answer);
            out.close();
        }catch (IOException e)
        {
            System.out.println("I/O error on file ");
            System.out.println("Error: " + e);
            return;
        }
    }
    private boolean try_kuhn(int v) {
        if (used.get(v) == true)
            return false;
        used.set(v, true);
        for (int i = 0; i < matrix.get((v)).size(); ++i) {
            int to = (matrix.get(v)).get(i);
            if ((mt.get(to) == -1) || try_kuhn(mt.get(to))) {
                mt.set(to, v);
                return true;
            }
        }
        return false;
    }
}
