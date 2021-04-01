package com.smallaswater.customcraft.recipe;

import cn.nukkit.inventory.ShapedRecipe;
import cn.nukkit.item.Item;

import java.util.List;
import java.util.Map;

public class CustomShapedRecipe extends ShapedRecipe {

    public CustomShapedRecipe(Item primaryResult, String[] shape, Map<Character, Item> ingredients, List<Item> extraResults) {
        super(primaryResult, shape, ingredients, extraResults);
    }

    public CustomShapedRecipe(String recipeId, int priority, Item primaryResult, String[] shape, Map<Character, Item> ingredients, List<Item> extraResults) {
        super(recipeId, priority, primaryResult, shape, ingredients, extraResults);
    }

}
