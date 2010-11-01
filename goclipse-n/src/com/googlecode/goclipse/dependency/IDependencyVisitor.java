package com.googlecode.goclipse.dependency;

public interface IDependencyVisitor {


	void visit(String aTarget, String ... dependencies);

}
