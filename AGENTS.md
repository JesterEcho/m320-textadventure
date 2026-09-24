# AGENTS.md – Regeln für den Agenten

Persönlich gesammelte Regeln für den KI-Agenten im Projekt m320-textadventure.

## Projekt-Setup

- Buildsystem ist Gradle (Wrapper `gradlew.bat` / `gradlew`), Java-Toolchain 25.
- Jackson (`jackson-dataformat-yaml`) ist die einzige erlaubte Library.
- Einstiegspunkt ist `ch.bbw.m320.textadventure.Main` (`application`-Plugin).

## Code-Konventionen

- Code ist vollständig auf Englisch (Klassen, Methoden, Variablen, Kommentare).
- Dokumentationskommentare (Javadoc) auf Englisch, prägnant.
- `records` für alle unveränderlichen Datenklassen (Story, Room, Action, GlobalVerb).
- `var` verwenden, wo der Typ auf der rechten Seite klar ersichtlich ist.
- Keine SonarLint-Warnungen: kleine Methoden, eine Sache pro Methode, keine leeren
  catch-Blöcke, keine toten Parameter.
- Keine weiteren Kommentare im Code, ausser wenn sie zwingend nötig sind.

## Architektur

- Trennung nach MVC:
  - `model` – Java-Records für das YAML-Format (`Story`, `Room`, `Action`, `GlobalVerb`)
    sowie der veränderliche Spielzustand (`GameState`).
  - `view` – `ConsoleView` für Ausgabe und Eingabe von der Konsole.
  - Controller – `GameEngine` (Aktionslogik, Synonyme, Fehlermeldungen) und `Main` (Main-Loop).
- Die Engine kennt die Konsole nicht; Ein-/Ausgabe läuft ausschliesslich über `ConsoleView`.
- Das YAML-Format aus `TextadventureAssignment.md` wird nicht angepasst;
  `src/main/resources/tutorial.yaml` bleibt 1:1 gemäss Vorgabe und muss immer funktionieren.

## YAML-Format (nicht ändern!)

- `startRoom`, `description`, `rooms: Map<String, Room>`, `globals`.
- Room: `name`, `description`, `verbs: Map<Verb, Map<Objekt, List<Action>>>`.
- Action: nur die **erste zutreffende** Aktion der Liste wird ausgeführt;
  `ifState` prüft, `addState` setzt, `room` wechselt den Raum, `message` gibt Text aus.
- `globals`: Synonyme pro Verb, `unknownVerb` / `unknownObject` (mit `{}`-Platzhalter),
  Fallback unter `default`.

## Arbeitsweise

- Pro Arbeitswoche mindestens ein Commit.
- Commits und Dokumente auf Deutsch (Schweizer Rechtschreibung, kein ß).
- `history.md`: Zusammenfassung der benutzten Prompts (generiert).
- `Selbstevaluation.md`: wird **händisch** geschrieben, mind. 200 Wörter.
- Vor jedem Commit: `./gradlew build` muss ohne Fehler durchlaufen.

## Verifikation

- Build und Tests: `.\gradlew.bat build`
- Spiel starten: `.\gradlew.bat run`