# FoodSaver - Application Android Anti-Gaspillage Alimentaire

Application mobile Android (Java + XML) pour réserver des paniers alimentaires et gérer les offres commerçants, dans le cadre de la réduction du gaspillage alimentaire.

## Ouvrir le projet dans Android Studio

1. Ouvrir **Android Studio**
2. **File → Open** (ou "Open an Existing Project")
3. Sélectionner le dossier **FoodSaver_Android** (ou `e:\FoodSaver_Android`)
4. Laisser Android Studio synchroniser le projet Gradle (téléchargement des dépendances si nécessaire)
5. Lancer l’application sur un émulateur ou un appareil : **Run → Run 'app'** (ou bouton vert Play)

## Prérequis

- **Android Studio** (Arctic Fox / Hedgehog ou plus récent recommandé)
- **JDK 17**
- **SDK Android** : compileSdk 34, minSdk 24

## Version 1 – Fonctionnalités implémentées

### Authentification locale
- Inscription et connexion
- Rôles **Client** / **Commerçant**
- Session avec **SharedPreferences**

### Côté Client
- Liste des commerces
- Paniers disponibles par commerce
- Réserver un panier (choix de la quantité)
- Historique des réservations
- Annulation d’une réservation

### Côté Commerçant
- Création d’un commerce (à l’inscription + ajout manuel)
- Ajout d’un panier (titre, prix, quantité)
- Modification et suppression d’un panier
- Consultation des réservations reçues par commerce

### Base de données locale (Room)
- **User** : id, email, password, name, role
- **Commerce** : id, name, merchantId
- **Panier** : id, commerceId, titre, prix, quantite
- **Reservation** : id, userId, panierId, quantiteReservee, dateReservation

Relations : User (1)→(N) Reservation, Commerce (1)→(N) Panier, Panier (1)→(N) Reservation.

## Structure du projet

- `app/src/main/java/com/foodsaver/android/`
  - `data/` : entités Room, DAOs, AppDatabase, repositories
  - `auth/` : SessionManager
  - `ui/` : MainActivity, auth (Login, Register), client (ClientHome, Paniers, History), merchant (MerchantHome, MerchantPaniers, EditPanier, Reservations)
- `app/src/main/res/` : layouts XML, menus, strings, thèmes Material

## Version 2 (à venir)

Prévue pour : authentification Firebase, synchronisation cloud (Firestore + Room), paiement, notifications, rôle Admin.
