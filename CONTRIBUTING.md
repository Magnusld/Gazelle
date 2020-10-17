# Utvikling og bidrag

Dette prosjektet utvikles av gruppe 10 i faget IT1901 på NTNU høsten 2020.
Gruppen har signert gruppekontrakt, og skal bruke smidige metoder, beskrevet i dette dokumentet.

## Prosjektplanlegging

I repoet ligger markdown-filer med brukerhistorier. Brukerhistoriene beskriver funksjonaliteten, og er på formen
```
Som en ___ trenger jeg ___ sånn at jeg ___.
```
Brukerhistoriene har en assosiert prioritet, og om de er ferdig implementert eller ikke.
Det er et poeng å ikke planlegge for mye før man begynner å implementere ting, for å
slippe å bruke mye ressurser på å forutse alle tenkelige situasjoner, og lettere kunne
endre plan underveis, for eksempel hvis mål endrer seg, eller hvis svakheter oppdages underveis.

## Sprintplanlegging

Arbeidet med implementering er delt inn i Sprinter, der hver sprint begynner 
med en sprintplanlegging og slutter med en milepæl i GitLab.
På sprintplanleggingen blir brukerhistorier for perioden valgt ut og omgjort til én eller flere oppgaver (GitLab issues).
Inndelingen er slik at hver oppgave blir så individuell som mulig, men likevel implementerer en konkret kodebit.
Oppgaven får en merkelapp med brukerhistorien, og blir tilordnet sprint-milepælen.

En sprint kan også inneholde oppgaver fra etterslep-lista, selv om de ikke hører til en brukerhistorie.
Etterslep-lista består av alle oppgaver som ikke er tilordnet milepæl, og utviklere
kan når som helst legge til nye oppgaver på etterslep-lista.

Alle oppgaver i en sprint får tildelt poeng som et estimat på arbeidsmengde. Vi bruker GitLab sin `/estimate`-kommando.
Når sprintplanleggingen er ferdig kan man se alle oppgaver i sprinten ved å gå til GitLab-tavla
[Utvikling](https://gitlab.stud.idi.ntnu.no/it1901/groups-2020/gr2010/gr2010/-/boards/2107)
og søke etter `milestone=<navn på sprint>`. Dette er da sprintens smidig-tavle.

## Underveis i sprinten

Minst to ganger i uka skal gruppen ha samlingsmøte, der medlemmene deler hva de har gjort siden sist,
og hva de skal jobbe med videre. Man ser på sprint-tavla og blir enige om hvem som skal tildeles
hvilke oppgaver. Tavla er delt inn i kolonner. Til å begynne med er alle oppgaver kun "åpne",
og krever mer eller mindre diskusjon i gruppa før et gruppemedlem kan begynne implementasjon.
Alle trenger ikke være med på all diskusjon, men alle kan komme med innspill på oppgavens GitLab-side.

Når diskusjoner er ferdig får oppgaven merkelappen "Utviklingsklar", som er en egen kolonne på tavla.
Da kan et gruppemedlem få oppgaven tildelt, og den flyttes til kolonnen "Under utvikling".
Ting kan selvfølgelig oppstå underveis, men ideelt sett utvikles da oppgaven ferdig.

### Utvikling

Hver oppgave skal på dette stadiet være et tenkt konkret stykke arbeid.
Dette arbeidet skal skje i en egen gren i git, og det skal skrives både tester og
eventuell dokumentasjon. Man trenger ikke dogmatisk følge testdrevet utvikling,
men all kode som skal inn i prosjektet skal ha enhetstester.

Grenen skal få navnet GitLab foreslår for oppgaven.
I tillegg skal alle kodebunter være navngitt deskriptivt etter reglene i
[conventional commits](https://www.conventionalcommits.org/en/v1.0.0/).
Dette vil si at alle buntmeldinger er på formen
```
feat(#43): legge til statuslinje for nettverksoprasjoner

Beskrivelse av forandringen, kun dersom det trengs.
Inni parantesen skrives oppgavenummeret bunten jobber mot.
Merk at resten av linjen er i infinitiv, har liten forbokstav og er uten tegnsetting.
Linjen skal passe inn i setningen:
  
  Hvis denne bunten flettes inn i kodebasen vil den <melding>.
  
BREAKING CHANGES: ødelegger funksjonalitet og må derfor ha denne linjen
```
Vi holder oss til de definerte engelske nøkkelordene for maskinlesbarheten sin del:

`feat`, `fix`, `chore`, `build`, `test`, `refactor`, `perf`, `style`, `ci`, `docs`.

Bunter med automatiske meldinger (`revert `, `merge `) trenger ikke følge reglene.

### Kontrollering

Etter utvikling lages en fletteforespørsel (GitLab Merge request) fra utviklingsgrenen til hovedgrenen.
Fletteforespørselen skal ha en fornuftig tittel, og beskrivelen skal kort beskrive forandingen
og *hvorfor* forandringen er blitt gjort.
Hva som er blitt gjort skal være beskrevet i buntmeldingene. Fletteforespørselen skal også
referere til oppgaven den løser med `Fixes #58`, som GitLab forstår.
Så fremt man har brukt GitLab sitt forslag til gren-navn på oppgaven vil fletteforespørselen
automatisk få merkelapper og `Closes #<oppgavenr>` i beskrivelsen.

Oppgaven på sprint-tavlen flyttes til "Til vurdering"-kolonnen, og et annet gruppemedlem
skal bli bedt om å vurdere fletteforespørselen, gjennom å få den tilegnet. Vidre forbedringer og forandringer skal
legges på utviklingsgrenen, og når grenen er klar skal den flettes inn i hovedgrenen.

Dersom forandringer har skjedd på hovedgrenen i mellomtiden må utviklingsgrenen enten sammeflettes
eller lempes om (rebase) med hovedgrenens nye innhold før fletteforespørselen kan flettes.

#### CI/CD

Prosjektet skal bruke en GitLab-pipeline for å automatisk kjøre testene i utviklingsgrener,
og raportere testdekning. Denne statistikken skal helst vises på README-siden.
Andre verktøy vi bruker, f.eks. sjekk av kodekvalitet og formatering, skal også kjøres
i Pipeline og godkjennes for hver fletteforespørsel. Hvis noen av sjekkene mislykkes skal 
Pipelinen feile.

## Sprintretrospektiv

Når sprinten er ferdig skal gruppen samles for å vise det de har gjort
og diskutere hvordan sprinten gikk. Uavhengig av hvordan sprinten går skal hovedgrenen til prosjektet til enhver
tid være i en kjørbar tilstand, selv om det ikke nødvendigvis tilfredsstiller alle krav enda.
Dersom prosjektet krever mer jobbing, skal en ny sprint planlegges. Før det skal alle skrive ned
hva de vil ha mer av / ta med vidre, og hva de vil ha mindre av. Ønskene tas opp i plenum.

# Språk

Vi bruker norsk som språk på GitLab og i dokumentasjon, og nynorsk og bokmål er sidestilt.
Variabelnavn og kommentarer i kode er på engelsk, og i noen *få* tilfeller brukes engelske ord
der det ikke er enighet om norske utgaver. Forslag:

 - issue -> **oppgave** (issue task list kalles **underoppgaver**)
 - tag -> **git-merkelapp**
 - label -> **GitLab-merkelapp**
 - commit -> **bunt**
 - merge request -> **fletteforespørsel**
 - merge -> **sammenflette**
 - rebase -> **lempe om**
