package jarvice.frontend.wookie.parsers;

import java.util.EnumSet;

import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;
import jarvice.intermediate.symtabimpl.*;
import jarvice.intermediate.typeimpl.*;

import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.icodeimpl.ICodeNodeTypeImpl.*;

/**
 * <h1>AssignmentStatementParser</h1>
 *
 * <p>Parse a Wookie assignment statement.</p>
 *
 */
public class AssignmentStatementParser extends StatementParser
{
    // Set to true to parse a function name
    // as the target of an assignment.
    private boolean isFunctionTarget = false;

    /**
     * Constructor.
     * @param parent the parent parser.
     */
    public AssignmentStatementParser(WookieParserTD parent)
    {
        super(parent);
    }

    // Synchronization set for the := token.
    private static final EnumSet<WookieTokenType> EQUALS_SET =
        ExpressionParser.EXPR_START_SET.clone();
    static {
        EQUALS_SET.add(EQUALS);
        EQUALS_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    /**
     * Parse an assignment statement.
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    public ICodeNode parse(Token token)
        throws Exception
    {
        // Create the ASSIGN node.
        ICodeNode assignNode = ICodeFactory.createICodeNode(ASSIGN);

        // Parse the target variable.
        VariableParser variableParser = new VariableParser(this);
        ICodeNode targetNode = isFunctionTarget
                               ? variableParser.parseFunctionNameTarget(token)
                               : variableParser.parse(token);
        TypeSpec targetType = targetNode != null ? targetNode.getTypeSpec()
                                                 : Predefined.undefinedType;

        // The ASSIGN node adopts the variable node as its first child.
        assignNode.addChild(targetNode);

        // Synchronize on the = token.
        token = synchronize(EQUALS_SET);
        if (token.getType() == EQUALS) {
            token = nextToken();  // consume the =
        }
        else {
            errorHandler.flag(token, MISSING_EQUALS, this);
        }

        // Parse the expression.  The ASSIGN node adopts the expression's
        // node as its second child.
        ExpressionParser expressionParser = new ExpressionParser(this);
        ICodeNode exprNode = expressionParser.parse(token);
        assignNode.addChild(exprNode);

        // Type check: Assignment compatible?
        TypeSpec exprType = exprNode != null ? exprNode.getTypeSpec()
                                             : Predefined.undefinedType;
        if (!TypeChecker.areAssignmentCompatible(targetType, exprType)) {
            errorHandler.flag(token, INCOMPATIBLE_TYPES, this);
        }

        assignNode.setTypeSpec(targetType);
        return assignNode;
    }

    /**
     * Parse an assignment to a function name.
     * @param token Token
     * @return ICodeNode
     * @throws Exception
     */
    public ICodeNode parseFunctionNameAssignment(Token token)
        throws Exception
    {
        isFunctionTarget = true;
        return parse(token);
    }
}
