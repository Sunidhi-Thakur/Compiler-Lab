package compilerconstruction;

import java.util.Scanner;

class Lab2Ques2 {

    private String check(String s) {
        String qt = "q0";

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == 'e') {
                qt = "String Rejected";
                return qt;
            } else {
                if ((qt.equals("q0")) && (c == 'a')) {
                    qt = "q0q1";
                } else if ((qt.equals("q0")) && (c == 'c')) {
                    qt = "q3";
                } else if ((qt.equals("q0")) && (c == 'b' || c == 'd')) {
                    qt = "Trap";
                } else if ((qt.equals("q0q1")) && (c == 'a')) {
                    qt = "q0q1";
                } else if ((qt.equals("q0q1")) && (c == 'b')) {
                    qt = "q1";
                } else if ((qt.equals("q0q1")) && (c == 'c')) {
                    qt = "q2q3";
                } else if ((qt.equals("q0q1")) && (c == 'd')) {
                    qt = "q2";
                } else if ((qt.equals("q1")) && (c == 'b')) {
                    qt = "q1";
                } else if ((qt.equals("q1")) && (c == 'a')) {
                    qt = "Trap";
                } else if ((qt.equals("q1")) && (c == 'c' || c == 'd')) {
                    qt = "q2";
                } else if ((qt.equals("q3")) && (c == 'c')) {
                    qt = "q3";
                } else if ((qt.equals("q3")) && (c == 'b')) {
                    qt = "q4";
                } else if ((qt.equals("q3")) && (c == 'a' || c == 'd')) {
                    qt = "Trap";
                } else if ((qt.equals("q4")) && (c == 'd')) {
                    qt = "q2";
                } else if ((qt.equals("q4")) && (c == 'c' || c == 'a' || c == 'b')) {
                    qt = "Trap";
                } else if ((qt.equals("q2"))) {
                    qt = "Trap";
                } else if ((qt.equals("q2q3")) && (c == 'c')) {
                    qt = "q3";
                } else if ((qt.equals("q2q3")) && (c == 'b')) {
                    qt = "q4";
                } else if ((qt.equals("q2q3")) && (c == 'a' || c == 'd')) {
                    qt = "Trap";
                }

            }
        }
        return qt;
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.println("ENTER A STRING. USE 'e' TO REPRESENT NULL");
        String s = sc.next();
        Lab2Ques2 L = new Lab2Ques2();
        String r = L.check(s);
        switch (r) {
            case "q2":
                System.out.println("String Accepted");
                break;
            default:
                System.out.println("String Rejected");

        }

    }

}
