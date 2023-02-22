package de.peterkossek.pinglog;

import org.apache.commons.cli.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PingLog {

    public static final String DEFAULT_INTERVAL = "5";

    public static void main(String[] args) throws InterruptedException {
        Options options = new Options();
        options.addRequiredOption("h", "host", true, "Host to ping");
        options.addOption("i", "interval", true, "Interval in seconds, optional, default is "+DEFAULT_INTERVAL);
        options.addOption("f", "file", true, "Output file path, optional");
        options.addOption("?", "help", false, "Print this helpt text");
        if (args.length == 0) {
            printUsage(options);
            return;
        }
        DefaultParser parser = new DefaultParser();
        CommandLine cli = null;
        try {
            cli = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            printUsage(options);
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int interval = Integer.parseInt(cli.getOptionValue("i", DEFAULT_INTERVAL)) * 1000;
        String host = cli.getOptionValue("h");
        BufferedWriter bw = null;
        if (cli.hasOption("f")) {
            String filePath = cli.getOptionValue("f");
            try {
                bw = new BufferedWriter(new FileWriter(filePath, Charset.forName("UTF-8")));
            } catch (IOException e) {
                System.err.println("Could not write to file "+filePath);
            }
        }
        while (true) {
            String timestamp = sdf.format(new Date());
            String message = "";
            try {
                boolean reachable = InetAddress.getByName(host).isReachable(200);
                message = timestamp + " " + host + " " + reachable;
            } catch (IOException e) {
                message = timestamp + " " + host + " error";
            }
            System.out.println(message);
            if (bw != null) {
                try {
                    bw.append(message);
                    bw.newLine();
                } catch (IOException e) {
                    System.err.println("Could not write to file!");
                }
            }
            Thread.sleep(interval);
        }
    }

    private static void printUsage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java "+PingLog.class.getSimpleName(), options);
    }
}