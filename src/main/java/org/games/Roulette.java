package org.games;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.games.dto.Bet;
import org.games.dto.BetType;
import org.games.dto.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Roulette {

    private List<Player> players = new ArrayList<>();

    private List<Bet> bets = new ArrayList<>();


    public Roulette() {
        System.out.println("Starting roulette");
        loadPlayerInfo();
        new Thread(this.new Wheel()).start();
    }

    public void play() {
        Scanner stdin = new Scanner(System.in);
        while (true) {
            String input = stdin.nextLine();

            if (input.equalsIgnoreCase("x")) {
                shutdown();
            } else if (StringUtils.startsWith(input, "register")) {
                registerPlayer(input);
            } else if (StringUtils.startsWith(input, "remove")) {
                removePlayer(input);
            } else if (StringUtils.startsWith(input, "info")) {
                displayPlayerInfo();
            } else if (StringUtils.startsWith(input, "bets")) {
                displayBets();
            } else {
                placeBet(input);
            }
        }
    }

    private void processSpin(final int number) {
        System.out.println("Number " + number);
        System.out.println("-----------------");
        bets.forEach(b -> {
            b.info();
        });

        bets.clear();
    }

    private void shutdown() {
        savePlayerInfo();
        System.exit(0);
    }

    private void loadPlayerInfo() {

    }

    private void savePlayerInfo() {

    }

    private void registerPlayer(String playerInfo) {
        String playerName = StringUtils.trim(StringUtils.removeStart(playerInfo, "register"));
        if (!StringUtils.isEmpty(playerName)) {
            players.add(Player.builder()
                    .name(playerName)
                    .build());
        }
    }

    private void removePlayer(String playerInfo) {
        String playerName = StringUtils.trim(StringUtils.removeStart(playerInfo, "remove"));
        Player search = Player.builder().name(playerName).build();
        players.remove(search);
    }

    private void displayPlayerInfo() {
        players.forEach(p -> p.info());
    }

    private void displayBets() {
        bets.forEach(b -> b.info());
    }

    private void placeBet(String bet) {
        String[] betdata = bet.split(" ");
        if (betdata.length == 3) {

            Player player = players.get(players.indexOf(Player
                    .builder()
                    .name(betdata[0])
                    .build()));

            if(player ==null){
                System.out.println(" Unknown player " + betdata[0]);
                return;
            }

            bets.add(Bet.builder().player(players.get(players.indexOf(Player
                    .builder()
                    .name(betdata[0])
                    .build())))
                    .betType(BetType.NUMBER)
                    .amountBet(BigDecimal.TEN)
                    .build());
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
                processSpin(RandomUtils.nextInt(1, 36));
            }
        }
    }

}
