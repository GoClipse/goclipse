package melnorme.lang.tooling.ast;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.downCast;
import melnorme.utilbox.misc.NumberUtil;


public final class SourceRange implements Comparable<SourceRange> {
	
	private final int offset;
	private final int length;
	
	public SourceRange(int offset, int length) {
		assertTrue(offset >= 0);
		assertTrue(length >= 0);
		this.offset = offset;
		this.length = length;
	}
	
	public static SourceRange srStartToEnd(int startPos, int endPos) {
		assertTrue(startPos >= 0 && endPos >= startPos);
		return new SourceRange(startPos, endPos - startPos);
	}
	
	public final int getOffset() {
		return offset;
	}
	
	public final int getLength() {
		return length;
	}
	
	public final int getStartPos() {
		return getOffset();
	}
	
	public final int getEndPos() {
		return getOffset() + getLength();
	}
	
	@Override
	public final String toString() {
		return "[" + offset + "+" + length + "]";
	}
	
	@Override
	public final boolean equals(Object obj) {
		if(!(obj instanceof SourceRange))
			return false;
		
		SourceRange other = downCast(obj);
		return this.offset == other.offset && this.length == other.length;
	}
	
	@Override
	public int compareTo(SourceRange other) {
		if(offset == other.offset) {
			return length - other.length;
		}
		return offset - other.offset;
	}
	
	/* -----------------  ----------------- */
	
	public boolean contains(SourceRange other) {
		return contains(other.getStartPos()) && contains(other.getEndPos());
	}
	
	public boolean contains(int otherOffset) {
		return NumberUtil.isInRange(getStartPos(), otherOffset, getEndPos());
	}
	
	public boolean inclusiveContains(int otherOffset) {
		return contains(otherOffset);
	}
	
	public boolean inclusiveContains(SourceRange other) {
		return contains(other);
	}
	
	/* -----------------  ----------------- */
	
	/** @return a substring of given source using the range of the receiver. */
	public String getRangeSubString(String source) {
		return source.substring(getStartPos(), getEndPos());
	}
	
}