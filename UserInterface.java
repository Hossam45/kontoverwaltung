
package kontoverwaltung;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Diese Klasse enthält die ausführbare main-Methode und dient zur Nutzerinteraktion über die Konsole mit einem Bank-Objekt.
 * Dieses Bankobjekt enthält die Daten der Kunden und ihrer Konten sowie Instanzmethoden um diese Daten abzufragen oder anzulegen.
 * Das Bankobjekt und alle Kundendaten wird aus einer lokalen Datei geladen, sofern vorhanden. Existiert diese Datei
 * nicht, wird eine neue vorgefertigte Bank automatisch angelegt. (hier wäre eventuell noch eine Nutzerinteraktion fein, 
 * aber ist noch nicht implementiert) 
 * Diese Klasse enthält selbst keine Methoden für den Benutzer, nur private Methoden zur Dateneingabe und -prüfung.
 * Genaueres ist im Quellcode in Blockkommentaren erläutert.
 *  
 * @author aschwegmann
 * @version 1.0
 *
 * @see Bank
 * @see Kunde
 * @see Privatkunde
 * @see Firmenkunde
 * @see Konto
 * @see Ansprechpartner
 * @see Adresse 
 */
public abstract class UserInterface {

	// Enum zur späteren Auswahl von Anzeige-Styles
	enum Style {
		COMPACT, NORMAL, DETAILED
	}
	
	// Patterns zur Überprüfung korrekter Nutzereingaben
	final static Pattern VALID_NAME = Pattern.compile("^([\\p{Alpha}äöüÄÖÜß\\.-]{2,}\\s+){1,}([\\p{Alpha}äöüÄÖÜß\\.-]{2,})$");	// Gültige zeichen für Namen?? Einschränken oder nicht?
	final static Pattern VALID_EMAIL = Pattern.compile("^[A-Z0-9\\._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	final static Pattern VALID_IBAN = Pattern.compile("^[\\p{Alpha}äöüÄÖÜß]{2}\\d{2}\\s?(\\w{4}\\s?){4}\\w{2}$");
	final static Pattern VALID_PHONE_NR = Pattern.compile("^(((00|\\+)[1-9]\\d{5})|(0[1-9]\\d{3}))(/| )?\\d{2,}$");
	final static Pattern VALID_DATE = Pattern.compile("^(0?[1-9]|[12][0-9]|3[01])\\.(0?[1-9]|1[012])\\.\\d{4}$");
	final static Pattern VALID_STR_HNR = Pattern.compile("^([\\p{Alpha}äöüÄÖÜß]\\.?{2,}(\\s|\\-)?)+\\d+[\\p{Alpha}]?$");
	final static Pattern VALID_KNR = Pattern.compile("^\\d{9,}$");
	final static Pattern VALID_PLZ = Pattern.compile("^\\d{5}$");
	
	/**
	 *  Das "bank"-Objekt, dass sämtliche Daten speichert
	 */
	static Bank bank = null;

	
	
	public static void main(String[] args) {

		// Einige Scanner und Attribute, mit denen das Programm arbeitet
		Scanner userChoiceInput = new Scanner(System.in);
		Scanner userLineInput = new Scanner(System.in);
		userLineInput.useDelimiter("\\n");
		Scanner userTokenInput = new Scanner(System.in);
		boolean exit = false;
		boolean abort = false;
		ObjectInputStream ois = null;
		String kundennummer = "";
		String iban = "";
				
		/*
		 * An dieser Stelle wird versucht die lokale Datenbank, welche sich im workspace als clientDatabase.dat
		 * befindet zu laden. Wenn dies erfolgreich ist, wird der Nutzer begrüßt. Ansonsten wird eine
		 * Standardbank ohne Kunden erstellt. (hier könnte man noch eine Nutzerinteraktion zur Anlage einer neuen Bank
		 * implementieren.
		 */
		
		System.out.println("Lade... bitte warten.");

		try {
			ois = new ObjectInputStream(new FileInputStream("clientDatabase.dat"));
			while (true) {
				try {
					bank = (Bank) (ois.readObject());
				} catch (EOFException eof) {
					break;
				}
			}
			System.out.println("Laden komplett! Herzlich willkommen!");
		} catch (FileNotFoundException e) {

			System.out.println("Datenbank nicht gefunden. Erzeuge neue leere Datenbank.");

			// TODO Hier könnte man einen schönen Begrüßungstext hinschreiben,
			// in der die Bank ihren Namen, und Daten angeben kann. Quasi eine
			// Erstinitialisierung anstelle von folgender zeile >>
			bank = new Bank("Geldgeier eV", "GEGEIER1CLV",new Adresse("Hauptstrasse 58", null, 55555, "Kleinhenneborn"));  // erzeugt ein neues Bank-Objekt

			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Klasse nicht gefunden!");
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
			}
		}

		
		
		/*
		 * Im Folgenden wird eine Endlosschleife ausgeführt, die den Nutzer immer wieder zum Hauptmenü zurückführt, 
		 * solange dieser nicht das Programm explizit beendet. Die einzelnen Menüpunkte werden dann über ein switch-
		 * case-Konstrukt behandelt. Details zu den Menüpunkten bei dem jeweiligen case. 
		 */
		
		while (!exit) {   //Menüschleife start

			/*
			 * Eine Besonderheit ist, dass zur komfortablen Dateneingabe, diese in einem String-Array gesammelt werden.
			 * Daher muss die Reihenfolge deingabe nicht mit der Reihenfolge im Konstruktor übereinstimmen und
			 * es kann eine Intuitivere Reihenfolge abgefragt werden. Der Sammelnde Array ist data.r Datene 
			 */
			String[] data;
			int choice = 0;
//			while (userChoiceInput.hasNext()){ // flush vom Scanner, damit keine reste drinbleiben und zu fehlern führen
//				userChoiceInput.next(); // Just discard this, not interested...
//			}

			System.out.printf("%nHAUPTMENUE:%n%n" + "(01): Neuen Privatkunden anlegen%n"
					+ "(02): Neuen Firmenkunden anlegen%n" + "(03): Konto anlegen und Kundennummer zuordnen%n"
					+ "(04): Kunde mit Konten anzeigen (Auswahl durch Kundennummer)%n"
					+ "(05): Kunde mit Konten anzeigen (Auswahl durch Name)%n"
					+ "(06): Konto anzeigen (Auswahl durch IBAN)%n"
					+ "(07): Datenbank einsehen (Alle Kunden unsortiert anzeigen)%n"
					+ "(08): Datenbank einsehen (Alle Kunden sortiert nach aufsteigender Kundenummer anzeigen)%n"
					+ "(09): Datenbank einsehen (Alle Konten unsortiert anzeigen)%n"
					+ "(10): Speichern und/oder Programm beenden%n%nWarte auf Eingabe >> ");

			try {
				choice = userChoiceInput.nextInt();
			} catch (InputMismatchException e) {
				userChoiceInput.nextLine(); // Just discard this whole mess, not interested...
			} // löst weiter unten eine warnung aus, da in diesem Fall choice auf 0 bleibt.

		
			switch (choice) { 	// Auswahl-Switch
			
				/*
				 * (01) Privatkunde anlegen
				 * Da sich einige der Eingabedaten mit denen eines Firmenkunden überschneiden, wurde die Dateneingabe 
				 * in eigene Methoden ausgelagert, und die gemeinsamen Daten werden durch eine einzige Methode 
				 * "AddCommonClientData" gesammelt. 
				 * Auf diese Weise kann auch (für den Nutzer angenehmer) zuerst  der Name durch "AddPrivateClientName", 
				 * dann die gemeinsamen Daten, dann die Sonderdaten (hier nur der Geburtstag) durch 
				 * "AddAdditionalPrivateClientData" gesammelt werden. 
				 * Alle Methoden schreiben die Informationen, die der Nutzer über die Konsole eingeben muss
				 * in den Data-Array, der 12 Felder eindeutig belegte Felder besitzt.
				 * Die Daten werden dann an die Bank-Methode addPrivateClient übergeben, und gegebenenfalls in passende
				 * Datentypen geparst/umgewandelt.
				 * 
				 */
			case 1:	
					
				data = AddAdditionalPrivateClientData(AddCommonClientData(AddPrivateClientName(new String[12])));

				if (data == null) {
					System.out.println("Vorgang wurde abgebrochen");
					break;
				}

				System.out.println(bank.addPrivateClient(data[0], data[1], data[2], data[3], data[4],
						Integer.parseInt(data[5]), data[6], data[7], data[8],
						new GregorianCalendar(Integer.parseInt(data[9].substring(6, 10)),
								Integer.parseInt(data[9].substring(3, 5)) - 1,
								Integer.parseInt(data[9].substring(0, 2)))) ? "Vorgang erfolgreich"
										: "Fehler bei Vorgang");

				System.out.println("Anzahl an Kunden jetzt " + bank.getKundenZahl());
				waitForEnter();
				break;

				/*
				 * (02) Firmenkunde anlegen
				 * Die Funktion ist komplementär zu Menüpunkt 01.
				 * 
				 */
			case 2:

				data = AddAdditionalBusinessClientData(AddCommonClientData(AddBusinessClientName(new String[12])));

				if (data == null) {
					System.out.println("Vorgang wurde abgebrochen");
					break;
				}

				System.out.println(bank.addBusinessClient(data[0], data[2], data[3], data[4], Integer.parseInt(data[5]),
						data[6], data[7], data[8], data[9], data[10], data[11]) ? "Vorgang erfolgreich"
								: "Fehler bei Vorgang");

				System.out.println("Anzahl an Kunden jetzt " + bank.getKundenZahl());
				waitForEnter();
				break;

				
				/*
				 * (03) Konto anlegen und Kundennummer zuordnen
				 * Da es keine Gemeinsamkeiten mit anderen Menüpunkten gibt, wurde die logik nicht ausgelagert,
				 * Im grunde werden nacheinander Kundennummer, IBAN und Startkapital abgefragt,
				 * durch Matcher geprüft und anschliessend an bank.addAccount weitergeleitet.
				 * 
				 * Die Prüfung durch Matcher an dieser Stelle ist übrigens dafür da, damit das Nutzerinterface die
				 * Überprüfung von Fehlern übernimmt und die Methoden in Bank sichere Eingaben erhalten. Eventuelle
				 * Fehler können dann so behandelt werden, dass das Programm nicht terminiert.
				 * 
				 * Fehler werden als System.err auf die Konsole ausgegeben. Zusätzlich wird über  aTinyDelay();
				 * nach jeder Fehlerausgabe 10 millisekunden verzögert um die Konsolenausgabe zu synchronisieren.
				 */
			case 3: 

				double startkapital = 0;
				abort = false;

				while (true && !abort) {
					
					System.out.print("Kundennummer des Klienten, für den ein Konto angelegt werden soll (leer lassen um den Vorgang abzubrechen):\n   > ");

					kundennummer = userLineInput.next().trim().replace(" ", "");
					
					Matcher m = VALID_KNR.matcher(kundennummer);

					if (m.find()) {
						if (!bank.getClientByKdnr(kundennummer, false)) {
							System.err.println("Es existiert kein Kunde mit der Kundennummer "+ kundennummer); aTinyDelay();
						} else if (bank.getClientAccountNumByKdnr(kundennummer) >= 10) {
							System.err.println("Der Kunde hat bereits die maximale Kontenzahl erreicht. (10)"); aTinyDelay();
						} else {							
							break; // exit loop
						}

					} else if (kundennummer.length() == 0) {
						abort = true;
						break;
					} else {
						System.err.println("Ungültiges Format! Eine Kundennummer hat mindestens 9 Stellen."); aTinyDelay();
					}
					
				}

				while (true && !abort) {
					
					System.out.print("IBAN des neuen Kontos:\n   > ");

					iban = createFormattedIban(userLineInput.next());
					if (iban!=null) break;
					System.err.println("Fehlerhaftes IBAN Format!"); aTinyDelay();
					
				}
				
				while (true && !abort) {

					System.out.print("Startkapital in lokaler Währung ohne Währungszeichen:\n   > ");

					try {
						startkapital = userTokenInput.nextDouble();
						break;
					} catch (InputMismatchException e) {
						System.err.println("Fehlerhafte Eingabe. Bitte ohne Währungszeichen oder dergleichen eingeben"); aTinyDelay();
					}
					
				}

				if (!abort) {
					System.out.println(bank.addAccount(kundennummer, iban, startkapital) ? "Vorgang erfolgreich" : "Fehler bei Vorgang"); aTinyDelay();
				}
				
				waitForEnter();
				break;

				/*
				 * (04) Kunde mit Konten anzeigen (Auswahl durch Kundennummer)
				 * Die Nutzereingabe einer Kundennummer, welche durch einen Matcher geprüft wird und
				 * dann an bank.getClientByKdnr weitergeleitet wird
				 */
			case 4:

				while (true) {

					System.out.printf("Kundennummer:  > ");

					kundennummer = userLineInput.next().trim().replace(" ", "");

					Matcher m = VALID_KNR.matcher(kundennummer);

					if (m.find()) {
						if(!bank.getClientByKdnr(kundennummer)){
							System.err.println("Kein Kunde mit dieser Kundennummer in der Datenbank."); aTinyDelay();
						}
						break;
					} else {
						System.err.println("Fehlerhaftes Format!");	aTinyDelay();
					}
					
				}
				waitForEnter();
				break;

				/*
				 * (05) Kunde mit Konten anzeigen (Auswahl durch Name)
				 * Im Grunde nur eine Weiterleitung an bank.getClientByName, welche den Kunden ausgibt.
				 * Praktischerweise kann der boolean Rückgabewert der Methode auch verwendet werden um
				 * das Vorhandensein eines Kunden zu testen.
				 */
			case 5:

				System.out.printf("Vollständiger Name des Kunden:  > ");

				if(!bank.getClientByName(userLineInput.next())){
					System.err.println("Kein Kunde dieses Namens in der Datenbank."); aTinyDelay();
				}
						
				waitForEnter();
				break;

				/*
				 * (06) Konto anzeigen (Auswahl durch IBAN)
				 * Die Nutzereingabe einer IBAN, diese wird dann an die Methode createFormattedIban
				 * weitergeleitet, die die Eingabe prüft und dann nach DIN 5008 umformatiert.
				 * War die Eingabe keine gültige IBAN, gibt die Methode NULL zurück und eine Fehlermeldung
				 * wird ausgegeben. War die IBAN gültig wird sie formatiert an bank.getAccount() weiter-
				 * geleitet.
				 */
			case 6:

				while (true) {

					System.out.printf("IBAN:  > ");

					iban = createFormattedIban(userLineInput.next());
					if (iban!=null) {
						bank.getAccount(iban); 
						break;
						}
					System.err.println("Fehlerhaftes IBAN Format!"); aTinyDelay();
				}
				
				waitForEnter();
				break;

				/*
				 * (07) Alle Kunden unsortiert anzeigen
				 * Im Grunde nur eine zusätzliche Nutzerabfrage "StyleChoice()" in der ein Ausgabedetailgrad abgefragt wird
				 * und dann ein Aufruf der Instanzmethode bank.getAllClients()
				 *  
				 */
			case 7:

				bank.getAllClients(StyleChoice());
				waitForEnter();
				break;

				/*
				 * (08) Alle Kunden sortiert nach aufsteigender Kundenummer anzeigen
				 * Im Grunde nur eine zusätzliche Nutzerabfrage "StyleChoice()" in der ein Ausgabedetailgrad abgefragt wird
				 * und dann ein Aufruf der Instanzmethode bank.getAllClientsSorted()
				 * 
				 */
			case 8:

				bank.getAllClientsSorted(StyleChoice());
				waitForEnter();
				break;

				/*
				 * (09) Alle Konten unsortiert anzeigen
				 * Im Grunde nur eine zusätzliche Nutzerabfrage "StyleChoice()" in der ein Ausgabedetailgrad abgefragt wird
				 * und dann ein Aufruf der Instanzmethode bank.getAllAccounts()
				 * 
				 */
			case 9:
				
				bank.getAllAccounts(StyleChoice());
				waitForEnter();
				break;

				/*
				 * (10) Beenden
				 * Ruft das Beenden-Untermenü auf:
				 * 		1) Save and Continue	Serialisiert das Bankobjekt und kehrt zum Hauptmenü zurück
 				 *      2) Save and Exit		Serialisiert das Bankobjekt und beendet über ein Flag die Endlosschleife
 				 *      3) Exit without Save	Nach einer weiteren Sicherheitsabfrage wird Beendet ohne das Bank-Objekt zu speichern
 				 *      4) Cancel				Kehrt ohne Aktion ins Hauptmenü zurück
				 * 		
				 */
			case 10:

				int selection = 0;

				while (selection < 1 || selection > 4) {

					System.out.printf(
							"%n  1) Save and Continue%n  2) Save and Exit%n  3) Exit without Save%n  4) Cancel%n > ");
					try {
						selection = userChoiceInput.nextInt();
					} catch (InputMismatchException e) {
						System.err.println("Ungültige Eingabe: " + selection); aTinyDelay();
						continue;
					} catch (Exception e) {
						System.err.println("Unerwarteter Fehler"); aTinyDelay();
						continue;
					}
				}

				ObjectOutputStream oos = null;
				
				switch (selection) {
				case 1:
					try {
						oos = new ObjectOutputStream(new FileOutputStream("clientDatabase.dat"));
						oos.writeObject(bank);
						System.out.println("Eingaben wurden gespeichert.");
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							oos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					exit = false;
					break;
				case 2:
					try {
						oos = new ObjectOutputStream(new FileOutputStream("clientDatabase.dat"));
						oos.writeObject(bank);
						System.out.println("Eingaben wurden gespeichert.");
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							oos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					exit = true;
					break;
				case 3:
					while (true) {
						System.out.printf("%nAre you sure? All changed Data will be lost! %n 1) Yes. Exit without Save%n 2) Cancel Exit ");
						try {
							choice = userChoiceInput.nextInt();
						} catch (InputMismatchException e) {
							System.err.println("Ungültige Eingabe: " + choice); aTinyDelay();
							continue;
						} catch (Exception e) {
							System.err.println("Unerwarteter Fehler"); aTinyDelay();
							continue;
						}
						if (choice >= 1 && choice < 3)
							break;
					}
					switch (choice) {
					case 1:
						exit = true;
						break;
					case 2:
						break;
					default:
						break;
					}
					break;
				case 4:
					exit = false;
					break;
				default:
					break;
				}
				try {
					oos.close();
				} catch (Exception e) {	} // soll es einfach nur versuchen und nicht abstürzen falls oos nicht existiert
				break;

			default:
				System.err.println("Fehlerhafte Eingabe."); aTinyDelay();
				break;
			}

		}

		System.out.println("Danke für die Nutzung dieses Systems\nAuf Wiedersehen!");

		userChoiceInput.close();
		userLineInput.close();
		userTokenInput.close();

	}

	/**
	 * Nutzerabfrage zum Detailgrad der Konsolenausgabe
	 * @return
	 * 		Ein Style-enum, dass den Detailgrad beschreibt
	 */
	private static Style StyleChoice() {
		int choice = 0;
		Scanner scScanner;

		while (choice < 1 || choice > 3) {

			System.out.printf("Anzeigeformat:%n  1) Kompakt%n  2) Normal%n  3) Detailliert%n > ");
			try {
				scScanner = new Scanner(System.in);
				choice = scScanner.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Ungültige Eingabe: " + choice); aTinyDelay();
				continue;
			} catch (Exception e) {
				System.err.println("Unerwarteter Fehler"); aTinyDelay();
				continue;
			} finally {
				if (choice < 1 || choice > 3) {
					System.err.println("Fehlerhafte Eingabe!"); aTinyDelay();
				}
			}
		}
		
		switch (choice) {
		case 1: return Style.COMPACT;
		case 2: return Style.NORMAL;
		case 3: return Style.DETAILED;
		default:
			assert false;
			return null;
		}
		
		
	}

	/**
	 * Konsoleneingabe und Matcher-Prüfung von Daten, die Privat- und Firmenkunde gemein haben.
	 * 
	 * @param sammelArray
	 * 			Braucht ein 12-Felder Stringarray, in das die Daten eingefügt werden
	 * @return
	 * 			Jenes Stringarray mit den Nutzereingaben in den passenden Feldern
	 */
	private static String[] AddCommonClientData(String[] sammelArray) {

		Scanner commonDataScanner = new Scanner(System.in);
		commonDataScanner.useDelimiter("\\n");
		String tempString;
		String[] tempArray;
		Matcher m;

		tempString = (sammelArray[1] == null) ? sammelArray[2] : (sammelArray[1] + " " + sammelArray[2]);

		if (bank.getClientByName(tempString)) {

			System.out.println();
			System.out.println(
					"Ein oder mehere Klient(en) dieses Namens befindet sich bereits in der Datenbank.\nStimmen die Daten überein? (y/n)");

			nutzerabfrage: while (true) {
				switch (commonDataScanner.next().toUpperCase().charAt(0)) {
				case 'Y':
					System.out.println("Anlegen eines neuen Klienten wird abgebrochen...");
					return null;
				case 'N':
					break nutzerabfrage;
				default:
					break;
				}
			}

		}

		sammelArray[0] = Integer.toString(bank.getKundenZahl() + 100000000); // generiert eine Mindestens 9-stellige Kundennummer

		for (int i = 0; i < 5; i++) {

			endlosschleife: while (true) {

				switch (i) {
				case 0:
					System.out.print("Strasse Hausnummer:\t   > ");
					
					tempArray = commonDataScanner.next().trim().split("\\s+");

					if (tempArray.length < 2) {
						System.err.println("Ungültige Adresse! Muss aus Strassenname und Hausnummer bestehen!"); aTinyDelay();
						break;
					}

					StringBuilder tmp = new StringBuilder();
					for (int j = 0; j < tempArray.length; j++) {
						tmp.append(tempArray[j] + " ");
					}
					String strasseHausnummer = tmp.toString().trim();

					m = VALID_STR_HNR.matcher(strasseHausnummer); // prüfe name und hausnummer

					if (!m.find()) {
						System.err.println("Ungültige Adresse! Schreibweise überprüfen."); aTinyDelay();
						break;
					}
					sammelArray[3] = strasseHausnummer;
					break endlosschleife;

				case 1:
					System.out.print("2. Adresszeile (optional): > ");

					tempString = commonDataScanner.next().trim();

					sammelArray[4] = tempString;

					break endlosschleife;

				case 2:

					System.out.print("PLZ Wohnort:\t\t   > ");
					
					tempArray = commonDataScanner.next().trim().split("\\s+");

					m = VALID_PLZ.matcher(tempArray[0]);

					// TODO überprüfung ob plz existiert und mit wohnort zusammenpasst

					if (tempArray.length < 2 || !m.find()) {
						System.err.println("Ungültige Eingabe! Muss aus PLZ und Wohntort bestehen, und die PLZ muss in der Form 12345 vorliegen!"); aTinyDelay();
						break;
					}

					StringBuilder wohnort = new StringBuilder();
					
					for (int j = 1; j < tempArray.length; j++) {
						wohnort.append(tempArray[j] + " ");
					}
					
					if (wohnort.length() < 2) {
						System.err.println("Ungültige Eingabe! Wohnort nicht angegeben!"); aTinyDelay();
						break;
					}

					sammelArray[5] = tempArray[0];
					sammelArray[6] = wohnort.toString().trim();
					break endlosschleife;

				case 3:

					sammelArray[7] = getPhoneNumberFromConsole();
					break endlosschleife;

				case 4:

					System.out.print("E-Mail:\t\t\t   > ");

					tempString = commonDataScanner.next().trim();

					m = VALID_EMAIL.matcher(tempString);

					if (!m.find()) {
						System.err.println("Ungültige E-Mail Adresse! \"" + tempString + "\""); aTinyDelay();
						break; // nur den switch
					}

					sammelArray[8] = tempString;
					break endlosschleife;

				default:
					assert false : "unexpected switch case";
					break endlosschleife;
				}
			} // end of inf-loop

		} // end of for-iteration

		return sammelArray;
	}

	/**
	 * Konsoleneingabe und Matcher-Prüfung vom Namen eines neuen Privatkunden.
	 * 
	 * @param sammelArray
	 * 			Braucht ein 12-Felder Stringarray, in das die Daten eingefügt werden
	 * @return
	 * 			Jenes Stringarray mit den Nutzereingaben in den passenden Feldern
	 */
	private static String[] AddPrivateClientName(String[] sammelArray) {

		System.out.println("Bitte Daten des neuen Privatklienten eingeben:");

		String[] temp = getFullNameFromConsole();
		sammelArray[1] = temp[0];
		sammelArray[2] = temp[1];

		return sammelArray;

		// EINFÜGEN IN Sammelarray [ kundennummer, vorname, nachname, adressZeile1, adressZeile2, plz, wohnort, telefonnummer, email, geburtstag, XXXX, XXXX ]
	}

	/**
	 * Konsoleneingabe und Matcher-Prüfung vom Firmennamen eines neuen Firmenkunden.
	 * 
	 * @param sammelArray
	 * 			Braucht ein 12-Felder Stringarray, in das die Daten eingefügt werden
	 * @return
	 * 			Jenes Stringarray mit den Nutzereingaben in den passenden Feldern
	 */
	private static String[] AddBusinessClientName(String[] sammelArray) {

		System.out.println("Bitte Daten des neuen Firmenklienten eingeben:");

		Scanner bcNameScanner = new Scanner(System.in).useDelimiter("\\n");

		while (true) {

			System.out.print("Firmenname:\t\t   > ");

			String temp = bcNameScanner.next();

			if (temp.trim().length() < 1) {
				System.err.println("Ungültiger Name! " + temp); aTinyDelay();
				continue;
			}

			sammelArray[2] = temp.trim();
			return sammelArray;
		}

		// Einfügen in Sammelarray [kundennummer, XXXXX, firmenname, adressZeile1, adressZeile2, plz, wohnort, telefonnummer, email, apVorname, apNachname, apTelefonnummer ]
	}

	/**
	 * Konsoleneingabe und Matcher-Prüfung den letzten fehlenden Daten eines neuen Privatkunden: Geburtstag
	 * 
	 * @param sammelArray
	 * 			Braucht ein 12-Felder Stringarray, in das die Daten eingefügt werden
	 * @return
	 * 			Jenes Stringarray mit den Nutzereingaben in den passenden Feldern
	 */
	private static String[] AddAdditionalPrivateClientData(String[] sammelArray) {

		if (sammelArray == null)
			return null;

		Scanner pcDataScanner = new Scanner(System.in).useDelimiter("\\n");

		while (true) {
			System.out.print("Geburtstag (DD.MM.YYYY):   > ");

			StringBuilder tmp = new StringBuilder(pcDataScanner.next().trim());

			Matcher m = VALID_DATE.matcher(tmp);

			if (!m.find()) {
				System.err.println("Ungültiges Format! \"" + tmp + "\""); aTinyDelay();
				continue; // nur den switch
			}

			if (tmp.charAt(1) == '.')
				tmp.insert(0, '0');
			if (tmp.charAt(4) == '.')
				tmp.insert(3, '0');

			sammelArray[9] = tmp.toString(); // umwandlung in kalenderobjekt folgt oben
			return sammelArray;

		}
	}

	/**
	 * Konsoleneingabe und Matcher-Prüfung den letzten fehlenden Daten eines neuen Firmenkunden: Daten des Ansprechpartners
	 * 
	 * @param sammelArray
	 * 			Braucht ein 12-Felder Stringarray, in das die Daten eingefügt werden
	 * @return
	 * 			Jenes Stringarray mit den Nutzereingaben in den passenden Feldern
	 */
	private static String[] AddAdditionalBusinessClientData(String[] sammelArray) {

		if (sammelArray == null)
			return null;

		System.out.println("Bitte Daten des Ansprechpartners eingeben:");
		String[] temp = getFullNameFromConsole();
		sammelArray[9] = temp[0];
		sammelArray[10] = temp[1];
		sammelArray[11] = getPhoneNumberFromConsole();
		return sammelArray;

	}
	
	/*
	 * Da Privatkunde sowie Ansprechpartner beide die Eingabe eines Namens erfordern, wurde dies
	 * in diese gemeinsame Methode ausgelagert. (Code sparen) 
	 */
	private static String[] getFullNameFromConsole() {

		Scanner nameScanner = new Scanner(System.in).useDelimiter("\\n");

		while (true) {

			System.out.print("Vorname (Mittelname) Name: > ");

			String tmpName = nameScanner.next().trim();
			String[] tempArray;

			Matcher m = VALID_NAME.matcher(tmpName);

			if (m.find()) {
				tempArray = tmpName.split("\\s+");
			} else {
				System.err.println("Ungültiger Name: " + tmpName); aTinyDelay();
				continue;
			}

			if (tempArray.length < 2) {
				System.err.println("Ungültiger Name"); aTinyDelay();
				continue;
			}

			StringBuilder vorname = new StringBuilder();
			for (int i = 0; i < tempArray.length - 1; i++) {
				vorname.append(tempArray[i] + " ");
			}
			return new String[] { vorname.toString().trim(), tempArray[tempArray.length - 1] };
		}
	}

	/*
	 * Da Privatkunde, Firmenkunde sowie Ansprechpartner beide die Eingabe einer Telefonnummer erfordern, wurde dies
	 * in diese gemeinsame Methode ausgelagert. (Code sparen) 
	 */
	private static String getPhoneNumberFromConsole() {
		Scanner phoneScanner = new Scanner(System.in).useDelimiter("\\n");

		while (true) {

			System.out.print("Telefonnummer:\t\t   > "); 
			
			StringBuilder sb = new StringBuilder(phoneScanner.next().replaceAll("[\\s/]", "").replace("+", "00"));

			Matcher m = VALID_PHONE_NR.matcher(sb);

			if (!m.find()) {
				System.err.println("Ungültige Telefonnummer! \"" + sb + "\""); aTinyDelay();
			} else {

				if (Pattern.compile("^0[1-9]\\d{5,}$").matcher(sb).find()) {
					sb.insert(5, ' ');
				} else if (Pattern.compile("^00[1-9]{3}\\d{5,}$").matcher(sb).find()) {
					sb.insert(8, ' ');
					sb.insert(4, ' ');
				} else {
					assert false : "unexpected phone number format";
				}
				return sb.toString().trim();
			}
		}
	}
	
	/*
	 * Ausgelagerte Methode, um eine Eingegebene IBAN zu prüfen, und in DIN 5008  zu formatieren
	 */
	private static String createFormattedIban(String input){
		
		StringBuilder ibantmp = new StringBuilder(input.trim().replace(" ", "").toUpperCase());
		
		Matcher m = VALID_IBAN.matcher(ibantmp);

		if (m.find()) {
			for (int i = 0; i < 5; i++) {
				ibantmp.insert(4 + i * 5, ' '); // Umformung auf DIN-Form für bessere Lesbarkeit bei der Ausgabe später
			}
			return ibantmp.toString();
		} else {
			return null;
		}
	}
	
	/*
	 * Why a Tiny Delay??? To ensure the correct output od System.err messages on the console. 
	 * They seem to be out of sync otherwise...
	 */
	private static void aTinyDelay(){
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Wartet auf die Betätigung von Enter. Wird oft benutzt, damit die Konsolenausgabe zum lesen bestehenbleibt
	 * und die Rückkehr zum Hauptmenü auf eine Bestätigung wartet.
	 * 
	 */
	private static void waitForEnter(){
		System.out.println("Press Enter to continue...");
		try {
			System.in.read();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
}
