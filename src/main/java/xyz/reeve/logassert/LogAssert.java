package xyz.reeve.logassert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogAssert {
    private String logString = null;

    public LogAssert() {
        clearLog();
    }

    public void assertLogsExist(String[] assertPatterns) throws Exception {
        logString = getLogs();
        for (String patternString:assertPatterns) {
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(logString);
            if(!matcher.find()) {
                throw new Exception("Asserted pattern [" + patternString + "] did not exist in logs");
            }
        }
    }

    public void assertLogsCustom() throws Exception {
        logString = getLogs();
        customHandler(logString);
    }

    public void customHandler(String logs) throws Exception {
        throw new Exception("No customHandler() override specified for LogAsser");
    }

    private void clearLog(){
        try {
            Process process = new ProcessBuilder()
                    .command("logcat", "-c")
                    .redirectErrorStream(true)
                    .start();
        } catch (IOException e) {
        }
    }

    private String getLogs(){
        Process logcat;
        final StringBuilder log = new StringBuilder();
        try {
            logcat = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});
            BufferedReader br = new BufferedReader(new InputStreamReader(logcat.getInputStream()),4*1024);
            String line;
            String separator = System.getProperty("line.separator");
            while ((line = br.readLine()) != null) {
                log.append(line);
                log.append(separator);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return log.toString();
    }
}
