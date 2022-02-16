// Derek Loza, Alejandro Maldonado, Matthew Moffitt

class Parser 
{
  public static void main(String[] args) 
  {
    String[] scanner_data = {args[0], "Automata.txt"};
    TokenScanner scn = new TokenScanner(scanner_data);
    TreeString output = new TreeString();

    if (Parse(scn, output))
    {
      output.Print();
    }
    else
    {
      System.out.print("Parse Error!");
    }
  }

  // --====================================================================-- //
  // Function: Parse                                                          //
  // Uses an input TokenScanner to parse a program based on a list of tokens. //
  // --====================================================================-- //
  public static boolean Parse(TokenScanner scn, TreeString str)
  {
    str.Add("<Program>\n");

    scn.Scan();

    boolean success;
    if (scn.Current().equals("IDKEYWORD") || scn.Current().equals("READ") || scn.Current().equals("WRITE") || scn.Current().equals("EOF"))
    {
      if (StmtList(scn, 1, str) && scn.Current().equals("EOF"))
      {
        success = true;
      }
      else
      {
        success = false;
      }
    }
    else
    {
      success = false;
    }

    str.Add("</Program>\n");

    return success;
  }

  // --====================================================================-- //
  // Function: StmtList                                                       //
  // Corresponds to <stmt_list> CFG node. Verifies each statement.            //
  // --====================================================================-- //
  public static boolean StmtList(TokenScanner scn, int depth, TreeString str)
  {
    str.Add(depth, "<stmt_list>\n");

    boolean success;
    if (scn.Current().equals("IDKEYWORD") || scn.Current().equals("READ") || scn.Current().equals("WRITE"))
    {
      success = Stmt(scn, depth+1, str) && StmtList(scn, depth+1, str);
    }
    else if (scn.Current().equals("EOF"))
    {
      success = true;
    }
    else
    {
      success = false;
    }

    str.Add(depth, "</stmt_list>\n");

    return success;
  }

  // --====================================================================-- //
  // Function: Stmt                                                           //
  // Verifies the correctness of a statement. Corresponds to <stmt> CFG node. //
  // --====================================================================-- //
  public static boolean Stmt(TokenScanner scn, int depth, TreeString str)
  {
    str.Add(depth, "<stmt>\n");

    boolean success;
    if (scn.Current().equals("IDKEYWORD"))
    {
      success = Match("IDKEYWORD", scn, depth+1, str) && Match("ASSIGN", scn, depth+1, str) && Expr(scn, depth+1, str);
    }
    else if (scn.Current().equals("READ"))
    {
      success = Match("READ", scn, depth+1, str) && Match("IDKEYWORD", scn, depth+1, str);
    }
    else if (scn.Current().equals("WRITE"))
    {
      success = Match("WRITE", scn, depth+1, str) && Expr(scn, depth+1, str);
    }
    else
    {
      success = false;
    }

    str.Add(depth, "</stmt>\n");

    return success;
  }

  // --====================================================================-- //
  // Function: Expr                                                           //
  // Verifies the correctness of an expression. Corresponds to <expr>.        //
  // --====================================================================-- //
  public static boolean Expr(TokenScanner scn, int depth, TreeString str)
  {
    str.Add(depth, "<expr>\n");

    boolean success;
    if (scn.Current().equals("LPAREN") || scn.Current().equals("IDKEYWORD") || scn.Current().equals("NUMBER"))
    {
      success = Term(scn, depth+1, str) && TermTail(scn, depth+1, str);
    }
    else
    {
      success = false;
    }

    str.Add(depth, "</expr>\n");

    return success;
  }

  // --====================================================================-- //
  // Function: Expr                                                           //
  // Verifies the correctness of an expression. Corresponds to <expr>.        //
  // --====================================================================-- //
  public static boolean Term(TokenScanner scn, int depth, TreeString str)
  {
    str.Add(depth, "<term>\n");

    boolean success;
    if (scn.Current().equals("LPAREN") || scn.Current().equals("IDKEYWORD") || scn.Current().equals("NUMBER"))
    {
      success = Factor(scn, depth+1, str) && FactorTail(scn, depth+1, str);
    }
    else
    {
      success = false;
    }

    str.Add(depth, "</term>\n");

    return success;
  }

  public static boolean TermTail(TokenScanner scn, int depth, TreeString str)
  {
    str.Add(depth, "<term_tail>\n");

    boolean success;
    if (scn.Current().equals("PLUS") || scn.Current().equals("MINUS"))
    {
      success = AddOp(scn, depth+1, str) && Term(scn, depth+1, str) && TermTail(scn, depth+1, str);
    }
    else
    {
      success = true;
    }

    str.Add(depth, "</term_tail>\n");

    return success;
  }

  public static boolean Factor(TokenScanner scn, int depth, TreeString str)
  {
    str.Add(depth, "<Factor>\n");

    boolean success;
    if (scn.Current().equals("LPAREN"))
    {
      success = Match("LPAREN", scn, depth+1, str) && Expr(scn, depth+1, str) && Match("RPAREN", scn, depth+1, str);
    }
    else if (scn.Current().equals("IDKEYWORD"))
    {
      success = Match("IDKEYWORD", scn, depth+1, str);
    }
    else if (scn.Current().equals("NUMBER"))
    {
      success = Match("NUMBER", scn, depth+1, str);
    }
    else
    {
      success = false;
    }

    str.Add(depth, "</Factor>\n");

    return success;
  }

  public static boolean FactorTail(TokenScanner scn, int depth, TreeString str)
  {
    str.Add(depth, "<factor_tail>\n");

    boolean success;
    if (scn.Current().equals("TIMES") || scn.Current().equals("DIV"))
    {
      success = MultOp(scn, depth+1, str) && Factor(scn, depth+1, str) && FactorTail(scn, depth+1, str);
    }
    else
    {
      success = true;
    }

    str.Add(depth, "</factor_tail>\n");

    return success;
  }

  public static boolean AddOp(TokenScanner scn, int depth, TreeString str)
  {
    str.Add(depth, "<add_op>\n");

    boolean success;
    if (scn.Current().equals("PLUS"))
    {
      success = Match("PLUS", scn, depth+1, str);
    }
    else if (scn.Current().equals("MINUS"))
    {
      success = Match("MINUS", scn, depth+1, str);
    }
    else
    {
      success = false;
    }

    str.Add(depth, "</add_op>\n");

    return success;
  }

  public static boolean MultOp(TokenScanner scn, int depth, TreeString str)
  {
    str.Add(depth, "<mult_op>\n");

    boolean success;
    if (scn.Current().equals("TIMES"))
    {
      success = Match("TIMES", scn, depth+1, str);
    }
    else if (scn.Current().equals("DIV"))
    {
      success = Match("DIV", scn, depth+1, str);
    }
    else
    {
      success = false;
    }

    str.Add(depth, "</mult_op>\n");

    return success;
  }


  // --====================================================================-- //
  // Function: Match                                                          //
  // Checks if the input token matches the most recent token on the           //
  // TokenScanner.                                                            //
  // --====================================================================-- //
  public static boolean Match(String tomatch, TokenScanner scn, int depth, TreeString str)
  {
    str.Add(depth, "<" + tomatch.toLowerCase() + ">\n");

    boolean success;
    if (scn.Current().equals(tomatch))
    {
      str.Add(depth+1, scn.CurrentValue() + "\n");
      scn.Scan();
      success = true;
    }
    else
    {
      success = false;
    }

    str.Add(depth, "</" + tomatch.toLowerCase() + ">\n");
    return success;
  }
}