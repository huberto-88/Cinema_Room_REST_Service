import java.util.Arrays;
import java.util.Scanner;

class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

            var sc = new Scanner(System.in).useDelimiter("\n");
        long start = System.currentTimeMillis();
        var size = Arrays.stream(sc.next().split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            var cinema = new String[size[0]][size[1]];
            for (int i = 0; i < size[0]; i++) {
                cinema[i] = sc.next().split("\\s+");
            }
            int seats = sc.nextInt();
            for (int i = 0; i < size[0]; i++) {
                if (String.join("", cinema[i]).contains("0".repeat(seats))) {
                    System.out.print(i + 1);
                    return;
                }
            }
            System.out.println(0);
        scanner.close();

        long stop = System.currentTimeMillis();
        System.out.println((stop - start));
    }

    public static int[][] read2DArray(int n, int m) {
        int[][] array = new int[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                array[i][j] = scanner.nextInt();
            }
        }
        return array;
    }
}