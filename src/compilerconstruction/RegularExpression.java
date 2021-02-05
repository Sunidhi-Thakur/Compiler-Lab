package compilerconstruction;

import static compilerconstruction.RegularExpression.KleeneClosure;
import static compilerconstruction.RegularExpression.unionOperator;
import static compilerconstruction.RegularExpression.bracketString;
import static compilerconstruction.RegularExpression.simple;
import java.util.Iterator;

import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

class RegularExpression {

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

        if (startS.contains("(")) {
            rSet = bracketString(startS, rSet, rExp.indexOf('('));
        } else {
            rSet = KleeneClosure(startS, rSet);
        }

        if (endS.contains("(")) {
            rSet = bracketString(endS, rSet, rExp.indexOf('('));
        } else {
            rSet = KleeneClosure(endS, rSet);
        }

        return rSet;
    }

    static LinkedHashSet<String> bracketString(String rExp, LinkedHashSet<String> rSet, int j) {
        String s = " ", s1, s2, s3 = " ", s4 = " ";
        if (Pattern.matches("[(]+[0-1]*[)]+", rExp)) {
            rSet.add(rExp.substring(1,rExp.length()-1));
            System.out.println("S --> "+rExp.substring(1,rExp.length()-1));
        } else if (Pattern.matches("[(]+[0-1]*[)]+[\\*]", rExp)) {
            for (int i = 0; i < rExp.length(); i++) {
                s = rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf(')'));
            }
            rSet = simpleKleeneClosure(s.trim(), rSet);
            System.out.println("S --> ϵ|AS");
            System.out.println("A --> "+rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf(')')));
        } else if (Pattern.matches("[(]+[0-1]*[\\+]+[0-1]*[)]+[\\*]", rExp)) {
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
            System.out.println("S --> ϵ|AS");
            System.out.println("A --> "+s1+"|"+s2);
        
        } else if (Pattern.matches("[(]+[0-1]*[\\*]*[0-1]*[\\+]+[0-1]*[\\*]*[0-1]*[)]+[\\*]*", rExp)) {
            
            s1 = rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf('+'));
            s2 = rExp.substring(rExp.indexOf('+') + 1, rExp.indexOf(')'));
            String r1 = s1.replace('*', 'A');
            String r2 = s2.replace('*', 'A');
            
            if (s1.contains("*")) {
                rSet.addAll(KleeneClosure(s1.trim(), rSet));
            }
            if (s2.contains("*")) {
                rSet.addAll(KleeneClosure(s2.trim(), rSet));
            } else {
                rSet = KleeneClosure(s1.trim(), rSet);
                rSet.addAll(KleeneClosure(s2.trim(), rSet));
            }
            if(rExp.charAt(rExp.length()-1) == '*')
                System.out.println("S --> ϵ|"+s1.replaceAll("[0-1]?[\\*]+","A")+"|"+s2.replaceAll("[0-1]?[\\*]+","A"));
            else
                System.out.println("S --> "+s1.replaceAll("[0-1]?[\\*]+","A")+"|"+s2.replaceAll("[0-1]?[\\*]+","A"));
            if(r1.length() > 1 && r2.length()>1 && r1.contains("A") && r2.contains("A"))
                System.out.println("A --> ϵ|"+r1.substring(r1.indexOf('A')-1, r1.indexOf('A')+1)+'|'+r2.substring(r2.indexOf('A')-1, r2.indexOf('A')+1));
           
            else if(r1.length()>1 && r1.contains("A"))
                System.out.println("A --> ϵ|"+r1.substring(r1.indexOf('A')-1, r1.indexOf('A')+1));
           
            else
                System.out.println("A --> ϵ|"+r2.substring(r2.indexOf('A')-1, r2.indexOf('A')+1));
            
        } 
//        else if (Pattern.matches("[(]+[0-1]*[\\*]*[0-1]*[\\+]+[0-1]*[\\*]*[0-1]*[)]+", rExp)) {
//            System.out.println("In 5");
//            s1 = rExp.substring(rExp.indexOf('(') + 1, rExp.indexOf('+'));
//            s2 = rExp.substring(rExp.indexOf('+') + 1, rExp.indexOf(')'));
//            String r1 = s1.replace('*', 'A');
//            String r2 = s2.replace('*', 'A');
//
//            if (s1.contains("*")) {
//                rSet.addAll(KleeneClosure(s1.trim(), rSet));
//            }
//            if (s2.contains("*")) {
//                rSet.addAll(KleeneClosure(s2.trim(), rSet));
//            } else {
//                rSet = KleeneClosure(s1.trim(), rSet);
//                rSet.addAll(KleeneClosure(s2.trim(), rSet));
//            }
//            
//            System.out.println("S --> "+s1.replaceAll("[0-1]?[\\*]+","A")+"|"+s2.replaceAll("[0-1]?[\\*]+","A"));
//            System.out.println("A --> ϵ|"+r1.substring(r1.indexOf('A')-1, r1.indexOf('A')+1)+'|'+r2.substring(r2.indexOf('A')-1, r2.indexOf('A')+1));
//
//
//        }
        
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

class RegularExpressionDriver {

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Regular Expression");
        String rExp = sc.nextLine();
        int flag = 0;
        LinkedHashSet<String> rSet = new LinkedHashSet<>();

        if (rExp.contains("(")) {
            rSet = bracketString(rExp, rSet, rExp.indexOf('('));
        } else {
            if (!rExp.contains("+") && !rExp.contains("*")) {
                rSet = simple(rExp, rSet);
                 System.out.println("S --> "+rExp);
            } else if (rExp.contains("+") && rExp.contains("*")) {
                rSet = KleeneClosure(rExp, rSet);
            } else if (rExp.contains("+")) {
                rSet = unionOperator(rExp, rSet);
                System.out.println("S --> "+rExp.replace('+', '|'));
            }else if (rExp.contains("*")) {
                String s1=" ",s2=" ";
                rSet = KleeneClosure(rExp, rSet);
                if(rExp.length()>2){
                  s1 = rExp.substring(0,rExp.indexOf('*')-1);
                
                if(rExp.length()-s1.trim().length() > 2){
                  s2 = rExp.substring(rExp.indexOf('*')+1);
                  System.out.println("S --> "+s1+'A'+s2);
                }else{
                 System.out.println("S --> "+s1+'A');
                }
                }
                String s = rExp.substring(rExp.indexOf('*')-1, rExp.indexOf('*')+1);
                System.out.println("A --> ϵ|"+s.replace('*', 'A'));
                }
                else{
                    System.out.println("S --> ϵ|"+rExp.charAt(0)+"S");
                }

        
        }
        System.out.print("Regular Set = [");
        int count = 0;
        int l = rSet.size();
        Iterator<String> it = rSet.iterator();
        while (it.hasNext() && count < 11) {
            count++;
            if(l > 1){
                System.out.print(it.next() + ",");
                l--;
            }
            else
                System.out.print(it.next());
        }
//System.out.println("Regular Set = " +rSet);
if(rSet.size() > 1)
    System.out.println(",........]\n");
else
    System.out.println("]\n");

    }
}
