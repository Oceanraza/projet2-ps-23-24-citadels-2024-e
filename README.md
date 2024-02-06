# Citadelles - Équipe E

Bienvenue dans Citadelles !
[Version anglaise](README-en.md)

Ce projet a été développé dans le cadre d'un projet collaboratif en programmation. Il s'agit d'un programme en Java permettant à 4 robots de jouer à la première édition du jeu Citadelles.

---

## Table des matières

- [Sujet](#sujet)
- [Membres de l'équipe](#membres-de-léquipe)
- [Instructions d'utilisation](#instructions-dutilisation)
  - [Prérequis](#prérequis)
  - [Installation](#installation)
  - [Exploration du Projet](#exploration-du-projet)
- [Utilisation](#utilisation)
  - [Compiler et Exécuter](#compiler-et-exécuter)
  - [Exécution des Tests](#exécution-des-tests)

---

## Sujet

Ce projet vise à créer une version informatisée du jeu "Citadelles", en se basant sur la première édition, en intégrant les fonctionnalités suivantes :

### Fonctionnalités :

- **Interface de jeu :**
  - Représentation des cartes et des villes de chaque joueur.
- **Moteur de jeu :**
  - Gestion de la mise en place, des tours, des actions, des interactions, de la progression et de la fin de la partie.
- **Développement de robots de jeu :**
  - Du niveau de base jusqu'à des stratégies sophistiquées démontrables.
- **Simulation de parties :**
  - Implémentation d'une simulation de partie entre 4 robots, avec calcul des points et établissement d'un classement à la fin.
- **Visualisation textuelle simplifiée :**
  - Affichage textuel de l'état actuel du jeu.

---

## Membres de l'équipe

Ce projet a été réalisé par l'équipe Polytâche (E), composée de 4 étudiants de 3ᵉ année de la promotion 2023 de Polytech Nice Sophia en filière Sciences Informatiques (SI) :
- [Darina Chan](https://github.com/DarinaChan)
- [Océan Razafiarison](https://github.com/Oceanraza)
- [Mathis Jullien](https://github.com/Mathis-Jullien)
- [Quentin Elleon](https://github.com/QuentinELLEON)

## Instructions d'utilisation

### Prérequis

Avant d'utiliser ce programme, assurez-vous d'avoir les éléments suivants installés sur votre machine :

- **Java 17** : Le projet est développé en Java 17. Vous pouvez le télécharger depuis le [site officiel d'OpenJDK](https://jdk.java.net/17/).

- **Git** : Si vous ne l'avez pas déjà, installez Git depuis le [site officiel de Git](https://git-scm.com).

- **Maven** : Utilisé pour compiler et exécuter le programme, vous pouvez le télécharger depuis le [site officiel de Maven](https://maven.apache.org/download.cgi).

### Installation

Pour installer le programme :

Ouvrez une console et placez-vous dans le répertoire où vous souhaitez télécharger le programme.
```bash
git clone https://github.com/pns-si3-projects/projet2-ps-23-24-citadels-2024-e.git
cd projet2-ps-23-24-citadels-2024-e
```

### Exploration du Projet

- **'src/main/java/fr/cotedazur/univ/polytech/startingpoint'** : Contient le code source.
- **'src/test/java/fr/cotedazur/univ/polytech/startingpoint'** : Contient les tests unitaires.

## Utilisation

### Compiler et Exécuter

Assurez-vous d'être dans le répertoire racine du projet (**'projet2-ps-23-24-citadels-2024-e'**). Vous pouvez alors compiler et exécuter le programme principal avec la commande Maven suivante :

```bash
mvn exec:java
```

### Exécution des Tests

Les tests unitaires sont situés dans le répertoire `src/test/java/fr/cotedazur/univ/polytech/startingpoint`. Pour les exécuter, utilisez la commande suivante :

```bash 
mvn clean package
```

Assurez-vous que tous les tests passent sans erreurs, confirmant ainsi la solidité du programme.

Nous espérons que vous apprécierez l'utilisation de notre programme ! N'hésitez pas à lancer plusieurs parties pour découvrir les différentes possibilités.