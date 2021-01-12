package compilerconstruction;

public class Try {

    public static void main(String[] args) {

        Lexer lexer = new Lexer("C:/Users/sunid/Input.txt");

        while (!lexer.isExausthed()) {
            System.out.printf("%-18s :  %s \n",lexer.currentLexema() , lexer.currentToken());
            lexer.moveAhead();
        }

        if (lexer.isSuccessful()) {
            System.out.println("Compiled Successfully");
        } else {
            System.out.println(lexer.errorMessage());
        }
    }
}