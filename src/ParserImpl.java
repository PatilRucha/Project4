
public class ParserImpl extends Parser {

    /*
     * Implements a recursive-descent parser for the following CFG:
     * 
     * T -> F AddOp T              { if ($2.type == TokenType.PLUS) { $$ = new PlusExpr($1,$3); } else { $$ = new MinusExpr($1, $3); } }
     * T -> F                      { $$ = $1; }
     * F -> Lit MulOp F            { if ($2.type == TokenType.Times) { $$ = new TimesExpr($1,$3); } else { $$ = new DivExpr($1, $3); } }
     * F -> Lit                    { $$ = $1; }
     * Lit -> NUM                  { $$ = new FloatExpr(Float.parseFloat($1.lexeme)); }
     * Lit -> LPAREN T RPAREN      { $$ = $2; }
     * AddOp -> PLUS               { $$ = $1; }
     * AddOp -> MINUS              { $$ = $1; }
     * MulOp -> TIMES              { $$ = $1; }
     * MulOp -> DIV                { $$ = $1; }
     */
@Override
    public Expr do_parse() throws Exception {
        return parseT();
    }

    // Parse T -> F AddOp T | F
    private Expr parseT() throws Exception {
        Expr left = parseF();
        while (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
            Token op = parseAddOp();
            Expr right = parseF();  
            if (op.ty == TokenType.PLUS) {
             left = new PlusExpr(left, right);  
        }   else {
                left = new MinusExpr(left, right);
        }
    }
        return left; 
    }

    // Parse F -> Lit MulOp F | Lit
    private Expr parseF() throws Exception {
        Expr left = parseLit();
        while (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
            Token op = parseMulOp();
            Expr right = parseLit();  
            if (op.ty == TokenType.TIMES) {
                left = new TimesExpr(left, right);  
        }   else {
                left = new DivExpr(left, right);
        }
    }

        return left; 
    }

    // Parse Lit -> NUM | LPAREN T RPAREN
    private Expr parseLit() throws Exception {
        TokenType[] expectedTypes = {TokenType.NUM, TokenType.LPAREN};
        for (TokenType type : expectedTypes) {
            if (peek(type, 0)) {
                if (type == TokenType.NUM) {
                    Token num = consume(TokenType.NUM);
                    return new FloatExpr(Float.parseFloat(num.lexeme));
                } else if (type == TokenType.LPAREN) {
                    consume(TokenType.LPAREN);
                    Expr expr = parseT();
                    consume(TokenType.RPAREN);
                    return expr;
                }
            }
        }
        throw new Exception("Expected a literal or parenthesized expression");
    }


    // Parse AddOp -> PLUS | MINUS
    private Token parseAddOp() throws Exception {
        TokenType[] expectedOps = {TokenType.PLUS, TokenType.MINUS};
        for (TokenType opType : expectedOps) {
            if (peek(opType, 0)) {
                return consume(opType);
            }
        }
            throw new Exception("Expected an AddOp (PLUS or MINUS)");
    }
        

    // Parse MulOp -> TIMES | DIV
    private Token parseMulOp() throws Exception {
        TokenType[] expectedOps = {TokenType.TIMES, TokenType.DIV};
        for (TokenType opType : expectedOps) {
            if (peek(opType, 0)) {
                return consume(opType);
            }
         }
            throw new Exception("Expected a MulOp (TIMES or DIV)");
     }
}
