package kontoverwaltung;

/**
 * 
 * This class inherits from the class Kunde and is for the representation of business customers
 * 
 * @author aschwegmann
 * @version 1.0
 *
 * @see Kunde
 */
public class Firmenkunde extends Kunde {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String firmenname;
	private Ansprechpartner ansprechpartner;

	/**
	 * Ein Parametrisierter Konstruktor zur Erzeugung eines neuen Firmenkunden-Objektes
	 * Das zugehörige Konten-Set wird immer als leeres HashSet erzeugt (keine doppelten Konten). 
	 * Ein neuer Kunde hat somit noch keine Konten.
	 * 
	 * @param kundennummer
	 * 			die Kundennummer dieses Kunden als String
	 * @param telefonnummer
	 * 			die Telefonnummer dieses Kunden als String
	 * @param email
	 * 			die Email-Adresse dieses Kunden als String
	 * @param adresse
	 * 			die Adresse dieses Kunden als Adresse-Objektes
	 * @param firmenname
	 * 			der Firmenname dieses Geschäftskunden
	 * @param ansprechpartner
	 * 			der Ansprechpartner dieses Geschäftskunden als Ansprechpartner-Objekt
	 */
	public Firmenkunde(String kundennummer, String telefonnummer, String email, Adresse adresse, String firmenname,	Ansprechpartner ansprechpartner) {
		super(kundennummer, telefonnummer, email, adresse);
		this.setFirmenname(firmenname);
		this.setAnsprechpartner(ansprechpartner);
	}

	/**
	 * Gibt das Feld "firmenname" dieses Geschäftskunden-Objektes zurück
	 * @return
	 * 			Der Name der Firma dieses Geschäftskunden als String
	 */
	public String getFirmenname() {
		return firmenname;
	}

	/**
	 * Setzt das Feld firmenname dieses Geschäftskunden-Objektes
	 * @param firmenname
	 * 			Der Name der Firma dieses Geschäftskunden als String
	 */
	public void setFirmenname(String firmenname) {
		this.firmenname = firmenname;
	}

	/**
	 * Gibt das Feld "ansprechpartner" dieses Geschäftskunden-Objektes zurück
	 * @return
	 * 			Der Ansprechpartner dieses Geschäftskunden als Ansprechpartner-Objekt
	 */
	public Ansprechpartner getAnsprechpartner() {
		return ansprechpartner;
	}

	/**
	 * Setzt das Feld ansprechpartner dieses Geschäftskunden
	 * @param ansprechpartner
	 * 			Der Ansprechpartner dieses Geschäftskunden als Ansprechpartner-Objekt
	 */
	public void setAnsprechpartner(Ansprechpartner ansprechpartner) {
		this.ansprechpartner = ansprechpartner;
	}

	@Override
	/**
	 * Gibt den Namen der Firma dieses Geschäftskunden als String zurück.
	 * Ruft im Grunde nur getFirmenname() auf und dient hier rein der Polymorphie.
	 * @return
	 * 			Der Inhalt des Feldes firmenname
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return (getFirmenname());
	}

	@Override
	public String toString() {
		return "\nName:\t\t\t" + getName() + "\n" + super.toString() + "\nAnsprechpartner:\t" + getAnsprechpartner();
	}

}
