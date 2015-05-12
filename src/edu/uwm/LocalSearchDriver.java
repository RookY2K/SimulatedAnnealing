package edu.uwm;

import edu.uwm.SimulatedAnnealing.SimulatedAnnealingResults;
import edu.uwm.HillClimbing.HillClimbingResults;

/**
 * Created by Vince Maiuri on 5/11/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class LocalSearchDriver {
    private static final int NUM_TILES = 8;
    private static final int NUM_REPEATS = 1;

    public static void main(String[] args){
        SimulatedAnnealingResults saResults = new SimulatedAnnealingResults();
        HillClimbingResults hcResults = new HillClimbingResults();
        for(int i=0; i<NUM_REPEATS; i++) {
            String start = TilePuzzle.getStartNode(NUM_TILES);
            Long startTime = System.currentTimeMillis();

            SimulatedAnnealingResults saTempResults = SimulatedAnnealing.repeatedAnneal(start, 1);
            saResults.totalCpuTime += System.currentTimeMillis() - startTime;
            saResults.totalSuccessPathCount += saTempResults.totalSuccessPathCount;
            saResults.totalFailurePathCount += saTempResults.totalFailurePathCount;
            saResults.numSuccesses += saTempResults.numSuccesses;
            saResults.numFailures += saTempResults.numFailures;

            if (saTempResults.numSuccesses == 1) {
                if (saTempResults.bestSuccessPathCount < saResults.bestSuccessPathCount){
                    saResults.bestSuccessPathCount = saTempResults.bestSuccessPathCount;
                    saResults.bestPath = saTempResults.bestPath;
                }
            } else {
                if (saTempResults.bestFailurePathCount < saResults.bestFailurePathCount){
                    saResults.bestFailurePathCount = saTempResults.bestFailurePathCount;
                }
            }

            startTime = System.currentTimeMillis();
            HillClimbingResults hcTempResults = HillClimbing.repeatedHillClimb(start, 1);
            hcResults.totalCpuTime += System.currentTimeMillis() - startTime;
            hcResults.totalSuccessPathCount += hcTempResults.totalSuccessPathCount;
            hcResults.totalFailurePathCount += hcTempResults.totalFailurePathCount;
            hcResults.numSuccesses += hcTempResults.numSuccesses;
            hcResults.numFailures += hcTempResults.numFailures;

            if (hcTempResults.numSuccesses == 1) {
                if (hcTempResults.bestSuccessPathCount < hcResults.bestSuccessPathCount){
                    hcResults.bestSuccessPathCount = hcTempResults.bestSuccessPathCount;
                    hcResults.bestPath = hcTempResults.bestPath;
                }
            } else {
                if (hcTempResults.bestFailurePathCount < hcResults.bestFailurePathCount){
                    hcResults.bestFailurePathCount = hcTempResults.bestFailurePathCount;
                }
            }
        }

//
//

        System.out.println("<----------------------SIMULATED ANNEALING----------------------------------------------->");
        if (saResults.numSuccesses > 0) TilePuzzle.printPath(saResults.bestPath);
        double averageSuccessPathCount = (double)saResults.totalSuccessPathCount / saResults.numSuccesses;
        double averageFailurePathCount = (double)saResults.totalFailurePathCount / saResults.numFailures;
        System.out.printf("The best successful path count = %d%n" +
                        "The best failed path count = %d%n" +
                        "The average path count for successes = %g%n" +
                        "The average path count for failures = %g%n" +
                        "There were %d success%n" +
                        "and %d failures%n" +
                        "for a success rate of %g%n" +
                        "and an average CPU time of %g ms%n",
                saResults.bestSuccessPathCount, saResults.bestFailurePathCount, averageSuccessPathCount,
                averageFailurePathCount, saResults.numSuccesses, saResults.numFailures,
                (double) saResults.numSuccesses / NUM_REPEATS, (double)saResults.totalCpuTime / NUM_REPEATS);
        



        System.out.println("<----------------------HILL CLIMBING----------------------------------------------->");
        if (hcResults.numSuccesses > 0) TilePuzzle.printPath(hcResults.bestPath);
        averageSuccessPathCount = (double)hcResults.totalSuccessPathCount / hcResults.numSuccesses;
        averageFailurePathCount = (double)hcResults.totalFailurePathCount / hcResults.numFailures;
        System.out.printf("The best successful path count = %d%n" +
                        "The best failed path count = %d%n" +
                        "The average path count for successes = %g%n" +
                        "The average path count for failures = %g%n" +
                        "There were %d success%n" +
                        "and %d failures%n" +
                        "for a success rate of %g%n" +
                        "and an average CPU time of %g ms%n",
                hcResults.bestSuccessPathCount, hcResults.bestFailurePathCount, averageSuccessPathCount,
                averageFailurePathCount, hcResults.numSuccesses, hcResults.numFailures,
                (double) hcResults.numSuccesses / NUM_REPEATS, (double)hcResults.totalCpuTime / NUM_REPEATS);
    }
}
