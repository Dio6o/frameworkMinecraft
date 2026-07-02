package dataPackCreator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

/**
 * Facade responsável pela criação de arquivos JSON de encantamentos para data packs do Minecraft.
 * <p>
 * Gera encantamentos customizados com suporte a configuração de custo, nível máximo,
 * slots e itens compatíveis, salvando os arquivos na pasta {@code output/}.
 * </p>
 */
public class EnchantmentFacade {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Cria um encantamento customizado com todas as suas propriedades.
     *
     * @param enchantmentName  Nome do arquivo JSON gerado (sem extensão).
     * @param description      Chave de tradução do nome do encantamento
     *                         (ex: {@code "enchantment.meupack.super_sharpness"}).
     * @param supportedItems   Tag dos itens que suportam o encantamento
     *                         (ex: {@code "#minecraft:enchantable/sharp_weapon"}).
     * @param primaryItems     Tag dos itens primários do encantamento, usados para
     *                         determinar a aparição na mesa de encantamentos
     *                         (ex: {@code "#minecraft:enchantable/sharp_weapon"}).
     * @param weight           Peso do encantamento, determina a frequência com que aparece
     *                         na mesa de encantamentos. Valores maiores aparecem com mais frequência.
     * @param maxLevel         Nível máximo do encantamento.
     * @param minCostBase      Custo base mínimo de experiência para aplicar o encantamento.
     * @param minCostPerLevel  Custo mínimo adicional de experiência por nível acima do primeiro.
     * @param maxCostBase      Custo base máximo de experiência para aplicar o encantamento.
     * @param maxCostPerLevel  Custo máximo adicional de experiência por nível acima do primeiro.
     * @param anvilCost        Custo em níveis de experiência para aplicar o encantamento na bigorna.
     * @param slots            Array com os slots onde o encantamento funciona
     *                         (ex: {@code "mainhand"}, {@code "armor"}).
     * @throws IOException Se ocorrer um erro ao escrever o arquivo JSON.
     *
     * @example
     * <pre>
     * createEnchantment(
     *     "super_sharpness",
     *     "enchantment.meupack.super_sharpness",
     *     "#minecraft:enchantable/sharp_weapon",
     *     "#minecraft:enchantable/sharp_weapon",
     *     10, 5,
     *     1, 11,
     *     21, 11,
     *     1,
     *     new String[]{"mainhand"}
     * );
     * </pre>
     */
    public void createEnchantment(String enchantmentName, String description,
                                  String supportedItems, String primaryItems,
                                  int weight, int maxLevel,
                                  int minCostBase, int minCostPerLevel,
                                  int maxCostBase, int maxCostPerLevel,
                                  int anvilCost, String[] slots) throws IOException {

        ObjectNode root = mapper.createObjectNode();

        root.putObject("description").put("translate", description);
        root.put("supported_items", supportedItems);
        root.put("primary_items", primaryItems);
        root.put("weight", weight);
        root.put("max_level", maxLevel);

        ObjectNode minCost = root.putObject("min_cost");
        minCost.put("base", minCostBase);
        minCost.put("per_level_above_first", minCostPerLevel);

        ObjectNode maxCost = root.putObject("max_cost");
        maxCost.put("base", maxCostBase);
        maxCost.put("per_level_above_first", maxCostPerLevel);

        root.put("anvil_cost", anvilCost);

        var slotsNode = root.putArray("slots");
        for (String slot : slots) {
            slotsNode.add(slot);
        }

        root.putObject("effects");

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File("output/" + enchantmentName + ".json"), root);
    }
}