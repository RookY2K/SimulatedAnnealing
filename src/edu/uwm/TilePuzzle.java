package edu.uwm;

import java.util.*;

/**
 * Created by Vince Maiuri on 4/8/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class TilePuzzle {
    private static int _n;
    private static String _goal;
    private static List<Integer> aGoals, bGoals, cGoals;

    static{
        aGoals = new LinkedList<>();
        bGoals = new LinkedList<>();
        cGoals = new LinkedList<>();
    }

    private static void fillGoalQueues() {
        for(int i=0; i<_n; ++i){
            aGoals.add(i);
            bGoals.add(i + _n);
            cGoals.add(i + 2 * _n);
        }
    }

//    public static int getEdgeCost(Vertex origin, Vertex destination) {
//        String start = (String) origin.getDetails("arrangement");
//        String finish = (String) destination.getDetails("arrangement");
//
//        int startE = start.indexOf('E');
//        int finishE = finish.indexOf('E');
//        int diff = Math.abs(finishE - startE);
//
//        return  diff == 1 ? diff : diff - 1;
//    }

    public static int getHeuristic(String arrangement){
        Integer h;
        h = calculateHeuristics(arrangement);

        return h;
    }

    private static int calculateHeuristics(String arrangement) {
        List<Integer> aDistances = new ArrayList<>(_n);
        List<Integer> bDistances = new ArrayList<>(_n);
        List<Integer> cDistances = new ArrayList<>(_n);
        int[] norm = new int[12];
        Iterator<Integer> aIt = aGoals.iterator();
        Iterator<Integer> bIt = bGoals.iterator();
        Iterator<Integer> cIt = cGoals.iterator();

        for(int i=0; i<arrangement.length(); ++i){
            char tile = arrangement.charAt(i);
            int distance, position;
            switch (tile){
                case 'A':
                    distance = aIt.next() - i;
                    position = aDistances.size();
                    norm(tile, norm, distance, position);
                    aDistances.add(Math.abs(distance));
                    break;
                case 'B':
                    distance = bIt.next() - i;
                    position = bDistances.size();
                    norm(tile, norm, distance, position);
                    bDistances.add(Math.abs(distance));
                    break;
                case 'C':
                    distance = cIt.next() - i;
                    position = cDistances.size();
                    norm(tile, norm, distance, position);
                    cDistances.add(Math.abs(distance));
                    break;
            }
        }
        return findH(aDistances, bDistances, cDistances, norm);
    }

    private static int findH(List<Integer> aDistances, List<Integer> bDistances, List<Integer> cDistances, int[] norm) {
        int h = 0;
        for(int i=0; i<_n; ++i){
            h += Math.ceil(aDistances.get(i)/2.0) +
                    Math.ceil(bDistances.get(i)/2.0) +
                    Math.ceil(cDistances.get(i)/2.0);
        }

        for(int i=0; i<12; i+=2){
            h -= Math.min(norm[i], norm[i+1]);
        }

        return h;
    }

    private static void norm(char letter, int[] norm, int distance, int position){
        if(distance % 2 == 0) return;
        int index = 0;
        switch(letter){
            case 'B':
                index += 4;
                break;
            case 'C':
                index += 8;
        }
        if(distance < 0) index += 2;
        if(position % 2 == 0) index += 1;
        norm[index]++;
    }

    public static String getStartNode(int numTiles) {
        determineStart(numTiles);
        String arrangement;

        StringBuilder ret = new StringBuilder(3 * _n + 1);
        StringBuilder charMap = new StringBuilder(_goal);
        int bound = charMap.length();
        Random rand = new Random();

        for (int i = 0; i <= 3 * _n; ++i) {
            int nextIndex = rand.nextInt(bound);
            char nextChar = charMap.charAt(nextIndex);
            ret.append(nextChar);
            charMap.deleteCharAt(nextIndex);
            bound = charMap.length();
        }
        arrangement = ret.toString();

        System.out.println("Start configuration = " + arrangement);
        return arrangement;
    }

    public static List<String> succ(String arrangement) {
        List<String> arrangements = new ArrayList<>();

        int eIndex = arrangement.indexOf('E');

        for(int i=1; i<=3; ++i){
            String newArrangement;
            StringBuilder sb;
            int swapLeftIndex = eIndex - i;
            int swapRightIndex = eIndex + i;

            if(swapLeftIndex >= 0){
                sb = new StringBuilder(arrangement);
                char swap = sb.charAt(swapLeftIndex);
                sb.setCharAt(eIndex, swap);
                sb.setCharAt(swapLeftIndex, 'E');
                newArrangement = sb.toString();
                arrangements.add(newArrangement);
            }

            if(swapRightIndex < arrangement.length()) {
                sb = new StringBuilder(arrangement);
                char swap = sb.charAt(swapRightIndex);
                sb.setCharAt(eIndex, swap);
                sb.setCharAt(swapRightIndex, 'E');
                newArrangement = sb.toString();
                arrangements.add(newArrangement);
            }
        }
        return arrangements;
    }

    public static void printPath(Stack<String> path) {
        System.out.printf("Solution for Sliding/Jumping Tile Puzzle" +
                " with %d tiles%n", 3 * _n);

        int step = path.size();
        String first = path.peek();
        while (!path.isEmpty()) {
            String prev = path.pop();
            if (step == 1) {
                first = prev;
            }
            System.out.printf("%d) %s %n", step, prev);
            step--;
        }

        System.out.println("The puzzle was solved!");
        System.out.println("Starting configuration = " + first);
    }

    private static void initGoal() {
        StringBuilder sb = new StringBuilder(3 * _n + 1);

        for(int i=0; i<=3*_n; ++i){
            sb.append('E');
        }

        for (int i = 0; i<_n; ++i){
            sb.setCharAt(i, 'A');
            sb.setCharAt(i + _n, 'B');
            sb.setCharAt(i + 2 * _n, 'C');
        }

        _goal = sb.toString();
    }

    private static void determineStart(int numTiles) {
        _n = numTiles;
        System.out.print("\n");
        initGoal();
        fillGoalQueues();
    }

    public static boolean isGoal(String arrangement) {
        return arrangement.equals(_goal);
    }

}

