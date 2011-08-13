// $ANTLR 3.3 Nov 29, 2010 18:17:33 /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g 2011-08-02 23:51:05

package com.googlecode.goclipse.go.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class GoSourceParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "SEMI", "String_Lit", "BREAK", "CONTINUE", "FALLTHROUGH", "RETURN", "Integer", "Octal_Lit", "Hex_Lit", "Float_Lit", "Imaginary_Lit", "Char_Lit", "Identifier", "APPEND", "CAP", "CLOSE", "CLOSED", "CMPLX", "COPY", "IMAG", "LEN", "MAKE", "PANIC", "PRINT", "PRINTLN", "REAL", "RECOVER", "IF", "STRUCT", "INTERFACE", "TYPE", "VAR", "Unicode_Letter", "Unicode_Digit", "PLUSPLUS", "MINUSMINUS", "CLOSE_BRACKET", "CLOSE_SQUARE", "CLOSE_CURLY", "Decimal_Digit", "Hex_Digit", "Octal_Digit", "Letter", "Digit", "Decimals", "Exponent", "InterpretedString_Lit", "RawString_Lit", "Little_U_Value", "Big_U_Value", "Escaped_char", "Unicode_Value", "Octal_Byte_Value", "Hex_Byte_Value", "Byte_Value", "SingleLineComment", "MultiLineComment", "WS", "'package'", "'import'", "'('", "'.'", "'='", "'func'", "'*'", "','", "'...'", "'{'", "'else'", "'switch'", "':'", "'case'", "'default'", "':='", "'go'", "'goto'", "'defer'", "'select'", "'<-'", "'for'", "'range'", "'*='", "'/='", "'+='", "'-='", "'|='", "'^='", "'%='", "'<<='", "'>>='", "'&='", "'&^='", "'<-='", "'const'", "'recover'", "'['", "'+'", "'-'", "'^'", "'&'", "'/'", "'%'", "'<<'", "'>>'", "'&^'", "'|'", "'=='", "'!='", "'<'", "'<='", "'>'", "'>='", "'!'", "'&&'", "'||'", "'map'", "'chan'"
    };
    public static final int EOF=-1;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int T__73=73;
    public static final int T__74=74;
    public static final int T__75=75;
    public static final int T__76=76;
    public static final int T__77=77;
    public static final int T__78=78;
    public static final int T__79=79;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int T__84=84;
    public static final int T__85=85;
    public static final int T__86=86;
    public static final int T__87=87;
    public static final int T__88=88;
    public static final int T__89=89;
    public static final int T__90=90;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__93=93;
    public static final int T__94=94;
    public static final int T__95=95;
    public static final int T__96=96;
    public static final int T__97=97;
    public static final int T__98=98;
    public static final int T__99=99;
    public static final int T__100=100;
    public static final int T__101=101;
    public static final int T__102=102;
    public static final int T__103=103;
    public static final int T__104=104;
    public static final int T__105=105;
    public static final int T__106=106;
    public static final int T__107=107;
    public static final int T__108=108;
    public static final int T__109=109;
    public static final int T__110=110;
    public static final int T__111=111;
    public static final int T__112=112;
    public static final int T__113=113;
    public static final int T__114=114;
    public static final int T__115=115;
    public static final int T__116=116;
    public static final int T__117=117;
    public static final int T__118=118;
    public static final int T__119=119;
    public static final int T__120=120;
    public static final int SEMI=4;
    public static final int String_Lit=5;
    public static final int BREAK=6;
    public static final int CONTINUE=7;
    public static final int FALLTHROUGH=8;
    public static final int RETURN=9;
    public static final int Integer=10;
    public static final int Octal_Lit=11;
    public static final int Hex_Lit=12;
    public static final int Float_Lit=13;
    public static final int Imaginary_Lit=14;
    public static final int Char_Lit=15;
    public static final int Identifier=16;
    public static final int APPEND=17;
    public static final int CAP=18;
    public static final int CLOSE=19;
    public static final int CLOSED=20;
    public static final int CMPLX=21;
    public static final int COPY=22;
    public static final int IMAG=23;
    public static final int LEN=24;
    public static final int MAKE=25;
    public static final int PANIC=26;
    public static final int PRINT=27;
    public static final int PRINTLN=28;
    public static final int REAL=29;
    public static final int RECOVER=30;
    public static final int IF=31;
    public static final int STRUCT=32;
    public static final int INTERFACE=33;
    public static final int TYPE=34;
    public static final int VAR=35;
    public static final int Unicode_Letter=36;
    public static final int Unicode_Digit=37;
    public static final int PLUSPLUS=38;
    public static final int MINUSMINUS=39;
    public static final int CLOSE_BRACKET=40;
    public static final int CLOSE_SQUARE=41;
    public static final int CLOSE_CURLY=42;
    public static final int Decimal_Digit=43;
    public static final int Hex_Digit=44;
    public static final int Octal_Digit=45;
    public static final int Letter=46;
    public static final int Digit=47;
    public static final int Decimals=48;
    public static final int Exponent=49;
    public static final int InterpretedString_Lit=50;
    public static final int RawString_Lit=51;
    public static final int Little_U_Value=52;
    public static final int Big_U_Value=53;
    public static final int Escaped_char=54;
    public static final int Unicode_Value=55;
    public static final int Octal_Byte_Value=56;
    public static final int Hex_Byte_Value=57;
    public static final int Byte_Value=58;
    public static final int SingleLineComment=59;
    public static final int MultiLineComment=60;
    public static final int WS=61;

    // delegates
    // delegators


        public GoSourceParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public GoSourceParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return GoSourceParser.tokenNames; }
    public String getGrammarFileName() { return "/home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g"; }


    	public boolean hasErrors = false;
    	public CodeContext ctx;
    	public Scope root = new Scope();
    	public Scope currentScope = root;
    	public ArrayList<Integer> scopeName = new ArrayList<Integer>();
    	public int scopedepth = 0;
    	{
    	   for(int i = 0; i < 255; i++){
    	     scopeName.add(0);
    	   }
    	}
    	
    	public void reportError(RecognitionException e) {
    		super.reportError(e);
    		hasErrors = true;
    	}
    	
    	public void pushScope(){
    	   scopedepth++;
    	   Integer i = scopeName.get(scopedepth);
    	   if(i!=null){
    	      scopeName.set(scopedepth, i+1);
    	   }
    	   else{
    	      scopeName.add(1);
    	   }
    	   
    	   Scope newScope  = new Scope();
    	   newScope.name   = getScopeName();
    	   newScope.parent = currentScope;
    	   ctx.scopes.add(newScope);
    	   
    	   currentScope.children.add(newScope);
    	   currentScope    = newScope;
    	   
    //	   System.out.println(getScopeName());
    	}
    	
    	public void popScope(){
    //     System.out.println(currentScope.startLine+" to "+currentScope.stopLine);
         scopedepth--;
         currentScope = currentScope.parent;
      }
      
      public String getScopeName(){
         String ret = "1";
         for(int i = 2; i<=scopedepth; i++){
            Integer j = scopeName.get(i);
            ret += "."+j;
         }
         return ret;
      }


    public static class program_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "program"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:154:1: program : packageClause SEMI ( ( importDecl | topLevelDecl )? SEMI )* EOF ;
    public final GoSourceParser.program_return program() throws RecognitionException {
        GoSourceParser.program_return retval = new GoSourceParser.program_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token SEMI2=null;
        Token SEMI5=null;
        Token EOF6=null;
        GoSourceParser.packageClause_return packageClause1 = null;

        GoSourceParser.importDecl_return importDecl3 = null;

        GoSourceParser.topLevelDecl_return topLevelDecl4 = null;


        CommonTree SEMI2_tree=null;
        CommonTree SEMI5_tree=null;
        CommonTree EOF6_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:155:1: ( packageClause SEMI ( ( importDecl | topLevelDecl )? SEMI )* EOF )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:156:3: packageClause SEMI ( ( importDecl | topLevelDecl )? SEMI )* EOF
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_packageClause_in_program87);
            packageClause1=packageClause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, packageClause1.getTree());
            SEMI2=(Token)match(input,SEMI,FOLLOW_SEMI_in_program89); if (state.failed) return retval;
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:157:3: ( ( importDecl | topLevelDecl )? SEMI )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==SEMI||(LA2_0>=TYPE && LA2_0<=VAR)||LA2_0==63||LA2_0==67||LA2_0==97) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:158:5: ( importDecl | topLevelDecl )? SEMI
            	    {
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:158:5: ( importDecl | topLevelDecl )?
            	    int alt1=3;
            	    int LA1_0 = input.LA(1);

            	    if ( (LA1_0==63) ) {
            	        alt1=1;
            	    }
            	    else if ( ((LA1_0>=TYPE && LA1_0<=VAR)||LA1_0==67||LA1_0==97) ) {
            	        alt1=2;
            	    }
            	    switch (alt1) {
            	        case 1 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:159:7: importDecl
            	            {
            	            pushFollow(FOLLOW_importDecl_in_program108);
            	            importDecl3=importDecl();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, importDecl3.getTree());

            	            }
            	            break;
            	        case 2 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:160:9: topLevelDecl
            	            {
            	            pushFollow(FOLLOW_topLevelDecl_in_program118);
            	            topLevelDecl4=topLevelDecl();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, topLevelDecl4.getTree());

            	            }
            	            break;

            	    }

            	    SEMI5=(Token)match(input,SEMI,FOLLOW_SEMI_in_program131); if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            EOF6=(Token)match(input,EOF,FOLLOW_EOF_in_program141); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            EOF6_tree = (CommonTree)adaptor.create(EOF6);
            adaptor.addChild(root_0, EOF6_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "program"

    public static class packageClause_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "packageClause"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:167:1: packageClause : 'package' packageName ;
    public final GoSourceParser.packageClause_return packageClause() throws RecognitionException {
        GoSourceParser.packageClause_return retval = new GoSourceParser.packageClause_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal7=null;
        GoSourceParser.packageName_return packageName8 = null;


        CommonTree string_literal7_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:168:3: ( 'package' packageName )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:169:3: 'package' packageName
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal7=(Token)match(input,62,FOLLOW_62_in_packageClause156); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal7_tree = (CommonTree)adaptor.create(string_literal7);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal7_tree, root_0);
            }
            pushFollow(FOLLOW_packageName_in_packageClause159);
            packageName8=packageName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, packageName8.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "packageClause"

    public static class importDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "importDecl"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:172:1: importDecl : 'import' ( importSpec | '(' ( importSpec SEMI )* ( SEMI )? ')' ) ;
    public final GoSourceParser.importDecl_return importDecl() throws RecognitionException {
        GoSourceParser.importDecl_return retval = new GoSourceParser.importDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal9=null;
        Token char_literal11=null;
        Token SEMI13=null;
        Token SEMI14=null;
        Token char_literal15=null;
        GoSourceParser.importSpec_return importSpec10 = null;

        GoSourceParser.importSpec_return importSpec12 = null;


        CommonTree string_literal9_tree=null;
        CommonTree char_literal11_tree=null;
        CommonTree SEMI13_tree=null;
        CommonTree SEMI14_tree=null;
        CommonTree char_literal15_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:173:3: ( 'import' ( importSpec | '(' ( importSpec SEMI )* ( SEMI )? ')' ) )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:174:3: 'import' ( importSpec | '(' ( importSpec SEMI )* ( SEMI )? ')' )
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal9=(Token)match(input,63,FOLLOW_63_in_importDecl174); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal9_tree = (CommonTree)adaptor.create(string_literal9);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal9_tree, root_0);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:175:3: ( importSpec | '(' ( importSpec SEMI )* ( SEMI )? ')' )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==String_Lit||(LA5_0>=Identifier && LA5_0<=REAL)||LA5_0==65||LA5_0==98) ) {
                alt5=1;
            }
            else if ( (LA5_0==64) ) {
                alt5=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:176:5: importSpec
                    {
                    pushFollow(FOLLOW_importSpec_in_importDecl185);
                    importSpec10=importSpec();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, importSpec10.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:177:7: '(' ( importSpec SEMI )* ( SEMI )? ')'
                    {
                    char_literal11=(Token)match(input,64,FOLLOW_64_in_importDecl193); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal11_tree = (CommonTree)adaptor.create(char_literal11);
                    adaptor.addChild(root_0, char_literal11_tree);
                    }
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:177:11: ( importSpec SEMI )*
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( (LA3_0==String_Lit||(LA3_0>=Identifier && LA3_0<=REAL)||LA3_0==65||LA3_0==98) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:177:12: importSpec SEMI
                    	    {
                    	    pushFollow(FOLLOW_importSpec_in_importDecl196);
                    	    importSpec12=importSpec();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, importSpec12.getTree());
                    	    SEMI13=(Token)match(input,SEMI,FOLLOW_SEMI_in_importDecl198); if (state.failed) return retval;

                    	    }
                    	    break;

                    	default :
                    	    break loop3;
                        }
                    } while (true);

                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:177:35: ( SEMI )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==SEMI) ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:177:35: SEMI
                            {
                            SEMI14=(Token)match(input,SEMI,FOLLOW_SEMI_in_importDecl203); if (state.failed) return retval;

                            }
                            break;

                    }

                    char_literal15=(Token)match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_importDecl212); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal15_tree = (CommonTree)adaptor.create(char_literal15);
                    adaptor.addChild(root_0, char_literal15_tree);
                    }

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "importDecl"

    public static class importSpec_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "importSpec"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:182:1: importSpec : ( '.' | packageName )? importPath ;
    public final GoSourceParser.importSpec_return importSpec() throws RecognitionException {
        GoSourceParser.importSpec_return retval = new GoSourceParser.importSpec_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal16=null;
        GoSourceParser.packageName_return packageName17 = null;

        GoSourceParser.importPath_return importPath18 = null;


        CommonTree char_literal16_tree=null;


          Import imp   = new Import();
          imp.type     = Import.Type.NORMAL;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:187:3: ( ( '.' | packageName )? importPath )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:188:3: ( '.' | packageName )? importPath
            {
            root_0 = (CommonTree)adaptor.nil();

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:188:3: ( '.' | packageName )?
            int alt6=3;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==65) ) {
                alt6=1;
            }
            else if ( ((LA6_0>=Identifier && LA6_0<=REAL)||LA6_0==98) ) {
                alt6=2;
            }
            switch (alt6) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:189:5: '.'
                    {
                    char_literal16=(Token)match(input,65,FOLLOW_65_in_importSpec241); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal16_tree = (CommonTree)adaptor.create(char_literal16);
                    adaptor.addChild(root_0, char_literal16_tree);
                    }
                    if ( state.backtracking==0 ) {

                            imp.type = Import.Type.FULL_IMPORT;
                          
                    }

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:194:7: packageName
                    {
                    pushFollow(FOLLOW_packageName_in_importSpec280);
                    packageName17=packageName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, packageName17.getTree());
                    if ( state.backtracking==0 ) {

                            if(input.toString(retval.start,input.LT(-1)).contains("_")){
                              imp.type = Import.Type.INIT_ONLY;
                            }
                            else{
                              imp.type = Import.Type.ALIAS; imp.alias = (packageName17!=null?input.toString(packageName17.start,packageName17.stop):null);
                            }
                          
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_importPath_in_importSpec305);
            importPath18=importPath();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, importPath18.getTree());
            if ( state.backtracking==0 ) {

                  // emit import
                  imp.displayText= input.toString(retval.start,input.LT(-1));
                  imp.line       = ((Token)retval.start).getLine();
                  imp.filepath   = ctx.filename;
                  imp.start      = ((Token)retval.start).getCharPositionInLine()+1;
                  imp.importPath = (importPath18!=null?input.toString(importPath18.start,importPath18.stop):null);
                  imp.scopename  = getScopeName();
                  ctx.imports.add(imp);
                
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "importSpec"

    public static class importPath_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "importPath"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:217:1: importPath : String_Lit ;
    public final GoSourceParser.importPath_return importPath() throws RecognitionException {
        GoSourceParser.importPath_return retval = new GoSourceParser.importPath_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token String_Lit19=null;

        CommonTree String_Lit19_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:218:3: ( String_Lit )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:219:3: String_Lit
            {
            root_0 = (CommonTree)adaptor.nil();

            String_Lit19=(Token)match(input,String_Lit,FOLLOW_String_Lit_in_importPath324); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            String_Lit19_tree = (CommonTree)adaptor.create(String_Lit19);
            adaptor.addChild(root_0, String_Lit19_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "importPath"

    public static class topLevelDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "topLevelDecl"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:222:1: topLevelDecl : ( declaration | ( 'func' '(' )=> methodDecl | functionDecl );
    public final GoSourceParser.topLevelDecl_return topLevelDecl() throws RecognitionException {
        GoSourceParser.topLevelDecl_return retval = new GoSourceParser.topLevelDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.declaration_return declaration20 = null;

        GoSourceParser.methodDecl_return methodDecl21 = null;

        GoSourceParser.functionDecl_return functionDecl22 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:223:3: ( declaration | ( 'func' '(' )=> methodDecl | functionDecl )
            int alt7=3;
            int LA7_0 = input.LA(1);

            if ( ((LA7_0>=TYPE && LA7_0<=VAR)||LA7_0==97) ) {
                alt7=1;
            }
            else if ( (LA7_0==67) ) {
                int LA7_2 = input.LA(2);

                if ( (LA7_2==64) && (synpred1_GoSource())) {
                    alt7=2;
                }
                else if ( ((LA7_2>=Identifier && LA7_2<=REAL)||LA7_2==98) ) {
                    alt7=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:224:3: declaration
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_declaration_in_topLevelDecl339);
                    declaration20=declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, declaration20.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:225:5: ( 'func' '(' )=> methodDecl
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_methodDecl_in_topLevelDecl354);
                    methodDecl21=methodDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, methodDecl21.getTree());

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:226:5: functionDecl
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_functionDecl_in_topLevelDecl360);
                    functionDecl22=functionDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functionDecl22.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "topLevelDecl"

    public static class declaration_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "declaration"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:229:1: declaration : ( constDecl | typeDecl | varDecl );
    public final GoSourceParser.declaration_return declaration() throws RecognitionException {
        GoSourceParser.declaration_return retval = new GoSourceParser.declaration_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.constDecl_return constDecl23 = null;

        GoSourceParser.typeDecl_return typeDecl24 = null;

        GoSourceParser.varDecl_return varDecl25 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:230:3: ( constDecl | typeDecl | varDecl )
            int alt8=3;
            switch ( input.LA(1) ) {
            case 97:
                {
                alt8=1;
                }
                break;
            case TYPE:
                {
                alt8=2;
                }
                break;
            case VAR:
                {
                alt8=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:231:3: constDecl
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_constDecl_in_declaration375);
                    constDecl23=constDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, constDecl23.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:232:5: typeDecl
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_typeDecl_in_declaration381);
                    typeDecl24=typeDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeDecl24.getTree());

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:233:5: varDecl
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_varDecl_in_declaration387);
                    varDecl25=varDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, varDecl25.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "declaration"

    public static class typeDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typeDecl"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:236:1: typeDecl : 'type' ( typeSpec | '(' ( typeSpec SEMI )* ( SEMI )? ')' ) ;
    public final GoSourceParser.typeDecl_return typeDecl() throws RecognitionException {
        GoSourceParser.typeDecl_return retval = new GoSourceParser.typeDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal26=null;
        Token char_literal28=null;
        Token SEMI30=null;
        Token SEMI31=null;
        Token char_literal32=null;
        GoSourceParser.typeSpec_return typeSpec27 = null;

        GoSourceParser.typeSpec_return typeSpec29 = null;


        CommonTree string_literal26_tree=null;
        CommonTree char_literal28_tree=null;
        CommonTree SEMI30_tree=null;
        CommonTree SEMI31_tree=null;
        CommonTree char_literal32_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:237:3: ( 'type' ( typeSpec | '(' ( typeSpec SEMI )* ( SEMI )? ')' ) )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:238:3: 'type' ( typeSpec | '(' ( typeSpec SEMI )* ( SEMI )? ')' )
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal26=(Token)match(input,TYPE,FOLLOW_TYPE_in_typeDecl402); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal26_tree = (CommonTree)adaptor.create(string_literal26);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal26_tree, root_0);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:239:3: ( typeSpec | '(' ( typeSpec SEMI )* ( SEMI )? ')' )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( ((LA11_0>=Identifier && LA11_0<=REAL)||LA11_0==98) ) {
                alt11=1;
            }
            else if ( (LA11_0==64) ) {
                alt11=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:240:5: typeSpec
                    {
                    pushFollow(FOLLOW_typeSpec_in_typeDecl413);
                    typeSpec27=typeSpec();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeSpec27.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:241:7: '(' ( typeSpec SEMI )* ( SEMI )? ')'
                    {
                    char_literal28=(Token)match(input,64,FOLLOW_64_in_typeDecl421); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal28_tree = (CommonTree)adaptor.create(char_literal28);
                    adaptor.addChild(root_0, char_literal28_tree);
                    }
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:241:11: ( typeSpec SEMI )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( ((LA9_0>=Identifier && LA9_0<=REAL)||LA9_0==98) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:241:12: typeSpec SEMI
                    	    {
                    	    pushFollow(FOLLOW_typeSpec_in_typeDecl424);
                    	    typeSpec29=typeSpec();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeSpec29.getTree());
                    	    SEMI30=(Token)match(input,SEMI,FOLLOW_SEMI_in_typeDecl426); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    SEMI30_tree = (CommonTree)adaptor.create(SEMI30);
                    	    adaptor.addChild(root_0, SEMI30_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop9;
                        }
                    } while (true);

                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:241:32: ( SEMI )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==SEMI) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:241:32: SEMI
                            {
                            SEMI31=(Token)match(input,SEMI,FOLLOW_SEMI_in_typeDecl430); if (state.failed) return retval;

                            }
                            break;

                    }

                    char_literal32=(Token)match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_typeDecl439); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal32_tree = (CommonTree)adaptor.create(char_literal32);
                    adaptor.addChild(root_0, char_literal32_tree);
                    }

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "typeDecl"

    public static class typeSpec_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typeSpec"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:246:1: typeSpec : identifier type ;
    public final GoSourceParser.typeSpec_return typeSpec() throws RecognitionException {
        GoSourceParser.typeSpec_return retval = new GoSourceParser.typeSpec_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.identifier_return identifier33 = null;

        GoSourceParser.type_return type34 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:247:3: ( identifier type )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:248:3: identifier type
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_identifier_in_typeSpec458);
            identifier33=identifier();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, identifier33.getTree());
            pushFollow(FOLLOW_type_in_typeSpec460);
            type34=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type34.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "typeSpec"

    public static class varDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "varDecl"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:251:1: varDecl : 'var' ( varSpec | '(' ( varSpec SEMI )* ( SEMI )? ')' ) ;
    public final GoSourceParser.varDecl_return varDecl() throws RecognitionException {
        GoSourceParser.varDecl_return retval = new GoSourceParser.varDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal35=null;
        Token char_literal37=null;
        Token SEMI39=null;
        Token SEMI40=null;
        Token char_literal41=null;
        GoSourceParser.varSpec_return varSpec36 = null;

        GoSourceParser.varSpec_return varSpec38 = null;


        CommonTree string_literal35_tree=null;
        CommonTree char_literal37_tree=null;
        CommonTree SEMI39_tree=null;
        CommonTree SEMI40_tree=null;
        CommonTree char_literal41_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:252:3: ( 'var' ( varSpec | '(' ( varSpec SEMI )* ( SEMI )? ')' ) )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:253:3: 'var' ( varSpec | '(' ( varSpec SEMI )* ( SEMI )? ')' )
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal35=(Token)match(input,VAR,FOLLOW_VAR_in_varDecl475); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal35_tree = (CommonTree)adaptor.create(string_literal35);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal35_tree, root_0);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:254:3: ( varSpec | '(' ( varSpec SEMI )* ( SEMI )? ')' )
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( ((LA14_0>=Identifier && LA14_0<=REAL)||LA14_0==98) ) {
                alt14=1;
            }
            else if ( (LA14_0==64) ) {
                alt14=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:255:5: varSpec
                    {
                    pushFollow(FOLLOW_varSpec_in_varDecl486);
                    varSpec36=varSpec();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, varSpec36.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:256:7: '(' ( varSpec SEMI )* ( SEMI )? ')'
                    {
                    char_literal37=(Token)match(input,64,FOLLOW_64_in_varDecl494); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal37_tree = (CommonTree)adaptor.create(char_literal37);
                    root_0 = (CommonTree)adaptor.becomeRoot(char_literal37_tree, root_0);
                    }
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:256:12: ( varSpec SEMI )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( ((LA12_0>=Identifier && LA12_0<=REAL)||LA12_0==98) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:256:13: varSpec SEMI
                    	    {
                    	    pushFollow(FOLLOW_varSpec_in_varDecl498);
                    	    varSpec38=varSpec();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, varSpec38.getTree());
                    	    SEMI39=(Token)match(input,SEMI,FOLLOW_SEMI_in_varDecl500); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    SEMI39_tree = (CommonTree)adaptor.create(SEMI39);
                    	    adaptor.addChild(root_0, SEMI39_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);

                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:256:32: ( SEMI )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==SEMI) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:256:32: SEMI
                            {
                            SEMI40=(Token)match(input,SEMI,FOLLOW_SEMI_in_varDecl504); if (state.failed) return retval;

                            }
                            break;

                    }

                    char_literal41=(Token)match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_varDecl513); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal41_tree = (CommonTree)adaptor.create(char_literal41);
                    adaptor.addChild(root_0, char_literal41_tree);
                    }

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "varDecl"

    public static class varSpec_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "varSpec"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:261:1: varSpec : ilist= identifierList ( '=' expressionList | ( ( type '=' )=>a= type '=' expressionList | b= type ) ) ;
    public final GoSourceParser.varSpec_return varSpec() throws RecognitionException {
        GoSourceParser.varSpec_return retval = new GoSourceParser.varSpec_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal42=null;
        Token char_literal44=null;
        GoSourceParser.identifierList_return ilist = null;

        GoSourceParser.type_return a = null;

        GoSourceParser.type_return b = null;

        GoSourceParser.expressionList_return expressionList43 = null;

        GoSourceParser.expressionList_return expressionList45 = null;


        CommonTree char_literal42_tree=null;
        CommonTree char_literal44_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:262:3: (ilist= identifierList ( '=' expressionList | ( ( type '=' )=>a= type '=' expressionList | b= type ) ) )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:263:3: ilist= identifierList ( '=' expressionList | ( ( type '=' )=>a= type '=' expressionList | b= type ) )
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_identifierList_in_varSpec536);
            ilist=identifierList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, ilist.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:264:3: ( '=' expressionList | ( ( type '=' )=>a= type '=' expressionList | b= type ) )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==66) ) {
                alt16=1;
            }
            else if ( ((LA16_0>=Identifier && LA16_0<=REAL)||(LA16_0>=STRUCT && LA16_0<=INTERFACE)||LA16_0==64||(LA16_0>=67 && LA16_0<=68)||LA16_0==82||(LA16_0>=98 && LA16_0<=99)||(LA16_0>=119 && LA16_0<=120)) ) {
                alt16=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:265:5: '=' expressionList
                    {
                    char_literal42=(Token)match(input,66,FOLLOW_66_in_varSpec546); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal42_tree = (CommonTree)adaptor.create(char_literal42);
                    root_0 = (CommonTree)adaptor.becomeRoot(char_literal42_tree, root_0);
                    }
                    pushFollow(FOLLOW_expressionList_in_varSpec549);
                    expressionList43=expressionList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expressionList43.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:267:5: ( ( type '=' )=>a= type '=' expressionList | b= type )
                    {
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:267:5: ( ( type '=' )=>a= type '=' expressionList | b= type )
                    int alt15=2;
                    alt15 = dfa15.predict(input);
                    switch (alt15) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:268:7: ( type '=' )=>a= type '=' expressionList
                            {
                            pushFollow(FOLLOW_type_in_varSpec579);
                            a=type();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, a.getTree());
                            char_literal44=(Token)match(input,66,FOLLOW_66_in_varSpec581); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            char_literal44_tree = (CommonTree)adaptor.create(char_literal44);
                            root_0 = (CommonTree)adaptor.becomeRoot(char_literal44_tree, root_0);
                            }
                            pushFollow(FOLLOW_expressionList_in_varSpec584);
                            expressionList45=expressionList();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, expressionList45.getTree());
                            if ( state.backtracking==0 ) {

                                      for(Var var:ilist.vars){
                                        Type type        = new Type();
                                        type.displayText = (a!=null?input.toString(a.start,a.stop):null);
                                        var.type         = type;
                                        var.displayText  = var.insertionText +" : "+ type.displayText;
                                        currentScope.variables.add(var);
                                      }
                                    
                            }

                            }
                            break;
                        case 2 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:278:9: b= type
                            {
                            pushFollow(FOLLOW_type_in_varSpec604);
                            b=type();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, b.getTree());
                            if ( state.backtracking==0 ) {

                                      for(Var var:ilist.vars){
                                        Type type        = new Type();
                                        type.displayText = (b!=null?input.toString(b.start,b.stop):null);
                                        var.type         = type;
                                        var.displayText  = var.insertionText +" : "+ type.displayText;
                                        currentScope.variables.add(var);
                                      }
                                    
                            }

                            }
                            break;

                    }


                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "varSpec"

    public static class methodDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "methodDecl"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:292:1: methodDecl : 'func' receiver identifier signature ( body )? ;
    public final GoSourceParser.methodDecl_return methodDecl() throws RecognitionException {
        GoSourceParser.methodDecl_return retval = new GoSourceParser.methodDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal46=null;
        GoSourceParser.receiver_return receiver47 = null;

        GoSourceParser.identifier_return identifier48 = null;

        GoSourceParser.signature_return signature49 = null;

        GoSourceParser.body_return body50 = null;


        CommonTree string_literal46_tree=null;


          pushScope();
          currentScope.type = Scope.Type.FUNCTION;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:297:3: ( 'func' receiver identifier signature ( body )? )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:298:3: 'func' receiver identifier signature ( body )?
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal46=(Token)match(input,67,FOLLOW_67_in_methodDecl641); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal46_tree = (CommonTree)adaptor.create(string_literal46);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal46_tree, root_0);
            }
            pushFollow(FOLLOW_receiver_in_methodDecl644);
            receiver47=receiver();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, receiver47.getTree());
            pushFollow(FOLLOW_identifier_in_methodDecl646);
            identifier48=identifier();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, identifier48.getTree());
            pushFollow(FOLLOW_signature_in_methodDecl648);
            signature49=signature();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, signature49.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:298:41: ( body )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==71) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:298:41: body
                    {
                    pushFollow(FOLLOW_body_in_methodDecl650);
                    body50=body();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, body50.getTree());

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

                  // emit func
                  Func func          = new Func();
                  func.scopename     = getScopeName();
                  func.insertionText = (identifier48!=null?input.toString(identifier48.start,identifier48.stop):null);
                  func.displayText   = (receiver47!=null?receiver47.var:null).type.displayText +":"+(identifier48!=null?input.toString(identifier48.start,identifier48.stop):null) + " "+(signature49!=null?input.toString(signature49.start,signature49.stop):null).replace(";", "");
                  func.line          = ((Token)retval.start).getLine();
                  func.start         = ((Token)retval.start).getCharPositionInLine()+1; 
                  func.receiver      = (receiver47!=null?receiver47.var:null);
                  
                  // add method to parent, but vars to current scope
                  currentScope.parent.methods.add(func);
                  
                  currentScope.name      = getScopeName();
                  currentScope.start     = func.start;
                  currentScope.startLine = func.line;
                  currentScope.stop      = (body50!=null?body50.stopPosition:0);
                  currentScope.stopLine  = (body50!=null?body50.stopLine:0);
                  popScope();
                
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "methodDecl"

    public static class receiver_return extends ParserRuleReturnScope {
        public Var var;;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "receiver"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:321:1: receiver returns [Var var;] : '(' (a= identifier )? ( '*' )? b= identifier ( SEMI )? ')' ;
    public final GoSourceParser.receiver_return receiver() throws RecognitionException {
        GoSourceParser.receiver_return retval = new GoSourceParser.receiver_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal51=null;
        Token char_literal52=null;
        Token SEMI53=null;
        Token char_literal54=null;
        GoSourceParser.identifier_return a = null;

        GoSourceParser.identifier_return b = null;


        CommonTree char_literal51_tree=null;
        CommonTree char_literal52_tree=null;
        CommonTree SEMI53_tree=null;
        CommonTree char_literal54_tree=null;


          retval.var = new Var();
          retval.var.scopename   = getScopeName();
          retval.var.filepath    = ctx.filename;
          retval.var.line        = ((Token)retval.start).getLine();
          retval.var.start       = ((Token)retval.start).getCharPositionInLine()+1; 
          
        //  System.out.println(retval.var);

          

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:333:3: ( '(' (a= identifier )? ( '*' )? b= identifier ( SEMI )? ')' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:334:3: '(' (a= identifier )? ( '*' )? b= identifier ( SEMI )? ')'
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal51=(Token)match(input,64,FOLLOW_64_in_receiver677); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal51_tree = (CommonTree)adaptor.create(char_literal51);
            adaptor.addChild(root_0, char_literal51_tree);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:334:7: (a= identifier )?
            int alt18=2;
            alt18 = dfa18.predict(input);
            switch (alt18) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:334:8: a= identifier
                    {
                    pushFollow(FOLLOW_identifier_in_receiver682);
                    a=identifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, a.getTree());
                    if ( state.backtracking==0 ) {
                      retval.var.displayText = (a!=null?input.toString(a.start,a.stop):null)+" ";
                    }

                    }
                    break;

            }

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:335:7: ( '*' )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==68) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:335:8: '*'
                    {
                    char_literal52=(Token)match(input,68,FOLLOW_68_in_receiver696); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal52_tree = (CommonTree)adaptor.create(char_literal52);
                    adaptor.addChild(root_0, char_literal52_tree);
                    }
                    if ( state.backtracking==0 ) {
                      retval.var.isPointer=true;
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_identifier_in_receiver711);
            b=identifier();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, b.getTree());
            if ( state.backtracking==0 ) {
              retval.var.type.displayText = (b!=null?input.toString(b.start,b.stop):null);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:337:11: ( SEMI )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==SEMI) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:337:11: SEMI
                    {
                    SEMI53=(Token)match(input,SEMI,FOLLOW_SEMI_in_receiver721); if (state.failed) return retval;

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

                      retval.var.displayText   = (a!=null?input.toString(a.start,a.stop):null) + " : " + (retval.var.isPointer?"*":"") + (b!=null?input.toString(b.start,b.stop):null); 
                      retval.var.insertionText = (a!=null?input.toString(a.start,a.stop):null);
                      currentScope.variables.add(retval.var);
                    
            }
            char_literal54=(Token)match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_receiver739); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal54_tree = (CommonTree)adaptor.create(char_literal54);
            adaptor.addChild(root_0, char_literal54_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "receiver"

    public static class functionDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "functionDecl"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:347:1: functionDecl : 'func' identifier signature ( body )? ;
    public final GoSourceParser.functionDecl_return functionDecl() throws RecognitionException {
        GoSourceParser.functionDecl_return retval = new GoSourceParser.functionDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal55=null;
        GoSourceParser.identifier_return identifier56 = null;

        GoSourceParser.signature_return signature57 = null;

        GoSourceParser.body_return body58 = null;


        CommonTree string_literal55_tree=null;


          pushScope();
          currentScope.type = Scope.Type.FUNCTION;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:352:3: ( 'func' identifier signature ( body )? )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:353:3: 'func' identifier signature ( body )?
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal55=(Token)match(input,67,FOLLOW_67_in_functionDecl758); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal55_tree = (CommonTree)adaptor.create(string_literal55);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal55_tree, root_0);
            }
            pushFollow(FOLLOW_identifier_in_functionDecl761);
            identifier56=identifier();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, identifier56.getTree());
            pushFollow(FOLLOW_signature_in_functionDecl763);
            signature57=signature();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, signature57.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:353:32: ( body )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==71) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:353:32: body
                    {
                    pushFollow(FOLLOW_body_in_functionDecl765);
                    body58=body();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, body58.getTree());

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

                    // emit func
                  Func func          = new Func();
                  func.scopename     = getScopeName();
                  func.insertionText = (identifier56!=null?input.toString(identifier56.start,identifier56.stop):null);
                  func.displayText   = (identifier56!=null?input.toString(identifier56.start,identifier56.stop):null) + (signature57!=null?input.toString(signature57.start,signature57.stop):null).replace(";", ""); 
                  func.line          = ((Token)retval.start).getLine();
                  func.start         = ((Token)retval.start).getCharPositionInLine()+1;
                  
                  // add func to parent, but vars to current scope
                  currentScope.parent.functions.add(func);
                  
                  currentScope.name      = getScopeName();
                  currentScope.start     = func.start;
                  currentScope.startLine = func.line;
                  currentScope.stop      = (body58!=null?body58.stopPosition:0);
                  currentScope.stopLine  = (body58!=null?body58.stopLine:0);
                  System.out.println(">>>> "+currentScope.stopLine);
                  popScope();
                
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "functionDecl"

    public static class signature_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "signature"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:376:1: signature : ( ( parameters result )=> parameters result | parameters );
    public final GoSourceParser.signature_return signature() throws RecognitionException {
        GoSourceParser.signature_return retval = new GoSourceParser.signature_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.parameters_return parameters59 = null;

        GoSourceParser.result_return result60 = null;

        GoSourceParser.parameters_return parameters61 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:377:3: ( ( parameters result )=> parameters result | parameters )
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==64) ) {
                int LA22_1 = input.LA(2);

                if ( (synpred3_GoSource()) ) {
                    alt22=1;
                }
                else if ( (true) ) {
                    alt22=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 22, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:378:3: ( parameters result )=> parameters result
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_parameters_in_signature793);
                    parameters59=parameters();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, parameters59.getTree());
                    pushFollow(FOLLOW_result_in_signature795);
                    result60=result();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, result60.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:379:5: parameters
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_parameters_in_signature801);
                    parameters61=parameters();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, parameters61.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "signature"

    public static class result_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "result"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:382:1: result : ( ( parameters )=> parameters | type );
    public final GoSourceParser.result_return result() throws RecognitionException {
        GoSourceParser.result_return retval = new GoSourceParser.result_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.parameters_return parameters62 = null;

        GoSourceParser.type_return type63 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:383:3: ( ( parameters )=> parameters | type )
            int alt23=2;
            alt23 = dfa23.predict(input);
            switch (alt23) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:384:3: ( parameters )=> parameters
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_parameters_in_result822);
                    parameters62=parameters();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, parameters62.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:385:5: type
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_type_in_result828);
                    type63=type();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, type63.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "result"

    public static class parameters_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parameters"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:388:1: parameters : '(' ( parametersList ( ',' )? )? ( SEMI )? ')' ;
    public final GoSourceParser.parameters_return parameters() throws RecognitionException {
        GoSourceParser.parameters_return retval = new GoSourceParser.parameters_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal64=null;
        Token char_literal66=null;
        Token SEMI67=null;
        Token char_literal68=null;
        GoSourceParser.parametersList_return parametersList65 = null;


        CommonTree char_literal64_tree=null;
        CommonTree char_literal66_tree=null;
        CommonTree SEMI67_tree=null;
        CommonTree char_literal68_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:389:3: ( '(' ( parametersList ( ',' )? )? ( SEMI )? ')' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:390:3: '(' ( parametersList ( ',' )? )? ( SEMI )? ')'
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal64=(Token)match(input,64,FOLLOW_64_in_parameters843); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal64_tree = (CommonTree)adaptor.create(char_literal64);
            adaptor.addChild(root_0, char_literal64_tree);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:390:7: ( parametersList ( ',' )? )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( ((LA25_0>=Identifier && LA25_0<=REAL)||(LA25_0>=STRUCT && LA25_0<=INTERFACE)||LA25_0==64||(LA25_0>=67 && LA25_0<=68)||LA25_0==70||LA25_0==82||(LA25_0>=98 && LA25_0<=99)||(LA25_0>=119 && LA25_0<=120)) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:390:8: parametersList ( ',' )?
                    {
                    pushFollow(FOLLOW_parametersList_in_parameters846);
                    parametersList65=parametersList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, parametersList65.getTree());
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:390:23: ( ',' )?
                    int alt24=2;
                    int LA24_0 = input.LA(1);

                    if ( (LA24_0==69) ) {
                        alt24=1;
                    }
                    switch (alt24) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:390:23: ','
                            {
                            char_literal66=(Token)match(input,69,FOLLOW_69_in_parameters848); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            char_literal66_tree = (CommonTree)adaptor.create(char_literal66);
                            adaptor.addChild(root_0, char_literal66_tree);
                            }

                            }
                            break;

                    }


                    }
                    break;

            }

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:390:34: ( SEMI )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==SEMI) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:390:34: SEMI
                    {
                    SEMI67=(Token)match(input,SEMI,FOLLOW_SEMI_in_parameters853); if (state.failed) return retval;

                    }
                    break;

            }

            char_literal68=(Token)match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_parameters860); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal68_tree = (CommonTree)adaptor.create(char_literal68);
            adaptor.addChild(root_0, char_literal68_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "parameters"

    public static class parametersList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parametersList"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:394:1: parametersList : parameterDecl ( ',' parameterDecl )* ;
    public final GoSourceParser.parametersList_return parametersList() throws RecognitionException {
        GoSourceParser.parametersList_return retval = new GoSourceParser.parametersList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal70=null;
        GoSourceParser.parameterDecl_return parameterDecl69 = null;

        GoSourceParser.parameterDecl_return parameterDecl71 = null;


        CommonTree char_literal70_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:395:3: ( parameterDecl ( ',' parameterDecl )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:396:3: parameterDecl ( ',' parameterDecl )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_parameterDecl_in_parametersList875);
            parameterDecl69=parameterDecl();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, parameterDecl69.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:396:17: ( ',' parameterDecl )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==69) ) {
                    int LA27_1 = input.LA(2);

                    if ( ((LA27_1>=Identifier && LA27_1<=REAL)||(LA27_1>=STRUCT && LA27_1<=INTERFACE)||LA27_1==64||(LA27_1>=67 && LA27_1<=68)||LA27_1==70||LA27_1==82||(LA27_1>=98 && LA27_1<=99)||(LA27_1>=119 && LA27_1<=120)) ) {
                        alt27=1;
                    }


                }


                switch (alt27) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:396:18: ',' parameterDecl
            	    {
            	    char_literal70=(Token)match(input,69,FOLLOW_69_in_parametersList878); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal70_tree = (CommonTree)adaptor.create(char_literal70);
            	    adaptor.addChild(root_0, char_literal70_tree);
            	    }
            	    pushFollow(FOLLOW_parameterDecl_in_parametersList880);
            	    parameterDecl71=parameterDecl();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, parameterDecl71.getTree());

            	    }
            	    break;

            	default :
            	    break loop27;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "parametersList"

    public static class parameterDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parameterDecl"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:399:1: parameterDecl : ( ( identifierList ( '...' )? type )=>ilist= identifierList ( '...' )? type | ( '...' )? type );
    public final GoSourceParser.parameterDecl_return parameterDecl() throws RecognitionException {
        GoSourceParser.parameterDecl_return retval = new GoSourceParser.parameterDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal72=null;
        Token string_literal74=null;
        GoSourceParser.identifierList_return ilist = null;

        GoSourceParser.type_return type73 = null;

        GoSourceParser.type_return type75 = null;


        CommonTree string_literal72_tree=null;
        CommonTree string_literal74_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:400:3: ( ( identifierList ( '...' )? type )=>ilist= identifierList ( '...' )? type | ( '...' )? type )
            int alt30=2;
            alt30 = dfa30.predict(input);
            switch (alt30) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:401:3: ( identifierList ( '...' )? type )=>ilist= identifierList ( '...' )? type
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_identifierList_in_parameterDecl910);
                    ilist=identifierList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, ilist.getTree());
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:401:56: ( '...' )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==70) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:401:56: '...'
                            {
                            string_literal72=(Token)match(input,70,FOLLOW_70_in_parameterDecl912); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal72_tree = (CommonTree)adaptor.create(string_literal72);
                            adaptor.addChild(root_0, string_literal72_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_type_in_parameterDecl915);
                    type73=type();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, type73.getTree());
                    if ( state.backtracking==0 ) {

                              for(Var var:ilist.vars){
                                Type type        = new Type();
                                type.displayText = (type73!=null?input.toString(type73.start,type73.stop):null);
                                var.type         = type;
                                var.displayText  = var.insertionText +" : "+ type.displayText;
                      	        currentScope.variables.add(var);
                              }
                        
                    }

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:411:5: ( '...' )? type
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:411:5: ( '...' )?
                    int alt29=2;
                    int LA29_0 = input.LA(1);

                    if ( (LA29_0==70) ) {
                        alt29=1;
                    }
                    switch (alt29) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:411:5: '...'
                            {
                            string_literal74=(Token)match(input,70,FOLLOW_70_in_parameterDecl925); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal74_tree = (CommonTree)adaptor.create(string_literal74);
                            adaptor.addChild(root_0, string_literal74_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_type_in_parameterDecl928);
                    type75=type();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, type75.getTree());
                    if ( state.backtracking==0 ) {

                        
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "parameterDecl"

    public static class body_return extends ParserRuleReturnScope {
        public int stopLine;
        public int stopPosition;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "body"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:416:1: body returns [int stopLine, int stopPosition] : b= block ;
    public final GoSourceParser.body_return body() throws RecognitionException {
        GoSourceParser.body_return retval = new GoSourceParser.body_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.block_return b = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:417:3: (b= block )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:418:3: b= block
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_block_in_body953);
            b=block();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, b.getTree());
            if ( state.backtracking==0 ) {

                  retval.stopLine        = (b!=null?b.stopLine:0);
                  retval.stopPosition    = (b!=null?b.stopPosition:0);
                
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "body"

    public static class block_return extends ParserRuleReturnScope {
        public int stopLine;
        public int stopPosition;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "block"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:425:1: block returns [int stopLine, int stopPosition] : '{' ( statement SEMI )* a= '}' ;
    public final GoSourceParser.block_return block() throws RecognitionException {
        GoSourceParser.block_return retval = new GoSourceParser.block_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token a=null;
        Token char_literal76=null;
        Token SEMI78=null;
        GoSourceParser.statement_return statement77 = null;


        CommonTree a_tree=null;
        CommonTree char_literal76_tree=null;
        CommonTree SEMI78_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:426:3: ( '{' ( statement SEMI )* a= '}' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:427:3: '{' ( statement SEMI )* a= '}'
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal76=(Token)match(input,71,FOLLOW_71_in_block976); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal76_tree = (CommonTree)adaptor.create(char_literal76);
            root_0 = (CommonTree)adaptor.becomeRoot(char_literal76_tree, root_0);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:430:3: ( statement SEMI )*
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( ((LA31_0>=SEMI && LA31_0<=REAL)||(LA31_0>=IF && LA31_0<=VAR)||LA31_0==64||(LA31_0>=67 && LA31_0<=68)||LA31_0==71||LA31_0==73||(LA31_0>=78 && LA31_0<=83)||(LA31_0>=97 && LA31_0<=103)||LA31_0==116||(LA31_0>=119 && LA31_0<=120)) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:430:4: statement SEMI
            	    {
            	    pushFollow(FOLLOW_statement_in_block987);
            	    statement77=statement();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, statement77.getTree());
            	    SEMI78=(Token)match(input,SEMI,FOLLOW_SEMI_in_block989); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    SEMI78_tree = (CommonTree)adaptor.create(SEMI78);
            	    adaptor.addChild(root_0, SEMI78_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop31;
                }
            } while (true);

            a=(Token)match(input,CLOSE_CURLY,FOLLOW_CLOSE_CURLY_in_block1001); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            a_tree = (CommonTree)adaptor.create(a);
            adaptor.addChild(root_0, a_tree);
            }
            if ( state.backtracking==0 ) {

                  retval.stopLine        = a.getLine();
                  retval.stopPosition    = a.getCharPositionInLine();
                
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "block"

    public static class statement_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "statement"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:440:1: statement : ( ( labeledStatement )=> labeledStatement | declaration | deferStmt | returnStmt | goStmt | breakStmt | continueStmt | fallthroughStmt | block | ifStmt | switchStmt | selectStmt | gotoStmt | forStmt | simpleStmt );
    public final GoSourceParser.statement_return statement() throws RecognitionException {
        GoSourceParser.statement_return retval = new GoSourceParser.statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.labeledStatement_return labeledStatement79 = null;

        GoSourceParser.declaration_return declaration80 = null;

        GoSourceParser.deferStmt_return deferStmt81 = null;

        GoSourceParser.returnStmt_return returnStmt82 = null;

        GoSourceParser.goStmt_return goStmt83 = null;

        GoSourceParser.breakStmt_return breakStmt84 = null;

        GoSourceParser.continueStmt_return continueStmt85 = null;

        GoSourceParser.fallthroughStmt_return fallthroughStmt86 = null;

        GoSourceParser.block_return block87 = null;

        GoSourceParser.ifStmt_return ifStmt88 = null;

        GoSourceParser.switchStmt_return switchStmt89 = null;

        GoSourceParser.selectStmt_return selectStmt90 = null;

        GoSourceParser.gotoStmt_return gotoStmt91 = null;

        GoSourceParser.forStmt_return forStmt92 = null;

        GoSourceParser.simpleStmt_return simpleStmt93 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:441:3: ( ( labeledStatement )=> labeledStatement | declaration | deferStmt | returnStmt | goStmt | breakStmt | continueStmt | fallthroughStmt | block | ifStmt | switchStmt | selectStmt | gotoStmt | forStmt | simpleStmt )
            int alt32=15;
            alt32 = dfa32.predict(input);
            switch (alt32) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:442:3: ( labeledStatement )=> labeledStatement
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_labeledStatement_in_statement1029);
                    labeledStatement79=labeledStatement();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, labeledStatement79.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:443:5: declaration
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_declaration_in_statement1035);
                    declaration80=declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, declaration80.getTree());

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:444:5: deferStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_deferStmt_in_statement1041);
                    deferStmt81=deferStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, deferStmt81.getTree());

                    }
                    break;
                case 4 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:445:5: returnStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_returnStmt_in_statement1047);
                    returnStmt82=returnStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, returnStmt82.getTree());

                    }
                    break;
                case 5 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:446:5: goStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_goStmt_in_statement1053);
                    goStmt83=goStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, goStmt83.getTree());

                    }
                    break;
                case 6 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:447:5: breakStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_breakStmt_in_statement1059);
                    breakStmt84=breakStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, breakStmt84.getTree());

                    }
                    break;
                case 7 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:448:5: continueStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_continueStmt_in_statement1065);
                    continueStmt85=continueStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, continueStmt85.getTree());

                    }
                    break;
                case 8 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:449:5: fallthroughStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_fallthroughStmt_in_statement1071);
                    fallthroughStmt86=fallthroughStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, fallthroughStmt86.getTree());

                    }
                    break;
                case 9 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:450:5: block
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_block_in_statement1077);
                    block87=block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, block87.getTree());

                    }
                    break;
                case 10 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:451:5: ifStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_ifStmt_in_statement1083);
                    ifStmt88=ifStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, ifStmt88.getTree());

                    }
                    break;
                case 11 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:452:5: switchStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_switchStmt_in_statement1089);
                    switchStmt89=switchStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, switchStmt89.getTree());

                    }
                    break;
                case 12 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:453:5: selectStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_selectStmt_in_statement1095);
                    selectStmt90=selectStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, selectStmt90.getTree());

                    }
                    break;
                case 13 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:454:5: gotoStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_gotoStmt_in_statement1101);
                    gotoStmt91=gotoStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, gotoStmt91.getTree());

                    }
                    break;
                case 14 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:455:5: forStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_forStmt_in_statement1107);
                    forStmt92=forStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, forStmt92.getTree());

                    }
                    break;
                case 15 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:456:5: simpleStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_simpleStmt_in_statement1113);
                    simpleStmt93=simpleStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simpleStmt93.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "statement"

    public static class simpleStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "simpleStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:459:1: simpleStmt : ( ( assignment )=> assignment | ( shortVarDecl )=> shortVarDecl | ( incDecStmt )=> incDecStmt | ( expressionStmt )=> expressionStmt | );
    public final GoSourceParser.simpleStmt_return simpleStmt() throws RecognitionException {
        GoSourceParser.simpleStmt_return retval = new GoSourceParser.simpleStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.assignment_return assignment94 = null;

        GoSourceParser.shortVarDecl_return shortVarDecl95 = null;

        GoSourceParser.incDecStmt_return incDecStmt96 = null;

        GoSourceParser.expressionStmt_return expressionStmt97 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:460:3: ( ( assignment )=> assignment | ( shortVarDecl )=> shortVarDecl | ( incDecStmt )=> incDecStmt | ( expressionStmt )=> expressionStmt | )
            int alt33=5;
            alt33 = dfa33.predict(input);
            switch (alt33) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:461:3: ( assignment )=> assignment
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_assignment_in_simpleStmt1134);
                    assignment94=assignment();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, assignment94.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:462:5: ( shortVarDecl )=> shortVarDecl
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_shortVarDecl_in_simpleStmt1146);
                    shortVarDecl95=shortVarDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, shortVarDecl95.getTree());

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:463:5: ( incDecStmt )=> incDecStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_incDecStmt_in_simpleStmt1158);
                    incDecStmt96=incDecStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, incDecStmt96.getTree());

                    }
                    break;
                case 4 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:464:5: ( expressionStmt )=> expressionStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_expressionStmt_in_simpleStmt1170);
                    expressionStmt97=expressionStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expressionStmt97.getTree());

                    }
                    break;
                case 5 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:467:3: 
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "simpleStmt"

    public static class ifStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ifStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:469:1: ifStmt : 'if' ( ( simpleStmt SEMI )=> simpleStmt SEMI expression block | ( expression block )=> expression block | block ) ( 'else' statement )? ;
    public final GoSourceParser.ifStmt_return ifStmt() throws RecognitionException {
        GoSourceParser.ifStmt_return retval = new GoSourceParser.ifStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal98=null;
        Token SEMI100=null;
        Token string_literal106=null;
        GoSourceParser.simpleStmt_return simpleStmt99 = null;

        GoSourceParser.expression_return expression101 = null;

        GoSourceParser.block_return block102 = null;

        GoSourceParser.expression_return expression103 = null;

        GoSourceParser.block_return block104 = null;

        GoSourceParser.block_return block105 = null;

        GoSourceParser.statement_return statement107 = null;


        CommonTree string_literal98_tree=null;
        CommonTree SEMI100_tree=null;
        CommonTree string_literal106_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:470:3: ( 'if' ( ( simpleStmt SEMI )=> simpleStmt SEMI expression block | ( expression block )=> expression block | block ) ( 'else' statement )? )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:471:3: 'if' ( ( simpleStmt SEMI )=> simpleStmt SEMI expression block | ( expression block )=> expression block | block ) ( 'else' statement )?
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal98=(Token)match(input,IF,FOLLOW_IF_in_ifStmt1193); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal98_tree = (CommonTree)adaptor.create(string_literal98);
            adaptor.addChild(root_0, string_literal98_tree);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:472:3: ( ( simpleStmt SEMI )=> simpleStmt SEMI expression block | ( expression block )=> expression block | block )
            int alt34=3;
            alt34 = dfa34.predict(input);
            switch (alt34) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:473:5: ( simpleStmt SEMI )=> simpleStmt SEMI expression block
                    {
                    pushFollow(FOLLOW_simpleStmt_in_ifStmt1211);
                    simpleStmt99=simpleStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simpleStmt99.getTree());
                    SEMI100=(Token)match(input,SEMI,FOLLOW_SEMI_in_ifStmt1213); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SEMI100_tree = (CommonTree)adaptor.create(SEMI100);
                    adaptor.addChild(root_0, SEMI100_tree);
                    }
                    pushFollow(FOLLOW_expression_in_ifStmt1215);
                    expression101=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression101.getTree());
                    pushFollow(FOLLOW_block_in_ifStmt1217);
                    block102=block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, block102.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:474:7: ( expression block )=> expression block
                    {
                    pushFollow(FOLLOW_expression_in_ifStmt1233);
                    expression103=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression103.getTree());
                    pushFollow(FOLLOW_block_in_ifStmt1235);
                    block104=block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, block104.getTree());

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:475:7: block
                    {
                    pushFollow(FOLLOW_block_in_ifStmt1243);
                    block105=block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, block105.getTree());

                    }
                    break;

            }

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:477:3: ( 'else' statement )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==72) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:477:4: 'else' statement
                    {
                    string_literal106=(Token)match(input,72,FOLLOW_72_in_ifStmt1252); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal106_tree = (CommonTree)adaptor.create(string_literal106);
                    adaptor.addChild(root_0, string_literal106_tree);
                    }
                    pushFollow(FOLLOW_statement_in_ifStmt1254);
                    statement107=statement();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, statement107.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ifStmt"

    public static class switchStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "switchStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:480:1: switchStmt : ( ( typeSwitchStmt )=> typeSwitchStmt | ( exprSwitchStmt )=> exprSwitchStmt );
    public final GoSourceParser.switchStmt_return switchStmt() throws RecognitionException {
        GoSourceParser.switchStmt_return retval = new GoSourceParser.switchStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.typeSwitchStmt_return typeSwitchStmt108 = null;

        GoSourceParser.exprSwitchStmt_return exprSwitchStmt109 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:481:3: ( ( typeSwitchStmt )=> typeSwitchStmt | ( exprSwitchStmt )=> exprSwitchStmt )
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==73) ) {
                int LA36_1 = input.LA(2);

                if ( (synpred13_GoSource()) ) {
                    alt36=1;
                }
                else if ( (synpred14_GoSource()) ) {
                    alt36=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 36, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;
            }
            switch (alt36) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:482:3: ( typeSwitchStmt )=> typeSwitchStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_typeSwitchStmt_in_switchStmt1277);
                    typeSwitchStmt108=typeSwitchStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeSwitchStmt108.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:483:5: ( exprSwitchStmt )=> exprSwitchStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_exprSwitchStmt_in_switchStmt1289);
                    exprSwitchStmt109=exprSwitchStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, exprSwitchStmt109.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "switchStmt"

    public static class exprSwitchStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "exprSwitchStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:486:1: exprSwitchStmt : 'switch' ( ( simpleStmt SEMI expression )=> simpleStmt SEMI expression | ( simpleStmt SEMI )=> simpleStmt SEMI | expression )? '{' ( exprSwitchClause )* '}' ;
    public final GoSourceParser.exprSwitchStmt_return exprSwitchStmt() throws RecognitionException {
        GoSourceParser.exprSwitchStmt_return retval = new GoSourceParser.exprSwitchStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal110=null;
        Token SEMI112=null;
        Token SEMI115=null;
        Token char_literal117=null;
        Token char_literal119=null;
        GoSourceParser.simpleStmt_return simpleStmt111 = null;

        GoSourceParser.expression_return expression113 = null;

        GoSourceParser.simpleStmt_return simpleStmt114 = null;

        GoSourceParser.expression_return expression116 = null;

        GoSourceParser.exprSwitchClause_return exprSwitchClause118 = null;


        CommonTree string_literal110_tree=null;
        CommonTree SEMI112_tree=null;
        CommonTree SEMI115_tree=null;
        CommonTree char_literal117_tree=null;
        CommonTree char_literal119_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:487:3: ( 'switch' ( ( simpleStmt SEMI expression )=> simpleStmt SEMI expression | ( simpleStmt SEMI )=> simpleStmt SEMI | expression )? '{' ( exprSwitchClause )* '}' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:488:3: 'switch' ( ( simpleStmt SEMI expression )=> simpleStmt SEMI expression | ( simpleStmt SEMI )=> simpleStmt SEMI | expression )? '{' ( exprSwitchClause )* '}'
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal110=(Token)match(input,73,FOLLOW_73_in_exprSwitchStmt1304); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal110_tree = (CommonTree)adaptor.create(string_literal110);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal110_tree, root_0);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:489:3: ( ( simpleStmt SEMI expression )=> simpleStmt SEMI expression | ( simpleStmt SEMI )=> simpleStmt SEMI | expression )?
            int alt37=4;
            alt37 = dfa37.predict(input);
            switch (alt37) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:490:5: ( simpleStmt SEMI expression )=> simpleStmt SEMI expression
                    {
                    pushFollow(FOLLOW_simpleStmt_in_exprSwitchStmt1325);
                    simpleStmt111=simpleStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simpleStmt111.getTree());
                    SEMI112=(Token)match(input,SEMI,FOLLOW_SEMI_in_exprSwitchStmt1327); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SEMI112_tree = (CommonTree)adaptor.create(SEMI112);
                    adaptor.addChild(root_0, SEMI112_tree);
                    }
                    pushFollow(FOLLOW_expression_in_exprSwitchStmt1329);
                    expression113=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression113.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:491:7: ( simpleStmt SEMI )=> simpleStmt SEMI
                    {
                    pushFollow(FOLLOW_simpleStmt_in_exprSwitchStmt1345);
                    simpleStmt114=simpleStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simpleStmt114.getTree());
                    SEMI115=(Token)match(input,SEMI,FOLLOW_SEMI_in_exprSwitchStmt1347); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SEMI115_tree = (CommonTree)adaptor.create(SEMI115);
                    adaptor.addChild(root_0, SEMI115_tree);
                    }

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:492:7: expression
                    {
                    pushFollow(FOLLOW_expression_in_exprSwitchStmt1355);
                    expression116=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression116.getTree());

                    }
                    break;

            }

            char_literal117=(Token)match(input,71,FOLLOW_71_in_exprSwitchStmt1364); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal117_tree = (CommonTree)adaptor.create(char_literal117);
            adaptor.addChild(root_0, char_literal117_tree);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:494:7: ( exprSwitchClause )*
            loop38:
            do {
                int alt38=2;
                int LA38_0 = input.LA(1);

                if ( ((LA38_0>=75 && LA38_0<=76)) ) {
                    alt38=1;
                }


                switch (alt38) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:494:7: exprSwitchClause
            	    {
            	    pushFollow(FOLLOW_exprSwitchClause_in_exprSwitchStmt1366);
            	    exprSwitchClause118=exprSwitchClause();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, exprSwitchClause118.getTree());

            	    }
            	    break;

            	default :
            	    break loop38;
                }
            } while (true);

            char_literal119=(Token)match(input,CLOSE_CURLY,FOLLOW_CLOSE_CURLY_in_exprSwitchStmt1369); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal119_tree = (CommonTree)adaptor.create(char_literal119);
            adaptor.addChild(root_0, char_literal119_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "exprSwitchStmt"

    public static class exprSwitchClause_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "exprSwitchClause"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:497:1: exprSwitchClause : exprSwitchCase ':' ( statement SEMI )* ;
    public final GoSourceParser.exprSwitchClause_return exprSwitchClause() throws RecognitionException {
        GoSourceParser.exprSwitchClause_return retval = new GoSourceParser.exprSwitchClause_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal121=null;
        Token SEMI123=null;
        GoSourceParser.exprSwitchCase_return exprSwitchCase120 = null;

        GoSourceParser.statement_return statement122 = null;


        CommonTree char_literal121_tree=null;
        CommonTree SEMI123_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:498:3: ( exprSwitchCase ':' ( statement SEMI )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:499:3: exprSwitchCase ':' ( statement SEMI )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_exprSwitchCase_in_exprSwitchClause1384);
            exprSwitchCase120=exprSwitchCase();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, exprSwitchCase120.getTree());
            char_literal121=(Token)match(input,74,FOLLOW_74_in_exprSwitchClause1386); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal121_tree = (CommonTree)adaptor.create(char_literal121);
            adaptor.addChild(root_0, char_literal121_tree);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:499:22: ( statement SEMI )*
            loop39:
            do {
                int alt39=2;
                int LA39_0 = input.LA(1);

                if ( ((LA39_0>=SEMI && LA39_0<=REAL)||(LA39_0>=IF && LA39_0<=VAR)||LA39_0==64||(LA39_0>=67 && LA39_0<=68)||LA39_0==71||LA39_0==73||(LA39_0>=78 && LA39_0<=83)||(LA39_0>=97 && LA39_0<=103)||LA39_0==116||(LA39_0>=119 && LA39_0<=120)) ) {
                    alt39=1;
                }


                switch (alt39) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:499:23: statement SEMI
            	    {
            	    pushFollow(FOLLOW_statement_in_exprSwitchClause1389);
            	    statement122=statement();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, statement122.getTree());
            	    SEMI123=(Token)match(input,SEMI,FOLLOW_SEMI_in_exprSwitchClause1391); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    SEMI123_tree = (CommonTree)adaptor.create(SEMI123);
            	    adaptor.addChild(root_0, SEMI123_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop39;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "exprSwitchClause"

    public static class exprSwitchCase_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "exprSwitchCase"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:502:1: exprSwitchCase : ( 'case' expressionList | 'default' );
    public final GoSourceParser.exprSwitchCase_return exprSwitchCase() throws RecognitionException {
        GoSourceParser.exprSwitchCase_return retval = new GoSourceParser.exprSwitchCase_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal124=null;
        Token string_literal126=null;
        GoSourceParser.expressionList_return expressionList125 = null;


        CommonTree string_literal124_tree=null;
        CommonTree string_literal126_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:503:3: ( 'case' expressionList | 'default' )
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==75) ) {
                alt40=1;
            }
            else if ( (LA40_0==76) ) {
                alt40=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 40, 0, input);

                throw nvae;
            }
            switch (alt40) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:504:3: 'case' expressionList
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal124=(Token)match(input,75,FOLLOW_75_in_exprSwitchCase1408); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal124_tree = (CommonTree)adaptor.create(string_literal124);
                    root_0 = (CommonTree)adaptor.becomeRoot(string_literal124_tree, root_0);
                    }
                    pushFollow(FOLLOW_expressionList_in_exprSwitchCase1411);
                    expressionList125=expressionList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expressionList125.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:505:5: 'default'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal126=(Token)match(input,76,FOLLOW_76_in_exprSwitchCase1417); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal126_tree = (CommonTree)adaptor.create(string_literal126);
                    adaptor.addChild(root_0, string_literal126_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "exprSwitchCase"

    public static class typeSwitchStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typeSwitchStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:508:1: typeSwitchStmt : 'switch' ( ( simpleStmt SEMI )=> simpleStmt SEMI typeSwitchGuard | typeSwitchGuard ) '{' ( typeSwitchClause )* '}' ;
    public final GoSourceParser.typeSwitchStmt_return typeSwitchStmt() throws RecognitionException {
        GoSourceParser.typeSwitchStmt_return retval = new GoSourceParser.typeSwitchStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal127=null;
        Token SEMI129=null;
        Token char_literal132=null;
        Token char_literal134=null;
        GoSourceParser.simpleStmt_return simpleStmt128 = null;

        GoSourceParser.typeSwitchGuard_return typeSwitchGuard130 = null;

        GoSourceParser.typeSwitchGuard_return typeSwitchGuard131 = null;

        GoSourceParser.typeSwitchClause_return typeSwitchClause133 = null;


        CommonTree string_literal127_tree=null;
        CommonTree SEMI129_tree=null;
        CommonTree char_literal132_tree=null;
        CommonTree char_literal134_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:509:3: ( 'switch' ( ( simpleStmt SEMI )=> simpleStmt SEMI typeSwitchGuard | typeSwitchGuard ) '{' ( typeSwitchClause )* '}' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:510:3: 'switch' ( ( simpleStmt SEMI )=> simpleStmt SEMI typeSwitchGuard | typeSwitchGuard ) '{' ( typeSwitchClause )* '}'
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal127=(Token)match(input,73,FOLLOW_73_in_typeSwitchStmt1432); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal127_tree = (CommonTree)adaptor.create(string_literal127);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal127_tree, root_0);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:511:3: ( ( simpleStmt SEMI )=> simpleStmt SEMI typeSwitchGuard | typeSwitchGuard )
            int alt41=2;
            alt41 = dfa41.predict(input);
            switch (alt41) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:512:5: ( simpleStmt SEMI )=> simpleStmt SEMI typeSwitchGuard
                    {
                    pushFollow(FOLLOW_simpleStmt_in_typeSwitchStmt1451);
                    simpleStmt128=simpleStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simpleStmt128.getTree());
                    SEMI129=(Token)match(input,SEMI,FOLLOW_SEMI_in_typeSwitchStmt1453); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SEMI129_tree = (CommonTree)adaptor.create(SEMI129);
                    adaptor.addChild(root_0, SEMI129_tree);
                    }
                    pushFollow(FOLLOW_typeSwitchGuard_in_typeSwitchStmt1455);
                    typeSwitchGuard130=typeSwitchGuard();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeSwitchGuard130.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:513:7: typeSwitchGuard
                    {
                    pushFollow(FOLLOW_typeSwitchGuard_in_typeSwitchStmt1463);
                    typeSwitchGuard131=typeSwitchGuard();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeSwitchGuard131.getTree());

                    }
                    break;

            }

            char_literal132=(Token)match(input,71,FOLLOW_71_in_typeSwitchStmt1471); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal132_tree = (CommonTree)adaptor.create(char_literal132);
            adaptor.addChild(root_0, char_literal132_tree);
            }
            if ( state.backtracking==0 ) {
              pushScope();
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:517:8: ( typeSwitchClause )*
            loop42:
            do {
                int alt42=2;
                int LA42_0 = input.LA(1);

                if ( ((LA42_0>=75 && LA42_0<=76)) ) {
                    alt42=1;
                }


                switch (alt42) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:517:8: typeSwitchClause
            	    {
            	    pushFollow(FOLLOW_typeSwitchClause_in_typeSwitchStmt1484);
            	    typeSwitchClause133=typeSwitchClause();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeSwitchClause133.getTree());

            	    }
            	    break;

            	default :
            	    break loop42;
                }
            } while (true);

            if ( state.backtracking==0 ) {
              popScope();
            }
            char_literal134=(Token)match(input,CLOSE_CURLY,FOLLOW_CLOSE_CURLY_in_typeSwitchStmt1494); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal134_tree = (CommonTree)adaptor.create(char_literal134);
            adaptor.addChild(root_0, char_literal134_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "typeSwitchStmt"

    public static class typeSwitchGuard_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typeSwitchGuard"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:522:1: typeSwitchGuard : ( identifier ':=' )? primaryExpr '.' '(' 'type' ( SEMI )? ')' ;
    public final GoSourceParser.typeSwitchGuard_return typeSwitchGuard() throws RecognitionException {
        GoSourceParser.typeSwitchGuard_return retval = new GoSourceParser.typeSwitchGuard_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal136=null;
        Token char_literal138=null;
        Token char_literal139=null;
        Token string_literal140=null;
        Token SEMI141=null;
        Token char_literal142=null;
        GoSourceParser.identifier_return identifier135 = null;

        GoSourceParser.primaryExpr_return primaryExpr137 = null;


        CommonTree string_literal136_tree=null;
        CommonTree char_literal138_tree=null;
        CommonTree char_literal139_tree=null;
        CommonTree string_literal140_tree=null;
        CommonTree SEMI141_tree=null;
        CommonTree char_literal142_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:523:3: ( ( identifier ':=' )? primaryExpr '.' '(' 'type' ( SEMI )? ')' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:524:3: ( identifier ':=' )? primaryExpr '.' '(' 'type' ( SEMI )? ')'
            {
            root_0 = (CommonTree)adaptor.nil();

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:524:3: ( identifier ':=' )?
            int alt43=2;
            alt43 = dfa43.predict(input);
            switch (alt43) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:524:4: identifier ':='
                    {
                    pushFollow(FOLLOW_identifier_in_typeSwitchGuard1510);
                    identifier135=identifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identifier135.getTree());
                    string_literal136=(Token)match(input,77,FOLLOW_77_in_typeSwitchGuard1512); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal136_tree = (CommonTree)adaptor.create(string_literal136);
                    adaptor.addChild(root_0, string_literal136_tree);
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_primaryExpr_in_typeSwitchGuard1516);
            primaryExpr137=primaryExpr();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, primaryExpr137.getTree());
            char_literal138=(Token)match(input,65,FOLLOW_65_in_typeSwitchGuard1518); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal138_tree = (CommonTree)adaptor.create(char_literal138);
            adaptor.addChild(root_0, char_literal138_tree);
            }
            char_literal139=(Token)match(input,64,FOLLOW_64_in_typeSwitchGuard1520); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal139_tree = (CommonTree)adaptor.create(char_literal139);
            adaptor.addChild(root_0, char_literal139_tree);
            }
            string_literal140=(Token)match(input,TYPE,FOLLOW_TYPE_in_typeSwitchGuard1522); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal140_tree = (CommonTree)adaptor.create(string_literal140);
            adaptor.addChild(root_0, string_literal140_tree);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:524:53: ( SEMI )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==SEMI) ) {
                alt44=1;
            }
            switch (alt44) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:524:53: SEMI
                    {
                    SEMI141=(Token)match(input,SEMI,FOLLOW_SEMI_in_typeSwitchGuard1524); if (state.failed) return retval;

                    }
                    break;

            }

            char_literal142=(Token)match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_typeSwitchGuard1531); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal142_tree = (CommonTree)adaptor.create(char_literal142);
            adaptor.addChild(root_0, char_literal142_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "typeSwitchGuard"

    public static class typeSwitchClause_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typeSwitchClause"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:528:1: typeSwitchClause : typeSwitchCase ':' ( statement SEMI )* ;
    public final GoSourceParser.typeSwitchClause_return typeSwitchClause() throws RecognitionException {
        GoSourceParser.typeSwitchClause_return retval = new GoSourceParser.typeSwitchClause_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal144=null;
        Token SEMI146=null;
        GoSourceParser.typeSwitchCase_return typeSwitchCase143 = null;

        GoSourceParser.statement_return statement145 = null;


        CommonTree char_literal144_tree=null;
        CommonTree SEMI146_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:529:3: ( typeSwitchCase ':' ( statement SEMI )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:530:3: typeSwitchCase ':' ( statement SEMI )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_typeSwitchCase_in_typeSwitchClause1546);
            typeSwitchCase143=typeSwitchCase();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, typeSwitchCase143.getTree());
            char_literal144=(Token)match(input,74,FOLLOW_74_in_typeSwitchClause1548); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal144_tree = (CommonTree)adaptor.create(char_literal144);
            adaptor.addChild(root_0, char_literal144_tree);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:530:22: ( statement SEMI )*
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( ((LA45_0>=SEMI && LA45_0<=REAL)||(LA45_0>=IF && LA45_0<=VAR)||LA45_0==64||(LA45_0>=67 && LA45_0<=68)||LA45_0==71||LA45_0==73||(LA45_0>=78 && LA45_0<=83)||(LA45_0>=97 && LA45_0<=103)||LA45_0==116||(LA45_0>=119 && LA45_0<=120)) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:530:23: statement SEMI
            	    {
            	    pushFollow(FOLLOW_statement_in_typeSwitchClause1551);
            	    statement145=statement();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, statement145.getTree());
            	    SEMI146=(Token)match(input,SEMI,FOLLOW_SEMI_in_typeSwitchClause1553); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    SEMI146_tree = (CommonTree)adaptor.create(SEMI146);
            	    adaptor.addChild(root_0, SEMI146_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop45;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "typeSwitchClause"

    public static class typeSwitchCase_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typeSwitchCase"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:533:1: typeSwitchCase : ( 'case' typeList | 'default' );
    public final GoSourceParser.typeSwitchCase_return typeSwitchCase() throws RecognitionException {
        GoSourceParser.typeSwitchCase_return retval = new GoSourceParser.typeSwitchCase_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal147=null;
        Token string_literal149=null;
        GoSourceParser.typeList_return typeList148 = null;


        CommonTree string_literal147_tree=null;
        CommonTree string_literal149_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:534:3: ( 'case' typeList | 'default' )
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==75) ) {
                alt46=1;
            }
            else if ( (LA46_0==76) ) {
                alt46=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 46, 0, input);

                throw nvae;
            }
            switch (alt46) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:535:3: 'case' typeList
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal147=(Token)match(input,75,FOLLOW_75_in_typeSwitchCase1570); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal147_tree = (CommonTree)adaptor.create(string_literal147);
                    root_0 = (CommonTree)adaptor.becomeRoot(string_literal147_tree, root_0);
                    }
                    pushFollow(FOLLOW_typeList_in_typeSwitchCase1573);
                    typeList148=typeList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeList148.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:536:5: 'default'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal149=(Token)match(input,76,FOLLOW_76_in_typeSwitchCase1579); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal149_tree = (CommonTree)adaptor.create(string_literal149);
                    adaptor.addChild(root_0, string_literal149_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "typeSwitchCase"

    public static class typeList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typeList"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:539:1: typeList : type ( ',' type )* ;
    public final GoSourceParser.typeList_return typeList() throws RecognitionException {
        GoSourceParser.typeList_return retval = new GoSourceParser.typeList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal151=null;
        GoSourceParser.type_return type150 = null;

        GoSourceParser.type_return type152 = null;


        CommonTree char_literal151_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:540:3: ( type ( ',' type )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:541:3: type ( ',' type )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_type_in_typeList1595);
            type150=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type150.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:541:8: ( ',' type )*
            loop47:
            do {
                int alt47=2;
                int LA47_0 = input.LA(1);

                if ( (LA47_0==69) ) {
                    alt47=1;
                }


                switch (alt47) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:541:9: ',' type
            	    {
            	    char_literal151=(Token)match(input,69,FOLLOW_69_in_typeList1598); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal151_tree = (CommonTree)adaptor.create(char_literal151);
            	    adaptor.addChild(root_0, char_literal151_tree);
            	    }
            	    pushFollow(FOLLOW_type_in_typeList1600);
            	    type152=type();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, type152.getTree());

            	    }
            	    break;

            	default :
            	    break loop47;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "typeList"

    public static class goStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "goStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:544:1: goStmt : 'go' expression ;
    public final GoSourceParser.goStmt_return goStmt() throws RecognitionException {
        GoSourceParser.goStmt_return retval = new GoSourceParser.goStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal153=null;
        GoSourceParser.expression_return expression154 = null;


        CommonTree string_literal153_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:545:3: ( 'go' expression )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:546:3: 'go' expression
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal153=(Token)match(input,78,FOLLOW_78_in_goStmt1617); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal153_tree = (CommonTree)adaptor.create(string_literal153);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal153_tree, root_0);
            }
            pushFollow(FOLLOW_expression_in_goStmt1620);
            expression154=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression154.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "goStmt"

    public static class gotoStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "gotoStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:549:1: gotoStmt : 'goto' label ;
    public final GoSourceParser.gotoStmt_return gotoStmt() throws RecognitionException {
        GoSourceParser.gotoStmt_return retval = new GoSourceParser.gotoStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal155=null;
        GoSourceParser.label_return label156 = null;


        CommonTree string_literal155_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:550:3: ( 'goto' label )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:551:3: 'goto' label
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal155=(Token)match(input,79,FOLLOW_79_in_gotoStmt1635); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal155_tree = (CommonTree)adaptor.create(string_literal155);
            adaptor.addChild(root_0, string_literal155_tree);
            }
            pushFollow(FOLLOW_label_in_gotoStmt1637);
            label156=label();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, label156.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "gotoStmt"

    public static class breakStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "breakStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:554:1: breakStmt : BREAK ( label )? ;
    public final GoSourceParser.breakStmt_return breakStmt() throws RecognitionException {
        GoSourceParser.breakStmt_return retval = new GoSourceParser.breakStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token BREAK157=null;
        GoSourceParser.label_return label158 = null;


        CommonTree BREAK157_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:555:3: ( BREAK ( label )? )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:556:3: BREAK ( label )?
            {
            root_0 = (CommonTree)adaptor.nil();

            BREAK157=(Token)match(input,BREAK,FOLLOW_BREAK_in_breakStmt1652); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BREAK157_tree = (CommonTree)adaptor.create(BREAK157);
            adaptor.addChild(root_0, BREAK157_tree);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:556:9: ( label )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( ((LA48_0>=Identifier && LA48_0<=REAL)||LA48_0==98) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:556:9: label
                    {
                    pushFollow(FOLLOW_label_in_breakStmt1654);
                    label158=label();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, label158.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "breakStmt"

    public static class continueStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "continueStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:559:1: continueStmt : CONTINUE ( label )? ;
    public final GoSourceParser.continueStmt_return continueStmt() throws RecognitionException {
        GoSourceParser.continueStmt_return retval = new GoSourceParser.continueStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token CONTINUE159=null;
        GoSourceParser.label_return label160 = null;


        CommonTree CONTINUE159_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:560:3: ( CONTINUE ( label )? )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:561:3: CONTINUE ( label )?
            {
            root_0 = (CommonTree)adaptor.nil();

            CONTINUE159=(Token)match(input,CONTINUE,FOLLOW_CONTINUE_in_continueStmt1670); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            CONTINUE159_tree = (CommonTree)adaptor.create(CONTINUE159);
            adaptor.addChild(root_0, CONTINUE159_tree);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:561:12: ( label )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( ((LA49_0>=Identifier && LA49_0<=REAL)||LA49_0==98) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:561:12: label
                    {
                    pushFollow(FOLLOW_label_in_continueStmt1672);
                    label160=label();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, label160.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "continueStmt"

    public static class fallthroughStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fallthroughStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:564:1: fallthroughStmt : FALLTHROUGH ;
    public final GoSourceParser.fallthroughStmt_return fallthroughStmt() throws RecognitionException {
        GoSourceParser.fallthroughStmt_return retval = new GoSourceParser.fallthroughStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token FALLTHROUGH161=null;

        CommonTree FALLTHROUGH161_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:565:3: ( FALLTHROUGH )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:566:3: FALLTHROUGH
            {
            root_0 = (CommonTree)adaptor.nil();

            FALLTHROUGH161=(Token)match(input,FALLTHROUGH,FOLLOW_FALLTHROUGH_in_fallthroughStmt1688); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            FALLTHROUGH161_tree = (CommonTree)adaptor.create(FALLTHROUGH161);
            adaptor.addChild(root_0, FALLTHROUGH161_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "fallthroughStmt"

    public static class returnStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "returnStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:569:1: returnStmt : RETURN ( expressionList )? ;
    public final GoSourceParser.returnStmt_return returnStmt() throws RecognitionException {
        GoSourceParser.returnStmt_return retval = new GoSourceParser.returnStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token RETURN162=null;
        GoSourceParser.expressionList_return expressionList163 = null;


        CommonTree RETURN162_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:570:3: ( RETURN ( expressionList )? )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:571:3: RETURN ( expressionList )?
            {
            root_0 = (CommonTree)adaptor.nil();

            RETURN162=(Token)match(input,RETURN,FOLLOW_RETURN_in_returnStmt1703); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            RETURN162_tree = (CommonTree)adaptor.create(RETURN162);
            root_0 = (CommonTree)adaptor.becomeRoot(RETURN162_tree, root_0);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:571:11: ( expressionList )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==String_Lit||(LA50_0>=Integer && LA50_0<=REAL)||(LA50_0>=STRUCT && LA50_0<=INTERFACE)||LA50_0==64||(LA50_0>=67 && LA50_0<=68)||LA50_0==82||(LA50_0>=98 && LA50_0<=103)||LA50_0==116||(LA50_0>=119 && LA50_0<=120)) ) {
                alt50=1;
            }
            switch (alt50) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:571:11: expressionList
                    {
                    pushFollow(FOLLOW_expressionList_in_returnStmt1706);
                    expressionList163=expressionList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expressionList163.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "returnStmt"

    public static class deferStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "deferStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:574:1: deferStmt : 'defer' expression ;
    public final GoSourceParser.deferStmt_return deferStmt() throws RecognitionException {
        GoSourceParser.deferStmt_return retval = new GoSourceParser.deferStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal164=null;
        GoSourceParser.expression_return expression165 = null;


        CommonTree string_literal164_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:575:3: ( 'defer' expression )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:576:3: 'defer' expression
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal164=(Token)match(input,80,FOLLOW_80_in_deferStmt1722); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal164_tree = (CommonTree)adaptor.create(string_literal164);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal164_tree, root_0);
            }
            pushFollow(FOLLOW_expression_in_deferStmt1725);
            expression165=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression165.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "deferStmt"

    public static class assignment_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "assignment"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:579:1: assignment : expressionList assignOp expressionList ;
    public final GoSourceParser.assignment_return assignment() throws RecognitionException {
        GoSourceParser.assignment_return retval = new GoSourceParser.assignment_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.expressionList_return expressionList166 = null;

        GoSourceParser.assignOp_return assignOp167 = null;

        GoSourceParser.expressionList_return expressionList168 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:580:3: ( expressionList assignOp expressionList )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:581:3: expressionList assignOp expressionList
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_expressionList_in_assignment1740);
            expressionList166=expressionList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expressionList166.getTree());
            pushFollow(FOLLOW_assignOp_in_assignment1742);
            assignOp167=assignOp();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(assignOp167.getTree(), root_0);
            pushFollow(FOLLOW_expressionList_in_assignment1745);
            expressionList168=expressionList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expressionList168.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "assignment"

    public static class selectStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "selectStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:584:1: selectStmt : 'select' '{' ( commClause )* '}' ;
    public final GoSourceParser.selectStmt_return selectStmt() throws RecognitionException {
        GoSourceParser.selectStmt_return retval = new GoSourceParser.selectStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal169=null;
        Token char_literal170=null;
        Token char_literal172=null;
        GoSourceParser.commClause_return commClause171 = null;


        CommonTree string_literal169_tree=null;
        CommonTree char_literal170_tree=null;
        CommonTree char_literal172_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:585:3: ( 'select' '{' ( commClause )* '}' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:586:3: 'select' '{' ( commClause )* '}'
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal169=(Token)match(input,81,FOLLOW_81_in_selectStmt1760); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal169_tree = (CommonTree)adaptor.create(string_literal169);
            adaptor.addChild(root_0, string_literal169_tree);
            }
            char_literal170=(Token)match(input,71,FOLLOW_71_in_selectStmt1762); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal170_tree = (CommonTree)adaptor.create(char_literal170);
            adaptor.addChild(root_0, char_literal170_tree);
            }
            if ( state.backtracking==0 ) {
              pushScope();
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:586:31: ( commClause )*
            loop51:
            do {
                int alt51=2;
                int LA51_0 = input.LA(1);

                if ( ((LA51_0>=75 && LA51_0<=76)) ) {
                    alt51=1;
                }


                switch (alt51) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:586:31: commClause
            	    {
            	    pushFollow(FOLLOW_commClause_in_selectStmt1766);
            	    commClause171=commClause();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, commClause171.getTree());

            	    }
            	    break;

            	default :
            	    break loop51;
                }
            } while (true);

            if ( state.backtracking==0 ) {
              popScope();
            }
            char_literal172=(Token)match(input,CLOSE_CURLY,FOLLOW_CLOSE_CURLY_in_selectStmt1771); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal172_tree = (CommonTree)adaptor.create(char_literal172);
            adaptor.addChild(root_0, char_literal172_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "selectStmt"

    public static class commClause_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "commClause"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:589:1: commClause : commCase ':' ( statement SEMI )* ;
    public final GoSourceParser.commClause_return commClause() throws RecognitionException {
        GoSourceParser.commClause_return retval = new GoSourceParser.commClause_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal174=null;
        Token SEMI176=null;
        GoSourceParser.commCase_return commCase173 = null;

        GoSourceParser.statement_return statement175 = null;


        CommonTree char_literal174_tree=null;
        CommonTree SEMI176_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:590:3: ( commCase ':' ( statement SEMI )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:591:3: commCase ':' ( statement SEMI )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_commCase_in_commClause1786);
            commCase173=commCase();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, commCase173.getTree());
            char_literal174=(Token)match(input,74,FOLLOW_74_in_commClause1788); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal174_tree = (CommonTree)adaptor.create(char_literal174);
            adaptor.addChild(root_0, char_literal174_tree);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:591:16: ( statement SEMI )*
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( ((LA52_0>=SEMI && LA52_0<=REAL)||(LA52_0>=IF && LA52_0<=VAR)||LA52_0==64||(LA52_0>=67 && LA52_0<=68)||LA52_0==71||LA52_0==73||(LA52_0>=78 && LA52_0<=83)||(LA52_0>=97 && LA52_0<=103)||LA52_0==116||(LA52_0>=119 && LA52_0<=120)) ) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:591:17: statement SEMI
            	    {
            	    pushFollow(FOLLOW_statement_in_commClause1791);
            	    statement175=statement();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, statement175.getTree());
            	    SEMI176=(Token)match(input,SEMI,FOLLOW_SEMI_in_commClause1793); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    SEMI176_tree = (CommonTree)adaptor.create(SEMI176);
            	    adaptor.addChild(root_0, SEMI176_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop52;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "commClause"

    public static class commCase_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "commCase"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:594:1: commCase : ( 'case' ( ( sendExpr )=> sendExpr | ( recvExpr )=> recvExpr ) | 'default' );
    public final GoSourceParser.commCase_return commCase() throws RecognitionException {
        GoSourceParser.commCase_return retval = new GoSourceParser.commCase_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal177=null;
        Token string_literal180=null;
        GoSourceParser.sendExpr_return sendExpr178 = null;

        GoSourceParser.recvExpr_return recvExpr179 = null;


        CommonTree string_literal177_tree=null;
        CommonTree string_literal180_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:595:3: ( 'case' ( ( sendExpr )=> sendExpr | ( recvExpr )=> recvExpr ) | 'default' )
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==75) ) {
                alt54=1;
            }
            else if ( (LA54_0==76) ) {
                alt54=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 54, 0, input);

                throw nvae;
            }
            switch (alt54) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:596:3: 'case' ( ( sendExpr )=> sendExpr | ( recvExpr )=> recvExpr )
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal177=(Token)match(input,75,FOLLOW_75_in_commCase1810); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal177_tree = (CommonTree)adaptor.create(string_literal177);
                    adaptor.addChild(root_0, string_literal177_tree);
                    }
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:597:3: ( ( sendExpr )=> sendExpr | ( recvExpr )=> recvExpr )
                    int alt53=2;
                    alt53 = dfa53.predict(input);
                    switch (alt53) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:598:5: ( sendExpr )=> sendExpr
                            {
                            pushFollow(FOLLOW_sendExpr_in_commCase1826);
                            sendExpr178=sendExpr();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, sendExpr178.getTree());

                            }
                            break;
                        case 2 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:599:7: ( recvExpr )=> recvExpr
                            {
                            pushFollow(FOLLOW_recvExpr_in_commCase1840);
                            recvExpr179=recvExpr();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, recvExpr179.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:601:5: 'default'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal180=(Token)match(input,76,FOLLOW_76_in_commCase1850); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal180_tree = (CommonTree)adaptor.create(string_literal180);
                    adaptor.addChild(root_0, string_literal180_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "commCase"

    public static class sendExpr_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sendExpr"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:604:1: sendExpr : relational '<-' relational ;
    public final GoSourceParser.sendExpr_return sendExpr() throws RecognitionException {
        GoSourceParser.sendExpr_return retval = new GoSourceParser.sendExpr_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal182=null;
        GoSourceParser.relational_return relational181 = null;

        GoSourceParser.relational_return relational183 = null;


        CommonTree string_literal182_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:605:3: ( relational '<-' relational )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:606:3: relational '<-' relational
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_relational_in_sendExpr1865);
            relational181=relational();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, relational181.getTree());
            string_literal182=(Token)match(input,82,FOLLOW_82_in_sendExpr1867); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal182_tree = (CommonTree)adaptor.create(string_literal182);
            adaptor.addChild(root_0, string_literal182_tree);
            }
            pushFollow(FOLLOW_relational_in_sendExpr1869);
            relational183=relational();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, relational183.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "sendExpr"

    public static class recvExpr_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "recvExpr"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:609:1: recvExpr : ( ( expression ( '=' | ':=' ) '<-' )=> expression ( '=' | ':=' ) '<-' relational | '<-' relational );
    public final GoSourceParser.recvExpr_return recvExpr() throws RecognitionException {
        GoSourceParser.recvExpr_return retval = new GoSourceParser.recvExpr_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set185=null;
        Token string_literal186=null;
        Token string_literal188=null;
        GoSourceParser.expression_return expression184 = null;

        GoSourceParser.relational_return relational187 = null;

        GoSourceParser.relational_return relational189 = null;


        CommonTree set185_tree=null;
        CommonTree string_literal186_tree=null;
        CommonTree string_literal188_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:610:3: ( ( expression ( '=' | ':=' ) '<-' )=> expression ( '=' | ':=' ) '<-' relational | '<-' relational )
            int alt55=2;
            alt55 = dfa55.predict(input);
            switch (alt55) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:611:3: ( expression ( '=' | ':=' ) '<-' )=> expression ( '=' | ':=' ) '<-' relational
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_expression_in_recvExpr1938);
                    expression184=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression184.getTree());
                    set185=(Token)input.LT(1);
                    if ( input.LA(1)==66||input.LA(1)==77 ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set185));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    string_literal186=(Token)match(input,82,FOLLOW_82_in_recvExpr1964); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal186_tree = (CommonTree)adaptor.create(string_literal186);
                    adaptor.addChild(root_0, string_literal186_tree);
                    }
                    pushFollow(FOLLOW_relational_in_recvExpr1966);
                    relational187=relational();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, relational187.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:625:5: '<-' relational
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal188=(Token)match(input,82,FOLLOW_82_in_recvExpr1972); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal188_tree = (CommonTree)adaptor.create(string_literal188);
                    adaptor.addChild(root_0, string_literal188_tree);
                    }
                    pushFollow(FOLLOW_relational_in_recvExpr1974);
                    relational189=relational();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, relational189.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "recvExpr"

    public static class forStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "forStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:628:1: forStmt : 'for' ( ( initStmt SEMI )=> forClause | ( expression '{' )=> condition | rangeClause )? block ;
    public final GoSourceParser.forStmt_return forStmt() throws RecognitionException {
        GoSourceParser.forStmt_return retval = new GoSourceParser.forStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal190=null;
        GoSourceParser.forClause_return forClause191 = null;

        GoSourceParser.condition_return condition192 = null;

        GoSourceParser.rangeClause_return rangeClause193 = null;

        GoSourceParser.block_return block194 = null;


        CommonTree string_literal190_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:629:3: ( 'for' ( ( initStmt SEMI )=> forClause | ( expression '{' )=> condition | rangeClause )? block )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:630:3: 'for' ( ( initStmt SEMI )=> forClause | ( expression '{' )=> condition | rangeClause )? block
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal190=(Token)match(input,83,FOLLOW_83_in_forStmt1989); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal190_tree = (CommonTree)adaptor.create(string_literal190);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal190_tree, root_0);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:631:3: ( ( initStmt SEMI )=> forClause | ( expression '{' )=> condition | rangeClause )?
            int alt56=4;
            alt56 = dfa56.predict(input);
            switch (alt56) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:632:5: ( initStmt SEMI )=> forClause
                    {
                    pushFollow(FOLLOW_forClause_in_forStmt2008);
                    forClause191=forClause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, forClause191.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:633:7: ( expression '{' )=> condition
                    {
                    pushFollow(FOLLOW_condition_in_forStmt2024);
                    condition192=condition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, condition192.getTree());

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:634:7: rangeClause
                    {
                    pushFollow(FOLLOW_rangeClause_in_forStmt2032);
                    rangeClause193=rangeClause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, rangeClause193.getTree());

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_forStmt2041);
            block194=block();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, block194.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "forStmt"

    public static class condition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "condition"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:639:1: condition : expression ;
    public final GoSourceParser.condition_return condition() throws RecognitionException {
        GoSourceParser.condition_return retval = new GoSourceParser.condition_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.expression_return expression195 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:640:3: ( expression )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:641:3: expression
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_expression_in_condition2056);
            expression195=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression195.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "condition"

    public static class forClause_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "forClause"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:644:1: forClause : ( ( initStmt SEMI )=> initStmt SEMI | SEMI ) ( ( condition SEMI )=> condition SEMI | SEMI ) ( postStmt )=> postStmt ;
    public final GoSourceParser.forClause_return forClause() throws RecognitionException {
        GoSourceParser.forClause_return retval = new GoSourceParser.forClause_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token SEMI197=null;
        Token SEMI198=null;
        Token SEMI200=null;
        Token SEMI201=null;
        GoSourceParser.initStmt_return initStmt196 = null;

        GoSourceParser.condition_return condition199 = null;

        GoSourceParser.postStmt_return postStmt202 = null;


        CommonTree SEMI197_tree=null;
        CommonTree SEMI198_tree=null;
        CommonTree SEMI200_tree=null;
        CommonTree SEMI201_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:645:3: ( ( ( initStmt SEMI )=> initStmt SEMI | SEMI ) ( ( condition SEMI )=> condition SEMI | SEMI ) ( postStmt )=> postStmt )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:646:3: ( ( initStmt SEMI )=> initStmt SEMI | SEMI ) ( ( condition SEMI )=> condition SEMI | SEMI ) ( postStmt )=> postStmt
            {
            root_0 = (CommonTree)adaptor.nil();

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:646:3: ( ( initStmt SEMI )=> initStmt SEMI | SEMI )
            int alt57=2;
            alt57 = dfa57.predict(input);
            switch (alt57) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:647:5: ( initStmt SEMI )=> initStmt SEMI
                    {
                    pushFollow(FOLLOW_initStmt_in_forClause2085);
                    initStmt196=initStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, initStmt196.getTree());
                    SEMI197=(Token)match(input,SEMI,FOLLOW_SEMI_in_forClause2087); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SEMI197_tree = (CommonTree)adaptor.create(SEMI197);
                    adaptor.addChild(root_0, SEMI197_tree);
                    }

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:648:7: SEMI
                    {
                    SEMI198=(Token)match(input,SEMI,FOLLOW_SEMI_in_forClause2095); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SEMI198_tree = (CommonTree)adaptor.create(SEMI198);
                    adaptor.addChild(root_0, SEMI198_tree);
                    }

                    }
                    break;

            }

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:650:3: ( ( condition SEMI )=> condition SEMI | SEMI )
            int alt58=2;
            alt58 = dfa58.predict(input);
            switch (alt58) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:651:5: ( condition SEMI )=> condition SEMI
                    {
                    pushFollow(FOLLOW_condition_in_forClause2117);
                    condition199=condition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, condition199.getTree());
                    SEMI200=(Token)match(input,SEMI,FOLLOW_SEMI_in_forClause2119); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SEMI200_tree = (CommonTree)adaptor.create(SEMI200);
                    adaptor.addChild(root_0, SEMI200_tree);
                    }

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:652:7: SEMI
                    {
                    SEMI201=(Token)match(input,SEMI,FOLLOW_SEMI_in_forClause2127); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SEMI201_tree = (CommonTree)adaptor.create(SEMI201);
                    adaptor.addChild(root_0, SEMI201_tree);
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_postStmt_in_forClause2141);
            postStmt202=postStmt();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, postStmt202.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "forClause"

    public static class rangeClause_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "rangeClause"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:657:1: rangeClause : expression ( ',' expression )? ( '=' | ':=' ) 'range' expression ;
    public final GoSourceParser.rangeClause_return rangeClause() throws RecognitionException {
        GoSourceParser.rangeClause_return retval = new GoSourceParser.rangeClause_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal204=null;
        Token set206=null;
        Token string_literal207=null;
        GoSourceParser.expression_return expression203 = null;

        GoSourceParser.expression_return expression205 = null;

        GoSourceParser.expression_return expression208 = null;


        CommonTree char_literal204_tree=null;
        CommonTree set206_tree=null;
        CommonTree string_literal207_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:658:3: ( expression ( ',' expression )? ( '=' | ':=' ) 'range' expression )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:659:3: expression ( ',' expression )? ( '=' | ':=' ) 'range' expression
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_expression_in_rangeClause2156);
            expression203=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression203.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:659:14: ( ',' expression )?
            int alt59=2;
            int LA59_0 = input.LA(1);

            if ( (LA59_0==69) ) {
                alt59=1;
            }
            switch (alt59) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:659:15: ',' expression
                    {
                    char_literal204=(Token)match(input,69,FOLLOW_69_in_rangeClause2159); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal204_tree = (CommonTree)adaptor.create(char_literal204);
                    adaptor.addChild(root_0, char_literal204_tree);
                    }
                    pushFollow(FOLLOW_expression_in_rangeClause2161);
                    expression205=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression205.getTree());

                    }
                    break;

            }

            set206=(Token)input.LT(1);
            if ( input.LA(1)==66||input.LA(1)==77 ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set206));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            string_literal207=(Token)match(input,84,FOLLOW_84_in_rangeClause2189); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal207_tree = (CommonTree)adaptor.create(string_literal207);
            adaptor.addChild(root_0, string_literal207_tree);
            }
            pushFollow(FOLLOW_expression_in_rangeClause2191);
            expression208=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression208.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "rangeClause"

    public static class initStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "initStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:667:1: initStmt : simpleStmt ;
    public final GoSourceParser.initStmt_return initStmt() throws RecognitionException {
        GoSourceParser.initStmt_return retval = new GoSourceParser.initStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.simpleStmt_return simpleStmt209 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:668:3: ( simpleStmt )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:669:3: simpleStmt
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_simpleStmt_in_initStmt2206);
            simpleStmt209=simpleStmt();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, simpleStmt209.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "initStmt"

    public static class postStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "postStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:672:1: postStmt : simpleStmt ;
    public final GoSourceParser.postStmt_return postStmt() throws RecognitionException {
        GoSourceParser.postStmt_return retval = new GoSourceParser.postStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.simpleStmt_return simpleStmt210 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:673:3: ( simpleStmt )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:674:3: simpleStmt
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_simpleStmt_in_postStmt2221);
            simpleStmt210=simpleStmt();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, simpleStmt210.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "postStmt"

    public static class assignOp_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "assignOp"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:677:1: assignOp : ( '=' | '*=' | '/=' | '+=' | '-=' | '|=' | '^=' | '%=' | '<<=' | '>>=' | '&=' | '&^=' | '<-=' );
    public final GoSourceParser.assignOp_return assignOp() throws RecognitionException {
        GoSourceParser.assignOp_return retval = new GoSourceParser.assignOp_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set211=null;

        CommonTree set211_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:678:3: ( '=' | '*=' | '/=' | '+=' | '-=' | '|=' | '^=' | '%=' | '<<=' | '>>=' | '&=' | '&^=' | '<-=' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set211=(Token)input.LT(1);
            if ( input.LA(1)==66||(input.LA(1)>=85 && input.LA(1)<=96) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set211));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "assignOp"

    public static class shortVarDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "shortVarDecl"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:694:1: shortVarDecl : ilist= identifierList ':=' expressionList ;
    public final GoSourceParser.shortVarDecl_return shortVarDecl() throws RecognitionException {
        GoSourceParser.shortVarDecl_return retval = new GoSourceParser.shortVarDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal212=null;
        GoSourceParser.identifierList_return ilist = null;

        GoSourceParser.expressionList_return expressionList213 = null;


        CommonTree string_literal212_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:695:3: (ilist= identifierList ':=' expressionList )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:696:3: ilist= identifierList ':=' expressionList
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_identifierList_in_shortVarDecl2325);
            ilist=identifierList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, ilist.getTree());
            string_literal212=(Token)match(input,77,FOLLOW_77_in_shortVarDecl2327); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal212_tree = (CommonTree)adaptor.create(string_literal212);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal212_tree, root_0);
            }
            pushFollow(FOLLOW_expressionList_in_shortVarDecl2330);
            expressionList213=expressionList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expressionList213.getTree());
            if ( state.backtracking==0 ) {

                
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "shortVarDecl"

    public static class labeledStatement_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "labeledStatement"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:701:1: labeledStatement : label ':' statement ;
    public final GoSourceParser.labeledStatement_return labeledStatement() throws RecognitionException {
        GoSourceParser.labeledStatement_return retval = new GoSourceParser.labeledStatement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal215=null;
        GoSourceParser.label_return label214 = null;

        GoSourceParser.statement_return statement216 = null;


        CommonTree char_literal215_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:702:3: ( label ':' statement )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:703:3: label ':' statement
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_label_in_labeledStatement2349);
            label214=label();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, label214.getTree());
            char_literal215=(Token)match(input,74,FOLLOW_74_in_labeledStatement2351); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal215_tree = (CommonTree)adaptor.create(char_literal215);
            root_0 = (CommonTree)adaptor.becomeRoot(char_literal215_tree, root_0);
            }
            pushFollow(FOLLOW_statement_in_labeledStatement2354);
            statement216=statement();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, statement216.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "labeledStatement"

    public static class label_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "label"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:706:1: label : identifier ;
    public final GoSourceParser.label_return label() throws RecognitionException {
        GoSourceParser.label_return retval = new GoSourceParser.label_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.identifier_return identifier217 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:707:3: ( identifier )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:708:3: identifier
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_identifier_in_label2369);
            identifier217=identifier();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, identifier217.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "label"

    public static class expressionStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expressionStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:711:1: expressionStmt : expression ;
    public final GoSourceParser.expressionStmt_return expressionStmt() throws RecognitionException {
        GoSourceParser.expressionStmt_return retval = new GoSourceParser.expressionStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.expression_return expression218 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:712:3: ( expression )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:713:3: expression
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_expression_in_expressionStmt2384);
            expression218=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression218.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "expressionStmt"

    public static class incDecStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "incDecStmt"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:716:1: incDecStmt : expression ( '++' | '--' ) ;
    public final GoSourceParser.incDecStmt_return incDecStmt() throws RecognitionException {
        GoSourceParser.incDecStmt_return retval = new GoSourceParser.incDecStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal220=null;
        Token string_literal221=null;
        GoSourceParser.expression_return expression219 = null;


        CommonTree string_literal220_tree=null;
        CommonTree string_literal221_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:717:3: ( expression ( '++' | '--' ) )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:718:3: expression ( '++' | '--' )
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_expression_in_incDecStmt2399);
            expression219=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression219.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:719:3: ( '++' | '--' )
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==PLUSPLUS) ) {
                alt60=1;
            }
            else if ( (LA60_0==MINUSMINUS) ) {
                alt60=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 60, 0, input);

                throw nvae;
            }
            switch (alt60) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:720:5: '++'
                    {
                    string_literal220=(Token)match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_incDecStmt2409); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal220_tree = (CommonTree)adaptor.create(string_literal220);
                    root_0 = (CommonTree)adaptor.becomeRoot(string_literal220_tree, root_0);
                    }

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:721:7: '--'
                    {
                    string_literal221=(Token)match(input,MINUSMINUS,FOLLOW_MINUSMINUS_in_incDecStmt2418); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal221_tree = (CommonTree)adaptor.create(string_literal221);
                    root_0 = (CommonTree)adaptor.becomeRoot(string_literal221_tree, root_0);
                    }

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "incDecStmt"

    public static class constDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "constDecl"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:725:1: constDecl : 'const' ( constSpec | '(' ( constSpec SEMI )* ( SEMI )? ')' ) ;
    public final GoSourceParser.constDecl_return constDecl() throws RecognitionException {
        GoSourceParser.constDecl_return retval = new GoSourceParser.constDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal222=null;
        Token char_literal224=null;
        Token SEMI226=null;
        Token SEMI227=null;
        Token char_literal228=null;
        GoSourceParser.constSpec_return constSpec223 = null;

        GoSourceParser.constSpec_return constSpec225 = null;


        CommonTree string_literal222_tree=null;
        CommonTree char_literal224_tree=null;
        CommonTree SEMI226_tree=null;
        CommonTree SEMI227_tree=null;
        CommonTree char_literal228_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:726:3: ( 'const' ( constSpec | '(' ( constSpec SEMI )* ( SEMI )? ')' ) )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:727:3: 'const' ( constSpec | '(' ( constSpec SEMI )* ( SEMI )? ')' )
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal222=(Token)match(input,97,FOLLOW_97_in_constDecl2438); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal222_tree = (CommonTree)adaptor.create(string_literal222);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal222_tree, root_0);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:728:3: ( constSpec | '(' ( constSpec SEMI )* ( SEMI )? ')' )
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( ((LA63_0>=Identifier && LA63_0<=REAL)||LA63_0==98) ) {
                alt63=1;
            }
            else if ( (LA63_0==64) ) {
                alt63=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 63, 0, input);

                throw nvae;
            }
            switch (alt63) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:729:5: constSpec
                    {
                    pushFollow(FOLLOW_constSpec_in_constDecl2449);
                    constSpec223=constSpec();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, constSpec223.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:730:7: '(' ( constSpec SEMI )* ( SEMI )? ')'
                    {
                    char_literal224=(Token)match(input,64,FOLLOW_64_in_constDecl2457); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal224_tree = (CommonTree)adaptor.create(char_literal224);
                    adaptor.addChild(root_0, char_literal224_tree);
                    }
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:730:11: ( constSpec SEMI )*
                    loop61:
                    do {
                        int alt61=2;
                        int LA61_0 = input.LA(1);

                        if ( ((LA61_0>=Identifier && LA61_0<=REAL)||LA61_0==98) ) {
                            alt61=1;
                        }


                        switch (alt61) {
                    	case 1 :
                    	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:730:12: constSpec SEMI
                    	    {
                    	    pushFollow(FOLLOW_constSpec_in_constDecl2460);
                    	    constSpec225=constSpec();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, constSpec225.getTree());
                    	    SEMI226=(Token)match(input,SEMI,FOLLOW_SEMI_in_constDecl2462); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    SEMI226_tree = (CommonTree)adaptor.create(SEMI226);
                    	    adaptor.addChild(root_0, SEMI226_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop61;
                        }
                    } while (true);

                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:730:33: ( SEMI )?
                    int alt62=2;
                    int LA62_0 = input.LA(1);

                    if ( (LA62_0==SEMI) ) {
                        alt62=1;
                    }
                    switch (alt62) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:730:33: SEMI
                            {
                            SEMI227=(Token)match(input,SEMI,FOLLOW_SEMI_in_constDecl2466); if (state.failed) return retval;

                            }
                            break;

                    }

                    char_literal228=(Token)match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_constDecl2475); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal228_tree = (CommonTree)adaptor.create(char_literal228);
                    adaptor.addChild(root_0, char_literal228_tree);
                    }

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "constDecl"

    public static class constSpec_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "constSpec"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:735:1: constSpec : identifierList ( ( ( ( type '=' )=> ( type '=' ) ) | '=' ) expressionList )? ;
    public final GoSourceParser.constSpec_return constSpec() throws RecognitionException {
        GoSourceParser.constSpec_return retval = new GoSourceParser.constSpec_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal231=null;
        Token char_literal232=null;
        GoSourceParser.identifierList_return identifierList229 = null;

        GoSourceParser.type_return type230 = null;

        GoSourceParser.expressionList_return expressionList233 = null;


        CommonTree char_literal231_tree=null;
        CommonTree char_literal232_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:736:3: ( identifierList ( ( ( ( type '=' )=> ( type '=' ) ) | '=' ) expressionList )? )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:737:3: identifierList ( ( ( ( type '=' )=> ( type '=' ) ) | '=' ) expressionList )?
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_identifierList_in_constSpec2494);
            identifierList229=identifierList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, identifierList229.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:738:3: ( ( ( ( type '=' )=> ( type '=' ) ) | '=' ) expressionList )?
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( ((LA65_0>=Identifier && LA65_0<=REAL)||(LA65_0>=STRUCT && LA65_0<=INTERFACE)||LA65_0==64||(LA65_0>=66 && LA65_0<=68)||LA65_0==82||(LA65_0>=98 && LA65_0<=99)||(LA65_0>=119 && LA65_0<=120)) ) {
                alt65=1;
            }
            switch (alt65) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:739:5: ( ( ( type '=' )=> ( type '=' ) ) | '=' ) expressionList
                    {
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:739:5: ( ( ( type '=' )=> ( type '=' ) ) | '=' )
                    int alt64=2;
                    alt64 = dfa64.predict(input);
                    switch (alt64) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:740:7: ( ( type '=' )=> ( type '=' ) )
                            {
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:740:7: ( ( type '=' )=> ( type '=' ) )
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:741:9: ( type '=' )=> ( type '=' )
                            {
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:741:23: ( type '=' )
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:741:24: type '='
                            {
                            pushFollow(FOLLOW_type_in_constSpec2531);
                            type230=type();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, type230.getTree());
                            char_literal231=(Token)match(input,66,FOLLOW_66_in_constSpec2533); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            char_literal231_tree = (CommonTree)adaptor.create(char_literal231);
                            adaptor.addChild(root_0, char_literal231_tree);
                            }

                            }


                            }


                            }
                            break;
                        case 2 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:743:9: '='
                            {
                            char_literal232=(Token)match(input,66,FOLLOW_66_in_constSpec2552); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            char_literal232_tree = (CommonTree)adaptor.create(char_literal232);
                            adaptor.addChild(root_0, char_literal232_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_expressionList_in_constSpec2564);
                    expressionList233=expressionList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expressionList233.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "constSpec"

    public static class identifierList_return extends ParserRuleReturnScope {
        public List<Var> vars;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "identifierList"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:749:1: identifierList returns [List<Var> vars] : a= identifier ( ',' b= identifier )* ;
    public final GoSourceParser.identifierList_return identifierList() throws RecognitionException {
        GoSourceParser.identifierList_return retval = new GoSourceParser.identifierList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal234=null;
        GoSourceParser.identifier_return a = null;

        GoSourceParser.identifier_return b = null;


        CommonTree char_literal234_tree=null;


          retval.vars = new ArrayList<Var>();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:753:3: (a= identifier ( ',' b= identifier )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:754:3: a= identifier ( ',' b= identifier )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_identifier_in_identifierList2593);
            a=identifier();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, a.getTree());
            if ( state.backtracking==0 ) {

                  Var var = new Var();
                  var.insertionText = (a!=null?input.toString(a.start,a.stop):null);
                  retval.vars.add(var);
                
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:761:3: ( ',' b= identifier )*
            loop66:
            do {
                int alt66=2;
                int LA66_0 = input.LA(1);

                if ( (LA66_0==69) ) {
                    alt66=1;
                }


                switch (alt66) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:761:4: ',' b= identifier
            	    {
            	    char_literal234=(Token)match(input,69,FOLLOW_69_in_identifierList2606); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal234_tree = (CommonTree)adaptor.create(char_literal234);
            	    root_0 = (CommonTree)adaptor.becomeRoot(char_literal234_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_identifier_in_identifierList2611);
            	    b=identifier();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, b.getTree());
            	    if ( state.backtracking==0 ) {

            	          Var var = new Var();
            	          var.insertionText = (b!=null?input.toString(b.start,b.stop):null);
            	          retval.vars.add(var);
            	        
            	    }

            	    }
            	    break;

            	default :
            	    break loop66;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "identifierList"

    public static class expressionList_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expressionList"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:771:1: expressionList returns [List<Type> types] : expression ( ',' expression )* ;
    public final GoSourceParser.expressionList_return expressionList() throws RecognitionException {
        GoSourceParser.expressionList_return retval = new GoSourceParser.expressionList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal236=null;
        GoSourceParser.expression_return expression235 = null;

        GoSourceParser.expression_return expression237 = null;


        CommonTree char_literal236_tree=null;


          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:775:3: ( expression ( ',' expression )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:776:3: expression ( ',' expression )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_expression_in_expressionList2646);
            expression235=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression235.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:776:14: ( ',' expression )*
            loop67:
            do {
                int alt67=2;
                int LA67_0 = input.LA(1);

                if ( (LA67_0==69) ) {
                    int LA67_2 = input.LA(2);

                    if ( (LA67_2==String_Lit||(LA67_2>=Integer && LA67_2<=REAL)||(LA67_2>=STRUCT && LA67_2<=INTERFACE)||LA67_2==64||(LA67_2>=67 && LA67_2<=68)||LA67_2==82||(LA67_2>=98 && LA67_2<=103)||LA67_2==116||(LA67_2>=119 && LA67_2<=120)) ) {
                        alt67=1;
                    }


                }


                switch (alt67) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:776:15: ',' expression
            	    {
            	    char_literal236=(Token)match(input,69,FOLLOW_69_in_expressionList2649); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal236_tree = (CommonTree)adaptor.create(char_literal236);
            	    root_0 = (CommonTree)adaptor.becomeRoot(char_literal236_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_expression_in_expressionList2652);
            	    expression237=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression237.getTree());

            	    }
            	    break;

            	default :
            	    break loop67;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "expressionList"

    public static class expression_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expression"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:779:1: expression returns [List<Type> types] : logical_or ;
    public final GoSourceParser.expression_return expression() throws RecognitionException {
        GoSourceParser.expression_return retval = new GoSourceParser.expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.logical_or_return logical_or238 = null;




          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:783:3: ( logical_or )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:786:3: logical_or
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_logical_or_in_expression2683);
            logical_or238=logical_or();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, logical_or238.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "expression"

    public static class base_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "base"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:789:1: base returns [List<Type> types] : ( ( builtinCall )=> builtinCall | ( operand )=> operand | ( conversion )=> conversion );
    public final GoSourceParser.base_return base() throws RecognitionException {
        GoSourceParser.base_return retval = new GoSourceParser.base_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.builtinCall_return builtinCall239 = null;

        GoSourceParser.operand_return operand240 = null;

        GoSourceParser.conversion_return conversion241 = null;




          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:794:3: ( ( builtinCall )=> builtinCall | ( operand )=> operand | ( conversion )=> conversion )
            int alt68=3;
            alt68 = dfa68.predict(input);
            switch (alt68) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:795:3: ( builtinCall )=> builtinCall
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_builtinCall_in_base2712);
                    builtinCall239=builtinCall();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, builtinCall239.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:796:5: ( operand )=> operand
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_operand_in_base2724);
                    operand240=operand();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, operand240.getTree());

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:797:5: ( conversion )=> conversion
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_conversion_in_base2736);
                    conversion241=conversion();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, conversion241.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "base"

    public static class primaryExpr_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "primaryExpr"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:800:1: primaryExpr returns [List<Type> types] : base ( ( ( call )=> call | ( selector )=> selector | ( index )=> index | ( typeAssertion )=> typeAssertion | ( slice )=> slice ) )* ;
    public final GoSourceParser.primaryExpr_return primaryExpr() throws RecognitionException {
        GoSourceParser.primaryExpr_return retval = new GoSourceParser.primaryExpr_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.base_return base242 = null;

        GoSourceParser.call_return call243 = null;

        GoSourceParser.selector_return selector244 = null;

        GoSourceParser.index_return index245 = null;

        GoSourceParser.typeAssertion_return typeAssertion246 = null;

        GoSourceParser.slice_return slice247 = null;




          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:805:3: ( base ( ( ( call )=> call | ( selector )=> selector | ( index )=> index | ( typeAssertion )=> typeAssertion | ( slice )=> slice ) )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:806:3: base ( ( ( call )=> call | ( selector )=> selector | ( index )=> index | ( typeAssertion )=> typeAssertion | ( slice )=> slice ) )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_base_in_primaryExpr2759);
            base242=base();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, base242.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:807:3: ( ( ( call )=> call | ( selector )=> selector | ( index )=> index | ( typeAssertion )=> typeAssertion | ( slice )=> slice ) )*
            loop70:
            do {
                int alt70=2;
                int LA70_0 = input.LA(1);

                if ( (LA70_0==65) ) {
                    int LA70_1 = input.LA(2);

                    if ( (LA70_1==64) ) {
                        int LA70_4 = input.LA(3);

                        if ( ((LA70_4>=Identifier && LA70_4<=REAL)||(LA70_4>=STRUCT && LA70_4<=INTERFACE)||LA70_4==64||(LA70_4>=67 && LA70_4<=68)||LA70_4==82||(LA70_4>=98 && LA70_4<=99)||(LA70_4>=119 && LA70_4<=120)) ) {
                            alt70=1;
                        }


                    }
                    else if ( ((LA70_1>=Identifier && LA70_1<=REAL)||LA70_1==98) ) {
                        alt70=1;
                    }


                }
                else if ( (LA70_0==64||LA70_0==99) ) {
                    alt70=1;
                }


                switch (alt70) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:808:5: ( ( call )=> call | ( selector )=> selector | ( index )=> index | ( typeAssertion )=> typeAssertion | ( slice )=> slice )
            	    {
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:808:5: ( ( call )=> call | ( selector )=> selector | ( index )=> index | ( typeAssertion )=> typeAssertion | ( slice )=> slice )
            	    int alt69=5;
            	    int LA69_0 = input.LA(1);

            	    if ( (LA69_0==64) && (synpred30_GoSource())) {
            	        alt69=1;
            	    }
            	    else if ( (LA69_0==65) ) {
            	        int LA69_2 = input.LA(2);

            	        if ( (synpred31_GoSource()) ) {
            	            alt69=2;
            	        }
            	        else if ( (synpred33_GoSource()) ) {
            	            alt69=4;
            	        }
            	        else {
            	            if (state.backtracking>0) {state.failed=true; return retval;}
            	            NoViableAltException nvae =
            	                new NoViableAltException("", 69, 2, input);

            	            throw nvae;
            	        }
            	    }
            	    else if ( (LA69_0==99) ) {
            	        int LA69_3 = input.LA(2);

            	        if ( (synpred32_GoSource()) ) {
            	            alt69=3;
            	        }
            	        else if ( (synpred34_GoSource()) ) {
            	            alt69=5;
            	        }
            	        else {
            	            if (state.backtracking>0) {state.failed=true; return retval;}
            	            NoViableAltException nvae =
            	                new NoViableAltException("", 69, 3, input);

            	            throw nvae;
            	        }
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 69, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt69) {
            	        case 1 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:809:7: ( call )=> call
            	            {
            	            pushFollow(FOLLOW_call_in_primaryExpr2783);
            	            call243=call();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, call243.getTree());

            	            }
            	            break;
            	        case 2 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:810:9: ( selector )=> selector
            	            {
            	            pushFollow(FOLLOW_selector_in_primaryExpr2799);
            	            selector244=selector();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, selector244.getTree());

            	            }
            	            break;
            	        case 3 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:811:9: ( index )=> index
            	            {
            	            pushFollow(FOLLOW_index_in_primaryExpr2815);
            	            index245=index();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, index245.getTree());

            	            }
            	            break;
            	        case 4 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:812:9: ( typeAssertion )=> typeAssertion
            	            {
            	            pushFollow(FOLLOW_typeAssertion_in_primaryExpr2831);
            	            typeAssertion246=typeAssertion();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, typeAssertion246.getTree());

            	            }
            	            break;
            	        case 5 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:813:9: ( slice )=> slice
            	            {
            	            pushFollow(FOLLOW_slice_in_primaryExpr2847);
            	            slice247=slice();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, slice247.getTree());

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop70;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "primaryExpr"

    public static class builtinName_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "builtinName"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:818:1: builtinName returns [List<Type> types] : ( 'append' | 'cap' | 'close' | 'closed' | 'cmplx' | 'copy' | 'imag' | 'len' | 'make' | 'panic' | 'print' | 'println' | 'real' | 'recover' );
    public final GoSourceParser.builtinName_return builtinName() throws RecognitionException {
        GoSourceParser.builtinName_return retval = new GoSourceParser.builtinName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set248=null;

        CommonTree set248_tree=null;


          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:823:3: ( 'append' | 'cap' | 'close' | 'closed' | 'cmplx' | 'copy' | 'imag' | 'len' | 'make' | 'panic' | 'print' | 'println' | 'real' | 'recover' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set248=(Token)input.LT(1);
            if ( (input.LA(1)>=APPEND && input.LA(1)<=REAL)||input.LA(1)==98 ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set248));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "builtinName"

    public static class builtinCall_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "builtinCall"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:840:1: builtinCall returns [List<Type> types] : ( builtinName '(' )=> builtinName '(' ( builtinArgs ( ',' )? )? ( SEMI )? ')' ;
    public final GoSourceParser.builtinCall_return builtinCall() throws RecognitionException {
        GoSourceParser.builtinCall_return retval = new GoSourceParser.builtinCall_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal250=null;
        Token char_literal252=null;
        Token SEMI253=null;
        Token char_literal254=null;
        GoSourceParser.builtinName_return builtinName249 = null;

        GoSourceParser.builtinArgs_return builtinArgs251 = null;


        CommonTree char_literal250_tree=null;
        CommonTree char_literal252_tree=null;
        CommonTree SEMI253_tree=null;
        CommonTree char_literal254_tree=null;


          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:845:3: ( ( builtinName '(' )=> builtinName '(' ( builtinArgs ( ',' )? )? ( SEMI )? ')' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:846:3: ( builtinName '(' )=> builtinName '(' ( builtinArgs ( ',' )? )? ( SEMI )? ')'
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_builtinName_in_builtinCall2990);
            builtinName249=builtinName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, builtinName249.getTree());
            char_literal250=(Token)match(input,64,FOLLOW_64_in_builtinCall2992); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal250_tree = (CommonTree)adaptor.create(char_literal250);
            adaptor.addChild(root_0, char_literal250_tree);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:846:40: ( builtinArgs ( ',' )? )?
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==String_Lit||(LA72_0>=Integer && LA72_0<=REAL)||(LA72_0>=STRUCT && LA72_0<=INTERFACE)||LA72_0==64||(LA72_0>=67 && LA72_0<=68)||LA72_0==82||(LA72_0>=98 && LA72_0<=103)||LA72_0==116||(LA72_0>=119 && LA72_0<=120)) ) {
                alt72=1;
            }
            switch (alt72) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:846:41: builtinArgs ( ',' )?
                    {
                    pushFollow(FOLLOW_builtinArgs_in_builtinCall2995);
                    builtinArgs251=builtinArgs();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, builtinArgs251.getTree());
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:846:53: ( ',' )?
                    int alt71=2;
                    int LA71_0 = input.LA(1);

                    if ( (LA71_0==69) ) {
                        alt71=1;
                    }
                    switch (alt71) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:846:54: ','
                            {
                            char_literal252=(Token)match(input,69,FOLLOW_69_in_builtinCall2998); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            char_literal252_tree = (CommonTree)adaptor.create(char_literal252);
                            adaptor.addChild(root_0, char_literal252_tree);
                            }

                            }
                            break;

                    }


                    }
                    break;

            }

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:846:66: ( SEMI )?
            int alt73=2;
            int LA73_0 = input.LA(1);

            if ( (LA73_0==SEMI) ) {
                alt73=1;
            }
            switch (alt73) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:846:66: SEMI
                    {
                    SEMI253=(Token)match(input,SEMI,FOLLOW_SEMI_in_builtinCall3004); if (state.failed) return retval;

                    }
                    break;

            }

            char_literal254=(Token)match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_builtinCall3011); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal254_tree = (CommonTree)adaptor.create(char_literal254);
            adaptor.addChild(root_0, char_literal254_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "builtinCall"

    public static class builtinArgs_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "builtinArgs"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:850:1: builtinArgs : ( ( type ',' expressionList )=> type ',' expressionList | ( expressionList )=> expressionList | type ) ;
    public final GoSourceParser.builtinArgs_return builtinArgs() throws RecognitionException {
        GoSourceParser.builtinArgs_return retval = new GoSourceParser.builtinArgs_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal256=null;
        GoSourceParser.type_return type255 = null;

        GoSourceParser.expressionList_return expressionList257 = null;

        GoSourceParser.expressionList_return expressionList258 = null;

        GoSourceParser.type_return type259 = null;


        CommonTree char_literal256_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:851:3: ( ( ( type ',' expressionList )=> type ',' expressionList | ( expressionList )=> expressionList | type ) )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:852:3: ( ( type ',' expressionList )=> type ',' expressionList | ( expressionList )=> expressionList | type )
            {
            root_0 = (CommonTree)adaptor.nil();

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:852:3: ( ( type ',' expressionList )=> type ',' expressionList | ( expressionList )=> expressionList | type )
            int alt74=3;
            alt74 = dfa74.predict(input);
            switch (alt74) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:853:5: ( type ',' expressionList )=> type ',' expressionList
                    {
                    pushFollow(FOLLOW_type_in_builtinArgs3042);
                    type255=type();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, type255.getTree());
                    char_literal256=(Token)match(input,69,FOLLOW_69_in_builtinArgs3044); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal256_tree = (CommonTree)adaptor.create(char_literal256);
                    adaptor.addChild(root_0, char_literal256_tree);
                    }
                    pushFollow(FOLLOW_expressionList_in_builtinArgs3046);
                    expressionList257=expressionList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expressionList257.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:854:7: ( expressionList )=> expressionList
                    {
                    pushFollow(FOLLOW_expressionList_in_builtinArgs3060);
                    expressionList258=expressionList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expressionList258.getTree());

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:855:7: type
                    {
                    pushFollow(FOLLOW_type_in_builtinArgs3068);
                    type259=type();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, type259.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "builtinArgs"

    public static class selector_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "selector"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:859:1: selector : '.' identifier ;
    public final GoSourceParser.selector_return selector() throws RecognitionException {
        GoSourceParser.selector_return retval = new GoSourceParser.selector_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal260=null;
        GoSourceParser.identifier_return identifier261 = null;


        CommonTree char_literal260_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:860:3: ( '.' identifier )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:861:3: '.' identifier
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal260=(Token)match(input,65,FOLLOW_65_in_selector3087); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal260_tree = (CommonTree)adaptor.create(char_literal260);
            adaptor.addChild(root_0, char_literal260_tree);
            }
            pushFollow(FOLLOW_identifier_in_selector3089);
            identifier261=identifier();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, identifier261.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "selector"

    public static class typeAssertion_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typeAssertion"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:864:1: typeAssertion : '.' '(' type ( SEMI )? ')' ;
    public final GoSourceParser.typeAssertion_return typeAssertion() throws RecognitionException {
        GoSourceParser.typeAssertion_return retval = new GoSourceParser.typeAssertion_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal262=null;
        Token char_literal263=null;
        Token SEMI265=null;
        Token char_literal266=null;
        GoSourceParser.type_return type264 = null;


        CommonTree char_literal262_tree=null;
        CommonTree char_literal263_tree=null;
        CommonTree SEMI265_tree=null;
        CommonTree char_literal266_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:865:3: ( '.' '(' type ( SEMI )? ')' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:866:3: '.' '(' type ( SEMI )? ')'
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal262=(Token)match(input,65,FOLLOW_65_in_typeAssertion3104); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal262_tree = (CommonTree)adaptor.create(char_literal262);
            adaptor.addChild(root_0, char_literal262_tree);
            }
            char_literal263=(Token)match(input,64,FOLLOW_64_in_typeAssertion3106); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal263_tree = (CommonTree)adaptor.create(char_literal263);
            adaptor.addChild(root_0, char_literal263_tree);
            }
            pushFollow(FOLLOW_type_in_typeAssertion3108);
            type264=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type264.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:866:20: ( SEMI )?
            int alt75=2;
            int LA75_0 = input.LA(1);

            if ( (LA75_0==SEMI) ) {
                alt75=1;
            }
            switch (alt75) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:866:20: SEMI
                    {
                    SEMI265=(Token)match(input,SEMI,FOLLOW_SEMI_in_typeAssertion3110); if (state.failed) return retval;

                    }
                    break;

            }

            char_literal266=(Token)match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_typeAssertion3117); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal266_tree = (CommonTree)adaptor.create(char_literal266);
            adaptor.addChild(root_0, char_literal266_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "typeAssertion"

    public static class index_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "index"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:870:1: index : '[' expression ']' ;
    public final GoSourceParser.index_return index() throws RecognitionException {
        GoSourceParser.index_return retval = new GoSourceParser.index_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal267=null;
        Token char_literal269=null;
        GoSourceParser.expression_return expression268 = null;


        CommonTree char_literal267_tree=null;
        CommonTree char_literal269_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:871:3: ( '[' expression ']' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:872:3: '[' expression ']'
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal267=(Token)match(input,99,FOLLOW_99_in_index3132); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal267_tree = (CommonTree)adaptor.create(char_literal267);
            adaptor.addChild(root_0, char_literal267_tree);
            }
            pushFollow(FOLLOW_expression_in_index3134);
            expression268=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression268.getTree());
            char_literal269=(Token)match(input,CLOSE_SQUARE,FOLLOW_CLOSE_SQUARE_in_index3136); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal269_tree = (CommonTree)adaptor.create(char_literal269);
            adaptor.addChild(root_0, char_literal269_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "index"

    public static class slice_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "slice"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:875:1: slice : '[' ( expression )? ':' ( expression )? ']' ;
    public final GoSourceParser.slice_return slice() throws RecognitionException {
        GoSourceParser.slice_return retval = new GoSourceParser.slice_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal270=null;
        Token char_literal272=null;
        Token char_literal274=null;
        GoSourceParser.expression_return expression271 = null;

        GoSourceParser.expression_return expression273 = null;


        CommonTree char_literal270_tree=null;
        CommonTree char_literal272_tree=null;
        CommonTree char_literal274_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:876:3: ( '[' ( expression )? ':' ( expression )? ']' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:877:3: '[' ( expression )? ':' ( expression )? ']'
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal270=(Token)match(input,99,FOLLOW_99_in_slice3151); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal270_tree = (CommonTree)adaptor.create(char_literal270);
            adaptor.addChild(root_0, char_literal270_tree);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:877:7: ( expression )?
            int alt76=2;
            int LA76_0 = input.LA(1);

            if ( (LA76_0==String_Lit||(LA76_0>=Integer && LA76_0<=REAL)||(LA76_0>=STRUCT && LA76_0<=INTERFACE)||LA76_0==64||(LA76_0>=67 && LA76_0<=68)||LA76_0==82||(LA76_0>=98 && LA76_0<=103)||LA76_0==116||(LA76_0>=119 && LA76_0<=120)) ) {
                alt76=1;
            }
            switch (alt76) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:877:8: expression
                    {
                    pushFollow(FOLLOW_expression_in_slice3154);
                    expression271=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression271.getTree());

                    }
                    break;

            }

            char_literal272=(Token)match(input,74,FOLLOW_74_in_slice3158); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal272_tree = (CommonTree)adaptor.create(char_literal272);
            adaptor.addChild(root_0, char_literal272_tree);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:877:25: ( expression )?
            int alt77=2;
            int LA77_0 = input.LA(1);

            if ( (LA77_0==String_Lit||(LA77_0>=Integer && LA77_0<=REAL)||(LA77_0>=STRUCT && LA77_0<=INTERFACE)||LA77_0==64||(LA77_0>=67 && LA77_0<=68)||LA77_0==82||(LA77_0>=98 && LA77_0<=103)||LA77_0==116||(LA77_0>=119 && LA77_0<=120)) ) {
                alt77=1;
            }
            switch (alt77) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:877:26: expression
                    {
                    pushFollow(FOLLOW_expression_in_slice3161);
                    expression273=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression273.getTree());

                    }
                    break;

            }

            char_literal274=(Token)match(input,CLOSE_SQUARE,FOLLOW_CLOSE_SQUARE_in_slice3165); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal274_tree = (CommonTree)adaptor.create(char_literal274);
            adaptor.addChild(root_0, char_literal274_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "slice"

    public static class call_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "call"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:880:1: call : '(' ( argumentList ( ',' )? )? ( SEMI )? ')' ;
    public final GoSourceParser.call_return call() throws RecognitionException {
        GoSourceParser.call_return retval = new GoSourceParser.call_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal275=null;
        Token char_literal277=null;
        Token SEMI278=null;
        Token char_literal279=null;
        GoSourceParser.argumentList_return argumentList276 = null;


        CommonTree char_literal275_tree=null;
        CommonTree char_literal277_tree=null;
        CommonTree SEMI278_tree=null;
        CommonTree char_literal279_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:881:3: ( '(' ( argumentList ( ',' )? )? ( SEMI )? ')' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:882:3: '(' ( argumentList ( ',' )? )? ( SEMI )? ')'
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal275=(Token)match(input,64,FOLLOW_64_in_call3180); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal275_tree = (CommonTree)adaptor.create(char_literal275);
            root_0 = (CommonTree)adaptor.becomeRoot(char_literal275_tree, root_0);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:882:8: ( argumentList ( ',' )? )?
            int alt79=2;
            int LA79_0 = input.LA(1);

            if ( (LA79_0==String_Lit||(LA79_0>=Integer && LA79_0<=REAL)||(LA79_0>=STRUCT && LA79_0<=INTERFACE)||LA79_0==64||(LA79_0>=67 && LA79_0<=68)||LA79_0==82||(LA79_0>=98 && LA79_0<=103)||LA79_0==116||(LA79_0>=119 && LA79_0<=120)) ) {
                alt79=1;
            }
            switch (alt79) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:882:9: argumentList ( ',' )?
                    {
                    pushFollow(FOLLOW_argumentList_in_call3184);
                    argumentList276=argumentList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, argumentList276.getTree());
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:882:22: ( ',' )?
                    int alt78=2;
                    int LA78_0 = input.LA(1);

                    if ( (LA78_0==69) ) {
                        alt78=1;
                    }
                    switch (alt78) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:882:22: ','
                            {
                            char_literal277=(Token)match(input,69,FOLLOW_69_in_call3186); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            char_literal277_tree = (CommonTree)adaptor.create(char_literal277);
                            adaptor.addChild(root_0, char_literal277_tree);
                            }

                            }
                            break;

                    }


                    }
                    break;

            }

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:882:33: ( SEMI )?
            int alt80=2;
            int LA80_0 = input.LA(1);

            if ( (LA80_0==SEMI) ) {
                alt80=1;
            }
            switch (alt80) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:882:33: SEMI
                    {
                    SEMI278=(Token)match(input,SEMI,FOLLOW_SEMI_in_call3191); if (state.failed) return retval;

                    }
                    break;

            }

            char_literal279=(Token)match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_call3198); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal279_tree = (CommonTree)adaptor.create(char_literal279);
            adaptor.addChild(root_0, char_literal279_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "call"

    public static class argumentList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "argumentList"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:886:1: argumentList : expressionList ( '...' )? ;
    public final GoSourceParser.argumentList_return argumentList() throws RecognitionException {
        GoSourceParser.argumentList_return retval = new GoSourceParser.argumentList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal281=null;
        GoSourceParser.expressionList_return expressionList280 = null;


        CommonTree string_literal281_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:887:3: ( expressionList ( '...' )? )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:888:3: expressionList ( '...' )?
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_expressionList_in_argumentList3213);
            expressionList280=expressionList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expressionList280.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:888:18: ( '...' )?
            int alt81=2;
            int LA81_0 = input.LA(1);

            if ( (LA81_0==70) ) {
                alt81=1;
            }
            switch (alt81) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:888:18: '...'
                    {
                    string_literal281=(Token)match(input,70,FOLLOW_70_in_argumentList3215); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal281_tree = (CommonTree)adaptor.create(string_literal281);
                    adaptor.addChild(root_0, string_literal281_tree);
                    }

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "argumentList"

    public static class conversion_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "conversion"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:891:1: conversion : type '(' expression ( SEMI )? ')' ;
    public final GoSourceParser.conversion_return conversion() throws RecognitionException {
        GoSourceParser.conversion_return retval = new GoSourceParser.conversion_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal283=null;
        Token SEMI285=null;
        Token char_literal286=null;
        GoSourceParser.type_return type282 = null;

        GoSourceParser.expression_return expression284 = null;


        CommonTree char_literal283_tree=null;
        CommonTree SEMI285_tree=null;
        CommonTree char_literal286_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:892:3: ( type '(' expression ( SEMI )? ')' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:893:3: type '(' expression ( SEMI )? ')'
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_type_in_conversion3231);
            type282=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type282.getTree());
            char_literal283=(Token)match(input,64,FOLLOW_64_in_conversion3233); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal283_tree = (CommonTree)adaptor.create(char_literal283);
            adaptor.addChild(root_0, char_literal283_tree);
            }
            pushFollow(FOLLOW_expression_in_conversion3235);
            expression284=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression284.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:893:27: ( SEMI )?
            int alt82=2;
            int LA82_0 = input.LA(1);

            if ( (LA82_0==SEMI) ) {
                alt82=1;
            }
            switch (alt82) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:893:27: SEMI
                    {
                    SEMI285=(Token)match(input,SEMI,FOLLOW_SEMI_in_conversion3237); if (state.failed) return retval;

                    }
                    break;

            }

            char_literal286=(Token)match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_conversion3244); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal286_tree = (CommonTree)adaptor.create(char_literal286);
            adaptor.addChild(root_0, char_literal286_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "conversion"

    public static class unary_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unary"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:897:1: unary returns [List<Type> types] : ( ( '<-' primaryExpr )=> '<-' primaryExpr | ( '*' primaryExpr )=> '*' primaryExpr | ( '+' | '-' | '^' | '&' )* primaryExpr );
    public final GoSourceParser.unary_return unary() throws RecognitionException {
        GoSourceParser.unary_return retval = new GoSourceParser.unary_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal287=null;
        Token char_literal289=null;
        Token char_literal291=null;
        Token char_literal292=null;
        Token char_literal293=null;
        Token char_literal294=null;
        GoSourceParser.primaryExpr_return primaryExpr288 = null;

        GoSourceParser.primaryExpr_return primaryExpr290 = null;

        GoSourceParser.primaryExpr_return primaryExpr295 = null;


        CommonTree string_literal287_tree=null;
        CommonTree char_literal289_tree=null;
        CommonTree char_literal291_tree=null;
        CommonTree char_literal292_tree=null;
        CommonTree char_literal293_tree=null;
        CommonTree char_literal294_tree=null;


          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:902:3: ( ( '<-' primaryExpr )=> '<-' primaryExpr | ( '*' primaryExpr )=> '*' primaryExpr | ( '+' | '-' | '^' | '&' )* primaryExpr )
            int alt84=3;
            alt84 = dfa84.predict(input);
            switch (alt84) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:903:3: ( '<-' primaryExpr )=> '<-' primaryExpr
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal287=(Token)match(input,82,FOLLOW_82_in_unary3275); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal287_tree = (CommonTree)adaptor.create(string_literal287);
                    root_0 = (CommonTree)adaptor.becomeRoot(string_literal287_tree, root_0);
                    }
                    pushFollow(FOLLOW_primaryExpr_in_unary3278);
                    primaryExpr288=primaryExpr();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, primaryExpr288.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:904:5: ( '*' primaryExpr )=> '*' primaryExpr
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    char_literal289=(Token)match(input,68,FOLLOW_68_in_unary3292); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal289_tree = (CommonTree)adaptor.create(char_literal289);
                    root_0 = (CommonTree)adaptor.becomeRoot(char_literal289_tree, root_0);
                    }
                    pushFollow(FOLLOW_primaryExpr_in_unary3295);
                    primaryExpr290=primaryExpr();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, primaryExpr290.getTree());

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:906:3: ( '+' | '-' | '^' | '&' )* primaryExpr
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:906:3: ( '+' | '-' | '^' | '&' )*
                    loop83:
                    do {
                        int alt83=5;
                        switch ( input.LA(1) ) {
                        case 100:
                            {
                            alt83=1;
                            }
                            break;
                        case 101:
                            {
                            alt83=2;
                            }
                            break;
                        case 102:
                            {
                            alt83=3;
                            }
                            break;
                        case 103:
                            {
                            alt83=4;
                            }
                            break;

                        }

                        switch (alt83) {
                    	case 1 :
                    	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:907:5: '+'
                    	    {
                    	    char_literal291=(Token)match(input,100,FOLLOW_100_in_unary3309); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal291_tree = (CommonTree)adaptor.create(char_literal291);
                    	    root_0 = (CommonTree)adaptor.becomeRoot(char_literal291_tree, root_0);
                    	    }

                    	    }
                    	    break;
                    	case 2 :
                    	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:908:7: '-'
                    	    {
                    	    char_literal292=(Token)match(input,101,FOLLOW_101_in_unary3318); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal292_tree = (CommonTree)adaptor.create(char_literal292);
                    	    root_0 = (CommonTree)adaptor.becomeRoot(char_literal292_tree, root_0);
                    	    }

                    	    }
                    	    break;
                    	case 3 :
                    	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:909:7: '^'
                    	    {
                    	    char_literal293=(Token)match(input,102,FOLLOW_102_in_unary3327); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal293_tree = (CommonTree)adaptor.create(char_literal293);
                    	    root_0 = (CommonTree)adaptor.becomeRoot(char_literal293_tree, root_0);
                    	    }

                    	    }
                    	    break;
                    	case 4 :
                    	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:910:7: '&'
                    	    {
                    	    char_literal294=(Token)match(input,103,FOLLOW_103_in_unary3336); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal294_tree = (CommonTree)adaptor.create(char_literal294);
                    	    root_0 = (CommonTree)adaptor.becomeRoot(char_literal294_tree, root_0);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop83;
                        }
                    } while (true);

                    pushFollow(FOLLOW_primaryExpr_in_unary3346);
                    primaryExpr295=primaryExpr();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, primaryExpr295.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "unary"

    public static class mult_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mult"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:915:1: mult returns [List<Type> types] : unary ( ( '*' | '/' | '%' | '<<' | '>>' | '&' | '&^' ) unary )* ;
    public final GoSourceParser.mult_return mult() throws RecognitionException {
        GoSourceParser.mult_return retval = new GoSourceParser.mult_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal297=null;
        Token char_literal298=null;
        Token char_literal299=null;
        Token string_literal300=null;
        Token string_literal301=null;
        Token char_literal302=null;
        Token string_literal303=null;
        GoSourceParser.unary_return unary296 = null;

        GoSourceParser.unary_return unary304 = null;


        CommonTree char_literal297_tree=null;
        CommonTree char_literal298_tree=null;
        CommonTree char_literal299_tree=null;
        CommonTree string_literal300_tree=null;
        CommonTree string_literal301_tree=null;
        CommonTree char_literal302_tree=null;
        CommonTree string_literal303_tree=null;


          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:920:3: ( unary ( ( '*' | '/' | '%' | '<<' | '>>' | '&' | '&^' ) unary )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:921:3: unary ( ( '*' | '/' | '%' | '<<' | '>>' | '&' | '&^' ) unary )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_unary_in_mult3369);
            unary296=unary();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, unary296.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:922:3: ( ( '*' | '/' | '%' | '<<' | '>>' | '&' | '&^' ) unary )*
            loop86:
            do {
                int alt86=2;
                int LA86_0 = input.LA(1);

                if ( (LA86_0==68||(LA86_0>=103 && LA86_0<=108)) ) {
                    alt86=1;
                }


                switch (alt86) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:923:5: ( '*' | '/' | '%' | '<<' | '>>' | '&' | '&^' ) unary
            	    {
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:923:5: ( '*' | '/' | '%' | '<<' | '>>' | '&' | '&^' )
            	    int alt85=7;
            	    switch ( input.LA(1) ) {
            	    case 68:
            	        {
            	        alt85=1;
            	        }
            	        break;
            	    case 104:
            	        {
            	        alt85=2;
            	        }
            	        break;
            	    case 105:
            	        {
            	        alt85=3;
            	        }
            	        break;
            	    case 106:
            	        {
            	        alt85=4;
            	        }
            	        break;
            	    case 107:
            	        {
            	        alt85=5;
            	        }
            	        break;
            	    case 103:
            	        {
            	        alt85=6;
            	        }
            	        break;
            	    case 108:
            	        {
            	        alt85=7;
            	        }
            	        break;
            	    default:
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 85, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt85) {
            	        case 1 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:924:7: '*'
            	            {
            	            char_literal297=(Token)match(input,68,FOLLOW_68_in_mult3387); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            char_literal297_tree = (CommonTree)adaptor.create(char_literal297);
            	            root_0 = (CommonTree)adaptor.becomeRoot(char_literal297_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:925:9: '/'
            	            {
            	            char_literal298=(Token)match(input,104,FOLLOW_104_in_mult3398); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            char_literal298_tree = (CommonTree)adaptor.create(char_literal298);
            	            root_0 = (CommonTree)adaptor.becomeRoot(char_literal298_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 3 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:926:9: '%'
            	            {
            	            char_literal299=(Token)match(input,105,FOLLOW_105_in_mult3409); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            char_literal299_tree = (CommonTree)adaptor.create(char_literal299);
            	            root_0 = (CommonTree)adaptor.becomeRoot(char_literal299_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 4 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:927:9: '<<'
            	            {
            	            string_literal300=(Token)match(input,106,FOLLOW_106_in_mult3420); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            string_literal300_tree = (CommonTree)adaptor.create(string_literal300);
            	            root_0 = (CommonTree)adaptor.becomeRoot(string_literal300_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 5 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:928:9: '>>'
            	            {
            	            string_literal301=(Token)match(input,107,FOLLOW_107_in_mult3431); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            string_literal301_tree = (CommonTree)adaptor.create(string_literal301);
            	            root_0 = (CommonTree)adaptor.becomeRoot(string_literal301_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 6 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:929:9: '&'
            	            {
            	            char_literal302=(Token)match(input,103,FOLLOW_103_in_mult3442); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            char_literal302_tree = (CommonTree)adaptor.create(char_literal302);
            	            root_0 = (CommonTree)adaptor.becomeRoot(char_literal302_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 7 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:930:9: '&^'
            	            {
            	            string_literal303=(Token)match(input,108,FOLLOW_108_in_mult3453); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            string_literal303_tree = (CommonTree)adaptor.create(string_literal303);
            	            root_0 = (CommonTree)adaptor.becomeRoot(string_literal303_tree, root_0);
            	            }

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_unary_in_mult3466);
            	    unary304=unary();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, unary304.getTree());

            	    }
            	    break;

            	default :
            	    break loop86;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "mult"

    public static class add_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "add"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:936:1: add returns [List<Type> types] : mult ( ( '+' | '-' | '|' | '^' ) mult )* ;
    public final GoSourceParser.add_return add() throws RecognitionException {
        GoSourceParser.add_return retval = new GoSourceParser.add_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal306=null;
        Token char_literal307=null;
        Token char_literal308=null;
        Token char_literal309=null;
        GoSourceParser.mult_return mult305 = null;

        GoSourceParser.mult_return mult310 = null;


        CommonTree char_literal306_tree=null;
        CommonTree char_literal307_tree=null;
        CommonTree char_literal308_tree=null;
        CommonTree char_literal309_tree=null;


          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:941:3: ( mult ( ( '+' | '-' | '|' | '^' ) mult )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:942:3: mult ( ( '+' | '-' | '|' | '^' ) mult )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_mult_in_add3494);
            mult305=mult();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, mult305.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:943:3: ( ( '+' | '-' | '|' | '^' ) mult )*
            loop88:
            do {
                int alt88=2;
                int LA88_0 = input.LA(1);

                if ( ((LA88_0>=100 && LA88_0<=102)||LA88_0==109) ) {
                    alt88=1;
                }


                switch (alt88) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:944:5: ( '+' | '-' | '|' | '^' ) mult
            	    {
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:944:5: ( '+' | '-' | '|' | '^' )
            	    int alt87=4;
            	    switch ( input.LA(1) ) {
            	    case 100:
            	        {
            	        alt87=1;
            	        }
            	        break;
            	    case 101:
            	        {
            	        alt87=2;
            	        }
            	        break;
            	    case 109:
            	        {
            	        alt87=3;
            	        }
            	        break;
            	    case 102:
            	        {
            	        alt87=4;
            	        }
            	        break;
            	    default:
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 87, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt87) {
            	        case 1 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:945:7: '+'
            	            {
            	            char_literal306=(Token)match(input,100,FOLLOW_100_in_add3512); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            char_literal306_tree = (CommonTree)adaptor.create(char_literal306);
            	            root_0 = (CommonTree)adaptor.becomeRoot(char_literal306_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:946:9: '-'
            	            {
            	            char_literal307=(Token)match(input,101,FOLLOW_101_in_add3523); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            char_literal307_tree = (CommonTree)adaptor.create(char_literal307);
            	            root_0 = (CommonTree)adaptor.becomeRoot(char_literal307_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 3 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:947:9: '|'
            	            {
            	            char_literal308=(Token)match(input,109,FOLLOW_109_in_add3534); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            char_literal308_tree = (CommonTree)adaptor.create(char_literal308);
            	            root_0 = (CommonTree)adaptor.becomeRoot(char_literal308_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 4 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:948:9: '^'
            	            {
            	            char_literal309=(Token)match(input,102,FOLLOW_102_in_add3545); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            char_literal309_tree = (CommonTree)adaptor.create(char_literal309);
            	            root_0 = (CommonTree)adaptor.becomeRoot(char_literal309_tree, root_0);
            	            }

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_mult_in_add3558);
            	    mult310=mult();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, mult310.getTree());

            	    }
            	    break;

            	default :
            	    break loop88;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "add"

    public static class relational_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "relational"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:954:1: relational returns [List<Type> types] : add ( ( '==' | '!=' | '<' | '<=' | '>' | '>=' ) add )* ;
    public final GoSourceParser.relational_return relational() throws RecognitionException {
        GoSourceParser.relational_return retval = new GoSourceParser.relational_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal312=null;
        Token string_literal313=null;
        Token char_literal314=null;
        Token string_literal315=null;
        Token char_literal316=null;
        Token string_literal317=null;
        GoSourceParser.add_return add311 = null;

        GoSourceParser.add_return add318 = null;


        CommonTree string_literal312_tree=null;
        CommonTree string_literal313_tree=null;
        CommonTree char_literal314_tree=null;
        CommonTree string_literal315_tree=null;
        CommonTree char_literal316_tree=null;
        CommonTree string_literal317_tree=null;


          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:959:3: ( add ( ( '==' | '!=' | '<' | '<=' | '>' | '>=' ) add )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:960:3: add ( ( '==' | '!=' | '<' | '<=' | '>' | '>=' ) add )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_add_in_relational3586);
            add311=add();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, add311.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:961:3: ( ( '==' | '!=' | '<' | '<=' | '>' | '>=' ) add )*
            loop90:
            do {
                int alt90=2;
                int LA90_0 = input.LA(1);

                if ( ((LA90_0>=110 && LA90_0<=115)) ) {
                    alt90=1;
                }


                switch (alt90) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:962:5: ( '==' | '!=' | '<' | '<=' | '>' | '>=' ) add
            	    {
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:962:5: ( '==' | '!=' | '<' | '<=' | '>' | '>=' )
            	    int alt89=6;
            	    switch ( input.LA(1) ) {
            	    case 110:
            	        {
            	        alt89=1;
            	        }
            	        break;
            	    case 111:
            	        {
            	        alt89=2;
            	        }
            	        break;
            	    case 112:
            	        {
            	        alt89=3;
            	        }
            	        break;
            	    case 113:
            	        {
            	        alt89=4;
            	        }
            	        break;
            	    case 114:
            	        {
            	        alt89=5;
            	        }
            	        break;
            	    case 115:
            	        {
            	        alt89=6;
            	        }
            	        break;
            	    default:
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 89, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt89) {
            	        case 1 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:963:7: '=='
            	            {
            	            string_literal312=(Token)match(input,110,FOLLOW_110_in_relational3604); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            string_literal312_tree = (CommonTree)adaptor.create(string_literal312);
            	            root_0 = (CommonTree)adaptor.becomeRoot(string_literal312_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:964:9: '!='
            	            {
            	            string_literal313=(Token)match(input,111,FOLLOW_111_in_relational3615); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            string_literal313_tree = (CommonTree)adaptor.create(string_literal313);
            	            root_0 = (CommonTree)adaptor.becomeRoot(string_literal313_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 3 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:965:9: '<'
            	            {
            	            char_literal314=(Token)match(input,112,FOLLOW_112_in_relational3626); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            char_literal314_tree = (CommonTree)adaptor.create(char_literal314);
            	            root_0 = (CommonTree)adaptor.becomeRoot(char_literal314_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 4 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:966:9: '<='
            	            {
            	            string_literal315=(Token)match(input,113,FOLLOW_113_in_relational3637); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            string_literal315_tree = (CommonTree)adaptor.create(string_literal315);
            	            root_0 = (CommonTree)adaptor.becomeRoot(string_literal315_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 5 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:967:9: '>'
            	            {
            	            char_literal316=(Token)match(input,114,FOLLOW_114_in_relational3648); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            char_literal316_tree = (CommonTree)adaptor.create(char_literal316);
            	            root_0 = (CommonTree)adaptor.becomeRoot(char_literal316_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 6 :
            	            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:968:9: '>='
            	            {
            	            string_literal317=(Token)match(input,115,FOLLOW_115_in_relational3659); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            string_literal317_tree = (CommonTree)adaptor.create(string_literal317);
            	            root_0 = (CommonTree)adaptor.becomeRoot(string_literal317_tree, root_0);
            	            }

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_add_in_relational3672);
            	    add318=add();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, add318.getTree());

            	    }
            	    break;

            	default :
            	    break loop90;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "relational"

    public static class channel_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "channel"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:974:1: channel returns [List<Type> types] : relational ( '<-' relational )* ;
    public final GoSourceParser.channel_return channel() throws RecognitionException {
        GoSourceParser.channel_return retval = new GoSourceParser.channel_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal320=null;
        GoSourceParser.relational_return relational319 = null;

        GoSourceParser.relational_return relational321 = null;


        CommonTree string_literal320_tree=null;


          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:979:3: ( relational ( '<-' relational )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:980:3: relational ( '<-' relational )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_relational_in_channel3700);
            relational319=relational();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, relational319.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:980:14: ( '<-' relational )*
            loop91:
            do {
                int alt91=2;
                int LA91_0 = input.LA(1);

                if ( (LA91_0==82) ) {
                    alt91=1;
                }


                switch (alt91) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:980:15: '<-' relational
            	    {
            	    string_literal320=(Token)match(input,82,FOLLOW_82_in_channel3703); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal320_tree = (CommonTree)adaptor.create(string_literal320);
            	    root_0 = (CommonTree)adaptor.becomeRoot(string_literal320_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_relational_in_channel3706);
            	    relational321=relational();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, relational321.getTree());

            	    }
            	    break;

            	default :
            	    break loop91;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "channel"

    public static class negation_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "negation"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:983:1: negation returns [List<Type> types] : ( '!' )* channel ;
    public final GoSourceParser.negation_return negation() throws RecognitionException {
        GoSourceParser.negation_return retval = new GoSourceParser.negation_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal322=null;
        GoSourceParser.channel_return channel323 = null;


        CommonTree char_literal322_tree=null;


          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:988:3: ( ( '!' )* channel )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:989:3: ( '!' )* channel
            {
            root_0 = (CommonTree)adaptor.nil();

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:989:6: ( '!' )*
            loop92:
            do {
                int alt92=2;
                int LA92_0 = input.LA(1);

                if ( (LA92_0==116) ) {
                    alt92=1;
                }


                switch (alt92) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:989:6: '!'
            	    {
            	    char_literal322=(Token)match(input,116,FOLLOW_116_in_negation3731); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal322_tree = (CommonTree)adaptor.create(char_literal322);
            	    root_0 = (CommonTree)adaptor.becomeRoot(char_literal322_tree, root_0);
            	    }

            	    }
            	    break;

            	default :
            	    break loop92;
                }
            } while (true);

            pushFollow(FOLLOW_channel_in_negation3735);
            channel323=channel();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, channel323.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "negation"

    public static class logical_and_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "logical_and"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:992:1: logical_and returns [List<Type> types] : negation ( '&&' negation )* ;
    public final GoSourceParser.logical_and_return logical_and() throws RecognitionException {
        GoSourceParser.logical_and_return retval = new GoSourceParser.logical_and_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal325=null;
        GoSourceParser.negation_return negation324 = null;

        GoSourceParser.negation_return negation326 = null;


        CommonTree string_literal325_tree=null;


          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:997:3: ( negation ( '&&' negation )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:998:3: negation ( '&&' negation )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_negation_in_logical_and3758);
            negation324=negation();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, negation324.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:998:12: ( '&&' negation )*
            loop93:
            do {
                int alt93=2;
                int LA93_0 = input.LA(1);

                if ( (LA93_0==117) ) {
                    alt93=1;
                }


                switch (alt93) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:998:13: '&&' negation
            	    {
            	    string_literal325=(Token)match(input,117,FOLLOW_117_in_logical_and3761); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal325_tree = (CommonTree)adaptor.create(string_literal325);
            	    root_0 = (CommonTree)adaptor.becomeRoot(string_literal325_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_negation_in_logical_and3764);
            	    negation326=negation();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, negation326.getTree());

            	    }
            	    break;

            	default :
            	    break loop93;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "logical_and"

    public static class logical_or_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "logical_or"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1001:1: logical_or returns [List<Type> types] : logical_and ( '||' logical_and )* ;
    public final GoSourceParser.logical_or_return logical_or() throws RecognitionException {
        GoSourceParser.logical_or_return retval = new GoSourceParser.logical_or_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal328=null;
        GoSourceParser.logical_and_return logical_and327 = null;

        GoSourceParser.logical_and_return logical_and329 = null;


        CommonTree string_literal328_tree=null;


          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1006:3: ( logical_and ( '||' logical_and )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1007:3: logical_and ( '||' logical_and )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_logical_and_in_logical_or3790);
            logical_and327=logical_and();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, logical_and327.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1007:15: ( '||' logical_and )*
            loop94:
            do {
                int alt94=2;
                int LA94_0 = input.LA(1);

                if ( (LA94_0==118) ) {
                    alt94=1;
                }


                switch (alt94) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1007:16: '||' logical_and
            	    {
            	    string_literal328=(Token)match(input,118,FOLLOW_118_in_logical_or3793); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal328_tree = (CommonTree)adaptor.create(string_literal328);
            	    root_0 = (CommonTree)adaptor.becomeRoot(string_literal328_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_logical_and_in_logical_or3796);
            	    logical_and329=logical_and();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, logical_and329.getTree());

            	    }
            	    break;

            	default :
            	    break loop94;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "logical_or"

    public static class operand_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "operand"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1010:1: operand returns [List<Type> types] : ( ( literal )=> literal | ( methodExpr )=> methodExpr | '(' expression ( SEMI )? ')' | qualifiedIdent );
    public final GoSourceParser.operand_return operand() throws RecognitionException {
        GoSourceParser.operand_return retval = new GoSourceParser.operand_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal332=null;
        Token SEMI334=null;
        Token char_literal335=null;
        GoSourceParser.literal_return literal330 = null;

        GoSourceParser.methodExpr_return methodExpr331 = null;

        GoSourceParser.expression_return expression333 = null;

        GoSourceParser.qualifiedIdent_return qualifiedIdent336 = null;


        CommonTree char_literal332_tree=null;
        CommonTree SEMI334_tree=null;
        CommonTree char_literal335_tree=null;


          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1015:3: ( ( literal )=> literal | ( methodExpr )=> methodExpr | '(' expression ( SEMI )? ')' | qualifiedIdent )
            int alt96=4;
            alt96 = dfa96.predict(input);
            switch (alt96) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1016:3: ( literal )=> literal
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_literal_in_operand3827);
                    literal330=literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, literal330.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1017:5: ( methodExpr )=> methodExpr
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_methodExpr_in_operand3839);
                    methodExpr331=methodExpr();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, methodExpr331.getTree());

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1018:5: '(' expression ( SEMI )? ')'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    char_literal332=(Token)match(input,64,FOLLOW_64_in_operand3845); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal332_tree = (CommonTree)adaptor.create(char_literal332);
                    adaptor.addChild(root_0, char_literal332_tree);
                    }
                    pushFollow(FOLLOW_expression_in_operand3847);
                    expression333=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression333.getTree());
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1018:24: ( SEMI )?
                    int alt95=2;
                    int LA95_0 = input.LA(1);

                    if ( (LA95_0==SEMI) ) {
                        alt95=1;
                    }
                    switch (alt95) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1018:24: SEMI
                            {
                            SEMI334=(Token)match(input,SEMI,FOLLOW_SEMI_in_operand3849); if (state.failed) return retval;

                            }
                            break;

                    }

                    char_literal335=(Token)match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_operand3856); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal335_tree = (CommonTree)adaptor.create(char_literal335);
                    adaptor.addChild(root_0, char_literal335_tree);
                    }

                    }
                    break;
                case 4 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1020:5: qualifiedIdent
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_qualifiedIdent_in_operand3862);
                    qualifiedIdent336=qualifiedIdent();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, qualifiedIdent336.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "operand"

    public static class methodExpr_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "methodExpr"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1023:1: methodExpr : receiverType '.' methodName ;
    public final GoSourceParser.methodExpr_return methodExpr() throws RecognitionException {
        GoSourceParser.methodExpr_return retval = new GoSourceParser.methodExpr_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal338=null;
        GoSourceParser.receiverType_return receiverType337 = null;

        GoSourceParser.methodName_return methodName339 = null;


        CommonTree char_literal338_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1024:3: ( receiverType '.' methodName )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1025:3: receiverType '.' methodName
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_receiverType_in_methodExpr3877);
            receiverType337=receiverType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, receiverType337.getTree());
            char_literal338=(Token)match(input,65,FOLLOW_65_in_methodExpr3879); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal338_tree = (CommonTree)adaptor.create(char_literal338);
            adaptor.addChild(root_0, char_literal338_tree);
            }
            pushFollow(FOLLOW_methodName_in_methodExpr3881);
            methodName339=methodName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, methodName339.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "methodExpr"

    public static class receiverType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "receiverType"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1028:1: receiverType : ( typeName | '(' '*' typeName ( SEMI )? ')' );
    public final GoSourceParser.receiverType_return receiverType() throws RecognitionException {
        GoSourceParser.receiverType_return retval = new GoSourceParser.receiverType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal341=null;
        Token char_literal342=null;
        Token SEMI344=null;
        Token char_literal345=null;
        GoSourceParser.typeName_return typeName340 = null;

        GoSourceParser.typeName_return typeName343 = null;


        CommonTree char_literal341_tree=null;
        CommonTree char_literal342_tree=null;
        CommonTree SEMI344_tree=null;
        CommonTree char_literal345_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1029:3: ( typeName | '(' '*' typeName ( SEMI )? ')' )
            int alt98=2;
            int LA98_0 = input.LA(1);

            if ( ((LA98_0>=Identifier && LA98_0<=REAL)||LA98_0==98) ) {
                alt98=1;
            }
            else if ( (LA98_0==64) ) {
                alt98=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 98, 0, input);

                throw nvae;
            }
            switch (alt98) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1030:3: typeName
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_typeName_in_receiverType3896);
                    typeName340=typeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeName340.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1031:5: '(' '*' typeName ( SEMI )? ')'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    char_literal341=(Token)match(input,64,FOLLOW_64_in_receiverType3902); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal341_tree = (CommonTree)adaptor.create(char_literal341);
                    adaptor.addChild(root_0, char_literal341_tree);
                    }
                    char_literal342=(Token)match(input,68,FOLLOW_68_in_receiverType3904); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal342_tree = (CommonTree)adaptor.create(char_literal342);
                    adaptor.addChild(root_0, char_literal342_tree);
                    }
                    pushFollow(FOLLOW_typeName_in_receiverType3906);
                    typeName343=typeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeName343.getTree());
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1031:26: ( SEMI )?
                    int alt97=2;
                    int LA97_0 = input.LA(1);

                    if ( (LA97_0==SEMI) ) {
                        alt97=1;
                    }
                    switch (alt97) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1031:26: SEMI
                            {
                            SEMI344=(Token)match(input,SEMI,FOLLOW_SEMI_in_receiverType3908); if (state.failed) return retval;

                            }
                            break;

                    }

                    char_literal345=(Token)match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_receiverType3915); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal345_tree = (CommonTree)adaptor.create(char_literal345);
                    adaptor.addChild(root_0, char_literal345_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "receiverType"

    public static class literal_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "literal"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1035:1: literal returns [List<Type> types] : ( basicLit | ( compositeLit ~ ( 'else' ) )=> compositeLit | functionLit );
    public final GoSourceParser.literal_return literal() throws RecognitionException {
        GoSourceParser.literal_return retval = new GoSourceParser.literal_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.basicLit_return basicLit346 = null;

        GoSourceParser.compositeLit_return compositeLit347 = null;

        GoSourceParser.functionLit_return functionLit348 = null;




          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1040:3: ( basicLit | ( compositeLit ~ ( 'else' ) )=> compositeLit | functionLit )
            int alt99=3;
            alt99 = dfa99.predict(input);
            switch (alt99) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1041:3: basicLit
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_basicLit_in_literal3938);
                    basicLit346=basicLit();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, basicLit346.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1042:5: ( compositeLit ~ ( 'else' ) )=> compositeLit
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_compositeLit_in_literal3956);
                    compositeLit347=compositeLit();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, compositeLit347.getTree());

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1043:5: functionLit
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_functionLit_in_literal3962);
                    functionLit348=functionLit();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functionLit348.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "literal"

    public static class basicLit_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "basicLit"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1046:1: basicLit : ( Integer | Octal_Lit | Hex_Lit | Float_Lit | Imaginary_Lit | Char_Lit | String_Lit );
    public final GoSourceParser.basicLit_return basicLit() throws RecognitionException {
        GoSourceParser.basicLit_return retval = new GoSourceParser.basicLit_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set349=null;

        CommonTree set349_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1047:3: ( Integer | Octal_Lit | Hex_Lit | Float_Lit | Imaginary_Lit | Char_Lit | String_Lit )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set349=(Token)input.LT(1);
            if ( input.LA(1)==String_Lit||(input.LA(1)>=Integer && input.LA(1)<=Char_Lit) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set349));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "basicLit"

    public static class compositeLit_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "compositeLit"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1057:1: compositeLit returns [List<Type> types] : literalType literalValue ;
    public final GoSourceParser.compositeLit_return compositeLit() throws RecognitionException {
        GoSourceParser.compositeLit_return retval = new GoSourceParser.compositeLit_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.literalType_return literalType350 = null;

        GoSourceParser.literalValue_return literalValue351 = null;




          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1062:3: ( literalType literalValue )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1063:3: literalType literalValue
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_literalType_in_compositeLit4036);
            literalType350=literalType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, literalType350.getTree());
            pushFollow(FOLLOW_literalValue_in_compositeLit4038);
            literalValue351=literalValue();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, literalValue351.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "compositeLit"

    public static class literalType_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "literalType"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1066:1: literalType returns [List<Type> types] : ( structType | arrayType | '[' '...' ']' elementType | sliceType | mapType | typeName );
    public final GoSourceParser.literalType_return literalType() throws RecognitionException {
        GoSourceParser.literalType_return retval = new GoSourceParser.literalType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal354=null;
        Token string_literal355=null;
        Token char_literal356=null;
        GoSourceParser.structType_return structType352 = null;

        GoSourceParser.arrayType_return arrayType353 = null;

        GoSourceParser.elementType_return elementType357 = null;

        GoSourceParser.sliceType_return sliceType358 = null;

        GoSourceParser.mapType_return mapType359 = null;

        GoSourceParser.typeName_return typeName360 = null;


        CommonTree char_literal354_tree=null;
        CommonTree string_literal355_tree=null;
        CommonTree char_literal356_tree=null;


          retval.types = new ArrayList();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1071:3: ( structType | arrayType | '[' '...' ']' elementType | sliceType | mapType | typeName )
            int alt100=6;
            switch ( input.LA(1) ) {
            case STRUCT:
                {
                alt100=1;
                }
                break;
            case 99:
                {
                switch ( input.LA(2) ) {
                case 70:
                    {
                    alt100=3;
                    }
                    break;
                case CLOSE_SQUARE:
                    {
                    alt100=4;
                    }
                    break;
                case String_Lit:
                case Integer:
                case Octal_Lit:
                case Hex_Lit:
                case Float_Lit:
                case Imaginary_Lit:
                case Char_Lit:
                case Identifier:
                case APPEND:
                case CAP:
                case CLOSE:
                case CLOSED:
                case CMPLX:
                case COPY:
                case IMAG:
                case LEN:
                case MAKE:
                case PANIC:
                case PRINT:
                case PRINTLN:
                case REAL:
                case STRUCT:
                case INTERFACE:
                case 64:
                case 67:
                case 68:
                case 82:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                case 116:
                case 119:
                case 120:
                    {
                    alt100=2;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 100, 2, input);

                    throw nvae;
                }

                }
                break;
            case 119:
                {
                alt100=5;
                }
                break;
            case Identifier:
            case APPEND:
            case CAP:
            case CLOSE:
            case CLOSED:
            case CMPLX:
            case COPY:
            case IMAG:
            case LEN:
            case MAKE:
            case PANIC:
            case PRINT:
            case PRINTLN:
            case REAL:
            case 98:
                {
                alt100=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 100, 0, input);

                throw nvae;
            }

            switch (alt100) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1072:3: structType
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_structType_in_literalType4061);
                    structType352=structType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, structType352.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1073:5: arrayType
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_arrayType_in_literalType4067);
                    arrayType353=arrayType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arrayType353.getTree());

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1074:5: '[' '...' ']' elementType
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    char_literal354=(Token)match(input,99,FOLLOW_99_in_literalType4073); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal354_tree = (CommonTree)adaptor.create(char_literal354);
                    adaptor.addChild(root_0, char_literal354_tree);
                    }
                    string_literal355=(Token)match(input,70,FOLLOW_70_in_literalType4075); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal355_tree = (CommonTree)adaptor.create(string_literal355);
                    adaptor.addChild(root_0, string_literal355_tree);
                    }
                    char_literal356=(Token)match(input,CLOSE_SQUARE,FOLLOW_CLOSE_SQUARE_in_literalType4077); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal356_tree = (CommonTree)adaptor.create(char_literal356);
                    adaptor.addChild(root_0, char_literal356_tree);
                    }
                    pushFollow(FOLLOW_elementType_in_literalType4079);
                    elementType357=elementType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, elementType357.getTree());

                    }
                    break;
                case 4 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1075:5: sliceType
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_sliceType_in_literalType4085);
                    sliceType358=sliceType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sliceType358.getTree());

                    }
                    break;
                case 5 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1076:5: mapType
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_mapType_in_literalType4091);
                    mapType359=mapType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, mapType359.getTree());

                    }
                    break;
                case 6 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1077:5: typeName
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_typeName_in_literalType4097);
                    typeName360=typeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeName360.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "literalType"

    public static class literalValue_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "literalValue"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1080:1: literalValue : '{' ( elementList ( ',' )? )? ( SEMI )? '}' ;
    public final GoSourceParser.literalValue_return literalValue() throws RecognitionException {
        GoSourceParser.literalValue_return retval = new GoSourceParser.literalValue_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal361=null;
        Token char_literal363=null;
        Token SEMI364=null;
        Token char_literal365=null;
        GoSourceParser.elementList_return elementList362 = null;


        CommonTree char_literal361_tree=null;
        CommonTree char_literal363_tree=null;
        CommonTree SEMI364_tree=null;
        CommonTree char_literal365_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1081:3: ( '{' ( elementList ( ',' )? )? ( SEMI )? '}' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1082:3: '{' ( elementList ( ',' )? )? ( SEMI )? '}'
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal361=(Token)match(input,71,FOLLOW_71_in_literalValue4112); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal361_tree = (CommonTree)adaptor.create(char_literal361);
            root_0 = (CommonTree)adaptor.becomeRoot(char_literal361_tree, root_0);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1082:8: ( elementList ( ',' )? )?
            int alt102=2;
            int LA102_0 = input.LA(1);

            if ( (LA102_0==String_Lit||(LA102_0>=Integer && LA102_0<=REAL)||(LA102_0>=STRUCT && LA102_0<=INTERFACE)||LA102_0==64||(LA102_0>=67 && LA102_0<=68)||LA102_0==71||LA102_0==82||(LA102_0>=98 && LA102_0<=103)||LA102_0==116||(LA102_0>=119 && LA102_0<=120)) ) {
                alt102=1;
            }
            switch (alt102) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1082:9: elementList ( ',' )?
                    {
                    pushFollow(FOLLOW_elementList_in_literalValue4116);
                    elementList362=elementList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, elementList362.getTree());
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1082:21: ( ',' )?
                    int alt101=2;
                    int LA101_0 = input.LA(1);

                    if ( (LA101_0==69) ) {
                        alt101=1;
                    }
                    switch (alt101) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1082:22: ','
                            {
                            char_literal363=(Token)match(input,69,FOLLOW_69_in_literalValue4119); if (state.failed) return retval;

                            }
                            break;

                    }


                    }
                    break;

            }

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1082:35: ( SEMI )?
            int alt103=2;
            int LA103_0 = input.LA(1);

            if ( (LA103_0==SEMI) ) {
                alt103=1;
            }
            switch (alt103) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1082:35: SEMI
                    {
                    SEMI364=(Token)match(input,SEMI,FOLLOW_SEMI_in_literalValue4126); if (state.failed) return retval;

                    }
                    break;

            }

            char_literal365=(Token)match(input,CLOSE_CURLY,FOLLOW_CLOSE_CURLY_in_literalValue4133); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal365_tree = (CommonTree)adaptor.create(char_literal365);
            adaptor.addChild(root_0, char_literal365_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "literalValue"

    public static class elementList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "elementList"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1086:1: elementList : element ( ',' element )* ;
    public final GoSourceParser.elementList_return elementList() throws RecognitionException {
        GoSourceParser.elementList_return retval = new GoSourceParser.elementList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal367=null;
        GoSourceParser.element_return element366 = null;

        GoSourceParser.element_return element368 = null;


        CommonTree char_literal367_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1087:3: ( element ( ',' element )* )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1088:3: element ( ',' element )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_element_in_elementList4148);
            element366=element();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, element366.getTree());
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1088:11: ( ',' element )*
            loop104:
            do {
                int alt104=2;
                int LA104_0 = input.LA(1);

                if ( (LA104_0==69) ) {
                    int LA104_1 = input.LA(2);

                    if ( (LA104_1==String_Lit||(LA104_1>=Integer && LA104_1<=REAL)||(LA104_1>=STRUCT && LA104_1<=INTERFACE)||LA104_1==64||(LA104_1>=67 && LA104_1<=68)||LA104_1==71||LA104_1==82||(LA104_1>=98 && LA104_1<=103)||LA104_1==116||(LA104_1>=119 && LA104_1<=120)) ) {
                        alt104=1;
                    }


                }


                switch (alt104) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1088:12: ',' element
            	    {
            	    char_literal367=(Token)match(input,69,FOLLOW_69_in_elementList4151); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal367_tree = (CommonTree)adaptor.create(char_literal367);
            	    root_0 = (CommonTree)adaptor.becomeRoot(char_literal367_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_element_in_elementList4154);
            	    element368=element();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, element368.getTree());

            	    }
            	    break;

            	default :
            	    break loop104;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "elementList"

    public static class element_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "element"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1091:1: element : ( ( key ':' )=> key ':' value | value );
    public final GoSourceParser.element_return element() throws RecognitionException {
        GoSourceParser.element_return retval = new GoSourceParser.element_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal370=null;
        GoSourceParser.key_return key369 = null;

        GoSourceParser.value_return value371 = null;

        GoSourceParser.value_return value372 = null;


        CommonTree char_literal370_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1092:3: ( ( key ':' )=> key ':' value | value )
            int alt105=2;
            alt105 = dfa105.predict(input);
            switch (alt105) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1093:3: ( key ':' )=> key ':' value
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_key_in_element4179);
                    key369=key();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, key369.getTree());
                    char_literal370=(Token)match(input,74,FOLLOW_74_in_element4181); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal370_tree = (CommonTree)adaptor.create(char_literal370);
                    root_0 = (CommonTree)adaptor.becomeRoot(char_literal370_tree, root_0);
                    }
                    pushFollow(FOLLOW_value_in_element4184);
                    value371=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value371.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1094:5: value
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_value_in_element4190);
                    value372=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value372.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "element"

    public static class key_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "key"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1097:1: key : ( ( fieldName )=> fieldName | elementIndex );
    public final GoSourceParser.key_return key() throws RecognitionException {
        GoSourceParser.key_return retval = new GoSourceParser.key_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.fieldName_return fieldName373 = null;

        GoSourceParser.elementIndex_return elementIndex374 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1098:3: ( ( fieldName )=> fieldName | elementIndex )
            int alt106=2;
            alt106 = dfa106.predict(input);
            switch (alt106) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1099:3: ( fieldName )=> fieldName
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_fieldName_in_key4211);
                    fieldName373=fieldName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, fieldName373.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1100:5: elementIndex
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_elementIndex_in_key4217);
                    elementIndex374=elementIndex();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, elementIndex374.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "key"

    public static class fieldName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fieldName"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1103:1: fieldName : identifier ;
    public final GoSourceParser.fieldName_return fieldName() throws RecognitionException {
        GoSourceParser.fieldName_return retval = new GoSourceParser.fieldName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.identifier_return identifier375 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1104:3: ( identifier )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1105:3: identifier
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_identifier_in_fieldName4232);
            identifier375=identifier();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, identifier375.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "fieldName"

    public static class elementIndex_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "elementIndex"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1108:1: elementIndex : expression ;
    public final GoSourceParser.elementIndex_return elementIndex() throws RecognitionException {
        GoSourceParser.elementIndex_return retval = new GoSourceParser.elementIndex_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.expression_return expression376 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1109:3: ( expression )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1110:3: expression
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_expression_in_elementIndex4247);
            expression376=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression376.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "elementIndex"

    public static class value_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1113:1: value : ( expression | literalValue );
    public final GoSourceParser.value_return value() throws RecognitionException {
        GoSourceParser.value_return retval = new GoSourceParser.value_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.expression_return expression377 = null;

        GoSourceParser.literalValue_return literalValue378 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1114:3: ( expression | literalValue )
            int alt107=2;
            int LA107_0 = input.LA(1);

            if ( (LA107_0==String_Lit||(LA107_0>=Integer && LA107_0<=REAL)||(LA107_0>=STRUCT && LA107_0<=INTERFACE)||LA107_0==64||(LA107_0>=67 && LA107_0<=68)||LA107_0==82||(LA107_0>=98 && LA107_0<=103)||LA107_0==116||(LA107_0>=119 && LA107_0<=120)) ) {
                alt107=1;
            }
            else if ( (LA107_0==71) ) {
                alt107=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 107, 0, input);

                throw nvae;
            }
            switch (alt107) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1115:3: expression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_expression_in_value4262);
                    expression377=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression377.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1116:5: literalValue
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_literalValue_in_value4268);
                    literalValue378=literalValue();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, literalValue378.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "value"

    public static class packageName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "packageName"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1119:1: packageName : identifier ;
    public final GoSourceParser.packageName_return packageName() throws RecognitionException {
        GoSourceParser.packageName_return retval = new GoSourceParser.packageName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.identifier_return identifier379 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1120:3: ( identifier )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1121:3: identifier
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_identifier_in_packageName4283);
            identifier379=identifier();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, identifier379.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "packageName"

    public static class qualifiedIdent_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "qualifiedIdent"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1124:1: qualifiedIdent : ( ( identifier '.' identifier )=> packageName '.' identifier | identifier );
    public final GoSourceParser.qualifiedIdent_return qualifiedIdent() throws RecognitionException {
        GoSourceParser.qualifiedIdent_return retval = new GoSourceParser.qualifiedIdent_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal381=null;
        GoSourceParser.packageName_return packageName380 = null;

        GoSourceParser.identifier_return identifier382 = null;

        GoSourceParser.identifier_return identifier383 = null;


        CommonTree char_literal381_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1125:3: ( ( identifier '.' identifier )=> packageName '.' identifier | identifier )
            int alt108=2;
            alt108 = dfa108.predict(input);
            switch (alt108) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1126:3: ( identifier '.' identifier )=> packageName '.' identifier
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_packageName_in_qualifiedIdent4308);
                    packageName380=packageName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, packageName380.getTree());
                    char_literal381=(Token)match(input,65,FOLLOW_65_in_qualifiedIdent4310); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal381_tree = (CommonTree)adaptor.create(char_literal381);
                    adaptor.addChild(root_0, char_literal381_tree);
                    }
                    pushFollow(FOLLOW_identifier_in_qualifiedIdent4312);
                    identifier382=identifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identifier382.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1127:5: identifier
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_identifier_in_qualifiedIdent4318);
                    identifier383=identifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identifier383.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "qualifiedIdent"

    public static class type_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "type"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1130:1: type : ( '(' type ( SEMI )? ')' | typeLit | typeName );
    public final GoSourceParser.type_return type() throws RecognitionException {
        GoSourceParser.type_return retval = new GoSourceParser.type_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal384=null;
        Token SEMI386=null;
        Token char_literal387=null;
        GoSourceParser.type_return type385 = null;

        GoSourceParser.typeLit_return typeLit388 = null;

        GoSourceParser.typeName_return typeName389 = null;


        CommonTree char_literal384_tree=null;
        CommonTree SEMI386_tree=null;
        CommonTree char_literal387_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1131:3: ( '(' type ( SEMI )? ')' | typeLit | typeName )
            int alt110=3;
            switch ( input.LA(1) ) {
            case 64:
                {
                alt110=1;
                }
                break;
            case STRUCT:
            case INTERFACE:
            case 67:
            case 68:
            case 82:
            case 99:
            case 119:
            case 120:
                {
                alt110=2;
                }
                break;
            case Identifier:
            case APPEND:
            case CAP:
            case CLOSE:
            case CLOSED:
            case CMPLX:
            case COPY:
            case IMAG:
            case LEN:
            case MAKE:
            case PANIC:
            case PRINT:
            case PRINTLN:
            case REAL:
            case 98:
                {
                alt110=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 110, 0, input);

                throw nvae;
            }

            switch (alt110) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1132:3: '(' type ( SEMI )? ')'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    char_literal384=(Token)match(input,64,FOLLOW_64_in_type4333); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal384_tree = (CommonTree)adaptor.create(char_literal384);
                    adaptor.addChild(root_0, char_literal384_tree);
                    }
                    pushFollow(FOLLOW_type_in_type4335);
                    type385=type();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, type385.getTree());
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1132:16: ( SEMI )?
                    int alt109=2;
                    int LA109_0 = input.LA(1);

                    if ( (LA109_0==SEMI) ) {
                        alt109=1;
                    }
                    switch (alt109) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1132:16: SEMI
                            {
                            SEMI386=(Token)match(input,SEMI,FOLLOW_SEMI_in_type4337); if (state.failed) return retval;

                            }
                            break;

                    }

                    char_literal387=(Token)match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_type4344); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal387_tree = (CommonTree)adaptor.create(char_literal387);
                    adaptor.addChild(root_0, char_literal387_tree);
                    }

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1134:5: typeLit
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_typeLit_in_type4350);
                    typeLit388=typeLit();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeLit388.getTree());

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1135:5: typeName
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_typeName_in_type4356);
                    typeName389=typeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeName389.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "type"

    public static class typeName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typeName"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1138:1: typeName : qualifiedIdent ;
    public final GoSourceParser.typeName_return typeName() throws RecognitionException {
        GoSourceParser.typeName_return retval = new GoSourceParser.typeName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.qualifiedIdent_return qualifiedIdent390 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1139:3: ( qualifiedIdent )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1140:3: qualifiedIdent
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_qualifiedIdent_in_typeName4371);
            qualifiedIdent390=qualifiedIdent();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, qualifiedIdent390.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "typeName"

    public static class typeLit_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typeLit"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1143:1: typeLit : ( arrayType | structType | functionType | interfaceType | sliceType | mapType | pointerType | channelType );
    public final GoSourceParser.typeLit_return typeLit() throws RecognitionException {
        GoSourceParser.typeLit_return retval = new GoSourceParser.typeLit_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.arrayType_return arrayType391 = null;

        GoSourceParser.structType_return structType392 = null;

        GoSourceParser.functionType_return functionType393 = null;

        GoSourceParser.interfaceType_return interfaceType394 = null;

        GoSourceParser.sliceType_return sliceType395 = null;

        GoSourceParser.mapType_return mapType396 = null;

        GoSourceParser.pointerType_return pointerType397 = null;

        GoSourceParser.channelType_return channelType398 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1144:3: ( arrayType | structType | functionType | interfaceType | sliceType | mapType | pointerType | channelType )
            int alt111=8;
            alt111 = dfa111.predict(input);
            switch (alt111) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1145:3: arrayType
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_arrayType_in_typeLit4386);
                    arrayType391=arrayType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arrayType391.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1146:5: structType
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_structType_in_typeLit4392);
                    structType392=structType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, structType392.getTree());

                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1147:5: functionType
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_functionType_in_typeLit4398);
                    functionType393=functionType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functionType393.getTree());

                    }
                    break;
                case 4 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1148:5: interfaceType
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_interfaceType_in_typeLit4404);
                    interfaceType394=interfaceType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, interfaceType394.getTree());

                    }
                    break;
                case 5 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1149:5: sliceType
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_sliceType_in_typeLit4410);
                    sliceType395=sliceType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sliceType395.getTree());

                    }
                    break;
                case 6 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1150:5: mapType
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_mapType_in_typeLit4416);
                    mapType396=mapType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, mapType396.getTree());

                    }
                    break;
                case 7 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1151:5: pointerType
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_pointerType_in_typeLit4422);
                    pointerType397=pointerType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, pointerType397.getTree());

                    }
                    break;
                case 8 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1152:5: channelType
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_channelType_in_typeLit4428);
                    channelType398=channelType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, channelType398.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "typeLit"

    public static class interfaceType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "interfaceType"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1155:1: interfaceType : 'interface' ( methodSpec SEMI )* ( SEMI )? '}' ;
    public final GoSourceParser.interfaceType_return interfaceType() throws RecognitionException {
        GoSourceParser.interfaceType_return retval = new GoSourceParser.interfaceType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal399=null;
        Token SEMI401=null;
        Token SEMI402=null;
        Token char_literal403=null;
        GoSourceParser.methodSpec_return methodSpec400 = null;


        CommonTree string_literal399_tree=null;
        CommonTree SEMI401_tree=null;
        CommonTree SEMI402_tree=null;
        CommonTree char_literal403_tree=null;


          pushScope();

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1159:3: ( 'interface' ( methodSpec SEMI )* ( SEMI )? '}' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1160:3: 'interface' ( methodSpec SEMI )* ( SEMI )? '}'
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal399=(Token)match(input,INTERFACE,FOLLOW_INTERFACE_in_interfaceType4447); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal399_tree = (CommonTree)adaptor.create(string_literal399);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal399_tree, root_0);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1160:16: ( methodSpec SEMI )*
            loop112:
            do {
                int alt112=2;
                int LA112_0 = input.LA(1);

                if ( ((LA112_0>=Identifier && LA112_0<=REAL)||LA112_0==98) ) {
                    alt112=1;
                }


                switch (alt112) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1160:17: methodSpec SEMI
            	    {
            	    pushFollow(FOLLOW_methodSpec_in_interfaceType4451);
            	    methodSpec400=methodSpec();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, methodSpec400.getTree());
            	    SEMI401=(Token)match(input,SEMI,FOLLOW_SEMI_in_interfaceType4453); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    SEMI401_tree = (CommonTree)adaptor.create(SEMI401);
            	    adaptor.addChild(root_0, SEMI401_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop112;
                }
            } while (true);

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1160:39: ( SEMI )?
            int alt113=2;
            int LA113_0 = input.LA(1);

            if ( (LA113_0==SEMI) ) {
                alt113=1;
            }
            switch (alt113) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1160:39: SEMI
                    {
                    SEMI402=(Token)match(input,SEMI,FOLLOW_SEMI_in_interfaceType4457); if (state.failed) return retval;

                    }
                    break;

            }

            char_literal403=(Token)match(input,CLOSE_CURLY,FOLLOW_CLOSE_CURLY_in_interfaceType4467); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal403_tree = (CommonTree)adaptor.create(char_literal403);
            adaptor.addChild(root_0, char_literal403_tree);
            }
            if ( state.backtracking==0 ) {
              popScope();
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "interfaceType"

    public static class methodSpec_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "methodSpec"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1166:1: methodSpec : ( methodName signature | interfaceTypeName );
    public final GoSourceParser.methodSpec_return methodSpec() throws RecognitionException {
        GoSourceParser.methodSpec_return retval = new GoSourceParser.methodSpec_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.methodName_return methodName404 = null;

        GoSourceParser.signature_return signature405 = null;

        GoSourceParser.interfaceTypeName_return interfaceTypeName406 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1167:3: ( methodName signature | interfaceTypeName )
            int alt114=2;
            alt114 = dfa114.predict(input);
            switch (alt114) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1168:3: methodName signature
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_methodName_in_methodSpec4486);
                    methodName404=methodName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, methodName404.getTree());
                    pushFollow(FOLLOW_signature_in_methodSpec4488);
                    signature405=signature();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, signature405.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1169:5: interfaceTypeName
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_interfaceTypeName_in_methodSpec4494);
                    interfaceTypeName406=interfaceTypeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, interfaceTypeName406.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "methodSpec"

    public static class methodName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "methodName"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1172:1: methodName : identifier ;
    public final GoSourceParser.methodName_return methodName() throws RecognitionException {
        GoSourceParser.methodName_return retval = new GoSourceParser.methodName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.identifier_return identifier407 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1173:3: ( identifier )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1174:3: identifier
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_identifier_in_methodName4509);
            identifier407=identifier();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, identifier407.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "methodName"

    public static class interfaceTypeName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "interfaceTypeName"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1177:1: interfaceTypeName : typeName ;
    public final GoSourceParser.interfaceTypeName_return interfaceTypeName() throws RecognitionException {
        GoSourceParser.interfaceTypeName_return retval = new GoSourceParser.interfaceTypeName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.typeName_return typeName408 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1178:3: ( typeName )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1179:3: typeName
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_typeName_in_interfaceTypeName4524);
            typeName408=typeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, typeName408.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "interfaceTypeName"

    public static class structType_return extends ParserRuleReturnScope {
        public List<Type> types;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "structType"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1182:1: structType returns [List<Type> types] : 'struct' '{' ( fieldDecl SEMI )* ( SEMI )? '}' ;
    public final GoSourceParser.structType_return structType() throws RecognitionException {
        GoSourceParser.structType_return retval = new GoSourceParser.structType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal409=null;
        Token char_literal410=null;
        Token SEMI412=null;
        Token SEMI413=null;
        Token char_literal414=null;
        GoSourceParser.fieldDecl_return fieldDecl411 = null;


        CommonTree string_literal409_tree=null;
        CommonTree char_literal410_tree=null;
        CommonTree SEMI412_tree=null;
        CommonTree SEMI413_tree=null;
        CommonTree char_literal414_tree=null;


          retval.types = new ArrayList();
          {
            pushScope();
          }

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1190:3: ( 'struct' '{' ( fieldDecl SEMI )* ( SEMI )? '}' )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1191:3: 'struct' '{' ( fieldDecl SEMI )* ( SEMI )? '}'
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal409=(Token)match(input,STRUCT,FOLLOW_STRUCT_in_structType4547); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal409_tree = (CommonTree)adaptor.create(string_literal409);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal409_tree, root_0);
            }
            char_literal410=(Token)match(input,71,FOLLOW_71_in_structType4553); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal410_tree = (CommonTree)adaptor.create(char_literal410);
            adaptor.addChild(root_0, char_literal410_tree);
            }
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1196:3: ( fieldDecl SEMI )*
            loop115:
            do {
                int alt115=2;
                int LA115_0 = input.LA(1);

                if ( ((LA115_0>=Identifier && LA115_0<=REAL)||LA115_0==68||LA115_0==98) ) {
                    alt115=1;
                }


                switch (alt115) {
            	case 1 :
            	    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1196:4: fieldDecl SEMI
            	    {
            	    pushFollow(FOLLOW_fieldDecl_in_structType4566);
            	    fieldDecl411=fieldDecl();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, fieldDecl411.getTree());
            	    SEMI412=(Token)match(input,SEMI,FOLLOW_SEMI_in_structType4568); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    SEMI412_tree = (CommonTree)adaptor.create(SEMI412);
            	    adaptor.addChild(root_0, SEMI412_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop115;
                }
            } while (true);

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1196:25: ( SEMI )?
            int alt116=2;
            int LA116_0 = input.LA(1);

            if ( (LA116_0==SEMI) ) {
                alt116=1;
            }
            switch (alt116) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1196:25: SEMI
                    {
                    SEMI413=(Token)match(input,SEMI,FOLLOW_SEMI_in_structType4572); if (state.failed) return retval;

                    }
                    break;

            }

            char_literal414=(Token)match(input,CLOSE_CURLY,FOLLOW_CLOSE_CURLY_in_structType4586); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal414_tree = (CommonTree)adaptor.create(char_literal414);
            adaptor.addChild(root_0, char_literal414_tree);
            }
            if ( state.backtracking==0 ) {

                  popScope();
                
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "structType"

    public static class fieldDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fieldDecl"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1206:1: fieldDecl : ( identifierList type | anonymousField ) ( tag )? ;
    public final GoSourceParser.fieldDecl_return fieldDecl() throws RecognitionException {
        GoSourceParser.fieldDecl_return retval = new GoSourceParser.fieldDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.identifierList_return identifierList415 = null;

        GoSourceParser.type_return type416 = null;

        GoSourceParser.anonymousField_return anonymousField417 = null;

        GoSourceParser.tag_return tag418 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1207:3: ( ( identifierList type | anonymousField ) ( tag )? )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1208:3: ( identifierList type | anonymousField ) ( tag )?
            {
            root_0 = (CommonTree)adaptor.nil();

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1208:3: ( identifierList type | anonymousField )
            int alt117=2;
            alt117 = dfa117.predict(input);
            switch (alt117) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1209:5: identifierList type
                    {
                    pushFollow(FOLLOW_identifierList_in_fieldDecl4611);
                    identifierList415=identifierList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identifierList415.getTree());
                    pushFollow(FOLLOW_type_in_fieldDecl4613);
                    type416=type();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, type416.getTree());

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1210:7: anonymousField
                    {
                    pushFollow(FOLLOW_anonymousField_in_fieldDecl4621);
                    anonymousField417=anonymousField();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, anonymousField417.getTree());

                    }
                    break;

            }

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1212:3: ( tag )?
            int alt118=2;
            int LA118_0 = input.LA(1);

            if ( (LA118_0==String_Lit) ) {
                alt118=1;
            }
            switch (alt118) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1212:4: tag
                    {
                    pushFollow(FOLLOW_tag_in_fieldDecl4630);
                    tag418=tag();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tag418.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "fieldDecl"

    public static class anonymousField_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "anonymousField"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1215:1: anonymousField : ( '*' )? typeName ;
    public final GoSourceParser.anonymousField_return anonymousField() throws RecognitionException {
        GoSourceParser.anonymousField_return retval = new GoSourceParser.anonymousField_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal419=null;
        GoSourceParser.typeName_return typeName420 = null;


        CommonTree char_literal419_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1216:3: ( ( '*' )? typeName )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1217:3: ( '*' )? typeName
            {
            root_0 = (CommonTree)adaptor.nil();

            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1217:3: ( '*' )?
            int alt119=2;
            int LA119_0 = input.LA(1);

            if ( (LA119_0==68) ) {
                alt119=1;
            }
            switch (alt119) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1217:3: '*'
                    {
                    char_literal419=(Token)match(input,68,FOLLOW_68_in_anonymousField4647); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal419_tree = (CommonTree)adaptor.create(char_literal419);
                    adaptor.addChild(root_0, char_literal419_tree);
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_typeName_in_anonymousField4650);
            typeName420=typeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, typeName420.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "anonymousField"

    public static class tag_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "tag"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1220:1: tag : String_Lit ;
    public final GoSourceParser.tag_return tag() throws RecognitionException {
        GoSourceParser.tag_return retval = new GoSourceParser.tag_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token String_Lit421=null;

        CommonTree String_Lit421_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1221:3: ( String_Lit )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1222:3: String_Lit
            {
            root_0 = (CommonTree)adaptor.nil();

            String_Lit421=(Token)match(input,String_Lit,FOLLOW_String_Lit_in_tag4665); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            String_Lit421_tree = (CommonTree)adaptor.create(String_Lit421);
            adaptor.addChild(root_0, String_Lit421_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "tag"

    public static class functionLit_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "functionLit"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1225:1: functionLit : functionType body ;
    public final GoSourceParser.functionLit_return functionLit() throws RecognitionException {
        GoSourceParser.functionLit_return retval = new GoSourceParser.functionLit_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.functionType_return functionType422 = null;

        GoSourceParser.body_return body423 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1226:3: ( functionType body )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1227:3: functionType body
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_functionType_in_functionLit4680);
            functionType422=functionType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, functionType422.getTree());
            pushFollow(FOLLOW_body_in_functionLit4682);
            body423=body();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, body423.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "functionLit"

    public static class functionType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "functionType"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1230:1: functionType : 'func' signature ;
    public final GoSourceParser.functionType_return functionType() throws RecognitionException {
        GoSourceParser.functionType_return retval = new GoSourceParser.functionType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal424=null;
        GoSourceParser.signature_return signature425 = null;


        CommonTree string_literal424_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1231:3: ( 'func' signature )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1232:3: 'func' signature
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal424=(Token)match(input,67,FOLLOW_67_in_functionType4697); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal424_tree = (CommonTree)adaptor.create(string_literal424);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal424_tree, root_0);
            }
            pushFollow(FOLLOW_signature_in_functionType4700);
            signature425=signature();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, signature425.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "functionType"

    public static class pointerType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pointerType"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1235:1: pointerType : '*' baseType ;
    public final GoSourceParser.pointerType_return pointerType() throws RecognitionException {
        GoSourceParser.pointerType_return retval = new GoSourceParser.pointerType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal426=null;
        GoSourceParser.baseType_return baseType427 = null;


        CommonTree char_literal426_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1236:3: ( '*' baseType )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1237:3: '*' baseType
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal426=(Token)match(input,68,FOLLOW_68_in_pointerType4715); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal426_tree = (CommonTree)adaptor.create(char_literal426);
            adaptor.addChild(root_0, char_literal426_tree);
            }
            pushFollow(FOLLOW_baseType_in_pointerType4717);
            baseType427=baseType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, baseType427.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "pointerType"

    public static class sliceType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sliceType"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1240:1: sliceType : '[' ']' elementType ;
    public final GoSourceParser.sliceType_return sliceType() throws RecognitionException {
        GoSourceParser.sliceType_return retval = new GoSourceParser.sliceType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal428=null;
        Token char_literal429=null;
        GoSourceParser.elementType_return elementType430 = null;


        CommonTree char_literal428_tree=null;
        CommonTree char_literal429_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1241:3: ( '[' ']' elementType )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1242:3: '[' ']' elementType
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal428=(Token)match(input,99,FOLLOW_99_in_sliceType4732); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal428_tree = (CommonTree)adaptor.create(char_literal428);
            adaptor.addChild(root_0, char_literal428_tree);
            }
            char_literal429=(Token)match(input,CLOSE_SQUARE,FOLLOW_CLOSE_SQUARE_in_sliceType4734); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal429_tree = (CommonTree)adaptor.create(char_literal429);
            adaptor.addChild(root_0, char_literal429_tree);
            }
            pushFollow(FOLLOW_elementType_in_sliceType4736);
            elementType430=elementType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, elementType430.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "sliceType"

    public static class mapType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mapType"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1245:1: mapType : 'map' '[' keyType ']' elementType ;
    public final GoSourceParser.mapType_return mapType() throws RecognitionException {
        GoSourceParser.mapType_return retval = new GoSourceParser.mapType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal431=null;
        Token char_literal432=null;
        Token char_literal434=null;
        GoSourceParser.keyType_return keyType433 = null;

        GoSourceParser.elementType_return elementType435 = null;


        CommonTree string_literal431_tree=null;
        CommonTree char_literal432_tree=null;
        CommonTree char_literal434_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1246:3: ( 'map' '[' keyType ']' elementType )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1247:3: 'map' '[' keyType ']' elementType
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal431=(Token)match(input,119,FOLLOW_119_in_mapType4751); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal431_tree = (CommonTree)adaptor.create(string_literal431);
            adaptor.addChild(root_0, string_literal431_tree);
            }
            char_literal432=(Token)match(input,99,FOLLOW_99_in_mapType4753); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal432_tree = (CommonTree)adaptor.create(char_literal432);
            adaptor.addChild(root_0, char_literal432_tree);
            }
            pushFollow(FOLLOW_keyType_in_mapType4755);
            keyType433=keyType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, keyType433.getTree());
            char_literal434=(Token)match(input,CLOSE_SQUARE,FOLLOW_CLOSE_SQUARE_in_mapType4757); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal434_tree = (CommonTree)adaptor.create(char_literal434);
            adaptor.addChild(root_0, char_literal434_tree);
            }
            pushFollow(FOLLOW_elementType_in_mapType4759);
            elementType435=elementType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, elementType435.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "mapType"

    public static class arrayType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arrayType"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1250:1: arrayType : '[' arrayLength ']' elementType ;
    public final GoSourceParser.arrayType_return arrayType() throws RecognitionException {
        GoSourceParser.arrayType_return retval = new GoSourceParser.arrayType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal436=null;
        Token char_literal438=null;
        GoSourceParser.arrayLength_return arrayLength437 = null;

        GoSourceParser.elementType_return elementType439 = null;


        CommonTree char_literal436_tree=null;
        CommonTree char_literal438_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1251:3: ( '[' arrayLength ']' elementType )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1252:3: '[' arrayLength ']' elementType
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal436=(Token)match(input,99,FOLLOW_99_in_arrayType4774); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal436_tree = (CommonTree)adaptor.create(char_literal436);
            adaptor.addChild(root_0, char_literal436_tree);
            }
            pushFollow(FOLLOW_arrayLength_in_arrayType4776);
            arrayLength437=arrayLength();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arrayLength437.getTree());
            char_literal438=(Token)match(input,CLOSE_SQUARE,FOLLOW_CLOSE_SQUARE_in_arrayType4778); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal438_tree = (CommonTree)adaptor.create(char_literal438);
            adaptor.addChild(root_0, char_literal438_tree);
            }
            pushFollow(FOLLOW_elementType_in_arrayType4780);
            elementType439=elementType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, elementType439.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "arrayType"

    public static class channelType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "channelType"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1255:1: channelType : ( ( '<-' 'chan' elementType )=> ( '<-' 'chan' elementType ) | ( 'chan' '<-' elementType )=> ( 'chan' '<-' elementType ) | 'chan' elementType );
    public final GoSourceParser.channelType_return channelType() throws RecognitionException {
        GoSourceParser.channelType_return retval = new GoSourceParser.channelType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal440=null;
        Token string_literal441=null;
        Token string_literal443=null;
        Token string_literal444=null;
        Token string_literal446=null;
        GoSourceParser.elementType_return elementType442 = null;

        GoSourceParser.elementType_return elementType445 = null;

        GoSourceParser.elementType_return elementType447 = null;


        CommonTree string_literal440_tree=null;
        CommonTree string_literal441_tree=null;
        CommonTree string_literal443_tree=null;
        CommonTree string_literal444_tree=null;
        CommonTree string_literal446_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1256:3: ( ( '<-' 'chan' elementType )=> ( '<-' 'chan' elementType ) | ( 'chan' '<-' elementType )=> ( 'chan' '<-' elementType ) | 'chan' elementType )
            int alt120=3;
            int LA120_0 = input.LA(1);

            if ( (LA120_0==82) && (synpred46_GoSource())) {
                alt120=1;
            }
            else if ( (LA120_0==120) ) {
                int LA120_2 = input.LA(2);

                if ( (synpred47_GoSource()) ) {
                    alt120=2;
                }
                else if ( (true) ) {
                    alt120=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 120, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 120, 0, input);

                throw nvae;
            }
            switch (alt120) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1257:3: ( '<-' 'chan' elementType )=> ( '<-' 'chan' elementType )
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1257:32: ( '<-' 'chan' elementType )
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1257:33: '<-' 'chan' elementType
                    {
                    string_literal440=(Token)match(input,82,FOLLOW_82_in_channelType4806); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal440_tree = (CommonTree)adaptor.create(string_literal440);
                    adaptor.addChild(root_0, string_literal440_tree);
                    }
                    string_literal441=(Token)match(input,120,FOLLOW_120_in_channelType4808); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal441_tree = (CommonTree)adaptor.create(string_literal441);
                    adaptor.addChild(root_0, string_literal441_tree);
                    }
                    pushFollow(FOLLOW_elementType_in_channelType4810);
                    elementType442=elementType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, elementType442.getTree());

                    }


                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1258:5: ( 'chan' '<-' elementType )=> ( 'chan' '<-' elementType )
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1258:34: ( 'chan' '<-' elementType )
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1258:35: 'chan' '<-' elementType
                    {
                    string_literal443=(Token)match(input,120,FOLLOW_120_in_channelType4828); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal443_tree = (CommonTree)adaptor.create(string_literal443);
                    adaptor.addChild(root_0, string_literal443_tree);
                    }
                    string_literal444=(Token)match(input,82,FOLLOW_82_in_channelType4830); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal444_tree = (CommonTree)adaptor.create(string_literal444);
                    adaptor.addChild(root_0, string_literal444_tree);
                    }
                    pushFollow(FOLLOW_elementType_in_channelType4832);
                    elementType445=elementType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, elementType445.getTree());

                    }


                    }
                    break;
                case 3 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1259:5: 'chan' elementType
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal446=(Token)match(input,120,FOLLOW_120_in_channelType4839); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal446_tree = (CommonTree)adaptor.create(string_literal446);
                    adaptor.addChild(root_0, string_literal446_tree);
                    }
                    pushFollow(FOLLOW_elementType_in_channelType4841);
                    elementType447=elementType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, elementType447.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "channelType"

    public static class arrayLength_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arrayLength"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1262:1: arrayLength : expression ;
    public final GoSourceParser.arrayLength_return arrayLength() throws RecognitionException {
        GoSourceParser.arrayLength_return retval = new GoSourceParser.arrayLength_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.expression_return expression448 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1263:3: ( expression )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1264:3: expression
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_expression_in_arrayLength4856);
            expression448=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression448.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "arrayLength"

    public static class baseType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "baseType"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1267:1: baseType : type ;
    public final GoSourceParser.baseType_return baseType() throws RecognitionException {
        GoSourceParser.baseType_return retval = new GoSourceParser.baseType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.type_return type449 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1268:3: ( type )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1269:3: type
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_type_in_baseType4871);
            type449=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type449.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "baseType"

    public static class keyType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "keyType"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1272:1: keyType : type ;
    public final GoSourceParser.keyType_return keyType() throws RecognitionException {
        GoSourceParser.keyType_return retval = new GoSourceParser.keyType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.type_return type450 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1273:3: ( type )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1274:3: type
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_type_in_keyType4886);
            type450=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type450.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "keyType"

    public static class elementType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "elementType"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1277:1: elementType : type ;
    public final GoSourceParser.elementType_return elementType() throws RecognitionException {
        GoSourceParser.elementType_return retval = new GoSourceParser.elementType_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        GoSourceParser.type_return type451 = null;



        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1278:3: ( type )
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1279:3: type
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_type_in_elementType4901);
            type451=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type451.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "elementType"

    public static class identifier_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "identifier"
    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1282:1: identifier : ( ( builtinName )=> (t= 'append' | t= 'cap' | t= 'close' | t= 'closed' | t= 'cmplx' | t= 'copy' | t= 'imag' | t= 'len' | t= 'make' | t= 'panic' | t= 'print' | t= 'println' | t= 'real' | t= 'recover' ) | Identifier );
    public final GoSourceParser.identifier_return identifier() throws RecognitionException {
        GoSourceParser.identifier_return retval = new GoSourceParser.identifier_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token t=null;
        Token Identifier452=null;

        CommonTree t_tree=null;
        CommonTree Identifier452_tree=null;

        try {
            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1283:3: ( ( builtinName )=> (t= 'append' | t= 'cap' | t= 'close' | t= 'closed' | t= 'cmplx' | t= 'copy' | t= 'imag' | t= 'len' | t= 'make' | t= 'panic' | t= 'print' | t= 'println' | t= 'real' | t= 'recover' ) | Identifier )
            int alt122=2;
            alt122 = dfa122.predict(input);
            switch (alt122) {
                case 1 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1284:3: ( builtinName )=> (t= 'append' | t= 'cap' | t= 'close' | t= 'closed' | t= 'cmplx' | t= 'copy' | t= 'imag' | t= 'len' | t= 'make' | t= 'panic' | t= 'print' | t= 'println' | t= 'real' | t= 'recover' )
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1285:3: (t= 'append' | t= 'cap' | t= 'close' | t= 'closed' | t= 'cmplx' | t= 'copy' | t= 'imag' | t= 'len' | t= 'make' | t= 'panic' | t= 'print' | t= 'println' | t= 'real' | t= 'recover' )
                    int alt121=14;
                    switch ( input.LA(1) ) {
                    case APPEND:
                        {
                        alt121=1;
                        }
                        break;
                    case CAP:
                        {
                        alt121=2;
                        }
                        break;
                    case CLOSE:
                        {
                        alt121=3;
                        }
                        break;
                    case CLOSED:
                        {
                        alt121=4;
                        }
                        break;
                    case CMPLX:
                        {
                        alt121=5;
                        }
                        break;
                    case COPY:
                        {
                        alt121=6;
                        }
                        break;
                    case IMAG:
                        {
                        alt121=7;
                        }
                        break;
                    case LEN:
                        {
                        alt121=8;
                        }
                        break;
                    case MAKE:
                        {
                        alt121=9;
                        }
                        break;
                    case PANIC:
                        {
                        alt121=10;
                        }
                        break;
                    case PRINT:
                        {
                        alt121=11;
                        }
                        break;
                    case PRINTLN:
                        {
                        alt121=12;
                        }
                        break;
                    case REAL:
                        {
                        alt121=13;
                        }
                        break;
                    case 98:
                        {
                        alt121=14;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 121, 0, input);

                        throw nvae;
                    }

                    switch (alt121) {
                        case 1 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1286:5: t= 'append'
                            {
                            t=(Token)match(input,APPEND,FOLLOW_APPEND_in_identifier4932); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            t_tree = (CommonTree)adaptor.create(t);
                            adaptor.addChild(root_0, t_tree);
                            }

                            }
                            break;
                        case 2 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1287:7: t= 'cap'
                            {
                            t=(Token)match(input,CAP,FOLLOW_CAP_in_identifier4942); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            t_tree = (CommonTree)adaptor.create(t);
                            adaptor.addChild(root_0, t_tree);
                            }

                            }
                            break;
                        case 3 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1288:7: t= 'close'
                            {
                            t=(Token)match(input,CLOSE,FOLLOW_CLOSE_in_identifier4952); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            t_tree = (CommonTree)adaptor.create(t);
                            adaptor.addChild(root_0, t_tree);
                            }

                            }
                            break;
                        case 4 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1289:7: t= 'closed'
                            {
                            t=(Token)match(input,CLOSED,FOLLOW_CLOSED_in_identifier4962); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            t_tree = (CommonTree)adaptor.create(t);
                            adaptor.addChild(root_0, t_tree);
                            }

                            }
                            break;
                        case 5 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1290:7: t= 'cmplx'
                            {
                            t=(Token)match(input,CMPLX,FOLLOW_CMPLX_in_identifier4972); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            t_tree = (CommonTree)adaptor.create(t);
                            adaptor.addChild(root_0, t_tree);
                            }

                            }
                            break;
                        case 6 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1291:7: t= 'copy'
                            {
                            t=(Token)match(input,COPY,FOLLOW_COPY_in_identifier4982); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            t_tree = (CommonTree)adaptor.create(t);
                            adaptor.addChild(root_0, t_tree);
                            }

                            }
                            break;
                        case 7 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1292:7: t= 'imag'
                            {
                            t=(Token)match(input,IMAG,FOLLOW_IMAG_in_identifier4992); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            t_tree = (CommonTree)adaptor.create(t);
                            adaptor.addChild(root_0, t_tree);
                            }

                            }
                            break;
                        case 8 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1293:7: t= 'len'
                            {
                            t=(Token)match(input,LEN,FOLLOW_LEN_in_identifier5002); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            t_tree = (CommonTree)adaptor.create(t);
                            adaptor.addChild(root_0, t_tree);
                            }

                            }
                            break;
                        case 9 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1294:7: t= 'make'
                            {
                            t=(Token)match(input,MAKE,FOLLOW_MAKE_in_identifier5012); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            t_tree = (CommonTree)adaptor.create(t);
                            adaptor.addChild(root_0, t_tree);
                            }

                            }
                            break;
                        case 10 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1295:7: t= 'panic'
                            {
                            t=(Token)match(input,PANIC,FOLLOW_PANIC_in_identifier5022); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            t_tree = (CommonTree)adaptor.create(t);
                            adaptor.addChild(root_0, t_tree);
                            }

                            }
                            break;
                        case 11 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1296:7: t= 'print'
                            {
                            t=(Token)match(input,PRINT,FOLLOW_PRINT_in_identifier5032); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            t_tree = (CommonTree)adaptor.create(t);
                            adaptor.addChild(root_0, t_tree);
                            }

                            }
                            break;
                        case 12 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1297:7: t= 'println'
                            {
                            t=(Token)match(input,PRINTLN,FOLLOW_PRINTLN_in_identifier5042); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            t_tree = (CommonTree)adaptor.create(t);
                            adaptor.addChild(root_0, t_tree);
                            }

                            }
                            break;
                        case 13 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1298:7: t= 'real'
                            {
                            t=(Token)match(input,REAL,FOLLOW_REAL_in_identifier5052); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            t_tree = (CommonTree)adaptor.create(t);
                            adaptor.addChild(root_0, t_tree);
                            }

                            }
                            break;
                        case 14 :
                            // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1299:7: t= 'recover'
                            {
                            t=(Token)match(input,98,FOLLOW_98_in_identifier5062); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            t_tree = (CommonTree)adaptor.create(t);
                            adaptor.addChild(root_0, t_tree);
                            }

                            }
                            break;

                    }

                    if ( state.backtracking==0 ) {

                         t.setType(Identifier);
                        
                    }

                    }
                    break;
                case 2 :
                    // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1305:5: Identifier
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    Identifier452=(Token)match(input,Identifier,FOLLOW_Identifier_in_identifier5079); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    Identifier452_tree = (CommonTree)adaptor.create(Identifier452);
                    adaptor.addChild(root_0, Identifier452_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "identifier"

    // $ANTLR start synpred1_GoSource
    public final void synpred1_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:225:5: ( 'func' '(' )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:225:6: 'func' '('
        {
        match(input,67,FOLLOW_67_in_synpred1_GoSource346); if (state.failed) return ;
        match(input,64,FOLLOW_64_in_synpred1_GoSource349); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_GoSource

    // $ANTLR start synpred2_GoSource
    public final void synpred2_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:268:7: ( type '=' )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:268:8: type '='
        {
        pushFollow(FOLLOW_type_in_synpred2_GoSource570);
        type();

        state._fsp--;
        if (state.failed) return ;
        match(input,66,FOLLOW_66_in_synpred2_GoSource572); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred2_GoSource

    // $ANTLR start synpred3_GoSource
    public final void synpred3_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:378:3: ( parameters result )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:378:4: parameters result
        {
        pushFollow(FOLLOW_parameters_in_synpred3_GoSource786);
        parameters();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_result_in_synpred3_GoSource788);
        result();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred3_GoSource

    // $ANTLR start synpred4_GoSource
    public final void synpred4_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:384:3: ( parameters )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:384:4: parameters
        {
        pushFollow(FOLLOW_parameters_in_synpred4_GoSource817);
        parameters();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred4_GoSource

    // $ANTLR start synpred5_GoSource
    public final void synpred5_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:401:3: ( identifierList ( '...' )? type )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:401:4: identifierList ( '...' )? type
        {
        pushFollow(FOLLOW_identifierList_in_synpred5_GoSource898);
        identifierList();

        state._fsp--;
        if (state.failed) return ;
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:401:19: ( '...' )?
        int alt123=2;
        int LA123_0 = input.LA(1);

        if ( (LA123_0==70) ) {
            alt123=1;
        }
        switch (alt123) {
            case 1 :
                // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:401:19: '...'
                {
                match(input,70,FOLLOW_70_in_synpred5_GoSource900); if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_type_in_synpred5_GoSource903);
        type();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred5_GoSource

    // $ANTLR start synpred6_GoSource
    public final void synpred6_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:442:3: ( labeledStatement )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:442:4: labeledStatement
        {
        pushFollow(FOLLOW_labeledStatement_in_synpred6_GoSource1024);
        labeledStatement();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred6_GoSource

    // $ANTLR start synpred7_GoSource
    public final void synpred7_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:461:3: ( assignment )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:461:4: assignment
        {
        pushFollow(FOLLOW_assignment_in_synpred7_GoSource1129);
        assignment();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred7_GoSource

    // $ANTLR start synpred8_GoSource
    public final void synpred8_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:462:5: ( shortVarDecl )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:462:6: shortVarDecl
        {
        pushFollow(FOLLOW_shortVarDecl_in_synpred8_GoSource1141);
        shortVarDecl();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred8_GoSource

    // $ANTLR start synpred9_GoSource
    public final void synpred9_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:463:5: ( incDecStmt )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:463:6: incDecStmt
        {
        pushFollow(FOLLOW_incDecStmt_in_synpred9_GoSource1153);
        incDecStmt();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred9_GoSource

    // $ANTLR start synpred10_GoSource
    public final void synpred10_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:464:5: ( expressionStmt )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:464:6: expressionStmt
        {
        pushFollow(FOLLOW_expressionStmt_in_synpred10_GoSource1165);
        expressionStmt();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred10_GoSource

    // $ANTLR start synpred11_GoSource
    public final void synpred11_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:473:5: ( simpleStmt SEMI )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:473:6: simpleStmt SEMI
        {
        pushFollow(FOLLOW_simpleStmt_in_synpred11_GoSource1204);
        simpleStmt();

        state._fsp--;
        if (state.failed) return ;
        match(input,SEMI,FOLLOW_SEMI_in_synpred11_GoSource1206); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred11_GoSource

    // $ANTLR start synpred12_GoSource
    public final void synpred12_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:474:7: ( expression block )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:474:8: expression block
        {
        pushFollow(FOLLOW_expression_in_synpred12_GoSource1226);
        expression();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_block_in_synpred12_GoSource1228);
        block();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred12_GoSource

    // $ANTLR start synpred13_GoSource
    public final void synpred13_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:482:3: ( typeSwitchStmt )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:482:4: typeSwitchStmt
        {
        pushFollow(FOLLOW_typeSwitchStmt_in_synpred13_GoSource1272);
        typeSwitchStmt();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred13_GoSource

    // $ANTLR start synpred14_GoSource
    public final void synpred14_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:483:5: ( exprSwitchStmt )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:483:6: exprSwitchStmt
        {
        pushFollow(FOLLOW_exprSwitchStmt_in_synpred14_GoSource1284);
        exprSwitchStmt();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred14_GoSource

    // $ANTLR start synpred15_GoSource
    public final void synpred15_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:490:5: ( simpleStmt SEMI expression )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:490:6: simpleStmt SEMI expression
        {
        pushFollow(FOLLOW_simpleStmt_in_synpred15_GoSource1316);
        simpleStmt();

        state._fsp--;
        if (state.failed) return ;
        match(input,SEMI,FOLLOW_SEMI_in_synpred15_GoSource1318); if (state.failed) return ;
        pushFollow(FOLLOW_expression_in_synpred15_GoSource1320);
        expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred15_GoSource

    // $ANTLR start synpred16_GoSource
    public final void synpred16_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:491:7: ( simpleStmt SEMI )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:491:8: simpleStmt SEMI
        {
        pushFollow(FOLLOW_simpleStmt_in_synpred16_GoSource1338);
        simpleStmt();

        state._fsp--;
        if (state.failed) return ;
        match(input,SEMI,FOLLOW_SEMI_in_synpred16_GoSource1340); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred16_GoSource

    // $ANTLR start synpred17_GoSource
    public final void synpred17_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:512:5: ( simpleStmt SEMI )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:512:6: simpleStmt SEMI
        {
        pushFollow(FOLLOW_simpleStmt_in_synpred17_GoSource1444);
        simpleStmt();

        state._fsp--;
        if (state.failed) return ;
        match(input,SEMI,FOLLOW_SEMI_in_synpred17_GoSource1446); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred17_GoSource

    // $ANTLR start synpred18_GoSource
    public final void synpred18_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:598:5: ( sendExpr )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:598:6: sendExpr
        {
        pushFollow(FOLLOW_sendExpr_in_synpred18_GoSource1821);
        sendExpr();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred18_GoSource

    // $ANTLR start synpred19_GoSource
    public final void synpred19_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:599:7: ( recvExpr )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:599:8: recvExpr
        {
        pushFollow(FOLLOW_recvExpr_in_synpred19_GoSource1835);
        recvExpr();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred19_GoSource

    // $ANTLR start synpred20_GoSource
    public final void synpred20_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:611:3: ( expression ( '=' | ':=' ) '<-' )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:612:5: expression ( '=' | ':=' ) '<-'
        {
        pushFollow(FOLLOW_expression_in_synpred20_GoSource1890);
        expression();

        state._fsp--;
        if (state.failed) return ;
        if ( input.LA(1)==66||input.LA(1)==77 ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        match(input,82,FOLLOW_82_in_synpred20_GoSource1926); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred20_GoSource

    // $ANTLR start synpred21_GoSource
    public final void synpred21_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:632:5: ( initStmt SEMI )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:632:6: initStmt SEMI
        {
        pushFollow(FOLLOW_initStmt_in_synpred21_GoSource2001);
        initStmt();

        state._fsp--;
        if (state.failed) return ;
        match(input,SEMI,FOLLOW_SEMI_in_synpred21_GoSource2003); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred21_GoSource

    // $ANTLR start synpred22_GoSource
    public final void synpred22_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:633:7: ( expression '{' )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:633:8: expression '{'
        {
        pushFollow(FOLLOW_expression_in_synpred22_GoSource2017);
        expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,71,FOLLOW_71_in_synpred22_GoSource2019); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred22_GoSource

    // $ANTLR start synpred23_GoSource
    public final void synpred23_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:647:5: ( initStmt SEMI )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:647:6: initStmt SEMI
        {
        pushFollow(FOLLOW_initStmt_in_synpred23_GoSource2078);
        initStmt();

        state._fsp--;
        if (state.failed) return ;
        match(input,SEMI,FOLLOW_SEMI_in_synpred23_GoSource2080); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred23_GoSource

    // $ANTLR start synpred24_GoSource
    public final void synpred24_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:651:5: ( condition SEMI )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:651:6: condition SEMI
        {
        pushFollow(FOLLOW_condition_in_synpred24_GoSource2110);
        condition();

        state._fsp--;
        if (state.failed) return ;
        match(input,SEMI,FOLLOW_SEMI_in_synpred24_GoSource2112); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred24_GoSource

    // $ANTLR start synpred26_GoSource
    public final void synpred26_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:741:9: ( type '=' )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:741:10: type '='
        {
        pushFollow(FOLLOW_type_in_synpred26_GoSource2523);
        type();

        state._fsp--;
        if (state.failed) return ;
        match(input,66,FOLLOW_66_in_synpred26_GoSource2525); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred26_GoSource

    // $ANTLR start synpred27_GoSource
    public final void synpred27_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:795:3: ( builtinCall )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:795:4: builtinCall
        {
        pushFollow(FOLLOW_builtinCall_in_synpred27_GoSource2707);
        builtinCall();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred27_GoSource

    // $ANTLR start synpred28_GoSource
    public final void synpred28_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:796:5: ( operand )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:796:6: operand
        {
        pushFollow(FOLLOW_operand_in_synpred28_GoSource2719);
        operand();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred28_GoSource

    // $ANTLR start synpred29_GoSource
    public final void synpred29_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:797:5: ( conversion )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:797:6: conversion
        {
        pushFollow(FOLLOW_conversion_in_synpred29_GoSource2731);
        conversion();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred29_GoSource

    // $ANTLR start synpred30_GoSource
    public final void synpred30_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:809:7: ( call )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:809:8: call
        {
        pushFollow(FOLLOW_call_in_synpred30_GoSource2778);
        call();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred30_GoSource

    // $ANTLR start synpred31_GoSource
    public final void synpred31_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:810:9: ( selector )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:810:10: selector
        {
        pushFollow(FOLLOW_selector_in_synpred31_GoSource2794);
        selector();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred31_GoSource

    // $ANTLR start synpred32_GoSource
    public final void synpred32_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:811:9: ( index )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:811:10: index
        {
        pushFollow(FOLLOW_index_in_synpred32_GoSource2810);
        index();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred32_GoSource

    // $ANTLR start synpred33_GoSource
    public final void synpred33_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:812:9: ( typeAssertion )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:812:10: typeAssertion
        {
        pushFollow(FOLLOW_typeAssertion_in_synpred33_GoSource2826);
        typeAssertion();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred33_GoSource

    // $ANTLR start synpred34_GoSource
    public final void synpred34_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:813:9: ( slice )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:813:10: slice
        {
        pushFollow(FOLLOW_slice_in_synpred34_GoSource2842);
        slice();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred34_GoSource

    // $ANTLR start synpred36_GoSource
    public final void synpred36_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:853:5: ( type ',' expressionList )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:853:6: type ',' expressionList
        {
        pushFollow(FOLLOW_type_in_synpred36_GoSource3033);
        type();

        state._fsp--;
        if (state.failed) return ;
        match(input,69,FOLLOW_69_in_synpred36_GoSource3035); if (state.failed) return ;
        pushFollow(FOLLOW_expressionList_in_synpred36_GoSource3037);
        expressionList();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred36_GoSource

    // $ANTLR start synpred37_GoSource
    public final void synpred37_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:854:7: ( expressionList )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:854:8: expressionList
        {
        pushFollow(FOLLOW_expressionList_in_synpred37_GoSource3055);
        expressionList();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred37_GoSource

    // $ANTLR start synpred38_GoSource
    public final void synpred38_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:903:3: ( '<-' primaryExpr )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:903:4: '<-' primaryExpr
        {
        match(input,82,FOLLOW_82_in_synpred38_GoSource3268); if (state.failed) return ;
        pushFollow(FOLLOW_primaryExpr_in_synpred38_GoSource3270);
        primaryExpr();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred38_GoSource

    // $ANTLR start synpred39_GoSource
    public final void synpred39_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:904:5: ( '*' primaryExpr )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:904:6: '*' primaryExpr
        {
        match(input,68,FOLLOW_68_in_synpred39_GoSource3285); if (state.failed) return ;
        pushFollow(FOLLOW_primaryExpr_in_synpred39_GoSource3287);
        primaryExpr();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred39_GoSource

    // $ANTLR start synpred40_GoSource
    public final void synpred40_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1016:3: ( literal )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1016:4: literal
        {
        pushFollow(FOLLOW_literal_in_synpred40_GoSource3822);
        literal();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred40_GoSource

    // $ANTLR start synpred41_GoSource
    public final void synpred41_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1017:5: ( methodExpr )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1017:6: methodExpr
        {
        pushFollow(FOLLOW_methodExpr_in_synpred41_GoSource3834);
        methodExpr();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred41_GoSource

    // $ANTLR start synpred42_GoSource
    public final void synpred42_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1042:5: ( compositeLit ~ ( 'else' ) )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1042:6: compositeLit ~ ( 'else' )
        {
        pushFollow(FOLLOW_compositeLit_in_synpred42_GoSource3945);
        compositeLit();

        state._fsp--;
        if (state.failed) return ;
        if ( (input.LA(1)>=SEMI && input.LA(1)<=71)||(input.LA(1)>=73 && input.LA(1)<=120) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred42_GoSource

    // $ANTLR start synpred43_GoSource
    public final void synpred43_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1093:3: ( key ':' )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1093:4: key ':'
        {
        pushFollow(FOLLOW_key_in_synpred43_GoSource4172);
        key();

        state._fsp--;
        if (state.failed) return ;
        match(input,74,FOLLOW_74_in_synpred43_GoSource4174); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred43_GoSource

    // $ANTLR start synpred44_GoSource
    public final void synpred44_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1099:3: ( fieldName )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1099:4: fieldName
        {
        pushFollow(FOLLOW_fieldName_in_synpred44_GoSource4206);
        fieldName();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred44_GoSource

    // $ANTLR start synpred45_GoSource
    public final void synpred45_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1126:3: ( identifier '.' identifier )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1126:4: identifier '.' identifier
        {
        pushFollow(FOLLOW_identifier_in_synpred45_GoSource4299);
        identifier();

        state._fsp--;
        if (state.failed) return ;
        match(input,65,FOLLOW_65_in_synpred45_GoSource4301); if (state.failed) return ;
        pushFollow(FOLLOW_identifier_in_synpred45_GoSource4303);
        identifier();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred45_GoSource

    // $ANTLR start synpred46_GoSource
    public final void synpred46_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1257:3: ( '<-' 'chan' elementType )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1257:4: '<-' 'chan' elementType
        {
        match(input,82,FOLLOW_82_in_synpred46_GoSource4796); if (state.failed) return ;
        match(input,120,FOLLOW_120_in_synpred46_GoSource4798); if (state.failed) return ;
        pushFollow(FOLLOW_elementType_in_synpred46_GoSource4800);
        elementType();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred46_GoSource

    // $ANTLR start synpred47_GoSource
    public final void synpred47_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1258:5: ( 'chan' '<-' elementType )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1258:6: 'chan' '<-' elementType
        {
        match(input,120,FOLLOW_120_in_synpred47_GoSource4818); if (state.failed) return ;
        match(input,82,FOLLOW_82_in_synpred47_GoSource4820); if (state.failed) return ;
        pushFollow(FOLLOW_elementType_in_synpred47_GoSource4822);
        elementType();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred47_GoSource

    // $ANTLR start synpred48_GoSource
    public final void synpred48_GoSource_fragment() throws RecognitionException {   
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1284:3: ( builtinName )
        // /home/steel/workspace/goclipse/experimental/src/antlr/GoSource.g:1284:4: builtinName
        {
        pushFollow(FOLLOW_builtinName_in_synpred48_GoSource4917);
        builtinName();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred48_GoSource

    // Delegated rules

    public final boolean synpred32_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred32_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred33_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred33_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred45_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred45_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred26_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred26_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred38_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred38_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred22_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred22_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred9_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred9_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred12_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred12_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred20_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred20_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred48_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred48_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred21_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred21_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred43_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred43_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred13_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred13_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred46_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred46_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred42_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred42_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred8_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred8_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred31_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred31_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred47_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred47_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred29_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred29_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred18_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred18_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred19_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred19_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred41_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred41_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred36_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred36_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred7_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred7_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred30_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred30_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred39_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred39_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred40_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred40_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred24_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred24_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred11_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred11_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred17_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred17_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred23_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred23_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred10_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred10_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred6_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred6_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred37_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred37_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred27_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred27_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred44_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred44_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred15_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred15_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred34_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred34_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred28_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred28_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred14_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred14_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred16_GoSource() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred16_GoSource_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA15 dfa15 = new DFA15(this);
    protected DFA18 dfa18 = new DFA18(this);
    protected DFA23 dfa23 = new DFA23(this);
    protected DFA30 dfa30 = new DFA30(this);
    protected DFA32 dfa32 = new DFA32(this);
    protected DFA33 dfa33 = new DFA33(this);
    protected DFA34 dfa34 = new DFA34(this);
    protected DFA37 dfa37 = new DFA37(this);
    protected DFA41 dfa41 = new DFA41(this);
    protected DFA43 dfa43 = new DFA43(this);
    protected DFA53 dfa53 = new DFA53(this);
    protected DFA55 dfa55 = new DFA55(this);
    protected DFA56 dfa56 = new DFA56(this);
    protected DFA57 dfa57 = new DFA57(this);
    protected DFA58 dfa58 = new DFA58(this);
    protected DFA64 dfa64 = new DFA64(this);
    protected DFA68 dfa68 = new DFA68(this);
    protected DFA74 dfa74 = new DFA74(this);
    protected DFA84 dfa84 = new DFA84(this);
    protected DFA96 dfa96 = new DFA96(this);
    protected DFA99 dfa99 = new DFA99(this);
    protected DFA105 dfa105 = new DFA105(this);
    protected DFA106 dfa106 = new DFA106(this);
    protected DFA108 dfa108 = new DFA108(this);
    protected DFA111 dfa111 = new DFA111(this);
    protected DFA114 dfa114 = new DFA114(this);
    protected DFA117 dfa117 = new DFA117(this);
    protected DFA122 dfa122 = new DFA122(this);
    static final String DFA15_eotS =
        "\33\uffff";
    static final String DFA15_eofS =
        "\33\uffff";
    static final String DFA15_minS =
        "\1\20\30\0\2\uffff";
    static final String DFA15_maxS =
        "\1\170\30\0\2\uffff";
    static final String DFA15_acceptS =
        "\31\uffff\1\1\1\2";
    static final String DFA15_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\2\uffff}>";
    static final String[] DFA15_transitionS = {
            "\1\30\1\12\1\13\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24"+
            "\1\25\1\26\2\uffff\1\3\1\5\36\uffff\1\1\2\uffff\1\4\1\7\15\uffff"+
            "\1\10\17\uffff\1\27\1\2\23\uffff\1\6\1\11",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA15_eot = DFA.unpackEncodedString(DFA15_eotS);
    static final short[] DFA15_eof = DFA.unpackEncodedString(DFA15_eofS);
    static final char[] DFA15_min = DFA.unpackEncodedStringToUnsignedChars(DFA15_minS);
    static final char[] DFA15_max = DFA.unpackEncodedStringToUnsignedChars(DFA15_maxS);
    static final short[] DFA15_accept = DFA.unpackEncodedString(DFA15_acceptS);
    static final short[] DFA15_special = DFA.unpackEncodedString(DFA15_specialS);
    static final short[][] DFA15_transition;

    static {
        int numStates = DFA15_transitionS.length;
        DFA15_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA15_transition[i] = DFA.unpackEncodedString(DFA15_transitionS[i]);
        }
    }

    class DFA15 extends DFA {

        public DFA15(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 15;
            this.eot = DFA15_eot;
            this.eof = DFA15_eof;
            this.min = DFA15_min;
            this.max = DFA15_max;
            this.accept = DFA15_accept;
            this.special = DFA15_special;
            this.transition = DFA15_transition;
        }
        public String getDescription() {
            return "267:5: ( ( type '=' )=>a= type '=' expressionList | b= type )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA15_1 = input.LA(1);

                         
                        int index15_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA15_2 = input.LA(1);

                         
                        int index15_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA15_3 = input.LA(1);

                         
                        int index15_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA15_4 = input.LA(1);

                         
                        int index15_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA15_5 = input.LA(1);

                         
                        int index15_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA15_6 = input.LA(1);

                         
                        int index15_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA15_7 = input.LA(1);

                         
                        int index15_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA15_8 = input.LA(1);

                         
                        int index15_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA15_9 = input.LA(1);

                         
                        int index15_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA15_10 = input.LA(1);

                         
                        int index15_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA15_11 = input.LA(1);

                         
                        int index15_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA15_12 = input.LA(1);

                         
                        int index15_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA15_13 = input.LA(1);

                         
                        int index15_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA15_14 = input.LA(1);

                         
                        int index15_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_14);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA15_15 = input.LA(1);

                         
                        int index15_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_15);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA15_16 = input.LA(1);

                         
                        int index15_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_16);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA15_17 = input.LA(1);

                         
                        int index15_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_17);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA15_18 = input.LA(1);

                         
                        int index15_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_18);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA15_19 = input.LA(1);

                         
                        int index15_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_19);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA15_20 = input.LA(1);

                         
                        int index15_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_20);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA15_21 = input.LA(1);

                         
                        int index15_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_21);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA15_22 = input.LA(1);

                         
                        int index15_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_22);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA15_23 = input.LA(1);

                         
                        int index15_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_23);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA15_24 = input.LA(1);

                         
                        int index15_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index15_24);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 15, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA18_eotS =
        "\22\uffff";
    static final String DFA18_eofS =
        "\22\uffff";
    static final String DFA18_minS =
        "\1\20\17\4\2\uffff";
    static final String DFA18_maxS =
        "\20\142\2\uffff";
    static final String DFA18_acceptS =
        "\20\uffff\1\2\1\1";
    static final String DFA18_specialS =
        "\22\uffff}>";
    static final String[] DFA18_transitionS = {
            "\1\17\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
            "\15\46\uffff\1\20\35\uffff\1\16",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "\1\20\13\uffff\16\21\12\uffff\1\20\33\uffff\1\21\35\uffff\1"+
            "\21",
            "",
            ""
    };

    static final short[] DFA18_eot = DFA.unpackEncodedString(DFA18_eotS);
    static final short[] DFA18_eof = DFA.unpackEncodedString(DFA18_eofS);
    static final char[] DFA18_min = DFA.unpackEncodedStringToUnsignedChars(DFA18_minS);
    static final char[] DFA18_max = DFA.unpackEncodedStringToUnsignedChars(DFA18_maxS);
    static final short[] DFA18_accept = DFA.unpackEncodedString(DFA18_acceptS);
    static final short[] DFA18_special = DFA.unpackEncodedString(DFA18_specialS);
    static final short[][] DFA18_transition;

    static {
        int numStates = DFA18_transitionS.length;
        DFA18_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA18_transition[i] = DFA.unpackEncodedString(DFA18_transitionS[i]);
        }
    }

    class DFA18 extends DFA {

        public DFA18(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 18;
            this.eot = DFA18_eot;
            this.eof = DFA18_eof;
            this.min = DFA18_min;
            this.max = DFA18_max;
            this.accept = DFA18_accept;
            this.special = DFA18_special;
            this.transition = DFA18_transition;
        }
        public String getDescription() {
            return "334:7: (a= identifier )?";
        }
    }
    static final String DFA23_eotS =
        "\32\uffff";
    static final String DFA23_eofS =
        "\32\uffff";
    static final String DFA23_minS =
        "\1\20\1\0\30\uffff";
    static final String DFA23_maxS =
        "\1\170\1\0\30\uffff";
    static final String DFA23_acceptS =
        "\2\uffff\1\2\26\uffff\1\1";
    static final String DFA23_specialS =
        "\1\uffff\1\0\30\uffff}>";
    static final String[] DFA23_transitionS = {
            "\16\2\2\uffff\2\2\36\uffff\1\1\2\uffff\2\2\15\uffff\1\2\17\uffff"+
            "\2\2\23\uffff\2\2",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA23_eot = DFA.unpackEncodedString(DFA23_eotS);
    static final short[] DFA23_eof = DFA.unpackEncodedString(DFA23_eofS);
    static final char[] DFA23_min = DFA.unpackEncodedStringToUnsignedChars(DFA23_minS);
    static final char[] DFA23_max = DFA.unpackEncodedStringToUnsignedChars(DFA23_maxS);
    static final short[] DFA23_accept = DFA.unpackEncodedString(DFA23_acceptS);
    static final short[] DFA23_special = DFA.unpackEncodedString(DFA23_specialS);
    static final short[][] DFA23_transition;

    static {
        int numStates = DFA23_transitionS.length;
        DFA23_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA23_transition[i] = DFA.unpackEncodedString(DFA23_transitionS[i]);
        }
    }

    class DFA23 extends DFA {

        public DFA23(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 23;
            this.eot = DFA23_eot;
            this.eof = DFA23_eof;
            this.min = DFA23_min;
            this.max = DFA23_max;
            this.accept = DFA23_accept;
            this.special = DFA23_special;
            this.transition = DFA23_transition;
        }
        public String getDescription() {
            return "382:1: result : ( ( parameters )=> parameters | type );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA23_1 = input.LA(1);

                         
                        int index23_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_GoSource()) ) {s = 25;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index23_1);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 23, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA30_eotS =
        "\72\uffff";
    static final String DFA30_eofS =
        "\72\uffff";
    static final String DFA30_minS =
        "\1\20\17\4\1\uffff\1\4\31\uffff\17\0";
    static final String DFA30_maxS =
        "\20\170\1\uffff\1\170\31\uffff\17\0";
    static final String DFA30_acceptS =
        "\20\uffff\1\2\1\uffff\31\1\17\uffff";
    static final String DFA30_specialS =
        "\1\uffff\1\5\1\13\1\10\1\15\1\14\1\16\1\20\1\23\1\21\1\25\1\27\1"+
        "\32\1\30\1\34\1\33\33\uffff\1\22\1\17\1\7\1\6\1\12\1\11\1\2\1\0"+
        "\1\4\1\3\1\31\1\35\1\24\1\26\1\1}>";
    static final String[] DFA30_transitionS = {
            "\1\17\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
            "\15\2\uffff\2\20\36\uffff\1\20\2\uffff\2\20\1\uffff\1\20\13"+
            "\uffff\1\20\17\uffff\1\16\1\20\23\uffff\2\20",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "\1\20\13\uffff\1\52\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
            "\1\44\1\45\1\46\1\47\1\50\2\uffff\1\25\1\27\6\uffff\1\20\27"+
            "\uffff\1\23\1\20\1\uffff\1\26\1\31\1\21\1\22\13\uffff\1\32\17"+
            "\uffff\1\51\1\24\23\uffff\1\30\1\33",
            "",
            "\1\20\13\uffff\1\71\1\53\1\54\1\55\1\56\1\57\1\60\1\61\1\62"+
            "\1\63\1\64\1\65\1\66\1\67\2\uffff\2\20\6\uffff\1\20\27\uffff"+
            "\1\20\2\uffff\2\20\1\uffff\1\20\13\uffff\1\20\17\uffff\1\70"+
            "\1\20\23\uffff\2\20",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff"
    };

    static final short[] DFA30_eot = DFA.unpackEncodedString(DFA30_eotS);
    static final short[] DFA30_eof = DFA.unpackEncodedString(DFA30_eofS);
    static final char[] DFA30_min = DFA.unpackEncodedStringToUnsignedChars(DFA30_minS);
    static final char[] DFA30_max = DFA.unpackEncodedStringToUnsignedChars(DFA30_maxS);
    static final short[] DFA30_accept = DFA.unpackEncodedString(DFA30_acceptS);
    static final short[] DFA30_special = DFA.unpackEncodedString(DFA30_specialS);
    static final short[][] DFA30_transition;

    static {
        int numStates = DFA30_transitionS.length;
        DFA30_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA30_transition[i] = DFA.unpackEncodedString(DFA30_transitionS[i]);
        }
    }

    class DFA30 extends DFA {

        public DFA30(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 30;
            this.eot = DFA30_eot;
            this.eof = DFA30_eof;
            this.min = DFA30_min;
            this.max = DFA30_max;
            this.accept = DFA30_accept;
            this.special = DFA30_special;
            this.transition = DFA30_transition;
        }
        public String getDescription() {
            return "399:1: parameterDecl : ( ( identifierList ( '...' )? type )=>ilist= identifierList ( '...' )? type | ( '...' )? type );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA30_50 = input.LA(1);

                         
                        int index30_50 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_50);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA30_57 = input.LA(1);

                         
                        int index30_57 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_57);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA30_49 = input.LA(1);

                         
                        int index30_49 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_49);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA30_52 = input.LA(1);

                         
                        int index30_52 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_52);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA30_51 = input.LA(1);

                         
                        int index30_51 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_51);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA30_1 = input.LA(1);

                         
                        int index30_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_1==69) ) {s = 17;}

                        else if ( (LA30_1==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_1==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_1==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_1==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_1==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_1==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_1==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_1==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_1==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_1==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_1==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_1==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_1==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_1==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_1==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_1==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_1==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_1==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_1==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_1==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_1==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_1==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_1==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_1==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_1==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_1==SEMI||LA30_1==CLOSE_BRACKET||LA30_1==65) ) {s = 16;}

                         
                        input.seek(index30_1);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA30_46 = input.LA(1);

                         
                        int index30_46 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_46);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA30_45 = input.LA(1);

                         
                        int index30_45 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_45);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA30_3 = input.LA(1);

                         
                        int index30_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_3==69) ) {s = 17;}

                        else if ( (LA30_3==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_3==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_3==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_3==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_3==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_3==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_3==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_3==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_3==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_3==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_3==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_3==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_3==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_3==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_3==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_3==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_3==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_3==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_3==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_3==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_3==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_3==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_3==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_3==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_3==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_3==SEMI||LA30_3==CLOSE_BRACKET||LA30_3==65) ) {s = 16;}

                         
                        input.seek(index30_3);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA30_48 = input.LA(1);

                         
                        int index30_48 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_48);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA30_47 = input.LA(1);

                         
                        int index30_47 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_47);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA30_2 = input.LA(1);

                         
                        int index30_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_2==69) ) {s = 17;}

                        else if ( (LA30_2==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_2==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_2==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_2==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_2==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_2==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_2==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_2==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_2==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_2==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_2==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_2==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_2==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_2==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_2==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_2==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_2==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_2==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_2==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_2==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_2==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_2==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_2==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_2==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_2==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_2==SEMI||LA30_2==CLOSE_BRACKET||LA30_2==65) ) {s = 16;}

                         
                        input.seek(index30_2);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA30_5 = input.LA(1);

                         
                        int index30_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_5==69) ) {s = 17;}

                        else if ( (LA30_5==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_5==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_5==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_5==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_5==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_5==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_5==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_5==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_5==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_5==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_5==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_5==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_5==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_5==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_5==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_5==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_5==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_5==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_5==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_5==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_5==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_5==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_5==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_5==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_5==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_5==SEMI||LA30_5==CLOSE_BRACKET||LA30_5==65) ) {s = 16;}

                         
                        input.seek(index30_5);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA30_4 = input.LA(1);

                         
                        int index30_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_4==69) ) {s = 17;}

                        else if ( (LA30_4==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_4==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_4==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_4==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_4==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_4==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_4==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_4==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_4==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_4==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_4==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_4==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_4==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_4==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_4==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_4==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_4==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_4==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_4==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_4==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_4==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_4==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_4==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_4==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_4==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_4==SEMI||LA30_4==CLOSE_BRACKET||LA30_4==65) ) {s = 16;}

                         
                        input.seek(index30_4);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA30_6 = input.LA(1);

                         
                        int index30_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_6==69) ) {s = 17;}

                        else if ( (LA30_6==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_6==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_6==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_6==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_6==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_6==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_6==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_6==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_6==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_6==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_6==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_6==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_6==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_6==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_6==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_6==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_6==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_6==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_6==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_6==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_6==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_6==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_6==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_6==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_6==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_6==SEMI||LA30_6==CLOSE_BRACKET||LA30_6==65) ) {s = 16;}

                         
                        input.seek(index30_6);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA30_44 = input.LA(1);

                         
                        int index30_44 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_44);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA30_7 = input.LA(1);

                         
                        int index30_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_7==69) ) {s = 17;}

                        else if ( (LA30_7==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_7==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_7==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_7==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_7==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_7==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_7==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_7==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_7==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_7==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_7==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_7==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_7==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_7==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_7==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_7==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_7==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_7==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_7==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_7==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_7==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_7==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_7==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_7==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_7==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_7==SEMI||LA30_7==CLOSE_BRACKET||LA30_7==65) ) {s = 16;}

                         
                        input.seek(index30_7);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA30_9 = input.LA(1);

                         
                        int index30_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_9==69) ) {s = 17;}

                        else if ( (LA30_9==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_9==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_9==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_9==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_9==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_9==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_9==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_9==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_9==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_9==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_9==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_9==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_9==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_9==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_9==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_9==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_9==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_9==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_9==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_9==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_9==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_9==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_9==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_9==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_9==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_9==SEMI||LA30_9==CLOSE_BRACKET||LA30_9==65) ) {s = 16;}

                         
                        input.seek(index30_9);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA30_43 = input.LA(1);

                         
                        int index30_43 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_43);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA30_8 = input.LA(1);

                         
                        int index30_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_8==69) ) {s = 17;}

                        else if ( (LA30_8==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_8==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_8==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_8==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_8==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_8==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_8==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_8==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_8==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_8==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_8==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_8==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_8==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_8==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_8==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_8==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_8==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_8==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_8==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_8==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_8==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_8==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_8==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_8==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_8==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_8==SEMI||LA30_8==CLOSE_BRACKET||LA30_8==65) ) {s = 16;}

                         
                        input.seek(index30_8);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA30_55 = input.LA(1);

                         
                        int index30_55 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_55);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA30_10 = input.LA(1);

                         
                        int index30_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_10==69) ) {s = 17;}

                        else if ( (LA30_10==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_10==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_10==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_10==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_10==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_10==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_10==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_10==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_10==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_10==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_10==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_10==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_10==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_10==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_10==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_10==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_10==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_10==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_10==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_10==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_10==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_10==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_10==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_10==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_10==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_10==SEMI||LA30_10==CLOSE_BRACKET||LA30_10==65) ) {s = 16;}

                         
                        input.seek(index30_10);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA30_56 = input.LA(1);

                         
                        int index30_56 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_56);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA30_11 = input.LA(1);

                         
                        int index30_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_11==69) ) {s = 17;}

                        else if ( (LA30_11==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_11==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_11==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_11==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_11==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_11==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_11==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_11==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_11==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_11==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_11==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_11==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_11==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_11==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_11==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_11==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_11==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_11==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_11==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_11==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_11==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_11==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_11==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_11==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_11==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_11==SEMI||LA30_11==CLOSE_BRACKET||LA30_11==65) ) {s = 16;}

                         
                        input.seek(index30_11);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA30_13 = input.LA(1);

                         
                        int index30_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_13==69) ) {s = 17;}

                        else if ( (LA30_13==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_13==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_13==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_13==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_13==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_13==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_13==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_13==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_13==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_13==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_13==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_13==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_13==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_13==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_13==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_13==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_13==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_13==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_13==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_13==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_13==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_13==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_13==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_13==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_13==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_13==SEMI||LA30_13==CLOSE_BRACKET||LA30_13==65) ) {s = 16;}

                         
                        input.seek(index30_13);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA30_53 = input.LA(1);

                         
                        int index30_53 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_53);
                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA30_12 = input.LA(1);

                         
                        int index30_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_12==69) ) {s = 17;}

                        else if ( (LA30_12==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_12==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_12==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_12==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_12==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_12==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_12==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_12==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_12==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_12==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_12==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_12==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_12==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_12==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_12==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_12==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_12==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_12==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_12==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_12==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_12==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_12==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_12==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_12==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_12==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_12==SEMI||LA30_12==CLOSE_BRACKET||LA30_12==65) ) {s = 16;}

                         
                        input.seek(index30_12);
                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA30_15 = input.LA(1);

                         
                        int index30_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_15==69) ) {s = 17;}

                        else if ( (LA30_15==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_15==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_15==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_15==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_15==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_15==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_15==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_15==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_15==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_15==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_15==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_15==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_15==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_15==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_15==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_15==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_15==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_15==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_15==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_15==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_15==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_15==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_15==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_15==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_15==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_15==SEMI||LA30_15==CLOSE_BRACKET||LA30_15==65) ) {s = 16;}

                         
                        input.seek(index30_15);
                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA30_14 = input.LA(1);

                         
                        int index30_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA30_14==69) ) {s = 17;}

                        else if ( (LA30_14==70) && (synpred5_GoSource())) {s = 18;}

                        else if ( (LA30_14==64) && (synpred5_GoSource())) {s = 19;}

                        else if ( (LA30_14==99) && (synpred5_GoSource())) {s = 20;}

                        else if ( (LA30_14==STRUCT) && (synpred5_GoSource())) {s = 21;}

                        else if ( (LA30_14==67) && (synpred5_GoSource())) {s = 22;}

                        else if ( (LA30_14==INTERFACE) && (synpred5_GoSource())) {s = 23;}

                        else if ( (LA30_14==119) && (synpred5_GoSource())) {s = 24;}

                        else if ( (LA30_14==68) && (synpred5_GoSource())) {s = 25;}

                        else if ( (LA30_14==82) && (synpred5_GoSource())) {s = 26;}

                        else if ( (LA30_14==120) && (synpred5_GoSource())) {s = 27;}

                        else if ( (LA30_14==APPEND) && (synpred5_GoSource())) {s = 28;}

                        else if ( (LA30_14==CAP) && (synpred5_GoSource())) {s = 29;}

                        else if ( (LA30_14==CLOSE) && (synpred5_GoSource())) {s = 30;}

                        else if ( (LA30_14==CLOSED) && (synpred5_GoSource())) {s = 31;}

                        else if ( (LA30_14==CMPLX) && (synpred5_GoSource())) {s = 32;}

                        else if ( (LA30_14==COPY) && (synpred5_GoSource())) {s = 33;}

                        else if ( (LA30_14==IMAG) && (synpred5_GoSource())) {s = 34;}

                        else if ( (LA30_14==LEN) && (synpred5_GoSource())) {s = 35;}

                        else if ( (LA30_14==MAKE) && (synpred5_GoSource())) {s = 36;}

                        else if ( (LA30_14==PANIC) && (synpred5_GoSource())) {s = 37;}

                        else if ( (LA30_14==PRINT) && (synpred5_GoSource())) {s = 38;}

                        else if ( (LA30_14==PRINTLN) && (synpred5_GoSource())) {s = 39;}

                        else if ( (LA30_14==REAL) && (synpred5_GoSource())) {s = 40;}

                        else if ( (LA30_14==98) && (synpred5_GoSource())) {s = 41;}

                        else if ( (LA30_14==Identifier) && (synpred5_GoSource())) {s = 42;}

                        else if ( (LA30_14==SEMI||LA30_14==CLOSE_BRACKET||LA30_14==65) ) {s = 16;}

                         
                        input.seek(index30_14);
                        if ( s>=0 ) return s;
                        break;
                    case 29 : 
                        int LA30_54 = input.LA(1);

                         
                        int index30_54 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_GoSource()) ) {s = 42;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index30_54);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 30, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA32_eotS =
        "\37\uffff";
    static final String DFA32_eofS =
        "\20\35\17\uffff";
    static final String DFA32_minS =
        "\20\4\17\uffff";
    static final String DFA32_maxS =
        "\1\170\17\166\17\uffff";
    static final String DFA32_acceptS =
        "\20\uffff\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15"+
        "\1\16\1\17\1\1";
    static final String DFA32_specialS =
        "\1\uffff\1\10\1\3\1\1\1\13\1\2\1\6\1\7\1\16\1\12\1\5\1\0\1\14\1"+
        "\15\1\11\1\4\17\uffff}>";
    static final String[] DFA32_transitionS = {
            "\2\35\1\24\1\25\1\26\1\22\6\35\1\17\1\1\1\2\1\3\1\4\1\5\1\6"+
            "\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1\uffff\1\30\2\35\2\20\34"+
            "\uffff\1\35\2\uffff\2\35\2\uffff\1\27\1\uffff\1\31\4\uffff\1"+
            "\23\1\33\1\21\1\32\1\35\1\34\15\uffff\1\20\1\16\5\35\14\uffff"+
            "\1\35\2\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "\1\35\41\uffff\2\35\30\uffff\3\35\1\uffff\2\35\1\uffff\1\35"+
            "\2\uffff\1\36\2\uffff\1\35\4\uffff\1\35\2\uffff\14\35\2\uffff"+
            "\21\35\1\uffff\2\35",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA32_eot = DFA.unpackEncodedString(DFA32_eotS);
    static final short[] DFA32_eof = DFA.unpackEncodedString(DFA32_eofS);
    static final char[] DFA32_min = DFA.unpackEncodedStringToUnsignedChars(DFA32_minS);
    static final char[] DFA32_max = DFA.unpackEncodedStringToUnsignedChars(DFA32_maxS);
    static final short[] DFA32_accept = DFA.unpackEncodedString(DFA32_acceptS);
    static final short[] DFA32_special = DFA.unpackEncodedString(DFA32_specialS);
    static final short[][] DFA32_transition;

    static {
        int numStates = DFA32_transitionS.length;
        DFA32_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA32_transition[i] = DFA.unpackEncodedString(DFA32_transitionS[i]);
        }
    }

    class DFA32 extends DFA {

        public DFA32(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 32;
            this.eot = DFA32_eot;
            this.eof = DFA32_eof;
            this.min = DFA32_min;
            this.max = DFA32_max;
            this.accept = DFA32_accept;
            this.special = DFA32_special;
            this.transition = DFA32_transition;
        }
        public String getDescription() {
            return "440:1: statement : ( ( labeledStatement )=> labeledStatement | declaration | deferStmt | returnStmt | goStmt | breakStmt | continueStmt | fallthroughStmt | block | ifStmt | switchStmt | selectStmt | gotoStmt | forStmt | simpleStmt );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA32_11 = input.LA(1);

                         
                        int index32_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_11==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_11==EOF||LA32_11==SEMI||(LA32_11>=PLUSPLUS && LA32_11<=MINUSMINUS)||(LA32_11>=64 && LA32_11<=66)||(LA32_11>=68 && LA32_11<=69)||LA32_11==71||LA32_11==77||LA32_11==82||(LA32_11>=85 && LA32_11<=96)||(LA32_11>=99 && LA32_11<=115)||(LA32_11>=117 && LA32_11<=118)) ) {s = 29;}

                         
                        input.seek(index32_11);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA32_3 = input.LA(1);

                         
                        int index32_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_3==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_3==EOF||LA32_3==SEMI||(LA32_3>=PLUSPLUS && LA32_3<=MINUSMINUS)||(LA32_3>=64 && LA32_3<=66)||(LA32_3>=68 && LA32_3<=69)||LA32_3==71||LA32_3==77||LA32_3==82||(LA32_3>=85 && LA32_3<=96)||(LA32_3>=99 && LA32_3<=115)||(LA32_3>=117 && LA32_3<=118)) ) {s = 29;}

                         
                        input.seek(index32_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA32_5 = input.LA(1);

                         
                        int index32_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_5==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_5==EOF||LA32_5==SEMI||(LA32_5>=PLUSPLUS && LA32_5<=MINUSMINUS)||(LA32_5>=64 && LA32_5<=66)||(LA32_5>=68 && LA32_5<=69)||LA32_5==71||LA32_5==77||LA32_5==82||(LA32_5>=85 && LA32_5<=96)||(LA32_5>=99 && LA32_5<=115)||(LA32_5>=117 && LA32_5<=118)) ) {s = 29;}

                         
                        input.seek(index32_5);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA32_2 = input.LA(1);

                         
                        int index32_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_2==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_2==EOF||LA32_2==SEMI||(LA32_2>=PLUSPLUS && LA32_2<=MINUSMINUS)||(LA32_2>=64 && LA32_2<=66)||(LA32_2>=68 && LA32_2<=69)||LA32_2==71||LA32_2==77||LA32_2==82||(LA32_2>=85 && LA32_2<=96)||(LA32_2>=99 && LA32_2<=115)||(LA32_2>=117 && LA32_2<=118)) ) {s = 29;}

                         
                        input.seek(index32_2);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA32_15 = input.LA(1);

                         
                        int index32_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_15==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_15==EOF||LA32_15==SEMI||(LA32_15>=PLUSPLUS && LA32_15<=MINUSMINUS)||(LA32_15>=64 && LA32_15<=66)||(LA32_15>=68 && LA32_15<=69)||LA32_15==71||LA32_15==77||LA32_15==82||(LA32_15>=85 && LA32_15<=96)||(LA32_15>=99 && LA32_15<=115)||(LA32_15>=117 && LA32_15<=118)) ) {s = 29;}

                         
                        input.seek(index32_15);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA32_10 = input.LA(1);

                         
                        int index32_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_10==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_10==EOF||LA32_10==SEMI||(LA32_10>=PLUSPLUS && LA32_10<=MINUSMINUS)||(LA32_10>=64 && LA32_10<=66)||(LA32_10>=68 && LA32_10<=69)||LA32_10==71||LA32_10==77||LA32_10==82||(LA32_10>=85 && LA32_10<=96)||(LA32_10>=99 && LA32_10<=115)||(LA32_10>=117 && LA32_10<=118)) ) {s = 29;}

                         
                        input.seek(index32_10);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA32_6 = input.LA(1);

                         
                        int index32_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_6==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_6==EOF||LA32_6==SEMI||(LA32_6>=PLUSPLUS && LA32_6<=MINUSMINUS)||(LA32_6>=64 && LA32_6<=66)||(LA32_6>=68 && LA32_6<=69)||LA32_6==71||LA32_6==77||LA32_6==82||(LA32_6>=85 && LA32_6<=96)||(LA32_6>=99 && LA32_6<=115)||(LA32_6>=117 && LA32_6<=118)) ) {s = 29;}

                         
                        input.seek(index32_6);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA32_7 = input.LA(1);

                         
                        int index32_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_7==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_7==EOF||LA32_7==SEMI||(LA32_7>=PLUSPLUS && LA32_7<=MINUSMINUS)||(LA32_7>=64 && LA32_7<=66)||(LA32_7>=68 && LA32_7<=69)||LA32_7==71||LA32_7==77||LA32_7==82||(LA32_7>=85 && LA32_7<=96)||(LA32_7>=99 && LA32_7<=115)||(LA32_7>=117 && LA32_7<=118)) ) {s = 29;}

                         
                        input.seek(index32_7);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA32_1 = input.LA(1);

                         
                        int index32_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_1==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_1==EOF||LA32_1==SEMI||(LA32_1>=PLUSPLUS && LA32_1<=MINUSMINUS)||(LA32_1>=64 && LA32_1<=66)||(LA32_1>=68 && LA32_1<=69)||LA32_1==71||LA32_1==77||LA32_1==82||(LA32_1>=85 && LA32_1<=96)||(LA32_1>=99 && LA32_1<=115)||(LA32_1>=117 && LA32_1<=118)) ) {s = 29;}

                         
                        input.seek(index32_1);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA32_14 = input.LA(1);

                         
                        int index32_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_14==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_14==EOF||LA32_14==SEMI||(LA32_14>=PLUSPLUS && LA32_14<=MINUSMINUS)||(LA32_14>=64 && LA32_14<=66)||(LA32_14>=68 && LA32_14<=69)||LA32_14==71||LA32_14==77||LA32_14==82||(LA32_14>=85 && LA32_14<=96)||(LA32_14>=99 && LA32_14<=115)||(LA32_14>=117 && LA32_14<=118)) ) {s = 29;}

                         
                        input.seek(index32_14);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA32_9 = input.LA(1);

                         
                        int index32_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_9==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_9==EOF||LA32_9==SEMI||(LA32_9>=PLUSPLUS && LA32_9<=MINUSMINUS)||(LA32_9>=64 && LA32_9<=66)||(LA32_9>=68 && LA32_9<=69)||LA32_9==71||LA32_9==77||LA32_9==82||(LA32_9>=85 && LA32_9<=96)||(LA32_9>=99 && LA32_9<=115)||(LA32_9>=117 && LA32_9<=118)) ) {s = 29;}

                         
                        input.seek(index32_9);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA32_4 = input.LA(1);

                         
                        int index32_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_4==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_4==EOF||LA32_4==SEMI||(LA32_4>=PLUSPLUS && LA32_4<=MINUSMINUS)||(LA32_4>=64 && LA32_4<=66)||(LA32_4>=68 && LA32_4<=69)||LA32_4==71||LA32_4==77||LA32_4==82||(LA32_4>=85 && LA32_4<=96)||(LA32_4>=99 && LA32_4<=115)||(LA32_4>=117 && LA32_4<=118)) ) {s = 29;}

                         
                        input.seek(index32_4);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA32_12 = input.LA(1);

                         
                        int index32_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_12==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_12==EOF||LA32_12==SEMI||(LA32_12>=PLUSPLUS && LA32_12<=MINUSMINUS)||(LA32_12>=64 && LA32_12<=66)||(LA32_12>=68 && LA32_12<=69)||LA32_12==71||LA32_12==77||LA32_12==82||(LA32_12>=85 && LA32_12<=96)||(LA32_12>=99 && LA32_12<=115)||(LA32_12>=117 && LA32_12<=118)) ) {s = 29;}

                         
                        input.seek(index32_12);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA32_13 = input.LA(1);

                         
                        int index32_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_13==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_13==EOF||LA32_13==SEMI||(LA32_13>=PLUSPLUS && LA32_13<=MINUSMINUS)||(LA32_13>=64 && LA32_13<=66)||(LA32_13>=68 && LA32_13<=69)||LA32_13==71||LA32_13==77||LA32_13==82||(LA32_13>=85 && LA32_13<=96)||(LA32_13>=99 && LA32_13<=115)||(LA32_13>=117 && LA32_13<=118)) ) {s = 29;}

                         
                        input.seek(index32_13);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA32_8 = input.LA(1);

                         
                        int index32_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA32_8==74) && (synpred6_GoSource())) {s = 30;}

                        else if ( (LA32_8==EOF||LA32_8==SEMI||(LA32_8>=PLUSPLUS && LA32_8<=MINUSMINUS)||(LA32_8>=64 && LA32_8<=66)||(LA32_8>=68 && LA32_8<=69)||LA32_8==71||LA32_8==77||LA32_8==82||(LA32_8>=85 && LA32_8<=96)||(LA32_8>=99 && LA32_8<=115)||(LA32_8>=117 && LA32_8<=118)) ) {s = 29;}

                         
                        input.seek(index32_8);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 32, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA33_eotS =
        "\46\uffff";
    static final String DFA33_eofS =
        "\1\37\45\uffff";
    static final String DFA33_minS =
        "\1\4\36\0\7\uffff";
    static final String DFA33_maxS =
        "\1\170\36\0\7\uffff";
    static final String DFA33_acceptS =
        "\37\uffff\1\5\2\uffff\1\1\1\3\1\4\1\2";
    static final String DFA33_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30\1\31"+
        "\1\32\1\33\1\34\1\35\7\uffff}>";
    static final String[] DFA33_transitionS = {
            "\1\37\1\11\4\uffff\6\11\1\32\1\10\1\15\1\16\1\17\1\20\1\21\1"+
            "\22\1\23\1\24\1\25\1\26\1\27\1\30\2\uffff\1\12\1\35\36\uffff"+
            "\1\34\2\uffff\1\33\1\3\2\uffff\1\37\12\uffff\1\2\17\uffff\1"+
            "\31\1\13\1\4\1\5\1\6\1\7\14\uffff\1\1\2\uffff\1\14\1\36",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA33_eot = DFA.unpackEncodedString(DFA33_eotS);
    static final short[] DFA33_eof = DFA.unpackEncodedString(DFA33_eofS);
    static final char[] DFA33_min = DFA.unpackEncodedStringToUnsignedChars(DFA33_minS);
    static final char[] DFA33_max = DFA.unpackEncodedStringToUnsignedChars(DFA33_maxS);
    static final short[] DFA33_accept = DFA.unpackEncodedString(DFA33_acceptS);
    static final short[] DFA33_special = DFA.unpackEncodedString(DFA33_specialS);
    static final short[][] DFA33_transition;

    static {
        int numStates = DFA33_transitionS.length;
        DFA33_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA33_transition[i] = DFA.unpackEncodedString(DFA33_transitionS[i]);
        }
    }

    class DFA33 extends DFA {

        public DFA33(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 33;
            this.eot = DFA33_eot;
            this.eof = DFA33_eof;
            this.min = DFA33_min;
            this.max = DFA33_max;
            this.accept = DFA33_accept;
            this.special = DFA33_special;
            this.transition = DFA33_transition;
        }
        public String getDescription() {
            return "459:1: simpleStmt : ( ( assignment )=> assignment | ( shortVarDecl )=> shortVarDecl | ( incDecStmt )=> incDecStmt | ( expressionStmt )=> expressionStmt | );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA33_1 = input.LA(1);

                         
                        int index33_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA33_2 = input.LA(1);

                         
                        int index33_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA33_3 = input.LA(1);

                         
                        int index33_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA33_4 = input.LA(1);

                         
                        int index33_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA33_5 = input.LA(1);

                         
                        int index33_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA33_6 = input.LA(1);

                         
                        int index33_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA33_7 = input.LA(1);

                         
                        int index33_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA33_8 = input.LA(1);

                         
                        int index33_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA33_9 = input.LA(1);

                         
                        int index33_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA33_10 = input.LA(1);

                         
                        int index33_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA33_11 = input.LA(1);

                         
                        int index33_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA33_12 = input.LA(1);

                         
                        int index33_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA33_13 = input.LA(1);

                         
                        int index33_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA33_14 = input.LA(1);

                         
                        int index33_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_14);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA33_15 = input.LA(1);

                         
                        int index33_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_15);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA33_16 = input.LA(1);

                         
                        int index33_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_16);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA33_17 = input.LA(1);

                         
                        int index33_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_17);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA33_18 = input.LA(1);

                         
                        int index33_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_18);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA33_19 = input.LA(1);

                         
                        int index33_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_19);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA33_20 = input.LA(1);

                         
                        int index33_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_20);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA33_21 = input.LA(1);

                         
                        int index33_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_21);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA33_22 = input.LA(1);

                         
                        int index33_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_22);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA33_23 = input.LA(1);

                         
                        int index33_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_23);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA33_24 = input.LA(1);

                         
                        int index33_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_24);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA33_25 = input.LA(1);

                         
                        int index33_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_25);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA33_26 = input.LA(1);

                         
                        int index33_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred8_GoSource()) ) {s = 37;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_26);
                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA33_27 = input.LA(1);

                         
                        int index33_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_27);
                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA33_28 = input.LA(1);

                         
                        int index33_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_28);
                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA33_29 = input.LA(1);

                         
                        int index33_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_29);
                        if ( s>=0 ) return s;
                        break;
                    case 29 : 
                        int LA33_30 = input.LA(1);

                         
                        int index33_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_GoSource()) ) {s = 34;}

                        else if ( (synpred9_GoSource()) ) {s = 35;}

                        else if ( (synpred10_GoSource()) ) {s = 36;}

                         
                        input.seek(index33_30);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 33, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA34_eotS =
        "\42\uffff";
    static final String DFA34_eofS =
        "\42\uffff";
    static final String DFA34_minS =
        "\1\4\36\0\3\uffff";
    static final String DFA34_maxS =
        "\1\170\36\0\3\uffff";
    static final String DFA34_acceptS =
        "\37\uffff\1\1\1\3\1\2";
    static final String DFA34_specialS =
        "\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1"+
        "\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30\1\31\1\32"+
        "\1\33\1\34\1\35\1\36\3\uffff}>";
    static final String[] DFA34_transitionS = {
            "\1\37\1\11\4\uffff\6\11\1\32\1\10\1\15\1\16\1\17\1\20\1\21\1"+
            "\22\1\23\1\24\1\25\1\26\1\27\1\30\2\uffff\1\12\1\35\36\uffff"+
            "\1\34\2\uffff\1\33\1\3\2\uffff\1\40\12\uffff\1\2\17\uffff\1"+
            "\31\1\13\1\4\1\5\1\6\1\7\14\uffff\1\1\2\uffff\1\14\1\36",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            ""
    };

    static final short[] DFA34_eot = DFA.unpackEncodedString(DFA34_eotS);
    static final short[] DFA34_eof = DFA.unpackEncodedString(DFA34_eofS);
    static final char[] DFA34_min = DFA.unpackEncodedStringToUnsignedChars(DFA34_minS);
    static final char[] DFA34_max = DFA.unpackEncodedStringToUnsignedChars(DFA34_maxS);
    static final short[] DFA34_accept = DFA.unpackEncodedString(DFA34_acceptS);
    static final short[] DFA34_special = DFA.unpackEncodedString(DFA34_specialS);
    static final short[][] DFA34_transition;

    static {
        int numStates = DFA34_transitionS.length;
        DFA34_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA34_transition[i] = DFA.unpackEncodedString(DFA34_transitionS[i]);
        }
    }

    class DFA34 extends DFA {

        public DFA34(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 34;
            this.eot = DFA34_eot;
            this.eof = DFA34_eof;
            this.min = DFA34_min;
            this.max = DFA34_max;
            this.accept = DFA34_accept;
            this.special = DFA34_special;
            this.transition = DFA34_transition;
        }
        public String getDescription() {
            return "472:3: ( ( simpleStmt SEMI )=> simpleStmt SEMI expression block | ( expression block )=> expression block | block )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA34_0 = input.LA(1);

                         
                        int index34_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA34_0==116) ) {s = 1;}

                        else if ( (LA34_0==82) ) {s = 2;}

                        else if ( (LA34_0==68) ) {s = 3;}

                        else if ( (LA34_0==100) ) {s = 4;}

                        else if ( (LA34_0==101) ) {s = 5;}

                        else if ( (LA34_0==102) ) {s = 6;}

                        else if ( (LA34_0==103) ) {s = 7;}

                        else if ( (LA34_0==APPEND) ) {s = 8;}

                        else if ( (LA34_0==String_Lit||(LA34_0>=Integer && LA34_0<=Char_Lit)) ) {s = 9;}

                        else if ( (LA34_0==STRUCT) ) {s = 10;}

                        else if ( (LA34_0==99) ) {s = 11;}

                        else if ( (LA34_0==119) ) {s = 12;}

                        else if ( (LA34_0==CAP) ) {s = 13;}

                        else if ( (LA34_0==CLOSE) ) {s = 14;}

                        else if ( (LA34_0==CLOSED) ) {s = 15;}

                        else if ( (LA34_0==CMPLX) ) {s = 16;}

                        else if ( (LA34_0==COPY) ) {s = 17;}

                        else if ( (LA34_0==IMAG) ) {s = 18;}

                        else if ( (LA34_0==LEN) ) {s = 19;}

                        else if ( (LA34_0==MAKE) ) {s = 20;}

                        else if ( (LA34_0==PANIC) ) {s = 21;}

                        else if ( (LA34_0==PRINT) ) {s = 22;}

                        else if ( (LA34_0==PRINTLN) ) {s = 23;}

                        else if ( (LA34_0==REAL) ) {s = 24;}

                        else if ( (LA34_0==98) ) {s = 25;}

                        else if ( (LA34_0==Identifier) ) {s = 26;}

                        else if ( (LA34_0==67) ) {s = 27;}

                        else if ( (LA34_0==64) ) {s = 28;}

                        else if ( (LA34_0==INTERFACE) ) {s = 29;}

                        else if ( (LA34_0==120) ) {s = 30;}

                        else if ( (LA34_0==SEMI) && (synpred11_GoSource())) {s = 31;}

                        else if ( (LA34_0==71) ) {s = 32;}

                         
                        input.seek(index34_0);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA34_1 = input.LA(1);

                         
                        int index34_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_1);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA34_2 = input.LA(1);

                         
                        int index34_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_2);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA34_3 = input.LA(1);

                         
                        int index34_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_3);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA34_4 = input.LA(1);

                         
                        int index34_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_4);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA34_5 = input.LA(1);

                         
                        int index34_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_5);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA34_6 = input.LA(1);

                         
                        int index34_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_6);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA34_7 = input.LA(1);

                         
                        int index34_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_7);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA34_8 = input.LA(1);

                         
                        int index34_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_8);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA34_9 = input.LA(1);

                         
                        int index34_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_9);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA34_10 = input.LA(1);

                         
                        int index34_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_10);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA34_11 = input.LA(1);

                         
                        int index34_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_11);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA34_12 = input.LA(1);

                         
                        int index34_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_12);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA34_13 = input.LA(1);

                         
                        int index34_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_13);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA34_14 = input.LA(1);

                         
                        int index34_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_14);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA34_15 = input.LA(1);

                         
                        int index34_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_15);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA34_16 = input.LA(1);

                         
                        int index34_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_16);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA34_17 = input.LA(1);

                         
                        int index34_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_17);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA34_18 = input.LA(1);

                         
                        int index34_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_18);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA34_19 = input.LA(1);

                         
                        int index34_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_19);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA34_20 = input.LA(1);

                         
                        int index34_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_20);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA34_21 = input.LA(1);

                         
                        int index34_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_21);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA34_22 = input.LA(1);

                         
                        int index34_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_22);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA34_23 = input.LA(1);

                         
                        int index34_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_23);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA34_24 = input.LA(1);

                         
                        int index34_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_24);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA34_25 = input.LA(1);

                         
                        int index34_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_25);
                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA34_26 = input.LA(1);

                         
                        int index34_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_26);
                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA34_27 = input.LA(1);

                         
                        int index34_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_27);
                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA34_28 = input.LA(1);

                         
                        int index34_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_28);
                        if ( s>=0 ) return s;
                        break;
                    case 29 : 
                        int LA34_29 = input.LA(1);

                         
                        int index34_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_29);
                        if ( s>=0 ) return s;
                        break;
                    case 30 : 
                        int LA34_30 = input.LA(1);

                         
                        int index34_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_GoSource()) ) {s = 31;}

                        else if ( (synpred12_GoSource()) ) {s = 33;}

                         
                        input.seek(index34_30);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 34, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA37_eotS =
        "\44\uffff";
    static final String DFA37_eofS =
        "\44\uffff";
    static final String DFA37_minS =
        "\1\4\37\0\4\uffff";
    static final String DFA37_maxS =
        "\1\170\37\0\4\uffff";
    static final String DFA37_acceptS =
        "\40\uffff\1\4\1\1\1\2\1\3";
    static final String DFA37_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30\1\31"+
        "\1\32\1\33\1\34\1\35\1\36\4\uffff}>";
    static final String[] DFA37_transitionS = {
            "\1\37\1\11\4\uffff\6\11\1\32\1\10\1\15\1\16\1\17\1\20\1\21\1"+
            "\22\1\23\1\24\1\25\1\26\1\27\1\30\2\uffff\1\12\1\35\36\uffff"+
            "\1\34\2\uffff\1\33\1\3\2\uffff\1\40\12\uffff\1\2\17\uffff\1"+
            "\31\1\13\1\4\1\5\1\6\1\7\14\uffff\1\1\2\uffff\1\14\1\36",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA37_eot = DFA.unpackEncodedString(DFA37_eotS);
    static final short[] DFA37_eof = DFA.unpackEncodedString(DFA37_eofS);
    static final char[] DFA37_min = DFA.unpackEncodedStringToUnsignedChars(DFA37_minS);
    static final char[] DFA37_max = DFA.unpackEncodedStringToUnsignedChars(DFA37_maxS);
    static final short[] DFA37_accept = DFA.unpackEncodedString(DFA37_acceptS);
    static final short[] DFA37_special = DFA.unpackEncodedString(DFA37_specialS);
    static final short[][] DFA37_transition;

    static {
        int numStates = DFA37_transitionS.length;
        DFA37_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA37_transition[i] = DFA.unpackEncodedString(DFA37_transitionS[i]);
        }
    }

    class DFA37 extends DFA {

        public DFA37(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 37;
            this.eot = DFA37_eot;
            this.eof = DFA37_eof;
            this.min = DFA37_min;
            this.max = DFA37_max;
            this.accept = DFA37_accept;
            this.special = DFA37_special;
            this.transition = DFA37_transition;
        }
        public String getDescription() {
            return "489:3: ( ( simpleStmt SEMI expression )=> simpleStmt SEMI expression | ( simpleStmt SEMI )=> simpleStmt SEMI | expression )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA37_1 = input.LA(1);

                         
                        int index37_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA37_2 = input.LA(1);

                         
                        int index37_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA37_3 = input.LA(1);

                         
                        int index37_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA37_4 = input.LA(1);

                         
                        int index37_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA37_5 = input.LA(1);

                         
                        int index37_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA37_6 = input.LA(1);

                         
                        int index37_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA37_7 = input.LA(1);

                         
                        int index37_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA37_8 = input.LA(1);

                         
                        int index37_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA37_9 = input.LA(1);

                         
                        int index37_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA37_10 = input.LA(1);

                         
                        int index37_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA37_11 = input.LA(1);

                         
                        int index37_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA37_12 = input.LA(1);

                         
                        int index37_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA37_13 = input.LA(1);

                         
                        int index37_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA37_14 = input.LA(1);

                         
                        int index37_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_14);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA37_15 = input.LA(1);

                         
                        int index37_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_15);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA37_16 = input.LA(1);

                         
                        int index37_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_16);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA37_17 = input.LA(1);

                         
                        int index37_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_17);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA37_18 = input.LA(1);

                         
                        int index37_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_18);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA37_19 = input.LA(1);

                         
                        int index37_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_19);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA37_20 = input.LA(1);

                         
                        int index37_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_20);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA37_21 = input.LA(1);

                         
                        int index37_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_21);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA37_22 = input.LA(1);

                         
                        int index37_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_22);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA37_23 = input.LA(1);

                         
                        int index37_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_23);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA37_24 = input.LA(1);

                         
                        int index37_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_24);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA37_25 = input.LA(1);

                         
                        int index37_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_25);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA37_26 = input.LA(1);

                         
                        int index37_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_26);
                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA37_27 = input.LA(1);

                         
                        int index37_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_27);
                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA37_28 = input.LA(1);

                         
                        int index37_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_28);
                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA37_29 = input.LA(1);

                         
                        int index37_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_29);
                        if ( s>=0 ) return s;
                        break;
                    case 29 : 
                        int LA37_30 = input.LA(1);

                         
                        int index37_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                        else if ( (true) ) {s = 35;}

                         
                        input.seek(index37_30);
                        if ( s>=0 ) return s;
                        break;
                    case 30 : 
                        int LA37_31 = input.LA(1);

                         
                        int index37_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_GoSource()) ) {s = 33;}

                        else if ( (synpred16_GoSource()) ) {s = 34;}

                         
                        input.seek(index37_31);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 37, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA41_eotS =
        "\41\uffff";
    static final String DFA41_eofS =
        "\41\uffff";
    static final String DFA41_minS =
        "\1\4\1\uffff\2\0\4\uffff\27\0\2\uffff";
    static final String DFA41_maxS =
        "\1\170\1\uffff\2\0\4\uffff\27\0\2\uffff";
    static final String DFA41_acceptS =
        "\1\uffff\1\1\2\uffff\4\1\27\uffff\1\1\1\2";
    static final String DFA41_specialS =
        "\1\0\1\uffff\1\1\1\2\4\uffff\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12"+
        "\1\13\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27"+
        "\1\30\1\31\2\uffff}>";
    static final String[] DFA41_transitionS = {
            "\1\37\1\11\4\uffff\6\11\1\32\1\10\1\15\1\16\1\17\1\20\1\21\1"+
            "\22\1\23\1\24\1\25\1\26\1\27\1\30\2\uffff\1\12\1\35\36\uffff"+
            "\1\34\2\uffff\1\33\1\3\15\uffff\1\2\17\uffff\1\31\1\13\1\4\1"+
            "\5\1\6\1\7\14\uffff\1\1\2\uffff\1\14\1\36",
            "",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA41_eot = DFA.unpackEncodedString(DFA41_eotS);
    static final short[] DFA41_eof = DFA.unpackEncodedString(DFA41_eofS);
    static final char[] DFA41_min = DFA.unpackEncodedStringToUnsignedChars(DFA41_minS);
    static final char[] DFA41_max = DFA.unpackEncodedStringToUnsignedChars(DFA41_maxS);
    static final short[] DFA41_accept = DFA.unpackEncodedString(DFA41_acceptS);
    static final short[] DFA41_special = DFA.unpackEncodedString(DFA41_specialS);
    static final short[][] DFA41_transition;

    static {
        int numStates = DFA41_transitionS.length;
        DFA41_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA41_transition[i] = DFA.unpackEncodedString(DFA41_transitionS[i]);
        }
    }

    class DFA41 extends DFA {

        public DFA41(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 41;
            this.eot = DFA41_eot;
            this.eof = DFA41_eof;
            this.min = DFA41_min;
            this.max = DFA41_max;
            this.accept = DFA41_accept;
            this.special = DFA41_special;
            this.transition = DFA41_transition;
        }
        public String getDescription() {
            return "511:3: ( ( simpleStmt SEMI )=> simpleStmt SEMI typeSwitchGuard | typeSwitchGuard )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA41_0 = input.LA(1);

                         
                        int index41_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA41_0==116) && (synpred17_GoSource())) {s = 1;}

                        else if ( (LA41_0==82) ) {s = 2;}

                        else if ( (LA41_0==68) ) {s = 3;}

                        else if ( (LA41_0==100) && (synpred17_GoSource())) {s = 4;}

                        else if ( (LA41_0==101) && (synpred17_GoSource())) {s = 5;}

                        else if ( (LA41_0==102) && (synpred17_GoSource())) {s = 6;}

                        else if ( (LA41_0==103) && (synpred17_GoSource())) {s = 7;}

                        else if ( (LA41_0==APPEND) ) {s = 8;}

                        else if ( (LA41_0==String_Lit||(LA41_0>=Integer && LA41_0<=Char_Lit)) ) {s = 9;}

                        else if ( (LA41_0==STRUCT) ) {s = 10;}

                        else if ( (LA41_0==99) ) {s = 11;}

                        else if ( (LA41_0==119) ) {s = 12;}

                        else if ( (LA41_0==CAP) ) {s = 13;}

                        else if ( (LA41_0==CLOSE) ) {s = 14;}

                        else if ( (LA41_0==CLOSED) ) {s = 15;}

                        else if ( (LA41_0==CMPLX) ) {s = 16;}

                        else if ( (LA41_0==COPY) ) {s = 17;}

                        else if ( (LA41_0==IMAG) ) {s = 18;}

                        else if ( (LA41_0==LEN) ) {s = 19;}

                        else if ( (LA41_0==MAKE) ) {s = 20;}

                        else if ( (LA41_0==PANIC) ) {s = 21;}

                        else if ( (LA41_0==PRINT) ) {s = 22;}

                        else if ( (LA41_0==PRINTLN) ) {s = 23;}

                        else if ( (LA41_0==REAL) ) {s = 24;}

                        else if ( (LA41_0==98) ) {s = 25;}

                        else if ( (LA41_0==Identifier) ) {s = 26;}

                        else if ( (LA41_0==67) ) {s = 27;}

                        else if ( (LA41_0==64) ) {s = 28;}

                        else if ( (LA41_0==INTERFACE) ) {s = 29;}

                        else if ( (LA41_0==120) ) {s = 30;}

                        else if ( (LA41_0==SEMI) && (synpred17_GoSource())) {s = 31;}

                         
                        input.seek(index41_0);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA41_2 = input.LA(1);

                         
                        int index41_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA41_3 = input.LA(1);

                         
                        int index41_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA41_8 = input.LA(1);

                         
                        int index41_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_8);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA41_9 = input.LA(1);

                         
                        int index41_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_9);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA41_10 = input.LA(1);

                         
                        int index41_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_10);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA41_11 = input.LA(1);

                         
                        int index41_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_11);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA41_12 = input.LA(1);

                         
                        int index41_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_12);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA41_13 = input.LA(1);

                         
                        int index41_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_13);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA41_14 = input.LA(1);

                         
                        int index41_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_14);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA41_15 = input.LA(1);

                         
                        int index41_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_15);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA41_16 = input.LA(1);

                         
                        int index41_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_16);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA41_17 = input.LA(1);

                         
                        int index41_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_17);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA41_18 = input.LA(1);

                         
                        int index41_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_18);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA41_19 = input.LA(1);

                         
                        int index41_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_19);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA41_20 = input.LA(1);

                         
                        int index41_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_20);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA41_21 = input.LA(1);

                         
                        int index41_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_21);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA41_22 = input.LA(1);

                         
                        int index41_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_22);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA41_23 = input.LA(1);

                         
                        int index41_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_23);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA41_24 = input.LA(1);

                         
                        int index41_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_24);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA41_25 = input.LA(1);

                         
                        int index41_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_25);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA41_26 = input.LA(1);

                         
                        int index41_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_26);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA41_27 = input.LA(1);

                         
                        int index41_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_27);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA41_28 = input.LA(1);

                         
                        int index41_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_28);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA41_29 = input.LA(1);

                         
                        int index41_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_29);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA41_30 = input.LA(1);

                         
                        int index41_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index41_30);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 41, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA43_eotS =
        "\22\uffff";
    static final String DFA43_eofS =
        "\22\uffff";
    static final String DFA43_minS =
        "\1\5\17\100\2\uffff";
    static final String DFA43_maxS =
        "\1\170\17\143\2\uffff";
    static final String DFA43_acceptS =
        "\20\uffff\1\2\1\1";
    static final String DFA43_specialS =
        "\22\uffff}>";
    static final String[] DFA43_transitionS = {
            "\1\20\4\uffff\6\20\1\17\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11"+
            "\1\12\1\13\1\14\1\15\2\uffff\2\20\36\uffff\1\20\2\uffff\2\20"+
            "\15\uffff\1\20\17\uffff\1\16\1\20\23\uffff\2\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "\2\20\5\uffff\1\20\5\uffff\1\21\25\uffff\1\20",
            "",
            ""
    };

    static final short[] DFA43_eot = DFA.unpackEncodedString(DFA43_eotS);
    static final short[] DFA43_eof = DFA.unpackEncodedString(DFA43_eofS);
    static final char[] DFA43_min = DFA.unpackEncodedStringToUnsignedChars(DFA43_minS);
    static final char[] DFA43_max = DFA.unpackEncodedStringToUnsignedChars(DFA43_maxS);
    static final short[] DFA43_accept = DFA.unpackEncodedString(DFA43_acceptS);
    static final short[] DFA43_special = DFA.unpackEncodedString(DFA43_specialS);
    static final short[][] DFA43_transition;

    static {
        int numStates = DFA43_transitionS.length;
        DFA43_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA43_transition[i] = DFA.unpackEncodedString(DFA43_transitionS[i]);
        }
    }

    class DFA43 extends DFA {

        public DFA43(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 43;
            this.eot = DFA43_eot;
            this.eof = DFA43_eof;
            this.min = DFA43_min;
            this.max = DFA43_max;
            this.accept = DFA43_accept;
            this.special = DFA43_special;
            this.transition = DFA43_transition;
        }
        public String getDescription() {
            return "524:3: ( identifier ':=' )?";
        }
    }
    static final String DFA53_eotS =
        "\40\uffff";
    static final String DFA53_eofS =
        "\40\uffff";
    static final String DFA53_minS =
        "\1\5\35\0\2\uffff";
    static final String DFA53_maxS =
        "\1\170\35\0\2\uffff";
    static final String DFA53_acceptS =
        "\36\uffff\1\2\1\1";
    static final String DFA53_specialS =
        "\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1"+
        "\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30\1\31\1\32"+
        "\1\33\1\34\1\35\2\uffff}>";
    static final String[] DFA53_transitionS = {
            "\1\10\4\uffff\6\10\1\31\1\7\1\14\1\15\1\16\1\17\1\20\1\21\1"+
            "\22\1\23\1\24\1\25\1\26\1\27\2\uffff\1\11\1\34\36\uffff\1\33"+
            "\2\uffff\1\32\1\2\15\uffff\1\1\17\uffff\1\30\1\12\1\3\1\4\1"+
            "\5\1\6\14\uffff\1\36\2\uffff\1\13\1\35",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA53_eot = DFA.unpackEncodedString(DFA53_eotS);
    static final short[] DFA53_eof = DFA.unpackEncodedString(DFA53_eofS);
    static final char[] DFA53_min = DFA.unpackEncodedStringToUnsignedChars(DFA53_minS);
    static final char[] DFA53_max = DFA.unpackEncodedStringToUnsignedChars(DFA53_maxS);
    static final short[] DFA53_accept = DFA.unpackEncodedString(DFA53_acceptS);
    static final short[] DFA53_special = DFA.unpackEncodedString(DFA53_specialS);
    static final short[][] DFA53_transition;

    static {
        int numStates = DFA53_transitionS.length;
        DFA53_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA53_transition[i] = DFA.unpackEncodedString(DFA53_transitionS[i]);
        }
    }

    class DFA53 extends DFA {

        public DFA53(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 53;
            this.eot = DFA53_eot;
            this.eof = DFA53_eof;
            this.min = DFA53_min;
            this.max = DFA53_max;
            this.accept = DFA53_accept;
            this.special = DFA53_special;
            this.transition = DFA53_transition;
        }
        public String getDescription() {
            return "597:3: ( ( sendExpr )=> sendExpr | ( recvExpr )=> recvExpr )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA53_0 = input.LA(1);

                         
                        int index53_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA53_0==82) ) {s = 1;}

                        else if ( (LA53_0==68) ) {s = 2;}

                        else if ( (LA53_0==100) ) {s = 3;}

                        else if ( (LA53_0==101) ) {s = 4;}

                        else if ( (LA53_0==102) ) {s = 5;}

                        else if ( (LA53_0==103) ) {s = 6;}

                        else if ( (LA53_0==APPEND) ) {s = 7;}

                        else if ( (LA53_0==String_Lit||(LA53_0>=Integer && LA53_0<=Char_Lit)) ) {s = 8;}

                        else if ( (LA53_0==STRUCT) ) {s = 9;}

                        else if ( (LA53_0==99) ) {s = 10;}

                        else if ( (LA53_0==119) ) {s = 11;}

                        else if ( (LA53_0==CAP) ) {s = 12;}

                        else if ( (LA53_0==CLOSE) ) {s = 13;}

                        else if ( (LA53_0==CLOSED) ) {s = 14;}

                        else if ( (LA53_0==CMPLX) ) {s = 15;}

                        else if ( (LA53_0==COPY) ) {s = 16;}

                        else if ( (LA53_0==IMAG) ) {s = 17;}

                        else if ( (LA53_0==LEN) ) {s = 18;}

                        else if ( (LA53_0==MAKE) ) {s = 19;}

                        else if ( (LA53_0==PANIC) ) {s = 20;}

                        else if ( (LA53_0==PRINT) ) {s = 21;}

                        else if ( (LA53_0==PRINTLN) ) {s = 22;}

                        else if ( (LA53_0==REAL) ) {s = 23;}

                        else if ( (LA53_0==98) ) {s = 24;}

                        else if ( (LA53_0==Identifier) ) {s = 25;}

                        else if ( (LA53_0==67) ) {s = 26;}

                        else if ( (LA53_0==64) ) {s = 27;}

                        else if ( (LA53_0==INTERFACE) ) {s = 28;}

                        else if ( (LA53_0==120) ) {s = 29;}

                        else if ( (LA53_0==116) && (synpred19_GoSource())) {s = 30;}

                         
                        input.seek(index53_0);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA53_1 = input.LA(1);

                         
                        int index53_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_1);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA53_2 = input.LA(1);

                         
                        int index53_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_2);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA53_3 = input.LA(1);

                         
                        int index53_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_3);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA53_4 = input.LA(1);

                         
                        int index53_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_4);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA53_5 = input.LA(1);

                         
                        int index53_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_5);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA53_6 = input.LA(1);

                         
                        int index53_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_6);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA53_7 = input.LA(1);

                         
                        int index53_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_7);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA53_8 = input.LA(1);

                         
                        int index53_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_8);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA53_9 = input.LA(1);

                         
                        int index53_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_9);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA53_10 = input.LA(1);

                         
                        int index53_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_10);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA53_11 = input.LA(1);

                         
                        int index53_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_11);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA53_12 = input.LA(1);

                         
                        int index53_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_12);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA53_13 = input.LA(1);

                         
                        int index53_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_13);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA53_14 = input.LA(1);

                         
                        int index53_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_14);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA53_15 = input.LA(1);

                         
                        int index53_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_15);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA53_16 = input.LA(1);

                         
                        int index53_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_16);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA53_17 = input.LA(1);

                         
                        int index53_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_17);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA53_18 = input.LA(1);

                         
                        int index53_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_18);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA53_19 = input.LA(1);

                         
                        int index53_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_19);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA53_20 = input.LA(1);

                         
                        int index53_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_20);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA53_21 = input.LA(1);

                         
                        int index53_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_21);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA53_22 = input.LA(1);

                         
                        int index53_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_22);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA53_23 = input.LA(1);

                         
                        int index53_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_23);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA53_24 = input.LA(1);

                         
                        int index53_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_24);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA53_25 = input.LA(1);

                         
                        int index53_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_25);
                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA53_26 = input.LA(1);

                         
                        int index53_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_26);
                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA53_27 = input.LA(1);

                         
                        int index53_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_27);
                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA53_28 = input.LA(1);

                         
                        int index53_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_28);
                        if ( s>=0 ) return s;
                        break;
                    case 29 : 
                        int LA53_29 = input.LA(1);

                         
                        int index53_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_GoSource()) ) {s = 31;}

                        else if ( (synpred19_GoSource()) ) {s = 30;}

                         
                        input.seek(index53_29);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 53, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA55_eotS =
        "\40\uffff";
    static final String DFA55_eofS =
        "\40\uffff";
    static final String DFA55_minS =
        "\1\5\1\uffff\1\0\35\uffff";
    static final String DFA55_maxS =
        "\1\170\1\uffff\1\0\35\uffff";
    static final String DFA55_acceptS =
        "\1\uffff\1\1\1\uffff\34\1\1\2";
    static final String DFA55_specialS =
        "\1\0\1\uffff\1\1\35\uffff}>";
    static final String[] DFA55_transitionS = {
            "\1\11\4\uffff\6\11\1\32\1\10\1\15\1\16\1\17\1\20\1\21\1\22\1"+
            "\23\1\24\1\25\1\26\1\27\1\30\2\uffff\1\12\1\35\36\uffff\1\34"+
            "\2\uffff\1\33\1\3\15\uffff\1\2\17\uffff\1\31\1\13\1\4\1\5\1"+
            "\6\1\7\14\uffff\1\1\2\uffff\1\14\1\36",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA55_eot = DFA.unpackEncodedString(DFA55_eotS);
    static final short[] DFA55_eof = DFA.unpackEncodedString(DFA55_eofS);
    static final char[] DFA55_min = DFA.unpackEncodedStringToUnsignedChars(DFA55_minS);
    static final char[] DFA55_max = DFA.unpackEncodedStringToUnsignedChars(DFA55_maxS);
    static final short[] DFA55_accept = DFA.unpackEncodedString(DFA55_acceptS);
    static final short[] DFA55_special = DFA.unpackEncodedString(DFA55_specialS);
    static final short[][] DFA55_transition;

    static {
        int numStates = DFA55_transitionS.length;
        DFA55_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA55_transition[i] = DFA.unpackEncodedString(DFA55_transitionS[i]);
        }
    }

    class DFA55 extends DFA {

        public DFA55(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 55;
            this.eot = DFA55_eot;
            this.eof = DFA55_eof;
            this.min = DFA55_min;
            this.max = DFA55_max;
            this.accept = DFA55_accept;
            this.special = DFA55_special;
            this.transition = DFA55_transition;
        }
        public String getDescription() {
            return "609:1: recvExpr : ( ( expression ( '=' | ':=' ) '<-' )=> expression ( '=' | ':=' ) '<-' relational | '<-' relational );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA55_0 = input.LA(1);

                         
                        int index55_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA55_0==116) && (synpred20_GoSource())) {s = 1;}

                        else if ( (LA55_0==82) ) {s = 2;}

                        else if ( (LA55_0==68) && (synpred20_GoSource())) {s = 3;}

                        else if ( (LA55_0==100) && (synpred20_GoSource())) {s = 4;}

                        else if ( (LA55_0==101) && (synpred20_GoSource())) {s = 5;}

                        else if ( (LA55_0==102) && (synpred20_GoSource())) {s = 6;}

                        else if ( (LA55_0==103) && (synpred20_GoSource())) {s = 7;}

                        else if ( (LA55_0==APPEND) && (synpred20_GoSource())) {s = 8;}

                        else if ( (LA55_0==String_Lit||(LA55_0>=Integer && LA55_0<=Char_Lit)) && (synpred20_GoSource())) {s = 9;}

                        else if ( (LA55_0==STRUCT) && (synpred20_GoSource())) {s = 10;}

                        else if ( (LA55_0==99) && (synpred20_GoSource())) {s = 11;}

                        else if ( (LA55_0==119) && (synpred20_GoSource())) {s = 12;}

                        else if ( (LA55_0==CAP) && (synpred20_GoSource())) {s = 13;}

                        else if ( (LA55_0==CLOSE) && (synpred20_GoSource())) {s = 14;}

                        else if ( (LA55_0==CLOSED) && (synpred20_GoSource())) {s = 15;}

                        else if ( (LA55_0==CMPLX) && (synpred20_GoSource())) {s = 16;}

                        else if ( (LA55_0==COPY) && (synpred20_GoSource())) {s = 17;}

                        else if ( (LA55_0==IMAG) && (synpred20_GoSource())) {s = 18;}

                        else if ( (LA55_0==LEN) && (synpred20_GoSource())) {s = 19;}

                        else if ( (LA55_0==MAKE) && (synpred20_GoSource())) {s = 20;}

                        else if ( (LA55_0==PANIC) && (synpred20_GoSource())) {s = 21;}

                        else if ( (LA55_0==PRINT) && (synpred20_GoSource())) {s = 22;}

                        else if ( (LA55_0==PRINTLN) && (synpred20_GoSource())) {s = 23;}

                        else if ( (LA55_0==REAL) && (synpred20_GoSource())) {s = 24;}

                        else if ( (LA55_0==98) && (synpred20_GoSource())) {s = 25;}

                        else if ( (LA55_0==Identifier) && (synpred20_GoSource())) {s = 26;}

                        else if ( (LA55_0==67) && (synpred20_GoSource())) {s = 27;}

                        else if ( (LA55_0==64) && (synpred20_GoSource())) {s = 28;}

                        else if ( (LA55_0==INTERFACE) && (synpred20_GoSource())) {s = 29;}

                        else if ( (LA55_0==120) && (synpred20_GoSource())) {s = 30;}

                         
                        input.seek(index55_0);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA55_2 = input.LA(1);

                         
                        int index55_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred20_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index55_2);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 55, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA56_eotS =
        "\43\uffff";
    static final String DFA56_eofS =
        "\43\uffff";
    static final String DFA56_minS =
        "\1\4\36\0\4\uffff";
    static final String DFA56_maxS =
        "\1\170\36\0\4\uffff";
    static final String DFA56_acceptS =
        "\37\uffff\1\1\1\4\1\2\1\3";
    static final String DFA56_specialS =
        "\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1"+
        "\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30\1\31\1\32"+
        "\1\33\1\34\1\35\1\36\4\uffff}>";
    static final String[] DFA56_transitionS = {
            "\1\37\1\11\4\uffff\6\11\1\32\1\10\1\15\1\16\1\17\1\20\1\21\1"+
            "\22\1\23\1\24\1\25\1\26\1\27\1\30\2\uffff\1\12\1\35\36\uffff"+
            "\1\34\2\uffff\1\33\1\3\2\uffff\1\40\12\uffff\1\2\17\uffff\1"+
            "\31\1\13\1\4\1\5\1\6\1\7\14\uffff\1\1\2\uffff\1\14\1\36",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA56_eot = DFA.unpackEncodedString(DFA56_eotS);
    static final short[] DFA56_eof = DFA.unpackEncodedString(DFA56_eofS);
    static final char[] DFA56_min = DFA.unpackEncodedStringToUnsignedChars(DFA56_minS);
    static final char[] DFA56_max = DFA.unpackEncodedStringToUnsignedChars(DFA56_maxS);
    static final short[] DFA56_accept = DFA.unpackEncodedString(DFA56_acceptS);
    static final short[] DFA56_special = DFA.unpackEncodedString(DFA56_specialS);
    static final short[][] DFA56_transition;

    static {
        int numStates = DFA56_transitionS.length;
        DFA56_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA56_transition[i] = DFA.unpackEncodedString(DFA56_transitionS[i]);
        }
    }

    class DFA56 extends DFA {

        public DFA56(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 56;
            this.eot = DFA56_eot;
            this.eof = DFA56_eof;
            this.min = DFA56_min;
            this.max = DFA56_max;
            this.accept = DFA56_accept;
            this.special = DFA56_special;
            this.transition = DFA56_transition;
        }
        public String getDescription() {
            return "631:3: ( ( initStmt SEMI )=> forClause | ( expression '{' )=> condition | rangeClause )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA56_0 = input.LA(1);

                         
                        int index56_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA56_0==116) ) {s = 1;}

                        else if ( (LA56_0==82) ) {s = 2;}

                        else if ( (LA56_0==68) ) {s = 3;}

                        else if ( (LA56_0==100) ) {s = 4;}

                        else if ( (LA56_0==101) ) {s = 5;}

                        else if ( (LA56_0==102) ) {s = 6;}

                        else if ( (LA56_0==103) ) {s = 7;}

                        else if ( (LA56_0==APPEND) ) {s = 8;}

                        else if ( (LA56_0==String_Lit||(LA56_0>=Integer && LA56_0<=Char_Lit)) ) {s = 9;}

                        else if ( (LA56_0==STRUCT) ) {s = 10;}

                        else if ( (LA56_0==99) ) {s = 11;}

                        else if ( (LA56_0==119) ) {s = 12;}

                        else if ( (LA56_0==CAP) ) {s = 13;}

                        else if ( (LA56_0==CLOSE) ) {s = 14;}

                        else if ( (LA56_0==CLOSED) ) {s = 15;}

                        else if ( (LA56_0==CMPLX) ) {s = 16;}

                        else if ( (LA56_0==COPY) ) {s = 17;}

                        else if ( (LA56_0==IMAG) ) {s = 18;}

                        else if ( (LA56_0==LEN) ) {s = 19;}

                        else if ( (LA56_0==MAKE) ) {s = 20;}

                        else if ( (LA56_0==PANIC) ) {s = 21;}

                        else if ( (LA56_0==PRINT) ) {s = 22;}

                        else if ( (LA56_0==PRINTLN) ) {s = 23;}

                        else if ( (LA56_0==REAL) ) {s = 24;}

                        else if ( (LA56_0==98) ) {s = 25;}

                        else if ( (LA56_0==Identifier) ) {s = 26;}

                        else if ( (LA56_0==67) ) {s = 27;}

                        else if ( (LA56_0==64) ) {s = 28;}

                        else if ( (LA56_0==INTERFACE) ) {s = 29;}

                        else if ( (LA56_0==120) ) {s = 30;}

                        else if ( (LA56_0==SEMI) && (synpred21_GoSource())) {s = 31;}

                        else if ( (LA56_0==71) ) {s = 32;}

                         
                        input.seek(index56_0);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA56_1 = input.LA(1);

                         
                        int index56_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_1);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA56_2 = input.LA(1);

                         
                        int index56_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_2);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA56_3 = input.LA(1);

                         
                        int index56_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_3);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA56_4 = input.LA(1);

                         
                        int index56_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_4);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA56_5 = input.LA(1);

                         
                        int index56_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_5);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA56_6 = input.LA(1);

                         
                        int index56_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_6);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA56_7 = input.LA(1);

                         
                        int index56_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_7);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA56_8 = input.LA(1);

                         
                        int index56_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_8);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA56_9 = input.LA(1);

                         
                        int index56_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_9);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA56_10 = input.LA(1);

                         
                        int index56_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_10);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA56_11 = input.LA(1);

                         
                        int index56_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_11);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA56_12 = input.LA(1);

                         
                        int index56_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_12);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA56_13 = input.LA(1);

                         
                        int index56_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_13);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA56_14 = input.LA(1);

                         
                        int index56_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_14);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA56_15 = input.LA(1);

                         
                        int index56_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_15);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA56_16 = input.LA(1);

                         
                        int index56_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_16);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA56_17 = input.LA(1);

                         
                        int index56_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_17);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA56_18 = input.LA(1);

                         
                        int index56_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_18);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA56_19 = input.LA(1);

                         
                        int index56_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_19);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA56_20 = input.LA(1);

                         
                        int index56_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_20);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA56_21 = input.LA(1);

                         
                        int index56_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_21);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA56_22 = input.LA(1);

                         
                        int index56_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_22);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA56_23 = input.LA(1);

                         
                        int index56_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_23);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA56_24 = input.LA(1);

                         
                        int index56_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_24);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA56_25 = input.LA(1);

                         
                        int index56_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_25);
                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA56_26 = input.LA(1);

                         
                        int index56_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_26);
                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA56_27 = input.LA(1);

                         
                        int index56_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_27);
                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA56_28 = input.LA(1);

                         
                        int index56_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_28);
                        if ( s>=0 ) return s;
                        break;
                    case 29 : 
                        int LA56_29 = input.LA(1);

                         
                        int index56_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_29);
                        if ( s>=0 ) return s;
                        break;
                    case 30 : 
                        int LA56_30 = input.LA(1);

                         
                        int index56_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_GoSource()) ) {s = 31;}

                        else if ( (synpred22_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index56_30);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 56, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA57_eotS =
        "\41\uffff";
    static final String DFA57_eofS =
        "\41\uffff";
    static final String DFA57_minS =
        "\1\4\36\uffff\1\0\1\uffff";
    static final String DFA57_maxS =
        "\1\170\36\uffff\1\0\1\uffff";
    static final String DFA57_acceptS =
        "\1\uffff\36\1\1\uffff\1\2";
    static final String DFA57_specialS =
        "\1\0\36\uffff\1\1\1\uffff}>";
    static final String[] DFA57_transitionS = {
            "\1\37\1\11\4\uffff\6\11\1\32\1\10\1\15\1\16\1\17\1\20\1\21\1"+
            "\22\1\23\1\24\1\25\1\26\1\27\1\30\2\uffff\1\12\1\35\36\uffff"+
            "\1\34\2\uffff\1\33\1\3\15\uffff\1\2\17\uffff\1\31\1\13\1\4\1"+
            "\5\1\6\1\7\14\uffff\1\1\2\uffff\1\14\1\36",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            ""
    };

    static final short[] DFA57_eot = DFA.unpackEncodedString(DFA57_eotS);
    static final short[] DFA57_eof = DFA.unpackEncodedString(DFA57_eofS);
    static final char[] DFA57_min = DFA.unpackEncodedStringToUnsignedChars(DFA57_minS);
    static final char[] DFA57_max = DFA.unpackEncodedStringToUnsignedChars(DFA57_maxS);
    static final short[] DFA57_accept = DFA.unpackEncodedString(DFA57_acceptS);
    static final short[] DFA57_special = DFA.unpackEncodedString(DFA57_specialS);
    static final short[][] DFA57_transition;

    static {
        int numStates = DFA57_transitionS.length;
        DFA57_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA57_transition[i] = DFA.unpackEncodedString(DFA57_transitionS[i]);
        }
    }

    class DFA57 extends DFA {

        public DFA57(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 57;
            this.eot = DFA57_eot;
            this.eof = DFA57_eof;
            this.min = DFA57_min;
            this.max = DFA57_max;
            this.accept = DFA57_accept;
            this.special = DFA57_special;
            this.transition = DFA57_transition;
        }
        public String getDescription() {
            return "646:3: ( ( initStmt SEMI )=> initStmt SEMI | SEMI )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA57_0 = input.LA(1);

                         
                        int index57_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA57_0==116) && (synpred23_GoSource())) {s = 1;}

                        else if ( (LA57_0==82) && (synpred23_GoSource())) {s = 2;}

                        else if ( (LA57_0==68) && (synpred23_GoSource())) {s = 3;}

                        else if ( (LA57_0==100) && (synpred23_GoSource())) {s = 4;}

                        else if ( (LA57_0==101) && (synpred23_GoSource())) {s = 5;}

                        else if ( (LA57_0==102) && (synpred23_GoSource())) {s = 6;}

                        else if ( (LA57_0==103) && (synpred23_GoSource())) {s = 7;}

                        else if ( (LA57_0==APPEND) && (synpred23_GoSource())) {s = 8;}

                        else if ( (LA57_0==String_Lit||(LA57_0>=Integer && LA57_0<=Char_Lit)) && (synpred23_GoSource())) {s = 9;}

                        else if ( (LA57_0==STRUCT) && (synpred23_GoSource())) {s = 10;}

                        else if ( (LA57_0==99) && (synpred23_GoSource())) {s = 11;}

                        else if ( (LA57_0==119) && (synpred23_GoSource())) {s = 12;}

                        else if ( (LA57_0==CAP) && (synpred23_GoSource())) {s = 13;}

                        else if ( (LA57_0==CLOSE) && (synpred23_GoSource())) {s = 14;}

                        else if ( (LA57_0==CLOSED) && (synpred23_GoSource())) {s = 15;}

                        else if ( (LA57_0==CMPLX) && (synpred23_GoSource())) {s = 16;}

                        else if ( (LA57_0==COPY) && (synpred23_GoSource())) {s = 17;}

                        else if ( (LA57_0==IMAG) && (synpred23_GoSource())) {s = 18;}

                        else if ( (LA57_0==LEN) && (synpred23_GoSource())) {s = 19;}

                        else if ( (LA57_0==MAKE) && (synpred23_GoSource())) {s = 20;}

                        else if ( (LA57_0==PANIC) && (synpred23_GoSource())) {s = 21;}

                        else if ( (LA57_0==PRINT) && (synpred23_GoSource())) {s = 22;}

                        else if ( (LA57_0==PRINTLN) && (synpred23_GoSource())) {s = 23;}

                        else if ( (LA57_0==REAL) && (synpred23_GoSource())) {s = 24;}

                        else if ( (LA57_0==98) && (synpred23_GoSource())) {s = 25;}

                        else if ( (LA57_0==Identifier) && (synpred23_GoSource())) {s = 26;}

                        else if ( (LA57_0==67) && (synpred23_GoSource())) {s = 27;}

                        else if ( (LA57_0==64) && (synpred23_GoSource())) {s = 28;}

                        else if ( (LA57_0==INTERFACE) && (synpred23_GoSource())) {s = 29;}

                        else if ( (LA57_0==120) && (synpred23_GoSource())) {s = 30;}

                        else if ( (LA57_0==SEMI) ) {s = 31;}

                         
                        input.seek(index57_0);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA57_31 = input.LA(1);

                         
                        int index57_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred23_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index57_31);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 57, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA58_eotS =
        "\40\uffff";
    static final String DFA58_eofS =
        "\40\uffff";
    static final String DFA58_minS =
        "\1\4\37\uffff";
    static final String DFA58_maxS =
        "\1\170\37\uffff";
    static final String DFA58_acceptS =
        "\1\uffff\36\1\1\2";
    static final String DFA58_specialS =
        "\1\0\37\uffff}>";
    static final String[] DFA58_transitionS = {
            "\1\37\1\11\4\uffff\6\11\1\32\1\10\1\15\1\16\1\17\1\20\1\21\1"+
            "\22\1\23\1\24\1\25\1\26\1\27\1\30\2\uffff\1\12\1\35\36\uffff"+
            "\1\34\2\uffff\1\33\1\3\15\uffff\1\2\17\uffff\1\31\1\13\1\4\1"+
            "\5\1\6\1\7\14\uffff\1\1\2\uffff\1\14\1\36",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA58_eot = DFA.unpackEncodedString(DFA58_eotS);
    static final short[] DFA58_eof = DFA.unpackEncodedString(DFA58_eofS);
    static final char[] DFA58_min = DFA.unpackEncodedStringToUnsignedChars(DFA58_minS);
    static final char[] DFA58_max = DFA.unpackEncodedStringToUnsignedChars(DFA58_maxS);
    static final short[] DFA58_accept = DFA.unpackEncodedString(DFA58_acceptS);
    static final short[] DFA58_special = DFA.unpackEncodedString(DFA58_specialS);
    static final short[][] DFA58_transition;

    static {
        int numStates = DFA58_transitionS.length;
        DFA58_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA58_transition[i] = DFA.unpackEncodedString(DFA58_transitionS[i]);
        }
    }

    class DFA58 extends DFA {

        public DFA58(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 58;
            this.eot = DFA58_eot;
            this.eof = DFA58_eof;
            this.min = DFA58_min;
            this.max = DFA58_max;
            this.accept = DFA58_accept;
            this.special = DFA58_special;
            this.transition = DFA58_transition;
        }
        public String getDescription() {
            return "650:3: ( ( condition SEMI )=> condition SEMI | SEMI )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA58_0 = input.LA(1);

                         
                        int index58_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA58_0==116) && (synpred24_GoSource())) {s = 1;}

                        else if ( (LA58_0==82) && (synpred24_GoSource())) {s = 2;}

                        else if ( (LA58_0==68) && (synpred24_GoSource())) {s = 3;}

                        else if ( (LA58_0==100) && (synpred24_GoSource())) {s = 4;}

                        else if ( (LA58_0==101) && (synpred24_GoSource())) {s = 5;}

                        else if ( (LA58_0==102) && (synpred24_GoSource())) {s = 6;}

                        else if ( (LA58_0==103) && (synpred24_GoSource())) {s = 7;}

                        else if ( (LA58_0==APPEND) && (synpred24_GoSource())) {s = 8;}

                        else if ( (LA58_0==String_Lit||(LA58_0>=Integer && LA58_0<=Char_Lit)) && (synpred24_GoSource())) {s = 9;}

                        else if ( (LA58_0==STRUCT) && (synpred24_GoSource())) {s = 10;}

                        else if ( (LA58_0==99) && (synpred24_GoSource())) {s = 11;}

                        else if ( (LA58_0==119) && (synpred24_GoSource())) {s = 12;}

                        else if ( (LA58_0==CAP) && (synpred24_GoSource())) {s = 13;}

                        else if ( (LA58_0==CLOSE) && (synpred24_GoSource())) {s = 14;}

                        else if ( (LA58_0==CLOSED) && (synpred24_GoSource())) {s = 15;}

                        else if ( (LA58_0==CMPLX) && (synpred24_GoSource())) {s = 16;}

                        else if ( (LA58_0==COPY) && (synpred24_GoSource())) {s = 17;}

                        else if ( (LA58_0==IMAG) && (synpred24_GoSource())) {s = 18;}

                        else if ( (LA58_0==LEN) && (synpred24_GoSource())) {s = 19;}

                        else if ( (LA58_0==MAKE) && (synpred24_GoSource())) {s = 20;}

                        else if ( (LA58_0==PANIC) && (synpred24_GoSource())) {s = 21;}

                        else if ( (LA58_0==PRINT) && (synpred24_GoSource())) {s = 22;}

                        else if ( (LA58_0==PRINTLN) && (synpred24_GoSource())) {s = 23;}

                        else if ( (LA58_0==REAL) && (synpred24_GoSource())) {s = 24;}

                        else if ( (LA58_0==98) && (synpred24_GoSource())) {s = 25;}

                        else if ( (LA58_0==Identifier) && (synpred24_GoSource())) {s = 26;}

                        else if ( (LA58_0==67) && (synpred24_GoSource())) {s = 27;}

                        else if ( (LA58_0==64) && (synpred24_GoSource())) {s = 28;}

                        else if ( (LA58_0==INTERFACE) && (synpred24_GoSource())) {s = 29;}

                        else if ( (LA58_0==120) && (synpred24_GoSource())) {s = 30;}

                        else if ( (LA58_0==SEMI) ) {s = 31;}

                         
                        input.seek(index58_0);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 58, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA64_eotS =
        "\32\uffff";
    static final String DFA64_eofS =
        "\32\uffff";
    static final String DFA64_minS =
        "\1\20\31\uffff";
    static final String DFA64_maxS =
        "\1\170\31\uffff";
    static final String DFA64_acceptS =
        "\1\uffff\30\1\1\2";
    static final String DFA64_specialS =
        "\1\0\31\uffff}>";
    static final String[] DFA64_transitionS = {
            "\1\30\1\12\1\13\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24"+
            "\1\25\1\26\2\uffff\1\3\1\5\36\uffff\1\1\1\uffff\1\31\1\4\1\7"+
            "\15\uffff\1\10\17\uffff\1\27\1\2\23\uffff\1\6\1\11",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA64_eot = DFA.unpackEncodedString(DFA64_eotS);
    static final short[] DFA64_eof = DFA.unpackEncodedString(DFA64_eofS);
    static final char[] DFA64_min = DFA.unpackEncodedStringToUnsignedChars(DFA64_minS);
    static final char[] DFA64_max = DFA.unpackEncodedStringToUnsignedChars(DFA64_maxS);
    static final short[] DFA64_accept = DFA.unpackEncodedString(DFA64_acceptS);
    static final short[] DFA64_special = DFA.unpackEncodedString(DFA64_specialS);
    static final short[][] DFA64_transition;

    static {
        int numStates = DFA64_transitionS.length;
        DFA64_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA64_transition[i] = DFA.unpackEncodedString(DFA64_transitionS[i]);
        }
    }

    class DFA64 extends DFA {

        public DFA64(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 64;
            this.eot = DFA64_eot;
            this.eof = DFA64_eof;
            this.min = DFA64_min;
            this.max = DFA64_max;
            this.accept = DFA64_accept;
            this.special = DFA64_special;
            this.transition = DFA64_transition;
        }
        public String getDescription() {
            return "739:5: ( ( ( type '=' )=> ( type '=' ) ) | '=' )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA64_0 = input.LA(1);

                         
                        int index64_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA64_0==64) && (synpred26_GoSource())) {s = 1;}

                        else if ( (LA64_0==99) && (synpred26_GoSource())) {s = 2;}

                        else if ( (LA64_0==STRUCT) && (synpred26_GoSource())) {s = 3;}

                        else if ( (LA64_0==67) && (synpred26_GoSource())) {s = 4;}

                        else if ( (LA64_0==INTERFACE) && (synpred26_GoSource())) {s = 5;}

                        else if ( (LA64_0==119) && (synpred26_GoSource())) {s = 6;}

                        else if ( (LA64_0==68) && (synpred26_GoSource())) {s = 7;}

                        else if ( (LA64_0==82) && (synpred26_GoSource())) {s = 8;}

                        else if ( (LA64_0==120) && (synpred26_GoSource())) {s = 9;}

                        else if ( (LA64_0==APPEND) && (synpred26_GoSource())) {s = 10;}

                        else if ( (LA64_0==CAP) && (synpred26_GoSource())) {s = 11;}

                        else if ( (LA64_0==CLOSE) && (synpred26_GoSource())) {s = 12;}

                        else if ( (LA64_0==CLOSED) && (synpred26_GoSource())) {s = 13;}

                        else if ( (LA64_0==CMPLX) && (synpred26_GoSource())) {s = 14;}

                        else if ( (LA64_0==COPY) && (synpred26_GoSource())) {s = 15;}

                        else if ( (LA64_0==IMAG) && (synpred26_GoSource())) {s = 16;}

                        else if ( (LA64_0==LEN) && (synpred26_GoSource())) {s = 17;}

                        else if ( (LA64_0==MAKE) && (synpred26_GoSource())) {s = 18;}

                        else if ( (LA64_0==PANIC) && (synpred26_GoSource())) {s = 19;}

                        else if ( (LA64_0==PRINT) && (synpred26_GoSource())) {s = 20;}

                        else if ( (LA64_0==PRINTLN) && (synpred26_GoSource())) {s = 21;}

                        else if ( (LA64_0==REAL) && (synpred26_GoSource())) {s = 22;}

                        else if ( (LA64_0==98) && (synpred26_GoSource())) {s = 23;}

                        else if ( (LA64_0==Identifier) && (synpred26_GoSource())) {s = 24;}

                        else if ( (LA64_0==66) ) {s = 25;}

                         
                        input.seek(index64_0);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 64, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA68_eotS =
        "\33\uffff";
    static final String DFA68_eofS =
        "\33\uffff";
    static final String DFA68_minS =
        "\1\5\1\0\1\uffff\23\0\5\uffff";
    static final String DFA68_maxS =
        "\1\170\1\0\1\uffff\23\0\5\uffff";
    static final String DFA68_acceptS =
        "\2\uffff\1\2\23\uffff\4\3\1\1";
    static final String DFA68_specialS =
        "\1\0\1\1\1\uffff\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\5\uffff}>";
    static final String[] DFA68_transitionS = {
            "\1\2\4\uffff\6\2\1\23\1\1\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
            "\15\1\16\1\17\1\20\1\21\2\uffff\1\3\1\26\36\uffff\1\25\2\uffff"+
            "\1\24\1\27\15\uffff\1\30\17\uffff\1\22\1\4\23\uffff\1\5\1\31",
            "\1\uffff",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA68_eot = DFA.unpackEncodedString(DFA68_eotS);
    static final short[] DFA68_eof = DFA.unpackEncodedString(DFA68_eofS);
    static final char[] DFA68_min = DFA.unpackEncodedStringToUnsignedChars(DFA68_minS);
    static final char[] DFA68_max = DFA.unpackEncodedStringToUnsignedChars(DFA68_maxS);
    static final short[] DFA68_accept = DFA.unpackEncodedString(DFA68_acceptS);
    static final short[] DFA68_special = DFA.unpackEncodedString(DFA68_specialS);
    static final short[][] DFA68_transition;

    static {
        int numStates = DFA68_transitionS.length;
        DFA68_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA68_transition[i] = DFA.unpackEncodedString(DFA68_transitionS[i]);
        }
    }

    class DFA68 extends DFA {

        public DFA68(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 68;
            this.eot = DFA68_eot;
            this.eof = DFA68_eof;
            this.min = DFA68_min;
            this.max = DFA68_max;
            this.accept = DFA68_accept;
            this.special = DFA68_special;
            this.transition = DFA68_transition;
        }
        public String getDescription() {
            return "789:1: base returns [List<Type> types] : ( ( builtinCall )=> builtinCall | ( operand )=> operand | ( conversion )=> conversion );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA68_0 = input.LA(1);

                         
                        int index68_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA68_0==APPEND) ) {s = 1;}

                        else if ( (LA68_0==String_Lit||(LA68_0>=Integer && LA68_0<=Char_Lit)) && (synpred28_GoSource())) {s = 2;}

                        else if ( (LA68_0==STRUCT) ) {s = 3;}

                        else if ( (LA68_0==99) ) {s = 4;}

                        else if ( (LA68_0==119) ) {s = 5;}

                        else if ( (LA68_0==CAP) ) {s = 6;}

                        else if ( (LA68_0==CLOSE) ) {s = 7;}

                        else if ( (LA68_0==CLOSED) ) {s = 8;}

                        else if ( (LA68_0==CMPLX) ) {s = 9;}

                        else if ( (LA68_0==COPY) ) {s = 10;}

                        else if ( (LA68_0==IMAG) ) {s = 11;}

                        else if ( (LA68_0==LEN) ) {s = 12;}

                        else if ( (LA68_0==MAKE) ) {s = 13;}

                        else if ( (LA68_0==PANIC) ) {s = 14;}

                        else if ( (LA68_0==PRINT) ) {s = 15;}

                        else if ( (LA68_0==PRINTLN) ) {s = 16;}

                        else if ( (LA68_0==REAL) ) {s = 17;}

                        else if ( (LA68_0==98) ) {s = 18;}

                        else if ( (LA68_0==Identifier) ) {s = 19;}

                        else if ( (LA68_0==67) ) {s = 20;}

                        else if ( (LA68_0==64) ) {s = 21;}

                        else if ( (LA68_0==INTERFACE) && (synpred29_GoSource())) {s = 22;}

                        else if ( (LA68_0==68) && (synpred29_GoSource())) {s = 23;}

                        else if ( (LA68_0==82) && (synpred29_GoSource())) {s = 24;}

                        else if ( (LA68_0==120) && (synpred29_GoSource())) {s = 25;}

                         
                        input.seek(index68_0);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA68_1 = input.LA(1);

                         
                        int index68_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_GoSource()) ) {s = 26;}

                        else if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_1);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA68_3 = input.LA(1);

                         
                        int index68_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA68_4 = input.LA(1);

                         
                        int index68_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA68_5 = input.LA(1);

                         
                        int index68_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA68_6 = input.LA(1);

                         
                        int index68_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_GoSource()) ) {s = 26;}

                        else if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA68_7 = input.LA(1);

                         
                        int index68_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_GoSource()) ) {s = 26;}

                        else if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA68_8 = input.LA(1);

                         
                        int index68_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_GoSource()) ) {s = 26;}

                        else if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA68_9 = input.LA(1);

                         
                        int index68_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_GoSource()) ) {s = 26;}

                        else if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA68_10 = input.LA(1);

                         
                        int index68_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_GoSource()) ) {s = 26;}

                        else if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA68_11 = input.LA(1);

                         
                        int index68_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_GoSource()) ) {s = 26;}

                        else if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA68_12 = input.LA(1);

                         
                        int index68_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_GoSource()) ) {s = 26;}

                        else if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA68_13 = input.LA(1);

                         
                        int index68_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_GoSource()) ) {s = 26;}

                        else if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA68_14 = input.LA(1);

                         
                        int index68_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_GoSource()) ) {s = 26;}

                        else if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_14);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA68_15 = input.LA(1);

                         
                        int index68_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_GoSource()) ) {s = 26;}

                        else if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_15);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA68_16 = input.LA(1);

                         
                        int index68_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_GoSource()) ) {s = 26;}

                        else if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_16);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA68_17 = input.LA(1);

                         
                        int index68_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_GoSource()) ) {s = 26;}

                        else if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_17);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA68_18 = input.LA(1);

                         
                        int index68_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_GoSource()) ) {s = 26;}

                        else if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_18);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA68_19 = input.LA(1);

                         
                        int index68_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_19);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA68_20 = input.LA(1);

                         
                        int index68_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_20);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA68_21 = input.LA(1);

                         
                        int index68_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred28_GoSource()) ) {s = 2;}

                        else if ( (synpred29_GoSource()) ) {s = 25;}

                         
                        input.seek(index68_21);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 68, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA74_eotS =
        "\41\uffff";
    static final String DFA74_eofS =
        "\41\uffff";
    static final String DFA74_minS =
        "\1\5\30\0\10\uffff";
    static final String DFA74_maxS =
        "\1\170\30\0\10\uffff";
    static final String DFA74_acceptS =
        "\31\uffff\6\2\1\1\1\3";
    static final String DFA74_specialS =
        "\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1"+
        "\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30\10\uffff}>";
    static final String[] DFA74_transitionS = {
            "\1\36\4\uffff\6\36\1\30\1\12\1\13\1\14\1\15\1\16\1\17\1\20\1"+
            "\21\1\22\1\23\1\24\1\25\1\26\2\uffff\1\3\1\5\36\uffff\1\1\2"+
            "\uffff\1\4\1\7\15\uffff\1\10\17\uffff\1\27\1\2\1\32\1\33\1\34"+
            "\1\35\14\uffff\1\31\2\uffff\1\6\1\11",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA74_eot = DFA.unpackEncodedString(DFA74_eotS);
    static final short[] DFA74_eof = DFA.unpackEncodedString(DFA74_eofS);
    static final char[] DFA74_min = DFA.unpackEncodedStringToUnsignedChars(DFA74_minS);
    static final char[] DFA74_max = DFA.unpackEncodedStringToUnsignedChars(DFA74_maxS);
    static final short[] DFA74_accept = DFA.unpackEncodedString(DFA74_acceptS);
    static final short[] DFA74_special = DFA.unpackEncodedString(DFA74_specialS);
    static final short[][] DFA74_transition;

    static {
        int numStates = DFA74_transitionS.length;
        DFA74_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA74_transition[i] = DFA.unpackEncodedString(DFA74_transitionS[i]);
        }
    }

    class DFA74 extends DFA {

        public DFA74(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 74;
            this.eot = DFA74_eot;
            this.eof = DFA74_eof;
            this.min = DFA74_min;
            this.max = DFA74_max;
            this.accept = DFA74_accept;
            this.special = DFA74_special;
            this.transition = DFA74_transition;
        }
        public String getDescription() {
            return "852:3: ( ( type ',' expressionList )=> type ',' expressionList | ( expressionList )=> expressionList | type )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA74_0 = input.LA(1);

                         
                        int index74_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA74_0==64) ) {s = 1;}

                        else if ( (LA74_0==99) ) {s = 2;}

                        else if ( (LA74_0==STRUCT) ) {s = 3;}

                        else if ( (LA74_0==67) ) {s = 4;}

                        else if ( (LA74_0==INTERFACE) ) {s = 5;}

                        else if ( (LA74_0==119) ) {s = 6;}

                        else if ( (LA74_0==68) ) {s = 7;}

                        else if ( (LA74_0==82) ) {s = 8;}

                        else if ( (LA74_0==120) ) {s = 9;}

                        else if ( (LA74_0==APPEND) ) {s = 10;}

                        else if ( (LA74_0==CAP) ) {s = 11;}

                        else if ( (LA74_0==CLOSE) ) {s = 12;}

                        else if ( (LA74_0==CLOSED) ) {s = 13;}

                        else if ( (LA74_0==CMPLX) ) {s = 14;}

                        else if ( (LA74_0==COPY) ) {s = 15;}

                        else if ( (LA74_0==IMAG) ) {s = 16;}

                        else if ( (LA74_0==LEN) ) {s = 17;}

                        else if ( (LA74_0==MAKE) ) {s = 18;}

                        else if ( (LA74_0==PANIC) ) {s = 19;}

                        else if ( (LA74_0==PRINT) ) {s = 20;}

                        else if ( (LA74_0==PRINTLN) ) {s = 21;}

                        else if ( (LA74_0==REAL) ) {s = 22;}

                        else if ( (LA74_0==98) ) {s = 23;}

                        else if ( (LA74_0==Identifier) ) {s = 24;}

                        else if ( (LA74_0==116) && (synpred37_GoSource())) {s = 25;}

                        else if ( (LA74_0==100) && (synpred37_GoSource())) {s = 26;}

                        else if ( (LA74_0==101) && (synpred37_GoSource())) {s = 27;}

                        else if ( (LA74_0==102) && (synpred37_GoSource())) {s = 28;}

                        else if ( (LA74_0==103) && (synpred37_GoSource())) {s = 29;}

                        else if ( (LA74_0==String_Lit||(LA74_0>=Integer && LA74_0<=Char_Lit)) && (synpred37_GoSource())) {s = 30;}

                         
                        input.seek(index74_0);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA74_1 = input.LA(1);

                         
                        int index74_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_1);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA74_2 = input.LA(1);

                         
                        int index74_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_2);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA74_3 = input.LA(1);

                         
                        int index74_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_3);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA74_4 = input.LA(1);

                         
                        int index74_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_4);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA74_5 = input.LA(1);

                         
                        int index74_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_5);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA74_6 = input.LA(1);

                         
                        int index74_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_6);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA74_7 = input.LA(1);

                         
                        int index74_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_7);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA74_8 = input.LA(1);

                         
                        int index74_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_8);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA74_9 = input.LA(1);

                         
                        int index74_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_9);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA74_10 = input.LA(1);

                         
                        int index74_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_10);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA74_11 = input.LA(1);

                         
                        int index74_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_11);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA74_12 = input.LA(1);

                         
                        int index74_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_12);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA74_13 = input.LA(1);

                         
                        int index74_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_13);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA74_14 = input.LA(1);

                         
                        int index74_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_14);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA74_15 = input.LA(1);

                         
                        int index74_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_15);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA74_16 = input.LA(1);

                         
                        int index74_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_16);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA74_17 = input.LA(1);

                         
                        int index74_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_17);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA74_18 = input.LA(1);

                         
                        int index74_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_18);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA74_19 = input.LA(1);

                         
                        int index74_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_19);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA74_20 = input.LA(1);

                         
                        int index74_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_20);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA74_21 = input.LA(1);

                         
                        int index74_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_21);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA74_22 = input.LA(1);

                         
                        int index74_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_22);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA74_23 = input.LA(1);

                         
                        int index74_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_23);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA74_24 = input.LA(1);

                         
                        int index74_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_GoSource()) ) {s = 31;}

                        else if ( (synpred37_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index74_24);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 74, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA84_eotS =
        "\40\uffff";
    static final String DFA84_eofS =
        "\40\uffff";
    static final String DFA84_minS =
        "\1\5\2\0\35\uffff";
    static final String DFA84_maxS =
        "\1\170\2\0\35\uffff";
    static final String DFA84_acceptS =
        "\3\uffff\1\3\32\uffff\1\1\1\2";
    static final String DFA84_specialS =
        "\1\uffff\1\0\1\1\35\uffff}>";
    static final String[] DFA84_transitionS = {
            "\1\3\4\uffff\24\3\2\uffff\2\3\36\uffff\1\3\2\uffff\1\3\1\2\15"+
            "\uffff\1\1\17\uffff\6\3\17\uffff\2\3",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA84_eot = DFA.unpackEncodedString(DFA84_eotS);
    static final short[] DFA84_eof = DFA.unpackEncodedString(DFA84_eofS);
    static final char[] DFA84_min = DFA.unpackEncodedStringToUnsignedChars(DFA84_minS);
    static final char[] DFA84_max = DFA.unpackEncodedStringToUnsignedChars(DFA84_maxS);
    static final short[] DFA84_accept = DFA.unpackEncodedString(DFA84_acceptS);
    static final short[] DFA84_special = DFA.unpackEncodedString(DFA84_specialS);
    static final short[][] DFA84_transition;

    static {
        int numStates = DFA84_transitionS.length;
        DFA84_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA84_transition[i] = DFA.unpackEncodedString(DFA84_transitionS[i]);
        }
    }

    class DFA84 extends DFA {

        public DFA84(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 84;
            this.eot = DFA84_eot;
            this.eof = DFA84_eof;
            this.min = DFA84_min;
            this.max = DFA84_max;
            this.accept = DFA84_accept;
            this.special = DFA84_special;
            this.transition = DFA84_transition;
        }
        public String getDescription() {
            return "897:1: unary returns [List<Type> types] : ( ( '<-' primaryExpr )=> '<-' primaryExpr | ( '*' primaryExpr )=> '*' primaryExpr | ( '+' | '-' | '^' | '&' )* primaryExpr );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA84_1 = input.LA(1);

                         
                        int index84_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred38_GoSource()) ) {s = 30;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index84_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA84_2 = input.LA(1);

                         
                        int index84_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred39_GoSource()) ) {s = 31;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index84_2);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 84, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA96_eotS =
        "\31\uffff";
    static final String DFA96_eofS =
        "\31\uffff";
    static final String DFA96_minS =
        "\1\5\4\uffff\17\0\1\uffff\1\0\3\uffff";
    static final String DFA96_maxS =
        "\1\167\4\uffff\17\0\1\uffff\1\0\3\uffff";
    static final String DFA96_acceptS =
        "\1\uffff\4\1\17\uffff\1\1\1\uffff\1\2\1\4\1\3";
    static final String DFA96_specialS =
        "\1\0\4\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\uffff\1\20\3\uffff}>";
    static final String[] DFA96_transitionS = {
            "\1\1\4\uffff\6\1\1\23\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
            "\15\1\16\1\17\1\20\1\21\2\uffff\1\2\37\uffff\1\25\2\uffff\1"+
            "\24\36\uffff\1\22\1\3\23\uffff\1\4",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "",
            ""
    };

    static final short[] DFA96_eot = DFA.unpackEncodedString(DFA96_eotS);
    static final short[] DFA96_eof = DFA.unpackEncodedString(DFA96_eofS);
    static final char[] DFA96_min = DFA.unpackEncodedStringToUnsignedChars(DFA96_minS);
    static final char[] DFA96_max = DFA.unpackEncodedStringToUnsignedChars(DFA96_maxS);
    static final short[] DFA96_accept = DFA.unpackEncodedString(DFA96_acceptS);
    static final short[] DFA96_special = DFA.unpackEncodedString(DFA96_specialS);
    static final short[][] DFA96_transition;

    static {
        int numStates = DFA96_transitionS.length;
        DFA96_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA96_transition[i] = DFA.unpackEncodedString(DFA96_transitionS[i]);
        }
    }

    class DFA96 extends DFA {

        public DFA96(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 96;
            this.eot = DFA96_eot;
            this.eof = DFA96_eof;
            this.min = DFA96_min;
            this.max = DFA96_max;
            this.accept = DFA96_accept;
            this.special = DFA96_special;
            this.transition = DFA96_transition;
        }
        public String getDescription() {
            return "1010:1: operand returns [List<Type> types] : ( ( literal )=> literal | ( methodExpr )=> methodExpr | '(' expression ( SEMI )? ')' | qualifiedIdent );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA96_0 = input.LA(1);

                         
                        int index96_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA96_0==String_Lit||(LA96_0>=Integer && LA96_0<=Char_Lit)) && (synpred40_GoSource())) {s = 1;}

                        else if ( (LA96_0==STRUCT) && (synpred40_GoSource())) {s = 2;}

                        else if ( (LA96_0==99) && (synpred40_GoSource())) {s = 3;}

                        else if ( (LA96_0==119) && (synpred40_GoSource())) {s = 4;}

                        else if ( (LA96_0==APPEND) ) {s = 5;}

                        else if ( (LA96_0==CAP) ) {s = 6;}

                        else if ( (LA96_0==CLOSE) ) {s = 7;}

                        else if ( (LA96_0==CLOSED) ) {s = 8;}

                        else if ( (LA96_0==CMPLX) ) {s = 9;}

                        else if ( (LA96_0==COPY) ) {s = 10;}

                        else if ( (LA96_0==IMAG) ) {s = 11;}

                        else if ( (LA96_0==LEN) ) {s = 12;}

                        else if ( (LA96_0==MAKE) ) {s = 13;}

                        else if ( (LA96_0==PANIC) ) {s = 14;}

                        else if ( (LA96_0==PRINT) ) {s = 15;}

                        else if ( (LA96_0==PRINTLN) ) {s = 16;}

                        else if ( (LA96_0==REAL) ) {s = 17;}

                        else if ( (LA96_0==98) ) {s = 18;}

                        else if ( (LA96_0==Identifier) ) {s = 19;}

                        else if ( (LA96_0==67) && (synpred40_GoSource())) {s = 20;}

                        else if ( (LA96_0==64) ) {s = 21;}

                         
                        input.seek(index96_0);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA96_5 = input.LA(1);

                         
                        int index96_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_5);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA96_6 = input.LA(1);

                         
                        int index96_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_6);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA96_7 = input.LA(1);

                         
                        int index96_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_7);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA96_8 = input.LA(1);

                         
                        int index96_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_8);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA96_9 = input.LA(1);

                         
                        int index96_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_9);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA96_10 = input.LA(1);

                         
                        int index96_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_10);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA96_11 = input.LA(1);

                         
                        int index96_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_11);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA96_12 = input.LA(1);

                         
                        int index96_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_12);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA96_13 = input.LA(1);

                         
                        int index96_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_13);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA96_14 = input.LA(1);

                         
                        int index96_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_14);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA96_15 = input.LA(1);

                         
                        int index96_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_15);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA96_16 = input.LA(1);

                         
                        int index96_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_16);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA96_17 = input.LA(1);

                         
                        int index96_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_17);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA96_18 = input.LA(1);

                         
                        int index96_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_18);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA96_19 = input.LA(1);

                         
                        int index96_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_GoSource()) ) {s = 20;}

                        else if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index96_19);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA96_21 = input.LA(1);

                         
                        int index96_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred41_GoSource()) ) {s = 22;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index96_21);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 96, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA99_eotS =
        "\25\uffff";
    static final String DFA99_eofS =
        "\25\uffff";
    static final String DFA99_minS =
        "\1\5\24\uffff";
    static final String DFA99_maxS =
        "\1\167\24\uffff";
    static final String DFA99_acceptS =
        "\1\uffff\1\1\22\2\1\3";
    static final String DFA99_specialS =
        "\1\0\24\uffff}>";
    static final String[] DFA99_transitionS = {
            "\1\1\4\uffff\6\1\1\23\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
            "\15\1\16\1\17\1\20\1\21\2\uffff\1\2\42\uffff\1\24\36\uffff\1"+
            "\22\1\3\23\uffff\1\4",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA99_eot = DFA.unpackEncodedString(DFA99_eotS);
    static final short[] DFA99_eof = DFA.unpackEncodedString(DFA99_eofS);
    static final char[] DFA99_min = DFA.unpackEncodedStringToUnsignedChars(DFA99_minS);
    static final char[] DFA99_max = DFA.unpackEncodedStringToUnsignedChars(DFA99_maxS);
    static final short[] DFA99_accept = DFA.unpackEncodedString(DFA99_acceptS);
    static final short[] DFA99_special = DFA.unpackEncodedString(DFA99_specialS);
    static final short[][] DFA99_transition;

    static {
        int numStates = DFA99_transitionS.length;
        DFA99_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA99_transition[i] = DFA.unpackEncodedString(DFA99_transitionS[i]);
        }
    }

    class DFA99 extends DFA {

        public DFA99(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 99;
            this.eot = DFA99_eot;
            this.eof = DFA99_eof;
            this.min = DFA99_min;
            this.max = DFA99_max;
            this.accept = DFA99_accept;
            this.special = DFA99_special;
            this.transition = DFA99_transition;
        }
        public String getDescription() {
            return "1035:1: literal returns [List<Type> types] : ( basicLit | ( compositeLit ~ ( 'else' ) )=> compositeLit | functionLit );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA99_0 = input.LA(1);

                         
                        int index99_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA99_0==String_Lit||(LA99_0>=Integer && LA99_0<=Char_Lit)) ) {s = 1;}

                        else if ( (LA99_0==STRUCT) && (synpred42_GoSource())) {s = 2;}

                        else if ( (LA99_0==99) && (synpred42_GoSource())) {s = 3;}

                        else if ( (LA99_0==119) && (synpred42_GoSource())) {s = 4;}

                        else if ( (LA99_0==APPEND) && (synpred42_GoSource())) {s = 5;}

                        else if ( (LA99_0==CAP) && (synpred42_GoSource())) {s = 6;}

                        else if ( (LA99_0==CLOSE) && (synpred42_GoSource())) {s = 7;}

                        else if ( (LA99_0==CLOSED) && (synpred42_GoSource())) {s = 8;}

                        else if ( (LA99_0==CMPLX) && (synpred42_GoSource())) {s = 9;}

                        else if ( (LA99_0==COPY) && (synpred42_GoSource())) {s = 10;}

                        else if ( (LA99_0==IMAG) && (synpred42_GoSource())) {s = 11;}

                        else if ( (LA99_0==LEN) && (synpred42_GoSource())) {s = 12;}

                        else if ( (LA99_0==MAKE) && (synpred42_GoSource())) {s = 13;}

                        else if ( (LA99_0==PANIC) && (synpred42_GoSource())) {s = 14;}

                        else if ( (LA99_0==PRINT) && (synpred42_GoSource())) {s = 15;}

                        else if ( (LA99_0==PRINTLN) && (synpred42_GoSource())) {s = 16;}

                        else if ( (LA99_0==REAL) && (synpred42_GoSource())) {s = 17;}

                        else if ( (LA99_0==98) && (synpred42_GoSource())) {s = 18;}

                        else if ( (LA99_0==Identifier) && (synpred42_GoSource())) {s = 19;}

                        else if ( (LA99_0==67) ) {s = 20;}

                         
                        input.seek(index99_0);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 99, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA105_eotS =
        "\41\uffff";
    static final String DFA105_eofS =
        "\41\uffff";
    static final String DFA105_minS =
        "\1\5\36\0\2\uffff";
    static final String DFA105_maxS =
        "\1\170\36\0\2\uffff";
    static final String DFA105_acceptS =
        "\37\uffff\1\2\1\1";
    static final String DFA105_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30\1\31"+
        "\1\32\1\33\1\34\1\35\2\uffff}>";
    static final String[] DFA105_transitionS = {
            "\1\27\4\uffff\6\27\1\17\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11"+
            "\1\12\1\13\1\14\1\15\2\uffff\1\30\1\35\36\uffff\1\34\2\uffff"+
            "\1\33\1\22\2\uffff\1\37\12\uffff\1\21\17\uffff\1\16\1\31\1\23"+
            "\1\24\1\25\1\26\14\uffff\1\20\2\uffff\1\32\1\36",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA105_eot = DFA.unpackEncodedString(DFA105_eotS);
    static final short[] DFA105_eof = DFA.unpackEncodedString(DFA105_eofS);
    static final char[] DFA105_min = DFA.unpackEncodedStringToUnsignedChars(DFA105_minS);
    static final char[] DFA105_max = DFA.unpackEncodedStringToUnsignedChars(DFA105_maxS);
    static final short[] DFA105_accept = DFA.unpackEncodedString(DFA105_acceptS);
    static final short[] DFA105_special = DFA.unpackEncodedString(DFA105_specialS);
    static final short[][] DFA105_transition;

    static {
        int numStates = DFA105_transitionS.length;
        DFA105_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA105_transition[i] = DFA.unpackEncodedString(DFA105_transitionS[i]);
        }
    }

    class DFA105 extends DFA {

        public DFA105(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 105;
            this.eot = DFA105_eot;
            this.eof = DFA105_eof;
            this.min = DFA105_min;
            this.max = DFA105_max;
            this.accept = DFA105_accept;
            this.special = DFA105_special;
            this.transition = DFA105_transition;
        }
        public String getDescription() {
            return "1091:1: element : ( ( key ':' )=> key ':' value | value );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA105_1 = input.LA(1);

                         
                        int index105_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA105_2 = input.LA(1);

                         
                        int index105_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA105_3 = input.LA(1);

                         
                        int index105_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA105_4 = input.LA(1);

                         
                        int index105_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA105_5 = input.LA(1);

                         
                        int index105_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA105_6 = input.LA(1);

                         
                        int index105_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA105_7 = input.LA(1);

                         
                        int index105_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA105_8 = input.LA(1);

                         
                        int index105_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA105_9 = input.LA(1);

                         
                        int index105_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA105_10 = input.LA(1);

                         
                        int index105_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA105_11 = input.LA(1);

                         
                        int index105_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA105_12 = input.LA(1);

                         
                        int index105_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA105_13 = input.LA(1);

                         
                        int index105_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA105_14 = input.LA(1);

                         
                        int index105_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_14);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA105_15 = input.LA(1);

                         
                        int index105_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_15);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA105_16 = input.LA(1);

                         
                        int index105_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_16);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA105_17 = input.LA(1);

                         
                        int index105_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_17);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA105_18 = input.LA(1);

                         
                        int index105_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_18);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA105_19 = input.LA(1);

                         
                        int index105_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_19);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA105_20 = input.LA(1);

                         
                        int index105_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_20);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA105_21 = input.LA(1);

                         
                        int index105_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_21);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA105_22 = input.LA(1);

                         
                        int index105_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_22);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA105_23 = input.LA(1);

                         
                        int index105_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_23);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA105_24 = input.LA(1);

                         
                        int index105_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_24);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA105_25 = input.LA(1);

                         
                        int index105_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_25);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA105_26 = input.LA(1);

                         
                        int index105_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_26);
                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA105_27 = input.LA(1);

                         
                        int index105_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_27);
                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA105_28 = input.LA(1);

                         
                        int index105_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_28);
                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA105_29 = input.LA(1);

                         
                        int index105_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_29);
                        if ( s>=0 ) return s;
                        break;
                    case 29 : 
                        int LA105_30 = input.LA(1);

                         
                        int index105_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_GoSource()) ) {s = 32;}

                        else if ( (true) ) {s = 31;}

                         
                        input.seek(index105_30);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 105, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA106_eotS =
        "\22\uffff";
    static final String DFA106_eofS =
        "\22\uffff";
    static final String DFA106_minS =
        "\1\5\17\0\2\uffff";
    static final String DFA106_maxS =
        "\1\170\17\0\2\uffff";
    static final String DFA106_acceptS =
        "\20\uffff\1\2\1\1";
    static final String DFA106_specialS =
        "\1\uffff\1\13\1\3\1\11\1\2\1\12\1\14\1\4\1\7\1\1\1\16\1\5\1\15\1"+
        "\6\1\10\1\0\2\uffff}>";
    static final String[] DFA106_transitionS = {
            "\1\20\4\uffff\6\20\1\17\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11"+
            "\1\12\1\13\1\14\1\15\2\uffff\2\20\36\uffff\1\20\2\uffff\2\20"+
            "\15\uffff\1\20\17\uffff\1\16\5\20\14\uffff\1\20\2\uffff\2\20",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA106_eot = DFA.unpackEncodedString(DFA106_eotS);
    static final short[] DFA106_eof = DFA.unpackEncodedString(DFA106_eofS);
    static final char[] DFA106_min = DFA.unpackEncodedStringToUnsignedChars(DFA106_minS);
    static final char[] DFA106_max = DFA.unpackEncodedStringToUnsignedChars(DFA106_maxS);
    static final short[] DFA106_accept = DFA.unpackEncodedString(DFA106_acceptS);
    static final short[] DFA106_special = DFA.unpackEncodedString(DFA106_specialS);
    static final short[][] DFA106_transition;

    static {
        int numStates = DFA106_transitionS.length;
        DFA106_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA106_transition[i] = DFA.unpackEncodedString(DFA106_transitionS[i]);
        }
    }

    class DFA106 extends DFA {

        public DFA106(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 106;
            this.eot = DFA106_eot;
            this.eof = DFA106_eof;
            this.min = DFA106_min;
            this.max = DFA106_max;
            this.accept = DFA106_accept;
            this.special = DFA106_special;
            this.transition = DFA106_transition;
        }
        public String getDescription() {
            return "1097:1: key : ( ( fieldName )=> fieldName | elementIndex );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA106_15 = input.LA(1);

                         
                        int index106_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_15);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA106_9 = input.LA(1);

                         
                        int index106_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_9);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA106_4 = input.LA(1);

                         
                        int index106_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA106_2 = input.LA(1);

                         
                        int index106_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_2);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA106_7 = input.LA(1);

                         
                        int index106_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_7);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA106_11 = input.LA(1);

                         
                        int index106_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_11);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA106_13 = input.LA(1);

                         
                        int index106_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_13);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA106_8 = input.LA(1);

                         
                        int index106_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA106_14 = input.LA(1);

                         
                        int index106_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_14);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA106_3 = input.LA(1);

                         
                        int index106_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_3);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA106_5 = input.LA(1);

                         
                        int index106_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_5);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA106_1 = input.LA(1);

                         
                        int index106_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_1);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA106_6 = input.LA(1);

                         
                        int index106_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_6);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA106_12 = input.LA(1);

                         
                        int index106_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_12);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA106_10 = input.LA(1);

                         
                        int index106_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_GoSource()) ) {s = 17;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index106_10);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 106, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA108_eotS =
        "\42\uffff";
    static final String DFA108_eofS =
        "\1\uffff\17\21\22\uffff";
    static final String DFA108_minS =
        "\1\20\17\4\1\20\1\uffff\17\0\1\uffff";
    static final String DFA108_maxS =
        "\1\142\17\166\1\142\1\uffff\17\0\1\uffff";
    static final String DFA108_acceptS =
        "\21\uffff\1\2\17\uffff\1\1";
    static final String DFA108_specialS =
        "\22\uffff\1\12\1\6\1\4\1\2\1\1\1\0\1\16\1\15\1\14\1\13\1\11\1\7"+
        "\1\5\1\3\1\10\1\uffff}>";
    static final String[] DFA108_transitionS = {
            "\1\17\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
            "\15\104\uffff\1\16",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\2\21\40\uffff\5\21\25\uffff\1\21\1\20\1\21\1\uffff\4\21\2"+
            "\uffff\1\21\2\uffff\1\21\4\uffff\1\21\2\uffff\14\21\2\uffff"+
            "\21\21\1\uffff\2\21",
            "\1\40\1\22\1\23\1\24\1\25\1\26\1\27\1\30\1\31\1\32\1\33\1\34"+
            "\1\35\1\36\42\uffff\1\21\41\uffff\1\37",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            ""
    };

    static final short[] DFA108_eot = DFA.unpackEncodedString(DFA108_eotS);
    static final short[] DFA108_eof = DFA.unpackEncodedString(DFA108_eofS);
    static final char[] DFA108_min = DFA.unpackEncodedStringToUnsignedChars(DFA108_minS);
    static final char[] DFA108_max = DFA.unpackEncodedStringToUnsignedChars(DFA108_maxS);
    static final short[] DFA108_accept = DFA.unpackEncodedString(DFA108_acceptS);
    static final short[] DFA108_special = DFA.unpackEncodedString(DFA108_specialS);
    static final short[][] DFA108_transition;

    static {
        int numStates = DFA108_transitionS.length;
        DFA108_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA108_transition[i] = DFA.unpackEncodedString(DFA108_transitionS[i]);
        }
    }

    class DFA108 extends DFA {

        public DFA108(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 108;
            this.eot = DFA108_eot;
            this.eof = DFA108_eof;
            this.min = DFA108_min;
            this.max = DFA108_max;
            this.accept = DFA108_accept;
            this.special = DFA108_special;
            this.transition = DFA108_transition;
        }
        public String getDescription() {
            return "1124:1: qualifiedIdent : ( ( identifier '.' identifier )=> packageName '.' identifier | identifier );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA108_23 = input.LA(1);

                         
                        int index108_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_23);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA108_22 = input.LA(1);

                         
                        int index108_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_22);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA108_21 = input.LA(1);

                         
                        int index108_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_21);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA108_31 = input.LA(1);

                         
                        int index108_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_31);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA108_20 = input.LA(1);

                         
                        int index108_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_20);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA108_30 = input.LA(1);

                         
                        int index108_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_30);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA108_19 = input.LA(1);

                         
                        int index108_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_19);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA108_29 = input.LA(1);

                         
                        int index108_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_29);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA108_32 = input.LA(1);

                         
                        int index108_32 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_32);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA108_28 = input.LA(1);

                         
                        int index108_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_28);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA108_18 = input.LA(1);

                         
                        int index108_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_18);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA108_27 = input.LA(1);

                         
                        int index108_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_27);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA108_26 = input.LA(1);

                         
                        int index108_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_26);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA108_25 = input.LA(1);

                         
                        int index108_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_25);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA108_24 = input.LA(1);

                         
                        int index108_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_GoSource()) ) {s = 33;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index108_24);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 108, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA111_eotS =
        "\12\uffff";
    static final String DFA111_eofS =
        "\12\uffff";
    static final String DFA111_minS =
        "\1\40\1\5\10\uffff";
    static final String DFA111_maxS =
        "\2\170\10\uffff";
    static final String DFA111_acceptS =
        "\2\uffff\1\2\1\3\1\4\1\6\1\7\1\10\1\5\1\1";
    static final String DFA111_specialS =
        "\12\uffff}>";
    static final String[] DFA111_transitionS = {
            "\1\2\1\4\41\uffff\1\3\1\6\15\uffff\1\7\20\uffff\1\1\23\uffff"+
            "\1\5\1\7",
            "\1\11\4\uffff\24\11\2\uffff\2\11\7\uffff\1\10\26\uffff\1\11"+
            "\2\uffff\2\11\15\uffff\1\11\17\uffff\6\11\14\uffff\1\11\2\uffff"+
            "\2\11",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA111_eot = DFA.unpackEncodedString(DFA111_eotS);
    static final short[] DFA111_eof = DFA.unpackEncodedString(DFA111_eofS);
    static final char[] DFA111_min = DFA.unpackEncodedStringToUnsignedChars(DFA111_minS);
    static final char[] DFA111_max = DFA.unpackEncodedStringToUnsignedChars(DFA111_maxS);
    static final short[] DFA111_accept = DFA.unpackEncodedString(DFA111_acceptS);
    static final short[] DFA111_special = DFA.unpackEncodedString(DFA111_specialS);
    static final short[][] DFA111_transition;

    static {
        int numStates = DFA111_transitionS.length;
        DFA111_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA111_transition[i] = DFA.unpackEncodedString(DFA111_transitionS[i]);
        }
    }

    class DFA111 extends DFA {

        public DFA111(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 111;
            this.eot = DFA111_eot;
            this.eof = DFA111_eof;
            this.min = DFA111_min;
            this.max = DFA111_max;
            this.accept = DFA111_accept;
            this.special = DFA111_special;
            this.transition = DFA111_transition;
        }
        public String getDescription() {
            return "1143:1: typeLit : ( arrayType | structType | functionType | interfaceType | sliceType | mapType | pointerType | channelType );";
        }
    }
    static final String DFA114_eotS =
        "\22\uffff";
    static final String DFA114_eofS =
        "\22\uffff";
    static final String DFA114_minS =
        "\1\20\17\4\2\uffff";
    static final String DFA114_maxS =
        "\1\142\17\101\2\uffff";
    static final String DFA114_acceptS =
        "\20\uffff\1\1\1\2";
    static final String DFA114_specialS =
        "\22\uffff}>";
    static final String[] DFA114_transitionS = {
            "\1\17\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
            "\15\104\uffff\1\16",
            "\1\21\73\uffff\1\20\1\21",
            "\1\21\73\uffff\1\20\1\21",
            "\1\21\73\uffff\1\20\1\21",
            "\1\21\73\uffff\1\20\1\21",
            "\1\21\73\uffff\1\20\1\21",
            "\1\21\73\uffff\1\20\1\21",
            "\1\21\73\uffff\1\20\1\21",
            "\1\21\73\uffff\1\20\1\21",
            "\1\21\73\uffff\1\20\1\21",
            "\1\21\73\uffff\1\20\1\21",
            "\1\21\73\uffff\1\20\1\21",
            "\1\21\73\uffff\1\20\1\21",
            "\1\21\73\uffff\1\20\1\21",
            "\1\21\73\uffff\1\20\1\21",
            "\1\21\73\uffff\1\20\1\21",
            "",
            ""
    };

    static final short[] DFA114_eot = DFA.unpackEncodedString(DFA114_eotS);
    static final short[] DFA114_eof = DFA.unpackEncodedString(DFA114_eofS);
    static final char[] DFA114_min = DFA.unpackEncodedStringToUnsignedChars(DFA114_minS);
    static final char[] DFA114_max = DFA.unpackEncodedStringToUnsignedChars(DFA114_maxS);
    static final short[] DFA114_accept = DFA.unpackEncodedString(DFA114_acceptS);
    static final short[] DFA114_special = DFA.unpackEncodedString(DFA114_specialS);
    static final short[][] DFA114_transition;

    static {
        int numStates = DFA114_transitionS.length;
        DFA114_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA114_transition[i] = DFA.unpackEncodedString(DFA114_transitionS[i]);
        }
    }

    class DFA114 extends DFA {

        public DFA114(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 114;
            this.eot = DFA114_eot;
            this.eof = DFA114_eof;
            this.min = DFA114_min;
            this.max = DFA114_max;
            this.accept = DFA114_accept;
            this.special = DFA114_special;
            this.transition = DFA114_transition;
        }
        public String getDescription() {
            return "1166:1: methodSpec : ( methodName signature | interfaceTypeName );";
        }
    }
    static final String DFA117_eotS =
        "\22\uffff";
    static final String DFA117_eofS =
        "\22\uffff";
    static final String DFA117_minS =
        "\1\20\17\4\2\uffff";
    static final String DFA117_maxS =
        "\1\142\17\170\2\uffff";
    static final String DFA117_acceptS =
        "\20\uffff\1\2\1\1";
    static final String DFA117_specialS =
        "\22\uffff}>";
    static final String[] DFA117_transitionS = {
            "\1\17\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
            "\15\46\uffff\1\20\35\uffff\1\16",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "\2\20\12\uffff\16\21\2\uffff\2\21\36\uffff\1\21\1\20\1\uffff"+
            "\3\21\14\uffff\1\21\17\uffff\2\21\23\uffff\2\21",
            "",
            ""
    };

    static final short[] DFA117_eot = DFA.unpackEncodedString(DFA117_eotS);
    static final short[] DFA117_eof = DFA.unpackEncodedString(DFA117_eofS);
    static final char[] DFA117_min = DFA.unpackEncodedStringToUnsignedChars(DFA117_minS);
    static final char[] DFA117_max = DFA.unpackEncodedStringToUnsignedChars(DFA117_maxS);
    static final short[] DFA117_accept = DFA.unpackEncodedString(DFA117_acceptS);
    static final short[] DFA117_special = DFA.unpackEncodedString(DFA117_specialS);
    static final short[][] DFA117_transition;

    static {
        int numStates = DFA117_transitionS.length;
        DFA117_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA117_transition[i] = DFA.unpackEncodedString(DFA117_transitionS[i]);
        }
    }

    class DFA117 extends DFA {

        public DFA117(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 117;
            this.eot = DFA117_eot;
            this.eof = DFA117_eof;
            this.min = DFA117_min;
            this.max = DFA117_max;
            this.accept = DFA117_accept;
            this.special = DFA117_special;
            this.transition = DFA117_transition;
        }
        public String getDescription() {
            return "1208:3: ( identifierList type | anonymousField )";
        }
    }
    static final String DFA122_eotS =
        "\20\uffff";
    static final String DFA122_eofS =
        "\20\uffff";
    static final String DFA122_minS =
        "\1\20\17\uffff";
    static final String DFA122_maxS =
        "\1\142\17\uffff";
    static final String DFA122_acceptS =
        "\1\uffff\16\1\1\2";
    static final String DFA122_specialS =
        "\1\0\17\uffff}>";
    static final String[] DFA122_transitionS = {
            "\1\17\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
            "\15\104\uffff\1\16",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA122_eot = DFA.unpackEncodedString(DFA122_eotS);
    static final short[] DFA122_eof = DFA.unpackEncodedString(DFA122_eofS);
    static final char[] DFA122_min = DFA.unpackEncodedStringToUnsignedChars(DFA122_minS);
    static final char[] DFA122_max = DFA.unpackEncodedStringToUnsignedChars(DFA122_maxS);
    static final short[] DFA122_accept = DFA.unpackEncodedString(DFA122_acceptS);
    static final short[] DFA122_special = DFA.unpackEncodedString(DFA122_specialS);
    static final short[][] DFA122_transition;

    static {
        int numStates = DFA122_transitionS.length;
        DFA122_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA122_transition[i] = DFA.unpackEncodedString(DFA122_transitionS[i]);
        }
    }

    class DFA122 extends DFA {

        public DFA122(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 122;
            this.eot = DFA122_eot;
            this.eof = DFA122_eof;
            this.min = DFA122_min;
            this.max = DFA122_max;
            this.accept = DFA122_accept;
            this.special = DFA122_special;
            this.transition = DFA122_transition;
        }
        public String getDescription() {
            return "1282:1: identifier : ( ( builtinName )=> (t= 'append' | t= 'cap' | t= 'close' | t= 'closed' | t= 'cmplx' | t= 'copy' | t= 'imag' | t= 'len' | t= 'make' | t= 'panic' | t= 'print' | t= 'println' | t= 'real' | t= 'recover' ) | Identifier );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA122_0 = input.LA(1);

                         
                        int index122_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA122_0==APPEND) && (synpred48_GoSource())) {s = 1;}

                        else if ( (LA122_0==CAP) && (synpred48_GoSource())) {s = 2;}

                        else if ( (LA122_0==CLOSE) && (synpred48_GoSource())) {s = 3;}

                        else if ( (LA122_0==CLOSED) && (synpred48_GoSource())) {s = 4;}

                        else if ( (LA122_0==CMPLX) && (synpred48_GoSource())) {s = 5;}

                        else if ( (LA122_0==COPY) && (synpred48_GoSource())) {s = 6;}

                        else if ( (LA122_0==IMAG) && (synpred48_GoSource())) {s = 7;}

                        else if ( (LA122_0==LEN) && (synpred48_GoSource())) {s = 8;}

                        else if ( (LA122_0==MAKE) && (synpred48_GoSource())) {s = 9;}

                        else if ( (LA122_0==PANIC) && (synpred48_GoSource())) {s = 10;}

                        else if ( (LA122_0==PRINT) && (synpred48_GoSource())) {s = 11;}

                        else if ( (LA122_0==PRINTLN) && (synpred48_GoSource())) {s = 12;}

                        else if ( (LA122_0==REAL) && (synpred48_GoSource())) {s = 13;}

                        else if ( (LA122_0==98) && (synpred48_GoSource())) {s = 14;}

                        else if ( (LA122_0==Identifier) ) {s = 15;}

                         
                        input.seek(index122_0);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 122, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_packageClause_in_program87 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_program89 = new BitSet(new long[]{0x8000000C00000010L,0x0000000200000008L});
    public static final BitSet FOLLOW_importDecl_in_program108 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_topLevelDecl_in_program118 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_program131 = new BitSet(new long[]{0x8000000C00000010L,0x0000000200000008L});
    public static final BitSet FOLLOW_EOF_in_program141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_packageClause156 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000000L});
    public static final BitSet FOLLOW_packageName_in_packageClause159 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_importDecl174 = new BitSet(new long[]{0x000000003FFF0020L,0x0000000400000003L});
    public static final BitSet FOLLOW_importSpec_in_importDecl185 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_importDecl193 = new BitSet(new long[]{0x000001003FFF0030L,0x0000000400000002L});
    public static final BitSet FOLLOW_importSpec_in_importDecl196 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_importDecl198 = new BitSet(new long[]{0x000001003FFF0030L,0x0000000400000002L});
    public static final BitSet FOLLOW_SEMI_in_importDecl203 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_importDecl212 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_importSpec241 = new BitSet(new long[]{0x000000003FFF0020L,0x0000000400000002L});
    public static final BitSet FOLLOW_packageName_in_importSpec280 = new BitSet(new long[]{0x000000003FFF0020L,0x0000000400000002L});
    public static final BitSet FOLLOW_importPath_in_importSpec305 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_String_Lit_in_importPath324 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_declaration_in_topLevelDecl339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_methodDecl_in_topLevelDecl354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionDecl_in_topLevelDecl360 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constDecl_in_declaration375 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeDecl_in_declaration381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_varDecl_in_declaration387 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPE_in_typeDecl402 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000001L});
    public static final BitSet FOLLOW_typeSpec_in_typeDecl413 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_typeDecl421 = new BitSet(new long[]{0x000001003FFF0010L,0x0000000400000000L});
    public static final BitSet FOLLOW_typeSpec_in_typeDecl424 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_typeDecl426 = new BitSet(new long[]{0x000001003FFF0010L,0x0000000400000000L});
    public static final BitSet FOLLOW_SEMI_in_typeDecl430 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_typeDecl439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_typeSpec458 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_type_in_typeSpec460 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_varDecl475 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000001L});
    public static final BitSet FOLLOW_varSpec_in_varDecl486 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_varDecl494 = new BitSet(new long[]{0x000001003FFF0010L,0x0000000400000000L});
    public static final BitSet FOLLOW_varSpec_in_varDecl498 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_varDecl500 = new BitSet(new long[]{0x000001003FFF0010L,0x0000000400000000L});
    public static final BitSet FOLLOW_SEMI_in_varDecl504 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_varDecl513 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierList_in_varSpec536 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C0004001DL});
    public static final BitSet FOLLOW_66_in_varSpec546 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expressionList_in_varSpec549 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_varSpec579 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_varSpec581 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expressionList_in_varSpec584 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_varSpec604 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_67_in_methodDecl641 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_receiver_in_methodDecl644 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000000L});
    public static final BitSet FOLLOW_identifier_in_methodDecl646 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_signature_in_methodDecl648 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_body_in_methodDecl650 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_receiver677 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000010L});
    public static final BitSet FOLLOW_identifier_in_receiver682 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000010L});
    public static final BitSet FOLLOW_68_in_receiver696 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000000L});
    public static final BitSet FOLLOW_identifier_in_receiver711 = new BitSet(new long[]{0x0000010000000010L});
    public static final BitSet FOLLOW_SEMI_in_receiver721 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_receiver739 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_67_in_functionDecl758 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000000L});
    public static final BitSet FOLLOW_identifier_in_functionDecl761 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_signature_in_functionDecl763 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_body_in_functionDecl765 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameters_in_signature793 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_result_in_signature795 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameters_in_signature801 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameters_in_result822 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_result828 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_parameters843 = new BitSet(new long[]{0x000001033FFF0010L,0x0180000C00040059L});
    public static final BitSet FOLLOW_parametersList_in_parameters846 = new BitSet(new long[]{0x0000010000000010L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_parameters848 = new BitSet(new long[]{0x0000010000000010L});
    public static final BitSet FOLLOW_SEMI_in_parameters853 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_parameters860 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterDecl_in_parametersList875 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_parametersList878 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040059L});
    public static final BitSet FOLLOW_parameterDecl_in_parametersList880 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_identifierList_in_parameterDecl910 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040059L});
    public static final BitSet FOLLOW_70_in_parameterDecl912 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_type_in_parameterDecl915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_70_in_parameterDecl925 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_type_in_parameterDecl928 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_body953 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_71_in_block976 = new BitSet(new long[]{0x0000040FBFFFFFE0L,0x019000FE000FC299L});
    public static final BitSet FOLLOW_statement_in_block987 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_block989 = new BitSet(new long[]{0x0000040FBFFFFFE0L,0x019000FE000FC299L});
    public static final BitSet FOLLOW_CLOSE_CURLY_in_block1001 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labeledStatement_in_statement1029 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_declaration_in_statement1035 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_deferStmt_in_statement1041 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnStmt_in_statement1047 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_goStmt_in_statement1053 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_breakStmt_in_statement1059 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_continueStmt_in_statement1065 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fallthroughStmt_in_statement1071 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_statement1077 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifStmt_in_statement1083 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_switchStmt_in_statement1089 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selectStmt_in_statement1095 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_gotoStmt_in_statement1101 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forStmt_in_statement1107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simpleStmt_in_statement1113 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignment_in_simpleStmt1134 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_shortVarDecl_in_simpleStmt1146 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_incDecStmt_in_simpleStmt1158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionStmt_in_simpleStmt1170 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifStmt1193 = new BitSet(new long[]{0x000000033FFFFC30L,0x019000FC00040099L});
    public static final BitSet FOLLOW_simpleStmt_in_ifStmt1211 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_ifStmt1213 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expression_in_ifStmt1215 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_block_in_ifStmt1217 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_expression_in_ifStmt1233 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_block_in_ifStmt1235 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_block_in_ifStmt1243 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_72_in_ifStmt1252 = new BitSet(new long[]{0x0000000FBFFFFFE0L,0x019000FE000FC299L});
    public static final BitSet FOLLOW_statement_in_ifStmt1254 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeSwitchStmt_in_switchStmt1277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exprSwitchStmt_in_switchStmt1289 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_73_in_exprSwitchStmt1304 = new BitSet(new long[]{0x000000033FFFFC30L,0x019000FC00040099L});
    public static final BitSet FOLLOW_simpleStmt_in_exprSwitchStmt1325 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_exprSwitchStmt1327 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expression_in_exprSwitchStmt1329 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_simpleStmt_in_exprSwitchStmt1345 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_exprSwitchStmt1347 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_expression_in_exprSwitchStmt1355 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_71_in_exprSwitchStmt1364 = new BitSet(new long[]{0x0000040000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_exprSwitchClause_in_exprSwitchStmt1366 = new BitSet(new long[]{0x0000040000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_CLOSE_CURLY_in_exprSwitchStmt1369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exprSwitchCase_in_exprSwitchClause1384 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_exprSwitchClause1386 = new BitSet(new long[]{0x0000000FBFFFFFE2L,0x019000FE000FC299L});
    public static final BitSet FOLLOW_statement_in_exprSwitchClause1389 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_exprSwitchClause1391 = new BitSet(new long[]{0x0000000FBFFFFFE2L,0x019000FE000FC299L});
    public static final BitSet FOLLOW_75_in_exprSwitchCase1408 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expressionList_in_exprSwitchCase1411 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_exprSwitchCase1417 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_73_in_typeSwitchStmt1432 = new BitSet(new long[]{0x000000033FFFFC30L,0x019000FC00040019L});
    public static final BitSet FOLLOW_simpleStmt_in_typeSwitchStmt1451 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_typeSwitchStmt1453 = new BitSet(new long[]{0x000000033FFFFC30L,0x019000FC00040019L});
    public static final BitSet FOLLOW_typeSwitchGuard_in_typeSwitchStmt1455 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_typeSwitchGuard_in_typeSwitchStmt1463 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_71_in_typeSwitchStmt1471 = new BitSet(new long[]{0x0000040000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_typeSwitchClause_in_typeSwitchStmt1484 = new BitSet(new long[]{0x0000040000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_CLOSE_CURLY_in_typeSwitchStmt1494 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_typeSwitchGuard1510 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_77_in_typeSwitchGuard1512 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_primaryExpr_in_typeSwitchGuard1516 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_typeSwitchGuard1518 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_typeSwitchGuard1520 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_TYPE_in_typeSwitchGuard1522 = new BitSet(new long[]{0x0000010000000010L});
    public static final BitSet FOLLOW_SEMI_in_typeSwitchGuard1524 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_typeSwitchGuard1531 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeSwitchCase_in_typeSwitchClause1546 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_typeSwitchClause1548 = new BitSet(new long[]{0x0000000FBFFFFFE2L,0x019000FE000FC299L});
    public static final BitSet FOLLOW_statement_in_typeSwitchClause1551 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_typeSwitchClause1553 = new BitSet(new long[]{0x0000000FBFFFFFE2L,0x019000FE000FC299L});
    public static final BitSet FOLLOW_75_in_typeSwitchCase1570 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_typeList_in_typeSwitchCase1573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_typeSwitchCase1579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeList1595 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_typeList1598 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_type_in_typeList1600 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_78_in_goStmt1617 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expression_in_goStmt1620 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_79_in_gotoStmt1635 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000000L});
    public static final BitSet FOLLOW_label_in_gotoStmt1637 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_breakStmt1652 = new BitSet(new long[]{0x000000003FFF0002L,0x0000000400000000L});
    public static final BitSet FOLLOW_label_in_breakStmt1654 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_continueStmt1670 = new BitSet(new long[]{0x000000003FFF0002L,0x0000000400000000L});
    public static final BitSet FOLLOW_label_in_continueStmt1672 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALLTHROUGH_in_fallthroughStmt1688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnStmt1703 = new BitSet(new long[]{0x000000033FFFFC22L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expressionList_in_returnStmt1706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_80_in_deferStmt1722 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expression_in_deferStmt1725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_assignment1740 = new BitSet(new long[]{0x0000000000000000L,0x00000001FFE00004L});
    public static final BitSet FOLLOW_assignOp_in_assignment1742 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expressionList_in_assignment1745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_81_in_selectStmt1760 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_71_in_selectStmt1762 = new BitSet(new long[]{0x0000040000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_commClause_in_selectStmt1766 = new BitSet(new long[]{0x0000040000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_CLOSE_CURLY_in_selectStmt1771 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_commCase_in_commClause1786 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_commClause1788 = new BitSet(new long[]{0x0000000FBFFFFFE2L,0x019000FE000FC299L});
    public static final BitSet FOLLOW_statement_in_commClause1791 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_commClause1793 = new BitSet(new long[]{0x0000000FBFFFFFE2L,0x019000FE000FC299L});
    public static final BitSet FOLLOW_75_in_commCase1810 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_sendExpr_in_commCase1826 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_recvExpr_in_commCase1840 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_commCase1850 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_relational_in_sendExpr1865 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_sendExpr1867 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_relational_in_sendExpr1869 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_recvExpr1938 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002004L});
    public static final BitSet FOLLOW_set_in_recvExpr1942 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_recvExpr1964 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_relational_in_recvExpr1966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_82_in_recvExpr1972 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_relational_in_recvExpr1974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_83_in_forStmt1989 = new BitSet(new long[]{0x000000033FFFFC30L,0x019000FC00040099L});
    public static final BitSet FOLLOW_forClause_in_forStmt2008 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_condition_in_forStmt2024 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_rangeClause_in_forStmt2032 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_block_in_forStmt2041 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_condition2056 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_initStmt_in_forClause2085 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_forClause2087 = new BitSet(new long[]{0x000000033FFFFC30L,0x019000FC00040019L});
    public static final BitSet FOLLOW_SEMI_in_forClause2095 = new BitSet(new long[]{0x000000033FFFFC30L,0x019000FC00040019L});
    public static final BitSet FOLLOW_condition_in_forClause2117 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_forClause2119 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_SEMI_in_forClause2127 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_postStmt_in_forClause2141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_rangeClause2156 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002024L});
    public static final BitSet FOLLOW_69_in_rangeClause2159 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expression_in_rangeClause2161 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002004L});
    public static final BitSet FOLLOW_set_in_rangeClause2167 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_84_in_rangeClause2189 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expression_in_rangeClause2191 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simpleStmt_in_initStmt2206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simpleStmt_in_postStmt2221 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_assignOp0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierList_in_shortVarDecl2325 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_77_in_shortVarDecl2327 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expressionList_in_shortVarDecl2330 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_label_in_labeledStatement2349 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_labeledStatement2351 = new BitSet(new long[]{0x0000000FBFFFFFE0L,0x019000FE000FC299L});
    public static final BitSet FOLLOW_statement_in_labeledStatement2354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_label2369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionStmt2384 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_incDecStmt2399 = new BitSet(new long[]{0x000000C000000000L});
    public static final BitSet FOLLOW_PLUSPLUS_in_incDecStmt2409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MINUSMINUS_in_incDecStmt2418 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_97_in_constDecl2438 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000001L});
    public static final BitSet FOLLOW_constSpec_in_constDecl2449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_constDecl2457 = new BitSet(new long[]{0x000001003FFF0010L,0x0000000400000000L});
    public static final BitSet FOLLOW_constSpec_in_constDecl2460 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_constDecl2462 = new BitSet(new long[]{0x000001003FFF0010L,0x0000000400000000L});
    public static final BitSet FOLLOW_SEMI_in_constDecl2466 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_constDecl2475 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierList_in_constSpec2494 = new BitSet(new long[]{0x000000033FFF0002L,0x0180000C0004001DL});
    public static final BitSet FOLLOW_type_in_constSpec2531 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_constSpec2533 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_66_in_constSpec2552 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expressionList_in_constSpec2564 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_identifierList2593 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_identifierList2606 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000000L});
    public static final BitSet FOLLOW_identifier_in_identifierList2611 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_expression_in_expressionList2646 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_expressionList2649 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expression_in_expressionList2652 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_logical_or_in_expression2683 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtinCall_in_base2712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_operand_in_base2724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conversion_in_base2736 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_base_in_primaryExpr2759 = new BitSet(new long[]{0x0000000000000002L,0x0000000800000003L});
    public static final BitSet FOLLOW_call_in_primaryExpr2783 = new BitSet(new long[]{0x0000000000000002L,0x0000000800000003L});
    public static final BitSet FOLLOW_selector_in_primaryExpr2799 = new BitSet(new long[]{0x0000000000000002L,0x0000000800000003L});
    public static final BitSet FOLLOW_index_in_primaryExpr2815 = new BitSet(new long[]{0x0000000000000002L,0x0000000800000003L});
    public static final BitSet FOLLOW_typeAssertion_in_primaryExpr2831 = new BitSet(new long[]{0x0000000000000002L,0x0000000800000003L});
    public static final BitSet FOLLOW_slice_in_primaryExpr2847 = new BitSet(new long[]{0x0000000000000002L,0x0000000800000003L});
    public static final BitSet FOLLOW_set_in_builtinName0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtinName_in_builtinCall2990 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_builtinCall2992 = new BitSet(new long[]{0x000001033FFFFC30L,0x019000FC00040019L});
    public static final BitSet FOLLOW_builtinArgs_in_builtinCall2995 = new BitSet(new long[]{0x0000010000000010L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_builtinCall2998 = new BitSet(new long[]{0x0000010000000010L});
    public static final BitSet FOLLOW_SEMI_in_builtinCall3004 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_builtinCall3011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_builtinArgs3042 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_builtinArgs3044 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expressionList_in_builtinArgs3046 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_builtinArgs3060 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_builtinArgs3068 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_selector3087 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000000L});
    public static final BitSet FOLLOW_identifier_in_selector3089 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_typeAssertion3104 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_typeAssertion3106 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_type_in_typeAssertion3108 = new BitSet(new long[]{0x0000010000000010L});
    public static final BitSet FOLLOW_SEMI_in_typeAssertion3110 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_typeAssertion3117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_index3132 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expression_in_index3134 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_CLOSE_SQUARE_in_index3136 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_slice3151 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040419L});
    public static final BitSet FOLLOW_expression_in_slice3154 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_slice3158 = new BitSet(new long[]{0x000002033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expression_in_slice3161 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_CLOSE_SQUARE_in_slice3165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_call3180 = new BitSet(new long[]{0x000001033FFFFC30L,0x019000FC00040019L});
    public static final BitSet FOLLOW_argumentList_in_call3184 = new BitSet(new long[]{0x0000010000000010L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_call3186 = new BitSet(new long[]{0x0000010000000010L});
    public static final BitSet FOLLOW_SEMI_in_call3191 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_call3198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_argumentList3213 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000040L});
    public static final BitSet FOLLOW_70_in_argumentList3215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_conversion3231 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_conversion3233 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expression_in_conversion3235 = new BitSet(new long[]{0x0000010000000010L});
    public static final BitSet FOLLOW_SEMI_in_conversion3237 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_conversion3244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_82_in_unary3275 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_primaryExpr_in_unary3278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_unary3292 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_primaryExpr_in_unary3295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_100_in_unary3309 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_101_in_unary3318 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_102_in_unary3327 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_103_in_unary3336 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_primaryExpr_in_unary3346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unary_in_mult3369 = new BitSet(new long[]{0x0000000000000002L,0x00001F8000000010L});
    public static final BitSet FOLLOW_68_in_mult3387 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_104_in_mult3398 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_105_in_mult3409 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_106_in_mult3420 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_107_in_mult3431 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_103_in_mult3442 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_108_in_mult3453 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_unary_in_mult3466 = new BitSet(new long[]{0x0000000000000002L,0x00001F8000000010L});
    public static final BitSet FOLLOW_mult_in_add3494 = new BitSet(new long[]{0x0000000000000002L,0x0000207000000000L});
    public static final BitSet FOLLOW_100_in_add3512 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_101_in_add3523 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_109_in_add3534 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_102_in_add3545 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_mult_in_add3558 = new BitSet(new long[]{0x0000000000000002L,0x0000207000000000L});
    public static final BitSet FOLLOW_add_in_relational3586 = new BitSet(new long[]{0x0000000000000002L,0x000FC00000000000L});
    public static final BitSet FOLLOW_110_in_relational3604 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_111_in_relational3615 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_112_in_relational3626 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_113_in_relational3637 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_114_in_relational3648 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_115_in_relational3659 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_add_in_relational3672 = new BitSet(new long[]{0x0000000000000002L,0x000FC00000000000L});
    public static final BitSet FOLLOW_relational_in_channel3700 = new BitSet(new long[]{0x0000000000000002L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_channel3703 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_relational_in_channel3706 = new BitSet(new long[]{0x0000000000000002L,0x0000000000040000L});
    public static final BitSet FOLLOW_116_in_negation3731 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_channel_in_negation3735 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_negation_in_logical_and3758 = new BitSet(new long[]{0x0000000000000002L,0x0020000000000000L});
    public static final BitSet FOLLOW_117_in_logical_and3761 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_negation_in_logical_and3764 = new BitSet(new long[]{0x0000000000000002L,0x0020000000000000L});
    public static final BitSet FOLLOW_logical_and_in_logical_or3790 = new BitSet(new long[]{0x0000000000000002L,0x0040000000000000L});
    public static final BitSet FOLLOW_118_in_logical_or3793 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_logical_and_in_logical_or3796 = new BitSet(new long[]{0x0000000000000002L,0x0040000000000000L});
    public static final BitSet FOLLOW_literal_in_operand3827 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_methodExpr_in_operand3839 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_operand3845 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expression_in_operand3847 = new BitSet(new long[]{0x0000010000000010L});
    public static final BitSet FOLLOW_SEMI_in_operand3849 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_operand3856 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedIdent_in_operand3862 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_receiverType_in_methodExpr3877 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_methodExpr3879 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000000L});
    public static final BitSet FOLLOW_methodName_in_methodExpr3881 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_receiverType3896 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_receiverType3902 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_68_in_receiverType3904 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_typeName_in_receiverType3906 = new BitSet(new long[]{0x0000010000000010L});
    public static final BitSet FOLLOW_SEMI_in_receiverType3908 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_receiverType3915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_basicLit_in_literal3938 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compositeLit_in_literal3956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionLit_in_literal3962 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_basicLit0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literalType_in_compositeLit4036 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_literalValue_in_compositeLit4038 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_structType_in_literalType4061 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arrayType_in_literalType4067 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_literalType4073 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_70_in_literalType4075 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_CLOSE_SQUARE_in_literalType4077 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_elementType_in_literalType4079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sliceType_in_literalType4085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mapType_in_literalType4091 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_literalType4097 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_71_in_literalValue4112 = new BitSet(new long[]{0x000004033FFFFC30L,0x019000FC00040099L});
    public static final BitSet FOLLOW_elementList_in_literalValue4116 = new BitSet(new long[]{0x0000040000000010L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_literalValue4119 = new BitSet(new long[]{0x0000040000000010L});
    public static final BitSet FOLLOW_SEMI_in_literalValue4126 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_CLOSE_CURLY_in_literalValue4133 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_element_in_elementList4148 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_elementList4151 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040099L});
    public static final BitSet FOLLOW_element_in_elementList4154 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_key_in_element4179 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_element4181 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040099L});
    public static final BitSet FOLLOW_value_in_element4184 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_element4190 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldName_in_key4211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_elementIndex_in_key4217 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_fieldName4232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_elementIndex4247 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_value4262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literalValue_in_value4268 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_packageName4283 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_packageName_in_qualifiedIdent4308 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_qualifiedIdent4310 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000000L});
    public static final BitSet FOLLOW_identifier_in_qualifiedIdent4312 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_qualifiedIdent4318 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_type4333 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_type_in_type4335 = new BitSet(new long[]{0x0000010000000010L});
    public static final BitSet FOLLOW_SEMI_in_type4337 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_type4344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeLit_in_type4350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_type4356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedIdent_in_typeName4371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arrayType_in_typeLit4386 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_structType_in_typeLit4392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionType_in_typeLit4398 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceType_in_typeLit4404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sliceType_in_typeLit4410 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mapType_in_typeLit4416 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pointerType_in_typeLit4422 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_channelType_in_typeLit4428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTERFACE_in_interfaceType4447 = new BitSet(new long[]{0x000004033FFF0010L,0x0180000C00040019L});
    public static final BitSet FOLLOW_methodSpec_in_interfaceType4451 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_interfaceType4453 = new BitSet(new long[]{0x000004033FFF0010L,0x0180000C00040019L});
    public static final BitSet FOLLOW_SEMI_in_interfaceType4457 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_CLOSE_CURLY_in_interfaceType4467 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_methodName_in_methodSpec4486 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_signature_in_methodSpec4488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceTypeName_in_methodSpec4494 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_methodName4509 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeName_in_interfaceTypeName4524 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRUCT_in_structType4547 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_71_in_structType4553 = new BitSet(new long[]{0x000004033FFF0010L,0x0180000C00040019L});
    public static final BitSet FOLLOW_fieldDecl_in_structType4566 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_structType4568 = new BitSet(new long[]{0x000004033FFF0010L,0x0180000C00040019L});
    public static final BitSet FOLLOW_SEMI_in_structType4572 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_CLOSE_CURLY_in_structType4586 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierList_in_fieldDecl4611 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_type_in_fieldDecl4613 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_anonymousField_in_fieldDecl4621 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_tag_in_fieldDecl4630 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_anonymousField4647 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_typeName_in_anonymousField4650 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_String_Lit_in_tag4665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionType_in_functionLit4680 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_body_in_functionLit4682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_67_in_functionType4697 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_signature_in_functionType4700 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_pointerType4715 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_baseType_in_pointerType4717 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_sliceType4732 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_CLOSE_SQUARE_in_sliceType4734 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_elementType_in_sliceType4736 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_119_in_mapType4751 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_99_in_mapType4753 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_keyType_in_mapType4755 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_CLOSE_SQUARE_in_mapType4757 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_elementType_in_mapType4759 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_arrayType4774 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_arrayLength_in_arrayType4776 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_CLOSE_SQUARE_in_arrayType4778 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_elementType_in_arrayType4780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_82_in_channelType4806 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_120_in_channelType4808 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_elementType_in_channelType4810 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_120_in_channelType4828 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_channelType4830 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_elementType_in_channelType4832 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_120_in_channelType4839 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_elementType_in_channelType4841 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_arrayLength4856 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_baseType4871 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_keyType4886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_elementType4901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_APPEND_in_identifier4932 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CAP_in_identifier4942 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CLOSE_in_identifier4952 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CLOSED_in_identifier4962 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CMPLX_in_identifier4972 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COPY_in_identifier4982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMAG_in_identifier4992 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEN_in_identifier5002 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MAKE_in_identifier5012 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PANIC_in_identifier5022 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PRINT_in_identifier5032 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PRINTLN_in_identifier5042 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REAL_in_identifier5052 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_98_in_identifier5062 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_identifier5079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_67_in_synpred1_GoSource346 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_synpred1_GoSource349 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_synpred2_GoSource570 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_synpred2_GoSource572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameters_in_synpred3_GoSource786 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_result_in_synpred3_GoSource788 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameters_in_synpred4_GoSource817 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierList_in_synpred5_GoSource898 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040059L});
    public static final BitSet FOLLOW_70_in_synpred5_GoSource900 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_type_in_synpred5_GoSource903 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labeledStatement_in_synpred6_GoSource1024 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignment_in_synpred7_GoSource1129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_shortVarDecl_in_synpred8_GoSource1141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_incDecStmt_in_synpred9_GoSource1153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionStmt_in_synpred10_GoSource1165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simpleStmt_in_synpred11_GoSource1204 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_synpred11_GoSource1206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred12_GoSource1226 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_block_in_synpred12_GoSource1228 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeSwitchStmt_in_synpred13_GoSource1272 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exprSwitchStmt_in_synpred14_GoSource1284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simpleStmt_in_synpred15_GoSource1316 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_synpred15_GoSource1318 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expression_in_synpred15_GoSource1320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simpleStmt_in_synpred16_GoSource1338 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_synpred16_GoSource1340 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simpleStmt_in_synpred17_GoSource1444 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_synpred17_GoSource1446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sendExpr_in_synpred18_GoSource1821 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_recvExpr_in_synpred19_GoSource1835 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred20_GoSource1890 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002004L});
    public static final BitSet FOLLOW_set_in_synpred20_GoSource1896 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_synpred20_GoSource1926 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_initStmt_in_synpred21_GoSource2001 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_synpred21_GoSource2003 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred22_GoSource2017 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_71_in_synpred22_GoSource2019 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_initStmt_in_synpred23_GoSource2078 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_synpred23_GoSource2080 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_condition_in_synpred24_GoSource2110 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_SEMI_in_synpred24_GoSource2112 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_synpred26_GoSource2523 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_synpred26_GoSource2525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtinCall_in_synpred27_GoSource2707 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_operand_in_synpred28_GoSource2719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conversion_in_synpred29_GoSource2731 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_call_in_synpred30_GoSource2778 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selector_in_synpred31_GoSource2794 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_index_in_synpred32_GoSource2810 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeAssertion_in_synpred33_GoSource2826 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_slice_in_synpred34_GoSource2842 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_synpred36_GoSource3033 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_synpred36_GoSource3035 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_expressionList_in_synpred36_GoSource3037 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_synpred37_GoSource3055 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_82_in_synpred38_GoSource3268 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_primaryExpr_in_synpred38_GoSource3270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_synpred39_GoSource3285 = new BitSet(new long[]{0x000000033FFFFC20L,0x019000FC00040019L});
    public static final BitSet FOLLOW_primaryExpr_in_synpred39_GoSource3287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_synpred40_GoSource3822 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_methodExpr_in_synpred41_GoSource3834 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compositeLit_in_synpred42_GoSource3945 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x01FFFFFFFFFFFEFFL});
    public static final BitSet FOLLOW_set_in_synpred42_GoSource3947 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_key_in_synpred43_GoSource4172 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_synpred43_GoSource4174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldName_in_synpred44_GoSource4206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_synpred45_GoSource4299 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_synpred45_GoSource4301 = new BitSet(new long[]{0x000000003FFF0000L,0x0000000400000000L});
    public static final BitSet FOLLOW_identifier_in_synpred45_GoSource4303 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_82_in_synpred46_GoSource4796 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_120_in_synpred46_GoSource4798 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_elementType_in_synpred46_GoSource4800 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_120_in_synpred47_GoSource4818 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_synpred47_GoSource4820 = new BitSet(new long[]{0x000000033FFF0000L,0x0180000C00040019L});
    public static final BitSet FOLLOW_elementType_in_synpred47_GoSource4822 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtinName_in_synpred48_GoSource4917 = new BitSet(new long[]{0x0000000000000002L});

}