/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilerconstruction;
import java.util.*;
public class Main {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter a number  :- ");
		int n = input.nextInt();
		
		System.out.println("The prime numbers between 1 and "+n+" are :- ");
		
		int count = 0;
		int i;
		int num;
                
                for(i = 2;i<=n;i++){
                    {
                        for(num=1;num<=i;num++)
                        {
                          if(i%num==0)
                              count++;
                        }
                        if(count==2)
                        System.out.println(i);
                        count=0;
                    }
                }
        }
}
