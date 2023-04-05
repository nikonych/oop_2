package org.example;

import org.example.enums.BetType;
import org.example.model.Horse;
import org.example.model.User;

import java.util.List;

public class Decor {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    static void printBlue(String text){
        System.out.println(ANSI_BLUE + text + ANSI_RESET);
    }

    static void printGreen(String text){
        System.out.println(ANSI_GREEN + text + ANSI_RESET);
    }

    static void printRed(String text){
        System.out.println(ANSI_RED + text + ANSI_RESET);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static Integer printHorses(List<Horse> horses, BetType betType){
        Integer count = horses.stream().mapToInt(Horse::getBetCount).sum();
        if (betType == BetType.WIN) {
            for (int i = 0; i < horses.size(); i++) {
                System.out.printf(ANSI_GREEN + "%2s %4s %5sx\n" + ANSI_RESET, i, horses.get(i).getName(), round(95/ ((double) horses.get(i).getBetCount()/count * 100), 2));
            }
        } else if (betType == BetType.PLACE) {
            for (int i = 0; i < horses.size(); i++) {
                System.out.printf(ANSI_GREEN + "%2s %4s %5sx\n" + ANSI_RESET, i, horses.get(i).getName(),  round((95/( (((double) count - horses.get(i).getBetCount())/count * 100))),2));
            }
        }
        return count;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


}
