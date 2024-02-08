package fr.cotedazur.univ.polytech.startingpoint.utils;

import com.beust.jcommander.JCommander;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArgsTest {
    Args commandLineArgs;
    String[] args2x1000games;
    String[] args1game;
    String[] argsCsvGame;

    @BeforeEach
    void setUp() {
        commandLineArgs = new Args();
        args2x1000games = new String[] {"--2thousands"};
        args1game = new String[] {"--demo"};
        argsCsvGame = new String[]{"--csv"};
    }

    // Launch 2 x 1000 tests with -Dexec.args="--2thousands"
    @Test
    void launch2x1000GamesTest() {
        JCommander.newBuilder()
                .addObject(commandLineArgs)
                .build()
                .parse(args2x1000games);

        assertEquals(Args.ArgsEnum.TWOTHOUSANDS, commandLineArgs.getCurrentMode());
    }

    // Launch 2000 tests with -Dexec.args="--demo"
    @Test
    void launch1GameTest() {
        JCommander.newBuilder()
                .addObject(commandLineArgs)
                .build()
                .parse(args1game);

        assertEquals(Args.ArgsEnum.DEMO, commandLineArgs.getCurrentMode());
    }

    @Test
    void launchCsvGameTest() {
        JCommander.newBuilder()
                .addObject(commandLineArgs)
                .build()
                .parse(argsCsvGame);

        assertEquals(Args.ArgsEnum.CSV, commandLineArgs.getCurrentMode());
    }
}
