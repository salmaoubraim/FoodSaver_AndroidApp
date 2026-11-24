# Résoudre "Gradle project sync failed"

## 1. Connectez-vous à Internet

Gradle doit télécharger des dépendances. Si le Wi‑Fi est **déconnecté** ("Non connecté"), la synchronisation échoue.

- Connectez le Wi‑Fi (ou le câble).
- Dans la barre jaune en haut, cliquez sur **"Try Again"** (Réessayer).

## 2. Vérifier Java (JDK)

Le projet utilise **JDK 11** (ou supérieur).

- Dans Cursor/VS Code : **File → Preferences → Settings**, chercher `java.home` ou `java.configuration.runtime`.
- Ou en ligne de commande : `java -version` (doit afficher 11 ou 17).

Si Java 11+ est installé avec Android Studio, `JAVA_HOME` pointe souvent vers le JDK d’Android Studio, par exemple :
`C:\Program Files\Android\Android Studio\jbr`

## 3. Ouvrir le projet dans Android Studio (recommandé)

Pour compiler et lancer l’app Android, le plus fiable est d’utiliser **Android Studio** :

1. Ouvrez **Android Studio**.
2. **File → Open** → sélectionnez le dossier **`FoodSaver_Android`**.
3. Attendez la fin de la synchronisation Gradle (avec Internet activé).
4. Le bouton **Run** vert apparaît ; vous pouvez lancer l’app.

Android Studio gère Gradle et le wrapper pour vous.

## 4. Voir le détail de l’erreur

Pour comprendre pourquoi la sync échoue :

- Cliquez sur **"Open 'Build' View"** ou **"Show Log"** dans le message d’erreur Gradle.
- Ou ouvrez l’onglet **Build** ou **Sync** en bas de l’IDE.

Les messages indiquent par exemple : problème de réseau, JDK manquant, ou chemin du SDK Android incorrect.

## 5. Vérifier le SDK Android

Le fichier **`local.properties`** doit contenir le chemin du SDK Android, par exemple :

```properties
sdk.dir=C\:\\Users\\VotreUser\\AppData\\Local\\Android\\Sdk
```

Si vous avez installé Android Studio, le SDK est en général dans  
`C:\Users\VotreNom\AppData\Local\Android\Sdk`.  
Ajustez `local.properties` si besoin.

---

En résumé : **connectez le Wi‑Fi**, cliquez sur **Try Again**, et si ça échoue encore, ouvrez le projet dans **Android Studio** pour faire la sync et lancer l’app.
