MelnormeEclipse
================

MelnormeEclipse is a framework for building Eclipse based IDEs for general purpose languages. It builds upon the Eclipse Platform to provide even more generic components and code infrastructure, that can then be customized or extended by each concrete IDE, making it easier to develop a full-fledged IDE. Some specific aspects of MelnormeEclipse:

* There is a focus on an IDE paradigm of using external tools for building, code completion, and any sort of language analysis. Most of MelnormeEclipse infrastructure is UI infrastructure, the rest of IDE engine functionality is usually driven by language-specific external tools.

* MelnormeEclipse is not linked to as a library, instead the whole source of MelnormeEclipse is embedded in the concrete IDE. This means each concrete IDE has its own version of MelnormeEclipse, which can be updated or modified separately. 

### History/Background

MelnormeEclipse originated from the [DDT](http://ddt-ide.github.io/) project, and a desire to refactor non language-specific code to a separate project, so that it could be reused by other IDEs. DDT had been using the [DLTK](https://eclipse.org/dltk/) framework for a long time, and DLTK has a similar goal as MelnormeEclipse: leverage a JDT-style infrastructure for use by other IDEs (and not necessarily just dynamic languages). But development on DLTK halted, whilst being left with many API and functionality limitations. 

To continue developing a project to provide common IDE infrastructure, it would be necessary to either fork DLTK, or build a new project from scractch. For several reasons the later option was chosen. As such, some MelnormeEclipse components where rewritten from scratch, others were rewritten from existing JDT/DLTK code, others still were copied from those IDEs with little to no modifications - whichever case was deemed better. 


### Design notes:

##### No IModelElement/IJavaElement model hierarchy
This is a key design difference between MelnormeEclipse and DLTK/JDT/CDT: avoiding the use of the IModelElement/IJavaElement model hierarchy (IModelElement is DLTK's analogue of IJavaElement), as this model is seen as having several shortcomings or unnecessary complexities. For example, the IModelElement/IJavaElement model classes don't adapt well to languages with structures significantly different than Java. Another issue is that it is felt that combining source elements (functions, types, etc.) and external elements (like source folders and package declarations) into the same hierarchy makes the design more clumsy that it should be, as these concerns are fairly separate.

##### Support for using external semantic tools.
In the more common case, MelnormeEclipse based IDEs will use external tools for functionality like building, but also code completion, find definition, etc. (for example, autocomplete deamons). But having that semantic functionality built in the host IDE itself is also well supported.

##### MelnormeEclipse source is embedded in host IDE.
MelnormeEclipse is designed to be used by embedding the its source code directly in the host IDE code. 
As opposed to DLTK or Xtext for example, where a runtime dependency on the framework plugins is required. 
As such, updating to a new MelnormeEclipse version is made by means of Git source control workflows. The motivation 
for this is so provide complete API control to host IDEs - if some change is desired that MelnormeEclipse is not able 
to be customized without changing MelnormeEclipse code, this won't be much of a problem, the local MelnormeEclipse source can be changed for theat host IDE. This also means different MelnormeEclipse-based IDEs can be installed on the same Eclipse installation, even if they are based on different (and otherwise incompatible) MelnormeEclipse versions or variations.
(The same would not be possible with DLTK for example)

### Functionality:

MelnormeEclipse is not currently as big or complete as JDT/DLTK in terms of provided functionality, but it covers the basics:

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

 
### Writing a MelnormeEclipse-based IDE:

To get started creating a new MelnormeEclipse-based IDE, fork this repository.

##### Understand MelnormeEclipse source embedding
The MelnormeEclipse source is embedded directly into the host IDE. To make it easier to manage source updates to and from MelnormeEclipse, the following rules need to be observed. 

 * The vast majority of `melnorme.lang` code, or simply Lang code, is code not specific to any language, and should only depend on other `melnorme` code, or on Eclipse.org platform plugins. But not on IDE specific code.   
   * Such is placed on a separate source folder: `src-lang/`.
   * The source of all code placed in `src-lang/` should be the exact same for all MelnormeEclipse based IDEs (even if the binary or runtime structure/API may differ).
 * Then, the rest of `melnorme.lang` code will have IDE specific code. Such classes should either be annotated with the `melnorme.lang.tooling.LANG_SPECIFIC` annotation, or have an `_Actual` suffix in the name. This code will contain bindings to IDE-specific code (such as ids, other IDE constants, or even IDE-specific methods).
  * This language specific `melnorme.lang` code must not be place on `src-lang/`, but on the same source folder as the rest of the language specific code (`src` by default).

##### Modifying the starting template
Follow these steps:

 * [ ] Do a search-replace on the following strings, replace for the appropriate text for your IDE project:

| String 	| Description | Example |
|---------	|--------------	| -----	|
|LANGUAGE_ |  Name of language - for Java class names.| <sub><sup>`Dee`</sup></sub> |
|LANG_NAME  | Name of language - for UI display. | <sub><sup>`D`</sup></sub> |
|LANG_PROJECT_ID | Common prefix for all plugin identifiers. | <sub><sup>`org.dsource.ddt`</sup></sub> |
|LANG_IDE_NAME   | Name of the IDE. | <sub><sup>`DDT`</sup></sub> |
|LANG_IDE_SITE   |  URL of project website. | <sub><sup>`http://ddt-ide.github.io/`</sup></sub> |
|LANG_IDE_UPDATE_SITE| URL of project's Eclipse Update Site. | <sub><sup>`http://ddt-ide.github.io/releases`</sup></sub> |
|LANG_IDE_WEBSITE_GIT_REPO| URL of the Git project of the Github-Pages-based project website. | <sub><sup>`https://github.com/DDT-IDE/ddt-ide.github.io`</sup></sub>  |
|LANG_OTHER      | Other changes specific to each location. | N/A |

 Note: some strings like `LANGUAGE_` or `LANG_PROJECT_ID`  will replace Java class identifiers. After this replace you will also need to rename the compilation unit, and/or move them to a different folder. This can be done quickly in Eclipse with quick-fixes.

* [ ] Write language specific code. Several language specific implementations need to be created or customized. These are usually marked with the `TODO: LANG` Java comment.

* [ ] Modify `icons/ide-logo.32x32.png`

* [ ] Update Changelog, Features, UserGuide, etc, in documentation folder, according to your IDE.

* [ ] Delete README-MelnormeEclipse.md

##### Merging new updates from upstream MelnormeEclipse repo.
TODO: Need to describe this better


