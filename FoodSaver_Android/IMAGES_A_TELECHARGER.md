# Images à télécharger pour FoodSaver

À placer dans **`app/src/main/res/drawable/`**.

Règles Android : **noms en minuscules, chiffres et underscores uniquement** (pas d’espaces, pas d’accents).  
Formats acceptés : **.png**, **.jpg** ou **.webp**.

---

## 1. Images des paniers (obligatoires pour l’affichage des paniers)

Utilisées dans la liste des paniers par commerce et en détail.  
**Nom exact du fichier** = nom à utiliser (sans extension dans le tableau, mais le fichier doit avoir l’extension).

| À télécharger (sujet) | Nom exact du fichier |
|----------------------|----------------------|
| Photo type Tagine / plat marocain | `panier_tagine.png` |
| Photo type Pizza | `panier_pizza.png` |
| Photo type Sandwich / wrap | `panier_sandwich.png` |
| Photo type Petit-déjeuner / viennoiseries | `panier_petit_dejeuner.png` |
| Photo type Salade / bowl | `panier_salade.png` |
| Photo type “autre” / plat générique | `panier_autre.png` |
| Image par défaut (repas / panier générique) | `placeholder_food.png` (optionnel : un placeholder existe déjà en .xml) |

**Noms à respecter à la lettre :**
- `panier_tagine`
- `panier_pizza`
- `panier_sandwich`
- `panier_petit_dejeuner`
- `panier_salade`
- `panier_autre`
- `placeholder_food` — seulement si vous voulez une image photo à la place du placeholder actuel (.xml)

Vous pouvez renommer vos fichiers après téléchargement pour qu’ils correspondent exactement à ces noms.

---

## 2. Où trouver des images libres de droits

- **Unsplash** : https://unsplash.com (rechercher "food", "tagine", "pizza", "sandwich", "breakfast", "salad")
- **Pexels** : https://www.pexels.com
- **Pixabay** : https://pixabay.com

Téléchargez en bonne résolution (ex. 800×600 px ou plus), puis renommez comme dans le tableau et copiez dans **`drawable/`**.

---

## 3. Récapitulatif des fichiers à avoir dans `drawable/`

```
app/src/main/res/drawable/
├── panier_tagine.png      (ou .jpg / .webp)
├── panier_pizza.png
├── panier_sandwich.png
├── panier_petit_dejeuner.png
├── panier_salade.png
├── panier_autre.png
└── placeholder_food.png   (optionnel : le projet a déjà placeholder_food.xml)
```

Les icônes (flèche, panier, carte, etc.) sont déjà en **vector drawable** (.xml) dans le projet ; vous n’avez pas besoin de les télécharger.

---

## 4. Vérification

Après avoir ajouté les fichiers :

1. **Build** du projet (Build → Make Project).
2. Si une erreur apparaît sur un nom de drawable, vérifiez qu’il n’y a ni majuscule, ni espace, ni accent dans le nom du fichier.

Exemple de noms **incorrects** :  
`Panier_Tagine.png`, `panier tagine.png`, `panier-tagine.png`  
Exemple **correct** :  
`panier_tagine.png`
