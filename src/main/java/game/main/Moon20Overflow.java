package game.main;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.GroundCreator;
import game.flora.*;
import game.grounds.AluminiumDoor;
import game.grounds.Hole;
import game.grounds.IronDoor;
import game.grounds.MagicCircle;
import game.grounds.TitaniumDoor;
import game.grounds.Vent;
import game.interfaces.Spawner;
import game.items.AlienCube;
import game.items.CRTMonitor;
import game.items.Lantern;
import game.systems.MultiSpawner;
import game.systems.ParasiteSpawner;
import game.systems.SlimeSpawner;
import game.systems.UndeadSpawner;
import game.systems.SnatcherSpawner;

import java.util.Arrays;
import java.util.List;

/**
 * The 20-Overflow moon map.
 * <p>
 * Sets up the layout, then drops in the static stuff (alien cubes, magic circles,
 * doors, a CRT monitor, a lantern), the REQ3 flora (Fleshy Sprout and Warper
 * Sapling), and the REQ4 hazards (a Hole spawning Undead + Parasites, and a Vent
 * spawning Parasites + Slimes).
 *
 * @author Rachel Chiew
 * @version 3.0
 */
public class Moon20Overflow extends GameMap {

    /**
     * Build the 20-Overflow map and fill it with items, flora, and hazards.
     *
     * @param groundCreator the shared ground-character registry built by EclipseNebula.
     * @throws Exception if the underlying GameMap can't be built.
     */
    public Moon20Overflow(GroundCreator groundCreator) throws Exception{
        super("20-Overflow", groundCreator, createMapLayout());

        this.spawnMoonItems();
        this.spawnHazards();
        this.spawnFlora();
    }

    /**
     * The raw character grid for this map.
     *
     * @return the list of row strings.
     */
    private static List<String> createMapLayout(){
        return Arrays.asList(
                ".....................≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈",
                "...#######...........≈≈≈≈≈≈≈≈≈≈≈≈≈≈##################≈≈≈≈≈≈≈",
                "...#≡____#...........≈≈≈≈≈≈≈≈≈≈≈≈≈≈#________________#≈≈≈≈≈≈≈",
                "...#__Φ__=...........≈≈≈≈≈≈≈≈#######_______◈________#≈≈≈≈≈≈≈",
                "...#_____#...........≈≈≈≈≈≈≈≈#_____=________________#≈≈≈≈≈≈≈",
                "...#######...≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈#_◎___###########=######≈≈≈≈≈≈≈",
                ".............≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈#_____#≈≈≈≈≈≈≈≈≈#______#≈≈≈≈≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈#########=#####≈≈≈≈≈≈≈≈≈#______#≈≈≈≈≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈#_____________#≈≈≈≈≈≈≈≈≈#___◎__#≈≈≈≈≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈#______o______#≈≈≈≈≈≈≈≈≈#______#≈≈≈≈≈≈≈",
                ".............≈≈≈≈≈≈≈≈######=########≈≈≈≈≈≈≈≈≈####=###≈≈≈≈≈≈≈",
                "...≈≈≈≈≈≈≈≈≈.≈≈≈≈≈≈≈≈≈≈≈≈≈#_#≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈#_#≈≈≈≈≈≈≈≈≈",
                "...≈≈≈≈≈≈≈≈≈.≈≈≈≈≈≈≈≈≈≈≈≈≈#_#≈≈≈≈≈###############_#######≈≈≈",
                ".............≈≈≈≈≈≈≈≈≈≈≈≈≈#_____________________________#≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈≈≈≈≈≈#_______=__________◈__≈≈≈≈____#≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈≈≈≈≈≈#___◎___#_____________≈≈≈≈≈≈__≈≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈≈≈≈≈≈######################≈≈≈≈≈≈≈≈≈≈≈≈",
                ".............≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈",
                ".....................≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈",
                ".....................≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈"
        );
    }

    /**
     * Place the static stuff on the map: alien cubes, magic circles, a CRT
     * monitor, a lantern, and the three door tiers (Aluminium, Iron, Titanium).
     */
    private void spawnMoonItems(){
        this.at(2, 9).addItem(new AlienCube());
        this.at(5, 2).addItem(new AlienCube());
        this.at(10, 2).setGround(new MagicCircle());
        this.at(4, 4).setGround(new MagicCircle());
        this.at(6, 4).setGround(new MagicCircle());
        this.at(8, 10).addItem(new CRTMonitor());
        this.at(5, 4).addItem(new Lantern());
        this.at(12, 3).setGround(new AluminiumDoor());
        this.at(12, 4).setGround(new IronDoor());
        this.at(12, 5).setGround(new TitaniumDoor());
    }

    /**
     * Drop in the REQ3 alien flora: a Fleshy Sprout and a Warper Sapling.
     */
    private void spawnFlora() {
        this.at(13, 0).setGround(new FleshySprout());

        this.at(11, 0).setGround(new WarperSapling());
    }

    /**
     * Drop in the REQ4 hazards:
     * <ul>
     *   <li>A fast Hole at (30, 14) that spawns Undead and Parasites every 2 turns.</li>
     *   <li>A Vent at (8, 13) that spawns Parasites and Slimes when a worker is adjacent.</li>
     * </ul>
     * Both use {@link MultiSpawner} so the roster is easy to swap later.
     */
    private void spawnHazards() {
        // 20-overflow Hole: Undead + Parasite, fast tempo (2 turns) for testing.
        Spawner overflowHoleSpawner = new MultiSpawner(new UndeadSpawner(), new ParasiteSpawner());
        this.at(30, 14).setGround(new Hole(overflowHoleSpawner, 2));

        // 20-overflow Vent: Parasite + Slime, motion-activated.
        Spawner overflowVentSpawner = new MultiSpawner(new ParasiteSpawner(), new SlimeSpawner());
        this.at(11, 3).setGround(new Vent(overflowVentSpawner));

        Spawner overflowVent1Spawner = new SnatcherSpawner();
        this.at(11, 5).setGround(new Vent(overflowVent1Spawner));
    }
}