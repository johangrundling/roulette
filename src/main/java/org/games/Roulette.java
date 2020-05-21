package org.games;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.games.dto.Bet;
import org.games.dto.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Roulette {

    private List<Player> players = new ArrayList<>();

    private List<Bet> bets = new ArrayList<>();


    public Roulette() {
        System.out.println("Starting roulette");
        new Thread(this.new Wheel()).start();
    }

    public void play() {
        Scanner stdin = new Scanner(System.in);
        while (true) {
            String input = stdin.nextLine();

            if (input.equalsIgnoreCase("x")) {
                System.exit(0);
            } else if (StringUtils.startsWith(input, "register")){
                System.out.println("add new player");
            } else if (StringUtils.startsWith(input, "remove")){
                System.out.println("remove player");
            } else {
                System.out.println("bet ");
            }
        }
    }


    public static void main(String[] args) {
        new Roulette().play();
    }

    public class Wheel implements Runnable {

        private static final long SPIN_DELAY = 30000;

        @lombok.SneakyThrows
        public void run() {
            while (true) {
                Thread.sleep(SPIN_DELAY);
                System.out.println("test ..." + RandomUtils.nextInt(1,36));
            }
        }
    }

}
