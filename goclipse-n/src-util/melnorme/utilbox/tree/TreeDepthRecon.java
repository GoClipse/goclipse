/*******************************************************************************
 * Copyright (c) 2007 DSource.org and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.utilbox.tree;


public class TreeDepthRecon extends TreeWalker {
	
	public int depth = 0;
	public int maxdepth = 0;
	public int mindepth = Integer.MAX_VALUE; //conceptually infinite;
	private boolean findmindepth = false;
	private boolean breakOnChildren = false;
	
	
	public static int findMaxDepth(IElement elem) {
		TreeDepthRecon tdr = new TreeDepthRecon();
		tdr.traverse(elem);
		return tdr.maxdepth;
	}

	public static int findMinLeafDepth(IElement elem) {
		TreeDepthRecon tdr = new TreeDepthRecon();
		tdr.traverse(elem);
		return tdr.maxdepth;
	}
	
	public static int findMaxDistance(IElement elem) {
		return findMaxDepth(elem)-1;
	}
	
	public static int findMinLeafDistance(IElement elem) {
		return findMinLeafDepth(elem)-1;
	}

	public static boolean isLeaf(IElement elem) {
		TreeDepthRecon tdr = new TreeDepthRecon();
		tdr.breakOnChildren = true;
		tdr.traverse(elem);
		return tdr.maxdepth == 1;
	}	
	
	
	@Override
	public boolean enterNode(IElement element) {
		depth++;

		if(breakOnChildren && depth == 2) {
			return false; // We're visiting a children, break
		}

		if (depth > maxdepth)
			maxdepth = depth;
		
		if (findmindepth && depth < mindepth && isLeaf(element))
			mindepth = depth;

		return true;
		
	}
	
	@Override
	public void leaveNode(IElement element) {
		depth--;
	}

}

