package kontoverwaltung;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * A class for the representation of contact persons
 * Eine Klasse zur Repräsentation von Ansprechpartnern
 * 
 * @author aschwegmann
 * @version 1.0
 *
 */
public class Ansprechpartner implements Serializable {

	private static final long serialVersionUID = 1L;
	private String vorname;
	private String nachname;
	private String telefonnummer;
	final static Pattern VALID_PHONE_NR = Pattern.compile("^(((00|\\+)[1-9]{2}(/| )?\\d{4})|(0[1-9]\\d{3}))(/| )?\\d{2,}$"); // achtung, angepasstes pattern
	
	
	/**
	 * Parametrisierter Konstruktor zur Erzeugung eines Ansprechpartner-Objektes
	 * 
	 * @param vorname
	 * 			Vorname des Ansprechpartners
	 * @param nachname
	 * 			Nachname des Ansprechpartners
	 * @param telefonnummer
	 * 			Telefonnummer des Ansprechpartners
	 * @throws IllegalArgumentException
	 * 			falls Vor- und Nachname nicht jeweils aus mindestens zwei Buchstaben bestehen
	 * 			oder wenn die Telefonnummer keine gültige Telefonnummer ist. Erlaubte Formate sind
	 * 			01234 56789 oder 0049 1234 56789 oder +49 1234 56789
	 * @throws NullPointerException
	 * 			falls bei der Telefonnummer null übergeben wird
	 */
	public Ansprechpartner(String vorname, String nachname, String telefonnummer) {
		this.setVorname(vorname);
		this.setNachname(nachname);
		this.setTelefonnummer(telefonnummer);
	}

	/**
	 * Gibt das Feld "Vorname" dieses Objektes zurück
	 * @return Vorname
	 * 			Vorname des Ansprechpartners
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * Setzt das Feld "Vorname" dieses Objektes
	 * @param vorname
	 * 			Vorname des Ansprechpartners
	 * @throws IllegalArgumentException
	 * 			falls Vorname nicht aus mindestens zwei Buchstaben besteht
	 */
	public void setVorname(String vorname) {
		if (vorname.length()<2)
			throw new IllegalArgumentException("Der Vorname muss aus mindestens zwei Buchstaben bestehen.");
		this.vorname = vorname;
	}

	/**
	 * Gibt das Feld "Nachname" dieses Objektes zurück
	 * @return Nachname
	 * 			Nachname des Ansprechpartners
	 */
	public String getNachname() {
		return nachname;
	}

	/**
	 * Setzt das Feld "Nachname" dieses Objektes
	 * @param nachname
	 * 			Nachname des Ansprechpartners
	 * @throws IllegalArgumentException
	 * 			falls Nachname nicht aus mindestens zwei Buchstaben besteht
	 */
	public void setNachname(String nachname) {
		if (vorname.length()<2)
			throw new IllegalArgumentException("Der Vorname muss aus mindestens zwei Buchstaben bestehen.");
		this.nachname = nachname;
	}

	/**
	 * Gibt das Feld "Telefonnummer" dieses Objektes zurück
	 * @return Telefonnummer
	 * 			Telefonnummer des Ansprechpartners
	 */
	public String getTelefonnummer() {
		return telefonnummer;
	}

	/**
	 * Setzt das Feld "Telefonnummer" dieses Objektes
	 * @param telefonnummer
	 * 			Telefonnummer des Ansprechpartners
	 * @throws IllegalArgumentException
	 * 			falls die Telefonnummer keine gültige Telefonnummer ist. Erlaubte Formate sind
	 * 			01234 56789 oder 0049 1234 56789 oder +49 1234 56789
	 * @throws NullPointerException
	 * 			falls bei der Telefonnummer null übergeben wird
	 * 
	 */
	public void setTelefonnummer(String telefonnummer) {
		Matcher m = VALID_PHONE_NR.matcher(telefonnummer); // prüfe name und hausnummer
		if (!m.find()) {
			throw new IllegalArgumentException("Ungültiges Eingabeformat für Telefonnummer. Erlaubte Formate sind 01234 56789 oder 0049 1234 56789 oder +49 1234 56789");
		}
		this.telefonnummer = telefonnummer;
	}

	/**
	 * Gibt den vollen Namen (die Kombination der Felder "Vorname" und
	 * "Nachname" dieses Objektes zurück.
	 * @return Name
	 * 			Name des Ansprechpartners
	 */
	public String getName() {
		return (getVorname() + " " + getNachname());
	}

	
	@Override
	public String toString() {
		return getName() + "\nTel.:\t\t\t" + telefonnummer + "\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nachname == null) ? 0 : nachname.hashCode());
		result = prime * result + ((telefonnummer == null) ? 0 : telefonnummer.hashCode());
		result = prime * result + ((vorname == null) ? 0 : vorname.hashCode());
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
		Ansprechpartner other = (Ansprechpartner) obj;
		if (nachname == null) {
			if (other.nachname != null)
				return false;
		} else if (!nachname.equals(other.nachname))
			return false;
		if (telefonnummer == null) {
			if (other.telefonnummer != null)
				return false;
		} else if (!telefonnummer.equals(other.telefonnummer))
			return false;
		if (vorname == null) {
			if (other.vorname != null)
				return false;
		} else if (!vorname.equals(other.vorname))
			return false;
		return true;
	}

}
