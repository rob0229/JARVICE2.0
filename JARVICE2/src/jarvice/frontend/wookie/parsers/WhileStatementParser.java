package jarvice.frontend.wookie.parsers;

import java.util.EnumSet;

import jarvice.frontend.Token;
import jarvice.frontend.wookie.WookieParserTD;
import jarvice.frontend.wookie.WookieTokenType;
import jarvice.frontend.wookie.parsers.ExpressionParser;
import jarvice.frontend.wookie.parsers.StatementParser;
import jarvice.intermediate.ICodeFactory;
import jarvice.intermediate.ICodeNode;
import jarvice.intermediate.TypeSpec;
import jarvice.intermediate.symtabimpl.Predefined;
import jarvice.intermediate.typeimpl.TypeChecker;
import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;
import jarvice.intermediate.icodeimpl.*;
import jarvice.intermediate.symtabimpl.*;
import jarvice.intermediate.typeimpl.*;
import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static jarvice.frontend.wookie.WookieErrorCode.INCOMPATIBLE_TYPES;
import static jarvice.frontend.wookie.WookieTokenType.DO;
import static jarvice.frontend.wookie.WookieTokenType.LEFT_BRACE;
import static jarvice.intermediate.icodeimpl.ICodeNodeTypeImpl.LOOP;
import static jarvice.intermediate.icodeimpl.ICodeNodeTypeImpl.TEST;


/**
 * <h1>WhileStatementParser</h1>
 *
 * <p>Parse a Pascal WHILE statement.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class WhileStatementParser extends StatementParser
{
    /**
     * Constructor.
     * @param parent the parent parser.
     */
    public WhileStatementParser(WookieParserTD parent)
    {
        super(parent);
    }

    // Synchronization set for DO.
    private static final EnumSet<WookieTokenType> LEFT_BRACE_SET =
        StatementParser.STMT_START_SET.clone();
    static {
        LEFT_BRACE_SET.add(LEFT_BRACE);
        LEFT_BRACE_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    /**
     * Parse a WHILE statement.
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    public ICodeNode parse(Token token)
        throws Exception
    {
        token = nextToken();  // consume the WHILE

//NEED TO CHECK FOR LEFT_PAREN, EXPRESSION THEN RIGHT_PAREN?        
        
      
        // Create LOOP, TEST, and NOT nodes.
        ICodeNode loopNode = ICodeFactory.createICodeNode(LOOP);
        ICodeNode breakNode = ICodeFactory.createICodeNode(TEST);
        
//ROB ADDED THIS        
//DO NOT NEED NOT, c loops while(true), PASCAL loops while(FALSE)************************************        
//        ICodeNode notNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.NOT);
//***************************************************************************************************
        // The LOOP node adopts the TEST node as its first child.
        // The TEST node adopts the NOT node as its only child.
        loopNode.addChild(breakNode);
       // breakNode.addChild(notNode);

        // Parse the expression.
        // The NOT node adopts the expression subtree as its only child.
        ExpressionParser expressionParser = new ExpressionParser(this);
        ICodeNode exprNode = expressionParser.parse(token);
       // notNode.addChild(exprNode);
//Rob ADDED breaknode.addchild() to skip notNode.addChild and complete the tree*******************************************************
        breakNode.addChild(exprNode);
        
        // Type check: The test expression must be boolean.
        TypeSpec exprType = exprNode != null ? exprNode.getTypeSpec()
                                             : Predefined.undefinedType;
        if (!TypeChecker.isBoolean(exprType)) {
            errorHandler.flag(token, INCOMPATIBLE_TYPES, this);
        }

       
        
        
        // Synchronize at the DO.
        token = synchronize(LEFT_BRACE_SET);
        if (token.getType() == LEFT_BRACE) {
            token = nextToken();  // consume the LEFTBRACE
        }
        else {
            errorHandler.flag(token, MISSING_LEFT_BRACE, this);
        }

        // Parse the statement.
        // The LOOP node adopts the statement subtree as its second child.
        StatementParser statementParser = new StatementParser(this);
        loopNode.addChild(statementParser.parse(token));

      
        if (token.getType() == RIGHT_BRACE)
        	token=nextToken();
        else
        {
            errorHandler.flag(token, MISSING_LEFT_PAREN, this);
        }
        
        return loopNode;
    }
}
