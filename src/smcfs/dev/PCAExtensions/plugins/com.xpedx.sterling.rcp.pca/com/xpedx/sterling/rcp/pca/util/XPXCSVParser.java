package com.xpedx.sterling.rcp.pca.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XPXCSVParser
{

    public XPXCSVParser()
    {
        this(',', '"', '\\');
    }

    public XPXCSVParser(char c)
    {
        this(c, '"', '\\');
    }

    public XPXCSVParser(char c, char c1)
    {
        this(c, c1, '\\');
    }

    public XPXCSVParser(char c, char c1, char c2)
    {
        this(c, c1, c2, false);
    }

    public XPXCSVParser(char c, char c1, char c2, boolean flag)
    {
        separator = c;
        quotechar = c1;
        escape = c2;
        strictQuotes = flag;
    }

    public boolean isPending()
    {
        return pending != null;
    }

    public String[] parseLineMulti(String s)
        throws IOException
    {
        return parseLine(s, true);
    }

    public String[] parseLine(String s)
        throws IOException
    {
        return parseLine(s, false);
    }

    private String[] parseLine(String s, boolean flag)
        throws IOException
    {
        if(!flag && pending != null)
            pending = null;
        if(s == null)
            if(pending != null)
            {
                String s1 = pending;
                pending = null;
                return (new String[] {
                    s1
                });
            } else
            {
                return null;
            }
        ArrayList arraylist = new ArrayList();
        StringBuilder stringbuilder = new StringBuilder(128);
        boolean flag1 = false;
        if(pending != null)
        {
            stringbuilder.append(pending);
            pending = null;
            flag1 = true;
        }
        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if(c == escape)
            {
                if(isNextCharacterEscapable(s, flag1, i))
                {
                    stringbuilder.append(s.charAt(i + 1));
                    i++;
                }
                continue;
            }
            if(c == quotechar)
            {
                if(isNextCharacterEscapedQuote(s, flag1, i))
                {
                    stringbuilder.append(s.charAt(i + 1));
                    i++;
                    continue;
                }
                flag1 = !flag1;
                if(!strictQuotes && i > 2 && s.charAt(i - 1) != separator && s.length() > i + 1 && s.charAt(i + 1) != separator)
                    stringbuilder.append(c);
                continue;
            }
            if(c == separator && !flag1)
            {
                arraylist.add(stringbuilder.toString());
                stringbuilder = new StringBuilder(128);
                continue;
            }
            if(!strictQuotes || flag1)
                stringbuilder.append(c);
        }

        if(flag1)
            if(flag)
            {
                stringbuilder.append("\n");
                pending = stringbuilder.toString();
                stringbuilder = null;
            } else
            {
                throw new IOException("Un-terminated quoted field at end of CSV line");
            }
        if(stringbuilder != null)
            arraylist.add(stringbuilder.toString());
        return (String[])arraylist.toArray(new String[arraylist.size()]);
    }

    private boolean isNextCharacterEscapedQuote(String s, boolean flag, int i)
    {
        return flag && s.length() > i + 1 && s.charAt(i + 1) == quotechar;
    }

    protected boolean isNextCharacterEscapable(String s, boolean flag, int i)
    {
        return flag && s.length() > i + 1 && (s.charAt(i + 1) == quotechar || s.charAt(i + 1) == escape);
    }

    private final char separator;
    private final char quotechar;
    private final char escape;
    private final boolean strictQuotes;
    private String pending;
    public static final char DEFAULT_SEPARATOR = 44;
    public static final int INITIAL_READ_SIZE = 128;
    public static final char DEFAULT_QUOTE_CHARACTER = 34;
    public static final char DEFAULT_ESCAPE_CHARACTER = 92;
    public static final boolean DEFAULT_STRICT_QUOTES = false;
}
