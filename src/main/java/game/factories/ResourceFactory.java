package game.factories;

import edu.monash.fit2099.engine.items.Item;
import game.items.AlienArtifact;
import game.items.AluminiumScrap;
import game.items.IndustrialFan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * A factory responsible for creating random depositable resources.
 *
 * The factory maintains a pool of resource suppliers and generates a new
 * instance of a randomly selected resource whenever requested.
 *
 * @author  Ren Tong Low
 * @version 1.0
 */
public class ResourceFactory {

    private final List<Supplier<Item>> resourcePool = new ArrayList<>();
    private final Random random = new Random();

    /**
     * Constructs a ResourceFactory and registers all available
     * resource types that may be generated.
     */
    public ResourceFactory() {
        resourcePool.add(AluminiumScrap::new);
        resourcePool.add(IndustrialFan::new);
        resourcePool.add(AlienArtifact::new);
    }

    /**
     * Creates a new instance of a randomly selected resource from the pool.
     *
     * @return a freshly instantiated random depositable resource
     */
    public Item createRandomResource() {
        int index = random.nextInt(resourcePool.size());
        return resourcePool.get(index).get();
    }
}