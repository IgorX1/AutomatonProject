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

    /*Current state of the automaton. Initial value is 0 (= q0)*/
    private String state = "0";

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
                //boolean containskey = sigma.containsKey("0");
                //LinkedList<String> st = sigma.get(state);
                //String st1 = st.get(getCharIndex(String.valueOf(cur)));
                state = sigma.get(state).get(getCharIndex(String.valueOf(cur)));

            }catch (ArrayIndexOutOfBoundsException exc){
                return false;
            }
        }

        if(isFinalState(state)) return true;

        return false;
    }

    private int getCharIndex(String c) {
        return alphabet.indexOf(c);
    }

    private boolean isFinalState(String st){
        return finish.contains(st);
    }

}
