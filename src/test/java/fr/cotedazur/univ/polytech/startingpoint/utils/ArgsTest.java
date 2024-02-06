package fr.cotedazur.univ.polytech.startingpoint.utils;

import com.beust.jcommander.JCommander;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArgsTest {
    Args commandLineArgs;
    String[] args2x1000games;
    String[] args1game;

    @BeforeEach
    void setUp() {
        commandLineArgs = new Args();
        args2x1000games = new String[] {"--2thousands"};
        args1game = new String[] {"--demo"};
    }

    // Launch 2 x 1000 tests with -Dexec.args="--2thousands"
    @Test
    void launch2x1000GamesTest() {
        JCommander.newBuilder()
                .addObject(commandLineArgs)
                .build()
                .parse(args2x1000games);

        assertTrue(commandLineArgs.is2Thousands());
        assertFalse(commandLineArgs.isDemo());
    }

    // Launch 2000 tests with -Dexec.args="--demo"
    @Test
    void launch1GameTest() {
        JCommander.newBuilder()
                .addObject(commandLineArgs)
                .build()
                .parse(args1game);

        assertTrue(commandLineArgs.isDemo());
        assertFalse(commandLineArgs.is2Thousands());
    }
}
