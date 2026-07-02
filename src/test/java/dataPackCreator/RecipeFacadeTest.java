package dataPackCreator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RecipeFacadeTest {

    private RecipeFacade recipeFacade;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        recipeFacade = new RecipeFacade();

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
    void testShapedRecipe() throws IOException {

        String[][] pattern = {
                {"", "D", ""},
                {"", "D", ""},
                {"", "S", ""}
        };

        String[][] key = {
                {"D", "diamond"},
                {"S", "stick"}
        };

        recipeFacade.shapedRecipe(
                "diamond_sword",
                pattern,
                key,
                "diamond_sword",
                1
        );

        File file = new File("output/diamond_sword.json");

        assertTrue(file.exists());

        JsonNode root = mapper.readTree(file);

        assertEquals("minecraft:crafting_shaped",
                root.get("type").asText());

        JsonNode patternNode = root.get("pattern");

        assertEquals("D", patternNode.get(0).asText());
        assertEquals("D", patternNode.get(1).asText());
        assertEquals("S", patternNode.get(2).asText());

        JsonNode keyNode = root.get("key");

        assertEquals("minecraft:diamond",
                keyNode.get("D").asText());

        assertEquals("minecraft:stick",
                keyNode.get("S").asText());

        JsonNode resultNode = root.get("result");

        assertEquals("minecraft:diamond_sword",
                resultNode.get("id").asText());

        assertEquals(1,
                resultNode.get("count").asInt());
    }

    @Test
    void testShapelessRecipe() throws IOException {

        String[] ingredients = {
                "coal",
                "blaze_powder",
                "gunpowder"
        };

        recipeFacade.shapelessRecipe(
                "fire_charge",
                ingredients,
                "fire_charge",
                3
        );

        File file = new File("output/fire_charge.json");

        assertTrue(file.exists());

        JsonNode root = mapper.readTree(file);

        assertEquals("minecraft:crafting_shapeless",
                root.get("type").asText());

        JsonNode ingredientsNode = root.get("ingredients");

        assertEquals("minecraft:coal",
                ingredientsNode.get(0).asText());

        assertEquals("minecraft:blaze_powder",
                ingredientsNode.get(1).asText());

        assertEquals("minecraft:gunpowder",
                ingredientsNode.get(2).asText());

        JsonNode resultNode = root.get("result");

        assertEquals("minecraft:fire_charge",
                resultNode.get("id").asText());

        assertEquals(3,
                resultNode.get("count").asInt());
    }
}