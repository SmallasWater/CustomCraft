package customcraft.recipe;

import cn.nukkit.inventory.ShapelessRecipe;
import cn.nukkit.item.Item;

import java.util.Collection;

public class CustomShapelessRecipe extends ShapelessRecipe {

    public CustomShapelessRecipe(Item result, Collection<Item> ingredients) {
        super(result, ingredients);
    }

    public CustomShapelessRecipe(String recipeId, int priority, Item result, Collection<Item> ingredients) {
        super(recipeId, priority, result, ingredients);
    }

}
