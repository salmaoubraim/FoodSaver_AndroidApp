-- ============================================================
-- FoodSaver - Script SQL pour MySQL (foodsaver_db)
-- À exécuter dans phpMyAdmin ou MySQL
-- Compatible avec votre structure actuelle (users déjà existant)
-- ============================================================

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

-- --------------------------------------------------------
-- 1. TABLE users (déjà existante - pas de modification)
-- Si vous voulez ajouter plus d'utilisateurs, utilisez les INSERT ci-dessous
-- --------------------------------------------------------

-- Ajout de plusieurs utilisateurs supplémentaires (optionnel)
-- Les nouveaux id seront 3, 4, 5, 6 (après vos users 1 et 2)
INSERT INTO `users` (`email`, `password`, `name`, `role`) VALUES
('admin@foodsaver.fr', '123456', 'Admin FoodSaver', 'admin'),
('client2@test.fr', '123456', 'Sophie Martin', 'client'),
('boulanger@test.fr', '123456', 'Pierre Boulanger', 'merchant'),
('primeur@test.fr', '123456', 'Luc Primeur', 'merchant');

-- --------------------------------------------------------
-- 2. TABLE commerces (à créer si elle n'existe pas)
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `commerces` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `merchant_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `merchant_id` (`merchant_id`),
  CONSTRAINT `commerces_ibfk_1` FOREIGN KEY (`merchant_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Données commerces : merchant_id 2 = merchant@test.fr (existant), 5 = Pierre, 6 = Luc
INSERT INTO `commerces` (`name`, `merchant_id`) VALUES
('Boulangerie du Centre', 2),
('Primeur Fraîcheur', 2),
('Épicerie Bio', 2),
('Boulangerie Pierre', 5),
('Marché des Légumes', 6);

-- --------------------------------------------------------
-- 3. TABLE paniers (à créer si elle n'existe pas)
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `paniers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commerce_id` int(11) NOT NULL,
  `titre` varchar(255) NOT NULL,
  `prix` double NOT NULL,
  `quantite` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `commerce_id` (`commerce_id`),
  CONSTRAINT `paniers_ibfk_1` FOREIGN KEY (`commerce_id`) REFERENCES `commerces` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Données paniers (plusieurs par commerce)
-- Commerces 1=Boulangerie du Centre, 2=Primeur, 3=Épicerie Bio, 4=Boulangerie Pierre, 5=Marché Légumes
INSERT INTO `paniers` (`commerce_id`, `titre`, `prix`, `quantite`) VALUES
(1, 'Viennoiseries du jour', 3.50, 10),
(1, 'Pain invendu', 1.00, 5),
(1, 'Assortiment pâtisseries', 5.00, 3),
(1, 'Baguettes de la veille', 0.50, 20),
(2, 'Légumes de fin de marché', 4.00, 8),
(2, 'Fruits mûrs', 3.00, 6),
(2, 'Panier surprise légumes', 5.00, 4),
(3, 'Produits proches DLC', 6.00, 4),
(3, 'Panier surprise', 8.00, 2),
(4, 'Pains spéciaux invendus', 2.50, 7),
(4, 'Croissants du matin', 2.00, 12),
(5, 'Légumes de saison', 4.50, 9),
(5, 'Fruits de fin de journée', 3.50, 5);

-- --------------------------------------------------------
-- 4. TABLE reservations (à créer si elle n'existe pas)
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `reservations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `panier_id` int(11) NOT NULL,
  `quantite_reservee` int(11) NOT NULL,
  `date_reservation` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `panier_id` (`panier_id`),
  CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`panier_id`) REFERENCES `paniers` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exemple de réservations (client 1 = Jean Client a réservé)
INSERT INTO `reservations` (`user_id`, `panier_id`, `quantite_reservee`, `date_reservation`) VALUES
(1, 1, 2, UNIX_TIMESTAMP(NOW()) * 1000),
(1, 5, 1, UNIX_TIMESTAMP(NOW()) * 1000);

COMMIT;

-- ============================================================
-- RÉSUMÉ
-- - users : déjà existant + 4 nouveaux utilisateurs
-- - commerces : 5 commerces
-- - paniers : 14 paniers
-- - reservations : 2 réservations exemple
-- ============================================================
