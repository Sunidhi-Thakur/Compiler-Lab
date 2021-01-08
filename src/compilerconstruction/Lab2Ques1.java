package compilerconstruction;

import java.util.Scanner;

class Lab2Ques1{

    private String check(String s) {
        String qt = "q0";
 
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if(c == 'e'){
                    qt = "String Rejected";
                    return qt;
                }
                else{
                if ((qt.equals("q0")) && (c == '0')) {
                    qt = "q0";
                }else if ((qt.equals("q0")) && (c == '1')) {
                    qt = "q1";
                }else if ((qt.equals("q1")) && (c == '0')){
                    qt = "q2";
                }else if ((qt.equals("q1")) && (c == '1')){
                    qt = "q1q2";
                }
                else if ((qt.equals("q1q2")) && (c == '0')){
                    qt = "q2";
                }
                else if ((qt.equals("q1q2")) && (c == '1')){
                    qt = "q1q2";
                }
                else if ((qt.equals("q2")) && (c == '0' || c=='1')) {
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
        Lab2Ques1 L = new Lab2Ques1();
        String r = L.check(s);
        switch (r) {
            case "q2":
            case "q1q2":
                System.out.println("String Accepted");
                break;
            default:
                System.out.println("String Rejected");
                
        }


    }

}

