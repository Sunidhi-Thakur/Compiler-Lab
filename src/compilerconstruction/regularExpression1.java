package compilerconstruction;

import static compilerconstruction.RegularExpression1.KleeneClosure;
import static compilerconstruction.RegularExpression1.PositiveClosure;
import static compilerconstruction.RegularExpression1.unionOperator;
import static compilerconstruction.RegularExpression1.bracketString;
import static compilerconstruction.RegularExpression1.simple;
import static compilerconstruction.RegularExpression1.bracketStringForPositive;
import java.io.BufferedWriter;

import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import java.io.IOException;
import java.io.FileWriter;
import java.util.Iterator;

class RegularExpression1 {

    static LinkedHashSet<String> simple(String rExp, LinkedHashSet<String> rSet) {
        String s = " ";
        for (int i = 0; i < rExp.length(); i++) {
            if (rExp.charAt(i) != '+' && rExp.charAt(i) != '*') {
                s += rExp.charAt(i);
            }
        }
        rSet.add(s.trim());
        return rSet;
    }

    static LinkedHashSet<String> unionOperator(String rExp, LinkedHashSet<String> rSet, BufferedWriter w) throws IOException {
        int i = rExp.indexOf('+');

        String startS = rExp.substring(0, i);
        String endS = rExp.substring(i + 1);

        if (startS.contains("(")) {
            rSet = bracketString(startS, rSet, rExp.indexOf('('), w);
        } else {
            rSet = KleeneClosure(startS, rSet, w);
        }

        if (endS.contains("(")) {
            rSet = bracketString(endS, rSet, rExp.indexOf('('), w);
        } else {
            rSet = KleeneClosure(endS, rSet, w);
        }

        return rSet;
    }

    static LinkedHashSet<String> bracketString(String rExp, LinkedHashSet<String> rSet, int j, BufferedWriter w) throws IOException {
        String s = " ", s1, s2, s3 = " ", s4 = " ";

        if (Pattern.matches("[(]+[0-1]*[a-z]*[)]+", rExp)) {
            rSet.add(rExp.substring(1, rExp.length() - 1));
            w.append("\nS --> " + rExp.substring(1, rExp.length() - 1));
        } else if (Pattern.matches("[(]+[0-1]*[a-z]*[)]+[\\*]", rExp)) {
            for (int i = 0; i < rExp.length(); i++) {
                s = rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf(')'));
            }
            rSet = simpleKleeneClosure(s.trim(), rSet);
            w.append("\nS --> ϵ|AS");
            w.append("\nA --> " + rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf(')')));
        } else if (Pattern.matches("[(]+[0-1]*[a-z]*[\\+]+[0-1]*[a-z]*[)]+[\\*]", rExp)) {
            s1 = rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf('+'));
            s2 = rExp.substring(rExp.indexOf('+') + 1, rExp.indexOf(')'));
            s3 += s1 + s2;
            s4 += s2 + s1;
            rSet = simpleKleeneClosure(s1.trim(), rSet);
            rSet.addAll(simpleKleeneClosure(s2.trim(), rSet));
            rSet.add(s3.trim());
            rSet.add(s3.trim() + s3.trim());
            rSet.add(s4.trim());
            rSet.add(s4.trim() + s4.trim());
            w.append("\nS --> ϵ|AS");
            w.append("\nA --> " + s1 + "|" + s2);

        } else if (Pattern.matches("[(]+[0-1]*[a-z]*[\\*]*[0-1]*[a-z]*[\\+]+[0-1]*[a-z]*[\\*]*[0-1]*[a-z]*[)]+[\\*]*", rExp)) {

            s1 = rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf('+'));
            s2 = rExp.substring(rExp.indexOf('+') + 1, rExp.indexOf(')'));
            String r1 = s1.replace('*', 'A');
            String r2 = s2.replace('*', 'A');

            if (s1.contains("*")) {
                rSet.addAll(KleeneClosure(s1.trim(), rSet, w));
            }
            if (s2.contains("*")) {
                rSet.addAll(KleeneClosure(s2.trim(), rSet, w));
            } else {
                rSet = KleeneClosure(s1.trim(), rSet, w);
                rSet.addAll(KleeneClosure(s2.trim(), rSet, w));
            }
            if (rExp.charAt(rExp.length() - 1) == '*') {
                w.append("\nS --> ϵ|" + s1.replaceAll("[0-1]?[a-z]?[\\*]+", "A") + "|" + s2.replaceAll("[0-1]?[a-z]?[\\*]+", "A"));
            } else {
                w.append("\nS --> " + s1.replaceAll("[0-1]?[a-z]?[\\*]+", "A") + "|" + s2.replaceAll("[0-1]?[a-z]?[\\*]+", "A"));
            }
            if (r1.length() > 1 && r2.length() > 1 && r1.contains("A") && r2.contains("A")) {
                w.append("\nA --> ϵ|" + r1.substring(r1.indexOf('A') - 1, r1.indexOf('A') + 1) + '|' + r2.substring(r2.indexOf('A') - 1, r2.indexOf('A') + 1));
            } else if (r1.length() > 1 && r1.contains("A")) {
                w.append("\nA --> ϵ|" + r1.substring(r1.indexOf('A') - 1, r1.indexOf('A') + 1));
            } else if (r2.length() > 1 && r2.contains("A")) {
                w.append("\nA --> ϵ|" + r2.substring(r2.indexOf('A') - 1, r2.indexOf('A') + 1));
            }

        } else if (Pattern.matches("[0-1]*[(]+[0-1]*[\\*]*[0-1]*[\\+]+[0-1]*[\\*]*[0-1]*[)]+[\\*]*[0-1]*", rExp)) {
            String plain;

            s1 = rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf('+'));
            s2 = rExp.substring(rExp.indexOf('+') + 1, rExp.indexOf(')'));

            if (rExp.charAt(0) == '(') {
                plain = rExp.substring(rExp.indexOf('*') + 1);
                
                if (s1.contains("*")) {
                    rSet.addAll(simpleKleeneClosure2(s1.trim() + plain, rSet));
                }
                if (s2.contains("*")) {
                    rSet.addAll(simpleKleeneClosure2(s2.trim() + plain, rSet));
                } else {
                    rSet = simpleKleeneClosure2(s1.trim() + plain, rSet);
                    rSet.addAll(simpleKleeneClosure2(s2.trim() + plain, rSet));
                }
                w.append("\nS --> A" + plain);
                w.append("\nA --> ϵ|" + s1 + "A|" + s2 + "A");

            } else {
                plain = rExp.substring(0, rExp.indexOf('('));
                if (s1.contains("*")) {
                    rSet.addAll(simpleKleeneClosure2(plain + s1.trim(), rSet));
                }
                if (s2.contains("*")) {
                    rSet.addAll(simpleKleeneClosure2(plain + s2.trim(), rSet));
                } else {
                    rSet = simpleKleeneClosure2(plain + s1.trim(), rSet);
                    rSet.addAll(simpleKleeneClosure2(plain + s2.trim(), rSet));
                }
                w.append("\nS --> " + plain + "A");
                w.append("\nA --> ϵ|" + s1 + "A|" + s2 + "A");

            }

        }

        return rSet;
    }

    static LinkedHashSet<String> KleeneClosure(String rExp, LinkedHashSet<String> rSet, BufferedWriter w) throws IOException {
        String mid = " ";
        String last = " ";
        String appearsBefore = " ";

        int count = 0, start = 0, end = 0;

        if (rExp.contains("+")) {
            unionOperator(rExp, rSet, w);
        }

        for (int i = 0; i < rExp.length(); i++) {
            if (!Character.isLetterOrDigit(rExp.charAt(i))) {
                if (rExp.charAt(i) == '*') {

                    for (int p = 0; p < i - 1; p++) {
                        if (rExp.charAt(p) != '*' && rExp.charAt(p) != '+' && rExp.charAt(p) != '$') {
                            appearsBefore += rExp.charAt(p);
                        } else {
                            count++;
                            start = p;
                        }
                    }

                    for (int p = i + 1; p < rExp.length(); p++) {
                        if (rExp.charAt(p) != '*' && rExp.charAt(p) != '+' && rExp.charAt(p) != '$') {
                            last += rExp.charAt(p);
                        } else {
                            count++;
                            end = p;
                        }
                    }

                    if (count > 1) {
                        for (int p = start + 1; p < end; p++) {
                            mid += rExp.charAt(p);
                        }
                    }

                    if (appearsBefore.length() <= 1 && last.length() <= 1) {
                        rSet.add("ϵ");
                    } else {
                        String s = " ";
                        appearsBefore = appearsBefore.trim();
                        last = last.trim();
                        for (int p = 0; p < appearsBefore.length(); p++) {
                            if (appearsBefore.charAt(p) != '+' && appearsBefore.charAt(p) != '*') {
                                s += appearsBefore.charAt(p);
                            }
                        }
                        for (int p = 0; p < last.length(); p++) {
                            if (last.charAt(p) != '+' && last.charAt(p) != '*') {
                                s += last.charAt(p);
                            }
                        }
                        rSet.add(s.trim());
                    }
                    printPattern(rExp.charAt(i - 1), appearsBefore.trim(), rSet, last.trim(), mid.trim());
                }
            } else {
                int flag = 0;
                for (int p = 0; p < rExp.length(); p++) {
                    if (rExp.charAt(p) == '*' || rExp.charAt(p) == '+') {
                        flag = 1;
                    }
                }
                if (flag == 0) {
                    rSet.add(rExp);
                }
            }

        }
        return rSet;
    }

    private static LinkedHashSet<String> simpleKleeneClosure(String rExp, LinkedHashSet<String> rSet) {
        rSet.add("ϵ");
        String s;
        for (int i = 0; i < 3; i++) {
            s = " ";
            for (int j = 0; j <= i; j++) {
                s += rExp;
            }
            rSet.add(s.trim());
        }
        return rSet;
    }

    private static LinkedHashSet<String> simpleKleeneClosure2(String rExp, LinkedHashSet<String> rSet) {
        String s;
        for (int i = 0; i < 3; i++) {
            s = " ";
            for (int j = 0; j <= i; j++) {
                s += rExp;
            }
            rSet.add(s.trim());
        }
        return rSet;
    }

    private static Set printPattern(char c, String appearsBefore, Set rSet, String last, String mid) {
        String s;
        for (int i = 0; i < 5; i++) {
            s = " ";
            s += appearsBefore;
            for (int j = 0; j <= i; j++) {
                s += c;
            }
            s += mid;
            s += last;
            if (s.length() > 1) {
                rSet.add(s.trim());
            }
        }
        return rSet;
    }

    static LinkedHashSet<String> PositiveClosure(String rExp, LinkedHashSet<String> rSet, BufferedWriter w) throws IOException {
        String mid = " ";
        String last = " ";
        String appearsBefore = " ";

        int count = 0, start = 0, end = 0;

        if (rExp.contains("+")) {
            unionOperatorForPositive(rExp, rSet, w);
        }

        for (int i = 0; i < rExp.length(); i++) {
            if (!Character.isLetterOrDigit(rExp.charAt(i))) {
                if (rExp.charAt(i) == '$') {
                    for (int p = 0; p < i - 1; p++) {
                        if (rExp.charAt(p) != '$' && rExp.charAt(p) != '+') {
                            appearsBefore += rExp.charAt(p);
                        } else {
                            count++;
                            start = p;
                        }
                    }
                    for (int p = i + 1; p < rExp.length(); p++) {
                        if (rExp.charAt(p) != '$' && rExp.charAt(p) != '+') {
                            last += rExp.charAt(p);
                        } else {
                            count++;
                            end = p;
                        }
                    }
                    if (count > 1) {
                        for (int p = start + 1; p < end; p++) {
                            mid += rExp.charAt(p);
                        }
                    }
                }
                printPattern(rExp.charAt(i - 1), appearsBefore.trim(), rSet, last.trim(), mid.trim());

            }
            int flag = 0;
            for (int p = 0; p < rExp.length(); p++) {
                if (rExp.charAt(p) == '$' || rExp.charAt(p) == '+') {
                    flag = 1;
                }
            }
            if (flag == 0) {
                rSet.add(rExp);
            }
        }
        return rSet;
    }

    static LinkedHashSet<String> unionOperatorForPositive(String rExp, LinkedHashSet<String> rSet, BufferedWriter w) throws IOException {
        int i = rExp.indexOf('+');

        String startS = rExp.substring(0, i);
        String endS = rExp.substring(i + 1);

        if (startS.contains("(")) {
            rSet = bracketStringForPositive(startS, rSet, rExp.indexOf('('), w);
        } else {
            rSet = PositiveClosure(startS, rSet, w);
        }
        if (endS.contains("(")) {
            rSet = bracketStringForPositive(endS, rSet, rExp.indexOf('('), w);
        } else {
            rSet = PositiveClosure(endS, rSet, w);
        }
        return rSet;
    }

    static LinkedHashSet<String> bracketStringForPositive(String rExp, LinkedHashSet<String> rSet, int j, BufferedWriter w) throws IOException {
        String s = " ", s1, s2, s3 = " ", s4 = " ";
        if (Pattern.matches("[(]+[0-1]*[a-z]*[)]+[\\$]", rExp)) {
            s1 = rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf(')'));
            rSet = simplePositiveClosure(s1.trim(), rSet);
            w.append("\nS --> " + s1 + "|A");
            w.append("\nA --> ϵ|" + s1 + "A");
        } else if (Pattern.matches("[(]+[0-1]*[a-z]*[\\+]+[0-1]*[a-z]*[)]+[\\$]", rExp)) {
            s1 = rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf('+'));
            s2 = rExp.substring(rExp.indexOf('+') + 1, rExp.indexOf(')'));
            s3 += s1 + s2;
            s4 += s2 + s1;
            rSet = simplePositiveClosure(s1.trim(), rSet);
            rSet.addAll(simplePositiveClosure(s2.trim(), rSet));
            rSet.add(s3.trim());
            rSet.add(s3.trim() + s3.trim());
            rSet.add(s4.trim());
            rSet.add(s4.trim() + s4.trim());
            w.append("\nS --> " + s1 + "A|" + s2 + "A");
            w.append("\nA --> ϵ|S");

        } else if (Pattern.matches("[(]+[0-1]*[a-z]*[\\$]*[0-1]*[a-z]*[\\+]+[0-1]*[a-z]*[\\$]*[0-1]*[a-z]*[)]+[\\$]*", rExp)) {

            s1 = rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf('+'));
            s2 = rExp.substring(rExp.indexOf('+') + 1, rExp.indexOf(')'));
            String r1 = s1.replace('$', 'A');
            String r2 = s2.replace('$', 'A');
            String e1 = " ", e2 = " ";
            if (r1.contains("A")) {
                e1 = r1.substring(r1.indexOf('A') - 1, r1.indexOf('A') + 1);
            }
            if (r2.contains("A")) {
                e2 = r2.substring(r2.indexOf('A') - 1, r2.indexOf('A') + 1);
            }

            if (s1.contains("$")) {
                rSet.addAll(PositiveClosure(s1.trim(), rSet, w));
            }
            if (s2.contains("$")) {
                rSet.addAll(PositiveClosure(s2.trim(), rSet, w));
            } else {
                rSet = PositiveClosure(s1.trim(), rSet, w);
                rSet.addAll(PositiveClosure(s2.trim(), rSet, w));
            }
            if (rExp.charAt(rExp.length() - 1) == '$') {
                w.append("\nS --> " + s1.replaceAll("[\\$]+", "A") + "|" + s2.replaceAll("[\\$]+", "A"));
            } else {
                w.append("\nS --> " + s1.replaceAll("[\\$]+", "A") + "|" + s2.replaceAll("[\\$]+", "A"));
            }
            if (r1.length() > 1 && r2.length() > 1 && r1.contains("A") && r2.contains("A")) {
                if (e1.equals(e2)) {
                    w.append("\nA --> ϵ|" + e1);
                } else {
                    w.append("\nA --> ϵ|" + e1 + '|' + e2);
                }
            } else if (r1.length() > 1 && r1.contains("A")) {
                w.append("\nA --> ϵ|" + e1);
            } else {
                w.append("\nA --> ϵ|" + e2);
            }
        }else if (Pattern.matches("[0-1]*[(]+[0-1]*[\\*]*[0-1]*[\\+]+[0-1]*[\\*]*[0-1]*[)]+[\\$]*[0-1]*", rExp)) {
            String plain;

            s1 = rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf('+'));
            s2 = rExp.substring(rExp.indexOf('+') + 1, rExp.indexOf(')'));

            if (rExp.charAt(0) == '(') {
                plain = rExp.substring(rExp.indexOf('$') + 1);
                
                if (s1.contains("$")) {
                    rSet.addAll(simpleKleeneClosure2(s1.trim() + plain, rSet));
                }
                if (s2.contains("$")) {
                    rSet.addAll(simpleKleeneClosure2(s2.trim() + plain, rSet));
                } else {
                    rSet = simpleKleeneClosure2(s1.trim() + plain, rSet);
                    rSet.addAll(simpleKleeneClosure2(s2.trim() + plain, rSet));
                }
                w.append("\nS --> " +s1+"A"+ plain +"|"+ s2+"A"+plain);
                w.append("\nA --> ϵ|"+ s1+"A|"+s2+"A" );

            } else {
                plain = rExp.substring(0, rExp.indexOf('('));
                if (s1.contains("$")) {
                    rSet.addAll(simpleKleeneClosure2(plain + s1.trim(), rSet));
                }
                if (s2.contains("$")) {
                    rSet.addAll(simpleKleeneClosure2(plain + s2.trim(), rSet));
                } else {
                    rSet = simpleKleeneClosure2(plain + s1.trim(), rSet);
                    rSet.addAll(simpleKleeneClosure2(plain + s2.trim(), rSet));
                }
                w.append("\nS --> " + plain + s1+"A|"+plain+s2+"A");
                w.append("\nA --> ϵ|"+ s1+"A|"+s2+"A" );

            }

        }
        return rSet;
    }

    private static LinkedHashSet<String> simplePositiveClosure(String rExp, LinkedHashSet<String> rSet) {
        String s;
        for (int i = 0; i < 3; i++) {
            s = " ";
            for (int j = 0; j <= i; j++) {
                s += rExp;
            }

            rSet.add(s.trim());
        }
        return rSet;
    }
}

class RegularExpressionDriver1 {

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Regular Expression. Press $ for positive closure.");
        String rExp = sc.nextLine();
        int count = 1;
        LinkedHashSet<String> rSet = new LinkedHashSet<>();
        try {
            try (BufferedWriter w = new BufferedWriter(new FileWriter("C:\\Users\\sunid\\Desktop\\output.txt"))) {
                w.append("Program" + count + "\n");

                if (rExp.contains("(") && rExp.contains("*") && !rExp.contains("$")) {
                    rSet = bracketString(rExp, rSet, rExp.indexOf('('), w);
                } else if (rExp.contains("(") && rExp.contains("$") && !rExp.contains("*")) {
                    rSet = bracketStringForPositive(rExp, rSet, rExp.indexOf('('), w);
                } else {
                    if (!rExp.contains("+") && !rExp.contains("*") && !rExp.contains("$")) {
                        rSet = simple(rExp, rSet);
                        w.append("\nS --> " + rExp);
                    } else if (rExp.contains("+") && rExp.contains("*")) {
                        rSet = KleeneClosure(rExp, rSet, w);
                    } else if (rExp.contains("+")) {
                        rSet = unionOperator(rExp, rSet, w);
                        w.append("\nS --> " + rExp.replace('+', '|'));
                    } else if (rExp.contains("*")) {
                        String s1, s2;
                        rSet = KleeneClosure(rExp, rSet, w);
                        if (rExp.length() > 2) {
                            s1 = rExp.substring(0, rExp.indexOf('*') - 1);

                            if (rExp.length() - s1.trim().length() > 2) {
                                s2 = rExp.substring(rExp.indexOf('*') + 1);
                                w.append("\nS --> " + s1 + 'A' + s2);
                            } else {
                                w.append("\nS --> " + s1 + 'A');

                            }
                            String s = rExp.substring(rExp.indexOf('*') - 1, rExp.indexOf('*') + 1);
                            w.append("\nA --> ϵ|" + s.replace('*', 'A'));
                        } else {
                            w.append("\nS --> ϵ|" + rExp.charAt(0) + "S");
                        }
                    } else if (rExp.contains("+") && rExp.contains("$")) {
                        rSet = PositiveClosure(rExp, rSet, w);
                    } else if (rExp.contains("$")) {
                        String s1, s2;
                        rSet = PositiveClosure(rExp, rSet, w);
                        if (rExp.length() > 2) {
                            s1 = rExp.substring(0, rExp.indexOf('$') - 1).trim();

                            if (rExp.length() - s1.trim().length() > 2) {
                                s2 = rExp.substring(rExp.indexOf('$') + 1).trim();
                                w.append("\nS --> " + s1 + 'A' + s2);
                            } else {
                                w.append("\nS --> " + s1 + 'A');

                            }
                            String s = rExp.substring(rExp.indexOf('$') - 1, rExp.indexOf('$') + 1);
                            w.append("\nA --> " + s.replace('$', 'B'));
                            w.append("\nB --> ϵ|A");
                        } else {
                            w.append("\nS --> " + rExp.charAt(0) + "A");
                            w.append("\nA --> ϵ|" + rExp.charAt(0) + "S");
                        }
                    }
                }
                System.out.print("Regular Set = [");
                int count1 = 0;
                int l = rSet.size();
                Iterator<String> it = rSet.iterator();
                while (it.hasNext() && count1 < 6) {
                    count++;
                    if (l > 1) {
                        System.out.print(it.next() + ",");
                        l--;
                    } else {
                        System.out.print(it.next());
                    }
                }
                w.append("\n\n\nEnd of Program");
                count++;
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        if (rSet.size() > 1) {
            System.out.println(",........]\n");
        } else {
            System.out.println("]\n");
        }
    }
}
