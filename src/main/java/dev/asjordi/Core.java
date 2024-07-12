package dev.asjordi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jordi <ejordi.ayala@gmail.com>
 */
public class Core {
    
    private Path path;
    private List<String> lines;
    
    private long numberOfLines;
    private long numberOfWords;
    private long numberOfCharacters;
    private long numberOfBytes;
    private long maxLineLength;
    private final Map<String, Integer> wordCountMap;
    private String highestRepeatedWord;
    private long highestRepeatedWordCount;

    public Core(Path path) {
        this.path = path;
        this.lines = FileUtil.readAllLines(this.path);
        this.numberOfLines = countLines();
        this.wordCountMap = new HashMap<>();
        this.calculateBytes();
        this.calculate();
    }

    public Core(List<String> lines) {
        this.lines = lines;
        this.numberOfLines = countLines();
        this.wordCountMap = new HashMap<>();
        this.calculate();
    }

    public long getNumberOfLines() {
        return numberOfLines;
    }

    public long getNumberOfWords() {
        return numberOfWords;
    }

    public long getNumberOfCharacters() {
        return numberOfCharacters;
    }

    public long getNumberOfBytes() {
        return numberOfBytes;
    }

    public long getMaxLineLength() {
        return maxLineLength;
    }

    public Map<String, Integer> getWordCountMap() {
        return wordCountMap;
    }

    public String getHighestRepeatedWord() {
        return highestRepeatedWord;
    }

    public long getHighestRepeatedWordCount() {
        return highestRepeatedWordCount;
    }

    public String getFileName() {
        return this.path.getFileName().toString();
    }

    public String getDefaultOutput() {
        return this.numberOfLines + " "
                + this.numberOfWords + " "
                + this.numberOfCharacters;
    }

    public static String getHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usage: java -jar javawc-1.0-SNAPSHOT.jar [OPTION]... [FILE]...\n");
        sb.append("Print newline, word, and byte counts for each FILE, and a total line if more than one FILE is specified.\n");
        sb.append("With no FILE, or when FILE is -, read standard input.\n\n");
        sb.append("The options below may be used to select which counts are printed, always in the following order: newline, word, character, byte, maximum line length.\n");
        sb.append("  -c                     print the byte counts\n");
        sb.append("  -m                     print the character counts\n");
        sb.append("  -l                     print the newline counts\n");
        sb.append("  -L                     print the length of the longest line\n");
        sb.append("  -w                     print the word counts\n");
        sb.append("  -r                     print the most repeated word in the file\n");
        sb.append("  -rr                    print the number of times the most repeated word is repeated\n");
        sb.append("  --help                 display this help and exit\n");
        sb.append("  --version              output version information and exit\n");
        return sb.toString();
    }

    public static String getVersion() {
        StringBuilder sb = new StringBuilder();
        sb.append("javawc utils v1.0\n");
        sb.append("Written by Jordi Ayala <@ASJordi>");
        return sb.toString();
    }

    private int countLines() {
        if (this.lines.isEmpty()) return 0;

        int lineCount = this.lines.size();

        if (!lines.get(lines.size() - 1).isEmpty()) lineCount++;

        return lineCount;
    }

    private void calculateBytes() {
        try {
            this.numberOfBytes = Files.size(this.path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void calculate() {
        Pattern pattern = Pattern.compile("\\S+");
        for (String line : lines) {
            numberOfCharacters += line.length();

            Matcher m = pattern.matcher(line);
            while (m.find()) {
                numberOfWords++;
                String word = m.group().replaceAll("[^A-Za-z0-9]","");
                wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            }

            if (line.length() > maxLineLength) maxLineLength = line.length();
        }

        numberOfCharacters += this.numberOfLines;

        var maxEntry = wordCountMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        if (maxEntry.isPresent()) {
            highestRepeatedWord = maxEntry.get().getKey();
            highestRepeatedWordCount = maxEntry.get().getValue();
        }
    }
}
