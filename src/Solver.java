import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private Stack<Board> solutions;
    private boolean solvable;
    private int minMoves;

    public Solver(Board initial){
        if (initial == null){
            throw new IllegalArgumentException("The argument to the constructor is null.");
        }

        solutions = null;
        solvable = false;
        minMoves = -1;

        MinPQ<Node> pq = new MinPQ<Node>();
        MinPQ<Node> twinpq = new MinPQ<Node>();
        pq.insert(new Node(initial));
        twinpq.insert(new Node(initial.twin()));

        while (!pq.isEmpty() && !twinpq.isEmpty()){
            Node node = pq.delMin();
            Node twinnode = twinpq.delMin();

            if (twinnode.board.manhattan() == 0){
                break;
            }
            if (node.board.manhattan() == 0){
                solutions = new Stack<Board>();
                solutions.push(node.board);
                solvable = true;
                minMoves = node.moves;

                Node prev = node.previous;
                while (prev != null){
                    solutions.push(prev.board);
                    prev = prev.previous;
                }
                break;
            }

            for (Board neighbor : node.board.neighbors()){
                if (node.previous == null || !node.previous.board.equals(neighbor)){
                    pq.insert(new Node(neighbor, node.moves + 1, node));
                }
            }

            for (Board neighbor : twinnode.board.neighbors()){
                if (twinnode.previous == null || !twinnode.previous.board.equals(neighbor)){
                    twinpq.insert(new Node(neighbor, twinnode.moves + 1, twinnode));
                }
            }
        }
    }

    private class Node implements Comparable<Node>{
        public Board board;
        public int moves;
        public Node previous;

        public Node(Board bd){
            board = bd;
            moves = 0;
            previous = null;
        }

        public Node(Board bd, int mov, Node prev){
            board = bd;
            moves = mov;
            previous = prev;
        }

        public int compareTo(Node that){
            int thisPriorityFunction = this.board.manhattan() + this.moves;
            int thatPriorityFunction = that.board.manhattan() + that.moves;
            if (thisPriorityFunction < thatPriorityFunction) return -1;
            else if (thisPriorityFunction > thatPriorityFunction) return 1;
            else return 0;
        }
    }

    public boolean isSolvable(){
        return solvable;
    }

    public int moves(){
        return minMoves;
    }

    public Iterable<Board> solution(){
        return solutions;
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

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
