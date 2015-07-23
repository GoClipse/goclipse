/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.operations.build;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import org.junit.Test;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTargetsSerializer;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.tests.CommonTest;

public class BuildTargetsSerializer_Test extends CommonTest {
	
	protected BuildTargetsSerializer serializer = LangCore.getBuildManager().createSerializer();
	
	@Test
	public void testname() throws Exception { testname$(); }
	public void testname$() throws Exception {
		testSerialize(new ArrayList2<>());
		testSerialize(new ArrayList2<>(createBuildTarget(true, "", "-opt")));
		testSerialize(new ArrayList2<>(createBuildTarget(false, "", null)));
		testSerialize(new ArrayList2<>(
				createBuildTarget(false, "", ""),
				createBuildTarget(true, "blah", "-opt"),
				createBuildTarget(true, "xxx", null)
		));
	}
	
	protected BuildTarget createBuildTarget(boolean enabled, String targetName, String buildOptions) {
		return LangCore.getBuildManager().createBuildTarget(targetName, enabled, buildOptions);
	}
	
	protected void testSerialize(ArrayList2<BuildTarget> buildTargets) {
		try {
			String xml = serializer.writeProjectBuildInfo(buildTargets);
			assertAreEqual(buildTargets, serializer.readProjectBuildInfo(xml));
		} catch(CommonException e) {
			assertFail();
		}
	}
	
}