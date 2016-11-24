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
package LANG_PROJECT_ID.ide.core.engine;

import melnorme.lang.ide.core.engine.SourceModelManager;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.lang.tooling.structure.StructureElementKind;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class LANGUAGE_SourceModelManager extends SourceModelManager {
	
	public LANGUAGE_SourceModelManager() {
	}
	
	@Override
	protected StructureUpdateTask createUpdateTask(StructureInfo structureInfo, String source) {
		return new StructureUpdateTask(structureInfo) {
			@Override
			protected SourceFileStructure doCreateNewData() throws CommonException {
				Location location = structureInfo.getLocation();
				SourceRange sr = new SourceRange(0, source.length());
				StructureElement element = new StructureElement("NOT_IMPLEMENTED", sr, sr, 
					StructureElementKind.MODULEDEC, new ElementAttributes(null), null, null);
				return new SourceFileStructure(location, ArrayList2.create(element), null);
			}
		};
	}
	
}