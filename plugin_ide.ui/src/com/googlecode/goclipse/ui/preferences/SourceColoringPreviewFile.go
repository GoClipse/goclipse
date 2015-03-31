package main
import "fmt"

// A comment for this function
func example() int {
	var x = true
	var str = "Hello"
	var mstr = `line 1
	line 2`
	a, b := 0, 1
	fmt.Println(str, mstr, a+b, x)
	return a + b;
}