import java.util.*;

public class Test {
    final static int gene_size = 9;

    public static void main(String[] args) throws InterruptedException {
        Integer[] arr = {1,2,3,4,5,6,7,8,9};
            List<Integer> array = new ArrayList<>(Arrays.asList(arr));
            Collections.shuffle(array);
            array.add(0, 0);
            System.out.println(array);
        Integer[] arr2 = {9,8,7,6,5,4,3,2,1};
        List<Integer> array2 = new ArrayList<>(Arrays.asList(arr2));
        Collections.shuffle(array2);
        array2.add(0, 0);
        System.out.println(array2);

        System.out.println(crossover(array,array2));

    }


    private static List<Integer> crossover(List<Integer> population1,List<Integer> population2) {

            List<Integer> crossover_gene = new ArrayList<Integer>(Arrays.asList(0,1, 2, 3, 4, 5, 6, 7, 8, 9));

            List<Integer> g1 = population1;
            List<Integer> g2 = population2;
            int flag1 = gene_size/3;
            int flag2 = gene_size*2/3;

            for(int j = flag1 ; j<flag2; j++)
            {
                crossover_gene.set(j,g1.get(j));
                g2.remove(g1.get(j));
            }
            int k = 0;
            for(int j = 0;j<gene_size;j++)
            {
                if(j>=flag1 && j<flag2)
                    continue;
                else {
                    crossover_gene.set(j, g2.get(k));
                    k++;
                }
            }

        return crossover_gene;
    }

}
