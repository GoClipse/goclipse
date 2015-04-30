/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.ui.editor.structure;

import melnorme.lang.ide.ui.editor.structure.StructureModelManager;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.structure.IStructureElement;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.lang.tooling.structure.StructureElementData;
import melnorme.lang.tooling.structure.StructureElementKind;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.misc.Location;

public class LANGUAGE_StructureModelManager extends StructureModelManager {
	
	public LANGUAGE_StructureModelManager() {
	}
	
	@Override
	protected StructureUpdateTask createStructureUpdateTask(Location location, String source) {
		return new StructureUpdateTask(location, source) {
			@Override
			protected SourceFileStructure createSourceFileStructure() {
				SourceRange sr = new SourceRange(0, source.length());
				StructureElement element = new StructureElement("NOT_IMPLEMENTED", sr, sr, 
					StructureElementKind.MODULEDEC, new StructureElementData(), null, null);
				return new SourceFileStructure(location, new ArrayList2<IStructureElement>(element));
			}
		};
	}
	
}