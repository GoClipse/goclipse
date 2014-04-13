package melnorme.lang.jdt.ui.wizards.dialogfields;

/**
 * Change listener used by <code>ListDialogField</code> and <code>CheckedListDialogField</code>
 * 
 * @param <E> the type of the list elements
 */
public interface IListAdapter<E> {

	/**
	 * A button from the button bar has been pressed.
	 * 
	 * @param field the dialog field 
	 * @param index the button index
	 */
	void customButtonPressed(ListDialogField<E> field, int index);

	/**
	 * The selection of the list has changed.
	 * 
	 * @param field the dialog field 
	 */
	void selectionChanged(ListDialogField<E> field);

	/**
	 * An entry in the list has been double clicked
	 * 
	 * @param field the dialog field 
	 */
	void doubleClicked(ListDialogField<E> field);

}
