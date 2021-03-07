package compilerconstruction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

class LeadingAndTrailing {

    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the number of non-terminals");
        int N = Integer.parseInt(br.readLine());
        System.out.println("Enter the productions");
        String[] p = new String[N];
        char nt[] = new char[N];
        String a="";
        int k=0;
        for (int i = 0; i < N; i++) {
            p[i] = br.readLine();
            nt[i] = p[i].charAt(0);
        }
        HashMap<Character, String> lead = new HashMap<>();
        HashMap<Character,String> trail = new HashMap<>();
        lead = leading(p, lead);
        
        for(int l=0;l<3;l++){
            k=0;
            a="";
        while(k<nt.length){
            String value = lead.get(nt[k]);
            for(int i=0; i<value.length();i++){
                if(Character.isUpperCase(value.charAt(i)))
                    a += lead.get(value.charAt(i));
                else
                    a+=value.charAt(i);
            }
            
            lead.put(nt[k],a);
            a="";
            k++;
        }
               
        }
        
        trail = trailing(p, trail);
        for(int l=0;l<3;l++){
            k=0;
            a="";
        while(k<nt.length){
            String value = trail.get(nt[k]);
            for(int i=0; i<value.length();i++){
                if(Character.isUpperCase(value.charAt(i)))
                    a += trail.get(value.charAt(i));
                else
                    a+=value.charAt(i);
            }
            
            trail.put(nt[k],a);
            a="";
            k++;
        }
               
        }
        
        
        System.out.println("\nLeading of elements:");
        System.out.println(lead+"\n");
        System.out.println("\nTrailing of elements:");
        System.out.println(trail+"\n");

    }

    private static HashMap<Character, String> leading(String[] p, HashMap<Character, String> lead) {
        String s,ans;
        for (String p1 : p) {
            
            StringTokenizer str = new StringTokenizer(p1, "|");
            ans = "";
            while(str.hasMoreElements()){
                s = str.nextToken();
                if(s.contains("->"))
                    s = s.substring(3);
                
                if(s.length()>1 && Character.isUpperCase(s.charAt(0)))
                    ans +=  s.charAt(1)+" ";
                else if(s.length() == 1 && Character.isUpperCase(s.charAt(0)))
                    ans +=  s.charAt(0)+" ";
                else
                    ans += s.charAt(0)+" ";
                lead.put(p1.charAt(0),ans);
                 
            }
        }
            return lead;
    }

    private static HashMap<Character, String> trailing(String[] p, HashMap<Character, String> trail) {
        String s,ans;
        for (String p1 : p) {
            
            StringTokenizer str = new StringTokenizer(p1, "|");
            ans = "";
            while(str.hasMoreElements()){
                s = str.nextToken();
                if(s.contains("->"))
                    s = s.substring(3);
                
                if(s.length()>1 && Character.isUpperCase(s.charAt(s.length()-1)))
                    ans +=  s.charAt(s.length()-2)+" ";
                else if(s.length() == 1 && Character.isUpperCase(s.charAt(0)))
                    ans +=  s.charAt(0)+" ";
                else
                    ans += s.charAt(s.length()-1)+" ";
                trail.put(p1.charAt(0),ans);
                 
            }
        }
            return trail;
    }
}
