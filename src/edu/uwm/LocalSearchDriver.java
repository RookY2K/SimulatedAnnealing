package edu.uwm;

import edu.uwm.SimulatedAnnealing.SimulatedAnnealingResults;
import edu.uwm.HillClimbing.HillClimbingResults;

/**
 * Created by Vince Maiuri on 5/11/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class LocalSearchDriver {
    private static final int NUM_TILES = 6;
    private static final int NUM_REPEATS = 100;

    public static void main(String[] args){
        String start = TilePuzzle.getStartNode(NUM_TILES);
        Long startTime = System.currentTimeMillis();

        SimulatedAnnealingResults saResults = SimulatedAnnealing.repeatedAnneal(start, NUM_REPEATS);

        Long totalTime = System.currentTimeMillis() - startTime;
        if (saResults.numSuccesses > 0) TilePuzzle.printPath(saResults.bestPath);

        System.out.println("<----------------------SIMULATED ANNEALING----------------------------------------------->");
        System.out.printf("The best successful path count = %d%n" +
                        "The best failed path count = %d%n" +
                        "The average path count for successes = %g%n" +
                        "The average path count for failures = %g%n" +
                        "There were %d success%n" +
                        "and %d failures%n" +
                        "for a success rate of %g%n" +
                        "and a total CPU time of %d ms%n",
                saResults.bestSuccessPathCount, saResults.bestFailurePathCount, saResults.averageSuccessPathCount,
                saResults.averageFailurePathCount, saResults.numSuccesses, saResults.numFailures,
                (double) saResults.numSuccesses / NUM_REPEATS, totalTime);
        

        startTime = System.currentTimeMillis();
        HillClimbingResults hcResults = HillClimbing.repeatedHillClimb(start, NUM_REPEATS);

        totalTime = System.currentTimeMillis() - startTime;

        if (hcResults.numSuccesses > 0) TilePuzzle.printPath(hcResults.bestPath);

        System.out.println("<----------------------HILL CLIMBING----------------------------------------------->");
        System.out.printf("The best successful path count = %d%n" +
                        "The best failed path count = %d%n" +
                        "The average path count for successes = %g%n" +
                        "The average path count for failures = %g%n" +
                        "There were %d success%n" +
                        "and %d failures%n" +
                        "for a success rate of %g%n" +
                        "and a total CPU time of %d ms%n",
                hcResults.bestSuccessPathCount, hcResults.bestFailurePathCount, hcResults.averageSuccessPathCount,
                hcResults.averageFailurePathCount, hcResults.numSuccesses, hcResults.numFailures,
                (double) hcResults.numSuccesses / NUM_REPEATS, totalTime);
    }
}
