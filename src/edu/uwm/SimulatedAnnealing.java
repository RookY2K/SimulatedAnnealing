package edu.uwm;

import java.util.*;

/**
 * Created by Vince Maiuri on 5/11/2015.
 */

@SuppressWarnings("DefaultFileTemplate")
public class SimulatedAnnealing {
    private static final int COOL_INTERVAL = 3000;
    private static final int STOPPING_VALUE = 1000;
    private static final double COOL_RATIO = .95;
    private static final double INIT_TEMP = 1.0;
    private static final double MIN_TEMP = Double.MIN_NORMAL;
    private static int LAST_HEURISTIC;
    private static int NEXT_HEURISTIC;
    private static long LAST_UPDATE_TIME = 0L;


    static SimulatedAnnealingResults repeatedAnneal(String start, int numRepeats) {
        SimulatedAnnealingResults results = new SimulatedAnnealingResults();
        int bestSuccessPathCount = Integer.MAX_VALUE;
        int bestFailurePathCount = Integer.MAX_VALUE;
        int totalSuccessPathCount = 0;
        int totalFailurePathCount = 0;

        for(int i=0; i<numRepeats; ++i){
            Stack<String> path = anneal(start);
            int currentPathCount = path.size();
            int h = LAST_HEURISTIC;

            boolean isSuccess = h == 0;

            if (isSuccess) {
                results.numSuccesses++;
                totalSuccessPathCount += currentPathCount;

                if (currentPathCount < bestSuccessPathCount) {
                    bestSuccessPathCount = currentPathCount;
                    results.bestPath = path;
                }
            } else {
                results.numFailures++;
                totalFailurePathCount += currentPathCount;

                if (currentPathCount < bestFailurePathCount) {
                    bestFailurePathCount = currentPathCount;
                }
            }
        }

        results.bestSuccessPathCount = bestSuccessPathCount;
        results.bestFailurePathCount = bestFailurePathCount;
        results.averageSuccessPathCount = (double)totalSuccessPathCount / results.numSuccesses;
        results.averageFailurePathCount = (double)totalFailurePathCount / results.numFailures;

        return results;
    }

    private static Stack<String> anneal (String start) {
        Stack<String> path = new Stack<>();
        boolean isStop = false;
        boolean isGoal;
        double T = INIT_TEMP;

        path.push(start);
        long t = 1L;
        String n = start;
        LAST_HEURISTIC = TilePuzzle.getHeuristic(start);
        while (!isStop) {
            T = schedule(t, T);
            isGoal = checkGoal(n);

            t++;

            if (T > MIN_TEMP && !isGoal) {
                String next = getRandomSuccessor(n);
                int deltaE = calculateHeuristicDifference(next);

                if (deltaE < 0) {
                    n = next;
                    path.push(next);
                    LAST_UPDATE_TIME = t;
                    LAST_HEURISTIC = NEXT_HEURISTIC;
                } else {
                    double probability = Math.exp((-deltaE) / T);
                    double accept = Math.random();

                    if (probability > accept) {
                        n = next;
                        path.push(next);
                        LAST_UPDATE_TIME = t;
                        LAST_HEURISTIC = NEXT_HEURISTIC;
                    }
                }
            } else isStop = true;
        }

        return path;
    }

    private static int calculateHeuristicDifference(String next) {
        NEXT_HEURISTIC = TilePuzzle.getHeuristic(next);
        return NEXT_HEURISTIC - LAST_HEURISTIC;
    }

    private static double schedule(long time, double temp) {
        if (time % COOL_INTERVAL == 0) {
            temp *= COOL_RATIO;
        }
        if (time - LAST_UPDATE_TIME > STOPPING_VALUE) {
            temp = 0;
        }
        return temp;
    }

    private static boolean checkGoal(String n) {
        return TilePuzzle.isGoal(n);
    }

    private static String getRandomSuccessor(String n) {
        List<String> successors = TilePuzzle.succ(n);
        int numSucc = successors.size();
        Random rand = new Random();
        int randomIndex = rand.nextInt(numSucc);

        return successors.get(randomIndex);
    }

    public static class SimulatedAnnealingResults {
        int bestSuccessPathCount, bestFailurePathCount;
        double averageSuccessPathCount, averageFailurePathCount;
        int numSuccesses, numFailures;
        Stack<String> bestPath;
    }
}
