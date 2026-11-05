# History – Zusammenfassung der benutzten Prompts

Generierte Zusammenfassung der Prompts, die zum Erarbeiten dieser Lösung benutzt wurden.

## Arbeitswoche 1 (KW 39/2026)

| # | Prompt (sinngemäss) | Rolle der KI | Resultat |
|---|---------------------|--------------|----------|
| 1 | «Lies das Assignment (`TextadventureAssignment_260923-a6547c5c.html`) und fasse mir die Aufgabe zusammen.» | Lernen | Verständnis der Aufgabe: Zork-artige Engine, Jackson/YAML, MVC, Java 25, Gradle. |
| 2 | «Konvertiere das Assignment-HTML in ein Markdown mit nur dem Inhalt, damit der Kontext nicht überläuft.» | Beispiel erhalten | `TextadventureAssignment.md` als schlanke Referenz inkl. aufgearbeitetem `tutorial.yaml`. |
| 3 | «Scaffold das Projekt: Gradle-Build, Wrapper, Paketstruktur nach MVC, `tutorial.yaml` als Resource, Grundgerüst Model/View/Controller mit Jackson-Deserialisierung.» | Beispiel erhalten | Projektgerüst: `build.gradle`, Wrapper, `model`-Records, `StoryLoader`, `GameEngine`, `ConsoleView`, `Main`. |
| 4 | «Erstelle `AGENTS.md` mit den persönlichen Regeln für den Agenten in diesem Projekt.» | Kontrolle | `AGENTS.md` mit Setup-, Konventions- und Architektur-Regeln. |
| 5 | «Fixe die Lint-Fehler in `GameEngine.java` und schliesse das Assignment-HTML vom SonarQube-Check aus.» | Kontrolle | Unbenutzte Imports entfernt; Exclusions konfiguriert. |
| 6 | «Warum kommen beim Gradle-Build Warnungen zu restricted methods, und wie werden sie behoben?» | Lernen / Fehleranalyse | Erklärung: JEP 472 auf Java 25 mit Gradle 9.1; Behoben durch Update auf Gradle 9.7.1 und zweimaliges Ausführen des `wrapper`-Tasks. |

## Arbeitswoche 2 (KW 40–43/2026, Nacharbeiten)

| # | Prompt (sinngemäss) | Rolle der KI | Resultat |
|---|---------------------|--------------|----------|
| 7 | «Planung: Was fehlt bis zur Abgabe am 5. November und in welcher Reihenfolge arbeiten?» | Reflexion | Plan mit Unit-Tests → Zusatzfeature (Save/Load) → eigene Story → `Selbstevaluation.md`. |
| 8 | «Erstelle die Änderungen für den Commit dieser Woche.» | Beispiel erhalten | JUnit-6-Testabhängigkeit, `ConsoleView` mit austauschbarem Output-Stream, 17 Unit-Tests für `GameEngine` und `StoryLoader`. |

## Arbeitswoche 4 (KW 44/2026)

| # | Prompt (sinngemäss) | Rolle der KI | Resultat |
|---|---------------------|--------------|----------|
| 9 | «Es ist die nächste Woche, arbeite am nächsten Commit.» (Plan: Zusatzfeature Save/Load) | Beispiel erhalten | `SaveGame`-Record, `SaveGameManager` mit Jackson-YAML-Persistenz und Slot-Validierung, `save`/`load` als eingebaute Befehle, 5 neue Tests; End-to-End im Spiel verifiziert. |

## Abgabewoche (KW 45/2026)

| # | Prompt (sinngemäss) | Rolle der KI | Resultat |
|---|---------------------|--------------|----------|
| 10 | «Es ist Abgabetag, finalisiere alles. Die eigene Geschichte soll zu Dragon Ball sein — als klassisches Textadventure.» | Beispiel erhalten / Ideen entwickeln | `dragonball.yaml`: «The Seven Signals» — klassische Jagd nach den sieben Dragon Balls à la Zork/Colossal Cave mit 20 Räumen und freier Erkundung ab Bulmas Lager (vier Himmelsrichtungen). Ein Rätsel pro Kugel (Boss Rabbit, Schildkröte/Flying Nimbus, Kamehameha beim Turtle Hermit, Yamchas Canyon, ewige Flamme, Lüftungsgitter in Pilafs Schatzkammer), Finale in der Halle der sieben Sterne, neues globales Verb `summon`. Keine Sackgassen: jede Sperre verweist auf den Lösungsweg, jeder Raum hat einen Rückzug. Story-Auswahl via Programmargument oder VS Code Launch-Konfig, `tutorial.yaml` bleibt Standard; 11 Walkthrough-Tests; README.md ergänzt. |

*(Diese Tabelle wird laufend ergänzt; die Prompts sind sinngemäss zusammengefasst.)*