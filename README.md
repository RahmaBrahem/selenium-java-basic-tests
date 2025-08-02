<h1 align="center">🧰 End-to-End UI Test Automation – Selenium Java TestNG</h1>
<p align="center"><i>Framework de test avec ExtentReports, capture vidéo et gestion des cas de test</i></p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white" />
  <img src="https://img.shields.io/badge/TestNG-F2A100?style=for-the-badge&logo=testng&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" />
  <img src="https://img.shields.io/badge/ExtentReports-blueviolet?style=for-the-badge&logo=report&logoColor=white" />
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" />
  <img src="https://img.shields.io/badge/build-passing-brightgreen?style=for-the-badge&logo=github" />
  <img src="https://img.shields.io/badge/Version-1.0-blue?logoColor=rgb(0%2C%200%2C%20255)&label=Version" />
</p>

## 🚀 À propos

Ce projet est une première exploration de l'automatisation des tests UI avec Java et Selenium.  
Le code est simple et regroupe tous les tests dans une seule classe (STSauceLabs), sans architecture avancée ni design pattern.
Il inclut l’enregistrement vidéo des tests via ATUTestRecorder et des rapports détaillés avec ExtentReports.

## 🧱 Structure du projet

Le projet est organisé de manière basique :

📁 **selenium-java-basic-tests/**  
├── 📁 **src/test/java/**  
│ └── 📦 **com.lineairecode.extent**  
│  └── 🧪 **STSauceLabs.java** → Classe de test principale  
├── 📁 **src/test/resources/**  
│ └── 📁 **Data/**  
│  └── 📄 **data.properties** → Fichier de configuration (non versionné Git)  
└── 📄 **pom.xml** → Fichier Maven avec les dépendances  

## ✅ Scénarios testés

- Connexion avec identifiants invalides  
- Connexion avec utilisateur verrouillé (locked out)  
- Connexion avec identifiants valides 
- Filtrage des produits par prix (low to high) 
- Ajout au panier  
- Visualisation du panier  
- Suppression d’articles du panier  
- Passage au paiement (checkout)    
- Déconnexion  

## 🔧 Technologies utilisées

- **Java 21 (OpenJDK)**
- **Selenium WebDriver**
- **TestNG**
- **Maven**
- **WebDriverManager** (gestion automatique des drivers)
- **ExtentReports** (rapports HTML avec captures et liens vidéos)
- **ATUTestRecorder** (enregistrement vidéo des tests)
- Fichier `.properties` pour la configuration

## ⚙️ Configuration locale

Le fichier `data.properties` contient toutes les données de configuration nécessaires au projet (URL, identifiants, navigateur, etc.).  
🔒 Ce fichier est **ignoré par Git** (`.gitignore`) pour protéger les informations sensibles.  
📁 Un **exemple de configuration** est disponible ici : [`properties-example.txt`](src/test/resources/Data/properties-example.txt)

### 🛠️ Pour configurer le projet localement :

1. **Créer** le dossier `Data` dans `src/test/resources/` s’il n’existe pas   
2. **Copier** le fichier `properties-example.txt` dans ce dossier  
3. **Renommer** ce fichier en `data.properties`  
4. **Modifier** les valeurs selon votre environnement (`baseUrl`, `First_Name`, `Last_Name`, `Postal_Code`, etc.)  

## ▶️ Exécution des Tests

### Avec Maven (ligne de commande) :

```bash
mvn clean test
```

### Avec Eclipse :

* Ouvrez le projet
* Faites un clic droit sur la classe `STSauceLabs` > Run As > TestNG Test

## 📊 Résultats et Rapports

Les résultats sont générés dans des dossiers dédiés, avec des fichiers nommés dynamiquement par date, heure, et nom de test.
  - Format date/heure : `yy_MM_dd_HH_mm_ss` (géré via la méthode `getActualDateTime()`)  

#### 📁 Rapport HTML
- 📂 Emplacement : `target/Spark/`
- 📝 Exemple : `SparkReport_25_07_26_11_13_46.html`

#### 📸 Captures d’écran
- 📂 Emplacement : `target/Screenshots/`
- 📝 Exemple : `Image_checkOutTest_25_07_30_18_18_22.png`

#### 🎥 Vidéos de test 
- 📂 Emplacement : `target/Videos/`
- 📝 Exemple : `Video_addToCartTest_25_07_16_10_38_01.mov`

## 📈 Évolutions prévues

Ce projet est une version basique d’automatisation UI, sans architecture complexe.  
Il sert de tremplin pour mes futurs projets plus avancés intégrant :
   - Modularisation du code (Page Object Model)
   - Gestion plus fine des erreurs et exceptions
   - Intégration de tests API

## ✍️ Auteur

  <strong>Rahma Brahem</strong><br>
  <em>QA Engineer | Passionnée par l'automatisation</em><br>
  📎 <a href="https://github.com/RahmaBrahem">GitHub</a> |
  <a href="https://www.linkedin.com/in/rahmabrahemqa">LinkedIn</a> 

## 📜 Licence

Ce projet est publié sous licence [MIT](https://opensource.org/licenses/MIT).  
Voir le fichier [LICENSE](./LICENSE) pour plus de détails.