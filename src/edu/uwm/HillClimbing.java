package edu.uwm;

import java.util.List;
import java.util.Stack;

/**
 * Created by Vince Maiuri on 5/11/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class HillClimbing {
    private static int LAST_HEURISTIC;
    private static int NEXT_HEURISTIC;
    private static int MOST_SIDEWAYS_MOVES = 100;

    private static Stack<String> hillClimb(String start) {
        String current = start;
        LAST_HEURISTIC = TilePuzzle.getHeuristic(current);
        Stack<String> path = new Stack<>();
        path.push(current);
        boolean stuck;
        int lastDelta = -1;
        int sidewaysMoves = 0;

        do {
            stuck = true;

            if (TilePuzzle.isGoal(current)) break;

            String neighbor = findBestNeighbor(current);

            int delta = getDelta();

            if (delta <= 0) {
                if (delta == 0 && lastDelta == 0) {
                    sidewaysMoves++;
                } else {
                    sidewaysMoves = 0;
                }
                lastDelta = delta;
                if (sidewaysMoves >= MOST_SIDEWAYS_MOVES) break;
                stuck = false;
                current = neighbor;
                LAST_HEURISTIC = NEXT_HEURISTIC;
                path.push(current);
            }
        } while (!stuck);
        return path;
    }

    private static String findBestNeighbor(String current) {
        List<String> successors = TilePuzzle.succ(current);
        int lastH = Integer.MAX_VALUE;
        String bestSuccessor = null;
        for(String successor : successors){
            int h = TilePuzzle.getHeuristic(successor);

            if (h < lastH) {
                bestSuccessor = successor;
                lastH = h;
                NEXT_HEURISTIC = lastH;
            }
        }

        return bestSuccessor;
    }

    private static int getDelta() {
        return NEXT_HEURISTIC - LAST_HEURISTIC;
    }

    public static HillClimbingResults repeatedHillClimb(String start, int numRepeats) {
        HillClimbingResults results = new HillClimbingResults();

        int bestSuccessPathCount = Integer.MAX_VALUE;
        int bestFailurePathCount = Integer.MAX_VALUE;
        int totalSuccessPathCount = 0;
        int totalFailurePathCount = 0;

        for(int i=0; i<numRepeats; ++i){
            Stack<String> path = hillClimb(start);
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
        results.totalSuccessPathCount = totalSuccessPathCount;
        results.totalFailurePathCount = totalFailurePathCount;

        return results;
    }

    public static class HillClimbingResults {
        int bestSuccessPathCount = Integer.MAX_VALUE,
                bestFailurePathCount = Integer.MAX_VALUE;
        int totalSuccessPathCount, totalFailurePathCount;
        int numSuccesses, numFailures;
        long totalCpuTime;
        Stack<String> bestPath;
    }
}
