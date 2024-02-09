package fr.cotedazur.univ.polytech.startingpoint.exception;

import java.io.IOException;

public class CSVWriteException extends IOException {
    public CSVWriteException(String message) {
        super(message);
    }
}
