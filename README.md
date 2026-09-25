# Nutri API

API REST développée avec Java et Spring Boot dans le cadre d'un projet personnel de remise à niveau en développement backend.

L'application permet à des diététiciens de créer et gérer des plans alimentaires pour leurs patients, à partir d'une base d'aliments contenant leurs valeurs nutritionnelles.

Un frontend React est développé séparément pour exploiter cette API.

## Fonctionnalités

- Inscription et authentification des utilisateurs avec **Spring Security / JWT**
- Gestion des rôles : patient, diététicien (et administrateur)
- Gestion des patients et des diététiciens
- Création et gestion de plans alimentaires
- Organisation des plans par repas et aliments
- Recherche d'aliments et calcul des valeurs nutritionnelles
- Gestion des droits d'accès selon l'utilisateur connecté
- Gestion de plans personnalisés et génériques

## Technologies

- Java
- Spring Boot
- Spring Web
- Spring Security
- JWT
- Spring Data JPA
- MySQL
- SQL

## Architecture

L'application suit une architecture classique en couches :

Controller
    ↓
Service
    ↓
Repository
    ↓
MySQL

Les entités JPA représentent notamment les utilisateurs, patients, diététiciens, plans alimentaires, repas, composants de repas et aliments.

Les DTOs permettent de séparer les données exposées par l'API des entités persistées.

Les règles d'accès aux données sont traitées au niveau métier afin de vérifier notamment qu'un diététicien ne peut accéder qu'aux patients qui lui sont associés.

## Exemple de structure d'un plan alimentaire

Un plan est composé de repas, eux-mêmes constitués de composants associés à des aliments :

Plan alimentaire
├── Petit-déjeuner
│   ├── Pain
│   └── Beurre
├── Déjeuner
│   ├── Riz
│   └── Poulet
└── Dîner
    ├── Pâtes
    └── Légumes

## Données nutritionnelles

Les aliments utilisés par l'application proviennent d'une base de données nutritionnelle basée sur les données Ciqual / ANSES.

Les valeurs nutritionnelles sont utilisées pour calculer les apports associés aux quantités d'aliments intégrées dans un plan.

## Tests

Des tests automatisés sont en cours d'ajout.

## Statut du projet

Projet en cours de développement, réalisé dans le cadre d'une remise à niveau en Java / Spring Boot.