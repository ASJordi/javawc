package dev.asjordi;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Jordi <ejordi.ayala@gmail.com>
 */
public class FileUtil {
    
    private FileUtil() {}

    public static boolean isValidFile(Path path) {
        return Files.exists(path) && Files.isRegularFile(path) && Files.isReadable(path);
    }
    
    public static List<String> readAllLines(Path path) {
        List<String> list = new LinkedList<>();

        try(Reader r = new FileReader(path.toFile(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(r)) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (Exception e) {
            System.out.println("Can't read file" + e.getMessage());
        }
        
        return list.stream()
                .filter(l -> !l.trim().isEmpty())
                .collect(Collectors.toList());
    }

    public static List<String> readFromConsole() {
        List<String> list = new LinkedList<>();

        try (Reader r = new InputStreamReader(System.in, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(r)) {
            String ch;
            while ((ch = br.readLine()) != null) {
                if (">exit".equalsIgnoreCase(ch.trim())) break;
                list.add(ch);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
    
}
