
package dev.asjordi;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Jordi <ejordi.ayala@gmail.com>
 */
public class WcTool {

    private static final List<String> definedCommands = List.of("-c", "-m", "-l", "-L", "-w", "-r", "-rr", "--help", "--version");

    public static void main(String[] args) {
        if (processEmptyArgumentCommand(args)) return;
        if (processSingleArgumentCommand(args)) return;
        handleMultipleArguments(args);
    }

    private static void handleMultipleArguments(String[] args) {
        Path path = null;
        Set<String> commands = new HashSet<>();

        for (String arg : args) {
            arg = arg.replaceAll("\\s+", "").trim();
            if (arg.startsWith("-")) {
                if (definedCommands.contains(arg)) commands.add(arg);
                else {
                    System.out.println("Invalid option " + arg + ", use --help for more information.");
                    return;
                }
            } else if (arg.lastIndexOf(".") != -1) {
                path = Paths.get(arg);
            } else {
                System.out.println("Invalid option " + arg + ", use --help for more information.");
                return;
            }
        }

        if (path != null && FileUtil.isValidFile(path) && !commands.isEmpty()) processFileCommands(path, commands);
    }

    private static void processFileCommands(Path path, Set<String> commands) {
        Core core = new Core(path);
        Object[] results = new Object[7];

        for (String command : commands) {
            switch (command) {
                case "-l" -> results[0] = core.getNumberOfLines();
                case "-w" -> results[1] = core.getNumberOfWords();
                case "-m" -> results[2] = core.getNumberOfCharacters();
                case "-c" -> results[3] = core.getNumberOfBytes();
                case "-L" -> results[4] = core.getMaxLineLength();
                case "-r" -> results[5] = core.getHighestRepeatedWord();
                case "-rr" -> results[6] = core.getHighestRepeatedWordCount();
                default -> System.out.println("Invalid option " + command + ", use --help for more information.");
            }
        }

        StringBuilder sb = formatResults(results);
        sb.append(path.getFileName().toString());

        System.out.println(sb);
    }

    private static StringBuilder formatResults(Object[] results) {
        String[] format = {"%d ", "%d ", "%d ", "%d ", "%d ", "%s ", "%d "};
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < results.length; i++) {
            if (results[i] != null) {
                sb.append(String.format(format[i], results[i]));
            }
        }

        return sb;
    }

    private static boolean processEmptyArgumentCommand(String[] args) {
        if (args.length == 0) {
            System.out.println("Standard input not supported yet. Use --help for more information.");
            return true;
        }
        return false;
    }

    private static boolean processSingleArgumentCommand(String[] args) {
        if (args.length == 1) {
            Path path = Paths.get(args[0]);
            if (FileUtil.isValidFile(path)) {
                Core core = new Core(path);
                System.out.println(core.getDefaultOutput());
                return true;
            } else if (args[0].equals("--help")) {
                System.out.println(Core.getHelp());
                return true;
            } else if (args[0].equals("--version")) {
                System.out.println(Core.getVersion());
                return true;
            } else {
                System.out.println("Invalid option " + args[0] + ", use --help for more information.");
                return true;
            }
        }
        return false;
    }

}
