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
package melnorme.lang.tooling.structure;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;
import static melnorme.utilbox.core.CoreUtil.nullToEmpty;
import melnorme.lang.tooling.ast.ParserError;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.misc.Location;

public class SourceFileStructure_Default extends StructureContainer implements ISourceFileStructure {
	
	protected final Location location;
	protected final Indexable<ParserError> parserProblems;
	
	public SourceFileStructure_Default(Location location, Indexable<StructureElement> children, 
			Indexable<ParserError> parserProblems) {
		super(children);
		this.location = assertNotNull(location);
		this.parserProblems = nullToEmpty(parserProblems);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof SourceFileStructure)) return false;
		
		SourceFileStructure other = (SourceFileStructure) obj;
		
		return 
			areEqual(location, other.location) &&
			areEqual(children, other.children);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(location, children.size());
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + location;
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public Location getLocation() {
		return location;
	}
	
	@Override
	public String getModuleName() {
		return location.getPath().getFileName().toString();
	}
	
	public StructureElement getStructureElementAt(int offset) {
		return new StructureElementFinderByOffset(offset).findInnerMost(this);
	}
	
	public Indexable<ParserError> getParserProblems() {
		return parserProblems;
	}
	
	/* ----------------- Utils ----------------- */
	
	public static class StructureElementFinderByOffset {
		
		protected final int offset;
		
		protected StructureElement pickedElement;
		
		public StructureElementFinderByOffset(int offset) {
			this.offset = offset;
		}
		
		public StructureElement findInnerMost(IStructureElementContainer container) {
			visitContainer(container);
			return pickedElement;
		}
		
		protected void visitContainer(IStructureElementContainer container) {
			assertNotNull(container);
			
			for(StructureElement structureElement : container.getChildren()) {
				if(structureElement.getSourceRange().inclusiveContains(offset)) {
					pickedElement = structureElement;
					
					visitContainer(structureElement); // Check children
					return;
				}
			}
			
		}
		
	}
	
}