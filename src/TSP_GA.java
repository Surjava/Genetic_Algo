import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TSP_GA {
    final static int gene_size = 13;
    final static int population_size = 10000;

    public static void main(String[] args) {
        // n is the number of nodes i.e. V

        long startTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);



//        initialization(gene_size,population_size).forEach((gene)->{
//            System.out.println(gene);
//            System.out.println("Fitness : "+fitness_value(gene));
//        });
        List<List<Integer>> initial_populaion = initialization(gene_size,population_size);
        List<List<Integer>> populaion = new ArrayList<>(initial_populaion);
        for(int i =1 ; i<= 1000 ; i++) {
            populaion = new ArrayList<>(selection(populaion));
        }
        populaion.forEach((gene)->{
            System.out.println(gene);
            System.out.println("Fitness : "+fitness_value(gene));
        });

        long endTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        System.out.println("Time taken = "+(endTime-startTime));


    }

    public static List<List<Integer>> initialization(int gene_size,int population_size)
    {
        //generating random array containing digits 1 to gene_size
       List<List<Integer>> population = new ArrayList<>();
       int[] sample = IntStream.range(1, gene_size).toArray();

        for(int i=1; i<=population_size;i++)
        {
            List<Integer> array = new ArrayList<>(Arrays.asList(Arrays.stream(sample).boxed().toArray(Integer[]::new)));
            Collections.shuffle(array);
            array.add(0, 0);
            population.add(array);
           // System.out.println(array);
        }
        return population;
    }

    public static int fitness_value(List<Integer> gene)
    {
        List<Integer> fit_gene = new ArrayList<>(gene);
        fit_gene.add(0);
        int fitness = 0;
        int[][] graph = {
                {0,5,7,7,8,10,3,1,2,4,4,9,10},
                {2,0,7,8,10,3,7,3,5,6,1,8,6},
                {2,10,0,10,4,5,2,1,10,10,6,7,6},
                {8,3,7,0,7,6,3,6,6,4,7,7,4},
                {7,5,1,5,0,9,4,6,3,5,9,6,10},
                {8,3,3,10,1,0,3,5,4,2,4,0,2},
                {9,9,7,3,7,5,0,8,10,2,8,7,1},
                {4,8,5,5,7,5,1,0,2,7,10,1,3},
                {8,7,2,3,5,3,4,9,0,5,1,4,5},
                {2,9,3,9,1,9,8,1,6,0,8,9,2},
                {5,1,8,9,6,6,9,9,3,10,0,6,5},
                {2,6,1,9,4,9,9,10,6,4,6,0,1},
                {9,7,5,9,10,3,5,9,4,1,10,2,0}
        };

        for(int i = 0;i<fit_gene.size()-1;i++)
        {
            fitness = fitness + graph[fit_gene.get(i)][fit_gene.get(i + 1)];
        }
        return fitness;
    }
    public static List<List<Integer>> selection ( List<List<Integer>> population)
    {
        List<List<Integer>> selected_population = new ArrayList<>();
        List<List<Integer>> selected_population_from_discarded = new ArrayList<>();
        List<List<Integer>> selected_crossover_population = new ArrayList<>();
        List<List<Integer>> discarded_population = new ArrayList<>();
        List<List<Integer>> final_population = new ArrayList<>();

        for(int i = 0; i < population.size()-1;i+=2)
        {
            if(fitness_value(population.get(i)) <= fitness_value(population.get(i + 1))) {
                selected_population.add(population.get(i));
                discarded_population.add(population.get(i + 1));
            }
            else {
                selected_population.add(population.get(i + 1));
                discarded_population.add(population.get(i));
            }
        }

        final_population.addAll(selected_population);
        selected_crossover_population = crossover(selected_population);
        final_population.addAll(selected_crossover_population);



        for(int i = 0; i < discarded_population.size()-1;i+=2)
        {
            if(fitness_value(discarded_population.get(i)) <= fitness_value(discarded_population.get(i + 1)))
                selected_population_from_discarded.add(discarded_population.get(i));
            else
                selected_population_from_discarded.add(discarded_population.get(i + 1));
        }

        final_population.addAll(selected_population_from_discarded);
        return final_population;
    }

    private static List<List<Integer>> crossover(List<List<Integer>> selected_population) {
        final List<List<Integer>> crossover_population = new ArrayList<>();
        for(int i = 0 ;i<selected_population.size()/2;i++)
        {
            List<Integer> crossover_gene = new ArrayList<>(selected_population.get(i+1));

            List<Integer> g1 = new ArrayList<>(selected_population.get(i));

            List<Integer> g2 = new ArrayList<>(selected_population.get(i+1));
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
            crossover_population.add(crossover_gene);

        }
        return crossover_population;
    }
}
