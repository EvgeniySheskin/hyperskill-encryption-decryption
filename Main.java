package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

enum Methods {
    SHIFT, UNICODE
}

// an abstract class, implementing common interface and some methods, common for both encryption - decryption operations
abstract class Processor {
// a set of parameters, used as settings

    private Settings settings;

// a constructor, defining settings from Settings object

    Processor(Settings settings) {
        this.settings = settings;
    }

// a method for result output, according with the desired concept - to standard output or to the specified file

    public Settings getSettings() {
        return this.settings;
    }

    abstract String process();

}
// a specific class for UNICODE encryption process exactly, with overridden "process" method
class UnicodeEncryptor extends Processor {

    public UnicodeEncryptor(Settings settings) {
        super(settings);
    }

    public String process() {
        String result = "";
        String message = super.getSettings().getMessage();
        int key = super.getSettings().getKey();
        for (int i = 0; i < message.length(); i++) {
            result += (char)((int)message.charAt(i) + key);
        }
        return result;
    }
}
// a specific class for SHIFT encryption process exactly, with overridden "process" method
class ShiftEncryptor extends Processor {

    public ShiftEncryptor(Settings settings) {
        super(settings);
    }

    public String process() {
        String result = "";
        String message = super.getSettings().getMessage();
        int key = super.getSettings().getKey();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < message.length(); i++) {
            boolean isCyphered = false;
            for (int j = 0; j < alphabet.length(); j++) {
                if (message.charAt(i) == alphabet.charAt(j)) {
                    if (j + key > alphabet.length()) {
                        result += alphabet.charAt(j + key - alphabet.length());
                    } else {
                        result += alphabet.charAt(j + key);
                    }
                    isCyphered = true;
                } else if (message.charAt(i) == alphabet.toUpperCase().charAt(j)) {
                    if (j + key > alphabet.length()) {
                        result += alphabet.toUpperCase().charAt(j + key - alphabet.length());
                    } else {
                        result += alphabet.toUpperCase().charAt(j + key);
                    }
                    isCyphered = true;
                }
            }
            if (!isCyphered) result += message.charAt(i);
        }
        return result;
    }
}
// a specific class for UNICODE decryption process exactly, with overridden "process" method
class UnicodeDecryptor extends Processor {

    public UnicodeDecryptor(Settings settings) {
        super(settings);
    }

    public String process() {
        String result = "";
        String message = super.getSettings().getMessage();
        int key = super.getSettings().getKey();
        for (int i = 0; i < message.length(); i++) {
            result += (char)((int)message.charAt(i) - key);
        }
        return result;
    }
}
// a specific class for SHIFT decryption process exactly, with overridden "process" method
class ShiftDecryptor extends Processor {

    public ShiftDecryptor(Settings settings) {
        super(settings);
    }

    public String process() {
        String result = "";
        String message = super.getSettings().getMessage();
        int key = super.getSettings().getKey();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < message.length(); i++) {
            boolean isDecrypted = false;
            for (int j = 0; j < alphabet.length(); j++) {
                if (message.charAt(i) == alphabet.charAt(j)) {
                    if (j - key < 0) {
                        result += alphabet.charAt(j - key + alphabet.length());
                    } else {
                        result += alphabet.charAt(j - key);
                    }
                    isDecrypted = true;
                } else if (message.charAt(i) == alphabet.toUpperCase().charAt(j)) {
                    if (j + key > alphabet.length()) {
                        result += alphabet.toUpperCase().charAt(j - key + alphabet.length());
                    } else {
                        result += alphabet.toUpperCase().charAt(j - key);
                    }
                    isDecrypted = true;
                }
            }
            if (!isDecrypted) result += message.charAt(i);
        }
        return result;
    }
}
// a factory idiom for Processor type object creation
class ProcessorFactory {

    public Processor CreateProcessor(Methods type, String mode, Settings settings) {
        switch (type) {
            case SHIFT:
                if (mode.equalsIgnoreCase("enc")) {
                    return new ShiftEncryptor(settings);
                } else if (mode.equalsIgnoreCase("dec")) {
                    return new ShiftDecryptor(settings);
                } else return null;
            case UNICODE:
                if (mode.equalsIgnoreCase("enc")) {
                    return new UnicodeEncryptor(settings);
                } else if (mode.equalsIgnoreCase("dec")) {
                    return new UnicodeDecryptor(settings);
                } else return null;
            default:
                System.out.println("Error: Unknown command");
                return null;
        }
    }
}
// its handy to have a class with commonly used settings
class Settings {

    private int key;
    private String message;
    private String mode;
    private String inputFile;
    private String outputFile;
    private Methods method;
    private boolean printToStd;
    private String processedMessage;

    // constructor, initializing settings with some default values
    public Settings() {

        this.key = 0;
        this.message = "";
        this.mode = "enc";
        this.inputFile = null;
        this.outputFile  =null;
        this.printToStd = true;
        this.method = Methods.SHIFT;
        this.processedMessage = "";
    }

    public int getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public Methods getMethod() {
        return method;
    }

    public String getMode() {
        return mode;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMethod(Methods method) {
        this.method = method;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public void setPrintToStd(boolean printToStd) {
        this.printToStd = printToStd;
    }

    public void setProcessedMessage(String processedMessage) {
        this.processedMessage = processedMessage;
    }
    // a method for input message import - from file or from "data" variable
    public void inputMessageFromFile() {
        if (this.inputFile != null && this.message.isEmpty()) {
            File file = new File(this.inputFile);
            try (Scanner input = new Scanner(file)) {
                if (input.hasNext()) {
                    this.message = input.nextLine();
                } else {
                    System.out.println("Error: Specified input file is empty.");
                }
            } catch (RuntimeException | FileNotFoundException e) {
                System.out.println("Error: Input file not found or cannot be read.");
            }
        }
    }
    public void outputResult() {
        if (this.printToStd) {
            System.out.println(this.processedMessage);
        } else {
            File file = new File(this.outputFile);
            try (PrintWriter printer = new PrintWriter(file)) {
                printer.println(this.processedMessage);
            } catch (RuntimeException | FileNotFoundException e) {
                System.out.println("Error: Output file not found or cannot be accessed.");
            }
        }
    }
}

class ParametersProcessing {

    public static Settings processParameters (String[] args) {

        Settings settings = new Settings();

        for (int i = 0; i < args.length - 1; i += 2) {
            switch(args[i]) {
                case "-mode":
                    try {
                        settings.setMode(args[i + 1]);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Error: No argument specified for \"-mode\".");
                    }
                    break;
                case "-key":
                    try {
                        settings.setKey(Integer.parseInt(args[i + 1]));
                    } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
                        System.out.println("Error: No argument specified for \"-key\" or it has illegal type.");
                    }
                    break;
                case "-data":
                    try {
                        settings.setMessage(args[i + 1]);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Error: No argument specified for \"-data\".");
                    }
                    break;
                case "-in":
                    try {
                        settings.setInputFile(args[i + 1]);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Error: No argument specified for \"-in\".");
                    }
                    break;
                case "-out":
                    try {
                        settings.setOutputFile(args[i + 1]);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Error: No argument specified for \"-out\".");
                    }
                    break;
                case "-alg":
                    try {
                        String temp = args[i + 1];
                        if (temp.equalsIgnoreCase("shift")) {
                            settings.setMethod(Methods.SHIFT);
                        } else if (temp.equalsIgnoreCase("unicode")) {
                            settings.setMethod(Methods.UNICODE);
                        } else {
                            System.out.println("Error: Illegal argument specified for \"-alg\".");
                        }
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Error: No argument specified for \"-out\".");
                    }
            }
        }
        settings.inputMessageFromFile();
        if (settings.getOutputFile() != null) settings.setPrintToStd(false);
        return settings;
    }
}

public class Main {
    public static void main(String[] args) {

        Settings settings = ParametersProcessing.processParameters(args);
        ProcessorFactory factory = new ProcessorFactory();
        Processor processor = factory.CreateProcessor(settings.getMethod(),settings.getMode(), settings);
        settings.setProcessedMessage(processor.process());
        settings.outputResult();
    }
}
