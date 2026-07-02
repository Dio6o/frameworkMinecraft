package dataPackCreator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DataPackFacade dp = new DataPackFacade();

        dp.recipes().shapedRecipe(
                "dirt_diamond",
                new String[][]{
                        {"A", "A", "A"},
                        {"A", "", "A"},
                        {"A", "A", "A"}
                },
                new String[][]{
                        {"A", "dirt"},
                },
                "diamond",
                1
        );

        dp.recipes().shapelessRecipe(
                "diamond_emerald",
                new String[]{
                        "diamond"
                },
                "emerald",
                1
        );

        dp.loot().createBlockLootWithRange(
                "sand_gold",
                "gold",
                1,
                5
        );


        dp.enchantments().createEnchantment(
                "super_afiacao",
                "enchantment.test.super_afiacao",
                "#minecraft:enchantable/sharp_weapon",
                "#minecraft:enchantable/sharp_weapon",
                10,
                5,
                1,
                11,
                21,
                11,
                1,
                new String[]{"mainhand"}
        );

        dp.advancements().createAdvancement(
                "root",
                null,
                "minecraft:grass_block",
                "DataPack test",
                "Teste de Conquista do Datapack.",
                "task",
                true,
                false,
                "start",
                "minecraft:tick"
        );

    }

}

