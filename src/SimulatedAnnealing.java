import edu.uwm.variations.TilePuzzle;
import edu.uwm.Vertex;

import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Created by Vince Maiuri on 5/11/2015.
 */

@SuppressWarnings({"DefaultFileTemplate", "FieldCanBeLocal"})
public class SimulatedAnnealing {
    private static int COOL_INTERVAL = 500;
    private static double COOL_RATIO = .97;
    private static double INIT_TEMP = 1.0;

    public static void main(String[] args){
        Vertex start = TilePuzzle.getStartNode();

        Stack<Vertex> path = anneal(start);
    }

    private static Stack<Vertex> anneal (Vertex start) {
        Stack<Vertex> path = new Stack<>();
        boolean isStop = false;
        boolean isGoal;
        double T = INIT_TEMP;

        path.push(start);
        long t = 1L;
        Vertex n = start;

        while (!isStop) {
            T = schedule(t, T);
            isGoal = checkGoal(n);

            t++;

            if (T != 0 && !isGoal) {
                Vertex next = getRandomSuccessor(n);
                int deltaE = calculateHeuristicDifference(next, n);

                if (deltaE < 0) {
                    n = next;
                    path.push(next);
                } else {
                    double probability = Math.exp((-deltaE) / T);
                    double accept = Math.random();

                    if (probability > accept) {
                        n = next;
                        path.push(next);
                    }
                }
            } else isStop = true;
        }

        return path;
    }

    private static int calculateHeuristicDifference(Vertex next, Vertex n) {
        return TilePuzzle.getHeuristic(next.getId()) - TilePuzzle.getHeuristic(n.getId());
    }

    private static double schedule(long time, double temp) {
        if (time % COOL_INTERVAL == 0) {
            temp *= COOL_RATIO;
        }

        return temp;
    }

    private static boolean checkGoal(Vertex n) {
        return TilePuzzle.getHeuristic(n.getId()) == 0;
    }

    private static Vertex getRandomSuccessor(Vertex n) {
        List<Vertex> successors = TilePuzzle.succ(n);
        int numSucc = successors.size();
        Random rand = new Random();
        int randomIndex = rand.nextInt(numSucc);

        return successors.get(randomIndex);
    }
}
