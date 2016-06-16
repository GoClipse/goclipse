/* 
 Some sample code to manually test debugger functionality,
 particularly, variable inspection.
*/

package main

import (
    "fmt"
    "time"
)


type Vertex struct {
    X int
    Y int
}

const World = "世界"

func debug_gauntlet(x int, str string) (ret1, ret2 int, ret3 *Vertex) {

	var localA, localB int;
	var aBool bool = true;
	var aRune rune = 'a'
	var aFloat float32 = 3.14;
	
	var aStruct Vertex = Vertex{10, 20};
	var aStructPointer *Vertex = &aStruct
	
	xStruct := Vertex{10, 20};
	xStructPointer := &aStruct
	
	var aString string = "Blah";
	xString := "Blah2";
	var aArray [2]int
	aArray[0] = 4
	aArray[1] = 2
	var	aSlice []int = []int{2, 3, 5, 7, 11, 13}
	var	aSlice2 []int // null
	
	var aMap = make(map[int]bool)
	
	var x1 = time.Now();
	
	fmt.Println(localA, localB, aBool, aRune, aFloat, aString, xString);
	fmt.Println(aStruct, aStructPointer, xStruct, xStructPointer)
	fmt.Println(aArray, aSlice, aSlice2)
	fmt.Println(aMap, aSlice, aSlice2)
	fmt.Println(x1);
	
	ret3 = aStructPointer
	return
}

func main() {
	fmt.Println("Hello debugger")
    
    debug_gauntlet(1, "hello string");
    
    fmt.Println("-----")
}
