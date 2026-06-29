# BadWallet API - Examen Design Pattern (L3 S2 2026)

## Auteur
Nom : [Thiombane]
Prénom : [Mouhamadou AlAmine]
Classe : L3 S2 2026

## Description
API de gestion de portefeuille électronique (BadWallet) composée de deux
micro-services Spring Boot :

| Service | Port | Rôle |
|---|---|---|
| `badwallet-api` | 8080 | API principale : portefeuilles, transactions, dépôts, retraits, transferts, paiements |
| `payment-service` | 8081 | Service externe simulant la facturation (ISM, WOYAFAL) |

## Design Patterns implémentés

| # | Pattern | Où | Pourquoi |
|---|---|---|---|
| 1 | **Strategy** | `wallet/service/strategy` | Choisir dynamiquement la méthode de dépôt (CREDIT_CARD, WALLET_TARGET) |
| 2 | **Factory** | `wallet/service/factory/TransactionFactory` | Centraliser la création des différents types de `Transaction` |
| 3 | **Proxy** | `payment/client/PaymentServiceClientImpl` | Représenter localement le micro-service externe `payment-service` |
| 4 | **Template Method** | `wallet/service/fees/FeeCalculator` | Algorithme fixe de calcul de frais (% plafonné), variantes retrait/transfert |
| 5 | **Observer** | `shared/events/TransactionEvent` + listeners | Notifier de façon asynchrone après chaque transaction réussie |
| 6 | **Builder** | `shared/response/ApiResponse` (Lombok `@Builder`) | Construire les réponses API de façon fluide |
| 7 | **Adapter** | `payment/adapter/PaymentServiceAdapter` | Convertir le modèle `Facture` du service externe en DTO interne |
| 8 | **Command** | `wallet/command/*` | Encapsuler dépôt / retrait / transfert en objets exécutables via un `Invoker` |

## Endpoints principaux
Voir `docs/API_CONTRACT.md` et le fichier `examen-design-pattern.http`.

## Comment lancer le projet

```bash
# Terminal 1 : démarrer le service externe de facturation (port 8081)
cd payment-service
mvn spring-boot:run

# Terminal 2 : démarrer l'API principale (port 8080)
cd badwallet-api
mvn spring-boot:run
```

Swagger : http://localhost:8080/swagger-ui.html
Console H2 (badwallet-api) : http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:badwallet`)
Console H2 (payment-service) : http://localhost:8081/h2-console (JDBC URL `jdbc:h2:mem:paymentservice`)

## Lancer les tests

```bash
cd badwallet-api
mvn test
```

## Stratégie Git (Feature Branching)

```
main (production)
  └── develop (intégration)
       ├── feature/wallet-seeder
       ├── feature/wallet-creation
       ├── feature/wallet-listing
       ├── feature/wallet-consultation
       ├── feature/transaction-deposit
       ├── feature/transaction-withdraw
       ├── feature/transaction-transfer
       ├── feature/payment-services
       ├── feature/transaction-history
       └── feature/proxy-factures
```

Workflow type :

```bash
git checkout -b develop
git checkout develop
git checkout -b feature/wallet-seeder
# ... coder, commiter ...
git push origin feature/wallet-seeder
# Pull Request -> revue de code -> merge dans develop
```

Détail complet dans `docs/STRATEGIE_GIT.md`.

## Livrable
Lien du dépôt GitHub à envoyer à `douvewane85@gmail.com`
Objet : `Examen de Design Pattern L3 S2 2026 - Nom et Prénom - Classe`
