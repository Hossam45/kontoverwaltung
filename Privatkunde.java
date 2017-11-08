package kontoverwaltung;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * 
 * This class inherits from the class Kunde and is meant for the representation of private customers
 * 
 * @author aschwegmann
 * @version 1.0
 *
 * @see Kunde
 */
public class Privatkunde extends Kunde {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String vorname;
	private String nachname;
	private Calendar geburtstag;


	/**
	 * Ein Parametrisierter Konstruktor zur Erzeugung eines neuen Privatkunden-Objektes
	 * Das zugehörige Konten-Set wird immer als leeres HashSet erzeugt (keine doppelten Konten). 
	 * Ein neuer Kunde hat somit noch keine Konten.
	 * 
	 * @param kundennummer
	 * 			die Kundennummer dieses Privatkunden als String
	 * @param telefonnummer
	 * 			die Telefonnummer dieses Privatkunden als String
	 * @param email
	 * 			die Email-Adresse dieses Privatkunden als String
	 * @param adresse
	 * 			die Adresse dieses Privatkunden als Adresse-Objekt
	 * @param vorname
	 * 			der Vorname dieses Privatkunden als String
	 * @param nachname
	 * 			der Nachname dieses Privatkunden als String
	 * @param geburtstag
	 * 			der Geburtstag dieses Privatkunden als Calendar-Objekt
	 */
	public Privatkunde(String kundennummer, String telefonnummer, String email, Adresse adresse, String vorname, String nachname, Calendar geburtstag) {
		super(kundennummer, telefonnummer, email, adresse);
		this.setVorname(vorname);
		this.setNachname(nachname);
		this.setGeburtstag(geburtstag);
	}

	/**
	 * Gibt das Feld "vorname" dieses Privatkunden-Objektes zurück
	 * @return
	 * 			Der Vorname dieses Privatkunden als String
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * Setzt das Feld vorname dieses Privatkunden-Objektes
	 * @param vorname
	 * 			Der Vorname dieses Privatkunden als String
	 */
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	/**
	 * Gibt das Feld "nachname" dieses Privatkunden-Objektes zurück
	 * @return
	 * 			Der Nachname dieses Privatkunden als String
	 */
	public String getNachname() {
		return nachname;
	}

	/**
	 * Setzt das Feld nachname dieses Privatkunden-Objektes
	 * @param nachname
	 * 			Der Nachname dieses Privatkunden als String
	 */
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	/**
	 * Gibt das Feld "geburtstag" dieses Privatkunden-Objektes zurück
	 * @return
	 * 			Der Geburtstag dieses Privatkunden als Calendar-Objekt
	 */
	public Calendar getGeburtstag() {
		return geburtstag;
	}

	/**
	 * Setzt das Feld geburtstag dieses Privatkunden-Objektes
	 * @param geburtstag
	 * 			Der Geburtstag dieses Privatkunden als Calendar-Objekt
	 */
	public void setGeburtstag(Calendar geburtstag) {
		this.geburtstag = geburtstag;
	}

	@Override
	/**
	 * Gibt den vollen Namen dieses Privatkunden als String zurück.
	 * Der volle Name ist die kombination der Felder vorname und nachname, getrennt
	 * durch ein Leerzeichen.
	 * @return
	 * 			Der volle Name dieses Privatkunden als String
	 */
	public String getName() {
		return (getVorname() + " " + getNachname());
	}

	@Override
	public String toString() {
		return "\nName:\t\t\t" + getName() + "\nGeburtstag:\t\t"
				+ DateFormat.getDateInstance(DateFormat.MEDIUM).format(getGeburtstag().getTime()) + "\n" + super.toString() + "\n";
	}

}
