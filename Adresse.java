package kontoverwaltung;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * A class for the representation of addresses
 * 
 * @author aschwegmann
 * @version 1.0
 *
 */
public class Adresse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String adresszeile1;
	private String adresszeile2;
	private int plz;
	private String ort;
	private final static Pattern VALID_STR_HNR = Pattern.compile("^([\\p{Alpha}äöüÄÖÜß]\\.?{2,}(\\s|\\-)?)+\\d+[\\p{Alpha}]?$");
	private final static Pattern VALID_PLZ = Pattern.compile("^\\d{5}$");
	

	/**
	 * Parametrisierter Konstruktor zur Erzugung eines Adress-Objektes
	 * @param adresszeile1
	 * 			Strassenname und Hausnummer - die Hausnummer kann einen Buchstaben am Ende besitzen. z.B. 52a
	 * @param adresszeile2
	 * 			zusätzliche Adressinformationen
	 * @param plz
	 * 			fünfstellige Postleitzahl
	 * @param ort
	 * 			Ort
	 * @throws IllegalArgumentException
	 * 			falls der Strassenname ungültige zeichen enthält, die Hausnummer nicht dem Format
	 * 			Zahl(ein optionaler Buchstabe) z.B. 51a oder 51 entspricht, die Postleitzahl keine Zahl
	 * 			zwischen 10000 und 99999 ist oder der Ort ein leerer String ist
	 * @throws NullPointerException
	 * 			falls null übergeben wird (ausser bei adresszeile 2)
	 */
	public Adresse(String adresszeile1, String adresszeile2, int plz, String ort) {
		this.setAdresszeile1(adresszeile1);
		this.setAdresszeile2(adresszeile2);
		this.setPlz(plz);
		this.setOrt(ort);
	}

	/**
	 * Returns the Adresszeile field of this Object
	 * @return Strasse und Hausnummer
	 */
	public String getAdresszeile1() {
		return adresszeile1;
	}

	/**
	 * Sets the Adresszeile field of this Object
	 * @param adresszeile1
	 * 			Strasse und Hausnummer- die Hausnummer kann einen Buchstaben am Ende besitzen. z.B. 52a
	 * 
	 * @throws IllegalArgumentException
	 * 			falls der Strassenname ungültige zeichen enthält oder die Hausnummer nicht dem Format
	 * 			Zahl-(ein optionaler Buchstabe) entspricht
	 * @throws NullPointerException
	 * 			falls null übergeben wird
	 */
	public void setAdresszeile1(String adresszeile1) {
		
		Matcher m = VALID_STR_HNR.matcher(adresszeile1); // prüfe name und hausnummer
		if (!m.find()) {
			throw new IllegalArgumentException("Ungültiges Eingabeformat für Strasse + Hausnummer");
		}
		
		this.adresszeile1 = adresszeile1;
	}

	/**
	 * Returns the Adresszeile 2 field of this Object
	 * @return Optionale Adressdaten
	 */
	public String getAdresszeile2() {
		return adresszeile2;
	}
	
	/**
	 * Sets the Adresszeile 2 field of this Object
	 * @param adresszeile2
	 * 			zusätzliche Adressinformationen
	 */
	public void setAdresszeile2(String adresszeile2) {
		this.adresszeile2 = adresszeile2;
	}

	/**
	 * Returns the Postleitzahl field of this Object
	 * @return Postleitzahl
	 */
	public int getPlz() {
		return plz;
	}

	/**
	 * Sets the Postleitzahl field of this Object
	 * @param plz
	 * 			Postleitzahl
	 * @throws IllegalArgumentException
	 * 			falls die Postleitzahl keine Zahl zwischen 10000 und 99999 ist
	 * @throws NullPointerException
	 * 			falls null übergeben wird
	 */
	public void setPlz(int plz) {
		
		Matcher m = VALID_PLZ.matcher(String.format("%05d",plz));
		
		// TODO überprüfung ob plz existiert und mit wohnort zusammenpasst
		if (!m.find()) {
			throw new IllegalArgumentException("Ungültiges Eingabeformat bei der Postleitzahl");
		}
		
		this.plz = plz;
	}

	/**
	 * Returns the Ort field of this Object
	 * @return Ort
	 */
	public String getOrt() {
		return ort;
	}

	/**
	 * Sets the Ort field of this Object
	 * @param ort
	 * 
	 * @throws IllegalArgumentException
	 * 			falls der Ort ein leerer String oder null ist
	 */
	public void setOrt(String ort) {
		if (ort == null || ort.length()<1){
			throw new IllegalArgumentException("Der Ort muss aus mindestens einem Buchstaben bestehen");
		}
		this.ort = ort;
	}

	@Override
	public String toString() {    

		if (getAdresszeile2().isEmpty()) {
			return getAdresszeile1() + "\n" + "\t\t\t" + String.format("%05d",getPlz()) + " " + getOrt() + "\n";
		}
		return getAdresszeile1() + "\n" + "\t\t\t" + getAdresszeile2() + "\n" + "\t\t\t" + getPlz() + " " + getOrt() + "\n";
	}

	/**
	 * Alternative toString()-Methode, die keine Tabulatoren vor den Zeilen nach der ersten Zeile erzeugt
	 * @return
	 * 		die Stringrepräsentation der Objektwerte ohne vorlaufende Tabulatoren
	 */
	public String toStringCompact() {

		if (getAdresszeile2().isEmpty()) {
			return getAdresszeile1() + "\n" + getPlz() + " " + getOrt() + "\n";
		}
		return getAdresszeile1() + "\n" + getAdresszeile2() + "\n" + getPlz() + " " + getOrt() + "\n";
	}

}
