# Base de données MySQL – foodsaver_db

## Connexion depuis l’app

- **Inscription** : les nouveaux comptes sont enregistrés dans la table **users** de MySQL via l’API (backend Spring Boot).
- **Connexion** : vérification dans la table **users** de MySQL.

Assurez-vous que le backend tourne et que l’URL dans `ApiConfig.java` pointe vers votre serveur.

## Exécuter le script SQL

1. Ouvrez **phpMyAdmin** (http://127.0.0.1/phpmyadmin ou votre URL).
2. Sélectionnez la base **foodsaver_db**.
3. Onglet **SQL**, collez le contenu de **foodsaver_db_complete.sql**.
4. Cliquez sur **Exécuter**.

Le script :

- **Ne modifie pas** la table `users` existante (il peut ajouter des lignes si vous le souhaitez).
- **Crée** les tables `commerces`, `paniers`, `reservations` si elles n’existent pas.
- **Insère** des données d’exemple (utilisateurs, commerces, paniers, réservations).

Après exécution, vous aurez une structure complète et des données pour tester l’application.
