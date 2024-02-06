package fr.cotedazur.univ.polytech.startingpoint;

import java.util.logging.*;

public class CitadelsLogger {
    public static final Logger LOGGER = Logger.getLogger("CitadelsLogger");

    private CitadelsLogger() {
        throw new IllegalStateException("CitadelsLogger is a utility class");
    }

    public static void setup() {
        LOGGER.setLevel(Level.INFO);

        // Remove all parent handlers
        for (Handler iHandler : LOGGER.getParent().getHandlers()) {
            LOGGER.getParent().removeHandler(iHandler);
        }

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
