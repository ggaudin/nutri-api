# Nutri API

API REST en Java / Spring Boot qui permet à des diététiciens de créer et gérer des plans alimentaires pour leurs patients, à partir d'une base d'aliments et de leurs valeurs nutritionnelles (données Ciqual / ANSES).

Un frontend React est développé séparément pour exploiter cette API : [nutri-front](https://github.com/ggaudin/nutri-front).

## Fonctionnalités

- Inscription et authentification par **JWT** (Spring Security, API stateless)
- Trois rôles : `PATIENT`, `DIETETICIEN`, `ADMIN`
- Espace diététicien : liste des patients qui lui sont rattachés et fiche détaillée de chacun
- Calcul des besoins nutritionnels d'un patient (taille, poids, NAP, âge, genre)
- Création, consultation, modification et suppression de plans alimentaires
- Modèles de plans réutilisables (templates)
- Recherche d'aliments et calcul des apports en macronutriments d'un plan
- Contrôle d'accès métier aux données selon l'utilisateur connecté

## Technologies

- Java 21
- Spring Boot 4 (Spring Web, Spring Security, Spring Data JPA)
- JWT (jjwt 0.12)
- MySQL
- Maven (wrapper inclus)
- JUnit 5 / Mockito

## Endpoints

Les routes publiques (`/auth/inscription`, `/auth/connexion`) ne nécessitent pas de token. Toutes les autres attendent le header `Authorization: Bearer <token>`, obtenu à la connexion.

| Méthode | URL | Rôle | Description |
|---|---|---|---|
| POST | `/auth/inscription` | public (sans token) | Création de compte |
| POST | `/auth/connexion` | public (sans token) | Connexion, renvoie le token JWT |
| GET | `/auth/me` | authentifié | Utilisateur connecté |
| GET | `/aliments/search?motRecherche=` | DIETETICIEN, PATIENT | Recherche d'aliments |
| GET | `/aliments/{id}` | DIETETICIEN, PATIENT | Détail d'un aliment et de ses macronutriments |
| GET | `/diet/profil` | DIETETICIEN | Profil du diététicien connecté |
| GET | `/diet/patients` | DIETETICIEN | Patients rattachés au diététicien |
| GET | `/diet/patients/{id}` | DIETETICIEN | Fiche d'un patient |
| GET | `/diet/patients/{id}/besoins` | DIETETICIEN | Besoins nutritionnels du patient |
| GET | `/diet/patients/{id}/palim` | DIETETICIEN | Plans du patient |
| POST | `/diet/patients/{id}/palim` | DIETETICIEN | Création d'un plan pour le patient |
| POST | `/diet/palim` | DIETETICIEN | Création d'un modèle de plan |
| GET | `/diet/palim-templates` | DIETETICIEN | Modèles disponibles (les siens + modèles globaux) |
| GET | `/palim/{id}` | DIETETICIEN, PATIENT | Consultation d'un plan avec ses apports |
| PUT | `/palim/{id}` | DIETETICIEN | Modification d'un plan |
| DELETE | `/palim/{id}` | DIETETICIEN | Suppression d'un plan |

## Architecture

Architecture en couches : `controller` → `service` → `repository` → MySQL.

- Les **entités JPA** (`model`) représentent utilisateurs, patients, diététiciens, plans, repas, composants de repas, aliments et leur composition.
- Les **DTOs** (`dto`) séparent les données exposées par l'API des entités persistées.
- La logique des plans alimentaires est répartie entre plusieurs services (`service/planalimentaire`) : construction, mise à jour, calcul nutritionnel et mapping.

### Règles d'accès

Le rôle est contrôlé par Spring Security selon l'URL. Les règles plus fines sont vérifiées côté métier, dans `AccessService` :

- un diététicien n'accède qu'aux patients qui lui sont rattachés ;
- un diététicien n'accède à un plan que s'il en est l'auteur et qu'il suit toujours le patient concerné ;
- un patient n'accède qu'à ses propres plans ;
- les modèles globaux (`GLOBAL`) sont en lecture seule pour les diététiciens ;
- un administrateur n'a pas accès aux données de santé des patients.

## Plans alimentaires

Un plan est composé de **repas**, eux-mêmes constitués de **composants** (un aliment et une quantité en grammes).

| | Valeurs | Signification |
|---|---|---|
| `TypePlan` | `PERSONNALISE` / `TEMPLATE` | Plan attribué à un patient / modèle réutilisable |
| `VisibilitePlan` | `PRIVATE` / `GLOBAL` | Visible par son auteur / modèle partagé géré par les admins |

## Données nutritionnelles

Les aliments et leur composition proviennent de la table **Ciqual (ANSES)**. Les teneurs sont exprimées pour 100 g. Les apports d'un plan sont calculés à partir des macronutriments, identifiés par leur code de constituant Ciqual :

| Code | Constituant |
|---|---|
| `25000` | Protéines |
| `31000` | Glucides |
| `40000` | Lipides |

## Tests

Tests unitaires de la couche service (JUnit 5 + Mockito, sans base de données) :

```bash
./mvnw test
```

## Exécution en local

La base de données (schéma et import Ciqual) n'est pas fournie dans ce dépôt. Les informations sensibles ne sont pas versionnées : elles sont lues depuis des variables d'environnement.

| Variable | Rôle |
|---|---|
| `DB_PASSWORD` | Mot de passe de la base MySQL `nutri` |
| `JWT_SECRET` | Clé de signature des tokens JWT |

```bash
./mvnw spring-boot:run      # Windows : .\mvnw.cmd spring-boot:run

## Statut et évolutions prévues

Projet en cours de développement. Évolutions prévues :

- gestion des patients par le diététicien (création, mise à jour, détachement)
- mise à jour et suppression du profil diététicien
- aliments personnalisés
- espace administrateur (création des modèles globaux)
