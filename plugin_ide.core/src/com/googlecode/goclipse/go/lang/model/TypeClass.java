package com.googlecode.goclipse.go.lang.model;

import java.io.Serializable;

public enum TypeClass implements Serializable {
	
	UINT, UINT8, UINT16, UINT32, UINT64, RUNE,
	INT, INT8, INT16, INT32, INT64,
	FLOAT, FLOAT8, FLOAT16, FLOAT32, FLOAT64,
	COMPLEX64, COMPLEX128,
	BYTE, UINTPTR, STRING, BOOL, CHAN,
	MAP, ARRAY, USER, UNKNOWN, STRUCT,
	INTERFACE

}
