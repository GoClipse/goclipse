LangEclipseIDE
================

LangEclipseIDE is a framework for building Eclipse based IDEs, reusing common infrastructure and code from projects like [JDT](https://eclipse.org/jdt/), [DLTK](https://eclipse.org/dltk/), or [CDT](https://eclipse.org/cdt/), as well code written completely from scratch. 
It aims to provide useful IDE functionality for new language IDEs, beyond what the Eclipse Platform provides.
It provides mostly UI infrastructure at the moment. There isn't support for building semantic functionality (like what [Xtext](https://www.eclipse.org/Xtext/) does, for example), although it could be gradually added in the future.

#### Projects using LangEclipseIDE

 * DDT - http://ddt-ide.github.io/
 * RustDT - http://rustdt.github.io/
 * Goclipse - http://goclipse.github.io/

### History/Background

LangEclipseIDE originated from the [DDT](http://ddt-ide.github.io/) project, and a desire to refactor non language-specific code to a separate project, so that it could be reused by other IDEs. DDT had been using the [DLTK](https://eclipse.org/dltk/) framework for a long time, and DLTK has a similar goal as LangEclipseIDE: leverage a JDT-style infrastructure for use by other IDEs (and not necessarily just dynamic languages). But development on DLTK halted, whilst being left with many API and functionality limitations. 

To continue developing a project to provide common IDE infrastructure, it would be necessary to either fork DLTK, or build a new project from scractch. For several reasons the later option was chosen. As such, some LangEclipseIDE components where rewritten from scratch, others were rewritten from JDT/DLTK code, others still are copied from those IDE with little to no modifications, whichever case is deemed better. 


### Design notes:

##### No IModelElement/IJavaElement model hierarchy
This is a key design difference between LangEclipseIDE and DLTK/JDT/CDT: avoiding the use of the IModelElement/IJavaElement model hierarchy (IModelElement is DLTK's analogue of IJavaElement), as this model is seen as having several shortcomings or unnecessary complexities. For example, the IModelElement/IJavaElement model classes don't adapt well to languages with structures significantly different than Java. Another issue is that it is felt that combining source elements (functions, types, etc.) and external elements (like source folders and package declarations) into the same hierarchy makes the design more clumsy that it should be, as these concerns are fairly separate.

##### Support for using external semantic tools.
In the more common case, LangEclipseIDE based IDEs will use external tools for functionality like building, but also code completion, find definition, etc. (for example, autocomplete deamons). But having that semantic functionality built in the host IDE itself is also well supported.

##### LangEclipseIDE source is embedded in host IDE.
LangEclipseIDE is designed to be used by embedding the its source code directly in the host IDE code. 
As opposed to DLTK or Xtext for example, where a runtime dependency on the framework plugins is required. 
As such, updating to a new LangEclipseIDE version is made by means of Git source control workflows. The motivation 
for this is so provide complete API control to host IDEs - if some change is desired that LangEclipseIDE is not able 
to be customized without changing LangEclipseIDE code, this won't be much of a problem, the local LangEclipseIDE source can be changed for theat host IDE. This also means different LangEclipseIDE-based IDEs can be installed on the same Eclipse installation, even if they are based on different (and otherwise incompatible) LangEclipseIDE versions or variations.
(The same would not be possible with DLTK for example)

### Functionality:

LangEclipseIDE is not currently as big or complete as JDT/DLTK in terms of provided functionality, but it covers the basics:

* New project wizard.

* Editor support:
 * Editor Outline and Quick-Outline (with filtering) support. Based on structure model
 * Editor source reconciliation and parse errors reporting.
 * An auto-indentation strategy for curly braces language.
 * Common editor actions: Indent/Deindent Source; Toggle Comment; Go To Matching Bracket.
 * Many other minor editor improvements and boilerplate.
 * Boilerplate code for Content Assist, Find-Definition, editor hyperlinks functionality.

* Content Assist "Code Snippets" functionality.

* Boilerplate project build support, parsing build tool output, creating error markers, etc..
 * Helper code for calling other external tools, their output displayed in Eclipse consoles.

* Boilerplate launch and debug support.
 * Debug support using CDT GDB integration.

* Preferences and preference pages: 
 * Source Coloring (aka syntax highlighting).
 * Typing/Auto-Indentation.
 * Content Assist.
 * Code snippets.
 * Build and other language tools.

* IDE build script (Maven/Tycho based).
 * Eclipse Update Site upload script. (uploads to a Git repo)
 * Skeleton project website, designed to be used by Github pages.

 
### Getting started writting a LangEclipseIDE-based IDE:

See LangIDE_templatedNames.txt from strings one needs to replace to create a newIDE.
TODO: add more detail / info
