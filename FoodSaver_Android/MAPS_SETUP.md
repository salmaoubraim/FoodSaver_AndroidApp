# Configuration Google Maps

Pour que la carte fonctionne dans l’app, il faut une **clé API Google Maps**.

1. Allez sur [Google Cloud Console](https://console.cloud.google.com/).
2. Créez un projet ou sélectionnez-en un.
3. Activez **Maps SDK for Android** (APIs & Services > Library > "Maps SDK for Android").
4. Créez une clé API : APIs & Services > Credentials > Create Credentials > API key.
5. Dans **AndroidManifest.xml**, remplacez la valeur actuelle :
   - Cherchez : `android:value="VOTRE_CLE_API_GOOGLE_MAPS"`.
   - Remplacez par votre clé, par exemple : `android:value="AIza..."`.
6. (Recommandé) Restreignez la clé à votre app : dans la console, limitez la clé à "Maps SDK for Android" et à votre nom de package + empreinte SHA-1.

Sans clé valide, l’écran carte affichera un message d’erreur à la place de la carte.
