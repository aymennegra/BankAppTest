# 🏦 Bank App - Test Mobile Crédit Agricole

Application bancaire mobile développée en **Kotlin Multiplatform (KMP)** avec architecture **MVVM Clean** pour Android.

---

## 📱 Fonctionnalités

### ✅ Écran 1 : Liste des Comptes
- Séparation des comptes Crédit Agricole et autres banques
- Cellules bancaires dépliables (expand/collapse)
- Tri alphabétique des banques
- Affichage du solde de chaque compte
- Navigation vers les opérations

### ✅ Écran 2 : Liste des Opérations
- Affichage des opérations d'un compte
- Tri par date (plus récent en haut)
- Tri alphabétique si même date
- Catégorisation des opérations (loisirs, alimentation, etc.)
- Bouton retour

---


### 🏗️ Structure du Projet

```
shared/                    # Code partagé KMP
├── domain/               # Logique métier
│   ├── model/           # Entités métier
│   ├── repository/      # Interfaces
│   └── usecase/         # Cas d'usage
├── data/                # Sources de données
│   ├── remote/          # API avec Ktor
│   ├── mapper/          # DTO → Domain
│   └── repository/      # Implémentation
└── presentation/        # ViewModels

androidApp/               # Application Android
├── ui/
│   ├── screens/         # Écrans Compose
│   ├── navigation/      # Navigation
│   └── theme/           # Thème Material 3
├── MainActivity.kt
└── BankApplication.kt
```

---

## 🛠️ Technologies & Versions

### Versions
```
Kotlin              1.9.20
Android Gradle      8.2.2
Compose             1.5.4
Material 3          1.1.2
Ktor                2.3.7
Coroutines          1.7.3
Koin                3.5.3
```

### Stack Technique

**Shared (KMP)**
- Kotlin Multiplatform
- Ktor Client (HTTP/JSON)
- Kotlinx Serialization
- Coroutines + Flow
- Koin (Dependency Injection)

**Android**
- Jetpack Compose
- Material Design 3
- Navigation Compose
- Lifecycle ViewModel
- Koin Android



---

## 🚀 Installation

### Prérequis
```
✅ Android Studio 
✅ JDK 17
✅ Kotlin 1.9.20+

Tests

JUnit 4
MockK
Turbine (Flow testing)
Kotest (Assertions)
Coroutines Test

Fichiers de test

BankMapperTest.kt (10 tests)
GetBanksUseCaseTest.kt (3 tests)
BankRepositoryImplTest.kt (4 tests)

## 📋 Règles Métier Implémentées

| Code | Description | Statut |
|------|-------------|--------|
| **RG00** | Séparation Crédit Agricole / Autres banques | ✅ |
| **RG01** | Cellules bancaires dépliables | ✅ |
| **RG02** | Deux sections distinctes | ✅ |
| **RG03** | Tri alphabétique des banques | ✅ |
| **RG04** | Affichage des comptes au dépliage | ✅ |
| **RG05** | Tri des opérations par date décroissante | ✅ |
| **RG06** | Tri alphabétique si même date | ✅ |
| **RG07** | Navigation retour | ✅ |





