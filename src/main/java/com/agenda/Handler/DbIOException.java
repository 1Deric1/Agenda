package com.agenda.Handler;

import java.io.IOException;

public class DbIOException extends IOException {
    public DbIOException(String message) {
        super(message);
    }
}
