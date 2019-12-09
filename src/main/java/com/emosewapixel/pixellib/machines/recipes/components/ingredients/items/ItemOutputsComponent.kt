package com.emosewapixel.pixellib.machines.recipes.components.ingredients.items

import com.emosewapixel.pixellib.machines.BaseTileEntity
import com.emosewapixel.pixellib.machines.gui.layout.IGUIComponent
import com.emosewapixel.pixellib.machines.gui.layout.components.slots.ItemSlotComponent
import com.emosewapixel.pixellib.machines.gui.layout.components.stacksuppliers.RecipeOutputItemStackSupplierSlot
import com.emosewapixel.pixellib.machines.properties.ITEBoundProperty
import com.emosewapixel.pixellib.machines.properties.implementations.items.TEItemInventoryProperty
import com.emosewapixel.pixellib.machines.properties.implementations.items.TEItemOutputProperty
import com.emosewapixel.pixellib.machines.recipes.Recipe
import com.emosewapixel.pixellib.machines.recipes.components.IRecipeComponent
import com.emosewapixel.pixellib.machines.recipes.components.grouping.RecipeComponentFamilies
import com.emosewapixel.pixellib.machines.recipes.components.ingredients.recipestacks.IRecipeStack
import com.emosewapixel.pixellib.machines.recipes.components.ingredients.utility.WeightedMap
import net.minecraft.item.ItemStack
import kotlin.math.max

class ItemOutputsComponent(val max: Int, val min: Int = 0) : IRecipeComponent<List<WeightedMap<IRecipeStack<ItemStack>>>> {
    override val name = "item_outputs"
    override val family = RecipeComponentFamilies.OUTPUT_SLOTS

    override fun isRecipeValid(recipe: Recipe): Boolean {
        val handler = recipe[this] ?: return min <= 0
        return handler.value.size in min..max
    }

    override fun addDefaultProperty(te: BaseTileEntity, properties: MutableList<ITEBoundProperty>) {
        properties += TEItemOutputProperty(max, "item_outputs", te)
    }

    override fun addGUIComponents(machine: BaseTileEntity?): List<IGUIComponent> =
            machine?.let {
                val property = it.teProperties["item_outputs"] as? TEItemInventoryProperty ?: return emptyList()
                (0 until property.value.slots).map { index -> ItemSlotComponent(property, index) }
            } ?: (0 until max).map(::RecipeOutputItemStackSupplierSlot)

    override fun addRecipeGUIComponents(machine: BaseTileEntity?, recipe: Recipe): List<IGUIComponent> {
        val handler = recipe[this]?.value ?: return emptyList()
        return machine?.let {
            val property = it.teProperties["item_outputs"] as? TEItemInventoryProperty ?: return emptyList()
            (0 until max(property.value.slots, handler.size)).map { index -> ItemSlotComponent(property, index) }
        } ?: (handler.indices).map(::RecipeOutputItemStackSupplierSlot)
    }
}