package dataPackCreator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AdvancementFacadeTest {

    private AdvancementFacade advancementFacade;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        advancementFacade = new AdvancementFacade();

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
    void testCreateRootAdvancement() throws IOException {

        advancementFacade.createAdvancement(
                "root",
                null,
                "minecraft:grass_block",
                "Meu Pack",
                "Bem vindo ao meu data pack!",
                "task",
                false,
                false,
                "start",
                "minecraft:tick"
        );

        File file = new File("output/root.json");

        assertTrue(file.exists());

        JsonNode root = mapper.readTree(file);

        assertFalse(root.has("parent"));

        JsonNode display = root.get("display");

        assertEquals(
                "minecraft:grass_block",
                display.get("icon")
                        .get("id")
                        .asText()
        );

        assertEquals(
                "Meu Pack",
                display.get("title")
                        .get("text")
                        .asText()
        );

        assertEquals(
                "Bem vindo ao meu data pack!",
                display.get("description")
                        .get("text")
                        .asText()
        );

        assertEquals(
                "task",
                display.get("frame").asText()
        );

        assertFalse(
                display.get("announce_to_chat").asBoolean()
        );

        assertFalse(
                display.get("hidden").asBoolean()
        );

        JsonNode criteria = root.get("criteria");

        assertEquals(
                "minecraft:tick",
                criteria.get("start")
                        .get("trigger")
                        .asText()
        );
    }

    @Test
    void testCreateChildAdvancement() throws IOException {

        advancementFacade.createAdvancement(
                "kill_dragon",
                "meupack:root",
                "minecraft:dragon_head",
                "Matador de Dragão",
                "Derrote o Dragão Ender",
                "challenge",
                true,
                false,
                "kill_dragon",
                "minecraft:player_killed_entity"
        );

        File file = new File("output/kill_dragon.json");

        assertTrue(file.exists());

        JsonNode root = mapper.readTree(file);

        assertEquals(
                "meupack:root",
                root.get("parent").asText()
        );

        JsonNode display = root.get("display");

        assertEquals(
                "minecraft:dragon_head",
                display.get("icon")
                        .get("id")
                        .asText()
        );

        assertEquals(
                "Matador de Dragão",
                display.get("title")
                        .get("text")
                        .asText()
        );

        assertEquals(
                "Derrote o Dragão Ender",
                display.get("description")
                        .get("text")
                        .asText()
        );

        assertEquals(
                "challenge",
                display.get("frame").asText()
        );

        assertTrue(
                display.get("announce_to_chat").asBoolean()
        );

        assertFalse(
                display.get("hidden").asBoolean()
        );

        JsonNode criteria = root.get("criteria");

        assertEquals(
                "minecraft:player_killed_entity",
                criteria.get("kill_dragon")
                        .get("trigger")
                        .asText()
        );
    }
}
