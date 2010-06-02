package com.googlecode.goclipse.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class GoSourceNumberRule implements IRule {

	private Token number;
	private Token octal;
	private Token hex;
	private Token unreal;

	public GoSourceNumberRule()
	{
		number = new Token(new TextAttribute(IGoSourceColorConstants.NUMBER));
		octal = new Token(new TextAttribute(IGoSourceColorConstants.NUMBER_OCT));
		hex = new Token(new TextAttribute(IGoSourceColorConstants.NUMBER_HEX));
		unreal = new Token(new TextAttribute(IGoSourceColorConstants.NUMBER_UNR));
	}

	public boolean isDigit(int ch) {
		return '0' <= ch && ch <= '9';
	}

	public boolean isHexDigit(int ch) {
		return isDigit(ch) || ('a' <= ch && ch <= 'f') || ('A' <= ch && ch <= 'F');
	}

	public boolean isOctalDigit(int ch) {
		return '0' <= ch && ch <= '7';
	}

	public IToken evaluate(ICharacterScanner scanner)
	{
		//		int_lit     = decimal_lit | octal_lit | hex_lit .
		//		decimal_lit = ( "1" ... "9" ) { decimal_digit } .
		//		octal_lit   = "0" { octal_digit } .
		//		hex_lit     = "0" ( "x" | "X" ) hex_digit { hex_digit } .

		boolean validNumber = false;

		boolean hexNumber = false;
		boolean octNumber = false;

		boolean hasDecimal = false;

		boolean hasExponent = false;
		boolean isUnreal = false;

		int ch = '\0', cch = '\0';
		for (int i = 0; (ch = scanner.read()) != ICharacterScanner.EOF; i++)
		{
			if (isUnreal)
			{
				// No characters are valid after the unreal marker
				break;
			}
			else if (i == 0 && ch == '0')
			{
				octNumber = true;
				validNumber = true;
			}
			else if (i == 1 && ch == 'x' && octNumber)
			{
				octNumber = false;
				hexNumber = true;
			}
			else if (ch == '.')
			{
				if (hasDecimal == true) break;
				hasDecimal = true;
				octNumber = false;
			}
			else if (i > 0 && ch == 'i')
			{
				isUnreal = true;
			}
			else if (hexNumber)
			{
				// Only hex digits are allowed in a hex number
				if (!isHexDigit(ch)) break;
				validNumber = true;
			}
			else if (octNumber) 
			{
				// Only octal digits are allowed in an octal number
				if (!isOctalDigit(ch)) break;
				validNumber = true;
			}
			else if (i > 0 && (ch == 'e' || ch == 'E'))
			{
				if (hasExponent == true) break;
				hasExponent = true;
			}
			else if (hasExponent && (ch == '+' || ch == '-') && (cch == 'e' || cch == 'E'))
			{
				// Do nothing... this is perfectly valid
			}
			else if (!isDigit(ch)) 
			{
				// No non-digit characters are valid here
				break;
			}
			else
			{
				validNumber = true;
			}

			cch = ch;
		}
		if (ch != ICharacterScanner.EOF)
			scanner.unread();

		if (!validNumber) return Token.UNDEFINED;
		else if (hexNumber) return hex;
		else if (octNumber) return octal;
		else if (isUnreal) return unreal;

		return number;
	}

}
