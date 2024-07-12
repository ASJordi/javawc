package dev.asjordi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
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
    
}
