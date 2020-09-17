[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.idi.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2020/gr2010/gr2010) 

# Gazelle

![Gazelle logo](/assets/logo.svg)

Dette er prosjektet til gruppe 10 i faget IT1901 høsten 2020.
For en komplett beskrivelse av prosjektet se [her](gazelleFX/README.md).

## Organisering
I mappen `gazelleFX/` ligger en standard trelagsapplikasjon med brukergrensesnitt
i JavaFX, et modellag og persistens til/fra JSON-filer.

#### Koden ligger i: 
 - `src/gazelle/ui` for app og kontroller klasser.
 - `src/gazelle/model` for datastrukturene i applikasjonen.
 - `src/gazelle/persinstance` for lagring og lesing til og fra fil.
 
#### FXML ligger i:
 - `res/scenes` for FXML scenes. 
 
#### Tester ligger i:
 - `test/gazelle/model` for tester til datastrukturene.
 - `test/gazelle/ui` for tester til kontroller og FXML.

## Bygging og testing
Vi bruker maven som byggverktøy, med to `pom.xml`-filer.

For å kompilere, teste og sjekke kodekvalitet skriv: 
```
mvn verify
```
For å kjøre, gå inn i `gazelleFX/` og skriv:
```
mvn javafx:run
``` 
Merk at `JAVA_HOME` må være satt til en installasjon av Java 14.

## Bidrag og utvikling
Se [CONTRIBUTING.md](/CONTRIBUTING.md).