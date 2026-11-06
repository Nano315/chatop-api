# ChâTop – API back-end

API REST du portail de location saisonnière **ChâTop** (projet OpenClassrooms « Modélisez et implémentez le back-end en utilisant du code Java maintenable »).

Elle remplace le mock Mockoon du [front-end Angular](https://github.com/OpenClassrooms-Student-Center/Mod-lisez-et-impl-mentez-le-back-end-en-utilisant-du-code-Java-maintenable) : inscription, connexion par token JWT, annonces de location avec photo et messages aux propriétaires.

## Technologies

- Java 25, Spring Boot 4.1 (Web MVC, Data JPA, Validation)
- Spring Security + OAuth2 Resource Server (tokens JWT signés en HS256)
- MySQL
- springdoc-openapi (documentation Swagger)

## Prérequis

- **JDK 25** (variable `JAVA_HOME` pointant dessus)
- **MySQL 8 ou plus récent**
- Maven n'est pas nécessaire : le wrapper `mvnw` est inclus
- Pour le front : Node.js 22 et le dépôt Angular ci-dessus

## 1. Configurer la base de données

Avec un compte administrateur MySQL (root), créez la base et un utilisateur dédié aux droits limités, puis les tables :

```sql
CREATE DATABASE chatop_db;
CREATE USER 'chatop_user'@'localhost' IDENTIFIED BY 'choisissez_un_mot_de_passe';
GRANT SELECT, INSERT, UPDATE, DELETE ON chatop_db.* TO 'chatop_user'@'localhost';
```

Exécutez ensuite, dans cet ordre (MySQL Workbench ou client `mysql`) :

| Script | Rôle |
|---|---|
| [`database/schema.sql`](database/schema.sql) | Crée les tables `users`, `rentals` et `messages` (en root) |
| [`database/demo-data.sql`](database/demo-data.sql) | *Optionnel.* Données de démo : 3 comptes, 8 annonces avec photos, 3 messages. Ré-exécutable à tout moment |

L'application ne crée ni ne modifie jamais les tables (`spring.jpa.hibernate.ddl-auto=validate`) : elle vérifie seulement au démarrage que les entités correspondent au schéma.

## 2. Définir les variables d'environnement

Aucun identifiant n'est écrit dans le code : l'application lit ces trois variables.

| Variable | Valeur |
|---|---|
| `DB_USERNAME` | Utilisateur MySQL (ex : `chatop_user`) |
| `DB_PASSWORD` | Son mot de passe |
| `JWT_SECRET` | Secret de signature des tokens, **32 caractères minimum** |

**Windows** : *Modifier les variables d'environnement pour votre compte* → *Nouvelle…*, puis redémarrez le terminal ou l'IDE.
Pour générer un secret aléatoire et l'enregistrer directement (PowerShell) :

```powershell
$b = New-Object byte[] 48; [Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($b); [Environment]::SetEnvironmentVariable("JWT_SECRET", [Convert]::ToBase64String($b), "User")
```

**Linux / macOS** :

```bash
export DB_USERNAME=chatop_user
export DB_PASSWORD='votre_mot_de_passe'
export JWT_SECRET=$(openssl rand -base64 48)
```

## 3. Lancer l'API

```bash
./mvnw spring-boot:run        # Linux / macOS
.\mvnw.cmd spring-boot:run    # Windows
```

L'API démarre sur **http://localhost:3001**, le port qu'utilisait Mockoon : le proxy du front Angular l'appelle sans aucune modification.

## 4. Lancer le front-end

Dans le dépôt Angular (**Mockoon doit être arrêté**, il utilise le même port) :

```bash
npm install
npm run start
```

Si `npm install` échoue avec une erreur 401 (le `package-lock.json` du dépôt pointe vers un registre privé), utilisez `npm install --no-package-lock`.

Puis ouvrez **http://localhost:4200**.

Avec les données de démo, connectez-vous avec `test@test.com` (mot de passe indiqué en tête de [`database/demo-data.sql`](database/demo-data.sql)) : ce compte possède 3 annonces modifiables.

## Documentation Swagger

Une fois l'API lancée : **http://localhost:3001/swagger-ui/index.html** (description OpenAPI brute : `/v3/api-docs`).

Pour tester les routes protégées : appelez `POST /api/auth/login` (ou `register`), copiez le `token` reçu, cliquez sur **Authorize** et collez-le.

## Routes

| Méthode | URL | Description | Codes |
|---|---|---|---|
| POST | `/api/auth/register` | Créer un compte (public) | 200, 400 |
| POST | `/api/auth/login` | Se connecter (public) | 200, 401 |
| GET | `/api/auth/me` | Utilisateur connecté | 200, 401 |
| GET | `/api/rentals` | Lister les annonces | 200, 401 |
| GET | `/api/rentals/{id}` | Détail d'une annonce | 200, 401, 404 |
| POST | `/api/rentals` | Créer une annonce (formulaire multipart avec photo) | 200, 400, 401 |
| PUT | `/api/rentals/{id}` | Modifier une annonce (propriétaire uniquement) | 200, 400, 401, 403, 404 |
| POST | `/api/messages` | Envoyer un message au propriétaire | 200, 400, 401 |
| GET | `/api/user/{id}` | Profil d'un utilisateur | 200, 401, 404 |

Toutes les routes exigent le header `Authorization: Bearer <token>`, sauf `register`, `login`, Swagger et les photos (`GET /images/**`, une balise `<img>` ne pouvant pas envoyer de token).

## Architecture

```
src/main/java/com/openclassrooms/chatop
├── configuration   Sécurité (JWT), Swagger, exposition des photos
├── controller      Points d'entrée HTTP (aucune logique métier)
├── dto             Objets échangés avec le front (les entités ne sont jamais exposées)
├── exception       Exceptions métier + GlobalExceptionHandler (codes HTTP et JSON d'erreur)
├── model           Entités JPA : User, Rental, Message
├── repository      Accès aux données (Spring Data JPA)
└── service         Logique métier : authentification, JWT, annonces, photos, messages
```

Chaque requête suit le même chemin : **Controller → Service → Repository → Entity**, avec les DTO en entrée et en sortie.

Les photos des annonces sont enregistrées dans le dossier `uploads/` (ignoré par git) et servies sur `/images/**` ; seule leur URL est stockée en base.
