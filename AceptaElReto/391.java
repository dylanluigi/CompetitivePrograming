import java.util.*;

public class Main {

    static java.util.Scanner in;

    public static boolean casoDePrueba() {
        if (!in.hasNext())
            return false;
        else {
            try {
                String line = in.nextLine();
                if(line.equals("")){
                    return false;
                }
                if (line.trim().equals("---")) {
                    System.out.println("---");
                    return true;
                }
                String[] parts = line.trim().split(" ");
                int n = Integer.parseInt(parts[0]);
                int nc = Integer.parseInt(parts[1]);
                int[][] edges = new int[nc][3];
                for (int i = 0; i < nc; i++) {
                    line = in.nextLine().trim();
                    parts = line.split(" ");
                    edges[i][0] = Integer.parseInt(parts[0]);
                    edges[i][1] = Integer.parseInt(parts[1]);
                    edges[i][2] = Integer.parseInt(parts[2]);
                }
                int c = Integer.parseInt(in.nextLine().trim());
                List<int[]> cursos = new ArrayList<>();
                for (int i = 0; i < c; i++) {
                    line = in.nextLine().trim();
                    parts = line.split(" ");
                    int[] curso = new int[parts.length];
                    for (int j = 0; j < parts.length; j++) {
                        curso[j] = Integer.parseInt(parts[j]);
                    }
                    cursos.add(curso);
                }
                double[][] distancias = floydWarshall(n, edges);
                List<Pair> resultados = encontrarMejorPueblo(n, distancias, cursos);
                for (Pair p : resultados) {
                    System.out.println(p.pueblo + " " + p.distancia);
                }
                System.out.println("---");
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public static void main(String[] args) {
        in = new java.util.Scanner(System.in);
        while (casoDePrueba()) {
        }
    } 

    static final double INF = Double.POSITIVE_INFINITY;

    public static double[][] floydWarshall(int n, int[][] edges) {
        double[][] dist = new double[n][n];

        // Initialize distances
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;
        }

        // Populate distances from edges
        for (int[] edge : edges) {
            int u = edge[0] - 1;
            int v = edge[1] - 1;
            double w = edge[2];
            dist[u][v] = Math.min(dist[u][v], w);
            dist[v][u] = Math.min(dist[v][u], w);
        }

        // Floyd-Warshall algorithm
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }


    public static List<Pair> encontrarMejorPueblo(int n, double[][] dist, List<int[]> cursos) {
        List<Pair> resultados = new ArrayList<>();
        for (int[] curso : cursos) {
            List<Integer> pueblosAsignados = new ArrayList<>();
            for (int i = 1; i < curso.length; i++) {
                pueblosAsignados.add(curso[i] - 1);
            }
            double mejorDistancia = INF;
            int mejorPueblo = -1;
            for (int pueblo = 0; pueblo < n; pueblo++) {
                if (!pueblosAsignados.contains(pueblo)) {
                    Permutation perm = new Permutation();
                    List<List<Integer>> perms = perm.permute(pueblosAsignados);
                    for (List<Integer> p : perms) {
                        double distanciaTotal = dist[pueblo][p.get(0)];
                        for (int i = 0; i < p.size() - 1; i++) {
                            distanciaTotal += dist[p.get(i)][p.get(i + 1)];
                        }
                        distanciaTotal += dist[p.get(p.size() - 1)][pueblo];
                        if (distanciaTotal < mejorDistancia) {
                            mejorDistancia = distanciaTotal;
                            mejorPueblo = pueblo + 1;
                        }
                    }
                }
            }
            resultados.add(new Pair(mejorPueblo, (int) Math.round(mejorDistancia)));
        }
        return resultados;
    }
}

class Pair {
    int pueblo;
    int distancia;

    Pair(int pueblo, int distancia) {
        this.pueblo = pueblo;
        this.distancia = distancia;
    }
}

class Permutation {
    public List<List<Integer>> permute(List<Integer> nums) {
        List<List<Integer>> list = new ArrayList<>();
        backtrack(list, new ArrayList<Integer>(), nums);
        return list;
    }

    private void backtrack(List<List<Integer>> list, List<Integer> tempList, List<Integer> nums) {
        if (tempList.size() == nums.size()) {
            list.add(new ArrayList<>(tempList));
        } else {
            for (int i = 0; i < nums.size(); i++) {
                if (tempList.contains(nums.get(i))) continue;
                tempList.add(nums.get(i));
                backtrack(list, tempList, nums);
                tempList.remove(tempList.size() - 1);
            }
        }
    }
}
