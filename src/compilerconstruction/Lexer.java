package compilerconstruction;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
 
public class Lexer {
    private int line;
    private int pos;
    private int position;
    private char chr;
    private final String s;
    Map<String, TokenType> keywords = new HashMap<>();
    static class Token {
        public TokenType tokentype;
        public String value;
        public int line;
        public int pos;
        Token(TokenType token, String value, int line, int pos) {
            this.tokentype = token; 
            this.value = value; 
            this.line = line; 
            this.pos = pos;
        }
        @Override
        public String toString() {
            String result = String.format("%5d  %5d     %-15s", this.line, this.pos, this.tokentype);
            switch (this.tokentype) {
                case Integer:
                    result += String.format("  %s", value);
                    break;
                case Identifier:
                    result += String.format("  %s", value);
                    break;
                case Literal:
                    result += String.format(" \"%s\"", value);
                    break;
            }
            return result;
        }
    }
    static enum TokenType {
        End_of_input, Op_multiply,  Op_divide, Op_mod, Op_add, Op_subtract,
        Op_negate, Op_not, Op_less, Op_lessequal, Op_greater, Op_greaterequal,
        Op_equal, Op_notequal, Op_assign, Op_and, Op_or, Keyword_if,
        Keyword_else, Keyword_while, Keyword_printf, Keyword_scanf, Keyword_for,
        Keyword_return, Keyword_void, Keyword_int, Keyword_long, 
        Keyword_float, Keyword_double, Keyword_do, Keyword_static, Keyword_struct,
        LeftParen, RightParen,
        LeftBrace, RightBrace, Delimiter, Identifier, Integer, Literal
    }
    Lexer(String source, int count) {
        this.line = count;
        this.pos = 0;
        this.position = 0;
        this.s = source;
        try{
        this.chr = this.s.charAt(0);
        }catch(Exception e){
            System.out.println("Compilation Complete");
        }
        this.keywords.put("if", TokenType.Keyword_if);
        this.keywords.put("else", TokenType.Keyword_else);
        this.keywords.put("printf", TokenType.Keyword_printf);
        this.keywords.put("scanf", TokenType.Keyword_scanf);
        this.keywords.put("while", TokenType.Keyword_while);
        this.keywords.put("for", TokenType.Keyword_for);
        this.keywords.put("return", TokenType.Keyword_return);
        this.keywords.put("void", TokenType.Keyword_void);
        this.keywords.put("int", TokenType.Keyword_int);
        this.keywords.put("long", TokenType.Keyword_long);
        this.keywords.put("float", TokenType.Keyword_float);
        this.keywords.put("double", TokenType.Keyword_double);
        this.keywords.put("do", TokenType.Keyword_do);
        this.keywords.put("static", TokenType.Keyword_static);
        this.keywords.put("struct", TokenType.Keyword_struct);
    }
    Token follow(char expect, TokenType ifyes, TokenType ifno, int line, int pos) {
        if (getNextChar() == expect) {
            getNextChar();
            return new Token(ifyes, "", line, pos);
        }

        return new Token(ifno, "", line, pos);
    }
    Token char_lit(int line, int pos) {
        char c = getNextChar(); 
        int n = (int)c;

        if (c == '\\') {
            c = getNextChar();
            switch (c) {
                case 'n':
                    n = 10;
                    break;
                case '\\':
                    n = '\\';
                    break;
                default:
                    break;
            }
        }

        getNextChar();
        return new Token(TokenType.Integer, "" + n, line, pos);
    }
    Token string_lit(char start, int line, int pos) {
        String result = "";
        while (getNextChar() != start) {

            result += this.chr;
        }
        getNextChar();
        return new Token(TokenType.Literal, result, line, pos);
    }
    Token div_or_comment(int line, int pos) {
        if (getNextChar() != '*') {
            return new Token(TokenType.Op_divide, "", line, pos);
        }
        getNextChar();
        while (true) { 
            switch (this.chr) {

                case '*':
                    if (getNextChar() == '/') {
                        getNextChar();
                        return getToken();
                    }   break;
                default:
                    getNextChar();
                    break;
            }
        }
    }
    Token identifier_or_integer(int line, int pos) {
        boolean is_number = true;
        String text = "";
 
        while (Character.isAlphabetic(this.chr) || Character.isDigit(this.chr) || this.chr == '_') {
            text += this.chr;
            if (!Character.isDigit(this.chr)) {
                is_number = false;
            }
            getNextChar();
        }
 
        if (Character.isDigit(text.charAt(0))) {
            return new Token(TokenType.Integer, text, line, pos);
        }
 
        if (this.keywords.containsKey(text)) {
            return new Token(this.keywords.get(text), "", line, pos);
        }
        return new Token(TokenType.Identifier, text, line, pos);
    }
    Token getToken() {
        int line1, pos1;
        while (Character.isWhitespace(this.chr)) {
            getNextChar();
        }
        line1 = this.line;
        pos1 = this.pos;
 
        switch (this.chr) {
            case '\u0000': return new Token(TokenType.End_of_input, "", this.line, this.pos);
            case '/': return div_or_comment(line1, pos1);
            case '\'': return char_lit(line1, pos1);
            case '<': return follow('=', TokenType.Op_lessequal, TokenType.Op_less, line1, pos1);
            case '>': return follow('=', TokenType.Op_greaterequal, TokenType.Op_greater, line1, pos1);
            case '=': return follow('=', TokenType.Op_equal, TokenType.Op_assign, line1, pos1);
            case '!': return follow('=', TokenType.Op_notequal, TokenType.Op_not, line1, pos1);
            case '&': return follow('&', TokenType.Op_and, TokenType.End_of_input, line1, pos1);
            case '|': return follow('|', TokenType.Op_or, TokenType.End_of_input, line1, pos1);
            case '"': return string_lit(this.chr, line1, pos1);
            case '{': getNextChar(); return new Token(TokenType.Delimiter, "", line1, pos1);
            case '}': getNextChar(); return new Token(TokenType.Delimiter, "", line1, pos1);
            case '(': getNextChar(); return new Token(TokenType.Delimiter, "", line1, pos1);
            case ')': getNextChar(); return new Token(TokenType.Delimiter, "", line1, pos1);
            case '+': getNextChar(); return new Token(TokenType.Op_add, "", line1, pos1);
            case '-': getNextChar(); return new Token(TokenType.Op_subtract, "", line1, pos1);
            case '*': getNextChar(); return new Token(TokenType.Op_multiply, "", line1, pos1);
            case '%': getNextChar(); return new Token(TokenType.Op_mod, "", line1, pos1);
            case ';': getNextChar(); return new Token(TokenType.Delimiter, "", line1, pos1);
            case ',': getNextChar(); return new Token(TokenType.Delimiter, "", line1, pos1);
 
            default: return identifier_or_integer(line1, pos1);
        }
    }
 
    char getNextChar() {
        this.pos++;
        this.position++;
        if (this.position >= this.s.length()) {
            this.chr = '\u0000';
            return this.chr;
        }
        this.chr = this.s.charAt(this.position);
        if (this.chr == '\n') {
            this.pos = 0;
        }
        return this.chr;
    }
 
    void printTokens() {
        Token t;
        while ((t = getToken()).tokentype != TokenType.End_of_input) {
            System.out.println(t);
        }
        
    }
    public static void main(String[] args)throws Exception {
        BufferedReader reader;
        int count = 1;
        System.out.print("LINE NO. POSITION  TOKEN          VALUE\n");
        try{
            reader = new BufferedReader(new FileReader("C:\\Users\\sunid\\Desktop\\input.txt"));
        String line = reader.readLine(); 
       do{
              Lexer l = new Lexer(line, count);
              count++;
              l.printTokens();
              line = reader.readLine();
        } while(line != null);
        reader.close();
        System.out.println("END OF FILE");
        }
        catch(FileNotFoundException e) {
            System.out.println("Compilation error");
           }
    }
}

