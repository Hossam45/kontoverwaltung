package kontoverwaltung;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Eine Klasse zur Repräsentation von Bankkonten mit einer IBAN und einem Kontostand.
 * 
 * @author aschwegmann
 * @version 1.0
 *
 */
public class Konto implements Serializable {

	private static final long serialVersionUID = 1L;
	final static Pattern VALID_IBAN = Pattern.compile("^[\\p{Alpha}äöüÄÖÜß]{2}\\d{2}\\s?(\\w{4}\\s?){4}\\w{2}$");
	private String iban;
	private double kontostand;

	//Anmerkung: Ich habe teilweise auch die runtime-Exceptions dokumentiert, bevor ich wusste, dass dies nicht nötig ist. Habe es drin gelassen.
	
	/**
	 * Parametrisierter Konstruktor für die Erzeugung eines Konto-Objektes
	 * @param iban
	 * 			IBAN für dieses Konto-Objekt
	 * @param kontostand
	 * 			Kontostand dieses Kontos 	
	 * @throws IllegalArgumentException	
	 * 			falls die IBAN keine deutsche IBAN ist (österreich und schweiz werden noch nicht unterstützt)
	 * 			Erlaubte Formate sind:
	 * 				DIN 5008  			 z.B. DE12 3456 7890 1234 5678 90
	 * 				maschinenfreundlich	 z.B. DE12345678901234567890
	 * @throws NullPointerException
	 * 			falls bei iban null übergeben wird
	 */			
	public Konto(String iban, double kontostand) {
		this.setIban(iban);
		this.setKontostand(kontostand);
	}

	/**
	 * Gibt die IBAN dieses Kontos aus
	 * @return iban
	 * 			IBAN dieses Kontos
	 */
	public String getIban() {	// setter und getter können hier ruhige alle public sein, da die erzeugten Konto-Objekte geschützt sind
		return iban;
	}

	/**
	 * Setzt die IBAN für dieses Konto
	 * @param iban
	 * 			iban für dieses Konto-Objekt
	 * @throws IllegalArgumentException
	 * 			falls die IBAN keine deutsche IBAN ist (österreich und schweiz werden noch nicht unterstützt)
	 * 			Erlaubte Formate sind:
	 * 				DIN 5008  			 z.B. DE12 3456 7890 1234 5678 90
	 * 				maschinenfreundlich	 z.B. DE12345678901234567890
	 * @throws NullPointerException
	 * 			falls null übergeben wird
	 * 
	 */
	public void setIban(String iban) {
		Matcher m = VALID_IBAN.matcher(iban); // prüfe name und hausnummer
		if (!m.find()) {
			throw new IllegalArgumentException("Ungültige IBAN. Erlaubt sind nur deutsche IBAN nach DIN 5008 oder maschinenfreundlich.");
		}
		this.iban = iban;
	}

	/**
	 * Gibt den Kontostand dieses Kontos aus
	 * @return Kontostand
	 * 			Kontostand dieses Kontos
	 */
	public double getKontostand() {
		return kontostand;
	}

	/**
	 * Setzt das Feld "Kontostand" dieses Kontos
	 * @param kontostand
	 * 			Kontostand dieses Kontos
	 */
	public void setKontostand(double kontostand) {
		this.kontostand = kontostand;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iban == null) ? 0 : iban.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Konto other = (Konto) obj;
		if (iban == null) {
			if (other.iban != null)
				return false;
		} else if (!iban.equals(other.iban))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return iban + ":\tSaldo:\t" + NumberFormat.getCurrencyInstance().format(kontostand) + "\t";
	}

}