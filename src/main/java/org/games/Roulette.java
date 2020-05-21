package org.games;

import org.apache.commons.lang3.RandomUtils;
import org.games.dto.Bet;
import org.games.dto.Player;

import java.util.Scanner;

public class Roulette {

    public Roulette() {
        System.out.println("Starting roulette");

        Player p = Player.builder()
                .name("test")
                .build();

        Bet b = Bet.builder()
                .player(p)
                .amount(23D)
                .amountWon(34.3D)
                .build();

        System.out.println(" player " + p);
        System.out.println(" bet " + b);

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
                System.out.println("test ..." + RandomUtils.nextInt(1,36));
            }
        }
    }

}
