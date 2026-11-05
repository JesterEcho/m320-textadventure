# m320-textadventure

Eine generische Textadventure-Engine (à la Zork) in Java 25: Geschichten werden
als YAML-Dateien geladen und mit Jackson deserialisiert. Entwickelt mit agentischem
Programmieren (KI-IDE-Integration).

## Ausführen

```
.\gradlew.bat run                        # Tutorial-Geschichte (tutorial.yaml)
.\gradlew.bat run --args="dragonball"    # Eigene Geschichte: «The Seven Signals»
```

Speichern/Laden während des Spiels: `save <slot>` und `load <slot>`
(Slots aus Kleinbuchstaben, Ziffern und Bindestrich). Beenden mit `quit`.

## Eigene Geschichte

Die Dragon-Ball-Geschichte (`src/main/resources/dragonball.yaml`) ist ein
klassisches Textadventure im Dragon-Ball-Universum: «The Seven Signals» —
20 Räume, freie Erkundung, sieben Dragon Balls und ein Wunsch beim Ewigen
Drachen.

Sammle alle sieben Kugeln — jede steckt hinter einem eigenen Rätsel:

- Four-Star: Grossvaters Andenken zu Hause auf Mount Paozu
- Two-Star: dem Karotten-Fluch des Boss Rabbit im geplagten Dorf entrissen
- Three-Star: das Andenken des Turtle Hermit — nur für seinen Schüler
  (Kamehameha lernen, Flying Nimbus verdienen)
- Five-Star: aus dem Versteck des Wüstenbanditen Yamcha
- Six-Star: hinter der ewigen Flamme des Fire Mountain (Kamehameha!)
- One-Star: aus Pilafs Schatzkammer, über ein Lüftungsgitter erreichbar,
  von Mai bewacht
- Seven-Star: direkt aus der Hand von Kaiser Pilaf selbst

Die Halle der sieben Sterne prüft im Finale jede Kugel einzeln — fehlt eine,
nennt der abgedunkelte Sockel mit Hinweis, wo sie zu finden ist. Kein
Sackgassen-Design: Jede Sperre verweist auf den Lösungsweg, jeder Raum hat
einen Rückzug.

Die Engine spielt jede Geschichte im Format von `tutorial.yaml` ohne Code-Änderung:
Räume, Verben, bedingte Aktionen (`ifState`/`addState`), Synonyme und globale
Fehlermeldungen stammen komplett aus der YAML-Datei.

## Build und Tests

```
.\gradlew.bat build
.\gradlew.bat test
```

33 Unit-Tests decken Engine-Semantik, YAML-Deserialisierung, Speichersystem und
beide Geschichten ab.

## Architektur (MVC)

- `model` – Records für das YAML-Format (`Story`, `Room`, `Action`, `GlobalVerb`,
  `SaveGame`) und der veränderliche Spielzustand (`GameState`)
- `view` – `ConsoleView` für sämtliche Ein- und Ausgabe
- `controller` – `GameEngine` (Aktionslogik), `StoryLoader`/`SaveGameManager`
  (Persistenz), `Main` (Main-Loop, eingebaute Befehle)

Jackson ist die einzige Runtime-Library; JUnit kommt nur für Tests zum Einsatz.
