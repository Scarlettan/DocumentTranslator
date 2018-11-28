/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package documenttranslator.services;

import documenttranslator.Prefs;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joni.jinon
 */
public final class Spy {

    public static enum Type {
        SILENT, NORMAL, VERBOSE
    };

    public static final String DIR = Prefs.DIR_APP + "logs" + File.separator;
    public static final String DIR_JOB_LOG = Spy.DIR + "jobs" + File.separator;
    public static final String FILE_MASTER_LOG = Spy.DIR + "master.log";
    public static final boolean WRITE_FILES = true;
    public static final Spy.Type LOGGING_TYPE = Spy.Type.VERBOSE;

    private String logEntryType = "MESSAGE";
    private String logEntryMessage = "Hewwo";
    private boolean writeToConsole = true;
    private boolean writeToMaster = false;
    private boolean writeToFile = false;
    private String writeToFileName;

    private void setLogMessage(String messageType, String message) {
        this.logEntryType = messageType;
        this.logEntryMessage = message;
        this.go();
    }

    private void writeEntryToFile() {

        if (Spy.WRITE_FILES || Spy.LOGGING_TYPE != Spy.Type.SILENT && !this.writeToFileName.isEmpty()) {

            try {

                File file = new File(this.writeToFileName);

                // if file doesnt exist, then create it
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }

                List<String> s = new ArrayList<>();
                s.add("["
                        + Spy.padRight(LocalDateTime.now().toString(), 23) + "] "
                        + Spy.padRight(this.logEntryType, 10) + ": "
                        + this.logEntryMessage + System.lineSeparator());
                Files.write(file.toPath(), s, StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE);

            } catch (IOException e) {

                Spy.log().toConsole().asException(e);

            }

        }
    }

    private void go() {

        if (Spy.LOGGING_TYPE == Spy.Type.SILENT) {
            return;
        }

        if (this.writeToConsole) {
            this.logEntryMessage = this.logEntryMessage.replace("\\", "/");
            switch (Spy.LOGGING_TYPE) {
                case NORMAL:
                    System.out.println(this.logEntryMessage);
                    break;
                case VERBOSE:
                    System.out.println("["
                            + Spy.padRight(LocalDateTime.now().toString(), 23) + "] "
                            + Spy.padRight(this.logEntryType, 10) + ": "
                            + this.logEntryMessage);
                    break;
            }
        }

        if (this.writeToMaster || this.writeToFile) {
            this.writeEntryToFile();
        }

    }

    public Spy asInfo(String text) {
        this.setLogMessage("INFO", text);
        return this;
    }

    public Spy asException(Exception e) {
        switch (Spy.LOGGING_TYPE) {
            case NORMAL:
                this.setLogMessage("EXCEPTION", e.getMessage());
                break;
            case VERBOSE:
                this.setLogMessage("EXCEPTION", e.toString());
                break;
        }
        return this;
    }

    public Spy asError(String text) {
        this.setLogMessage("ERROR", text);
        return this;
    }

    public Spy toConsole() {
        this.writeToConsole = true;
        return this;
    }

    public Spy toMaster() {
        this.writeToFileName = Spy.FILE_MASTER_LOG;
        this.writeToMaster = true;
        return this;
    }

    public Spy toJobFile(String filename) {
        this.writeToFileName = Spy.DIR_JOB_LOG + filename + ".log";
        this.writeToFile = true;
        return this;
    }

    public static String padRight(String text, int pad) {
        return String.format("%1$-" + pad + "s", text);
    }

    public static synchronized Spy log() {
        return new Spy();
    }

}
