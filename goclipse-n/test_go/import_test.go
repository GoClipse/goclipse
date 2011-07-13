package test

import "other"

import ( 
  fmt "os";
  . "flag"
  "boss/dave"
  _ "dook" 
  // command line option parser 
)

import d "K"

// this is a variable comment
var variable string

// this does something
type bb int

var a1 int

type A struct{
	
}

func (a *A) method(){
	return x
}

// This is a test comment
func foo()int{
	return 1
}

// Sleep pauses the current goroutine for at least ns nanoseconds.
// Higher resolution sleeping may be provided by syscall.Nanosleep 
// on some operating systems.
func Sleep(ns int64) os.Error {
	_, err := sleep(Nanoseconds(), ns)
	return err
}
