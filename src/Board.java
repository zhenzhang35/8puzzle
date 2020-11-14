import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

public class Board{

    private int n;
    private int [][] tiles;

    public Board(int[][] Tiles){
        n = Tiles[0].length;
        tiles = new int[n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                tiles[i][j] = Tiles[i][j];
            }
        }
    }

    public String toString(){
        StringBuilder representation = new StringBuilder();
        representation.append(String.format("%2d\n", n));
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                representation.append(String.format(" %2d", tiles[i][j]));
            }
            representation.append("\n");
        }
        return representation.toString();
    }

    public int dimension(){
        return n;
    }

    public int hamming(){
        int hamming = 0;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                int goalValue = i * n + j + 1;
                if (goalValue < n*n && goalValue != tiles[i][j]){
                    hamming++;
                }
            }
        }
        return hamming;
    }

    public int manhattan(){
        int manhattan = 0;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                int goalRow = (tiles[i][j] - 1) / n;
                int goalCol = (tiles[i][j] - 1) % n;
                if (tiles[i][j] != 0){
                    manhattan += Math.abs(goalRow - i) + Math.abs(goalCol - j);
                }
            }
        }
        return manhattan;
    }

    public boolean isGoal(){
        return hamming() == 0;
    }

    public boolean equals(Object y){
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.n != that.n) return false;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                if (this.tiles[i][j] != that.tiles[i][j]) return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors(){
        int zeroi = 0;
        int zeroj = 0;
        int [][] copy = new int[n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                if (tiles[i][j] == 0){
                    zeroi = i;
                    zeroj = j;
                }
                copy[i][j] = tiles[i][j];
            }
        }

        int stepi;
        int stepj;
        Queue<Board> neighbors = new Queue<Board>();

        // up
        stepi = zeroi - 1;
        stepj = zeroj;
        if (stepi >= 0 && stepi < n && stepj >= 0 && stepj < n){
            exch(copy, zeroi, zeroj, stepi, stepj);
            neighbors.enqueue(new Board(copy));
            exch(copy, zeroi, zeroj, stepi, stepj);
        }

        // down
        stepi = zeroi + 1;
        stepj = zeroj;
        if (stepi >= 0 && stepi < n && stepj >= 0 && stepj < n){
            exch(copy, zeroi, zeroj, stepi, stepj);
            neighbors.enqueue(new Board(copy));
            exch(copy, zeroi, zeroj, stepi, stepj);
        }
        
        // left
        stepi = zeroi;
        stepj = zeroj - 1;
        if (stepi >= 0 && stepi < n && stepj >= 0 && stepj < n){
            exch(copy, zeroi, zeroj, stepi, stepj);
            neighbors.enqueue(new Board(copy));
            exch(copy, zeroi, zeroj, stepi, stepj);
        }

        // right
        stepi = zeroi;
        stepj = zeroj + 1;
        if (stepi >= 0 && stepi < n && stepj >= 0 && stepj < n){
            exch(copy, zeroi, zeroj, stepi, stepj);
            neighbors.enqueue(new Board(copy));
            exch(copy, zeroi, zeroj, stepi, stepj);
        }

        return neighbors;
    }

    private void exch(int[][] a, int i, int j, int k, int l){
        int x = a[i][j];
        a[i][j] = a[k][l];
        a[k][l] = x;
    }

    public Board twin(){
        if (n == 1) return null;

        int [][] copy = new int[n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                copy[i][j] = tiles[i][j];
            }
        }

        if (copy[0][0] != 0 && copy[0][1] != 0){   // swap the first two non-zero elements in the first row
            exch(copy, 0, 0, 0, 1);
        } else {   // otherwise swap the first two elements in the second row
            exch(copy, 1, 0, 1, 1);
        }

        Board twin = new Board(copy);
        return twin;
    }

    public static void main(String[] args){
        
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println(initial.toString());
        StdOut.println(initial.hamming());
        StdOut.println(initial.manhattan());
        
        int[][] tiles2 = {{8,1,3},{4,0,2},{7,6,5}};
        Board second = new Board(tiles2);
        StdOut.println(initial.equals(second));

        int[][] tiles3 = {{0,1,3},{4,2,5},{7,8,6}};
        Board third = new Board(tiles3);
        StdOut.println(initial.equals(third));

        Iterable<Board> neighbors = initial.neighbors();
        for (Board b : neighbors){
            StdOut.println(b.toString());
        }

        Board twin = initial.twin();
        StdOut.println(twin.toString());
    }
}