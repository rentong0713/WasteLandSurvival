**Feedback** - Overall looks good. Just make sure each of the six concrete classes has its own meaningful, complex effect.

Feature Proposal

REQ3 — The Weapon Dealer

The Pitch

A new NPC called the Weapon Dealer wanders Moon 99-Deprecated and sells three distinct
weapons to contracted workers. Each weapon introduces a unique combat mechanic that
goes far beyond simple damage: chain lightning that punishes clustered enemies, a frost
blade that permanently mutates terrain into a freezing hazard, and a long-range parasite rifle
that lets workers eliminate Parasites from a safe distance before they can infect anyone,
leaving a poison ground tile where the Parasite died.

The Mechanics

Weapon Dealer NPC

● A new NPC (WeaponDealer) wanders the map.

● When a worker stands adjacent to it, three BuyWeaponAction options appear in the
menu.

● The Weapon Dealer cannot be attacked and does not fight back.

Weapon 1 — Electric Rod

● Deals 5 HP to the primary target.

● On hit, chain lightning arcs to every actor adjacent to the wielder, dealing 3 AoE
damage each.

● The arc does not bounce back to the wielder.

● Any secondary victim killed by the arc is immediately removed from the map.

Weapon 2 — Frost Blade

● Deals 6 HP to the primary target.

● On hit, the target's tile is replaced with an IceTile ground that lasts 5 turns.

● Any actor standing on the IceTile each turn takes 1 cold damage and receives a frostbite PoisonStatus (1 dmg/turn for 3 turns).

● After 5 turns the ice melts and the original ground is restored.

Weapon 3 — Parasite Rifle

● Scans the entire row of the wielder (left or right direction chosen by player).

● Hits the first Parasite found in that row — workers are never targeted.

● Instantly kills the Parasite on hit.

● The Parasite's tile is replaced with a PoisonGround tile that lasts 6 turns.

● Any actor who steps onto PoisonGround receives a PoisonStatus (2 dmg/turn for 3
turns).

● If no Parasite exists in the row, the shot misses and a 15 turn cooldown applies.
General Weapon Loop

● All three weapons are Wieldable items that sit in the worker's inventory after
purchase.

● A worker carrying a Wieldable sees a WieldAttackAction in their menu when a valid
target is adjacent or in row.

● After the attack lands,the weapon's Weapon Effect apply automatically.
The Architecture

New Abstractions

● Interface: Wieldable (package: game. Interfaces)
● Abstract Class: WeaponEffect (package: game.weapons)

Six Concrete Classes

All six are entirely new classes. No existing class is retrofitted for this requirement.
1. ElectricRod — NEW — implements Wieldable
   ○ On hit, iterates all exits of the wielder's tile and deals 3 AoE damage to every
   adjacent actor.
2. FrostBlade — NEW — implements Wieldable
   ○ On hit, it calls setGround(IceTile) on the target's tile, mutating the terrain.
3. ParasiteRifle — NEW — implements Wieldable
   ○ Scans the wielder's full row, removes the first Parasite found, then calls
   setGround(PoisonGround) on its tile.
4. ChainLightningEffect — NEW — extends WeaponEffect
   ○ Loops over all exits of the wielder's location, calls hurt() on each adjacent
   actor, and calls unconscious() on any that die.
5. FrostEffect — NEW — extends WeaponEffect
   ○ Replaces the target's ground with a new IceTile; the tile's tick() applies
   PoisonStatus and cold damage every turn until it melts.
6. ParasiteBlastEffect — NEW — extends WeaponEffect
   ○ Removes the Parasite actor from the map and replaces its tile with
   PoisonGround; the tile's tick() applies PoisonStatus to any future visitor.

Additional Supporting Classes (not counted in the 6)

● WeaponDealer — new NPC that wanders and exposes BuyWeaponAction to adjacent workers

● IceTile — new Ground subclass; applies cold damage and frostbite PoisonStatus each tick, then restores original ground

● PoisonGround — new Ground subclass; applies PoisonStatus to any actor on the tile each tick, then restores original ground

● WieldAttackAction — new Action; executes the primary hit

● BuyWeaponAction — new Action; deducts credits and adds the chosen weapon to the worker's inventory