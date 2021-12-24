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
        List<List<Integer>> initial_population = initialization(gene_size,population_size);
        List<List<Integer>> populaion = new ArrayList<>(initial_population);
        for(int i =1 ; i<= 1000 ; i++) {
            populaion = new ArrayList<>(selection(populaion));
            if(i%5 == 0)
            {
                //Introducing a mutation every 5th generation
                mutation(populaion);
            }
        }
        populaion.forEach((gene)->{
            System.out.println(gene);
            System.out.println("Fitness : "+fitness_value(gene));
        });

        long endTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        System.out.println("Time taken = "+(endTime-startTime) + " seconds");

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

    //To calculate the cost of the path taken
    public static int fitness_value(List<Integer> gene)
    {
        List<Integer> fit_gene = new ArrayList<>(gene);
        fit_gene.add(0);//adding 0 at end to calculate cost of returning to starting position
        int fitness = 0;
        int[][] graph = {
                {0,5,7,7,8,3,3,1,2,4,4,9,3},
                {2,0,7,8,3,3,7,3,5,6,1,8,6},
                {2,3,0,3,4,5,2,1,3,3,6,7,6},
                {8,3,7,0,7,6,3,6,6,4,7,7,4},
                {7,5,1,5,0,9,4,6,3,5,9,6,3},
                {8,3,3,3,1,0,3,5,4,2,4,3,2},
                {9,9,7,3,7,5,0,8,3,2,8,7,1},
                {4,8,5,5,7,5,1,0,2,7,3,1,3},
                {8,7,2,3,5,3,4,9,0,5,1,4,5},
                {2,9,3,9,1,9,8,1,6,0,8,9,2},
                {5,1,8,9,6,6,9,9,3,3,0,6,5},
                {2,6,1,9,4,9,9,3,6,4,6,0,1},
                {9,7,5,9,3,3,5,9,4,1,3,2,0}
        };

        for(int i = 0;i<fit_gene.size()-1;i++)
        {
            fitness = fitness + graph[fit_gene.get(i)][fit_gene.get(i + 1)];
        }
        return fitness;
    }
    public static List<List<Integer>> selection ( List<List<Integer>> population)
    {
        int min = Integer.MAX_VALUE;//just for reference
        List<List<Integer>> selected_population = new ArrayList<>();
        List<List<Integer>> selected_population_from_discarded = new ArrayList<>();
        List<List<Integer>> selected_crossover_population = new ArrayList<>();
        List<List<Integer>> discarded_population = new ArrayList<>();
        List<List<Integer>> final_population = new ArrayList<>();

        //comparing 2 consecutive Lists and selecting the one with lower cost path
        for(int i = 0; i < population.size()-1;i+=2)
        {
            int fitness1 = fitness_value(population.get(i));
            int fitness2 = fitness_value(population.get(i + 1));
            min =Math.min(min, Math.min(fitness1,fitness2));
            if(fitness1 <= fitness2) {
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



        //selecting best among the discarded.
        for(int i = 0; i < discarded_population.size()-1;i+=2)
        {
            if(fitness_value(discarded_population.get(i)) <= fitness_value(discarded_population.get(i + 1)))
                selected_population_from_discarded.add(discarded_population.get(i));
            else
                selected_population_from_discarded.add(discarded_population.get(i + 1));
        }

        final_population.addAll(selected_population_from_discarded);
       // System.out.println("Min = "+ min);
        return final_population;
    }

    //breaking the 2 consecutive lists into 3 parts
    //adding the middle part of first list in new list (in same positions)
    //adding the other elements of the second list into the new list in order (excluding elements taken from first list)
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

    //select random gene from population -> select two random position(other than 0th) and swap
    private static void mutation(List<List<Integer>> population)
    {
        Random rd =  new Random();
        List<Integer> gene_to_be_mutated = population.get(rd.nextInt(population_size));
        int position1 = rd.nextInt(gene_size);
        while (position1==0) {
            position1 = rd.nextInt(gene_size); //to prevent 0th element to be switched
        }
        int position2 = rd.nextInt(gene_size);
        while (position2==0){
            position2 = rd.nextInt(gene_size);
        }
        int temp = gene_to_be_mutated.get(position1);
        gene_to_be_mutated.set(position1,gene_to_be_mutated.get(position2));
        gene_to_be_mutated.set(position2,temp);
    }
}
