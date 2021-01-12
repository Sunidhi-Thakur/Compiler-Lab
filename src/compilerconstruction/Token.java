package compilerconstruction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public enum Token {

    MINUS ("-"), 
    QUOTE_SINGLE("\'"),
    QUOTE_DOUBLE("\""),
    PLUS ("\\+"), 
    MUL ("\\*"), 
    DIV ("/"), 
    NOT ("~"), 
    AND ("&"),  
    OR ("\\|"),  
    LESS ("<"),
    LEG ("<="),
    GT (">"),
    GEQ (">="), 
    EQ ("=="),
    ASSIGN ("="),
    OPEN ("\\("),
    CLOSE ("\\)"), 
    SEMI (";"), 
    COMMA (","), 
    KEY_DEFINE ("define"), 
    KEY_PRINT ("printf"),
    KEY_SCAN ("scanf"),
    KEY_AS ("as"),
    KEY_IS ("is"),
    KEY_IF ("if"), 
    KEY_THEN ("then"), 
    KEY_ELSE ("else"), 
    KEY_ENDIF ("endif"),
    BRACKET ("\\{"),
    DELIMITER_CLOSE ("\\}"),
    DIFFERENT ("<>"),

    STRING ("\"[^\"]+\""),
    INTEGER ("\\d+"), 
    IDENTIFIER ("\\w+"),
    REAL ("(\\d*)\\.\\d+");

    private final Pattern pattern;

    Token(String regex) {
        pattern = Pattern.compile("^" + regex);
    }

    public int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);

        if (m.find()) {
            return m.end();
        }
        return -1;
    }
}
