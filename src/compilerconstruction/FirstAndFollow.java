package compilerconstruction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;

class FirstAndFollow {

    ArrayList<String> removeLeftRecursion(String p) {
        ArrayList<String> noLeftRecur = new ArrayList<>();
        String first = " ";
        String dash = " ";
        int count = 0;
        char Terminal = p.charAt(0);

        String r = p.substring(p.indexOf('>') + 1);
        if (r.length() > 1) {

            StringTokenizer str = new StringTokenizer(r, "|");

            while (str.hasMoreTokens()) {
                String s = str.nextToken();
                if (s.charAt(0) == Terminal) {
                    dash += s.substring(1) + Terminal + "'|";
                    count++;
                } else {
                    first += s + Terminal + "'|";
                }
            }

            first = first.trim();
            dash = dash.trim();
            if (count > 0) {
                if (first.trim().length() > 1) {
                    noLeftRecur.add(Terminal + "->" + first.substring(0, first.length() - 1));
                    noLeftRecur.add(Terminal + "'->" + dash + "ϵ");
                } else {
                    noLeftRecur.add(Terminal + "->" + first + Terminal + "'");
                    noLeftRecur.add(Terminal + "'->" + dash + "ϵ");
                }
            } else {
                noLeftRecur.add(Terminal + "->" + r);
            }
        } else {
            noLeftRecur.add(Terminal + "->" + r);
        }
        return noLeftRecur;
    }

    ArrayList<String> firstOfNonTerminals(ArrayList<String> prod, ArrayList<String> first, char Symbol) {
        Iterator<String> it = prod.iterator();
        String f = "", a1 = "", a2 = "";
        int k;
        while (it.hasNext()) {
            String a = it.next();
            if (!(a.contains("|"))) {
                if (a.charAt(a.indexOf('>') + 1) == 'ϵ' || a.charAt(a.indexOf('|') + 1) == 'ϵ') {
                    f = "ϵ ";
                }
                if (!(Character.isUpperCase(a.charAt(a.indexOf('>') + 1)))) {
                    k = a.indexOf('>') + 1;
                    while (k < a.length() && !(Character.isUpperCase(a.charAt(k)))) {
                        f += a.charAt(k);
                        k++;
                    }
                } else {
                    f = "S";
                    if (a.contains("'")) {
                        f += a.substring(a.indexOf('>') + 1);
                    } else {
                        f += a.charAt(a.indexOf('>') + 1);
                    }
                }
                first.add(f.trim());
                f = "";
            } else {
                a2 = a.substring(a.indexOf(">") + 1);
                StringTokenizer str = new StringTokenizer(a2, "|");
                while (str.hasMoreTokens()) {
                    a1 = str.nextToken();
                    k = 0;

                    if (k < a1.length() && !(Character.isUpperCase(a1.charAt(k)))) {
                        while (k < a1.length() && !(Character.isUpperCase(a1.charAt(k)))) {
                            f += a1.charAt(k) + " ";
                            k++;
                        }
                    } else {
                        f = "S";
                        if (a1.contains("'")) {
                            f += a1.substring(0);
                        } else {
                            f += a1.charAt(a1.charAt(0));
                        }
                    }
                }
                first.add(f.trim());
                f = "";
            }

        }
        return first;
    }

//    ArrayList<String> followOfNonTerminals(String sub, ArrayList<String> follow, char symbol, LinkedHashSet<String> nonTerminals) {
//        
//        
//        return follow;
//    }
}

class FirstAndFollowDriver {

    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the number of Non-Terminals:");
        int N = Integer.parseInt(br.readLine());
        System.out.println("Enter Productions:");
        String[] p = new String[N];
        String f = " ";
        LinkedHashSet<String> terminals = new LinkedHashSet<>();
        LinkedHashSet<String> nonTerminals = new LinkedHashSet<>();

        ArrayList<String> prod = new ArrayList<>();

        ArrayList<String> first = new ArrayList<>();
        ArrayList<String> follow = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            p[i] = br.readLine();

        }
        char symbol = p[0].charAt(0);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < p[i].length(); j++) {
                if (!(p[i].charAt(j) >= 65 && p[i].charAt(j) <= 90) && p[i].charAt(j) != '|' && p[i].charAt(j) != '-' && p[i].charAt(j) != '>') {
                    f += p[i].charAt(j);

                } else {
                    terminals.add(f.trim());
                    f = " ";
                }
            }

            terminals.add(f.trim());
        }

        System.out.println();
        FirstAndFollow l = new FirstAndFollow();
        for (int i = 0; i < N; i++) {
            prod.addAll(l.removeLeftRecursion(p[i]));
        }
        System.out.println("After removing left recursion");

        Iterator<String> it = prod.iterator();
        while (it.hasNext()) {
            String a = it.next();
            System.out.println(a);
            nonTerminals.add(a.substring(0, a.indexOf('-')));

        }
        first = l.firstOfNonTerminals(prod, first, symbol);
        Iterator<String> it2 = prod.iterator();
        String a, sub = " ";
        while (it.hasNext()) {
            a = it.next();
            sub += a.substring(a.indexOf(">") + 1);
        }
        //follow = l.followOfNonTerminals(sub, follow, symbol, nonTerminals);

        String t[] = terminals.toArray(new String[terminals.size()]);
        String nt[] = nonTerminals.toArray(new String[nonTerminals.size()]);

        System.out.println(first);
        String key = "", check = "";
        int k = 0;
        for (int i = 0; i < nonTerminals.size(); i++) {
            check = first.get(i);
            if (check.startsWith("S")) {
                if (check.charAt(2) == '\'') {
                    key = check.substring(1, 3);
                    System.out.println("key="+ key);
                } else {
                    key = check.substring(1, 2);
                    System.out.println("key="+key);
                }
                while (k < nt.length) {
                    if (nt[k].equals(key)) {
                        System.out.println("First(" + nt[i] + ")={" + first.get(k) + "}");
                        break;
                    } else {
                        k++;
                    }
                }
            } else {
                System.out.println("First(" + nt[i] + ")={" + first.get(i) + "}");
            }
        }

        System.out.println();
    }

}
