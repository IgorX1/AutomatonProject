package com.devx;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println("Konobas Igor TTP-31");
        Automaton automaton = new Automaton("automaton.txt");
        String w0 = getWord();
        if(w0==null) return;
        System.out.println(automaton.doesAcceptWordOfW1W0W2Structure(w0) ? "YES" : "NO");

    }

    public static String getWord(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the word:");
        String s;
        try{
            s = scanner.nextLine();
        }catch (NoSuchElementException e){
            System.out.println("Wrong input");
            return null;
        }
        return s;
    }
}
