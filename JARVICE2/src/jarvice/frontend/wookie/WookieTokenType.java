package jarvice.frontend.wookie;

import java.util.Hashtable;
import java.util.HashSet;

import jarvice.frontend.TokenType;

/**
 * <h1>PascalTokenType</h1>
 *
 * <p>Pascal token types.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public enum WookieTokenType implements TokenType
{
	 
     //WOOKIE reserved words
	
      AUTO, ELSE, LONG, SWITCH, BREAK, ENUM, REGISTER, TYPEDEF, CASE, EXTERN,
      RETURN, UNION, CHAR, FLOAT, SHORT, UNSIGNED, CONST, FOR, SIGNED, VOID,
      CONTINUE, GOTO, SIZEOF, VOLATILE, DEFAULT, IF, STATIC, WHILE, DO, INT,
      STRUCT, _PACKED, DOUBLE, _BOOL,
      
      /* Words in both PASCAL and WOOKIE
       AND, CASE, CONST, DO, ELSE, FOR, GOTO, IF, NOT, OR, WHILE
      */
      
      // PASCAL reserved words
      
      ARRAY, BEGIN, DIV, DOWNTO, END,
      FILE, FUNCTION, IN, LABEL, MOD, NIL,
      OF, PACKED, PROCEDURE, PROGRAM, RECORD, REPEAT, SET,
      THEN, TO, TYPE, UNTIL, VAR, WITH,
      
      
      
      //WOOKIE Special Symbols 
      
      //Arithmetic 
      PLUS("+"), MINUS("-"), PRODUCT("*"), DIVIDE("/"), MODULUS("%"),
      
      //Sequencing 
      COMMA(","), SEMICOLON(";"), COLON(":"),
      
      //Assignment 
      EQUALS("="), PLUS_EQUALS("+="), MINUS_EQUALS("-="),
      
      //Grouping 
      LEFT_PAREN("("), RIGHT_PAREN(")"),
      
      //Reference and Dereference 
      LEFT_BRACKET("["), RIGHT_BRACKET("]"), AMPERSAND("&"), STAR("*"), QUOTE("'"),
      
      //Equality Testing 
      EQUALS_EQUALS("=="), NOT_EQUALS("!="),
      
      //Boolean logic 
      AND("&&"), OR("||"), NOT("!"),
      
      //Order relation 
      LESS_THAN("<"), LESS_EQUALS("<="), GREATER_EQUALS(">="), GREATER_THAN(">"),
      LEFT_BRACE("{"), RIGHT_BRACE("}"), UP_ARROW("^"),
     

    // PASCAL Special symbols.
    
    /* Symbols in both PASCAL and WOOKIE
    
    COMMA(","), SEMICOLON(";"), COLON(":"), QUOTE("'"),
    EQUALS("="), NOT_EQUALS("<>"), LESS_THAN("<"), LESS_EQUALS("<="),
    GREATER_EQUALS(">="), GREATER_THAN(">"), LEFT_PAREN("("), RIGHT_PAREN(")"),
    LEFT_BRACKET("["), RIGHT_BRACKET("]"), LEFT_BRACE("{"), RIGHT_BRACE("}"),
    UP_ARROW("^"), STAR("*"), PLUS("+"), MINUS("-"), 
    
     */
    
    SLASH("/"), //COLON_EQUALS(":="),
    DOT("."), DOT_DOT(".."),

    IDENTIFIER, REAL, STRING,
    ERROR, END_OF_FILE;

    private static final int FIRST_RESERVED_INDEX = AUTO.ordinal();
    private static final int LAST_RESERVED_INDEX  = WITH.ordinal(); //pascal and wookie
    //private static final int LAST_RESERVED_INDEX  = WITH.ordinal(); //wookie only
   
    private static final int FIRST_SPECIAL_INDEX = PLUS.ordinal();
    private static final int LAST_SPECIAL_INDEX  = DOT_DOT.ordinal(); //pascal and wookie
    //private static final int LAST_SPECIAL_INDEX  = UP_ARROW.ordinal(); //wookie only
    
    private String text;  // token text

    /**
     * Constructor.
     */
    WookieTokenType()
    {
        this.text = this.toString().toLowerCase();
    }

    /**
     * Constructor.
     * @param text the token text.
     */
    WookieTokenType(String text)
    {
        this.text = text;
    }

    /**
     * Getter.
     * @return the token text.
     */
    public String getText()
    {
        return text;
    }

    // Set of lower-cased Pascal reserved word text strings.
    public static HashSet<String> RESERVED_WORDS = new HashSet<String>();
    static {
        WookieTokenType values[] = WookieTokenType.values();
        for (int i = FIRST_RESERVED_INDEX; i <= LAST_RESERVED_INDEX; ++i) {
            RESERVED_WORDS.add(values[i].getText().toLowerCase());
        }
    }

    // Hash table of Pascal special symbols.  Each special symbol's text
    // is the key to its Pascal token type.
    public static Hashtable<String, WookieTokenType> SPECIAL_SYMBOLS =
        new Hashtable<String, WookieTokenType>();
    static {
        WookieTokenType values[] = WookieTokenType.values();
        for (int i = FIRST_SPECIAL_INDEX; i <= LAST_SPECIAL_INDEX; ++i) {
            SPECIAL_SYMBOLS.put(values[i].getText(), values[i]);
        }
    }
}
