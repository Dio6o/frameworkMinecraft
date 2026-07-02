package dataPackCreator;

/**
 * Fachada principal do framework de criação de data packs para o Minecraft.
 * <p>
 * Centraliza o acesso a todas as fachadas especializadas, fornecendo um ponto
 * único de entrada para a criação de receitas, loot tables, encantamentos e conquistas.
 * </p>
 */
public class DataPackFacade {

    private final RecipeFacade recipes;
    private final LootFacade loot;
    private final EnchantmentFacade enchantments;
    private final AdvancementFacade advancements;

    /**
     * Inicializa a fachada principal com todas as fachadas especializadas.
     */
    public DataPackFacade() {
        this.recipes      = new RecipeFacade();
        this.loot         = new LootFacade();
        this.enchantments = new EnchantmentFacade();
        this.advancements = new AdvancementFacade();
    }

    /**
     * Retorna a fachada responsável pela criação de receitas.
     *
     * @return Instância de {@link RecipeFacade}.
     */
    public RecipeFacade recipes() {
        return recipes;
    }

    /**
     * Retorna a fachada responsável pela criação de loot tables.
     *
     * @return Instância de {@link LootFacade}.
     */
    public LootFacade loot() {
        return loot;
    }

    /**
     * Retorna a fachada responsável pela criação de encantamentos.
     *
     * @return Instância de {@link EnchantmentFacade}.
     */
    public EnchantmentFacade enchantments() {
        return enchantments;
    }

    /**
     * Retorna a fachada responsável pela criação de conquistas.
     *
     * @return Instância de {@link AdvancementFacade}.
     */
    public AdvancementFacade advancements() {
        return advancements;
    }
}