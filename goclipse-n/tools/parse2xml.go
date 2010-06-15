
package main

import (
    "flag"
    "go/parser"
    "fmt"
    "os"
    "go/ast"          
);

var filename = flag.String("f", "", "-f [filename]")
var nFlag = flag.Bool("n", false, `no \n`)

// testfunc does squat
func testfunc(i int) (int){
    return 4
}


// main application - translate go source file to xml 
func main() {
    flag.Parse()
    fmt.Printf("%v", *filename)    
    file, err := parser.ParseFile(*filename, nil, nil, 0)

    if file == nil {
        fmt.Printf("problem: err=%s\n", err.String())
        os.Exit(1)
    }

    //ast.FileExports(file);
    fmt.Printf("\n<gofile package=\"%v\" filename=\"%v\">", file.Name, *filename)
    
    if file.Doc != nil {
        fmt.Printf("\n\t<comment>")
        for _, value := range file.Doc.List{
            for _, b := range value.Text{
                fmt.Printf("%c", b)
            }
        }
    }

    for _, value := range file.Decls{
        
        genDecl, ok1 := value.(*ast.GenDecl)
        
        if ok1 {
            for _, spec:= range genDecl.Specs{
            
                // VARIABLES
                valuespec, ok3 := spec.(*ast.ValueSpec)
                if ok3 {
                    fmt.Printf("\n\t<variable line=\"%v\" column=\"%v\" name=\"%v\" type=\"%v\" exported=\"%v\"/>", 
                        valuespec.Pos().Line, valuespec.Pos().Column, valuespec.Names[0], valuespec.Type, ast.IsExported(fmt.Sprint(valuespec.Names[0])))    
                }
                
                // IMPORTS
                importspec, ok4 := spec.(*ast.ImportSpec)
                if ok4 {
                    fmt.Printf("\n\t<import path=")
                    //for _, element := range importspec.Path {
                        fmt.Printf("%s", importspec.Path.Value)
                    //}
                    fmt.Printf("/>")
                        
                }
            }   
        }
        
        funcDecl, ok2 := value.(*ast.FuncDecl)
        
        if ok2 {
            // BEGIN FUNCTION
            fmt.Printf("\n\t<func line=\"%v\" column=\"%v\" text=\"%v(", funcDecl.Name.Position.Line, funcDecl.Name.Position.Column, funcDecl.Name);
            firstIter := true;
             
            if funcDecl.Type.Params.List != nil {
                for _, field := range funcDecl.Type.Params.List{
                    
                    if field!=nil {
                    
                        if !firstIter {
                            fmt.Printf(", ")
                        }
                        
                        fmt.Printf("%v", field.Names[0])
                        fmt.Printf(" %v", field.Type)
                        firstIter = false
                    }
                }
            }
            fmt.Printf(")")
            
            // FUNCTION RESULT SET
            if funcDecl.Type.Results != nil && funcDecl.Type.Results.List != nil {
                firstIter = true
                hasResults := false
                for _, field:= range funcDecl.Type.Results.List{
                    
                    if !firstIter {
                        fmt.Printf(", ")
                    } else {
                        fmt.Printf("(")
                    }
                    
                    fmt.Printf("%v", field.Type)
                    firstIter = false
                    hasResults = true
                }
            
            
                // Only print if the function returned a value 
                if hasResults {
                    fmt.Printf(")")
                }
            
                fmt.Printf("\" exported=\"%v\">", ast.IsExported(fmt.Sprint(funcDecl.Name)))
                
                // BEGIN/END PARAMETER
                for _, field:= range funcDecl.Type.Params.List{
                    fmt.Printf("\n\t\t<param ")
                    fmt.Printf("name=\"%v\"", field.Names[0])
                    fmt.Printf(" type=\"%v\"", field.Type)
                    fmt.Printf("/>")
                }
    
                // BEGIN/END RESULT
                for _, field:= range funcDecl.Type.Results.List{
                    fmt.Printf("\n\t\t<result ")
                    fmt.Printf("type=\"%v\"", field.Type)
                    fmt.Printf("/>")
                }
            } else {
                fmt.Printf("\" exported=\"%v\">", ast.IsExported(fmt.Sprint(funcDecl.Name)))
            }
            
            if funcDecl.Doc != nil && funcDecl.Doc.List != nil {
                fmt.Printf("\n\t\t<comment>")
                for _, value := range funcDecl.Doc.List{
                    for _, b := range value.Text{
                        fmt.Printf("%c", b)
                    }
                }
                fmt.Printf("</comment>\n")
            }

            // END FUNCTION
            fmt.Printf("\n\t</func>")
            //fmt.Printf("node = %v\n", funcDecl.Recv);
        }
        
    }
    fmt.Printf("\n</gofile>")
    fmt.Printf("\n\n")
}

