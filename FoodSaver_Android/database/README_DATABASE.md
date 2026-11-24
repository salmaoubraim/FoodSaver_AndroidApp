# Base de données FoodSaver

## Création automatique

Avec l’application Android, la base **foodsaver_db** et les tables sont créées automatiquement par **Room** au premier lancement. Vous n’avez rien à exécuter à la main pour que l’app fonctionne.

## Fichier de référence

- **`schema_and_data.sql`** : schéma des tables (CREATE TABLE) et exemples d’INSERT pour référence ou pour une base SQLite externe.

## Tables

| Table           | Description                          |
|----------------|--------------------------------------|
| **users**      | Utilisateurs (client / commerçant)   |
| **commerces**  | Commerces (liés à un merchant)       |
| **paniers**    | Paniers proposés par un commerce     |
| **reservations** | Réservations (client + panier + quantité + date) |

## Colonnes par table

### users
| Colonne   | Type    | Contraintes |
|-----------|---------|-------------|
| id        | INTEGER | PK, AUTOINCREMENT |
| email     | TEXT    | |
| password  | TEXT    | |
| name      | TEXT    | |
| role      | TEXT    | `client` ou `merchant` |

### commerces
| Colonne   | Type    | Contraintes |
|-----------|---------|-------------|
| id        | INTEGER | PK, AUTOINCREMENT |
| name      | TEXT    | |
| merchantId| INTEGER | FK → users(id) ON DELETE CASCADE |

### paniers
| Colonne   | Type    | Contraintes |
|-----------|---------|-------------|
| id        | INTEGER | PK, AUTOINCREMENT |
| commerceId| INTEGER | FK → commerces(id) ON DELETE CASCADE |
| titre     | TEXT    | |
| prix      | REAL    | |
| quantite  | INTEGER | |

### reservations
| Colonne         | Type    | Contraintes |
|-----------------|---------|-------------|
| id              | INTEGER | PK, AUTOINCREMENT |
| userId          | INTEGER | FK → users(id) ON DELETE CASCADE |
| panierId        | INTEGER | FK → paniers(id) ON DELETE CASCADE |
| quantiteReservee| INTEGER | |
| dateReservation | INTEGER | (timestamp en millisecondes) |

## Relations

- **User (1) → (N) Reservation** : un utilisateur a plusieurs réservations.
- **Commerce (1) → (N) Panier** : un commerce a plusieurs paniers.
- **Panier (1) → (N) Reservation** : un panier peut être réservé plusieurs fois (quantités différentes).
- **User [merchant] (1) → (N) Commerce** : un commerçant peut avoir plusieurs commerces.
