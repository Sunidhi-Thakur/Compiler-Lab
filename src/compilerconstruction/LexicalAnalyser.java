package compilerconstruction;

import java.util.*;
/**
 *
 * @author Sunidhi Thakur
 */
class LexicalAnalyser {


    Map check(String s) {
        char c;
        Map<Character, String> myTokens = new HashMap<>();
        for(int i = 0; i<s.length(); i++){
            c = s.charAt(i);
            if(c == '+' || c == '-' || c == '=' || c=='*' || c=='/')
                myTokens.put(c,"OPERATOR");
            else if(Character.isDigit(c))
                myTokens.put(c,"CONSTANT");
            else if(Character.isLetter(c))
                myTokens.put(c,"IDENTIFIER");
            else if(c == ';' || c == '{' || c=='}' || c == '(' || c == ')' || c == ',')
                myTokens.put(c,"DELIMITER");
            else{
               myTokens.clear();
               return myTokens;
            }
        }
        return myTokens;
    
}
}

class LexicalAnalyzerTester{
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter code: ");
        String s = sc.nextLine();
        Map<Character, String> myTokens;
        LexicalAnalyser LA = new LexicalAnalyser();
        myTokens = LA.check(s);
        if(myTokens.isEmpty())
            System.out.println("Compiler Error");
        else
            for(Map.Entry m : myTokens.entrySet()){    
            System.out.println("<"+m.getKey()+","+m.getValue()+">");   
        
    }
    
}
}
 

