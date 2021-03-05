package compilerconstruction;

import static compilerconstruction.Lab5.removeLeftRecursion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Lab5 {

    static void removeLeftRecursion(String p) {
        String first = " ";
        String dash = " ";
        int count=0;
        char Terminal = p.charAt(0);
      
        String r = p.substring(p.indexOf('>') + 1);
        if(r.length()>1){

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
        if(count>0){
        if (first.trim().length() > 1) {
            System.out.println(Terminal + "->" + first.substring(0,first.length()-1));
            System.out.println(Terminal + "'->" + dash + "ϵ");
        } else {
            System.out.println(Terminal + "->" + first+Terminal+"'");
            System.out.println(Terminal + "'->" + dash + "ϵ");
        }
        }else{
            System.out.println(Terminal+"->"+r);
        }
        }
        else{
            System.out.println(Terminal+"->"+r);
    }

}
}
class Lab5Driver {

    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the number of Non-Terminals:");
        int N = Integer.parseInt(br.readLine());
        System.out.println("Enter Productions:");
        String[] p = new String[N];
        for (int i = 0; i < N; i++) {
            p[i] = br.readLine();

        }
        System.out.println();
        for (int i = 0; i < N; i++) {
            removeLeftRecursion(p[i]);
        }

    }

}
