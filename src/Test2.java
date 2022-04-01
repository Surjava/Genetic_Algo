public class Test2 {
    public static void main(String[] args) {
        int values[][] = new int[30][30];

        for (int i = 0; i < values.length; i++) {
            // do the for in the row according to the column size
            System.out.print("{");
            for (int j = 0; j < values[i].length; j++) {
                // multiple the random by 10 and then cast to int
                if(i==j)
                {
                    System.out.print("0,");
                    continue;
                }
                while (values[i][j]==0) {
                    values[i][j] = ((int) (Math.random() * 100));
                }
                System.out.print(values[i][j]);
                if(j!=values[i].length-1)
                {
                    System.out.print(",");
                }
                else {
                    System.out.print("},");
                }
            }
            // add a new line
        }
        System.out.println("Done");
    }
}
