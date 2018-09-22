package com.devx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
* Automaton entity represents NDF automaton in the computer memory
* */
public class Automaton {

    /*Sigma function of the automaton*/
    private Map<String , LinkedList<String>> sigma = new HashMap<String, LinkedList<String>>();

    /*The alphabet, which might be accepted by the automaton*/
    private LinkedList<String> alphabet = new LinkedList<>();

    /*The set of final states of the automaton*/
    private LinkedList<String> finish = new LinkedList<>();

    /*Current state of the automaton. Initial value is 0 (= q0)
    * Actually, represents the current vertex of the
    * graph representation of this automaton, which we are currently on*/
    private String state = "0";

    /*q0 vertex*/
    private static final String initialState = "0";

    /*Stands for UNDEFINED state of the automaton
     * or for unknown character, which can't be processed
     * by the automaton*/
    private static final String error = "-1";

    public Automaton(String path){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(path));

            //parse the alphabet
            String line = reader.readLine();
            for(String r: line.split(" ")){
                alphabet.add(r);
            }

            //parse the final states
            line = reader.readLine();
            for(String r: line.split(" ")){
                finish.add(r);
            }

            //parse the sigma function table
            String q, qArr;
            LinkedList<String> ls;
            line = reader.readLine();
            while(line!=null){
                String[] r = line.split(":");
                q = r[0];
                qArr = r[1];
                ls = new LinkedList<>();
                for(String res: qArr.split(" ")){
                    if(!res.equals(""))
                    ls.add(res);
                }

                if(ls.size()!=alphabet.size()){
                    throw new RuntimeException("Mistake in the input text file found");
                }

                sigma.put(q.trim(),ls);

                line = reader.readLine();
            }
            reader.close();
        }catch (IOException exc){
            System.out.println("Input/output problems.");
            throw new RuntimeException();
        }

    }

    public boolean doesAcceptWord(String word){
        char cur;
        StringBuilder w = new StringBuilder(word);
        while(w.length()>0){
            //pop the first character from the 'input stream'
            cur = w.charAt(0);//get the first character
            w.deleteCharAt(0);

            try{
                LinkedList<String> r1 = sigma.get(state);
                int ind = getCharIndex(String.valueOf(cur));
                /*For the case when state becomes -1*/
                if(r1==null) continue;
                String r2 = r1.get(ind);
                //state = sigma.get(state).get(getCharIndex(String.valueOf(cur)));
                state = r2;
            }catch (ArrayIndexOutOfBoundsException exc){
                return false;
            }catch (NullPointerException exc){
                return false;
            }catch (IndexOutOfBoundsException exc){
                return false;
            }
        }

        //to prevent mistake when calculating isFinalState for -1
        if(state.equals(error)) return false;

        /*
        * Even in case when the vertex is not final,
        * still the word may be accepted.
        * This will happen in case when a subtree, which
        * starts from the current vertex contains final vertices
        * (w2 will be concatenated and will lead to final state)
        * */
        if(isFinalState(state)) return true;
        else if(doesSubtreeContainFinalVertices(state)){
            return true;
        }

        return false;

    }

    /*Checks if the automaton accepts the words of structure w=w1w0w2*/
    public boolean doesAcceptWordOfW1W0W2Structure(String word){
        //loop through each and every vertex of the graph
        for(String vertex : sigma.keySet()){
            /*
            * w=w1w0w2:
            * We can't take qo, because in this case we will only be able
            * to process the words of type w = w0w2;
            * In case of dead-end vertices we limit ourselves in
            * processing the words of any type except of type w = w1w0
            * */
            if(vertex==initialState || isDeadEndVertex(vertex)){
                continue;
            }

            state = vertex;

            if(doesAcceptWord(word)){
                if(isConnectedWithQ0(vertex) && doesSubtreeContainFinalVertices(vertex)){
                    return true;
                }
            }

        }
        return false;
    }

    private int getCharIndex(String c) {
        return alphabet.indexOf(c);
    }

    private boolean isFinalState(String st){
        return finish.contains(st);
    }

    /*Checks if vertex is a dead-end vertex*/
    private boolean isDeadEndVertex(String vertex){
        //Checking if all elements of the list are equal
        for(String s : sigma.get(vertex)){
            if(!s.equals(sigma.get(0))){
                return false;
            }
        }
        return true;
    }

    /*Checks if there exists a walk between vertex and
    * initial vertex qo in this graph.
    * BFS is used for search*/
    private boolean isConnectedWithQ0(String vertex){

        //set all elements are false by default
        //boolean[] visited = new boolean[sigma.keySet().size()];
        HashMap<String, Boolean> visited = new HashMap<>();
        for(String s : sigma.keySet()){
            visited.put(s, false);
        }

        // Create a queue for BFS
        LinkedList<String> queue = new LinkedList<>();

        //MArk the current vertex as visited and add it to the queue
        visited.put(initialState,true);
        queue.add(initialState);

        String s,n;
        Iterator<String> i;
        while(queue.size()!=0){
            s=queue.poll();
            i = getAllAdjusent(s);
            while(i.hasNext()){

                n = i.next();
                if(n.equals(vertex)) return true;

                // Else continue the algorithm BFS
                if(!visited.get(n)){
                    visited.put(n, true);
                    queue.add(n);
                }
            }
        }

        //We reach this place in case when BFS is finished without finding vertex
        return false;
    }

    /*Checks if the subtree, which starts from the @param vertex,
    * contains at least one final vertex */
    private boolean doesSubtreeContainFinalVertices(String vertex){
        //set all elements are false by default
        //boolean[] visited = new boolean[sigma.keySet().size()];
        HashMap<String, Boolean> visited = new HashMap<>();
        for(String s : sigma.keySet()){
            visited.put(s, false);
        }

        // Create a queue for BFS
        LinkedList<String> queue = new LinkedList<>();

        //MArk the current vertex as visited and add it to the queue
        visited.put(vertex,true);
        queue.add(vertex);

        String s,n;
        Iterator<String> i;
        while(queue.size()!=0){
            s=queue.poll();
            i = getAllAdjusent(s);
            while(i.hasNext()){

                n = i.next();
                if(isFinalState(n)) return true;

                // Else continue the algorithm BFS
                if(!visited.get(n)){
                    visited.put(n, true);
                    queue.add(n);
                }
            }
        }

        //We reach this place in case when BFS is finished without finding vertex
        return false;
    }

    private Iterator<String> getAllAdjusent(String vertex){
        HashSet<String> set = new HashSet<>();
        for(String s : sigma.get(vertex)){
            if(!s.equals(error)) set.add(s);
        }
        return  set.iterator();
    }

}
