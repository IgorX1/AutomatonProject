package com.devx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.io.BufferedInputStream;

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
                state = sigma.get(state).get(getCharIndex(String.valueOf(cur)));
            }catch (ArrayIndexOutOfBoundsException exc){
                return false;
            }
        }

        if(isFinalState(state)) return true;

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
    * initial vertex qo in this graph*/
    private boolean isConnectedWithq0(String vertex){

        return false;
    }

}
