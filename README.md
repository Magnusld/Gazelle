[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.idi.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2020/gr2010/gr2010) 
[![pipeline status](https://gitlab.stud.idi.ntnu.no/it1901/groups-2020/gr2010/gr2010/badges/master/pipeline.svg)](https://gitlab.stud.idi.ntnu.no/it1901/groups-2020/gr2010/gr2010/-/commits/master)

# Gazelle

![Gazelle logo](assets/logo.svg)

Dette er prosjektet til gruppe 10 i faget IT1901 høsten 2020.
For en komplett beskrivelse av prosjektet se [her](gazelle/README.md).
For å se aller siste versjon av prosjeket, se [gazelle.haved.no](https://gazelle.haved.no).
For å bygge og teste prosjektet selv, fortsett å lese!

## Organisering
Appen er delt inn i flere moduler: Webklient, JavaFX-klient, tjener og delt logikk.
 - Webklienten er skrevet med Vue og ligger i mappen `gazelle/`
 - [JavaFX-klienten](gazelleFX/README.md) er skrevet i JavaFX og ligger i mappen `gazelleFX/`
 - Tjeneren er skrevet i Spring Boot og ligger i mappen `server/`
 - [Delt logikk](common/README.md), slik som objekter og tekst-tolking ligger i `common/`

![PlantUML](/assets/architecture.png)

Tjeneren tar seg av lagring og det meste av prosessering.
Den har et REST-api som klientene bruker for å hente og sende data.

## Byggesystem
Vi bruker maven som byggverktøy, og alle modulene er samlet i toppnivå-`pom.xml`-filen.
Også webklienten bygges og testes gjennom maven.

Merk at `JAVA_HOME` må være satt til en installasjon av Java 14.

## Testing
Hver modul inneholder enhetstester som kjøres med.
```
mvn test
```
Dette tester hver modul for seg.
Klienter bruker derfor mock-servere for å simulere kommunikasjon med REST-apiet.

For å kompilere, teste, sjekke kodestil og kodekvalitet på hele prosjektet, skriv:
```
mvn verify
```
For å se testdekningsgrad av javamodulene, gå til ```target/site/jacoco/index.html``` i hver modul.
Testdekning på webappen ligger i ```gazelle/coverage/lcov-report/index.html```.

**Merk**: I dette prosjektet har JavaFX-klienten relativt dårlig testdekningsgrad
i forhold til de andre modulene. Dette skyldes fokuset vårt på Vue som frontend,
og forvirring rundt oppgavekrav.

## CI / CD
Alle grener testes automatisk i en GitLab Pipeline, for å hindre fletting av utestet kode.
Feil i kodestil og kodekvalitet får også Pipelinen til å feile.

På hovedgrenen brukes en Pipeline for å pakke tjeneren til en jar, og
webappen kompileres for nettleser. Ved vellykket testing og kompilering blir
en WebHook sendt til en DigitalOcean-droplet, som laster ned artifaktene fra Pipelinen.
Der blir gazelle-tjeneren kjørt, og webappen servert. Se [produksjonstjeneren](https://gazelle.haved.no).

Produksjonstjeneren kjører en [hjemmelaget tjener](https://github.com/haved/DropletManager) for å håndtere WebHooks.
Vi skulle gjerne hatt en testtjener òg, men slik har vi ikke råd til.

## Kjøring av app
For å kjøre kommandoer i enkeltmoduler, bruk `-pl <mappe>`.
Dette krever at andre moduler er installert i den lokale maven-siloen.
```
mvn install -DskipTests
```

For å kjøre tjeneren (port 8088), skriv
```
mvn spring-boot:run -pl server
```
**Merk:** I GitPod må du gå til Åpne Porter og tykke "Make Public" for å gi tilgang til klientene.

For å åpne webappen i nettleser (port 8080), åpne en ny terminal og skriv
```
mvn frontend:yarn@"yarn serve" -pl gazelle
```

For å starte JavaFX-klienten, skriv
```
mvn javafx:run -pl gazelleFX
```

#### Manuell bruk av database
Mens tjeneren kjører, gå til `localhost:8088/h2`. Skriv inn følgende:
 - JDBC URL: `jdbc:h2:file:./database`
 - User Name: `sa`
 - Password: blankt

Her får du tilgang til databasen, og kan legge til og fjerne elementer med SQL.
 
##### I GitPod
Gå til Åpne Porter, trykk "Open Browser" på port 8088, og legg til `/h2` bak URLen.
 
## Bidrag og utvikling

Brukerhistorier ligger [her](/brukerhistorier/brukerhistorier.md).

For prosess og system for bruk av GitLab til smidig utvikling se [CONTRIBUTING.md](/CONTRIBUTING.md).