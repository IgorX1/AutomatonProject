package com.devx;

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
    private Map<Integer , LinkedList<String>> sigma = new HashMap<Integer, LinkedList<String>>();

    /*The alphabet, which might be accepted by the automaton*/
    private LinkedList<Character> alphabet = new LinkedList<>();

    /*The set of final states of the automaton*/
    private LinkedList<Integer> finish = new LinkedList<>();

    public Automaton(String path){


    }
}
