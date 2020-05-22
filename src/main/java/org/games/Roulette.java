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
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class Roulette {

    private List<Player> players = new ArrayList<>();

    //this list should be synchronized but that is beyond the scope of this exercise
    private List<Bet> bets = new ArrayList<>();


    public Roulette() {
        System.out.println("Starting roulette");
        displayOptions();
        loadPlayerInfo();
        displayPlayerInfo();
        new Thread(this.new Wheel()).start();
    }

    public void play() {
        Scanner stdin = new Scanner(System.in);
        while (true) {
            System.out.println("\n\nPlace your bet: ");
            String input = stdin.nextLine();

            if (input.equalsIgnoreCase("x")) {
                shutdown();
            } else if (input.equalsIgnoreCase("m")) {
                displayOptions();
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
        System.out.format("%n%-20s %-7s %10s %10s %n",
                "Player"
                , "Bet"
                , "Outcome"
                , "Winnings");
        System.out.println("------");

        bets.forEach(b -> {
            b.process(number);
            b.receipt();
        });

        bets.clear();

        displayPlayerInfo();
    }

    private void displayOptions() {
        StringBuilder options = new StringBuilder();
        options.append("menu options")
                .append("\nx - exits program and write players list to file")
                .append("\nm - display options")
                .append("\nregister - Register a new player. Must start with character. Spaces are replaces by _")
                .append("\n      usage: register <player name> .")
                .append("\n      exmaple: register johan gamble . Registers player johan_gamble")
                .append("\nremove - removes a player from the list")
                .append("\n      usage: remove <player name> .")
                .append("\n      exmaple: remove johan gamble . Removes player johan_gamble")
                .append("\ninfo - display player list")
                .append("\nbets - display current bets")
                .append("");
        System.out.println(options.toString());
    }


    private void shutdown() {
        //this should really cater for existing bets before exiting.
        //maybe if this ever gets productionized.
        //beyond the scope of this exercise
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

        String playerName = extractPlayerName(playerInfo, "register");

        if (!Objects.isNull(findPlayer(playerName))) {
            System.out.println("Player with similar name already exists. Player name: " + playerName);
            return;
        }

        if (!StringUtils.isEmpty(playerName)
                && StringUtils.isAlpha(StringUtils.substring(playerName, 0, 1))
                && playerName.length() >= 3
                && playerName.length() <= 20) {
            players.add(Player.builder()
                    .name(playerName)
                    .quickCode(PlayerQuickSearchCode.generateQuickCode())
                    .build());
            System.out.println("player " + playerName + " successfully registered");
        } else {
            System.out.println("The players name must start with a character and must be between 3 and 20 characters long.");
        }
    }

    private void removePlayer(String playerInfo) {
        String playerName = extractPlayerName(playerInfo, "remove");
        Player search = Player.builder().name(playerName).build();
        boolean result = players.remove(search);
        if (result) {
            System.out.println("Player " + playerName + " removed");
        } else {
            System.out.println("Player " + playerName + " could not be found");
        }
    }

    private String extractPlayerName(String input, String remove) {
        return StringUtils.replace(
                StringUtils.trim(StringUtils.removeStart(input, remove)),
                " ",
                "_"
        );
    }

    private void displayPlayerInfo() {
        System.out.format("%n%-20s %-7s %10s %10s %n",
                "Player"
                , "Quick"
                , "Tot Won"
                , "Tot Bet");
        System.out.println("------");
        players.forEach(p -> p.info());
    }

    private void displayBets() {
        System.out.format("%n%-20s %-7s %10s %n",
                "Player"
                , "Bet"
                , "Amount");
        System.out.println("------");
        bets.forEach(b -> b.info());
    }


    private Player findPlayer(String info) {
        return NumberUtils.isDigits(info) ?
                findByQuickCode(Integer.valueOf(info))
                : findByName(info);
    }

    private Player findByQuickCode(int quickCode) {
        Optional<Player> op = players.stream().filter(p -> p.getQuickCode() == quickCode).findFirst();
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    private Player findByName(String name) {
        Player search = Player
                .builder()
                .name(name)
                .build();
        int index = players.indexOf(search);
        return index < 0 ? null : players.get(index);
    }

    private void placeBet(String bet) {
        String[] betdata = bet.split(" ");

        if (betdata.length != 3) {
            System.out.println("Invalid bet information");
            System.out.println("Usage: <name or quick code of player> <bet on 1-36 , even or odd> <amount wagered>");
            return;
        }


        Player player = findPlayer(betdata[0]);

        if (Objects.isNull(player)) {
            System.out.println(" Unknown player " + betdata[0]);
            return;
        }

        String betOn = betdata[1];

        if ((!NumberUtils.isDigits(betOn) || (NumberUtils.isDigits(betOn)
                && (Integer.parseInt(betOn) < 1
                    || Integer.parseInt(betOn) > 36)))
                && !"even".equalsIgnoreCase(betOn)
                && !"odd".equalsIgnoreCase(betOn)) {
            System.out.println("Invalid bet. Valid options: 1-36, even , odd");
            return;
        }

        String amount = betdata[2];

        if(!NumberUtils.isCreatable(amount)){
            System.out.println("Invalid bet. The 3rd value must be an amount");
            return;
        }

        BetType type = NumberUtils.isDigits(betOn) ? BetType.NUMBER
                : BetType.ODD_EVEN;

        int numberBetOn = 0;
        boolean oddOrEven = false;

        if(type == BetType.NUMBER){
            numberBetOn = NumberUtils.toInt(betOn);
        }else{
            oddOrEven = StringUtils.equalsIgnoreCase(betOn,"even");
        }


        bets.add(Bet.builder().player(player)
                .betType(type)
                .numberBetOn(numberBetOn)
                .even(oddOrEven)
                .amountBet(NumberUtils.createBigDecimal(amount))
                .build());

    }

    public static void main(String[] args) {
        new Roulette().play();
    }

    public class Wheel implements Runnable {

        private static final long SPIN_DELAY = 30*1000l;

        @lombok.SneakyThrows
        public void run() {
            while (true) {
                Thread.sleep(SPIN_DELAY);
                processSpin(RandomUtils.nextInt(1, 36));
            }
        }
    }

}
