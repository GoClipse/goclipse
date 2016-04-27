/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
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
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.tests.CommonTest;

public class BuildTargetsSerializer_Test extends CommonTest {
	
	public static CommandInvocation cmd(String commandArguments) {
		return commandArguments == null ? null :new CommandInvocation(commandArguments);
	}
	
	public static BuildTargetData bt(String targetName, boolean enabled, boolean autoEnabled, 
			String buildArguments, String executablePath) {
		return btd(targetName, enabled, autoEnabled, cmd(buildArguments), executablePath);
	}
	
	public static BuildTargetData btd(String targetName, boolean enabled, boolean autoEnabled, 
			CommandInvocation buildCommand, String executablePath) {
		BuildTargetData bt = new BuildTargetData(
			targetName, 
			enabled, 
			autoEnabled,
			buildCommand, 
			executablePath
		);
		
		assertEquals(bt, bt);
		assertEquals(bt, new BuildTargetData().setData(bt));
		return bt;
	}
	
	protected final BuildManager buildMgr = LangCore.getBuildManager();
	protected BuildTargetsSerializer serializer = buildMgr.createSerializer();
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		testSerialize(new ArrayList2<>());
		testSerialize(new ArrayList2<>(bt("", false, false, null, null)));
		testSerialize(new ArrayList2<>(btd("", true, true, cmd("-opt"), "foo.exe")));
		testSerialize(new ArrayList2<>(
				btd("", false, true, cmd(""), ""),
				btd("blah", true, false, cmd("-opt"), "foo.exe"),
				bt("xxx", true, false, null, "foo/bar.ooo")
		));
	}
	
	protected void testSerialize(ArrayList2<BuildTargetData> buildTargetsData) {
		try {
			String xml = serializer.writeToString(buildTargetsData);
			assertAreEqual(buildTargetsData, serializer.readFromString(xml));
		} catch(CommonException e) {
			assertFail();
		}
	}
	
}