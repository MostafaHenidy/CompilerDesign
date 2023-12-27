import java.util.ArrayList;
import java.util.List;

enum Token_type {
    VARIABLE, INTEGER, PLUS, MINUS, LPAREN, RPAREN, EOF
}

class Token {
    Token_type type;
    String value;

    public Token(Token_type type, String value) {
        this.type = type;
        this.value = value;
    }
}

public class SimpleLexer {
    private String input;
    private int currentPosition;

    public SimpleLexer(String input) {
        this.input = input;
        this.currentPosition = 0;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (currentPosition < input.length()) {
            char currentChar = input.charAt(currentPosition);
            if (Character.isWhitespace(currentChar)) {
                currentPosition++;
            } else if (Character.isLetter(currentChar)) {
                tokens.add(readVariable());
            } else if (Character.isDigit(currentChar)) {
                tokens.add(readInteger());
            } else if (currentChar == '+') {
                tokens.add(new Token(Token_type.PLUS, String.valueOf(currentChar)));
            } else if (currentChar == '-') {
                tokens.add(new Token(Token_type.MINUS, String.valueOf(currentChar)));
            } else if (currentChar == '(') {
                tokens.add(new Token(Token_type.LPAREN, String.valueOf(currentChar)));
            } else if (currentChar == ')') {
                tokens.add(new Token(Token_type.RPAREN, String.valueOf(currentChar)));
            } else {
                System.out.print("Error: Unrecognize character");
            }

        }
        tokens.add(new Token(Token_type.EOF, ""));
        return tokens;
    }

    private Token readInteger() {
        StringBuilder value = new StringBuilder();
        while (currentPosition < input.length() && Character.isDigit(input.charAt(currentPosition))) {
            value.append(input.charAt(currentPosition));
            currentPosition++;
        }
        return new Token(Token_type.INTEGER, value.toString());
    }

    private Token readVariable() {
        StringBuilder value = new StringBuilder();
        while (currentPosition < input.length() && Character.isLetterOrDigit(input.charAt(currentPosition))) {
            value.append(input.charAt(currentPosition));
            currentPosition++;
        }
        return new Token(Token_type.VARIABLE, value.toString());
    }
}

class SimpleParser {
    private List<Token> tokens;
    private int currentPosition;

    public SimpleParser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentPosition = 0;
    }

    public void parse() {
        expression();
    }

    private void expression() {
        term();
        while (match(Token_type.PLUS, Token_type.MINUS)) {
            Token operator = previous();
            term();
            
        }
    }

    private void term() {
        factor();
        while (match(Token_type.PLUS, Token_type.MINUS)) {
            Token operator = previous();
            factor();
            
        }
    }

    private void factor() {
        if (match(Token_type.INTEGER, Token_type.VARIABLE)) {
            
        } else if (match(Token_type.LPAREN)) {
            expression();
            consume(Token_type.RPAREN, "Expect ')' after expression.");
        } else {
            
        }
    }

    private boolean match(Token_type... types) {
        for (Token_type type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(Token_type type) {
        return !isAtEnd() && peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            currentPosition++;
        }
        return previous();
    }

    private Token peek() {
        return tokens.get(currentPosition);
    }

    private boolean isAtEnd() {
        return peek().type == Token_type.EOF;
    }

    private Token previous() {
        return tokens.get(currentPosition - 1);
    }

    private Token consume(Token_type type, String errorMessage) {
        if (check(type)) {
            return advance();
        } else {
            
            throw new RuntimeException(errorMessage);
        }
    }

    public static void main(String[] args) {
        String input = "y + 35 - (x + 1)";
        SimpleLexer lexer = new SimpleLexer(input);
        List<Token> tokens = lexer.tokenize();

        SimpleParser parser = new SimpleParser(tokens);
        parser.parse();
    }
}
