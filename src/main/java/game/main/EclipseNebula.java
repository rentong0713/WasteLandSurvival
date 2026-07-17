package game.main;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.World;
import game.actors.ContractedWorker;
import game.flora.DeprecatedSprout;
import game.flora.FleshySprout;
import game.flora.WarperSapling;
import game.inventory.WeightLimitedInventory;
import game.grounds.*;
import game.items.*;
import game.interfaces.*;
import game.systems.*;

import java.util.Arrays;
import java.util.List;

/**
 * This class handles the miracle of creation for Moon 99-Deprecated.
 * Focusing on REQ1 (Economy) and REQ2 (Scrap & Items).
 */
public class EclipseNebula extends World {
    public EclipseNebula(Display display) {
        super(display);
    }

    /**
     * Initialise maps, actors, items, and grounds of the game world.
     */
    public void initialise() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();

        groundCreator.registerGround('.', Dirt::new);
        groundCreator.registerGround('#', Wall::new);
        groundCreator.registerGround('~', Puddle::new);
        groundCreator.registerGround('_', Floor::new);

//        groundCreator.registerGround('y', FleshySprout::new);
//        groundCreator.registerGround('w', WarperSapling::new);
//        groundCreator.registerGround('y', DeprecatedSprout::new);


        groundCreator.registerGround('=', AluminiumDoor::new);
        groundCreator.registerGround('N', IronDoor::new);
        groundCreator.registerGround('M', TitaniumDoor::new);
        groundCreator.registerGround('≈', ToxicWaste::new);
        groundCreator.registerGround('≡', Supercomputer::new);

        groundCreator.registerGround('Φ', TeleportationTube::new);

        groundCreator.registerGround('◈', Floor::new);
        groundCreator.registerGround('o', Floor::new);
        groundCreator.registerGround('◎', Floor::new);
        groundCreator.registerGround('V', Floor::new);
        groundCreator.registerGround('O', Floor::new);
        groundCreator.registerGround('⚔', WeaponStore::new);

        List<String> moon99Deprecated = Arrays.asList(
                "....................########################################",
                "...#######..........#__________________#___________________#",
                "...#≡____#..........=__________________=___________________#",
                "...#_____=...~......#__________________#___________________#",
                "...#_____#..~~~.....########=#####=#####___#############___#",
                "...#######.~~~~.....#______#_#_________#___#___________#___#",
                ".........~~~~.......#______#_#_________#####___________#####",
                "....................#______=_#_________#___________________#",
                "......~.............#______#_#_________#___________________#",
                ".....~~~............#______#_###########___#############___#",
                ".....~..............#______#___________#___#___________#___#",
                "....................=______#___________=___=___________=___#",
                "....................#______#############___#############___#",
                ".........~~~~.......#______#___________#####################",
                "........~~~~~~......#______#___________=___________________#",
                ".........~~~~.......#______#___________#___________________#",
                "....................#______#############___#############___#",
                "....................#______#___________#___#___________#___#",
                "..~.................#______=___________=___=___________=___#",
                "....................########################################"
        );

        GameMap moon99DeprecatedMap = new GameMap("99-Deprecated", groundCreator, moon99Deprecated);
        this.addGameMap(moon99DeprecatedMap);

        Moon20Overflow moon20 = new Moon20Overflow(groundCreator);
        this.addGameMap(moon20);

        Location mainTubeLoc = moon99DeprecatedMap.at(5, 3);
        TeleportationTube mainTube = new TeleportationTube();
        mainTubeLoc.setGround(mainTube);

        Location overflowTubeLoc = moon20.at(6, 3);
        TeleportationTube overflowTube = new TeleportationTube();
        overflowTubeLoc.setGround(overflowTube);

        mainTube.addDestination(overflowTubeLoc);
        overflowTube.addDestination(mainTubeLoc);

        moon99DeprecatedMap.at(7, 2).addItem(new AccessCardLevel1());
        moon99DeprecatedMap.at(7, 3).addItem(new FirstAidKit());
        moon99DeprecatedMap.at(35, 2).addItem(new SterilisationBox());

        moon99DeprecatedMap.at(5, 2).addItem(new Apple());
        moon99DeprecatedMap.at(5, 2).addItem(new Cookies());
        moon99DeprecatedMap.at(5, 2).addItem(new Lantern());
        moon99DeprecatedMap.at(5, 2).addItem(new CRTMonitor());
        moon99DeprecatedMap.at(5, 2).addItem(new FloppyDisk());

        // Add TRAP SPAWNS
        // Placing them on Floor tiles inside the facility structure walkway loop
        moon99DeprecatedMap.at(6, 4).addItem(new FireMine());
        moon99DeprecatedMap.at(7, 4).addItem(new GasMine());
        moon99DeprecatedMap.at(8, 4).addItem(new StunMine());

        moon99DeprecatedMap.at(11, 2).setGround(new DeprecatedSprout());

        Spawner deprecatedHoleSpawner = new MultiSpawner(new SlimeSpawner(), new UndeadSpawner());
        moon99DeprecatedMap.at(23, 6).setGround(new Hole(deprecatedHoleSpawner, 20));

        Spawner deprecatedHole1Spawner = new SnatcherSpawner();
        moon99DeprecatedMap.at(23, 7).setGround(new Hole(deprecatedHole1Spawner, 20));

        moon99DeprecatedMap.at(10, 4).setGround(new WeaponStore());

        this.addPlayer(createNewWorker("#1 Bob", 'ඞ'), moon99DeprecatedMap.at(6, 2));
        this.addPlayer(createNewWorker("#2 Tom", 'ඞ'), moon99DeprecatedMap.at(7, 2));
        this.addPlayer(createNewWorker("#3 Sarah", 'ඞ'), moon99DeprecatedMap.at(8, 2));
        this.addPlayer(createNewWorker("#4 Julie", 'ඞ'), moon99DeprecatedMap.at(6, 4));
        this.addPlayer(createNewWorker("#5 Rick", 'ඞ'), moon99DeprecatedMap.at(8, 4));

    }

    /**
     * Helper to ensure each worker is independent and starts with a Flask.
     */
    private ContractedWorker createNewWorker(String name, char skin) {
        WeightLimitedInventory inv = new WeightLimitedInventory(50);
        inv.add(new Flask());
        return new ContractedWorker(name, skin, 100, inv);
    }
}