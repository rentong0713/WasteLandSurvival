**Feedback** - Overall looks good. Just make sure each of the six concrete classes has its own meaningful, complex effect.

# Custom Feature Proposal: Tactical Trap System

## The Pitch
The Tactical Trap System introduces strategically deployed ground-based hazards into the game map that autonomously intercept and neutralize advancing entities. When an actor (player or monster) steps directly onto a trap item, it triggers a localized, multi-tile elemental explosion that dynamically alters the terrain and infects all surrounding targets with severe status debuffs. This system adds a deep layer of tactical environmental hazard control, rewarding careful map positioning, baiting strategies, and movement planning.

---

## The Mechanics

### ⚙️ Spawning and Triggering
Traps are spawned directly on map coordinates during world initialization (e.g., right below the player spawn point for streamlined testing). They behave like un-pickable items sitting on the ground layer, actively scanning their coordinates every engine tick. The moment any actor—whether it is an NPC creature or a `ContractedWorker` like Bob or Tom—steps onto their specific tile, the proximity sensor trips.

### 💥 The Blast Radius
Upon detonation, the trap instantly constructs a `TrapExplosionAction`. This action calculates a $3 \times 3$ grid centered around the detonation epicenter, checking boundaries safely against the margins of the `GameMap` ranges before deploying its payloads. Once processed, the original trap item is cleanly purged from the location's item inventory list.

### 🌍 Environmental Transformation
Ground tiles caught within a `FireMine` blast radius permanently alter their physical state. Standard safe facility `Floor` tiles (`_`) intercept the blast wave and swap themselves on the map grid for a hazardous, custom `ScorchedEarth` terrain layer (`⌂`).

### 🤢 Dynamic Status Infliction
Any actor caught within the $3 \times 3$ blast radius of a detonated trap is instantly inflicted with a status effect tailored to that specific weapon archetype:
* **Fire Mine (`☼`):** Deals 10 instant damage and applies a `BurnStatus` (3 turns, 2 damage/turn).
* **Gas Mine (`☁`):** Deals 5 instant damage and applies a `PoisonStatus` (5 turns, 1 damage/turn).
* **Stun Mine (`⚡`):** Deals 0 physical damage but applies a `StunStatus` (2 turns) that completely paralyzes the victim's control loops.

---

## The Architecture

### New Interfaces Created
* **`Detonatable`**: Implemented by items acting as proximity explosives that constantly check for actor intersection during the engine's item tick cycle to execute a map-wide explosion.
* **`TrapTriggerable`**: Implemented by game world components (Ground or Actor classes) capable of intercepting and reacting polymorphically to blast wave payloads.

---

### Concrete Classes Breakdown

#### 1. New Concrete Classes Created for this Feature
* **`TrapExplosionAction`** *(Extends `Action`)*: The central operational bridge that calculates the $3 \times 3$ grid coordinates, queries target tiles, purges the spent mine, and handles the standalone execution loop for both terrain and creature layers.
* **`FireMine`** *(Extends `Item`, Implements `Detonatable`)*: A proximity hazard item that initializes a fiery explosion sequence when an actor ticks on its location.
* **`GasMine`** *(Extends `Item`, Implements `Detonatable`)*: A tactical hazard item that releases toxic chemical gas across the blast area when tripped, dealing initial impact damage and lingering poison.
* **`StunMine`** *(Extends `Item`, Implements `Detonatable`)*: A mechanical disruption hazard item that emits a powerful paralyzing shockwave to freeze surrounding entities completely in place.
* **`ScorchedEarth`** *(Extends `Ground`, Implements `TrapTriggerable`)*: A hazardous terrain subclass represented by the `⌂` display symbol. It wraps an internal compositional `Fire` tracking layer that deals passive damage to any actor standing on it and dynamically logs terrain-specific damage messages.

#### 2. Retrofitted Classes (Existing Engine Classes Modified)
* **`Floor`** *(Extends `Ground`, Implements `TrapTriggerable`)*: Retrofitted to handle explosions via `.reactToTrap()`, enabling tiles to dynamically replace themselves on the map grid with permanent `ScorchedEarth` when exposed to a `FireMine`.
* **`ContractedWorker`** *(Extends `Actor`)*: Retrofitted to intercept active status capability tags inside its core `playTurn` loop. If an active `Status.STUN` capability is detected, it completely short-circuits the menu selection interface, displays a paralyzing alert, and automatically returns a `DoNothingAction()`.
* **`NPC`** *(Abstract Base Monster Class)*: Retrofitted to implement `TrapTriggerable` so all enemies (like Slimes and Undead) collectively process trap payloads, take ongoing damage, and check for the `Status.STUN` capability to halt their automated tracking and wandering AI behaviors.