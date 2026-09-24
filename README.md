# Inventario
Applicazione web in Java per la gestione di un inventario e di un carrello: permette di registrare, organizzare e consultare gli articoli, aggiungerli al carrello e, una volta convalidato l'ordine, aggiornare automaticamente il cassetto di riferimento. Il progetto include autenticazione OAuth con Google ed è pronto per il deploy su Heroku.

✨ Features
📦 Gestione articoli dell'inventario (aggiunta, modifica, eliminazione)

🛒 Gestione carrello: aggiunta/rimozione articoli, riepilogo

✅ Convalida ordine: all'atto della conferma, il cassetto di riferimento viene aggiornato automaticamente

🔐 Autenticazione OAuth con Google

🗂️ Organizzazione degli articoli per cassetto/categoria

🔎 Consultazione e ricerca degli elementi

🌐 Interfaccia web dinamica con JavaScript

🗄️ Persistenza su MySQL

☁️ Deploy su Heroku tramite Procfile

⚙️ Build con Maven

🏗️ Tecnologie
Componente	Tecnologia
Linguaggio	Java
Build	Maven
Web server	Jetty (embedded)
Database	MySQL
Autenticazione	OAuth 2.0 (Google)
Frontend	HTML / CSS / JavaScript
Deploy	Heroku
IDE	Eclipse
📂 Struttura del repository
File / Cartella	Descrizione
src/main/	Codice sorgente dell'applicazione
pom.xml	Configurazione Maven e dipendenze
Procfile	Istruzioni di avvio per Heroku (WAR su root context)
system.properties	Versione di Java per Heroku
.classpath, .project, .settings	File di progetto Eclipse
.gitignore	File esclusi dal versionamento
🔄 Flusso principale
L'utente si autentica tramite OAuth con Google

Sfoglia l'inventario e aggiunge gli articoli al carrello

Convalida l'ordine dal carrello

Il sistema aggiorna il cassetto di riferimento con i movimenti effettuati

🚀 Build e avvio in locale
Requisiti:

JDK (versione indicata in system.properties)

Maven installato

MySQL attivo con un database configurato

Credenziali OAuth Google (Client ID e Client Secret) da Google Cloud Console

bash
# Clona il repository
git clone https://github.com/gabbro95/inventario.git
cd inventario

# Compila il progetto
mvn clean package

# Avvia con Jetty
mvn jetty:run
L'applicazione sarà disponibile all'indirizzo indicato nel log di avvio (di solito http://localhost:8080/).

⚠️ Configura le credenziali MySQL e i parametri OAuth Google (Client ID, Client Secret, redirect URI) prima dell'avvio.

☁️ Deploy su Heroku
Il progetto è già configurato per Heroku:

Procfile → avvia il WAR sul root context (--path /)

system.properties → specifica la versione di Java

maven-dependency-plugin → scarica Jetty Runner per l'esecuzione

bash
heroku create
git push heroku main
💡 Ricorda di impostare le variabili d'ambiente per MySQL e OAuth Google anche su Heroku (es. heroku config:set).

🖼️ Screenshot


🤝 Contributi
Contributi e segnalazioni sono benvenuti: apri una issue o una pull request.

📄 Licenza
Rilasciato sotto licenza MIT. Vedi il file LICENSE per i dettagli.
