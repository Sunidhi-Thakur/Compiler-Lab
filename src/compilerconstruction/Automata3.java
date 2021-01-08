package compilerconstruction;

import java.util.Scanner;

class Automata3 {

    private String check(String s) {
        String qt = "q0";
        switch (s) {
            case "e":
            case "def":
                return "qf";
            case "99":
                return "q0";
            default:
                for (int i = 0; i < s.length(); i++) {
                    char ch = s.charAt(i);
                    switch (qt) {
                        case "q0":
                            switch (ch) {
                                case 'a':
                                    qt = "q1";
                                    break;
                                case 'd':
                                    qt = "qf";
                                    break;
                                default:
                                    qt = "Trap";
                                    break;
                            }
                            break;
                        case "q1":
                            switch (ch) {
                                case 'a':
                                    qt = "q0";
                                    break;
                                case 'b':
                                    qt = "q1";
                                    break;
                                case 'c':
                                    qt = "qf";
                                    break;
                                case 'd':
                                    qt = "qf";
                                    break;
                                default:
                                    qt = "Trap";
                                    break;
                            }
                            break;
                        case "qf":
                        case "Trap":
                            return "Trap";
                        default:
                            return "X";
                    }

                }
                return qt;
        }
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.println("ENTER A STRING. USE '99' TO REPRESENT NULL");
        String s = sc.next();
        Automata3 A1 = new Automata3();
        String r = A1.check(s);
        switch (r) {
            case "X":
                System.out.println("Invalid characters present");
                break;
            case "qf":
                System.out.println("String Accepted");
                break;
            case "Trap":
                System.out.println("Trap state reached. String Rejected");
                break;
            default:
                System.out.println("String Rejected");
        }

    }

}

