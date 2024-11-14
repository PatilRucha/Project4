public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * WHITE_SPACE (' '|\n|\r|\t)*
     */
    @Override
    protected void init_lexer() {
        Lexer lexer = new LexerImpl();

        // NUM automaton
        Automaton numAutomaton = new AutomatonImpl();
        numAutomaton.addState(0, true, false);
        numAutomaton.addState(1, false, false);
        numAutomaton.addState(2, false, true);

        for (int i = 0; i <= 9; i++) {
        char ch = (char) ('0' + i);  
        numAutomaton.addTransition(0, ch, 0);
        numAutomaton.addTransition(1, ch, 2);
        numAutomaton.addTransition(2, ch, 2);
        }

        numAutomaton.addTransition(0, '.', 1);

        lexer.add_automaton(TokenType.NUM, numAutomaton);

        char[] symbols = {'+', '-', '*', '/', '(', ')'};
        TokenType[] tokenTypes = {TokenType.PLUS, TokenType.MINUS, TokenType.TIMES, TokenType.DIV, TokenType.LPAREN, TokenType.RPAREN};

        for (int i = 0; i < symbols.length; i++) {
        Automaton automaton = createSimpleAutomaton(symbols[i]);
        lexer.add_automaton(tokenTypes[i], automaton);
        }

        Automaton wsAutomaton = new AutomatonImpl();
        wsAutomaton.addState(0, true, true);
        wsAutomaton.addTransition(0, ' ', 0);
        wsAutomaton.addTransition(0, '\n', 0);
        wsAutomaton.addTransition(0, '\r', 0);
        wsAutomaton.addTransition(0, '\t', 0);
        lexer.add_automaton(TokenType.WHITE_SPACE, wsAutomaton);

        this.lex = lexer;
    }

    private Automaton createSimpleAutomaton(char symbol) {
        Automaton automaton = new AutomatonImpl();
        automaton.addState(0, true, false);
        automaton.addState(1, false, true);
        automaton.addTransition(0, symbol, 1);
        return automaton;
    }

}
