import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java_cup.runtime.Symbol;

%%
%public
%class QueryLexer
%implements sym
%unicode
%cup
%line
%column
%char

%{
  //variable to create symbol for scanner
  private ComplexSymbolFactory symbolFactory;

  public QueryLexer(java.io.Reader reader, ComplexSymbolFactory factory) {
         this(reader);
         symbolFactory = factory;
  }
  
  //method to create symbol with specific type
  public Symbol symbol(String name, int type) {
         return symbolFactory.newSymbol(name, type,
                                                  new Location(yyline+1, yycolumn+1, yychar), 
                                                  new Location(yyline+1, yycolumn+yylength(), yychar+yylength()) );
  }
  
  //method to create symbol with specific type and value
  public Symbol symbol(String name, int type, String value) {
         return symbolFactory.newSymbol(name, type, 
                                                  new Location(yyline+1, yycolumn+1, yychar),
                                                  new Location(yyline+1, yycolumn+yylength(), yychar+yylength()), value );
  }
%}

%eofval{
        return symbolFactory.newSymbol("EOF", sym.EOF, new Location(yyline+1, yycolumn+1, yychar), 
                                                       new Location(yyline+1, yycolumn+1, yychar+1) );
%eofval}

newline = \r|\n|\r\n
space = [\ \t]
attribute = [a-z][a-z0-9]*
table = [A-Z]+
number = [0-9]+

%%
"σ" { return symbol("SELECT", sym.SELECT, yytext()); }
"π" { return symbol("PROJECT", sym.PROJECT, yytext()); }
"⋃" { return symbol("UNION", sym.UNION, yytext()); }
"⋂" { return symbol("INTER", sym.INTER, yytext()); }
"⋊" { return symbol("RIGHT-JOIN", sym.RJOIN, yytext()); }
"⋉" { return symbol("LEFT-JOIN", sym.LJOIN, yytext()); }
"⋈" { return symbol("JOIN", sym.JOIN, yytext()); }
"x" { return symbol("PRODUCT", sym.PRODUCT, yytext()); }
">" { return symbol("GREATER", sym.GREATER, yytext()); }
"<" { return symbol("SMALLER", sym.SMALLER, yytext()); }
"=" { return symbol("EQUAL", sym.EQUAL, yytext()); }
">=" { return symbol("GEQUAL", sym.GEQUAL, yytext()); }
"<=" { return symbol("LEQUAL", sym.LEQUAL, yytext()); }
"!=" { return symbol("NEQUAL", sym.NEQUAL, yytext()); }
"," { return symbol("COMMA", sym.COMMA, yytext()); }
"(" { return symbol("LEFT", sym.LEFT, yytext()); }
")" { return symbol("RIGHT", sym.RIGHT, yytext()); }
"\"" { return symbol("QUOTE", sym.QUOTE, yytext()); }
{space} |
{newline} {  }
{number} { return symbol("NUMBER", sym.NUMBER, yytext()); }
{attribute} { return symbol("ATTRIBUTE", sym.ATTRIBUTE, yytext()); }
{table} { return symbol("TABLE", sym.TABLE, yytext()); }

. { return symbol("ERROR", sym.error); }