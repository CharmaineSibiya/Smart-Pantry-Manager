# Smart Pantry Manager

**Mobile App Development 700 – Practical Assignment**

## App Description

Smart Pantry Manager helps users reduce food waste by tracking the ingredients they actually have at home (their “pantry”) and suggesting recipes they can cook using **strictly** those leftover ingredients. No shopping trip required, and no recipe is suggested unless the user genuinely already has everything it needs.

### Core Features

- **Pantry management** – Add, edit, and delete pantry items (name, quantity, unit, optional expiry date)
- **Pantry list** – RecyclerView bound to SQLite database
- **Recipe collection** – 20 pre-seeded recipes stored in the database
- **Suggested Recipes** – Strict-matching logic: a recipe appears only if *every* required ingredient is present in sufficient quantity
- **Recipe detail** – Full ingredient list and preparation method
- **Settings** – Toggles for expiring-soon alerts and metric preference
- **Empty-state feedback** – Clear message when no recipes match

### Strict-Matching Rule

A recipe is shown as “suggested” only when:

1. Every ingredient it requires exists in the user’s pantry
2. The available quantity is ≥ the required quantity (with simple unit conversion: g/kg, ml/L, cups, tbsp, tsp)
3. Matching is robust to singular/plural forms (e.g. “tomato” ↔ “tomatoes”)

Partial matches are **excluded**.

## Database Choice: SQLite

**Chosen option:** SQLite (via `SQLiteOpenHelper`)

**Why SQLite?**

- Fully on-device, no internet required
- Perfect for a personal pantry app (data stays private and local)
- Matches the persistent-data chapter of the module
- Simple, reliable CRUD with no external dependencies or API keys
- Data survives app close/reopen

Tables:

- `pantry` – user ingredients
- `recipes` – recipe name + method
- `recipe_ingredients` – ingredients required by each recipe

## Screens (5+)

1. **MainActivity** – Host with Bottom Navigation
2. **PantryFragment** – List of ingredients + FAB to add
3. **AddEditIngredientActivity** – Create / Update / Delete with validation
4. **RecipesFragment** – Strict-matched suggested recipes
5. **RecipeDetailActivity** – Full recipe view
6. **SettingsFragment** – Preferences and about

## Technical Stack

- **Language:** Java (not Kotlin)
- **Min SDK:** 24 | **Target SDK:** 34
- **UI:** Material Components, ConstraintLayout, RecyclerView, BottomNavigationView
- **Database:** SQLiteOpenHelper
- **Navigation:** Intents + Bottom Navigation + Fragments

## Setup & Run Instructions

1. Open **Android Studio** (Hedgehog / Iguana or newer recommended)
2. **File → Open** and select the `SmartPantryManager` folder
3. Wait for Gradle sync to finish (first sync may take a few minutes)
4. If prompted, accept SDK licenses and install any missing platforms
5. Create/start an emulator (API 24+) or connect a physical device
6. Click **Run** (green play button) or press `Shift+F10`

### First Launch

- 20 recipes are automatically seeded into the database
- Pantry starts empty – use the **+** button to add ingredients
- Switch to **Suggested Recipes** to see which recipes you can make

### Testing the Strict-Matching Rule

1. Add: 3 eggs, 15 g butter, 1 tsp salt → “Scrambled Eggs” appears
2. Delete butter → “Scrambled Eggs” disappears from suggestions
3. Close and reopen the app – pantry data is still there

## Project Structure

```
app/src/main/java/com/smartpantry/manager/
├── activities/
│   ├── MainActivity.java
│   ├── AddEditIngredientActivity.java
│   ├── RecipeDetailActivity.java
│   └── SettingsActivity.java
├── fragments/
│   ├── PantryFragment.java
│   ├── RecipesFragment.java
│   └── SettingsFragment.java
├── adapters/
│   ├── IngredientAdapter.java
│   └── RecipeAdapter.java
├── database/
│   └── DatabaseHelper.java      ← CRUD + strict matching + seed data
└── models/
    ├── Ingredient.java
    └── Recipe.java
```


## Author

Chairmaine Sibiya (402203071)
