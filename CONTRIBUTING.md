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
slippe å bruke mye ressursjer på å forutse alle tenkelige situasjoner, og lettere kunne
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
[Utviktling](https://gitlab.stud.idi.ntnu.no/it1901/groups-2020/gr2010/gr2010/-/boards/2107)
og søke etter `milestone=<navn på sprint>`. Dette er da sprintens smidig-tavle.
I tillegg lager GitLab burndown-diagrammer for hver milepæl.

## Underveis i sprinten

Minst to ganger i uka skal gruppen ha samlingsmøte, der medlemmene deler hva de har gjort siden sist,
og hva de skal jobbe med vidre. Man ser på sprint-tavla og blir enige om hvem som skal tildeles
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

I tillegg til å lage en utviklingsgren med et fornuftig navn,
skal alle kodebunter være navngitt deskriptivt etter reglene i [conventional commits](https://www.conventionalcommits.org/en/v1.0.0/).
Dette vil si at alle buntmeldinger er på formen
```
feat(grensesnitt): legge til statuslinje for nettverksoprasjoner

Beskrivelse av forandringen, dersom det trengs.
Merk at øverste linje er i presens, har liten forbokstav og er uten tegnsetting.
```
Vi holder oss til de definerte engelske nøkkelordene for maskinlesbarheten sin del.

### Kontrollering

Etter utvikling lages en fletteforespørsel (Merge request) fra utviklingsgrenen til hovedgrenen.
Dersom forandringer har skjedd på hovedgrenen i mellomtiden må utviklingsgrenen enten sammeflettes
eller lempes om. Oppgaven på sprint-tavlen flyttes til "Kontroll"-kolonnen, og et annet gruppemedlem
blir bedt om å vurdere fletteforespørselen.

## Sprintretrospektiv

Når sprinten er ferdig skal gruppen samles for å vise det de har gjort, se på burndown-grafen,
og diskutere hvordan sprinten gikk. De skal skrive ned hva de vil ha mer av / ta med vidre,
og hva de vil ha mindre av. Ønskene tas opp i plenum.

# Språk

Vi bruker norsk som språk på GitLab og i dokumentasjon, og nynorsk og bokmål er sidestilt.
Variabelnavn og kommentarer i kode er på engelsk, og i noen *få* tilfeller brukes engelske ord
der det ikke er enighet om norske utgaver. Forslag:

 - issue -> **oppgave** (issue task list kalles **underoppgaver**)
 - tag -> **git-merkelapp**
 - label -> **GitLab-merkelapp**
 - commit -> **bunt**
 - merge request -> **fletteforespørsel**
 - merge -> **sammeflette**
 - rebase -> **lempe om**
