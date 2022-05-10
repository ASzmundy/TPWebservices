# TP Architectures orienté systèmes
## Dossiers
### Docker
Docker contient les fichiers relatifs à Docker : le fichier dockercompose.yml et les fichiers de configuration

### GUI
Contient le GUI (Interface utilisateur) du projet

### Mirth Connect
Contient le code à mettre dans Mirth Connect

### SQL
Contient les scripts SQL à lancer dans la base PostgreSQL

### ws_client
Contient le code source du webservice client permettant de dispatcher les requêtes du GUI ( remplace ZUUL )

### ws_newHL7
Contient le code source du webservice de génération de fichier HL7

### ws_query
Contient le code source du webservice de requête à la base de données

### ws_update
Contient le code source du webservice de mise à jour de la base de données


## Guide d'utilisation
### Installation

 1. Lancer docker et lancer la commande compose sur le fichier dockercompose.yml du dossier docker
 2. Se connecter sur la base de données postgresql avec les identifiants ( login : postgres mdp: admin )
 3. Exécuter les scripts suivants du dossier SQL :
	3.1 CREATE Database
	3.2 CREATE Table
 4. Lancer Mirth Connect et créer un channel avec pour dossier source **ws_newHL7/Output**
 5. Dans Mirth Connect mettre le script SQL Channel mirthconnect.sql dans le channel et appliquer le transformer javascript contenu dans Transformer mirthconnect.js
 6. Lancer les webs services
 7. Lancer le GUI
 

 
