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
		IGoPackage pkg1 = null;
		IGoPackage pkg2 = null;

		if (e1 instanceof IGoPackage) {
			pkg1 = (IGoPackage) e1;
		}
		if (e2 instanceof IGoPackage) {
			pkg2 = (IGoPackage) e2;
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
