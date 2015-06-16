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

 
### Writing a LangEclipseIDE-based IDE:

To get started creating a new LangEclipseIDE-based IDE, fork the repo, and then do a search-replace on the following strings, replace for the appropriate text for your project:

| String 	| Description | Example |
|---------	|--------------	| -----	|
|LANGUAGE  | Name of language - for UI display. | <sub><sup>`D`</sup></sub> |
|LANGUAGE_ |  Name of language - for Java class names| <sub><sup>`Dee`</sup></sub> |
|LANG_PROJECT_ID | Common identifier prefix for all IDE plugins. | <sub><sup>`org.dsource.ddt`</sup></sub> |
|LANG_IDE_NAME   | Name of IDE, long form. | <sub><sup>`D Development Tools`</sup></sub> |
|LANG_IDE_SITE   |  URL of project website | <sub><sup>`http://ddt-ide.github.io/`</sup></sub> |
|LANG_IDE_UPDATE_SITE| URL of project's Eclipse Update Site | <sub><sup>`http://ddt-ide.github.io/releases`</sup></sub> |
|LANG_IDE_WEBSITE_GIT_REPO| URL of the Git repository of the Github-Pages-based project website | <sub><sup>`https://github.com/DDT-IDE/ddt-ide.github.io.git`</sup></sub>  |


Note: some strings like `LANGUAGE_` or `LANG_PROJECT_ID`  will replace Java class identifiers. After this replace you will need to rename the compilation unit, and/or move them to a different folder. This can be done quickly in Eclipse with quick-fixes.

##### Language specific code.
To implement language specific IDE functionality, several language specific implementations need to be created or customized.

* TODO: document this more

##### LangEclipseIDE source embedding
The LangEclipseIDE source is embedded directly into the host IDE. To make it easier to manage source updates to and from LangEclipseIDE, the following rules need to be observed. 

 * The vast majority of `melnorme.lang` code, or simply Lang code, is code not specific to any language, and should only depend on other `melnorme` code, or on Eclipse.org platform plugins. But not on IDE specific code.   
   * Such is placed on a separate source folder: `src-lang/`.
   * The source of all code placed in `src-lang/` should be the exact same for all LangEclipseIDE based IDEs (even if the binary or runtime structure/API may differ).
 * Then, the rest of `melnorme.lang` code will have IDE specific code. Such classes must be annotated with the `melnorme.lang.tooling.LANG_SPECIFIC` annotation, or have an `_Actual` suffix in the name (this is deprecated). This code will contain bindings to IDE-specific code (such as ids, other IDE constants, or even IDE-specific methods).
  * This language specific `melnorme.lang` code must be place not on `src-lang/`, but on the same source folder as the rest of the language specific code (`src` by default).
