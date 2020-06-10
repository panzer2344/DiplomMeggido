import data.reader.DataReader;
import model.Inequality;
import model.LPTask;
import org.javatuples.Pair;
import solver.Solver2D;
import util.InequalitiesGenerator;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    protected static Map<String, String> onGenerateArgs = new LinkedHashMap<String, String>(){{
        put("--top_start_a", "");
        put("--top_end_a", "");
        put("--top_start_free", "");
        put("--top_end_free", "");
        put("--top_count", "");

        put("--bot_start_a", "");
        put("--bot_end_a", "");
        put("--bot_start_free", "");
        put("--bot_end_free", "");
        put("--bot_count", "");

        put("--left_start_free", "");
        put("--left_end_free", "");
        put("--left_count", "");

        put("--right_start_free", "");
        put("--right_end_free", "");
        put("--right_count", "");

    }};

    public static void main(String[] args) {

        if(args.length < 1) {
            showHelp();
            return;
        }

        String typeOfRun = args[0];

        if("--generate".equals(typeOfRun)) {
            handleGenerate(args);
        } else if("--existent".equals(typeOfRun)) {
            handleExistent(args);
        } else if("--help".equals(typeOfRun)) {
            showHelp();
        } else {
            showHelp();
        }

    }

    protected static void handleGenerate(String[] args) {
        if(args.length < 33) {
            showHelp();
            return;
        }

        for(int i = 1; i < args.length; i += 2) {
            if(onGenerateArgs.get(args[i]) != null) {
                onGenerateArgs.put(args[i], args[i + 1]);
            }
        }

        InequalitiesGenerator inequalitiesGenerator = new InequalitiesGenerator();

        Inequality[] top = inequalitiesGenerator.generate(
                        Long.parseLong(onGenerateArgs.getOrDefault("--top_start_a", "0")),
                        Long.parseLong(onGenerateArgs.getOrDefault("--top_end_a", "0")),
                        Long.parseLong(onGenerateArgs.getOrDefault("--top_start_free", "0")),
                        Long.parseLong(onGenerateArgs.getOrDefault("--top_end", "0")),
                        Integer.parseInt(onGenerateArgs.getOrDefault("--top_count", "0")),
                        Inequality.Sign.LESS_OR_EQUAL,
                        !Inequality.ZERO_CONSTRAINT);

        Inequality[] bot = inequalitiesGenerator.generate(
                        Long.parseLong(onGenerateArgs.getOrDefault("--bot_start_a", "0")),
                        Long.parseLong(onGenerateArgs.getOrDefault("--bot_end_a", "0")),
                        Long.parseLong(onGenerateArgs.getOrDefault("--bot_start_free", "0")),
                        Long.parseLong(onGenerateArgs.getOrDefault("--bot_end", "0")),
                        Integer.parseInt(onGenerateArgs.getOrDefault("--bot_count", "0")),
                        Inequality.Sign.GREAT_OR_EQUAL,
                        !Inequality.ZERO_CONSTRAINT);

        Inequality[] left = inequalitiesGenerator.generate(
                        1,
                        1,
                        Long.parseLong(onGenerateArgs.getOrDefault("--left_start_free", "0")),
                        Long.parseLong(onGenerateArgs.getOrDefault("--left_end", "0")),
                        Integer.parseInt(onGenerateArgs.getOrDefault("--left_count", "0")),
                        Inequality.Sign.LESS_OR_EQUAL,
                        Inequality.ZERO_CONSTRAINT);

        Inequality[] right = inequalitiesGenerator.generate(
                        1,
                        1,
                        Long.parseLong(onGenerateArgs.getOrDefault("--right_start_free", "0")),
                        Long.parseLong(onGenerateArgs.getOrDefault("--right_end", "0")),
                        Integer.parseInt(onGenerateArgs.getOrDefault("--right_count", "0")),
                        Inequality.Sign.GREAT_OR_EQUAL,
                        Inequality.ZERO_CONSTRAINT);

        Inequality[] inequalities = inequalitiesGenerator.mergeIneqs(top, bot, left, right);

        Pair<Double, Double> solution = new Solver2D().solve(inequalities);

        System.out.println("Solution: " + solution);

    }

    protected static void handleExistent(String[] args) {
        if(args.length < 3 && !"--file".equals(args[1])) {
            showHelp();
            return;
        }

        String fileName = args[2];

        DataReader dataReader = new DataReader();

        LPTask lpTask = null;
        try {
             lpTask = dataReader.readFromFile(fileName, dataReader::readLPTask);
        } catch (IOException io) {
            System.out.println(io.getMessage());
            return;
        }

        Pair<Double, Double> solution = new Solver2D().solve(lpTask);

        System.out.println("Solution: " + solution);
    }

    protected static void showHelp() {
        System.out.println("Use examples:");
        System.out.println("1. ");
        System.out.println("java program_name.jar --generate --top_start_a 30 --top_end_a -30 --top_start_free 5 --top_end_free 15 --top_count 15" +
                " --bot_start_a -30 --bot_end_a 30 --bot_start_free -5 --bot_end_free -15 --bot_count 10" +
                " --left_start_free 10 --left_end_free 15 --left_count 5" +
                " --right_start_free -10 --right_end_free -15 --right_count 2");
        System.out.println("2.");
        System.out.println("java program_name.jar --existent --file data/lp_task_test_data.txt");
        System.out.println("3.");
        System.out.println("java program_name.jar --help");
    }

}
