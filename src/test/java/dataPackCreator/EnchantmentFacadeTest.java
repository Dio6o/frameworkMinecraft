package dataPackCreator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EnchantmentFacadeTest {

    private EnchantmentFacade enchantmentFacade;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        enchantmentFacade = new EnchantmentFacade();

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
    void testCreateEnchantment() throws IOException {

        String[] slots = {"mainhand"};

        enchantmentFacade.createEnchantment(
                "super_sharpness",
                "enchantment.meupack.super_sharpness",
                "#minecraft:enchantable/sharp_weapon",
                "#minecraft:enchantable/sharp_weapon",
                10,
                5,
                1,
                11,
                21,
                11,
                1,
                slots
        );

        File file = new File("output/super_sharpness.json");

        assertTrue(file.exists());

        JsonNode root = mapper.readTree(file);

        assertEquals(
                "enchantment.meupack.super_sharpness",
                root.get("description")
                        .get("translate")
                        .asText()
        );

        assertEquals(
                "#minecraft:enchantable/sharp_weapon",
                root.get("supported_items").asText()
        );

        assertEquals(
                "#minecraft:enchantable/sharp_weapon",
                root.get("primary_items").asText()
        );

        assertEquals(10, root.get("weight").asInt());

        assertEquals(5, root.get("max_level").asInt());

        JsonNode minCost = root.get("min_cost");

        assertEquals(1, minCost.get("base").asInt());

        assertEquals(
                11,
                minCost.get("per_level_above_first").asInt()
        );

        JsonNode maxCost = root.get("max_cost");

        assertEquals(21, maxCost.get("base").asInt());

        assertEquals(
                11,
                maxCost.get("per_level_above_first").asInt()
        );

        assertEquals(1, root.get("anvil_cost").asInt());

        JsonNode slotsNode = root.get("slots");

        assertEquals(1, slotsNode.size());

        assertEquals(
                "mainhand",
                slotsNode.get(0).asText()
        );

        assertTrue(root.has("effects"));
    }
}