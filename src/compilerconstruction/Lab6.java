package compilerconstruction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;

class Lab6 {

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
        String f = "", a1, a2;
        int k;
        while (it.hasNext()) {
            String a = it.next();

            if (!(a.contains("|"))) {
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
                if (a.contains("ϵ")) {
                    f += "ϵ ";
                }
                if (f.contains("9")) {
                    f = f.replace('9', 'ϵ');
                    first.add(f.trim());
                } else {
                    first.add(f.trim());
                }
                f = "";
            } else {
                a2 = a.substring(a.indexOf(">") + 1);
                StringTokenizer str = new StringTokenizer(a2, "|");

                while (str.hasMoreTokens()) {
                    a1 = str.nextToken();
                    k = 0;
                    if (k < a1.length() && !(Character.isUpperCase(a1.charAt(k)))) {
                        f += " ";
                        while (k < a1.length() && !(Character.isUpperCase(a1.charAt(k)))) {
                            f += a1.charAt(k);
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
                if (a.contains("ϵ") && !(f.contains("ϵ"))) {
                    f += "ϵ ";
                }

                if (f.contains("9")) {
                    f = f.replace('9', 'ϵ');
                    first.add(f.trim());
                } else {
                    first.add(f.trim());
                }
                f = "";
            }

        }

        return first;
    }

    ArrayList<String> followOfNonTerminals(ArrayList<String> first, ArrayList<String> follow, ArrayList<String> prod, String[] nt) {
        Iterator<String> it = prod.iterator();
        String a[] = new String[10];
        int k = 0;
        String t = "";
        while (it.hasNext()) {
            t= it.next();
            a[k++] = t.substring(t.indexOf('>')+1);
        }
        for(int i=0; i<nt.length;i++){
            while(!(a[k].contains(nt[i]))){
                k++;
            }
            if(Character.isUpperCase(a[k].indexOf(nt[i]+1))){
                if(a[k].length() >=2 && a[k].indexOf(nt[i]+2) == '\'')
                    follow.add(first.get(k));
            }
        }
        return follow;
    }
}

class Lab6Driver {

    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the number of Non-Terminals. Press 9 for ϵ :");
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
        Lab6 l = new Lab6();
        for (int i = 0; i < N; i++) {
            prod.addAll(l.removeLeftRecursion(p[i]));
        }
        for (int i = 0; i < prod.size(); i++) {
            String rem9 = prod.get(i);
            if (rem9.contains("9")) {
                prod.set(i, rem9.replace("9", "").trim());
            }
        }
        System.out.println("After removing left recursion");

        Iterator<String> it = prod.iterator();
        while (it.hasNext()) {
            String a = it.next();
            System.out.println(a);
            nonTerminals.add(a.substring(0, a.indexOf('-')));

        }
        first = l.firstOfNonTerminals(prod, first, symbol);
              
       // String t[] = terminals.toArray(new String[terminals.size()]);
        String nt[] = nonTerminals.toArray(new String[nonTerminals.size()]);

        System.out.println();
        String key, check;
        int k = nt.length - 1;
        for (int i = nonTerminals.size() - 1; i >= 0; i--) {
            check = first.get(i);
            if (check.startsWith("S")) {
                if (check.length() > 2 && check.charAt(2) == '\'') {
                    key = check.substring(1, 3);
                } else {
                    key = check.substring(1, 2);
                }
                while (k >= 0) {
                    if (nt[k].equals(key)) {
                        first.set(i, first.get(k));
                        if (prod.get(i).contains("ϵ") && !(first.get(i).contains("ϵ"))) {
                            System.out.println("First(" + nt[i] + ")={" + first.get(k) + " ϵ}");
                        } else {
                            System.out.println("First(" + nt[i] + ")={" + first.get(k) + "}");
                        }
                        break;
                    } else {
                        k--;
                    }
                }
            } else {
                System.out.println("First(" + nt[i] + ")={" + first.get(i) + "}");
            }
        }
        follow = l.followOfNonTerminals(first, follow, prod, nt);

        System.out.println(follow);
    }

}
