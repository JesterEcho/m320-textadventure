# Selbstevaluation

## Persönliche Wertung des Projektes

Das Projekt hat mir grossen Spass gemacht, gerade weil die Engine von Anfang an
datengetrieben geplant war: Räume, Verben und Aktionen liegen vollständig in
YAML, der Java-Code kennt keine einzige Geschichte. Das hat sich besonders
bezahlt gemacht, als ich meine Dragon-Ball-Story nach dem ersten Entwurf
komplett verworfen und als klassische Schatzsuche neu aufgebaut habe — ohne
eine Zeile Engine-Code anzufassen. Lehrreich war die Semantik der Aktionen
(die erste zutreffende Aktion gewinnt, `ifState`/`addState`): Aus diesen
einfachen Regeln lässt sich die komplette Rätsellogik aufbauen. Aufwändig war
das Feintuning der eigenen Story: Die erste Version hatte Sackgassen, weil
Fortschritt über Power-Gates statt über Gegenstände lief, und Kanon-Details
wollten sorgfältig geprüft sein. Erstaunlich fand ich, wie schnell mit
agentischem Programmieren ein lauffähiges, getestetes Produkt entsteht.

## Meine Wertung von agentischem Programmieren

Bewährt hat sich, der KI feste Regeln mitzugeben (`AGENTS.md`: Konventionen,
Architektur, Verifikation) und in Fachbegriffen zu prompten. Die Arbeit
direkt in der IDE — ohne Chat-Copy-Paste — macht Iterationen sehr kurz.
Stark ist die KI beim Gerüst, bei Umbauten und bei mechanischen
Test-Suiten; schwächer war sie bei inhaltlicher Logik: Die Sackgassen und
Kanon-Widersprüche der ersten Story zeigten sich erst beim eigenen Spielen.
Wichtig war mir deshalb, jede Lösung selbst zu verifizieren (`gradlew
build`, Geschichte live durchspielen) und das Feedback gezielt in die
nächste Prompt-Runde einfliessen zu lassen.

## Zusatzfeatures im Produkt

Sichtbares Zusatzfeature ist das Speichersystem: `save`/`load` mit Slots,
serialisiert via Jackson als YAML, über Neustarts hinweg verifiziert.
Dazu kommen die freie Story-Auswahl per Programmargument respektive VS
Code Launch-Konfiguration und die eigene Geschichte «The Seven Signals»
mit sieben Rätseln, der Halle der sieben Sterne als Finale und dem neuen
globalen Verb `summon`. Besonders stolz bin ich auf das Sackgassen-freie
Design: Jede Sperre nennt ihren Lösungsweg, jeder Raum hat einen Rückzug.

## Vorgehen für die geforderten Kompetenzen

Ich habe pro Arbeitswoche mindestens einen Commit mit vorher grünem Build
gemacht, Unit-Tests (33 Stück) für Engine, Loader, Speicherung und beide
Geschichten geschrieben und die Rollen der KI bewusst gewählt: Lernen beim
Format-Verständnis, Beispiel erhalten beim Gerüst, Kontrolle und
Fehleranalyse bei Build- und Logikproblemen, Reflexion beim Story-Redesign.
