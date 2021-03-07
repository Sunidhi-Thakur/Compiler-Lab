package compilerconstruction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

class FirstAndFollow {

    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the number of non-terminals");
        int N = Integer.parseInt(br.readLine());
        System.out.println("Enter the productions");
        String[] p = new String[N];

        ArrayList<String> prod = new ArrayList<>();
        String a;
        int k;
        for (int i = 0; i < N; i++) {
            p[i] = br.readLine();

        }

        for (int i = 0; i < N; i++) {
            prod.addAll(removeLeftRecursion(p[i]));
        }

        System.out.println("\nAfter removing left recursion");

        Iterator<String> it = prod.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }

        String[] p1 = new String[prod.size()];
        p1 = prod.toArray(p1);
        String nt[] = new String[p1.length];
        for (int i = 0; i < p1.length; i++) {
            nt[i] = p1[i].substring(0, p1[i].indexOf("-"));
        }

        HashMap<String, String> first = new HashMap<>();
        HashMap<String, String> follow = new HashMap<>();
        first = firstOfGrammar(p1, first);

        for (int l = 0; l < 3; l++) {
            k = 0;
            a = "";
            while (k < nt.length) {
                String value = first.get(nt[k] + "");

                for (int i = 0; i < value.length(); i++) {

                    if (Character.isUpperCase(value.charAt(i))) {
                        a += first.get(value.charAt(i) + "");
                    } else {
                        a += value.charAt(i);
                    }
                }

                first.put(nt[k] + "", a);
                a = "";
                k++;
            }
        }

        follow = followOfGrammar(nt, p1, follow, first);
        String ans = follow.get(p[0].substring(0, 1));
        ans += " $ ";
        follow.put(p[0].substring(0, 1), ans);
        for (int l = 0; l < 3; l++) {
            k = 0;
            a = "";
            while (k < nt.length) {
                String value = follow.get(nt[k] + "");
                for (int i = 0; i < value.length(); i++) {
                    if (Character.isUpperCase(value.charAt(i))) {
                        a += follow.get(value.charAt(i) + "");
                    } else {
                        a += value.charAt(i);
                    }
                }

                follow.put(nt[k] + "", a);
                a = "";
                k++;
            }
        }

        System.out.println("\nFirst of elements:");
        for (Map.Entry<String, String> entry : first.entrySet()) {
            System.out.println("First(" + entry.getKey() + ")={" + entry.getValue().trim() + "}");
        }
        System.out.println("\nFollow of elements:");
        for (Map.Entry<String, String> entry : follow.entrySet()) {
            String d=entry.getValue();
            if(entry.getValue().contains("'")){
                d = entry.getValue().replaceAll("'", "");
            }
            System.out.println("Follow(" + entry.getKey() + ")={" + d.trim() + "}");
        }
    }

    private static HashMap<String, String> firstOfGrammar(String[] p, HashMap<String, String> first) {
        String s, ans;
        for (String p1 : p) {

            StringTokenizer str = new StringTokenizer(p1, "|");
            ans = "";
            while (str.hasMoreElements()) {
                s = str.nextToken();
                if (s.contains("->")) {
                    s = s.substring(3);
                }

                if (s.equals("∈")) {
                    ans += "∈ ";
                } else {
                    ans += s.charAt(s.indexOf(">") + 1) + " ";
                }

                if (p1.charAt(1) == '\'') {
                    first.put(p1.substring(0, 2), ans);
                } else {
                    first.put(p1.charAt(0) + "", ans);
                }
            }
        }

        return first;
    }

    private static ArrayList<String> removeLeftRecursion(String p) {
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

    private static HashMap<String, String> followOfGrammar(String[] nt, String[] p1, HashMap<String, String> follow, HashMap<String, String> first) {
        String s, ans;
        int k = 0;
        while (k < nt.length) {
            for (String p : p1) {
                StringTokenizer str = new StringTokenizer(p, "|");
                ans = "";
                while (str.hasMoreElements()) {
                    s = str.nextToken();
                    if (s.contains("->")) {
                        s = s.substring(s.indexOf(">") + 1);
                    }

                    if (s.contains(nt[k])) {
                        if (nt[k].contains("'") && s.contains("'") && s.length() > 1) {
                            if (s.charAt(s.length() - 1) == '\'') {
                                ans += p.substring(0, p.indexOf("-"));
                            } else {
                                ans += s.substring(s.indexOf(nt[k]) + 1);
                            }

                        } else if (!(nt[k].contains("'")) && s.contains("'")) {
                            if (s.charAt(s.indexOf(nt[k]) + 1) != '\'') {
                                ans += first.get(s.substring(s.indexOf(nt[k]) + 1));
                                if (ans.contains("ϵ")) {
                                    ans = ans.replace("ϵ", "");
                                    if (s.length() - 1 > s.indexOf("'") && Character.isUpperCase(s.charAt(s.indexOf("'") + 1))) {
                                        ans += first.get(s.substring(s.indexOf("'") + 1));

                                    } else if (s.length() - 1 > s.indexOf("'") && Character.isLowerCase(s.charAt(s.indexOf("'") + 1))) {
                                        ans += s.substring(s.indexOf("'") + 1);
                                    } else {
                                        ans += p.substring(0, p.indexOf("-"));
                                    }
                                }
                            }

                        } else {
                            ans += s.charAt(s.indexOf(nt[k]) + 1);

                        }
                    }
                }
                if (ans.length() >= 1 && !(ans.equals(nt[k]))) {
                    follow.put(nt[k], ans);
                }
            }
            k++;
        }
        return follow;
    }
}
