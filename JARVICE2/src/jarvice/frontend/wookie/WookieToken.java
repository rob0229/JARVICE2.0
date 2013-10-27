package jarvice.frontend.wookie;

import jarvice.frontend.*;

/**
 * <h1>PascalToken</h1>
 *
 * <p>Base class for Pascal token classes.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class WookieToken extends Token
{
    /**
     * Constructor.
     * @param source the source from where to fetch the token's characters.
     * @throws Exception if an error occurred.
     */
    protected WookieToken(Source source)
        throws Exception
    {
        super(source);
    }
}
