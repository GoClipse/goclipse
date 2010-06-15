package com.googlecode.goclipse.go;

public enum Keywords implements Comparable<Keywords>{
   
// BUILT-IN TYPES
   UINT8("uint8",    TokenClass.TYPE,"unsigned 8-bit integers (0 to 255)"),                      
   UINT16("uint16",  TokenClass.TYPE,"unsigned 16-bit integers (0 to 65,535)"),                   
   UINT32("uint32",  TokenClass.TYPE,"unsigned 32-bit integers (0 to 4,294,967,295)"),              
   UINT64("uint64",  TokenClass.TYPE,"unsigned 64-bit integers (0 to 18,446,744,073,709,551,615)"),    
   INT8("int8",      TokenClass.TYPE,"signed  8-bit integers (-128 to 127)"),                    
   INT16("int16",    TokenClass.TYPE,"signed 16-bit integers (-32,768 to 32,767)"),                
   INT32("int32",    TokenClass.TYPE,"signed 32-bit integers (-2,147,483,648 to 2,147,483,647)"),      
   INT64("int64",    TokenClass.TYPE,"signed 64-bit integers (-9,223,372,036,854,775,808 to 9,223,372,036,854,775,807)"),
   FLOAT32("float32",TokenClass.TYPE,"IEEE-754 32-bit floating-point numbers"),  
   FLOAT64("float64",TokenClass.TYPE,"IEEE-754 64-bit floating-point numbers"),  
   BYTE("byte",TokenClass.TYPE,""),        // familiar alias for uint8
   UINT("uint",      TokenClass.TYPE,"either 32 or 64 bits depending on architecture"),      
   INT("int",        TokenClass.TYPE,"either 32 or 64 bits depending on architecture"),      
   FLOAT("float",    TokenClass.TYPE,"either 32 or 64 bits depending on architecture"),      
   UINTPTR("uintptr",TokenClass.TYPE,"an unsigned integer large enough to store the uninterpreted bits of a pointer value"),  
   STRING("string", TokenClass.TYPE,"A string type represents the set of string values. Strings behave like arrays of bytes but are immutable: once created, it is impossible to change the contents of a string."),
   BOOL("bool", TokenClass.TYPE, ""),
   CHAN("chan",TokenClass.TYPE,""), 
   MAP("map",TokenClass.TYPE,"   <html>   \n" +
         "  <head>   \n" +
         "  <meta content=\"text/html; charset=ISO-8859-1\" \n" +
         "  http-equiv=\"Content-Type\">  \n" +
         "  <title></title>   \n" +
         "  </head>  \n" +
         "  <body>   \n" +
         "  <meta charset=\"utf-8\" id=\"webkit-interchange-charset\">  \n" +
         "  <span class=\"Apple-style-span\" \n" +
         "  style=\"border-collapse: separate; color: rgb(0, 0, 0); font-family: Times; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; font-size: medium;\"><span  \n" +
         "  class=\"Apple-style-span\" \n" +
         "  style=\"font-family: Helvetica,Arial,sans-serif; font-size: 13px;\"> \n" +
         "  <h3 id=\"Map_types\" \n" +
         "  style=\"font-family: Helvetica,Arial,sans-serif; margin-bottom: 0.25em;\">Map \n" +
         "  types</h3>  \n" +
         "  <p>A map is an unordered group of elements of one type, called the   \n" +
         "  element type, indexed by a set of unique<span   \n" +
         "  class=\"Apple-converted-space\">&nbsp;</span><i>keys</i><span  \n" +
         "  class=\"Apple-converted-space\">&nbsp;</span>of another type, called the   \n" +
         "  key type. A map value may be<span class=\"Apple-converted-space\">&nbsp;</span><code   \n" +
         "  style=\"font-size: 13px; font-family: monospace; color: rgb(15, 57, 141);\">nil</code>.</p>  \n" +
         "  <pre class=\"ebnf\"  \n" +
         "  style=\"margin: 1em 0px 0px; padding: 0.99em; overflow: auto; font-size: 9pt; background-color: rgb(255, 255, 233); line-height: 15px; word-wrap: break-word;\"><a   \n" +
         "  id=\"MapType\" style=\"text-decoration: none;\">MapType</a> = \"map\" \"[\" <a   \n" +
         "  href=\"#KeyType\" class=\"noline\"  \n" +
         "  style=\"text-decoration: none; color: rgb(15, 57, 141);\">KeyType</a> \"]\" <a   \n" +
         "  href=\"#ElementType\" class=\"noline\" \n" +
         "  style=\"text-decoration: none; color: rgb(15, 57, 141);\">ElementType</a> .   \n" +
         "  <a id=\"KeyType\" style=\"text-decoration: none;\">KeyType</a> = <a  \n" +
         "  href=\"#Type\" class=\"noline\"  \n" +
         "  style=\"text-decoration: none; color: rgb(15, 57, 141);\">Type</a> . \n" +
         "  </pre>   \n" +
         "  <p>The comparison operators<span class=\"Apple-converted-space\">&nbsp;</span><code \n" +
         "  style=\"font-size: 13px; font-family: monospace; color: rgb(15, 57, 141);\">==</code><span   \n" +
         "  class=\"Apple-converted-space\">&nbsp;</span>and<span \n" +
         "  class=\"Apple-converted-space\">&nbsp;</span><code \n" +
         "  style=\"font-size: 13px; font-family: monospace; color: rgb(15, 57, 141);\">!=</code><span   \n" +
         "  class=\"Apple-converted-space\">&nbsp;</span>(&sect;<a   \n" +
         "  href=\"#Comparison_operators\"   \n" +
         "  style=\"text-decoration: none; color: rgb(15, 57, 141);\">Comparison \n" +
         "  operators</a>) must be fully defined for operands of the key type; thus \n" +
         "  the key type must be a boolean, numeric, string, pointer, function,  \n" +
         "  interface, map, or channel type. If the key type is an interface type,  \n" +
         "  these comparison operators must be defined for the dynamic key values;  \n" +
         "  failure will cause a run-time error.</p>  \n" +
         "  <pre  \n" +
         "  style=\"margin: 1em 0px 0px; padding: 0.99em; overflow: auto; font-size: 9pt; background-color: rgb(248, 248, 255); line-height: 15px; word-wrap: break-word;\">map [string] int \n" +
         "  map [*T] struct { x, y float }   \n" +
         "  map [string] interface {}  \n" +
         "  </pre>   \n" +
         "  <p>The number of elements is called the length and is never negative.   \n" +
         "  The length of a map<span class=\"Apple-converted-space\">&nbsp;</span><code   \n" +
         "  style=\"font-size: 13px; font-family: monospace; color: rgb(15, 57, 141);\">m</code>can   \n" +
         "  be discovered using the built-in function<span  \n" +
         "  class=\"Apple-converted-space\">&nbsp;</span><code \n" +
         "  style=\"font-size: 13px; font-family: monospace; color: rgb(15, 57, 141);\">len(m)</code><span  \n" +
         "  class=\"Apple-converted-space\">&nbsp;</span>and may change during   \n" +
         "  execution. Values may be added and removed during execution using \n" +
         "  special forms of<span class=\"Apple-converted-space\">&nbsp;</span><a   \n" +
         "  href=\"#Assignments\"   \n" +
         "  style=\"text-decoration: none; color: rgb(15, 57, 141);\">assignment</a>.</p> \n" +
         "  <p>The value of an uninitialized map is<span \n" +
         "  class=\"Apple-converted-space\">&nbsp;</span><code \n" +
         "  style=\"font-size: 13px; font-family: monospace; color: rgb(15, 57, 141);\">nil</code>.   \n" +
         "  A new, empty map value is made using the built-in function<span   \n" +
         "  class=\"Apple-converted-space\">&nbsp;</span><code \n" +
         "  style=\"font-size: 13px; font-family: monospace; color: rgb(15, 57, 141);\">make</code>,  \n" +
         "  which takes the map type and an optional capacity hint as arguments:</p>   \n" +
         "  <pre  \n" +
         "  style=\"margin: 1em 0px 0px; padding: 0.99em; overflow: auto; font-size: 9pt; background-color: rgb(248, 248, 255); line-height: 15px; word-wrap: break-word;\">make(map[string] int)  \n" +
         "  make(map[string] int, 100) \n" +
         "  </pre>   \n" +
         "  <p>The initial capacity does not bound its size: maps grow to  \n" +
         "  accommodate the number of items stored in them.</p>   \n" +
         "  </span></span> \n" +
         "  </body>  \n" +
         "  </html>  \n" 
),       
   
   STRUCT("struct", TokenClass.KEYWORD,  "A struct is a sequence of named elements, called fields, \n" +
         "each of which has a name and a type. Field names may be specified \n" +
         "explicitly (IdentifierList) or implicitly (AnonymousField). Within a \n" +
         "struct, non-blank field names must be unique. "),   
         
   INTERFACE("interface", TokenClass.KEYWORD,
         "Interfaces in Go provide a way to specify the behavior of an object: \n"+
         "if something can do this, then it can be used here. We've seen a couple \n"+
         "of simple examples already; custom printers can be implemented by a \n"+
         "String method while Fprintf can generate output to anything with a \n"+
         "Write method. Interfaces with only one or two methods are common in Go \n"+
         "code, and are usually given a name derived from the method, such as \n" +
         "io.Writer for something that implements Write.\n\n"+
         "A type can implement multiple interfaces. For instance, a collection \n"+
         "can be sorted by the routines in package sort if it implements \n"+
         "sort.Interface, which contains Len(), Less(i, j int) bool, and \n"+
         "Swap(i, j int), and it could also have a custom formatter. In this \n"+
         "contrived example Sequence satisfies both. An interface type specifies \n"+
         "a method set called its interface. A variable of interface type can \n"+
         "store a value of any type with a method set that is any superset of the\n"+
         "interface. Such a type is said to implement the interface. An interface\n"+
         "value may be nil. " +
         "\n\n// A simple File interface\n"+
         "interface {\n"+
         "  Read(b Buffer) bool \n"+
         "  Write(b Buffer) bool\n"+
         "  Close()             \n"+
         "}\n" +
         ""),
   
   // VALUES
   NIL("nil",TokenClass.VALUE,""),
   TRUE("true",TokenClass.VALUE,""),
   FALSE("false",TokenClass.VALUE,""),
   IOTA("iota",TokenClass.VALUE,""),
   
   // BUILTIN FUNCTION
   CAP("cap",TokenClass.BUILT_IN_FUNCTION,""), 
   CLOSE("close",TokenClass.BUILT_IN_FUNCTION,"") , 
   CLOSED("closed",TokenClass.BUILT_IN_FUNCTION,""), 
   LEN("len",TokenClass.BUILT_IN_FUNCTION,""), 
   MAKE("make",TokenClass.BUILT_IN_FUNCTION,""), 
   NEW("new",TokenClass.BUILT_IN_FUNCTION,""), 
   PANIC("panic",TokenClass.BUILT_IN_FUNCTION,""), 
   PANICLN("panicln",TokenClass.BUILT_IN_FUNCTION,""), 
   PRINT("print",TokenClass.BUILT_IN_FUNCTION,""), 
   PRINTLN("println",TokenClass.BUILT_IN_FUNCTION,""),
   
   // KEYWORDS
   BREAK("break",TokenClass.KEYWORD,""),
   DEFAULT("default",TokenClass.KEYWORD,""),      
   FUNC("func",TokenClass.KEYWORD,""),         
   
 
   SELECT("select",TokenClass.KEYWORD,""),       
   CASE("case",TokenClass.KEYWORD,""),         
   DEFER("defer",TokenClass.KEYWORD,""),        
   GO("go",TokenClass.KEYWORD,""),           
   
       
       
   ELSE("else",TokenClass.KEYWORD,""),         
   GOTO("goto",TokenClass.KEYWORD,""),         
   PACKAGE("package",TokenClass.KEYWORD,""),      
   SWITCH("switch",TokenClass.KEYWORD,""),       
   CONST("const",TokenClass.KEYWORD,""),        
   FALLTHROUGH("fallthrough",TokenClass.KEYWORD,""),  
   IF("if",TokenClass.KEYWORD,""),           
   RANGE("range",TokenClass.KEYWORD,""),        
   TYPE("type",TokenClass.KEYWORD,""),         
   CONTINUE("continue",TokenClass.KEYWORD,""),     
   FOR("for",TokenClass.KEYWORD,""),          
   IMPORT("import",TokenClass.KEYWORD,""),       
   RETURN("return",TokenClass.KEYWORD,""),       
   VAR("var",TokenClass.KEYWORD,"");
   
   String value, description="";
   TokenClass tokenClass;
   
   Keywords(String val){
      value = val;
   }
   
   Keywords(String val, TokenClass c, String desc){
      value = val;
      tokenClass = c;
      description = desc+"\n Creative Commons Attribution 3.0 via http://golang.org";
   }

   /**
    * @return the value
    */
   public String getValue() {
      return value;
   }

   /**
    * @param value the value to set
    */
   public void setValue(String value) {
      this.value = value;
   }

   /**
    * @return the description
    */
   public String getDescription() {
      return description;
   }

   /**
    * @param description the description to set
    */
   public void setDescription(String description) {
      this.description = description;
   }

}
