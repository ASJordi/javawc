package dev.asjordi;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Jordi <ejordi.ayala@gmail.com>
 */
public class WcTool {

    private static final List<String> definedCommands = List.of("-c", "-m", "-l", "-L", "-w", "-r", "-rr", "--help", "--version");

    public static void main(String[] args) {
        processEmptyArgumentCommand(args);
        if (processSingleArgumentCommand(args)) return;
        handleMultipleArguments(args);
    }

    private static void handleMultipleArguments(String[] args) {
        List<Path> paths = new LinkedList<>();
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
                paths.add(Paths.get(arg));
            } else {
                System.out.println("Invalid option " + arg + ", use --help for more information.");
                return;
            }
        }

        if (!paths.isEmpty() && paths.size() > 1) {
            processMultipleFileCommands(paths, commands);
        } else if (paths.size() == 1 && !commands.isEmpty() && FileUtil.isValidFile(paths.getFirst())) {
            processFileCommands(paths.getFirst(), commands);
        }
    }

    private static void processMultipleFileCommands(List<Path> paths, Set<String> commands) {
        List<Object[]> results = new LinkedList<>();

        paths.forEach(p -> {
            Core c = new Core(p);
            Object[] res = new Object[7];
            if (FileUtil.isValidFile(p)) {
                processCommands(commands, c, res);
                results.add(res);
            } else {
                System.out.println("Path " + p.getFileName() + " is not a valid file.");
            }
        });

        for (int i = 0; i < results.size(); i++) {
            StringBuilder sb = formatResults(results.get(i));
            sb.append(paths.get(i).getFileName().toString());
            System.out.println(sb);
        }

        Object[] sum = new Object[7];
        for (Object[] result : results) {
            for (int j = 0; j < result.length; j++) {
                if (result[j] != null) {
                    if (sum[j] == null) sum[j] = result[j] instanceof String ? 0L : result[j];
                    else {
                        if (result[j] instanceof String) continue;
                        else sum[j] = (long) sum[j] + (long) result[j];
                    }
                }
            }
        }

        StringBuilder sb = formatResults(sum);
        sb.append("total");
        System.out.println(sb);
    }

    private static void processCommands(Set<String> commands, Core core, Object[] result) {
        if (!commands.isEmpty()) {
            for (String command : commands) {
                switch (command) {
                    case "-l" -> result[0] = core.getNumberOfLines();
                    case "-w" -> result[1] = core.getNumberOfWords();
                    case "-m" -> result[2] = core.getNumberOfCharacters();
                    case "-c" -> result[3] = core.getNumberOfBytes();
                    case "-L" -> result[4] = core.getMaxLineLength();
                    case "-r" -> result[5] = core.getHighestRepeatedWord();
                    case "-rr" -> result[6] = core.getHighestRepeatedWordCount();
                    default -> System.out.println("Invalid option " + command + ", use --help for more information.");
                }
            }
        } else {
            result[0] = core.getNumberOfLines();
            result[1] = core.getNumberOfWords();
            result[2] = core.getNumberOfCharacters();
        }
    }

    private static void processFileCommands(Path path, Set<String> commands) {
        Core core = new Core(path);
        Object[] results = new Object[7];

        processCommands(commands, core, results);

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

    private static void processEmptyArgumentCommand(String[] args) {
        if (args.length == 0) {
            List<String> lines = FileUtil.readFromConsole();
            Core core = new Core(lines);
            System.out.println(core.getDefaultOutput());
        }
    }

    private static boolean processSingleArgumentCommand(String[] args) {
        if (args.length == 1) {
            Path path = Paths.get(args[0]);
            if (FileUtil.isValidFile(path)) {
                Core core = new Core(path);
                System.out.println(core.getDefaultOutput() + " " + path.getFileName().toString());
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
