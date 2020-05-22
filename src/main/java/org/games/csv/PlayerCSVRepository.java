package org.games.csv;

import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.games.dto.Player;
import org.games.dto.PlayerQuickSearchCode;

import java.io.BufferedWriter;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class PlayerCSVRepository {

    private static final String SAMPLE_CSV_FILE_PATH = "./players.csv";

    @SneakyThrows
    public static List<Player> readPlayersFile() {
        List<Player> players = new ArrayList<>();
        if (Files.exists(Paths.get(SAMPLE_CSV_FILE_PATH))) {
            try {
                try (
                        Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
                        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
                ) {
                    for (CSVRecord csvRecord : csvParser) {
                        String name = csvRecord.get(0);
                        String totalBets = "0.0";
                        String totalWinnings = "0.0";
                        if (csvRecord.size() > 1) {
                            totalBets = csvRecord.get(1);
                            totalWinnings = csvRecord.get(2);
                        }
                        players.add(Player.builder()
                                .name(name)
                                .quickCode(PlayerQuickSearchCode.generateQuickCode())
                                .totalAmountBet(new BigDecimal(totalBets))
                                .totalAmountWon(new BigDecimal(totalWinnings))
                                .build());
                    }
                }
            } catch (Exception x) {
                x.printStackTrace();
            }
        } else {
            System.out.println("Players file does not exists. Skipping.");
        }
        return players;
    }


    @SneakyThrows
    public static void writePlayersFile(List<Player> players) {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE_PATH));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        ) {
            for (Player p : players) {
                csvPrinter.printRecord(p.getName()
                        , p.getTotalAmountBet()
                        , p.getTotalAmountWon());
            }

            csvPrinter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
