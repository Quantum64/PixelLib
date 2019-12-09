package com.emosewapixel.pixellib.machines.gui.layout.components.slots

import com.emosewapixel.pixellib.machines.gui.GUiUtils
import com.emosewapixel.pixellib.machines.properties.implementations.fluids.TERecipeFluidInputProperty
import com.emosewapixel.pixellib.machines.recipes.components.ingredients.fluids.FluidInputsComponent
import net.minecraft.client.Minecraft
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

open class RecipeFluidSlotComponent(property: TERecipeFluidInputProperty, tankIndex: Int) : FluidSlotComponent(property, tankIndex) {
    val recipeProperty = property.value.recipeProperty

    @OnlyIn(Dist.CLIENT)
    override fun drawInBackground(mouseX: Double, mouseY: Double, xOffset: Int, yOffset: Int, guiLeft: Int, guiTop: Int) {
        super.drawInBackground(mouseX, mouseY, xOffset, yOffset, guiLeft, guiTop)
        if (property.value.getFluidInTank(tankIndex).isEmpty)
            recipeProperty.value?.get(FluidInputsComponent::class.java)?.value?.getOrNull(tankIndex)?.let {
                val stacks = it.first.stacks
                val stack = stacks[(Minecraft.getInstance().world.gameTime / 40).toInt() % stacks.size]
                GUiUtils.drawTransparentFluidStack(stack, xOffset + x + 1, yOffset + y + 1, height - 2, width - 2)
            }
    }
}