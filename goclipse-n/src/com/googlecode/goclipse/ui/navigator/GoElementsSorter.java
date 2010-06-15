package com.googlecode.goclipse.ui.navigator;

import java.text.Collator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
public class GoElementsSorter extends ViewerSorter {

	public GoElementsSorter() {

	}

	public GoElementsSorter(Collator collator) {
		super(collator);

	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		GoPackage pkg1 = null;
		GoPackage pkg2 = null;

		if (e1 instanceof GoPackage) {
			pkg1 = (GoPackage) e1;
		}
		if (e2 instanceof GoPackage) {
			pkg2 = (GoPackage) e2;
		}

		if (pkg1 != null && pkg2 != null) {
			return pkg1.getName().compareTo(pkg2.getName());
		} else if (pkg1 != null && pkg2 == null) {
			return -1;
		} else if (pkg1 == null && pkg2 != null) {
			return 1;
		} else {
			return super.compare(viewer, e1, e2);
		}
	}

}
