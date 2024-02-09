package fr.cotedazur.univ.polytech.startingpoint.utils;

import java.util.logging.*;

public class CitadelsLogger {
    /**
     * Logger utilisé pour enregistrer les informations de débogage et les erreurs.
     * Il est initialisé avec le nom "Citadels Logger".
     */
    public static final Logger LOGGER = Logger.getLogger("Citadels Logger");
    public static final Level CSV_OR_THOUSAND = new CsvOrThousandLogLevel("MY_CUSTOM_LEVEL", Level.WARNING.intValue() + 1);


    public static final String COLOR_BLUE = "\033[0;34m";
    public static final String COLOR_RED = "\033[0;31m";
    public static final String COLOR_GREEN = "\033[0;32m";
    public static final String COLOR_PURPLE = "\033[0;35m";
    public static final String COLOR_RESET = "\033[0m";

    private CitadelsLogger() {
        throw new IllegalStateException("This logger is a utility class");
    }

    public static void setupCsvOr2Thousand() {
        LOGGER.setLevel(CSV_OR_THOUSAND);

        // Remove all parent handlers
        for (Handler iHandler : LOGGER.getParent().getHandlers()) {
            LOGGER.getParent().removeHandler(iHandler);
        }

        // Set the basic console handler
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter() {
            @Override
            public String format(LogRecord inputRecord) {
                return inputRecord.getMessage() + "\n";
            }
        });
        LOGGER.addHandler(consoleHandler);
    }

    public static void setupDemo() {
        LOGGER.setLevel(Level.INFO);

        // Remove all parent handlers
        for (Handler iHandler : LOGGER.getParent().getHandlers()) {
            LOGGER.getParent().removeHandler(iHandler);
        }

        // Set the basic console handler
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter() {
            @Override
            public String format(LogRecord inputRecord) {
                return inputRecord.getMessage() + "\n";
            }
        });
        LOGGER.addHandler(consoleHandler);
    }

    public static void setGlobalLogLevel(Level level) {
        LOGGER.setLevel(level);
    }
}
