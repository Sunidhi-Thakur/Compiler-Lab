package compilerconstruction;  
import java.util.*; 
public class Postfix {
 
    public static void paren(String input_str) {
  
    Stack<Character> s = new Stack<>();
    for (char st : input_str.toCharArray()) {
   
      if (st == '(' || st == '{' || st == '[') {
        s.push(st);
      }
       
      else {
          if(s.empty()) {
              System.out.println(Postfix(input_str));
            return;
          }
          else{
          
          char top = s.peek();
          if(st == ')' && top == '(' ||
          st == '}' && top == '{' ||
          st == ']' && top == '['){
            s.pop();
          }
          else{
             System.out.println(Postfix(input_str));
            return;
          }
        }
      }
    }

    if(s.empty()) {
        System.out.println(Postfix(input_str)); 
    }
    else{
      System.out.println("Invalid Expression");
    }
  }

    static int Prec(char ch) 
    { 
        switch (ch) 
        { 
        case '+': 
        case '-': 
            return 1; 
       
        case '*': 
        case '/': 
            return 2; 
       
//        case '^': 
//            return 3; 
        } 
        return -1; 
    } 
       
    static String Postfix(String exp) 
    { 
        String result = ""; 
        char c2;
          
        Stack<Character> stack = new Stack<>(); 
          
        for (int i = 0; i<exp.length(); i++) 
        { 
            char c = exp.charAt(i); 
            
//            if(i<exp.length()-1){
//               c2 = exp.charAt(i+1);
//               if(Character.isLetterOrDigit(c) && Character.isLetterOrDigit(c2))
//               return "Invalid Expression";
//        }
//            
            if (Character.isLetterOrDigit(c)){
                result += c; 
               
            }
            else if(c == '^')
                stack.push(c);
               
            else if (c == '(') 
                stack.push(c); 
              
            else if (c == ')') 
            { 
                while (!stack.isEmpty() &&  
                        stack.peek() != '(') 
                    result += stack.pop(); 
                  
                stack.pop(); 
            } 
            else
            { 
                while (!stack.isEmpty() && Prec(c)  
                         <= Prec(stack.peek())){ 
                  
                    result += stack.pop(); 
             } 
                stack.push(c); 
            } 
       
        } 
       
        while (!stack.isEmpty()){ 
            if(stack.peek() == '(') 
                return "Invalid Expression"; 
            result += stack.pop(); 
         } 
        return result; 
    } 
  
    public static void main(String[] args)  
    { 
        int c=0;
        Scanner sc = new Scanner(System.in);
         System.out.println("Sunidhi\tA2305218584");
        System.out.println("Enter infix Expression");
        String exp = sc.nextLine();
        System.out.println("Converting....");
        for(int i=0;i<exp.length();i++){
            if(exp.charAt(i)=='(' ||exp.charAt(i)==')'){
                c=1;
                break;
            
            }
        }
        if(c==0)
            System.out.println(Postfix(exp));
        else
            paren(exp);
            
      
    } 
}