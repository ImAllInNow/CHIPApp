/*
 * Created by: Robert Tanniru
 * 2014
 */

package chiprogram.chipapp.classes;

public abstract interface SavableObject {
	public abstract void readFromFileString(String fs);
	public abstract String toFileString();
}
