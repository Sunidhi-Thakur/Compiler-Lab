package compilerconstruction;

import static compilerconstruction.RegularSet.unionOperator;
import java.util.*;
//10*1, 10001* 1*0 1*0* single+ passed

public class Ques4 {

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Regular Expression");
        String rExp = sc.nextLine();
        String start = " ", end = " ", mid = " ", temp;
        int l = rExp.length(), j = 0;

        LinkedHashSet<String> rSet = new LinkedHashSet<>();
        temp = rExp;
        System.out.println("temp = "+temp);
        while(temp.length() > 1){
        for(int i=0; i<temp.length()-1; i++){
            if(i+2 <= temp.length() && temp.charAt(i+1) == '*' ){
                temp = temp.substring(i+2);
                System.out.println("temp = "+temp);
                j = i+1;
                break;
            }
            else{
                start += temp.charAt(i);
              
            }
        }
        System.out.println("start = "+start);
        rSet.addAll(KleeneClosure(start.trim(), rSet));
        }
        

       System.out.println(rSet);

    }
static LinkedHashSet<String> KleeneClosure(String rExp, LinkedHashSet<String> rSet) {
        String mid = " ";
        String last = " ";
        String appearsBefore = " ";

        int count = 0, start = 0, end = 0;
        
        if(rExp.contains("+")){
            unionOperator(rExp, rSet);
        }

        for (int i = 0; i < rExp.length(); i++) {
            if (!Character.isLetterOrDigit(rExp.charAt(i))) {
                if (rExp.charAt(i) == '*') {

                    for (int p = 0; p < i - 1; p++) {
                        if (rExp.charAt(p) != '*' && rExp.charAt(p) != '+' ) {
                            appearsBefore += rExp.charAt(p);
                        } else {
                            count++;
                            start = p;
                        }
                    }

                    for (int p = i + 1; p < rExp.length(); p++) {
                        if (rExp.charAt(p) != '*' && rExp.charAt(p) != '+' ) {
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
        for(int i=0; i<3;i++){
            s = " ";
            for(int j=0; j <= i; j++)
                s += rExp;
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


