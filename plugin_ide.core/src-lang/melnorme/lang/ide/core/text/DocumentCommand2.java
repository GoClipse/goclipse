/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.text;

//Clone org.eclipse.jface.text.DocumentCommand because it's in a UI plugin

/**
 * Represents a text modification as a document replace command. The text
 * modification is given as a {@link org.eclipse.swt.events.VerifyEvent} and
 * translated into a document replace command relative to a given offset. A
 * document command can also be used to initialize a given
 * <code>VerifyEvent</code>.
 * <p>
 * A document command can also represent a list of related changes.</p>
 */
public class DocumentCommand2 {

	/** Must the command be updated */
	public boolean doit= false;
	/** The offset of the command. */
	
	public int offset;
	/** The length of the command */
	public int length;
	/** The text to be inserted */
	public String text;
//	/**
//	 * The owner of the document command which will not be notified.
//	 * @since 2.1
//	 */
//	public IDocumentListener owner;
	/**
	 * The caret offset with respect to the document before the document command is executed.
	 * @since 2.1
	 */
	public int caretOffset;
//	/**
//	 * Additional document commands.
//	 * @since 2.1
//	 */
//	private final List<Command> fCommands= new ArrayList<>();
	/**
	 * Indicates whether the caret should be shifted by this command.
	 * @since 3.0
	 */
	public boolean shiftsCaret;

	
	

	public DocumentCommand2(boolean doit, int offset, int length, String text, int caretOffset, boolean shiftsCaret) {
		super();
		this.doit = doit;
		this.offset = offset;
		this.length = length;
		this.text = text;
		this.caretOffset = caretOffset;
		this.shiftsCaret = shiftsCaret;
	}
	
	public DocumentCommand2() {
		super();
	}

}