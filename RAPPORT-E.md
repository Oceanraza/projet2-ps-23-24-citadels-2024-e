# Rapport de Projet : Citadelles - Équipe E

## Auteurs

- [Darina Chan](https://github.com/DarinaChan)
- [Océan Razafiarison](https://github.com/Oceanraza)
- [Mathis Jullien](https://github.com/Mathis-Jullien)
- [Quentin Elleon](https://github.com/QuentinELLEON)
## Sommaire

- [Introduction](#introduction)
- [Fonctionnalités Réalisées](#fonctionnalités-réalisées)
    - [Liste des Fonctionnalités](#liste-des-fonctionnalités)
- [Système de Logs](#système-de-logs)
- [Archive des Statistiques sous forme de CSV](#archive-des-statistiques-sous-forme-de-csv)
- [Stratégie du Bot Richard](#stratégie-du-bot-richard)
- [Analyse : Pourquoi Einstein bat-il Richard ?](#analyse-pourquoi-einstein-bat-il-richard-)
- [Architecture du Projet](#architecture-du-projet)
    - [Localisation des Informations](#localisation-des-informations)
    - [État de la Base de Code](#état-de-la-base-de-code)
- [Processus de Développement](#processus-de-développement)
- [Conclusion](#conclusion)

<div style="page-break-before: always;"></div>

## Introduction

Dans le cadre d'un projet collaboratif en programmation, notre équipe, composée de quatre étudiants de Polytech Nice
Sophia, a développé un programme en Java permettant à quatre robots de jouer à la première édition du jeu Citadelles. Ce
rapport vise à présenter les avancées significatives du projet, les défis rencontrés et les solutions apportées, ainsi
que l'architecture du système développé.

## Fonctionalités réalisées

`Toutes` les fonctionalités `basiques` du plateau de jeu ont étés réalisées.
Les seules règles n'ayant pas étés traitées sont celles des limitations techniques d'un jeu de société (nombre de pièces
dans la banque).

### Liste les fonctionalités réalisées

## Système de logs

Les logs ont étés réalisés à l'aide de la librairie de logging interne a java (java.util.logging).

- `Problème :` Nous avons besoin de régler les logs en deux parties différentes puisque nous ne devons pas avoir les
  logs d'une partie normale (description de chaque joueur, leurs choix...etc) lors de l'éxécution des 2x1000 parties.
- `Solution :` Pour pallier à ce problème, nous avons crée un second logger avec un niveau différent du premier, ce qui
  nous permet donc d'activer ou de désactiver le logger non voulu à notre guise.

## Archive des statistiques sous forme de CSV

Lorsque nous mettons comme argument --csv, nous sauvegardons alors les parties jouées. 20 parties sont jouées et nous
obtenons un CSV de la forme suivante :
![Alt text](doc/csv_example.PNG)
Il est bon de noter que lorsque nous réexcutons la même commande avec --csv, les données sont agrégées et les scores
moyens sont recalculés (scoreInitial + nouveauScore)/2
**Explication rapide :**

- *Main.java :* C'est ici que les nouvelles données sont stockées et calculées avant de les envoyer au programme les
  rajoutant dans le CSV. Elles sont stockées sous la forme de deux HashMap, les deux ayant pour clé le nom du joueur
  mais avec une différence dans leur valeur :
  `totalScores` associe son score à un joueur.
  `totalPlacements` associe un array de quatre entiers représentant la 1ère place, 2e place... à un joueur, donnant donc
  le nombre de fois où il a obtenu cette place.
- *Csv.java :* C'est ici que les données sont injectées dans le Csv. Son principe est simple et se divise en deux
  parties :
  `Vérification de l'existence du fichier` , s'il n'existe pas, en créer un avec les en-têtes de colonnes (resetStats).
  `Rajout de données ` si le fichier existe, lire les données déjà existantes et les additionner (ou dans le cas des
  scores, faire une moyenne) avec celles que nous voulons rajouter. Cela est géré en regardant la colonne n°1 qui est le
  nom du joueur et en ajoutant ses valeurs a la liste envoyée par Main correspondante.
  `Si le joueur n'est pas déjà dans la base de données ` mais que nous avons d'autres joueurs déjà présents, il sera
  alors rajouté à la fin.
  `Enfin` le fichier est reinitialisé, nous rajoutons les en-têtes et les nouvelles données.

## Bot Richard

Le bot RichardAlgo implémente une stratégie nuancée visant à optimiser chaque mouvement en fonction des positions des
joueurs et de la phase du jeu. Voici un résumé des stratégies clés employées par RichardAlgo pour guider les joueurs
novices et expérimentés :

### Stratégie de base

#### Assassin

- **Usage sélectif contre le Voleur :** L'Assassin ne cible le Voleur que pour empêcher un joueur de s'enrichir de
  manière excessive ou si le Voleur est choisi par un concurrent direct pour la victoire. Cela car le Voleur ne peut pas
  voler l'Assassin, éliminant un risque direct tout en neutralisant une menace pour les adversaires.
- **Prudence avec le Condottiere :** L'Assassin évite de cibler le Condottiere à moins d'être en tête ou de soupçonner
  le Condottiere d'être choisi par un joueur proche de la victoire. Cela minimise les risques pour l'Assassin, sauf dans
  des circonstances très spécifiques.

#### Architecte

- **Contrôle du leader :** L'Architecte est évité par les joueurs bien placés pour prévenir une avance décisive. Si un
  joueur peut potentiellement faire un bond significatif dans sa construction, les autres joueurs doivent intervenir
  pour empêcher cette situation, idéalement en choisissant l'Assassin pour éliminer l'Architecte du tour.

#### Roi

- **Interruption de séquences gagnantes :** Le Roi est une cible prioritaire pour empêcher un joueur en position de
  gagner de jouer en premier dans le prochain tour et de choisir des personnages clés comme l'Assassin. Les joueurs
  doivent ajuster leur sélection de personnages pour contrer stratégiquement ce joueur.

### Dernier tour

- **Blocage du joueur en tête :** Si un joueur est sur le point de construire son dernier quartier, les adversaires
  doivent utiliser une combinaison de personnages pour bloquer ou ralentir sa progression. Cela peut inclure
  l'utilisation de l'Assassin pour éliminer des personnages clés ou du Condottiere pour détruire un quartier crucial.

## Pourquoi Einstein bat-il Richard ?

*Einstein joue pour lui-même :* La principale différence vient du fait qu'Einstein joue pour lui-même et pour maximiser
ses propres chances. Il va essayer de récupérer le plus d'argent possible et de construire ses bâtiment le plus vite
possible. `A l'instar `de Richard qui lui essaie de déstabiliser ses adversaires et de les faire perdre. Dans une partie
avec quatre joueurs, celui se priorisant sur les autres est voué à être plus performant sur le long terme en général.

---

# Architecture du projet

![img.png](doc/img.png)

L'architecture de notre projet a été conçue avec une approche modulaire et orientée objet, ce qui nous a permis de
maintenir une séparation claire des responsabilités tout en facilitant l'extensibilité. Au cœur de notre application, la
classe `Game` orchestre la logique de jeu, s'appuyant sur des composants spécialisés tels que `ActionManager` pour gérer
les actions des joueurs et `GameState` pour suivre l'état courant du jeu. Nous avons opté pour des classes abstraites
telles que `Player` et `GameCharacter` pour fournir des modèles extensibles pour les différentes entités du jeu, comme
les personnages `King`, `Assassin`, entre autres. Cette décision a été guidée par la volonté de faciliter l'ajout de
nouveaux types de joueurs et de personnages sans perturber la logique existante. De plus, l'emploi de fichiers JSON pour
la gestion des données de jeu offre une flexibilité dans la manipulation et la persistance des états de jeu.

## Desing Patterns

- `Singleton :` Utilisé pour le logger avec la classe ``CitadelsLogger``.
- `Command :` Utilisé pour les actions des joueurs avec la classe ``ActionManager``.
- `Strategy :` Utilisé pour les stratégies des bots avec les classes ``RichardAlgo`` et ``EinsteinAlgo``.
- `State :` Utilisé pour les états du jeu avec la classe ``GameState``.
### Localisation des Informations

La documentation [JavaDoc](doc/javadoc/index.html), générée et structurée de manière exhaustive, sert de référence
principale pour la compréhension des points clés et des classes importantes du projet. Chaque fichier HTML de
documentation, comme `Game.html`, `ActionManager.html`, ou encore ceux situés dans les dossiers `character` et `city`,
contient des informations détaillées sur les responsabilités et les comportements des classes et méthodes. Cette
documentation est un outil précieux pour les développeurs actuels et futurs afin de se familiariser rapidement avec le
projet.

### État de la Base de Code

//Mathis
(Rajouter image de la couverture de code et de sonarqube ?)

---

# Processus

Le projet est divisé en plusieurs catégories toutes différentes les unes des autres :

- `Personnages :` Quentin et Darina.
- `Merveilles :` Mathis et Océan.
- `Planification et gestion de l'architecture du projet :` Océan.
- `Stratégie Richard :` Mathis et Océan.
- `Stratégie Einstein :` Quentin et Darina.
- `Implémentation Jcommander :` Darina.
- `2x1000 et csv :` Quentin.
- `Logger :` Mathis.
- `Calcul des scores :` Mathis.
- `Tests unitaires et Mocks :` Tout le monde mais surtout Darina.

*Process de l'équipe :*
//Mathis

Nous espérons que vous apprécierez l'utilisation de notre programme ! N'hésitez pas à lancer plusieurs parties pour
découvrir les différentes possibilités.