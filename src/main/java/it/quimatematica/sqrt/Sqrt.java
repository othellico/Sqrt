package it.quimatematica.sqrt;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.floor;
import static java.lang.Math.sqrt;

/**
 * Non thread safe.
 */
@Slf4j
public class Sqrt {

    private List<Integer> groups;
    private List<Integer> resultDigits;

    private List<String> leftLines;
    private List<String> rightLines;

    public SqrtResult calculate(int number) {
        clean();

        groups = split(number);

        int firstGroup = groups.get(0);
        List<Integer> remainingGroups = groups.subList(1, groups.size());
        log.info("Considering {}; remaining: {}", firstGroup, remainingGroups);

        int firstResultDigit = (int) floor(sqrt(firstGroup));
        resultDigits.add(firstResultDigit);
        int firstResultDigitPower = firstResultDigit * firstResultDigit;

        addLeftNumber(firstResultDigitPower, remainingGroups.size());
        addLeftLine();

        int partialRest = firstGroup - firstResultDigitPower;

        while (!remainingGroups.isEmpty()) {
            addRightSeparator();

            int group = remainingGroups.get(0);
            remainingGroups = remainingGroups.subList(1, remainingGroups.size());
            log.info("Considering {}; remaining: {}", group, remainingGroups);

            int rest = partialRest * 100 + group;

            addLeftNumber(rest, remainingGroups.size());

            int partialResult = partialResult();
            int found = 10;
            int times;
            do {
                found--;
                times = (partialResult * 20 + found) * found;

                addRightLine(partialResult, found, times);
            } while (times > rest);

            resultDigits.add(found);

            addLeftNumber(times, remainingGroups.size());
            addLeftLine();

            partialRest = rest - times;
        }

        addLastLeftNumber(partialRest);

        return SqrtResult.builder().sqrt(partialResult()).rest(partialRest).build();
    }

    private void addRightLine(int partialResult, int found, int times) {
        rightLines.add(" " + (partialResult * 20 + found) + " x " + found + " = " + times);
    }

    private void addRightSeparator() {
        rightLines.add("----------------------");
    }

    private int partialResult() {
        int temp = 0;
        for (Integer resultDigit : resultDigits) {
            temp = temp * 10 + resultDigit;
        }
        return temp;
    }

    private void clean() {
        resultDigits = new ArrayList<>();
        leftLines = new ArrayList<>();
        rightLines = new ArrayList<>();
    }

    private void addLeftLine() {
        StringBuilder builder = new StringBuilder();
        int target = leftLinesLength();
        while (builder.length() < target) {
            builder.append('-');
        }
        leftLines.add(builder.toString());
    }

    private void addLastLeftNumber(int rest) {
        StringBuilder builder = new StringBuilder();

        if (rest < 100) {
            builder.append(rest).append(' ');
        }
        else {
            int temp = rest;
            while (temp > 0) {
                if (temp < 100) {
                    builder.insert(0, (temp % 100) + " ");
                } else {
                    builder.insert(0, pad((temp % 100)) + " ");
                }
                temp = temp / 100;
            }
        }

        int target = leftLinesLength();
        while (builder.length() < target) {
            builder.insert(0, ' ');
        }

        leftLines.add(builder.toString());
    }

    private void addLeftNumber(int number, int emptySpaces) {
        StringBuilder builder = new StringBuilder();

        int temp = number;
        while (temp > 0) {
            if (temp < 100) {
                builder.insert(0, (temp % 100) + " ");
            } else {
                builder.insert(0, pad((temp % 100)) + " ");
            }
            temp = temp / 100;
        }

        for (int i = 0; i < emptySpaces; i++) {
            builder.append("   ");
        }

        int target = leftLinesLength();
        while (builder.length() < target) {
            builder.insert(0, ' ');
        }

        leftLines.add(builder.toString());
    }

    private int leftLinesLength() {
        int length = groups.get(0) < 10 ? 1 : 2;
        length += (groups.size() - 1) * 3;
        return length + 1;
    }

    private static List<Integer> split(int number) {
        List<Integer> groups = new ArrayList<>();
        int current = number;
        while (current > 0) {
            int group = current % 100;
            groups.add(0, group);
            current = current / 100;
        }
        log.info("Split result: input number: {}; split: {}", number, groups);
        return groups;
    }

    public void dump(PrintStream stream) {
        stream.println(generateDumpZeroLine());
        stream.println(generateDumpFirstLine());

        int lines = leftLines.size();
        if (lines < rightLines.size()) lines = rightLines.size();

        for (int i = 0; i < lines; i++) {
            stream.println(generateDumpLine(i));
        }
    }

    private String generateDumpLine(int i) {
        StringBuilder builder = new StringBuilder();
        builder.append("   ");
        if (i < leftLines.size()) {
            builder.append(leftLines.get(i));
        } else {
            int target = leftLinesLength() + 3;
            while (builder.length() < target) {
                builder.append(' ');
            }
        }
        builder.append("|");
        if (i < rightLines.size()) {
            builder.append(rightLines.get(i));
        }
        return builder.toString();
    }

    private String generateDumpFirstLine() {
        StringBuilder builder = new StringBuilder();
        builder.append(" \u221A ");
        builder.append(groups.get(0));
        for (int i = 1; i < groups.size(); i++) {
            builder.append('.');
            builder.append(pad(groups.get(i)));
        }
        builder.append(" | ");
        for (Integer resultDigit : resultDigits) {
            builder.append(resultDigit);
        }
        return builder.toString();
    }

    private static String pad(int number) {
        StringBuilder builder = new StringBuilder();
        builder.append(number);
        while (builder.length() < 2) {
            builder.insert(0, '0');
        }
        return builder.toString();
    }

    private String generateDumpZeroLine() {
        StringBuilder builder = new StringBuilder();
        builder.append("  ");
        for (int i = 0; i < groups.size(); i++) {
            builder.append("___");
        }
        return builder.toString();
    }

}
