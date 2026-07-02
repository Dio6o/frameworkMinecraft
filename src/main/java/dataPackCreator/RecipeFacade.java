package dataPackCreator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

/**
 * Facade responsável pela criação de arquivos JSON de receitas para data packs do Minecraft.
 * <p>
 * Suporta receitas com formato (shaped) e sem formato (shapeless),
 * gerando os arquivos diretamente na pasta {@code output/}.
 * </p>
 */
public class RecipeFacade {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Cria uma receita com formato (shaped), definindo a posição de cada ingrediente
     * na grade de crafting através de um padrão e uma chave.
     *
     * @param recipeName Nome do arquivo JSON gerado (sem extensão).
     * @param pattern    Matriz 3x3 representando o padrão da grade de crafting.
     *                   Cada célula contém o símbolo da chave ou uma string vazia para espaço.
     * @param key        Matriz onde cada linha mapeia um símbolo ({@code key[i][0]})
     *                   ao seu respectivo item ({@code key[i][1]}).
     * @param result     Nome do item resultante da receita (sem o prefixo {@code minecraft:}).
     * @param count      Quantidade do item resultante.
     * @throws IOException Se ocorrer um erro ao escrever o arquivo JSON.
     *
     * @example
     * <pre>
     * shapedRecipe(
     *     "diamond_sword",
     *     new String[][]{{"","D",""}, {"","D",""}, {"","S",""}},
     *     new String[][]{{"D", "diamond"}, {"S", "stick"}},
     *     "diamond_sword",
     *     1
     * );
     * </pre>
     */
    public void shapedRecipe(String recipeName, String[][] pattern, String[][] key,
                             String result, int count) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        root.put("type", "minecraft:crafting_shaped");

        ArrayNode patternNode = root.putArray("pattern");
        StringBuilder layout = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!pattern[i][j].isEmpty()) {
                    layout.append(pattern[i][j]);
                }
            }
            patternNode.add(layout.toString());
            layout.setLength(0);
        }

        ObjectNode keyNode = root.putObject("key");
        for (int i = 0; i < key.length; i++) {
            keyNode.put(key[i][0], "minecraft:" + key[i][1]);
        }

        ObjectNode resultNode = root.putObject("result");
        resultNode.put("id", "minecraft:" + result);
        resultNode.put("count", count);

        save(recipeName, root);
    }

    /**
     * Cria uma receita sem formato (shapeless), onde os ingredientes podem ser
     * colocados em qualquer posição da grade de crafting.
     *
     * @param recipeName  Nome do arquivo JSON gerado (sem extensão).
     * @param ingredients Array com os nomes dos ingredientes necessários
     *                    (sem o prefixo {@code minecraft:}).
     * @param result      Nome do item resultante da receita (sem o prefixo {@code minecraft:}).
     * @param count       Quantidade do item resultante.
     * @throws IOException Se ocorrer um erro ao escrever o arquivo JSON.
     *
     * @example
     * <pre>
     * shapelessRecipe(
     *     "fire_charge",
     *     new String[]{"coal", "blaze_powder", "gunpowder"},
     *     "fire_charge",
     *     3
     * );
     * </pre>
     */
    public void shapelessRecipe(String recipeName, String[] ingredients,
                                String result, int count) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        root.put("type", "minecraft:crafting_shapeless");

        ArrayNode ingredientsNode = root.putArray("ingredients");
        for (String ingredient : ingredients) {
            ingredientsNode.add("minecraft:" + ingredient);
        }

        ObjectNode resultNode = root.putObject("result");
        resultNode.put("id", "minecraft:" + result);
        resultNode.put("count", count);

        save(recipeName, root);
    }

    /**
     * Salva o JSON gerado em um arquivo na pasta {@code output/}.
     *
     * @param name Nome do arquivo (sem extensão).
     * @param root Nó raiz do JSON a ser salvo.
     * @throws IOException Se ocorrer um erro ao escrever o arquivo.
     */
    private void save(String name, ObjectNode root) throws IOException {
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File("output/" + name + ".json"), root);
    }
}