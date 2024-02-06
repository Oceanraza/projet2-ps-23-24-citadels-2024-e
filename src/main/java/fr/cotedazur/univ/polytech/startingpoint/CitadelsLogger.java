package fr.cotedazur.univ.polytech.startingpoint;

import java.util.logging.*;

public class CitadelsLogger {
    public static final Logger LOGGER = Logger.getLogger("CitadelsLogger");

    public static final String COLOR_BLUE = "\033[0;34m";
    public static final String COLOR_RED = "\033[0;31m";
    public static final String COLOR_GREEN = "\033[0;32m";
    public static final String COLOR_RESET = "\033[0m";

    private CitadelsLogger() {
        throw new IllegalStateException("CitadelsLogger is a utility class");
    }

    public static void setup() {
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
