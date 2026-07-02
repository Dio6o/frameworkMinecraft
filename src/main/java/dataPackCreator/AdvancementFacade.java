package dataPackCreator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

/**
 * Fachada responsável pela criação de arquivos JSON de conquistas para data packs do Minecraft.
 * <p>
 * Gera conquistas customizadas com suporte a ícone, descrição, critério de conclusão
 * e posicionamento na árvore de conquistas, salvando os arquivos na pasta {@code output/}.
 * </p>
 */
public class AdvancementFacade {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Cria uma conquista customizada com todas as suas propriedades.
     * <p>
     * Caso {@code parent} seja {@code null}, a conquista será tratada como raiz
     * de uma nova árvore de conquistas.
     * </p>
     *
     * @param advancementName Nome do arquivo JSON gerado (sem extensão).
     * @param parent          Identificador da conquista pai na árvore
     *                        (ex: {@code "meupack:root"}). Passar {@code null}
     *                        cria uma conquista raiz sem pai.
     * @param icon            Nome do item usado como ícone da conquista
     *                        (ex: {@code "minecraft:diamond_sword"}).
     * @param title           Título da conquista exibido ao jogador.
     * @param description     Descrição da conquista exibida ao passar o mouse.
     * @param frame           Formato do ícone na árvore de conquistas.
     *                        Valores aceitos: {@code "task"}, {@code "goal"}, {@code "challenge"}.
     * @param announceToChat  Se {@code true}, anuncia no chat quando a conquista é completada.
     * @param hidden          Se {@code true}, a conquista fica oculta até ser completada.
     * @param criterionName   Nome interno do critério que completa a conquista.
     * @param triggerType     Tipo de gatilho que completa a conquista
     *                        (ex: {@code "minecraft:player_killed_entity"}).
     * @throws IOException Se ocorrer um erro ao escrever o arquivo JSON.
     *
     * @example
     * <pre>
     * // Conquista raiz
     * createAdvancement(
     *     "root", null,
     *     "minecraft:grass_block",
     *     "Meu Pack", "Bem vindo ao meu data pack!",
     *     "task", false, false,
     *     "start", "minecraft:tick"
     * );
     *
     * // Conquista filha
     * createAdvancement(
     *     "kill_dragon", "meupack:root",
     *     "minecraft:dragon_head",
     *     "Matador de Dragão", "Derrote o Dragão Ender",
     *     "challenge", true, false,
     *     "kill_dragon", "minecraft:player_killed_entity"
     * );
     * </pre>
     */
    public void createAdvancement(String advancementName, String parent,
                                  String icon, String title, String description,
                                  String frame, boolean announceToChat, boolean hidden,
                                  String criterionName, String triggerType) throws IOException {
        ObjectNode root = mapper.createObjectNode();

        if (parent != null) {
            root.put("parent", parent);
        }

        ObjectNode display = root.putObject("display");

        display.putObject("icon").put("id", icon);
        display.putObject("title").put("text", title);
        display.putObject("description").put("text", description);
        display.put("frame", frame);
        display.put("announce_to_chat", announceToChat);
        display.put("hidden", hidden);

        ObjectNode criteria = root.putObject("criteria");
        criteria.putObject(criterionName).put("trigger", triggerType);

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File("output/" + advancementName + ".json"), root);
    }
}