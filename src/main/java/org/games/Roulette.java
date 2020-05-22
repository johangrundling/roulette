package org.games;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.games.csv.PlayerCSVRepository;
import org.games.dto.Bet;
import org.games.dto.BetType;
import org.games.dto.Player;
import org.games.dto.PlayerQuickSearchCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Roulette {

    private List<Player> players = new ArrayList<>();

    private List<Bet> bets = new ArrayList<>();


    public Roulette() {
        System.out.println("Starting roulette");
        displayOptions();
        loadPlayerInfo();
        new Thread(this.new Wheel()).start();
    }

    public void play() {
        Scanner stdin = new Scanner(System.in);
        while (true) {
            String input = stdin.nextLine();

            if (input.equalsIgnoreCase("x")) {
                shutdown();
            } else if (input.equalsIgnoreCase("m")) {
                displayOptions();
            }else if (StringUtils.startsWith(input, "register")) {
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
            b.process(number);
            b.receipt();
        });

        bets.clear();
    }

    private void displayOptions() {
        StringBuilder options = new StringBuilder();
        options.append("menu options")
                .append("\nx - exits program and write players list to file")
                .append("\nm - display options")
                .append("\nregister")
                .append("\nremove")
                .append("");
        System.out.println(options.toString());
    }


    private void shutdown() {
        savePlayerInfo();
        System.exit(0);
    }

    private void loadPlayerInfo() {
         players = PlayerCSVRepository.readPlayersFile();
    }

    private void savePlayerInfo() {
        PlayerCSVRepository.writePlayersFile(players);
    }

    private void registerPlayer(String playerInfo) {
        String playerName = StringUtils.replace(
                StringUtils.trim(StringUtils.removeStart(playerInfo, "register")),
                " ",
                "_"
        );
        if (!StringUtils.isEmpty(playerName)) {
            players.add(Player.builder()
                    .name(playerName)
                    .quickCode(PlayerQuickSearchCode.generateQuickCode())
                    .build());
        }
    }

    private void removePlayer(String playerInfo) {
        String playerName = StringUtils.replace(
                StringUtils.trim(StringUtils.removeStart(playerInfo, "remove")),
                " ",
                "_"
        );
        Player search = Player.builder().name(playerName).build();
        players.remove(search);
    }

    private void displayPlayerInfo() {
        players.forEach(p -> p.info());
    }

    private void displayBets() {
        bets.forEach(b -> b.info());
    }

    private Player findByQuickCode(int quickCode){
        Optional<Player> op = players.stream().filter(p->p.getQuickCode() == quickCode).findFirst();
        if(op.isPresent()){
            return op.get();
        }
        return null;
    }

    private void placeBet(String bet) {
        String[] betdata = bet.split(" ");

        if (betdata.length == 3) {

            System.out.println(betdata[0]);

            Player player = NumberUtils.isDigits(betdata[0])?
                    findByQuickCode(Integer.valueOf(betdata[0]))
                    :players.get(players.indexOf(Player
                    .builder()
                    .name(betdata[0])
                    .build()));

            if (player == null) {
                System.out.println(" Unknown player " + betdata[0]);
                return;
            }

            bets.add(Bet.builder().player(player)
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
