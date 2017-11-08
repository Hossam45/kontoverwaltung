package kontoverwaltung;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import kontoverwaltung.UserInterface.Style;

/**
 * 
 * This class represents banks and gives various methods to set and handle the data of bank-objects
 * 
 * @author aschwegmann
 * @version 1.0
 *
 * @see UserInterface
 * @see Kunde
 * @see Privatkunde
 * @see Firmenkunde
 * @see Konto
 * @see Ansprechpartner
 * @see Adresse 
 */
public final class Bank implements Serializable { // das Final steht hier zur Sicherheit

	private static final long serialVersionUID = 1L;
	private String name;
	private String bic;
	private Adresse adresse;
	private Set<Kunde> kunden;
	private List<Ansprechpartner> bekannteAnsprechparnter = new ArrayList<Ansprechpartner>();

	/**
	 * Parametrisierter Konstruktor zur Erzeugung eines neuen Bank-Objektes.
	 * Jede Bank hat ein eigenes Set von Kunden. Die Einzigartigkeit der Kunden wird durch
	 * die Kundennummer realisiert. Das Set ist bei der Erzeugung standardmäßig leer.
	 * 
	 * @param name
	 * 			Name der Bank als String
	 * @param bic
	 * 			Bank-Identifikationsnummer als String
	 * @param adresse
	 * 			Adresse der Bank als Adresse-Objekt
	 */
	public Bank(String name, String bic, Adresse adresse) {
		this.setName(name);
		this.setBic(bic);
		this.setAdresse(adresse);
		this.setKunden(new HashSet<Kunde>());
	}

	/**
	 * Gibt das Feld "name" dieser Bank zurück
	 * @return
	 * 			Der Name dieser Bank
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzt das Feld name dieser Bank
	 * @param name
	 * 			Der Name dieser Bank
	 */
	public void setName(String name) { // name der bank
		this.name = name;
	}

	/**
	 * Gibt das Feld "bic" dieser Bank zurück
	 * @return
	 * 			Die Bank-Identifikationsnummer dieser Bank
	 */
	public String getBic() {
		return bic;
	}

	/**
	 * Setzt das Feld bic dieser Bank
	 * @param bic
	 * 			Die Bank-Identifikationsnummer dieser Bank
	 */
	public void setBic(String bic) {
		this.bic = bic;
	}

	/**
	 * Gibt das Feld "adresse" dieser Bank zurück
	 * @return
	 * 			Die Adresse dieser Bank
	 */
	public Adresse getAdresse() {
		return adresse;
	}

	/**
	 * Setzt das Feld adresse dieses Kunden-Objektes
	 * @param adresse
	 * 			Die Adresse dieser Bank
	 */
	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	// public Set<? extends Kunde> getKunden() { } // Kundenliste ist geheim. Da
	// eh nicht vererbt wird - kein Getter > sichert die Kundenliste gegen
	// Veränderungen, die nicht über Methoden laufen

	/**
	 * Gibt die Anzahl der Kunden in der Kundenliste dieser Bank zurück
	 * @return
	 * 			Anzahl der Kunden in der Kundenliste
	 */
	public int getKundenZahl() { // Anzahl der Kunden ist nicht geheim.
		return kunden.size();
	}

	/**
	 * Private Methode, um die Kundenliste zu setzen. Wird im 
	 * grunde nur vom Konstruktor verwendet
	 * @param kunden
	 * 			Ein Set für Kunden-Objekte
	 */
	private void setKunden(Set<Kunde> kunden) {
		this.kunden = kunden;
	}

	// Nutzermethoden

	// (01) Privatkunde anlegen

	/**
	 * Diese Methode legt einen neuen Privatkunden an und fügt ihn der Kundenliste hinzu.
	 * 
	 * @param kundennummer
	 * 			die Kundennummer dieses Kunden als String
	 * @param vorname
	 * 			der Vorname des Kunden als String
	 * @param nachname
	 * 			der Nachname des Kunden als String
	 * @param adressZeile1
	 * 			die erste Adresszeile des neuen Kunden mit Straße und Hausnummer als String
	 * @param adressZeile2
	 * 			eine zweite optionale Adresszeile - kann leer bleiben
	 * @param plz
	 * 			die fünfstellige Postleitzahl des neuen Kunden als int
	 * @param wohnort
	 * 			der Wohnort des Kunden als String
	 * @param telefonnummer
	 * 			die Telefonnummer dieses Kunden als String
	 * @param email
	 * 			die Email-Adresse dieses Kunden als String
	 * @param geburtstag
	 * 			der Geburtstag des Kunden als Calendar-Objekt
	 * @return
	 * 			true, wenn das hinzufügen in die Liste erfolgreich war, sonst false
	 */
	public boolean addPrivateClient(String kundennummer, String vorname, String nachname, String adressZeile1,
			String adressZeile2, int plz, String wohnort, String telefonnummer, String email, Calendar geburtstag) {
		
		return kunden.add(new Privatkunde(kundennummer, telefonnummer, email,
				new Adresse(adressZeile1, adressZeile2, plz, wohnort), vorname, nachname, geburtstag));

	}

	// (02) Firmenkunde anlegen

	/**
	 * Diese Methode legt einen neuen Geschäftskunden an und fügt ihn der Kundenliste hinzu.
	 * Wenn der Ansprechpartner bereits bekannt ist wird auf den bekannten Ansprechpartner referenziert, ansonsten
	 * wird auch dieser neu angelegt.
	 * 
	 * @param kundennummer
	 * 			die Kundennummer dieses Kunden als String
	 * @param firmenname
	 * 			der Name der Firma als String
	 * @param adressZeile1
	 * 			die erste Adresszeile des neuen Kunden mit Straße und Hausnummer als String
	 * @param adressZeile2
	 * 			eine zweite optionale Adresszeile - kann leer bleiben
	 * @param plz
	 * 			die fünfstellige Postleitzahl des neuen Kunden als int
	 * @param wohnort
	 * 			der Wohnort des Kunden als String
	 * @param telefonnummer
	 * 			die Telefonnummer dieses Kunden als String
	 * @param email
	 * 			die Email-Adresse dieses Kunden als String
	 * @param apVorname
	 * 			der Vorname des Ansprechpartners als String
	 * @param apNachname
	 * 			der Nachname des Ansprechpartners als String
	 * @param apTelefonnummer
	 * 			die Telefonnummer des Ansprechpartners als String
	 * @return	
	 * 			true, wenn das hinzufügen in die Liste erfolgreich war, sonst false
	 */
	public boolean addBusinessClient(String kundennummer, String firmenname, String adressZeile1, String adressZeile2,
			int plz, String wohnort, String telefonnummer, String email, String apVorname, String apNachname,
			String apTelefonnummer) {

		Ansprechpartner ansprechpartner = new Ansprechpartner(apVorname, apNachname, apTelefonnummer);

		if (bekannteAnsprechparnter.contains(ansprechpartner)) { // schon in der Liste
			ansprechpartner = bekannteAnsprechparnter.get(bekannteAnsprechparnter.indexOf(ansprechpartner));	// verhindert zwei Ansprechpartner-Objekte gleichen Inhalts
		} else { // war noch unbekannt > der liste der bekannten Ansprechpartner hinzufügen
			bekannteAnsprechparnter.add(ansprechpartner);
		}

		return kunden.add(new Firmenkunde(kundennummer, telefonnummer, email,
				new Adresse(adressZeile1, adressZeile2, plz, wohnort), firmenname, ansprechpartner));
	}

	// (03) Konto anlegen und Kundennummer zuordnen

	/**
	 * Diese Methode legt ein neues Kontoobjekt an, und fügt es einem Kunden aus der 
	 * Kundenliste hinzu. Sollte es hierbei zu Fehlern kommen, werden diese als Fehlertext
	 * auf der Konsole ausgegeben. Mögliche Fehlerursachen sind
	 * 		- es gibt den genannten Kunden nicht 
	 * 		- das Konto existiert bereits
	 * 		- der Kunde hat bereits die maximale Anzahl von Konten erreicht
	 * 		- unbekannte Ausnahmefehler
	 * 
	 * @param kundennummer
	 * 		die Kundennummer des Kunden, dem das neue Konto zugeordnet werden soll als String
	 * @param iban
	 * 		die IBAN für das neue Konto als String
	 * @param startkapital
	 * 		das Startkapital, dass bei der Erzeugung des neuen Kontos auf das Konto gebucht werden soll
	 * @return
	 * 		true, wenn das Konto fehlerfrei erzeugt ud zugeordnet werden konnte, sonst false (zusätzliche Informationen in diesem Fall auf der Konsole)
	 */
	public boolean addAccount(String kundennummer, String iban, Double startkapital) {
		
		if (!getClientByKdnr(kundennummer, false)) {
			System.err.println("Keinen Eintrag für Kundennummer " + kundennummer + " gefunden");
		}

		for (Kunde kunde : kunden) {
			for (Konto konto : kunde.getKonten()) {
				if (konto.getIban().equals(iban)) {
					System.err.println("Das Konto mit der IBAN   " + iban + "   existiert bereits!");
					return false;
				}
			}
			if (kunde.getKundennummer().equals(kundennummer)) {

				if (kunde.getKonten().size() >= 10) {
					System.err.println("Maximale Kontenzahl für Kunden erreicht");
					return false;
				}

				try {
					if (kunde.addKonto(new Konto(iban, startkapital))){
						System.out.println("Das Konto " + iban + " wurde dem Kunden " + kundennummer + " hinzugefügt");
						return true;
					}
					return false;
				} catch (AccountLimitReachedException ex){
					System.err.println("Der Kunde hat bereits die maximale Anzahl von Konten (10)");
					return false;
				}
			}
		}
		assert false : "Im Grunde sollte dieser Code nie erreicht werden und da nachfolgende Returnstatement dient nur dazu, dass der Code compiliert";
		return false; 
	}

	// (04) Kunde mit Konten anzeigen (Auswahl durch Kundennummer)

	/**
	 * Diese überladene Methode gibt die Informationen über Kunden, der durch eine Kundennummer ausgewählt
	 * werden kann, auf der Konsole aus. 
	 * 
	 * @param kundennummer
	 * 			Die Kundennummer des Kunden, der ausgegeben werden soll.
	 * @return
	 * 			true, wenn der Kunde existiert, false, wenn er nicht existiert
	 */
	public boolean getClientByKdnr(String kundennummer) {
		return getClientByKdnr(kundennummer, true);
	}

	/**
	 * Diese überladene Methode gibt die Informationen über Kunden, der durch eine Kundennummer ausgewählt
	 * werden kann, auf der Konsole aus. Zusätzlich wurde noch ein boolean flag eingebaut, um die Konsolenausgabe
	 * zu unterbinden. Dadurch kann diese Methode auch verwendet werden, um nur den returnvalue zu nutzen, z.B.
	 * zum Test, ob es einen Kunden mit dieser Kundennummer bereits gibt.
	 * 
	 * @param kundennummer
	 * 			Die Kundennummer des Kunden, der ausgegeben und/oder geprüft werden soll.
	 * @param echo
	 * @return
	 * 			true, wenn der Kunde existiert, false, wenn er nicht existiert
	 */
	public boolean getClientByKdnr(String kundennummer, boolean echo) {
		for (Kunde kunde : kunden) {
			if (kunde.getKundennummer().equalsIgnoreCase(kundennummer)) {

				if (echo) System.out.println(kunde);
				return true;
				
			}
		}
		return false;
	}

	/**
	 * Gibt zurück, wie viele Konten der Kunde mit der übergebenen Kundennummer besitzt.
	 * 
	 * @param kundennummer
	 * 			Kontonummer des Kunden, dessen Kontoanzahl zurückgegeben werden soll.
	 * @return
	 * 			Anzahl der Konten. Wenn der Kunde nicht existiert: -1
	 */
	public int getClientAccountNumByKdnr(String kundennummer) { 
		for (Kunde kunde : kunden) {
			if (kunde.getKundennummer().equalsIgnoreCase(kundennummer)) {
				return kunde.getKonten().size();
			} 
		}
		return -1;
	}

	// (05) Kunde mit Konten anzeigen (Auswahl durch Name)

	/**
	 * Diese überladene Methode gibt die Informationen über Kunden, der durch seinen Namen ausgewählt
	 * werden kann, auf der Konsole aus. In dieser Version werden immer die vollen Informationen über den
	 * Kunden auf die Konsole gegeben.
	 * 
	 * @param name
	 * 			Voller Name (Vor- und Nachname) des Kunden, der ausgegeben werden soll.
	 * @return
	 * 			true, wenn der Kunde existiert, sonst false
	 */
	public boolean getClientByName(String name) {
		
		return getClientbyName(name, Style.DETAILED);
		
	}

	/**
	 * Diese überladene Methode gibt die Informationen über Kunden, der durch seinen Namen ausgewählt
	 * werden kann, auf der Konsole aus. Über ein zusätzliches Flag kann gewählt werden, in welchem 
	 * Detailgrad die Information angezeigt wird:
	 * 		COMPACT:	Nur die Kundennummern und der Name
	 * 		NORMAL:		Kundennummer, Name und Adresse
	 * 		DETAILED:	Alle Informationen inkl. Konten
	 * 
	 * @param name
	 * 			Voller Name (Vor- und Nachname) des Kunden, der ausgegeben werden soll.
	 * @param style
	 * 			
	 * @return
	 * 			true, wenn der Kunde existiert, sonst false
	 */
	public boolean getClientbyName(String name, Style style) {
		
		boolean retval = false;

		for (Kunde kunde : kunden) {
			
			if (kunde.getName().equalsIgnoreCase(name.trim())) {
				switch (style) {
				case COMPACT:
					System.out.println("Kundennummer " + kunde.getKundennummer() + ": " + kunde.getName());
					break;
				case NORMAL:
					System.out.println("Kundennummer " + kunde.getKundennummer() + ":\n" + kunde.getName() + "\n"
							+ kunde.getAdresse());
					break;
				case DETAILED:
					System.out.println(kunde);
					break;
				default:
					assert false;
					break;
				}

				retval = true;
			}
		}

		return retval;

	}

	// (06) Konto anzeigen (Auswahl durch IBAN)

	/**
	 * Diese Methode gibt die Informationen eines Kontos, das durch die Kontonummer ausgewählt
	 * werden kann, auf der Konsole aus. Die Angezeigte Information ist die Iban, Saldo, sowie der Kunde,
	 * auf den das Konto registriert ist. 
	 * 
	 * @param iban
	 * 			IBAN (deutsche IBAN in DIN 5008 (z.B. DE12 3456 7890 1234 5678 90)) des auszugebenen Kontos 
	 * @return
	 * 			true, wenn das Konto existiert, sonst false
	 */
	public boolean getAccount(String iban) { 
		
		for (Kunde kunde : kunden) {
			for (Konto konto : kunde.getKonten()) {
				if (konto.getIban().equals(iban)) {

					System.out.println(konto + "\nRegistriert auf:");
					System.out.println("Kundennummer " + kunde.getKundennummer() + ":\n" + kunde.getName() + "\n"
							+ kunde.getAdresse().toStringCompact());
					// System.out.println(kunde);

					return true;

				}
			}
		}
		System.err.println("Account " + iban + "not found");
		return false;
		
	}

	// (07) Alle Kunden unsortiert anzeigen

	/**
	 * Diese Methode gibt die Informationen über alle Kunden in der Datenbank auf der Konsole aus. (unsortiert)
	 * Über ein zusätzliches Flag kann gewählt werden, in welchem Detailgrad die Information angezeigt wird:
	 * 
	 * 		COMPACT:	Nur die Kundennummern und der Name
	 * 		NORMAL:		Kundennummer, Name und Adresse
	 * 		DETAILED:	Alle Informationen inkl. Konten
	 * 
	 * @param stlye
	 * 			Style-Flag, das den Detailgrad der Konsolenausgabe bestimmt
	 */
	public void getAllClients(Style stlye) {

		List<Kunde> kundenUnsortedTemp = new ArrayList<Kunde>(kunden);

		Collections.shuffle(kundenUnsortedTemp); // wirkliche unsortiertheit gewährleisten durch shuffle
		
		Set<Kunde> kundenUnsorted = new LinkedHashSet<Kunde>(kundenUnsortedTemp);

		printAllOnConsole(kundenUnsorted, stlye);

	}

	// (08) Alle Kunden sortiert nach aufsteigender Kundenummer anzeigen

	/**
	 * Diese Methode gibt die Informationen über alle Kunden in der Datenbank sortiert nach aufsteigender Kundennummer auf der Konsole aus.
	 * Über ein zusätzliches Flag kann gewählt werden, in welchem Detailgrad die Information angezeigt wird:
	 * 
	 * 		COMPACT:	Nur die Kundennummern und der Name
	 * 		NORMAL:		Kundennummer, Name und Adresse
	 * 		DETAILED:	Alle Informationen inkl. Konten
	 * 
	 * @param stlye
	 * 			Style-Flag, das den Detailgrad der Konsolenausgabe bestimmt
	 */
	public void getAllClientsSorted(Style stlye) {

		TreeSet<Kunde> kundenSorted = new TreeSet<Kunde>(kunden); // neues treeset sortiert automatisch nach natürlichem rang

		printAllOnConsole(kundenSorted, stlye);

	}

	// (09) Alle Konten unsortiert anzeigen
	
	/**
	 * Diese Methode gibt die Informationen über alle Konten in der Datenbank auf der Konsole aus. (unsortiert)
	 * Über ein zusätzliches Flag kann gewählt werden, in welchem Detailgrad die Information angezeigt wird:
	 * 
	 * 		COMPACT:	Nur die IBAN und das Saldo
	 * 		NORMAL:		IBAN, Saldo, und der Kunde, auf den das Konto registriert ist
	 * 		DETAILED:	IBAN, Saldo, und alle Kundeninformationen
	 * 
	 * @param stlye
	 * 			Style-Flag, das den Detailgrad der Konsolenausgabe bestimmt
	 */
	public void getAllAccounts(Style stlye) {

		Map<Konto, Kunde> kontenUnsorted = new HashMap<Konto, Kunde>(); 

		for (Kunde kunde : kunden) {

			for (Konto konto : kunde.getKonten()) {

				kontenUnsorted.put(konto, kunde); 
			}
		}

		printAllOnConsole(kontenUnsorted, stlye);

	}

	/**
	 * private generische Methode zur hübschen Konsolenausgabe eines ganzen Sets inkl. Styleparameter
	 * 
	 * @param set
	 * 			das Set, das ausgeben werden soll
	 * @param style
	 * 			Styleparameter, dass den Detailgrad der ausgabe bestimmt
	 * @see getAllClientsSorted, getAllClients
	 */
	private <T extends Kunde> void printAllOnConsole(Set<T> set, Style style) { // generische Methode zur hübschen Konsolenausgabe eines ganzen Sets inkl. Styleparameter

		for (T element : set) {

			switch (style) {
			case COMPACT:
				System.out.println("Kundennummer " + element.getKundennummer() + ": " + element.getName());
				break;
			case NORMAL:
				System.out.println("Kundennummer " + element.getKundennummer() + ":\n" + element.getName() + "\n"
						+ element.getAdresse().toStringCompact());
				break;
			case DETAILED:
				System.out.println(element);
				break;
			default:
				break;
			}
		}

	}

	/**
	 * private generische Methode zur hübschen Konsolenausgabe einer ganzen Map inkl. Styleparameter
	 * 
	 * @param set
	 * 			Die Map, die ausgeben werden soll
	 * @param style
	 * 			Styleparameter, dass den Detailgrad der ausgabe bestimmt
	 * @see getAllAccounts
	 */
	private <K, V extends Kunde> void printAllOnConsole(Map<K, V> map, Style style) {
		for (Map.Entry<K, V> entry : map.entrySet()) {

			switch (style) {
			case COMPACT:
				System.out.println(entry.getKey());
				break;
			case NORMAL:
				System.out.println(entry.getKey() + " Registriert auf: " + entry.getValue().getName());
				break;
			case DETAILED:
				System.out.println(entry.getKey() + "\nRegistriert auf: " + entry.getValue());
				break;
			default:
				break;
			}
		}
	}

}