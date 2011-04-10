grammar GoSource;

options {
  language     = Java;
  output       = AST;
  ASTLabelType = CommonTree;
}

@header {
package com.googlecode.goclipse.go.antlr;
}

@lexer::header {
package com.googlecode.goclipse.go.antlr;
}

@lexer::members {
	List tokens = new ArrayList();
	
	public void emit(Token token) {
		state.token = token;
		tokens.add(token);
	}
	
	public Token nextToken() {
		Token t = super.nextToken();
		int tType = t.getType();
		int la = input.LA(1);
		if (la == ' ' || la == '/' || la == '\t' || la == '\f') { //start of hidden
			Token next = super.nextToken();
			while (next.getType() != Token.EOF && next.getChannel() == HIDDEN) {
				String text = next.getText();
				if (text.contains("\n") || text.contains("\r")) {
					la = '\n';
					break;
				}
				la = input.LA(1);
				if (la == ' ' || la == '/' || la == '\t' || la == '\f') { //start of hidden
					next = super.nextToken();
				} else {
					break;
				}
	
			}
		}
	
		if ((la == '\n' || la == '\r')
				&& (tType == Identifier // last identifier on the line
						|| tType == Integer //last integer on the line
						|| tType == Hex_Lit //last hex
						|| tType == Octal_Lit //last octal
						|| tType == Float_Lit //last float
						|| tType == Imaginary_Lit //last imaginary
						|| tType == Char_Lit //last char
						|| tType == String_Lit //last string
						|| tType == APPEND || tType == CAP
						|| tType == CLOSE
						|| tType == CLOSED || tType == CMPLX
						|| tType == COPY
						|| tType == IMAG || tType == LEN
						|| tType == MAKE
						|| tType == PANIC || tType == PRINT
						|| tType == PRINTLN
						|| tType == REAL || tType == RECOVER
						|| tType == IF
						|| tType == STRUCT || tType == INTERFACE
						|| tType == TYPE
						|| tType == VAR || tType == BREAK
						|| tType == RETURN
						|| tType == CONTINUE
						|| tType == FALLTHROUGH
						|| tType == PLUSPLUS
						|| tType == MINUSMINUS
						|| tType == CLOSE_BRACKET || tType == CLOSE_SQUARE || tType == CLOSE_CURLY)) {
			t = emit();
			t.setChannel(Token.DEFAULT_CHANNEL);
			t.setType(SEMI);
			t.setText(";");
		}
		
		if (la == ')' || la == '}') {
			if (tType != SEMI) {
				//this introduces problems with SEMI in wrong places
				//the parser checks for those places. ugly. 
				//TODO: find a better way
				t = emit();
				t.setChannel(Token.DEFAULT_CHANNEL);
				t.setType(SEMI);
				t.setText(";");
			}
		}
		if (tokens.size() == 0) {
			return Token.EOF_TOKEN;
		}
		return (Token) tokens.remove(0);
}
}

@parser::members {
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
}

program
:
  packageClause SEMI!
  (
    (
      importDecl
      | topLevelDecl
    )?
    SEMI!
  )*
  EOF
  ;

packageClause
  :
  'package'^ packageName
  ;

importDecl
  :
  'import'^
  (
    importSpec
    | '(' (importSpec SEMI!)* SEMI!? //this is a fake SEMI introduced by Lexer
    ')'
  )
  ;

importSpec
@init{
  Import imp   = new Import();
  imp.type     = Import.Type.NORMAL;
}
  :
  (
    '.'                    
    {
      imp.type = Import.Type.FULL_IMPORT;
    }
    
    | packageName          
    {
      if($importSpec.text.contains("_")){
        imp.type = Import.Type.INIT_ONLY;
      }
      else{
        imp.type = Import.Type.ALIAS; imp.alias = $packageName.text;
      }
    }
  )?
  importPath
  {
    // emit import
    imp.displayText= $importSpec.text;
    imp.line       = $start.getLine();
    imp.filepath   = ctx.filename;
    imp.start      = $start.getCharPositionInLine()+1;
    imp.importPath = $importPath.text;
    imp.scopename  = getScopeName();
    ctx.imports.add(imp);
  }
  ;

importPath
  :
  String_Lit
  ;

topLevelDecl
  :
  declaration
  | ('func'^ '(') => methodDecl
  | functionDecl
  ;

declaration
  :
  constDecl
  | typeDecl
  | varDecl
  ;

typeDecl
  :
  'type'^
  (
    typeSpec
    | '(' (typeSpec SEMI)* SEMI!? //this is a fake SEMI introduced by Lexer
    ')'
  )
  ;

typeSpec
  :
  identifier type
  ;

varDecl
  :
  'var'^
  (
    varSpec
    | '('^ (varSpec SEMI)* SEMI!? //this is a fake SEMI introduced by Lexer
    ')'
  )
  ;

varSpec
  :
  ilist = identifierList
  (
    '='^ expressionList
    |
    (
      (type '=') => a=type '='^ expressionList
      {
        for(Var var:ilist.vars){
          Type type        = new Type();
          type.displayText = $a.text;
          var.type         = type;
          var.displayText  = var.insertionText +" : "+ type.displayText;
          currentScope.variables.add(var);
        }
      }
      | b=type
      {
        for(Var var:ilist.vars){
          Type type        = new Type();
          type.displayText = $b.text;
          var.type         = type;
          var.displayText  = var.insertionText +" : "+ type.displayText;
          currentScope.variables.add(var);
        }
      }
    )
  )
  ;

methodDecl
@init{
  pushScope();
  currentScope.type = Scope.Type.FUNCTION;
}
  :
  'func'^ receiver identifier signature body?
  {
    // emit func
    Func func          = new Func();
    func.scopename     = getScopeName();
    func.insertionText = $identifier.text;
    func.displayText   = $receiver.var.type.displayText +":"+$identifier.text + " "+$signature.text.replace(";", "");
    func.line          = $start.getLine();
    func.start         = $start.getCharPositionInLine()+1; 
    func.receiver      = $receiver.var;
    
    // add method to parent, but vars to current scope
    currentScope.parent.methods.add(func);
    
    currentScope.name      = getScopeName();
    currentScope.start     = func.start;
    currentScope.startLine = func.line;
    currentScope.stop      = $body.stopPosition;
    currentScope.stopLine  = $body.stopLine;
    popScope();
  }
  ;

receiver returns[Var var;]
@init{
  $receiver.var             = new Var();
  $receiver.var.scopename   = getScopeName();
  $receiver.var.filepath    = ctx.filename;
  $receiver.var.line        = $start.getLine();
  $receiver.var.start       = $start.getCharPositionInLine()+1; 
  
//  System.out.println($receiver.var);

  
}
  :
  '(' (a=identifier {$receiver.var.displayText = $a.text+" ";})? 
      ('*' {$receiver.var.isPointer=true;})? 
      b=identifier{$receiver.var.type.displayText = $b.text;} 
      SEMI!? //this is a fake SEMI introduced by Lexer
      {
        $receiver.var.displayText   = $a.text + " : " + ($receiver.var.isPointer?"*":"") + $b.text; 
        $receiver.var.insertionText = $a.text;
        currentScope.variables.add($receiver.var);
      }
  
  ')'
  ;

functionDecl
@init{
  pushScope();
  currentScope.type = Scope.Type.FUNCTION;
}
  :
  'func'^ identifier signature body?
  {
      // emit func
    Func func          = new Func();
    func.scopename     = getScopeName();
    func.insertionText = $identifier.text;
    func.displayText   = $identifier.text + $signature.text.replace(";", ""); 
    func.line          = $start.getLine();
    func.start         = $start.getCharPositionInLine()+1;
    
    // add func to parent, but vars to current scope
    currentScope.parent.functions.add(func);
    
    currentScope.name      = getScopeName();
    currentScope.start     = func.start;
    currentScope.startLine = func.line;
    currentScope.stop      = $body.stopPosition;
    currentScope.stopLine  = $body.stopLine;
    System.out.println(">>>> "+currentScope.stopLine);
    popScope();
  }
  ;

signature
  :
  (parameters result) => parameters result
  | parameters
  ;

result
  :
  (parameters) => parameters
  | type
  ;

parameters
  :
  '(' (parametersList ','?)? SEMI!? //this is a fake SEMI introduced by Lexer
  ')'
  ;

parametersList
  :
  parameterDecl (',' parameterDecl)*
  ;

parameterDecl
  :
  (identifierList '...'? type) => ilist=identifierList '...'? type
  {
        for(Var var:ilist.vars){
          Type type        = new Type();
          type.displayText = $type.text;
          var.type         = type;
          var.displayText  = var.insertionText +" : "+ type.displayText;
	        currentScope.variables.add(var);
        }
  }
  | '...'? type
  {
  }
  ;

body returns [int stopLine, int stopPosition]
  :
  b=block
  {
    retval.stopLine        = $b.stopLine;
    retval.stopPosition    = $b.stopPosition;
  }
  ;

block returns [int stopLine, int stopPosition]
  :
  '{'^ 

  
  (statement SEMI)*
   
  a='}'
  {
    retval.stopLine        = $a.getLine();
    retval.stopPosition    = $a.getCharPositionInLine();
  }
  
  ;

statement
  :
  (labeledStatement) => labeledStatement
  | declaration
  | deferStmt
  | returnStmt
  | goStmt
  | breakStmt
  | continueStmt
  | fallthroughStmt
  | block
  | ifStmt
  | switchStmt
  | selectStmt
  | gotoStmt
  | forStmt
  | simpleStmt
  ;

simpleStmt
  :
  (assignment) => assignment
  | (shortVarDecl) => shortVarDecl
  | (incDecStmt) => incDecStmt
  | (expressionStmt) => expressionStmt //must come after incDecStmt
  |
  //emptyStatement
  ;

ifStmt
  :
  'if'
  (
    (simpleStmt SEMI) => simpleStmt SEMI expression block
    | (expression block) => expression block
    | block
  )
  ('else' statement)?
  ;

switchStmt
  :
  (typeSwitchStmt) => typeSwitchStmt
  | (exprSwitchStmt) => exprSwitchStmt
  ;

exprSwitchStmt
  :
  'switch'^
  (
    (simpleStmt SEMI expression) => simpleStmt SEMI expression
    | (simpleStmt SEMI) => simpleStmt SEMI
    | expression
  )?
  '{' exprSwitchClause* '}'
  ;

exprSwitchClause
  :
  exprSwitchCase ':' (statement SEMI)*
  ;

exprSwitchCase
  :
  'case'^ expressionList
  | 'default'
  ;

typeSwitchStmt
  :
  'switch'^
  (
    (simpleStmt SEMI) => simpleStmt SEMI typeSwitchGuard
    | typeSwitchGuard
  )
  '{'
  {pushScope();}
       typeSwitchClause* 
  {popScope();}
  '}'
  ;

typeSwitchGuard
  :
  (identifier ':=')? primaryExpr '.' '(' 'type' SEMI!? //this is a fake SEMI introduced by Lexer
  ')'
  ;

typeSwitchClause
  :
  typeSwitchCase ':' (statement SEMI)*
  ;

typeSwitchCase
  :
  'case'^ typeList
  | 'default'
  ;

typeList 
  :
  type (',' type)*
  ;

goStmt
  :
  'go'^ expression
  ;

gotoStmt
  :
  'goto' label
  ;

breakStmt
  :
  BREAK label?
  ;

continueStmt
  :
  CONTINUE label?
  ;

fallthroughStmt
  :
  FALLTHROUGH
  ;

returnStmt
  :
  RETURN^ expressionList?
  ;

deferStmt
  :
  'defer'^ expression
  ;

assignment
  :
  expressionList assignOp^ expressionList
  ;

selectStmt
  :
  'select' '{' {pushScope();} commClause* {popScope();} '}'
  ;

commClause
  :
  commCase ':' (statement SEMI)*
  ;

commCase
  :
  'case'
  (
    (sendExpr) => sendExpr
    | (recvExpr) => recvExpr
  )
  | 'default'
  ;

sendExpr
  :
  relational '<-' relational
  ;

recvExpr
  :
  (
    expression
    (
      '='
      | ':='
    )
    '<-'
  )
    => expression
  (
    '='
    | ':='
  )
  '<-' relational
  | '<-' relational
  ;

forStmt
  :
  'for'^
  (
    (initStmt SEMI) => forClause
    | (expression '{') => condition
    | rangeClause
  )?
  block
  ;

condition
  :
  expression
  ;

forClause
  :
  (
    (initStmt SEMI) => initStmt SEMI
    | SEMI
  )
  (
    (condition SEMI) => condition SEMI
    | SEMI
  )
  (postStmt) => postStmt
  ;

rangeClause
  :
  expression (',' expression)?
  (
    '='
    | ':='
  )
  'range' expression
  ;

initStmt
  :
  simpleStmt
  ;

postStmt
  :
  simpleStmt
  ;

assignOp
  :
  '='
  | '*='
  | '/='
  | '+='
  | '-='
  | '|='
  | '^='
  | '%='
  | '<<='
  | '>>='
  | '&='
  | '&^='
  | '<-='
  ;

shortVarDecl
  :
  ilist=identifierList ':='^ expressionList
  {
  }
  ;

labeledStatement
  :
  label ':'^ statement
  ;

label
  :
  identifier
  ;

expressionStmt
  :
  expression
  ;

incDecStmt
  :
  expression
  (
    '++'^
    | '--'^
  )
  ;

constDecl
  :
  'const'^
  (
    constSpec
    | '(' (constSpec SEMI)* SEMI!? //this is a fake SEMI introduced by Lexer
    ')'
  )
  ;

constSpec
  :
  identifierList
  (
    (
      (
        (type '=') => (type '=')
      )
      | '='
    )
    expressionList
  )?
  ;

identifierList returns[List<Var> vars]
@init{
  $identifierList.vars = new ArrayList<Var>();
}
  :
  a=identifier
  {
    Var var = new Var();
    var.insertionText = $a.text;
    $identifierList.vars.add(var);
  } 
  
  (','^ b=identifier
  {
    Var var = new Var();
    var.insertionText = $b.text;
    $identifierList.vars.add(var);
  }
  )*
  
  ;

expressionList returns [List<Type> types]
@init{
  $expressionList.types = new ArrayList();
}
  :
  expression (','^ expression)*
  ;

expression returns [List<Type> types]
@init{
  $expression.types = new ArrayList();
}
  :
  //('<-' relational) => ('<-' relational)
  //|
  logical_or
  ;

base
returns [List<Type> types]
@init{
  $base.types = new ArrayList();
}
  :
  (builtinCall) => builtinCall
  | (operand) => operand
  | (conversion) => conversion
  ;

primaryExpr
returns [List<Type> types]
@init{
  $primaryExpr.types = new ArrayList();
}
  :
  base
  (
    (
      (call) => call
      | (selector) => selector
      | (index) => index
      | (typeAssertion) => typeAssertion
      | (slice) => slice
    )
  )*
  ;

builtinName
returns [List<Type> types]
@init{
  $builtinName.types = new ArrayList();
}
  :
  'append'
  | 'cap'
  | 'close'
  | 'closed'
  | 'cmplx'
  | 'copy'
  | 'imag'
  | 'len'
  | 'make'
  | 'panic'
  | 'print'
  | 'println'
  | 'real'
  | 'recover'
  ;

builtinCall
returns [List<Type> types]
@init{
  $builtinCall.types = new ArrayList();
}
  :
  (builtinName '(') => builtinName '(' (builtinArgs (',')?)? SEMI!? //this is a fake SEMI introduced by Lexer
  ')'
  ;

builtinArgs
  :
  (
    (type ',' expressionList) => type ',' expressionList
    | (expressionList) => expressionList
    | type
  )
  ;

selector
  :
  '.' identifier
  ;

typeAssertion
  :
  '.' '(' type SEMI!? //this is a fake SEMI introduced by Lexer
  ')'
  ;

index
  :
  '[' expression ']'
  ;

slice
  :
  '[' (expression)? ':' (expression)? ']'
  ;

call
  :
  '('^ (argumentList ','?)? SEMI!? //this is a fake SEMI introduced by Lexer
  ')'
  ;

argumentList
  :
  expressionList '...'?
  ;

conversion
  :
  type '(' expression SEMI!? //this is a fake SEMI introduced by Lexer
  ')'
  ;

unary
returns [List<Type> types]
@init{
  $unary.types = new ArrayList();
}
  :
  ('<-' primaryExpr) => '<-'^ primaryExpr
  | ('*' primaryExpr) => '*'^ primaryExpr
  |
  (
    '+'^
    | '-'^
    | '^'^
    | '&'^
  )*
  primaryExpr
  ;

mult
returns [List<Type> types]
@init{
  $mult.types = new ArrayList();
}
  :
  unary
  (
    (
      '*'^
      | '/'^
      | '%'^
      | '<<'^
      | '>>'^
      | '&'^
      | '&^'^
    )
    unary
  )*
  ;

add
returns [List<Type> types]
@init{
  $add.types = new ArrayList();
}
  :
  mult
  (
    (
      '+'^
      | '-'^
      | '|'^
      | '^'^
    )
    mult
  )*
  ;

relational
returns [List<Type> types]
@init{
  $relational.types = new ArrayList();
}
  :
  add
  (
    (
      '=='^
      | '!='^
      | '<'^
      | '<='^
      | '>'^
      | '>='^
    )
    add
  )*
  ;

channel
returns [List<Type> types]
@init{
  $channel.types = new ArrayList();
}
  :
  relational ('<-'^ relational)*
  ;

negation
returns [List<Type> types]
@init{
  $negation.types = new ArrayList();
}
  :
  '!'^* channel
  ;

logical_and
returns [List<Type> types]
@init{
  $logical_and.types = new ArrayList();
}
  :
  negation ('&&'^ negation)*
  ;

logical_or 
returns [List<Type> types]
@init{
  $logical_or.types = new ArrayList();
}
  :
  logical_and ('||'^ logical_and)*
  ;

operand
returns [List<Type> types]
@init{
  $operand.types = new ArrayList();
}
  :
  (literal) => literal
  | (methodExpr) => methodExpr
  | '(' expression SEMI!? //this is a fake SEMI introduced by Lexer
  ')'
  | qualifiedIdent
  ;

methodExpr
  :
  receiverType '.' methodName
  ;

receiverType
  :
  typeName
  | '(' '*' typeName SEMI!? //this is a fake SEMI introduced by Lexer
  ')'
  ;

literal
returns [List<Type> types]
@init{
  $literal.types = new ArrayList();
}
  :
  basicLit
  | (compositeLit ~('else' )) => compositeLit
  | functionLit
  ;

basicLit
  :
  Integer
  | Octal_Lit
  | Hex_Lit
  | Float_Lit
  | Imaginary_Lit
  | Char_Lit
  | String_Lit
  ;

compositeLit
returns [List<Type> types]
@init{
  $compositeLit.types = new ArrayList();
}
  :
  literalType literalValue
  ;

literalType
returns [List<Type> types]
@init{
  $literalType.types = new ArrayList();
}
  :
  structType
  | arrayType
  | '[' '...' ']' elementType
  | sliceType
  | mapType
  | typeName
  ;

literalValue
  :
  '{'^ (elementList (','!)?)? SEMI!? //this is a fake SEMI introduced by Lexer
  '}'
  ;

elementList
  :
  element (','^ element)*
  ;

element
  :
  (key ':') => key ':'^ value
  | value
  ;

key
  :
  (fieldName) => fieldName
  | elementIndex
  ;

fieldName
  :
  identifier
  ;

elementIndex
  :
  expression
  ;

value
  :
  expression
  | literalValue
  ;

packageName
  :
  identifier
  ;

qualifiedIdent
  :
  (identifier '.' identifier) => packageName '.' identifier
  | identifier
  ;

type
  :
  '(' type SEMI!? //this is a fake SEMI introduced by Lexer
  ')'
  | typeLit
  | typeName
  ;

typeName
  :
  qualifiedIdent
  ;

typeLit
  :
  arrayType
  | structType
  | functionType
  | interfaceType
  | sliceType
  | mapType
  | pointerType
  | channelType
  ;

interfaceType
@init{
  pushScope();
}
  :
  'interface'^ (methodSpec SEMI)* SEMI!? //this is a fake SEMI introduced by Lexer
  
  '}'
  {popScope();}
  ;

methodSpec
  :
  methodName signature
  | interfaceTypeName
  ;

methodName
  :
  identifier
  ;

interfaceTypeName
  :
  typeName
  ;

structType
returns [List<Type> types]
@init{
  $structType.types = new ArrayList();
  {
    pushScope();
  }
}
  :
  'struct'^ 
  '{' 
  

  
  (fieldDecl SEMI)* SEMI!? //this is a fake SEMI introduced by Lexer
  

  
  '}'
  {
    popScope();
  }
  ;

fieldDecl
  :
  (
    identifierList type
    | anonymousField
  )
  (tag)?
  ;

anonymousField
  :
  '*'? typeName
  ;

tag
  :
  String_Lit
  ;

functionLit
  :
  functionType body
  ;

functionType
  :
  'func'^ signature
  ;

pointerType
  :
  '*' baseType
  ;

sliceType
  :
  '[' ']' elementType
  ;

mapType
  :
  'map' '[' keyType ']' elementType
  ;

arrayType
  :
  '[' arrayLength ']' elementType
  ;

channelType
  :
  ('<-' 'chan' elementType) => ('<-' 'chan' elementType)
  | ('chan' '<-' elementType) => ('chan' '<-' elementType)
  | 'chan' elementType
  ;

arrayLength
  :
  expression
  ;

baseType
  :
  type
  ;

keyType
  :
  type
  ;

elementType
  :
  type
  ;

identifier
  :
  (builtinName) =>
  (
    t='append'
    | t='cap'
    | t='close'
    | t='closed'
    | t='cmplx'
    | t='copy'
    | t='imag'
    | t='len'
    | t='make'
    | t='panic'
    | t='print'
    | t='println'
    | t='real'
    | t='recover'
  )
  
  {
   t.setType(Identifier);
  }
  | Identifier
  ;
//LEXER

APPEND
  :
  'append'
  ;

CAP
  :
  'cap'
  ;

CLOSE
  :
  'close'
  ;

CLOSED
  :
  'closed'
  ;

CMPLX
  :
  'cmplx'
  ;

COPY
  :
  'copy'
  ;

IMAG
  :
  'imag'
  ;

LEN
  :
  'len'
  ;

MAKE
  :
  'make'
  ;

PANIC
  :
  'panic'
  ;

PRINT
  :
  'print'
  ;

PRINTLN
  :
  'println'
  ;

REAL
  :
  'real'
  ;

RECOVER
  :
  'recover*'
  ;

IF
  :
  'if'
  ;

STRUCT
  :
  'struct'
  ;

INTERFACE
  :
  'interface'
  ;

TYPE
  :
  'type'
  ;

VAR
  :
  'var'
  ;

fragment
Unicode_Letter
  :
  ('\u0041'..'\u005A')
  | ('\u0061'..'\u007A')
  | '\u00AA'
  | '\u00B5'
  | '\u00BA'
  | ('\u00C0'..'\u00D6')
  | ('\u00D8'..'\u00F6')
  | ('\u00F8'..'\u021F')
  | ('\u0222'..'\u0233')
  | ('\u0250'..'\u02AD')
  | ('\u02B0'..'\u02B8')
  | ('\u02BB'..'\u02C1')
  | ('\u02D0'..'\u02D1')
  | ('\u02E0'..'\u02E4')
  | '\u02EE'
  | '\u037A'
  | '\u0386'
  | ('\u0388'..'\u038A')
  | '\u038C'
  | ('\u038E'..'\u03A1')
  | ('\u03A3'..'\u03CE')
  | ('\u03D0'..'\u03D7')
  | ('\u03DA'..'\u03F3')
  | ('\u0400'..'\u0481')
  | ('\u048C'..'\u04C4')
  | ('\u04C7'..'\u04C8')
  | ('\u04CB'..'\u04CC')
  | ('\u04D0'..'\u04F5')
  | ('\u04F8'..'\u04F9')
  | ('\u0531'..'\u0556')
  | '\u0559'
  | ('\u0561'..'\u0587')
  | ('\u05D0'..'\u05EA')
  | ('\u05F0'..'\u05F2')
  | ('\u0621'..'\u063A')
  | ('\u0640'..'\u064A')
  | ('\u0671'..'\u06D3')
  | '\u06D5'
  | ('\u06E5'..'\u06E6')
  | ('\u06FA'..'\u06FC')
  | '\u0710'
  | ('\u0712'..'\u072C')
  | ('\u0780'..'\u07A5')
  | ('\u0905'..'\u0939')
  | '\u093D'
  | '\u0950'
  | ('\u0958'..'\u0961')
  | ('\u0985'..'\u098C')
  | ('\u098F'..'\u0990')
  | ('\u0993'..'\u09A8')
  | ('\u09AA'..'\u09B0')
  | '\u09B2'
  | ('\u09B6'..'\u09B9')
  | ('\u09DC'..'\u09DD')
  | ('\u09DF'..'\u09E1')
  | ('\u09F0'..'\u09F1')
  | ('\u0A05'..'\u0A0A')
  | ('\u0A0F'..'\u0A10')
  | ('\u0A13'..'\u0A28')
  | ('\u0A2A'..'\u0A30')
  | ('\u0A32'..'\u0A33')
  | ('\u0A35'..'\u0A36')
  | ('\u0A38'..'\u0A39')
  | ('\u0A59'..'\u0A5C')
  | '\u0A5E'
  | ('\u0A72'..'\u0A74')
  | ('\u0A85'..'\u0A8B')
  | '\u0A8D'
  | ('\u0A8F'..'\u0A91')
  | ('\u0A93'..'\u0AA8')
  | ('\u0AAA'..'\u0AB0')
  | ('\u0AB2'..'\u0AB3')
  | ('\u0AB5'..'\u0AB9')
  | '\u0ABD'
  | '\u0AD0'
  | '\u0AE0'
  | ('\u0B05'..'\u0B0C')
  | ('\u0B0F'..'\u0B10')
  | ('\u0B13'..'\u0B28')
  | ('\u0B2A'..'\u0B30')
  | ('\u0B32'..'\u0B33')
  | ('\u0B36'..'\u0B39')
  | '\u0B3D'
  | ('\u0B5C'..'\u0B5D')
  | ('\u0B5F'..'\u0B61')
  | ('\u0B85'..'\u0B8A')
  | ('\u0B8E'..'\u0B90')
  | ('\u0B92'..'\u0B95')
  | ('\u0B99'..'\u0B9A')
  | '\u0B9C'
  | ('\u0B9E'..'\u0B9F')
  | ('\u0BA3'..'\u0BA4')
  | ('\u0BA8'..'\u0BAA')
  | ('\u0BAE'..'\u0BB5')
  | ('\u0BB7'..'\u0BB9')
  | ('\u0C05'..'\u0C0C')
  | ('\u0C0E'..'\u0C10')
  | ('\u0C12'..'\u0C28')
  | ('\u0C2A'..'\u0C33')
  | ('\u0C35'..'\u0C39')
  | ('\u0C60'..'\u0C61')
  | ('\u0C85'..'\u0C8C')
  | ('\u0C8E'..'\u0C90')
  | ('\u0C92'..'\u0CA8')
  | ('\u0CAA'..'\u0CB3')
  | ('\u0CB5'..'\u0CB9')
  | '\u0CDE'
  | ('\u0CE0'..'\u0CE1')
  | ('\u0D05'..'\u0D0C')
  | ('\u0D0E'..'\u0D10')
  | ('\u0D12'..'\u0D28')
  | ('\u0D2A'..'\u0D39')
  | ('\u0D60'..'\u0D61')
  | ('\u0D85'..'\u0D96')
  | ('\u0D9A'..'\u0DB1')
  | ('\u0DB3'..'\u0DBB')
  | '\u0DBD'
  | ('\u0DC0'..'\u0DC6')
  | ('\u0E01'..'\u0E30')
  | ('\u0E32'..'\u0E33')
  | ('\u0E40'..'\u0E46')
  | ('\u0E81'..'\u0E82')
  | '\u0E84'
  | ('\u0E87'..'\u0E88')
  | '\u0E8A'
  | '\u0E8D'
  | ('\u0E94'..'\u0E97')
  | ('\u0E99'..'\u0E9F')
  | ('\u0EA1'..'\u0EA3')
  | '\u0EA5'
  | '\u0EA7'
  | ('\u0EAA'..'\u0EAB')
  | ('\u0EAD'..'\u0EB0')
  | ('\u0EB2'..'\u0EB3')
  | ('\u0EBD'..'\u0EC4')
  | '\u0EC6'
  | ('\u0EDC'..'\u0EDD')
  | '\u0F00'
  | ('\u0F40'..'\u0F6A')
  | ('\u0F88'..'\u0F8B')
  | ('\u1000'..'\u1021')
  | ('\u1023'..'\u1027')
  | ('\u1029'..'\u102A')
  | ('\u1050'..'\u1055')
  | ('\u10A0'..'\u10C5')
  | ('\u10D0'..'\u10F6')
  | ('\u1100'..'\u1159')
  | ('\u115F'..'\u11A2')
  | ('\u11A8'..'\u11F9')
  | ('\u1200'..'\u1206')
  | ('\u1208'..'\u1246')
  | '\u1248'
  | ('\u124A'..'\u124D')
  | ('\u1250'..'\u1256')
  | '\u1258'
  | ('\u125A'..'\u125D')
  | ('\u1260'..'\u1286')
  | '\u1288'
  | ('\u128A'..'\u128D')
  | ('\u1290'..'\u12AE')
  | '\u12B0'
  | ('\u12B2'..'\u12B5')
  | ('\u12B8'..'\u12BE')
  | '\u12C0'
  | ('\u12C2'..'\u12C5')
  | ('\u12C8'..'\u12CE')
  | ('\u12D0'..'\u12D6')
  | ('\u12D8'..'\u12EE')
  | ('\u12F0'..'\u130E')
  | '\u1310'
  | ('\u1312'..'\u1315')
  | ('\u1318'..'\u131E')
  | ('\u1320'..'\u1346')
  | ('\u1348'..'\u135A')
  | ('\u13A0'..'\u13B0')
  | ('\u13B1'..'\u13F4')
  | ('\u1401'..'\u1676')
  | ('\u1681'..'\u169A')
  | ('\u16A0'..'\u16EA')
  | ('\u1780'..'\u17B3')
  | ('\u1820'..'\u1877')
  | ('\u1880'..'\u18A8')
  | ('\u1E00'..'\u1E9B')
  | ('\u1EA0'..'\u1EE0')
  | ('\u1EE1'..'\u1EF9')
  | ('\u1F00'..'\u1F15')
  | ('\u1F18'..'\u1F1D')
  | ('\u1F20'..'\u1F39')
  | ('\u1F3A'..'\u1F45')
  | ('\u1F48'..'\u1F4D')
  | ('\u1F50'..'\u1F57')
  | '\u1F59'
  | '\u1F5B'
  | '\u1F5D'
  | ('\u1F5F'..'\u1F7D')
  | ('\u1F80'..'\u1FB4')
  | ('\u1FB6'..'\u1FBC')
  | '\u1FBE'
  | ('\u1FC2'..'\u1FC4')
  | ('\u1FC6'..'\u1FCC')
  | ('\u1FD0'..'\u1FD3')
  | ('\u1FD6'..'\u1FDB')
  | ('\u1FE0'..'\u1FEC')
  | ('\u1FF2'..'\u1FF4')
  | ('\u1FF6'..'\u1FFC')
  | '\u207F'
  | '\u2102'
  | '\u2107'
  | ('\u210A'..'\u2113')
  | '\u2115'
  | ('\u2119'..'\u211D')
  | '\u2124'
  | '\u2126'
  | '\u2128'
  | ('\u212A'..'\u212D')
  | ('\u212F'..'\u2131')
  | ('\u2133'..'\u2139')
  | ('\u2160'..'\u2183')
  | ('\u3005'..'\u3007')
  | ('\u3021'..'\u3029')
  | ('\u3031'..'\u3035')
  | ('\u3038'..'\u303A')
  | ('\u3041'..'\u3094')
  | ('\u309D'..'\u309E')
  | ('\u30A1'..'\u30FA')
  | ('\u30FC'..'\u30FE')
  | ('\u3105'..'\u312C')
  | ('\u3131'..'\u318E')
  | ('\u31A0'..'\u31B7')
  | '\u3400'
  | '\u4DB5'
  | '\u4E00'
  | '\u9FA5'
  | ('\uA000'..'\uA48C')
  | '\uAC00'
  | '\uD7A3'
  | ('\uF900'..'\uFA2D')
  | ('\uFB00'..'\uFB06')
  | ('\uFB13'..'\uFB17')
  | '\uFB1D'
  | ('\uFB1F'..'\uFB28')
  | ('\uFB2A'..'\uFB36')
  | ('\uFB38'..'\uFB3C')
  | '\uFB3E'
  | ('\uFB40'..'\uFB41')
  | ('\uFB43'..'\uFB44')
  | ('\uFB46'..'\uFBB1')
  | ('\uFBD3'..'\uFD3D')
  | ('\uFD50'..'\uFD8F')
  | ('\uFD92'..'\uFDC7')
  | ('\uFDF0'..'\uFDFB')
  | ('\uFE70'..'\uFE72')
  | '\uFE74'
  | ('\uFE76'..'\uFEFC')
  | ('\uFF21'..'\uFF3A')
  | ('\uFF41'..'\uFF5A')
  | ('\uFF66'..'\uFFBE')
  | ('\uFFC2'..'\uFFC7')
  | ('\uFFCA'..'\uFFCF')
  | ('\uFFD2'..'\uFFD7')
  | ('\uFFDA'..'\uFFDC')
  ;

fragment
Unicode_Digit
  :
  ('\u0030'..'\u0039')
  | ('\u0660'..'\u0669')
  | ('\u06F0'..'\u06F9')
  | ('\u0966'..'\u096F')
  | ('\u09E6'..'\u09EF')
  | ('\u0A66'..'\u0A6F')
  | ('\u0AE6'..'\u0AEF')
  | ('\u0B66'..'\u0B6F')
  | ('\u0BE7'..'\u0BEF')
  | ('\u0C66'..'\u0C6F')
  | ('\u0CE6'..'\u0CEF')
  | ('\u0D66'..'\u0D6F')
  | ('\u0E50'..'\u0E59')
  | ('\u0ED0'..'\u0ED9')
  | ('\u0F20'..'\u0F29')
  | ('\u1040'..'\u1049')
  | ('\u1369'..'\u1371')
  | ('\u17E0'..'\u17E9')
  | ('\u1810'..'\u1819')
  | ('\uFF10'..'\uFF19')
  ;

SEMI
  :
  ';'
  ;

BREAK
  :
  'break'
  ;

RETURN
  :
  'return'
  ;

CONTINUE
  :
  'continue'
  ;

FALLTHROUGH
  :
  'fallthrough'
  ;

PLUSPLUS
  :
  '++'
  ;

MINUSMINUS
  :
  '--'
  ;

CLOSE_BRACKET
  :
  ')'
  ;

CLOSE_SQUARE
  :
  ']'
  ;

CLOSE_CURLY
  :
  '}'
  ;

fragment
Decimal_Digit
  :
  '0'..'9'
  ;

fragment
Hex_Digit
  :
  '0'..'9'
  | 'A'..'F'
  | 'a'..'f'
  ;

fragment
Octal_Digit
  :
  '0'..'7'
  ;

Octal_Lit
  :
  '0' (Octal_Digit)+
  ;

Hex_Lit
  :
  '0'
  (
    'x'
    | 'X'
  )
  Hex_Digit+
  ;

fragment
Letter
  :
  '_'
  | Unicode_Letter
  ;

fragment
Digit
  :
  Unicode_Digit
  ;

Integer
  :
  Unicode_Digit Unicode_Digit*
  ;

Identifier
  :
  Letter
  (
    Letter
    | Unicode_Digit
  )*
  ;

Float_Lit
  :
  '.' Decimals (Exponent)?
  | Decimals
  (
    '.' (Decimals)? (Exponent)?
    | Exponent
  )
  ;

Imaginary_Lit
  :
  (
    Decimals
    | Float_Lit
  )
  'i'
  ;

fragment
InterpretedString_Lit
  :
  '\"'
  (
    '\\' '\"'
    | ~'\"'
  )*
  '\"'
  ;

fragment
RawString_Lit
  :
  '`' .* '`'
  ;

String_Lit
  :
  RawString_Lit
  | InterpretedString_Lit
  ;

fragment
Exponent
  :
  (
    'e'
    | 'E'
  )
  (
    '+'
    | '-'
  )?
  Decimals
  ;

fragment
Decimals
  :
  Decimal_Digit (Decimal_Digit)*
  ;

Char_Lit
  :
  '\''
  (
    '\\' '\''
    | ~'\''
  )*
  '\''
  ;

fragment
Unicode_Value
  :
  Little_U_Value
  | Big_U_Value
  | Escaped_char
  | .
  ;

fragment
Byte_Value
  :
  Octal_Byte_Value
  | Hex_Byte_Value
  ;

fragment
Octal_Byte_Value
  :
  '\\' Octal_Digit Octal_Digit Octal_Digit
  ;

fragment
Hex_Byte_Value
  :
  '\\' 'x' Hex_Digit Hex_Digit
  ;

fragment
Little_U_Value
  :
  '\\' 'u' Hex_Digit Hex_Digit Hex_Digit Hex_Digit
  ;

fragment
Big_U_Value
  :
  '\\' 'U' Hex_Digit Hex_Digit Hex_Digit Hex_Digit Hex_Digit Hex_Digit Hex_Digit Hex_Digit
  ;

fragment
Escaped_char
  :
  '\\'
  (
    'a'
    | 'b'
    | 'f'
    | 'n'
    | 'r'
    | 't'
    | 'v'
    | '\\'
    | '\''
    | '"'
  )
  ;

SingleLineComment
  :
  '//'
  (
    ~(
      '\n'
      | '\r'
     )
  )*
  (
    '\n'
    | '\r' ('\n')?
  )?
  
  {
   $channel = HIDDEN;
  }
  ;

MultiLineComment
  :
  '/*' .* '*/' 
              {
               $channel = HIDDEN;
              }
  ;

WS
  :
  (
    ' '
    | '\t'
    | '\n'
    | '\r'
    | '\f'
  )+
  
  {
   $channel = HIDDEN;
  }
  ;
