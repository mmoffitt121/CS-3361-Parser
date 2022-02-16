# Parser
## Preface
This program was created for CS-3361 Concepts of Programming Languages at Texas Tech University.
## Problem
For this assignment, we were instructed to create a parser based on a specific set of context-free grammar specifying the rules for the parser.

    <program> → <stmt list> $$
    <stmt list> → <stmt> <stmt list> | empty
    <stmt> → id assign <expr> | read id | write <expr>
    <expr> → <term> <term tail>
    <term tail> → <add op> <term> <term tail> | empty
    <term> → <factor> <fact tail>
    <fact tail> → <mult op> <factor> <fact tail> | empty
    <factor> → lparen <expr> rparen | id | number
    <add op> → plus | minus
    <mult op> → times | div


Previously, we were instructed to create an automata-based scanner that outputs a list of tokens if the program is scanned correctly, or [ERROR] if the program encounters an error. The program makes use of an input text file, and an input automata file.

The automata-based scanner is used in creating the token list for the parser.

## Usage

To compile and use, run 

    javac Parser.java

    java Parser <your program here.txt>

"your program here.txt" is to be replaced with the desire program to scan and parse.