package kontoverwaltung;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * This is an abstract class containing the common data of customers/clients
 * It's meant to be extended to specified Versions of clients, for example a business client.
 * 
 * @author aschwegmann
 * @version 1.0
 *
 * @see UserInterface
 * @see Privatkunde
 * @see Firmenkunde
 */
public abstract class Kunde implements Serializable, Comparable<Kunde> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Pattern VALID_EMAIL = Pattern.compile("^[A-Z0-9\\._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	final static Pattern VALID_PHONE_NR = Pattern.compile("^(((00|\\+)[1-9]{2}(/| )?\\d{4})|(0[1-9]\\d{3}))(/| )?\\d{2,}$"); // achtung, angepasstes pattern
	private String kundennummer;
	private String telefonnummer;
	private String email;
	private Adresse adresse;
	private Set<Konto> konten; // max 10

	
	
	/**
	 * Parametrisierter Konstruktor, mit dem die gemeinsamen Elemente eines neuen Kundenobjektes
	 * gesetzt werden. Der Konstruktor kann nur �ber den super-Aufruf von erbenden Subklassen verwendet werden.
	 * Das Konten-Set wird immer als leeres HashSet erzeugt (keine doppelten Konten). Ein neuer Kunde hat somit
	 * immer ein leeres Konten-Set.
	 * 
	 * @param kundennummer
	 * 			die Kundennummer dieses Kunden als String
	 * @param telefonnummer
	 * 			die Telefonnummer dieses Kunden als String
	 * @param email
	 * 			die Email-Adresse dieses Kunden als String
	 * @param adresse
	 * 			die Adresse dieses Kunden als Adresse-Objekt
	 * 
	 */
	public Kunde(String kundennummer, String telefonnummer, String email, Adresse adresse) {
		this.setKundennummer(kundennummer);
		this.setTelefonnummer(telefonnummer);
		this.setEmail(email);
		this.setAdresse(adresse);
		konten = new HashSet<Konto>();
	}

	/**
	 * Abstrakte Methode zur implementierung in den Subklassen um Polymorphie zu erm�glichen.
	 * Soll dann den Namen des Objektes, wenn vorhanden, zur�ckgeben.
	 * @return name
	 * 			den Namen dieses Kunden, je nach implementierung in den Subklassen
	 */
	public abstract String getName();

	/**
	 * Gibt das Feld "Kundennummer" dieses Kunden-Objektes zur�ck
	 * @return
	 * 			die Kundennummer dieses Kunden
	 */
	public String getKundennummer() {
		return kundennummer;
	}

	/**
	 * Setzt das Feld kundennummer dieses Kunden-Objektes
	 * @param kundennummer
	 * 			die Kundennummer dieses Kunden
	 */
	public void setKundennummer(String kundennummer) {
		this.kundennummer = kundennummer;
	}

	/**
	 * Gibt das Feld "Telefonnummer" dieses Kunden-Objektes zur�ck
	 * @return Telefonnummer
	 * 			die Telefonnummer dieses Kunden als String
	 */
	public String getTelefonnummer() {
		return telefonnummer;
	}

	/**
	 * Setzt das Feld telefonnummer dieses Kunden-Objektes.
	 * Die Telefonnummer muss eine g�ltige Telefonnummer sein, sonst kommt es zu einem Laufzeitfehler.
	 * @param telefonnummer
	 * 			eine g�ltige Telefonnummer als String
	 */
	public void setTelefonnummer(String telefonnummer) {
		Matcher m = VALID_PHONE_NR.matcher(telefonnummer); // pr�fe name und hausnummer
		if (!m.find()) {
			throw new IllegalArgumentException("Ung�ltiges Eingabeformat f�r Telefonnummer. Erlaubte Formate sind z.B. 01234 56789 oder 0049 1234 56789 oder +49 1234 56789");
		}
		this.telefonnummer = telefonnummer;
	}

	/**
	 * Gibt das Feld "Email" dieses Objektes zur�ck
	 * @return
	 * 			die Email-Adresse dieses Kunden
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setzt das Feld Email dieses Kunden.
	 * Die Email muss eine g�ltige Email sein, sonst kommt es zu einem Laufzeitfehler.
	 * @param email
	 * 			Eine g�ltige Email als String
	 */
	public void setEmail(String email) {
		Matcher m = VALID_EMAIL.matcher(email); // pr�fe name und hausnummer
		if (!m.find()) {
			throw new IllegalArgumentException("Ung�ltige E-Mail Adresse.");
		}
		this.email = email;
	}

	/**
	 * Gibt das Feld "Adresse" dieses Objektes als Adressobjekt zur�ck
	 * @return
	 * 			die Adresse dieses Kunden als Adresse-Objekt
	 */
	public Adresse getAdresse() {
		return adresse;
	}

	/**
	 * Setzt das Feld Adresse dieses Kunden
	 * @param adresse
	 * 			die Adresse dieses Kunden als Adresse-Objekt
	 */
	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	/**
	 * Gibt das Feld Konten dieses Objektes als Set (Laufzeittyp: HashSet) dieses Objektes zur�ck
	 * @return
	 * 			die Konten dieses Objektes als Set (Laufzeittyp: HashSet)
	 */
	public Set<Konto> getKonten() {
		return konten;
	}

	/**
	 * Setzt das Feld Konto dieses Kunden. Methode ist private, da ein Hinzuf�gen eines kompletten Konto-Sets
	 * nicht vorgesehen ist, und das Hinzuf�gen einzelner Konten �ber die entsprechende Methode erfolgen sollten.
	 * @param konten
	 * 
	 * @throws AccountLimitReachedException
	 * 			Wird geworfen, wenn ein Set mit mehr als 10 Konten �bergeben wird, da die maximale Kontenzahl
	 * 			pro Kunde auf 10 begrenzt wurde.
	 * 
	 * @see addKonto
	 */
	@SuppressWarnings("unused")
	private void setKonten(Set<Konto> konten) throws AccountLimitReachedException{
		if (konten.size() > 10) throw new AccountLimitReachedException("Maximum limit of accounts: 10");
		this.konten = konten;
	}

	/**
	 * F�gt der Kontoliste dieses Kunden ein Konto hinzu
	 * @param konto
	 * 			Konto-Objekt, das hinzugef�gt werden soll
	 * @return
	 * 			true, wenn das hinzuf�gen erfolgreich war, sonst false
	 * @throws AccountLimitReachedException
	 *	 		Wird geworfen, wenn der Kunde bereits 10 Konten besitzt, da die maximale Kontenzahl
	 * 			pro Kunde auf 10 begrenzt wurde.
	 */
	public boolean addKonto(Konto konto) throws AccountLimitReachedException{ // max 10 > eigene exception
		if (konten.size() >= 10) throw new AccountLimitReachedException("Maximum limit of accounts: 10");
		return this.konten.add(konto);
	}

	/**
	 * Entfernt das �bergebene Konto aus der Kontoliste dieses Kunden
	 * @param konto
	 * @return	true, wenn das �bergebene Konto erfolgreich aus der Collection entfernt werden konnte, sonst false.
	 * 			
	 */
	public boolean removeKonto(Konto konto) {
		return this.konten.remove(konto);
	}

	@Override
	public int compareTo(Kunde other) {
		return this.kundennummer.compareTo(other.kundennummer);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kundennummer == null) ? 0 : kundennummer.hashCode());
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
		Kunde other = (Kunde) obj;
		if (kundennummer == null) {
			if (other.kundennummer != null)
				return false;
		} else if (!kundennummer.equals(other.kundennummer))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Kundennummer:\t\t" + kundennummer + "\nTelefonnummer:\t\t" + telefonnummer + "\nEmail:\t\t\t" + email
				+ "\nAdresse:\t\t" + adresse + "Gelistete Konten: (" + konten.size() + ")\t" + konten;
	}
}

/**
 * Eine Exception, die geworfen wird, wenn einem Kundenobjekt zuviele Konten zugeordnet werden
 * 
 * @author aschwegmann
 *
 */
class AccountLimitReachedException extends Exception {

	public AccountLimitReachedException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;
}