package dataPackCreator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

/**
 * Facade responsável pela criação de arquivos JSON de loot tables para data packs do Minecraft.
 * <p>
 * Suporta loot tables para blocos com drop fixo, blocos com drop variável
 * e entidades com chance de drop, gerando os arquivos na pasta {@code output/}.
 * </p>
 */
public class LootFacade {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Cria uma loot table para um bloco que rende um único item com quantidade fixa.
     * O item só é dropado se a ferramenta utilizada possuir o encantamento Silk Touch.
     *
     * @param name  Nome do arquivo JSON gerado (sem extensão).
     * @param item  Nome do item que será dropado (sem o prefixo {@code minecraft:}).
     * @param count Quantidade fixa do item dropado.
     * @throws IOException Se ocorrer um erro ao escrever o arquivo JSON.
     *
     * @example
     * <pre>
     * createBlockLoot("diamond_ore", "diamond", 1);
     * </pre>
     */
    public void createBlockLoot(String name, String item, int count) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        root.put("type", "minecraft:block");

        ObjectNode entry = buildItemEntry(item, count, count);
        addSilkTouchCondition(entry);
        buildPool(root, 1, 1, entry);

        write(name, root);
    }

    /**
     * Cria uma loot table para um bloco que rende uma quantidade variável de itens.
     * O item só é dropado se a ferramenta utilizada possuir o encantamento Silk Touch.
     *
     * @param name     Nome do arquivo JSON gerado (sem extensão).
     * @param item     Nome do item que será dropado (sem o prefixo {@code minecraft:}).
     * @param minCount Quantidade mínima do item dropado.
     * @param maxCount Quantidade máxima do item dropado.
     * @throws IOException Se ocorrer um erro ao escrever o arquivo JSON.
     *
     * @example
     * <pre>
     * createBlockLootWithRange("gravel", "flint", 0, 2);
     * </pre>
     */
    public void createBlockLootWithRange(String name, String item,
                                         int minCount, int maxCount) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        root.put("type", "minecraft:block");

        ObjectNode entry = buildItemEntry(item, minCount, maxCount);
        addSilkTouchCondition(entry);
        buildPool(root, 1, 1, entry);

        write(name, root);
    }

    /**
     * Cria uma loot table para uma entidade com chance de drop de um item.
     *
     * @param name     Nome do arquivo JSON gerado (sem extensão).
     * @param item     Nome do item que será dropado (sem o prefixo {@code minecraft:}).
     * @param minCount Quantidade mínima do item dropado.
     * @param maxCount Quantidade máxima do item dropado.
     * @param chance   Chance de drop do item, entre {@code 0.0} (0%) e {@code 1.0} (100%).
     * @throws IOException Se ocorrer um erro ao escrever o arquivo JSON.
     *
     * @example
     * <pre>
     * createEntityLoot("my_mob", "bone", 1, 3, 0.5f);
     * </pre>
     */
    public void createEntityLoot(String name, String item,
                                 int minCount, int maxCount,
                                 float chance) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        root.put("type", "minecraft:entity");

        ObjectNode entry = buildItemEntry(item, minCount, maxCount);

        ArrayNode conditions = entry.putArray("conditions");
        conditions.addObject()
                .put("condition", "minecraft:random_chance")
                .put("chance", chance);

        buildPool(root, 1, 1, entry);

        write(name, root);
    }

    /**
     * Constrói o nó JSON que representa o item que será dropado e sua quantidade.
     *
     * @param item Nome do item (sem o prefixo {@code minecraft:}).
     * @param min  Quantidade mínima do item.
     * @param max  Quantidade máxima do item.
     * @return Nó JSON do entry configurado.
     */
    private ObjectNode buildItemEntry(String item, int min, int max) {
        ObjectNode entry = mapper.createObjectNode();
        entry.put("type", "minecraft:item");
        entry.put("name", item);

        ArrayNode functions = entry.putArray("functions");
        ObjectNode setCount = functions.addObject();
        setCount.put("function", "minecraft:set_count");
        ObjectNode count = setCount.putObject("count");
        count.put("type", "minecraft:uniform");
        count.put("min", min);
        count.put("max", max);

        return entry;
    }

    /**
     * Adiciona a condição de Silk Touch a um entry existente.
     * O item só será dropado se a ferramenta utilizada possuir esse encantamento.
     *
     * @param entry Nó JSON do entry ao qual a condição será adicionada.
     */
    private void addSilkTouchCondition(ObjectNode entry) {
        ArrayNode conditions = entry.putArray("conditions");
        conditions.addObject()
                .put("condition", "minecraft:match_tool")
                .putObject("predicate")
                .putArray("enchantments")
                .addObject()
                .put("enchantment", "minecraft:silk_touch");
    }

    /**
     * Envolve um entry dentro de um pool e o anexa ao nó raiz do JSON.
     * O pool define quantas vezes o jogo sorteia os drops.
     *
     * @param root     Nó raiz do JSON da loot table.
     * @param minRolls Número mínimo de sorteios do pool.
     * @param maxRolls Número máximo de sorteios do pool.
     * @param entry    Nó JSON do entry que será adicionado ao pool.
     */
    private void buildPool(ObjectNode root, int minRolls, int maxRolls, ObjectNode entry) {
        ArrayNode pools = root.putArray("pools");
        ObjectNode pool = pools.addObject();

        ObjectNode rolls = pool.putObject("rolls");
        rolls.put("type", "minecraft:uniform");
        rolls.put("min", minRolls);
        rolls.put("max", maxRolls);

        pool.putArray("entries").add(entry);
    }

    /**
     * Salva o JSON gerado em um arquivo na pasta {@code output/}.
     *
     * @param name Nome do arquivo (sem extensão).
     * @param root Nó raiz do JSON a ser salvo.
     * @throws IOException Se ocorrer um erro ao escrever o arquivo.
     */
    private void write(String name, ObjectNode root) throws IOException {
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File("output/" + name + ".json"), root);
    }
}