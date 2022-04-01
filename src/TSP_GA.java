import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;

public class TSP_GA {
    final static int gene_size = 30;
    final static int population_size = 5000;

    public static void main(String[] args) {
        // n is the number of nodes i.e. V

        long startTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        List<List<Integer>> initial_population = initialization(gene_size,population_size);
        List<List<Integer>> populaion = new ArrayList<>(initial_population);
        for(int i =1 ; i<= 10000 ; i++) {
            populaion = new ArrayList<>(selection(populaion));
            if(i%5 == 0)
            {
                //Introducing a mutation every 5th generation
                mutation(populaion);
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
//        int[][] graph = {
//                {0,5,7,7,8,3,3,1,2,4,4,9,3},
//                {2,0,7,8,3,3,7,3,5,6,1,8,6},
//                {2,3,0,3,4,5,2,1,3,3,6,7,6},
//                {8,3,7,0,7,6,3,6,6,4,7,7,4},
//                {7,5,1,5,0,9,4,6,3,5,9,6,3},
//                {8,3,3,3,1,0,3,5,4,2,4,3,2},
//                {9,9,7,3,7,5,0,8,3,2,8,7,1},
//                {4,8,5,5,7,5,1,0,2,7,3,1,3},
//                {8,7,2,3,5,3,4,9,0,5,1,4,5},
//                {2,9,3,9,1,9,8,1,6,0,8,9,2},
//                {5,1,8,9,6,6,9,9,3,3,0,6,5},
//                {2,6,1,9,4,9,9,3,6,4,6,0,1},
//                {9,7,5,9,3,3,5,9,4,1,3,2,0}
//        };

//        int[][] graph = {
//                {0,33,49,36,30,48,87,61,59,68,62,91,45},
//                {16,0,72,98,48,51,50,87,34,6,92,73,35},
//                {74,17,0,33,66,82,60,25,18,81,69,23,89},
//                {52,22,58,0,82,63,63,2,52,44,23,88,34},
//                {51,76,88,68,0,55,25,74,81,72,49,85,73},
//                {64,62,56,12,75,0,73,47,44,33,3,88,60},
//                {59,1,78,96,22,28,0,53,79,52,25,70,63},
//                {54,97,50,23,37,32,26,0,36,66,7,99,22},
//                {71,54,63,77,26,26,98,98,0,96,87,14,75},
//                {84,95,82,14,90,73,93,10,9,0,35,93,76},
//                {42,29,94,7,97,35,40,48,43,70,0,73,63},
//                {48,1,47,2,70,38,90,29,6,71,11,0,43},
//                {69,61,60,6,1,21,94,98,93,61,43,47,0}
//
//        };

        int[][] graph = {{0,3,63,17,65,73,55,33,53,99,68,14,90,34,68,69,33,43,40,37,40,7,72,25,63,88,1,44,77,35},{38,0,93,35,85,63,40,6,26,70,64,52,59,43,29,91,40,4,80,89,26,28,65,91,35,32,15,77,51,58},{25,15,0,4,34,29,28,98,91,58,52,46,84,48,45,22,67,58,14,54,69,39,1,3,87,83,91,23,53,13},{69,24,74,0,11,16,88,41,59,41,47,76,96,64,63,89,69,22,41,3,38,40,57,56,9,49,61,52,79,48},{21,67,65,87,0,65,7,27,24,93,18,51,45,28,33,29,4,34,1,19,35,99,63,55,43,56,76,31,59,74},{53,55,53,17,2,0,65,45,83,27,21,79,54,71,78,84,46,84,66,75,46,97,85,8,21,12,9,42,29,29},{3,20,63,17,18,44,0,88,25,84,70,51,78,52,12,8,83,38,26,62,82,7,92,73,12,3,1,83,97,55},{25,11,9,39,23,20,87,0,35,33,57,64,27,56,18,77,23,64,72,4,15,20,3,25,10,24,22,54,42,65},{30,62,45,90,31,78,20,88,0,81,93,31,88,56,23,78,10,45,85,86,75,28,89,68,47,84,79,97,63,84},{72,7,38,68,1,35,76,67,92,0,12,20,20,64,7,22,75,72,23,59,14,72,63,18,90,35,51,83,22,92},{84,18,97,57,77,71,88,32,27,10,0,14,58,97,89,79,53,84,97,50,49,95,87,62,3,96,69,85,51,67},{93,75,38,35,37,44,57,54,74,10,95,0,21,85,87,9,43,37,22,1,62,57,62,2,40,98,36,51,18,89},{71,45,45,50,6,92,37,51,63,31,55,90,0,57,80,7,62,45,8,87,3,12,10,18,90,71,46,17,2,82},{35,52,46,71,44,37,75,76,15,11,48,20,8,0,96,49,76,70,99,38,76,55,82,11,57,22,5,73,42,12},{22,52,81,32,48,33,27,47,7,58,78,34,63,70,0,38,95,23,27,45,30,96,86,48,92,2,11,21,74,45},{75,17,88,34,34,14,51,11,21,1,81,90,73,7,98,0,4,52,3,1,7,93,24,1,80,73,47,32,32,82},{38,75,17,8,89,49,28,16,65,38,46,21,83,21,98,61,0,49,87,92,7,75,1,63,90,48,71,1,75,89},{84,7,53,88,19,50,52,30,40,92,65,11,49,73,85,20,31,0,69,68,64,80,35,23,35,62,79,90,5,21},{60,7,12,44,89,16,97,4,81,92,53,64,22,42,39,94,62,86,0,30,78,80,60,50,83,23,81,78,83,45},{19,2,53,61,99,29,38,40,48,68,80,58,2,16,26,64,26,32,85,0,97,11,65,82,33,65,83,79,47,46},{38,44,6,74,56,89,95,6,79,33,67,39,10,76,48,64,3,68,83,81,0,5,62,67,4,99,77,35,83,11},{47,92,30,94,5,48,5,85,10,77,64,61,77,9,66,67,36,17,78,41,78,0,44,26,12,61,22,68,13,45},{40,16,83,79,37,27,54,38,10,78,76,95,13,8,68,74,30,25,44,24,76,71,0,71,42,13,13,54,50,64},{61,68,29,81,3,92,11,17,56,52,25,45,52,80,79,35,82,15,34,46,2,22,53,0,89,57,48,5,86,24},{6,92,36,18,64,27,73,77,49,74,18,79,84,43,60,25,29,32,45,12,6,26,72,32,0,41,37,49,62,86},{29,17,20,82,99,50,86,24,5,73,80,35,71,96,97,34,95,54,83,74,50,52,87,33,50,0,77,16,98,47},{31,29,53,88,91,25,48,35,95,90,23,67,14,74,25,16,9,4,58,48,26,99,95,15,59,78,0,59,9,96},{24,98,86,58,84,56,53,98,76,61,80,11,22,16,49,36,96,88,60,82,41,46,31,34,29,89,68,0,10,44},{39,32,33,14,77,50,16,63,43,36,67,73,49,4,1,12,20,75,68,20,22,15,20,50,14,94,9,53,0,87},{31,39,58,44,47,96,92,62,50,9,33,56,79,9,33,66,39,7,77,49,23,91,73,52,65,48,60,2,61,0}};

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
        int pos = rd.nextInt(population_size);
        List<Integer> gene_to_be_mutated = population.get(pos);
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

        population.set(pos,gene_to_be_mutated);
    }
}
