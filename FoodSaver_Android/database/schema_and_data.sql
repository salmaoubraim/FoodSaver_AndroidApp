-- ============================================================
-- FoodSaver - Schéma de la base de données (SQLite / Room)
-- ============================================================
-- L'application crée cette base automatiquement via Room.
-- Ce fichier sert de référence pour les tables et données de test.
-- Nom de la base : foodsaver_db
-- ============================================================

-- --------------------------------------------
-- 1. Table USERS (utilisateurs)
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    email TEXT NOT NULL,
    password TEXT NOT NULL,
    name TEXT NOT NULL,
    role TEXT NOT NULL
);
-- role : 'client' ou 'merchant'

-- --------------------------------------------
-- 2. Table COMMERCES
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS commerces (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    name TEXT NOT NULL,
    merchantId INTEGER NOT NULL,
    FOREIGN KEY (merchantId) REFERENCES users(id) ON DELETE CASCADE
);

-- --------------------------------------------
-- 3. Table PANIERS
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS paniers (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    commerceId INTEGER NOT NULL,
    titre TEXT NOT NULL,
    prix REAL NOT NULL,
    quantite INTEGER NOT NULL,
    FOREIGN KEY (commerceId) REFERENCES commerces(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_paniers_commerceId ON paniers(commerceId);

-- --------------------------------------------
-- 4. Table RESERVATIONS
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS reservations (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    userId INTEGER NOT NULL,
    panierId INTEGER NOT NULL,
    quantiteReservee INTEGER NOT NULL,
    dateReservation INTEGER NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (panierId) REFERENCES paniers(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_reservations_userId ON reservations(userId);
CREATE INDEX IF NOT EXISTS index_reservations_panierId ON reservations(panierId);

-- ============================================================
-- DONNÉES D'EXEMPLE (optionnel)
-- ============================================================
-- Décommenter et exécuter pour insérer des données de test.
-- Attention : les id peuvent varier si la base existe déjà.

-- Utilisateurs
-- INSERT INTO users (email, password, name, role) VALUES ('client@test.fr', '123456', 'Jean Client', 'client');
-- INSERT INTO users (email, password, name, role) VALUES ('boulanger@test.fr', '123456', 'Marie Boulanger', 'merchant');

-- Commerces (merchantId = 2 si le commerçant est le 2e user inséré)
-- INSERT INTO commerces (name, merchantId) VALUES ('Boulangerie du Centre', 2);
-- INSERT INTO commerces (name, merchantId) VALUES ('Primeur Fraîcheur', 2);

-- Paniers (commerceId 1 = Boulangerie, 2 = Primeur)
-- INSERT INTO paniers (commerceId, titre, prix, quantite) VALUES (1, 'Panier viennoiseries du jour', 3.50, 10);
-- INSERT INTO paniers (commerceId, titre, prix, quantite) VALUES (1, 'Pain invendu', 1.00, 5);
-- INSERT INTO paniers (commerceId, titre, prix, quantite) VALUES (2, 'Légumes de fin de marché', 4.00, 8);

-- Réservations (userId 1 = client, panierId 1 = premier panier, dateReservation = timestamp en ms)
-- INSERT INTO reservations (userId, panierId, quantiteReservee, dateReservation) VALUES (1, 1, 2, 1710000000000);

-- ============================================================
-- RÉSUMÉ DES RELATIONS
-- ============================================================
-- User (1) -----> (N) Reservation   (userId)
-- Commerce (1) --> (N) Panier        (commerceId)
-- Panier (1) ----> (N) Reservation  (panierId)
-- User (1) -----> (N) Commerce       (merchantId, pour les commerçants)
