package dataPackCreator;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecipeFacadeTest {

    /**
     * Testa a criação de uma receita com formato.
     */
    @Test
    void testShapedRecipe() throws IOException {

        RecipeFacade facade = new RecipeFacade();

        String[][] pattern = {
                {"", "D", ""},
                {"", "D", ""},
                {"", "S", ""}
        };

        String[][] key = {
                {"D", "diamond"},
                {"S", "stick"}
        };

        facade.shapedRecipe(
                "diamond_sword",
                pattern,
                key,
                "diamond_sword",
                1
        );

        File file = new File("output/diamond_sword.json");

        assertTrue(file.exists());
    }

    /**
     * Testa a criação de uma receita sem formato.
     */
    @Test
    void testShapelessRecipe() throws IOException {

        RecipeFacade facade = new RecipeFacade();

        String[] ingredients = {
                "coal",
                "gunpowder"
        };

        facade.shapelessRecipe(
                "fire_charge",
                ingredients,
                "fire_charge",
                1
        );

        File file = new File("output/fire_charge.json");

        assertTrue(file.exists());
    }
}

