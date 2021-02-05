package compilerconstruction;

import static compilerconstruction.RegularSet.KleeneClosure;
import static compilerconstruction.RegularSet.unionOperator;
import static compilerconstruction.RegularSet.bracketString;
import static compilerconstruction.RegularSet.simple;
import java.util.Iterator;

import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

class RegularSet {

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

    static LinkedHashSet<String> unionOperator(String rExp, LinkedHashSet<String> rSet) {
        int i = rExp.indexOf('+');

        String startS = rExp.substring(0, i);
        String endS = rExp.substring(i + 1);

        
            rSet = KleeneClosure(startS, rSet);
       
            rSet = KleeneClosure(endS, rSet);
        

        return rSet;
    }

    static LinkedHashSet<String> bracketString(String rExp, LinkedHashSet<String> rSet, int j) {
        String s = " ", s1, s2, s3 = " ", s4 = " ";
        
        if (Pattern.matches("[(]+[0-1]*[)]+[\\*]", rExp)) {
            System.out.println("In 2");
            for (int i = 0; i < rExp.length(); i++) {
                s = rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf(')'));
            }
            rSet = simpleKleeneClosure(s.trim(), rSet);
        } else if (Pattern.matches("[(]+[0-1]*[\\+]+[0-1]*[)]+[\\*]", rExp)) {
            System.out.println("In 3");
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
        } else if (Pattern.matches("[(]+[0-1]*[\\*]*[0-1]*[\\+]+[0-1]*[\\*]*[0-1]*[)]+[\\*]+", rExp)) {
            System.out.println("In 4");
            s1 = rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf('+'));
            s2 = rExp.substring(rExp.indexOf('+') + 1, rExp.indexOf(')'));

            if (s1.contains("*")) {
                rSet.addAll(KleeneClosure(s1.trim(), rSet));
            }
            if (s2.contains("*")) {
                rSet.addAll(KleeneClosure(s2.trim(), rSet));
            } else {
                rSet = KleeneClosure(s1.trim(), rSet);
                rSet.addAll(KleeneClosure(s2.trim(), rSet));
            }
        } else if (Pattern.matches("[(]+[0-1]*[\\*]*[0-1]*[\\+]+[0-1]*[\\*]*[0-1]*[)]+", rExp)) {
            System.out.println("In 5");
            s1 = rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf('+'));
            s2 = rExp.substring(rExp.indexOf('+') + 1, rExp.indexOf(')'));

            if (s1.contains("*")) {
                rSet.addAll(KleeneClosure(s1.trim(), rSet));
            }
            if (s2.contains("*")) {
                rSet.addAll(KleeneClosure(s2.trim(), rSet));
            } else {
                rSet = KleeneClosure(s1.trim(), rSet);
                rSet.addAll(KleeneClosure(s2.trim(), rSet));
            }
        }

        return rSet;
    }

    static LinkedHashSet<String> KleeneClosure(String rExp, LinkedHashSet<String> rSet) {
        String mid = " ";
        String last = " ";
        String appearsBefore = " ";

        int count = 0, start = 0, end = 0;

        if (rExp.contains("+")) {
            unionOperator(rExp, rSet);
        }

        for (int i = 0; i < rExp.length(); i++) {
            if (!Character.isLetterOrDigit(rExp.charAt(i))) {
                if (rExp.charAt(i) == '*') {

                    for (int p = 0; p < i - 1; p++) {
                        if (rExp.charAt(p) != '*' && rExp.charAt(p) != '+') {
                            appearsBefore += rExp.charAt(p);
                        } else {
                            count++;
                            start = p;
                        }
                    }

                    for (int p = i + 1; p < rExp.length(); p++) {
                        if (rExp.charAt(p) != '*' && rExp.charAt(p) != '+') {
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
                } else if (rExp.charAt(i) == '%') {
                    last = rExp.substring(i + 1);
                    appearsBefore = rExp.substring(0, i - 1);
                    printPattern(rExp.charAt(i - 1), appearsBefore, rSet, last.trim(), mid.trim());
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
            rSet.add(s.trim());
        }
        return rSet;
    }
}

class RegularSetDriver {

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Regular Expression. Use ");
        String rExp = sc.nextLine();
        int flag = 0;
        LinkedHashSet<String> rSet = new LinkedHashSet<>();

        if (rExp.contains("(")) {
            rSet = bracketString(rExp, rSet, rExp.indexOf('('));
        } else {
            if (!rExp.contains("+") && !rExp.contains("*")) {
                rSet = simple(rExp, rSet);
            } else if (rExp.contains("+")) {
                rSet = unionOperator(rExp, rSet);
            } else if (rExp.contains("*")) {
                rSet = KleeneClosure(rExp, rSet);

            }
        }
        int count = 0;
        Iterator<String> it = rSet.iterator();
        while (it.hasNext() && count < 11) {
            count++;
            System.out.print(it.next() + ",");
        }

System.out.println("........\n");

    }
}
