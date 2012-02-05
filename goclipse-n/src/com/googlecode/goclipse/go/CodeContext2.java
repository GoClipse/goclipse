package com.googlecode.goclipse.go;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.googlecode.goclipse.go.lang.model.FileScope;
import com.googlecode.goclipse.go.lang.model.Import;
import com.googlecode.goclipse.go.lang.model.Node;
import com.googlecode.goclipse.go.lang.model.Var;

@SuppressWarnings("unused")
public class CodeContext2 {

	private FileScope fileScope;
	
	/**
	 * Builds a scope tree
	 * 
	 * @author steel
	 * 
	 */
	class InternalDefaultHandler extends DefaultHandler {
		
    private static final String ASSIGNMENT_POSITION = "assignment_position";
		private static final String COLUMN              = "column";
		private static final String COMMENT             = "comment";
		private static final String ENDCOLUMN           = "endcolumn";
		private static final String ENDLINE             = "endline";
		private static final String ENDOFFSET           = "endoffset";
		private static final String FIELD               = "field";
		private static final String FILE                = "file";
		private static final String FUNC                = "func";
		private static final String INTERFACE           = "interface";
		private static final String IMPORT              = "import";
		private static final String LINE                = "line";
		private static final String METHOD 				= "method";
		private static final String NAME                = "name";
		private static final String OFFSET              = "offset";
		private static final String PARAMETERS          = "parameters";
		private static final String PATH                = "path";
		private static final String RESULTS             = "results";
		private static final String SCOPE               = "scope";
		private static final String STRUCT              = "struct";
		private static final String TAG                 = "tag";
		private static final String TEXT                = "text";
		private static final String TYPE                = "type";
		private static final String VAR                 = "var";
		
		private Node activeNode;
		
		/**
		 */
		@Override
    public void endElement(String uri, String localName, String qName)
			throws SAXException {

			if (qName.equalsIgnoreCase(FILE)) {
				
			} else if (qName.equalsIgnoreCase(IMPORT)) {
				
			} else if (qName.equalsIgnoreCase("var")) {
				
			} else if (qName.equalsIgnoreCase("type")) {
				
			}
		}

		/**
		 * 
		 */
		@Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
			try {
				if (FILE.equals(qName)) {
					System.out.println(FILE);
					fileScope = new FileScope();
					fileScope.setStart(Integer.parseInt(attributes.getValue(LINE)));
					fileScope.setEnd(Integer.parseInt(attributes.getValue(ENDLINE)));
					activeNode = fileScope;
				} else if (IMPORT.equals(qName)) {
					System.out.println(IMPORT);
					Import imp = new Import();
					imp.setLine(Integer.parseInt(attributes.getValue(LINE)));
					imp.setInsertionText(attributes.getValue(NAME));
					imp.setName(attributes.getValue(PATH));
					fileScope.addImport(imp);
				} else if (VAR.equals(qName)) {
					Var var = new Var();
					var.setInsertionText(attributes.getValue(NAME));
					var.setLine(Integer.parseInt(attributes.getValue(LINE)));
					//var.setType(attributes.getValue(TYPE));
				} else if (TYPE.equals(qName)) {
					System.out.println("type");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static CodeContext2 buildCodeContext(String xml) {
		CodeContext2 context2 = new CodeContext2();
		try {
			SAXParserFactory     factory = SAXParserFactory.newInstance();
			SAXParser            parser  = factory.newSAXParser();
			ByteArrayInputStream bs      = new ByteArrayInputStream(xml.getBytes());
			parser.parse(bs, context2.new InternalDefaultHandler());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return context2;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader reader = null;
		try {
			try {
				StringBuilder builder = new StringBuilder();
				reader = new BufferedReader(new FileReader("test/input/indexerinput.xml"));
				String line;
				while( (line = reader.readLine()) != null ) {
					builder.append(line);
				}
				buildCodeContext(builder.toString());
			} finally {
				reader.close();
			}
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	private CodeContext2() {
	}
}
