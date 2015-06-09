LangEclipseIDE
================

LangEclipseIDE is a framework for building Eclipse based IDEs, leveraging common infrastructure and code from projects 
like [JDT](https://eclipse.org/jdt/) or [DLTK](https://eclipse.org/dltk/), as well code written completely from scratch. 
It aims to provide useful IDE functionality for new language IDEs, beyond what the Eclipse Platform provides.
It provides mostly UI infrastructure at the moment. Support for building semantic engines/functionality (for example like 
[Xtext](https://www.eclipse.org/Xtext/) does), is not present, although it could be gradually added in the future.

#### Projects using LangEclipseIDE

 * DDT - http://ddt-ide.github.io/
 * RustDT - http://rustdt.github.io/
 * Goclipse - http://goclipse.github.io/

### History/Background

LangEclipseIDE grew out of the [DDT](http://ddt-ide.github.io/) project, and a desire to refactor non language-specific 
code to some other project so that it could be reused by other IDEs. DDT has been using the DLTK framework for a long time 
(which aims to do about the same as LangEclipseIDE: leverage a JDT-style infrastructure for use by other IDEs), but development 
on DLTK halted, whilst being left with many API and functionality limitations. And from here grew the motivation to write 
a new framework, with cleaner code. 

Some components where rewritten from scratch, others were rewritten from JDT/DLTK code, 
others still are copied from those IDE with little to no modifications, whichever case is deemed better.

### Design notes:
LangEclipseIDE is designed to be used by embedding the LangEclipseIDE source code directly in the host IDE code. 
As opposed to DLTK or Xtext for example, where a runtime dependency on the framework plugins is required. 
As such, updating to a new LangEclipseIDE version is made by means of Git source control workflows. The motivation 
for this is so provide complete API control to host IDEs - if some change is desired that LangEclipseIDE is not able 
to be customized without changing LangEclipseIDE code, this won't be much of a problem, the local LangEclipseIDE source 
can be changed for theat host IDE. This also means different LangEclipseIDE-based IDEs can be installed on the same 
Eclipse installation, even if they are based on different (and otherwise incompatible) LangEclipseIDE versions or variations.
(The same would not be possible with DLTK for example)
 
#### Getting started writting a LangEclipseIDE-based UIDE:

See LangIDE_templatedNames.txt from strings one needs to replace to create a newIDE.
TODO: add more detail / info
