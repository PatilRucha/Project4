# Instructions

Please only modify the files `AutomatonImpl.java`, `CompilerFrontendImpl.java`, and `ParserImpl.java`.
When we grade your solutions, we will replace all other files with our own versions, so please ensure all required code is in these 4 files.

## Part 1a

In the file `AutomatonImpl.java`, implement an interpreter for nondeterministic finite automata.
This interpreter must implement all of the abstract methods declared in `Automaton.java`.

## Part 1b

In the file `CompilerFrontendImpl.java`, construct finite automata corresponding to the following lexer specification:
```
NUM: [0-9]*\.[0-9]+
PLUS: \+
MINUS: -
TIMES: \*
DIV: /
LPAREN: \(
RPAREN: \)
WHITE_SPACE (' '|\n|\r|\t)*
```
Use these automata to construct a lexer of type `LexerImpl` (as defined in `LexerImpl.java`). An example of how to construct a lexer is given in the `test_lexer` function in `Tests.java`.

## Part 2

Write a recursive-descent parser for the following grammar using the given SDT rules:

```
T -> F AddOp T              { if ($2.type == TokenType.PLUS) { $$ = new PlusExpr($1,$3); } else { $$ = new MinusExpr($1, $3); } }
T -> F                      { $$ = $1; }
F -> Lit MulOp F            { if ($2.type == TokenType.Times) { $$ = new TimesExpr($1,$3); } else { $$ = new DivExpr($1, $3); } }
F -> Lit                    { $$ = $1; }
Lit -> NUM                  { $$ = new FloatExpr(Float.parseFloat($1.lexeme)); }
Lit -> LPAREN T RPAREN      { $$ = $2; }
AddOp -> PLUS               { $$ = $1; }
AddOp -> MINUS              { $$ = $1; }
MulOp -> TIMES              { $$ = $1; }
MulOp -> DIV                { $$ = $1; }
```

To implement the parser, you should implemment the `do_parse` method in `ParserImpl.java`, as well as any necessary helper methods.

For your convenience, you may also use the `peek` and `consume` methods that `ParserImpl` inherits from `Parser`.

## Expected Results

You can run the first part with `make test1` and the second `make test2`. The expected outputs are in `part1_out` and `part2_out`, respectively.
