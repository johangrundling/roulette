package org.games;

import java.util.Scanner;

public class Roulette {

    public Roulette() {
        System.out.println("Starting roulette");
        new Thread(this.new Wheel()).start();
    }

    public void play() {
        Scanner stdin = new Scanner(System.in);
        while (true) {
            String input = stdin.nextLine();
            System.out.println("test bet " + input);
            if (input.equalsIgnoreCase("x")) {
                System.exit(0);
            }
        }
    }


    public static void main(String[] args) {
        new Roulette().play();
    }

    public class Wheel implements Runnable {

        @lombok.SneakyThrows
        public void run() {
            while (true) {
                Thread.sleep(1000);
                System.out.println("test ...");
            }
        }
    }

}
