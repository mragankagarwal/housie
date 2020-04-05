package com.example.servingwebcontent;

import java.util.*;

/**
 * @author Paritosh
 *
 */
public class Tambola {

    private static final int TICKETS_IN_A_SHEET = 6;
    private static final int NUMBER_OF_COLUMNS = 9;
    private static final int NUMBER_OF_ROWS = 3;

    public static class Ticket {
        public int[][] numbers;

        Ticket(){
            this.numbers = new int[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        }

        int getRowCount(int r){
            int count = 0;
            for(int i = 0; i < NUMBER_OF_COLUMNS; i++){
                if( numbers[r][i]!=0 ) count++;
            }
            return count;
        }

        @Override
        public String toString() {
            return Arrays.deepToString(this.numbers).replace("],", "],\n")+"\n\n";
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(getTickets(6)));
    }

    static int getRand(int min, int max){
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }

    static int getNumberOfElementsInSet(List<List<Integer>> set){
        int count = 0;
        for(List<Integer> li : set) count += li.size();
        return count;
    }

    public static Ticket[] getTickets(int n) {
        Ticket[] res = new Ticket[n];
        Ticket[] generatedTickets = generateTickets();
        System.arraycopy(generatedTickets, 0, res, 0, n);
        return res;
    }

    private static Ticket[] generateTickets() {

        List<List<Integer>> columns = getTicketColumns();
        List<List<List<Integer>>> sets = initializeColumnForEachTicket();

        Ticket[] tickets = new Ticket[TICKETS_IN_A_SHEET];
        for(int i = 0; i < TICKETS_IN_A_SHEET; i++){
            tickets[i] = new Ticket();
        }

        //assigning elements to each set for each column
        for(int i = 0; i < NUMBER_OF_COLUMNS; i++) {
            List<Integer> li = columns.get(i);
            for(int j = 0; j < TICKETS_IN_A_SHEET; j++){
                int randNumIndex = getRand(0, li.size()-1);
                int randNum = li.get(randNumIndex);

                List<Integer> set = sets.get(j).get(i);
                set.add(randNum);

                li.remove(randNumIndex);
            }
        }

        //assign element from last column to random set
        List<Integer> lastCol = columns.get(8);
        int randNumIndex = getRand(0, lastCol.size()-1);
        int randNum = lastCol.get(randNumIndex);

        int randSetIndex = getRand(0,sets.size()-1);
        List<Integer> randSet = sets.get(randSetIndex).get(8);
        randSet.add(randNum);

        lastCol.remove(randNumIndex);

        //3 passes over the remaining columns
        for(int pass=0;pass<3;pass++) {
            for(int i=0;i<9;i++) {
                List<Integer> col = columns.get(i);
                if(col.size()==0) continue;

                int randNumIndex_p = getRand(0, col.size()-1);
                int randNum_p = col.get(randNumIndex_p);

                boolean vacantSetFound = false;
                while(!vacantSetFound) {
                    int randSetIndex_p = getRand(0,sets.size()-1);
                    List<List<Integer>> randSet_p = sets.get(randSetIndex_p);

                    if(getNumberOfElementsInSet(randSet_p)==15 || randSet_p.get(i).size()==2) continue;

                    vacantSetFound = true;
                    randSet_p.get(i).add(randNum_p);

                    col.remove(randNumIndex_p);
                }
            }
        }

        //one more pass over the remaining columns
        for(int i=0;i<9;i++) {
            List<Integer> col = columns.get(i);
            if(col.size()==0) continue;

            int randNumIndex_p = getRand(0, col.size()-1);
            int randNum_p = col.get(randNumIndex_p);

            boolean vacantSetFound = false;
            while(!vacantSetFound) {
                int randSetIndex_p = getRand(0,sets.size()-1);
                List<List<Integer>> randSet_p = sets.get(randSetIndex_p);

                if(getNumberOfElementsInSet(randSet_p)==15 || randSet_p.get(i).size()==3) continue;

                vacantSetFound = true;
                randSet_p.get(i).add(randNum_p);

                col.remove(randNumIndex_p);
            }
        }

        //sort the internal sets
        for(int i=0;i<6;i++){
            for(int j=0;j<9;j++){
                Collections.sort(sets.get(i).get(j));
            }
        }

        printSets(sets);


        //got the sets - need to arrange in tickets now
        for(int setIndex=0;setIndex<6;setIndex++) {
            List<List<Integer>> currSet = sets.get(setIndex);
            Ticket currTicket = tickets[setIndex];

            //fill first row
            for(int size=3;size>0;size--){
                if(currTicket.getRowCount(0)==5) break;
                for(int colIndex=0;colIndex<9;colIndex++){
                    if(currTicket.getRowCount(0)==5) break;
                    if(currTicket.numbers[0][colIndex]!=0) continue;

                    List<Integer> currSetCol = currSet.get(colIndex);
                    if(currSetCol.size()!=size) continue;

                    currTicket.numbers[0][colIndex]=currSetCol.remove(0);
                }
            }

            //fill second row
            for(int size=2;size>0;size--){
                if(currTicket.getRowCount(1)==5) break;
                for(int colIndex=0;colIndex<9;colIndex++){
                    if(currTicket.getRowCount(1)==5) break;
                    if(currTicket.numbers[1][colIndex]!=0) continue;

                    List<Integer> currSetCol = currSet.get(colIndex);
                    if(currSetCol.size()!=size) continue;

                    currTicket.numbers[1][colIndex]=currSetCol.remove(0);
                }
            }

            //fill third row
            for(int size=1;size>0;size--){
                if(currTicket.getRowCount(2)==5) break;
                for(int colIndex=0;colIndex<9;colIndex++){
                    if(currTicket.getRowCount(2)==5) break;
                    if(currTicket.numbers[2][colIndex]!=0) continue;

                    List<Integer> currSetCol = currSet.get(colIndex);
                    if(currSetCol.size()!=size) continue;

                    currTicket.numbers[2][colIndex]=currSetCol.remove(0);
                }
            }
        }

        //print the tickets
//        System.out.println(Arrays.toString(tickets));

        return tickets;
    }

    private static List<List<List<Integer>>> initializeColumnForEachTicket() {
        List<List<List<Integer>>> sets = new ArrayList<>();
        for (int i = 0; i < TICKETS_IN_A_SHEET; i++) {
            List<List<Integer>> set = new ArrayList<>();
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                set.add(new ArrayList<>());
            }
            sets.add(set);
        }
        return sets;
    }

    private static List<List<Integer>> getTicketColumns() {
        // Generating all the 9 columns with 10 numbers each [[1-10],[11-20],.....,[81,90]]
        List<Integer> l1 = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            l1.add(i);
        }

        List<Integer> l2 = new ArrayList<>();
        for (int i = 10; i <= 19; i++) {
            l2.add(i);
        }

        List<Integer> l3 = new ArrayList<>();
        for (int i = 20; i <= 29; i++) {
            l3.add(i);
        }

        List<Integer> l4 = new ArrayList<>();
        for (int i = 30; i <= 39; i++) {
            l4.add(i);
        }

        List<Integer> l5 = new ArrayList<>();
        for (int i = 40; i <= 49; i++) {
            l5.add(i);
        }

        List<Integer> l6 = new ArrayList<>();
        for (int i = 50; i <= 59; i++) {
            l6.add(i);
        }

        List<Integer> l7 = new ArrayList<>();
        for (int i = 60; i <= 69; i++) {
            l7.add(i);
        }

        List<Integer> l8 = new ArrayList<>();
        for (int i = 70; i <= 79; i++) {
            l8.add(i);
        }

        List<Integer> l9 = new ArrayList<>();
        for (int i = 80; i <= 90; i++) {
            l9.add(i);
        }

        List<List<Integer>> columns = new ArrayList<>();
        columns.add(l1);
        columns.add(l2);
        columns.add(l3);
        columns.add(l4);
        columns.add(l5);
        columns.add(l6);
        columns.add(l7);
        columns.add(l8);
        columns.add(l9);

        return columns;
    }

    static void printSets(List<List<List<Integer>>> sets) {
        for (List<List<Integer>> set: sets) {
            System.out.println(set);
        }
    }

}
