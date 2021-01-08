package compilerconstruction;

import java.util.Scanner;

class Automata {

    private char check(String s) {
        char qt = 'q';
        if ((s.length() == 1) && (s.equals("e"))) {
            return 'Q';
        } 
        else {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if ((qt == 'q') && (c == '0')) {
                    qt = 'Q';
                } else if ((qt == 'q') && (c == '1')) {
                    qt = 'Q';
                } else if ((qt == 'Q') && (c == '0')) {
                    qt = 'q';
                } else if ((qt == 'Q') && (c == '1')) {
                    qt = 'Q';
                } else {
                    return 'X';
                }

            }
            return qt;
        }
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.println("ENTER A STRING. USE 'e' TO REPRESENT NULL");
        String s = sc.next();
        Automata A1 = new Automata();
        char r = A1.check(s);
        switch (r) {
            case 'X':
                System.out.println("Invalid characters present");
                break;
            case 'Q':
                System.out.println("String Accepted");
                break;
            default:
                System.out.println("String Rejected");
                break;
        }

    }

}
