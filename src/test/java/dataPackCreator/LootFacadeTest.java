package dataPackCreator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LootFacadeTest {
    private LootFacade lootFacade;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        lootFacade = new LootFacade();

        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
    }

    @AfterEach
    void cleanUp() {
        File outputDir = new File("output");

        File[] files = outputDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    @Test
    void testCreateBlockLoot() throws IOException {
        lootFacade.createBlockLoot("diamond_ore", "diamond", 1);

        File file = new File("output/diamond_ore.json");

        assertTrue(file.exists());

        JsonNode root = mapper.readTree(file);

        assertEquals("minecraft:block", root.get("type").asText());

        JsonNode entry = root.get("pools")
                .get(0)
                .get("entries")
                .get(0);

        assertEquals("minecraft:item", entry.get("type").asText());
        assertEquals("diamond", entry.get("name").asText());

        JsonNode count = entry.get("functions")
                .get(0)
                .get("count");

        assertEquals(1, count.get("min").asInt());
        assertEquals(1, count.get("max").asInt());

        JsonNode condition = entry.get("conditions").get(0);

        assertEquals("minecraft:match_tool",
                condition.get("condition").asText());
    }

    @Test
    void testCreateBlockLootWithRange() throws IOException {
        lootFacade.createBlockLootWithRange("gravel", "flint", 0, 2);

        File file = new File("output/gravel.json");

        assertTrue(file.exists());

        JsonNode root = mapper.readTree(file);

        assertEquals("minecraft:block", root.get("type").asText());

        JsonNode count = root.get("pools")
                .get(0)
                .get("entries")
                .get(0)
                .get("functions")
                .get(0)
                .get("count");

        assertEquals(0, count.get("min").asInt());
        assertEquals(2, count.get("max").asInt());
    }

    @Test
    void testCreateEntityLoot() throws IOException {
        lootFacade.createEntityLoot("my_mob", "bone", 1, 3, 0.5f);

        File file = new File("output/my_mob.json");

        assertTrue(file.exists());

        JsonNode root = mapper.readTree(file);

        assertEquals("minecraft:entity", root.get("type").asText());

        JsonNode entry = root.get("pools")
                .get(0)
                .get("entries")
                .get(0);

        assertEquals("bone", entry.get("name").asText());

        JsonNode count = entry.get("functions")
                .get(0)
                .get("count");

        assertEquals(1, count.get("min").asInt());
        assertEquals(3, count.get("max").asInt());

        JsonNode condition = entry.get("conditions").get(0);

        assertEquals("minecraft:random_chance",
                condition.get("condition").asText());

        assertEquals(0.5f,
                (float) condition.get("chance").asDouble());
    }
}
