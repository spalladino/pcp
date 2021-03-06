/* Generated By:JavaCC: Do not edit this line. PortaParser.java */
package porta.parser;

@SuppressWarnings("unchecked")
public class PortaParser implements PortaParserConstants {

        porta.model.BaseModel model;
        porta.processing.ITranslator translator;
        porta.model.BaseConstraint current;

        public PortaParser initialize(porta.model.BaseModel model, porta.processing.ITranslator t) {
                this.model = model;
                this.translator = t;
                this.current = null;
                return this;
        }

  final public porta.model.BaseModel ieq() throws ParseException {
    dimension();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LOWERB:
      case UPPERB:
      case VALID:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LOWERB:
        lower();
        break;
      case UPPERB:
        upper();
        break;
      case VALID:
        valid();
        break;
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    inequalities();
    end();
          {if (true) return this.model;}
    throw new Error("Missing return statement in function");
  }

  final public void inequalities() throws ParseException {
    jj_consume_token(INEQS);
    label_2:
    while (true) {
      inequality();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case MINUS:
      case LPAREN:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
    }
  }

  final public void inequality() throws ParseException {
        current = model.createConstraint();
        Integer c, b, i = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LPAREN:
      i = index();
      break;
    default:
      jj_la1[3] = jj_gen;
      ;
    }
    label_3:
    while (true) {
      term();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case MINUS:
        ;
        break;
      default:
        jj_la1[4] = jj_gen;
        break label_3;
      }
    }
    c = comparison();
    b = integer();
          current.withBound(b).withCompare(c).withIndex(String.valueOf(i));
  }

  final public Integer comparison() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EQUALS:
      jj_consume_token(EQUALS);
                       {if (true) return 0;}
      break;
    case LEQ:
      jj_consume_token(LEQ);
                      {if (true) return -1;}
      break;
    case GEQ:
      jj_consume_token(GEQ);
                      {if (true) return 1;}
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public void term() throws ParseException {
        int s = 1;
        int n = 1;
        int i = 0;
    s = sign();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUMBER:
      n = natural();
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
    jj_consume_token(VARNAME);
    i = natural();
                porta.model.BaseVariable var = translator.convertPortaToModel(i);
                current.addVar(var, s * n);
  }

  final public int integer() throws ParseException {
        int s = 1;
        int n = 1;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PLUS:
    case MINUS:
      s = sign();
      break;
    default:
      jj_la1[7] = jj_gen;
      ;
    }
    n = natural();
          {if (true) return s * n;}
    throw new Error("Missing return statement in function");
  }

  final public int natural() throws ParseException {
        Token t;
    t = jj_consume_token(NUMBER);
                         {if (true) return Integer.valueOf(t.image);}
    throw new Error("Missing return statement in function");
  }

  final public int sign() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PLUS:
      jj_consume_token(PLUS);
                          {if (true) return 1;}
      break;
    case MINUS:
      jj_consume_token(MINUS);
                          {if (true) return -1;}
      break;
    default:
      jj_la1[8] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public int index() throws ParseException {
        int i;
    jj_consume_token(LPAREN);
    i = natural();
    jj_consume_token(RPAREN);
          {if (true) return i;}
    throw new Error("Missing return statement in function");
  }

  final public void dimension() throws ParseException {
    jj_consume_token(DIM);
    jj_consume_token(ASSIGN);
    jj_consume_token(NUMBER);
  }

  final public void lower() throws ParseException {
    jj_consume_token(LOWERB);
    label_4:
    while (true) {
      jj_consume_token(NUMBER);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NUMBER:
        ;
        break;
      default:
        jj_la1[9] = jj_gen;
        break label_4;
      }
    }
  }

  final public void upper() throws ParseException {
    jj_consume_token(UPPERB);
    label_5:
    while (true) {
      jj_consume_token(NUMBER);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NUMBER:
        ;
        break;
      default:
        jj_la1[10] = jj_gen;
        break label_5;
      }
    }
  }

  final public void valid() throws ParseException {
    jj_consume_token(VALID);
    label_6:
    while (true) {
      jj_consume_token(NUMBER);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NUMBER:
        ;
        break;
      default:
        jj_la1[11] = jj_gen;
        break label_6;
      }
    }
  }

  final public void end() throws ParseException {
    jj_consume_token(END);
  }

  /** Generated Token Manager. */
  public PortaParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[12];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x1c000,0x1c000,0x860,0x800,0x60,0x380,0x100000,0x60,0x60,0x100000,0x100000,0x100000,};
   }

  /** Constructor with InputStream. */
  public PortaParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public PortaParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new PortaParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 12; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 12; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public PortaParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new PortaParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 12; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 12; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public PortaParser(PortaParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 12; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(PortaParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 12; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[21];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 12; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 21; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
