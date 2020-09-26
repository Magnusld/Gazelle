[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.idi.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2020/gr2010/gr2010) 

# Gazelle

![Gazelle logo](/assets/logo.svg)

Dette er prosjektet til gruppe 10 i faget IT1901 høsten 2020.
For en komplett beskrivelse av prosjektet se [her](gazelleFX/README.md).

## Organisering
Appen er delt inn i flere moduler: Klient, tjener og delt logikk.
 - Klienten er skrevet i JavaFX og ligger i mappen `gazelleFX/`
 - Tjeneren er skrevet i Spring Boot og ligger i mappen `server/`
 - Delt logikk, slik som objekter og tekst-tolking ligger i `common/`

Tjeneren tar seg av lagring og det meste av prosessering.
Den har et REST-api som klienten bruker for å hente og sende data.

## Bygging og testing
Vi bruker maven som byggverktøy, og alle modulene er samlet i toppnivå-`pom.xml`-filen.

For å kompilere, teste og sjekke kodekvalitet på hele prosjektet, skriv:
```
mvn verify
```

For å se testdekningsgrad gå til:
TODO

For å kjøre klienten, gå inn i `gazelleFX/` og skriv:
```
mvn javafx:run
``` 
Merk at `JAVA_HOME` må være satt til en installasjon av Java 14.

## Bidrag og utvikling

Brukerhistorier ligger [her](/brukerhistorier/brukerhistorier.md).

For prosess og system for bruk av GitLab til smidig utvikling se [CONTRIBUTING.md](/CONTRIBUTING.md).