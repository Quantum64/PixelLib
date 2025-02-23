package com.emosewapixel.pixellib.fluids

import com.emosewapixel.pixellib.extensions.plus
import com.emosewapixel.pixellib.materialsystem.elements.ElementUtils
import com.emosewapixel.pixellib.materialsystem.main.Material
import com.emosewapixel.pixellib.materialsystem.main.ObjectType
import net.minecraft.fluid.Fluid
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvents
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.util.text.translation.LanguageMap
import net.minecraftforge.fluids.FluidAttributes
import net.minecraftforge.fluids.FluidStack

class MaterialFluidAttributes(builder: FluidAttributes.Builder, fluid: Fluid, val nameFun: MaterialFluidAttributes.() -> ITextComponent) : FluidAttributes(builder, fluid) {
    override fun getDisplayName(stack: FluidStack?) = nameFun()

    class Builder(mat: Material, type: ObjectType, baseLocation: ResourceLocation) : FluidAttributes.Builder(baseLocation + "_still", baseLocation + "_flow", { builder: FluidAttributes.Builder, fluid: Fluid -> MaterialFluidAttributes(builder, fluid) { if (LanguageMap.getInstance().exists(translationKey)) TranslationTextComponent(translationKey) else type.localize(mat) } }) {
        init {
            translationKey(type.registryName(mat).toString())
            color(type.color(mat) or (mat.alpha shl 24))
            sound(SoundEvents.ITEM_BUCKET_FILL, SoundEvents.ITEM_BUCKET_EMPTY)
            temperature(type.temperature(mat))
            luminosity(((mat.fluidTemperature - 500) / 50).coerceIn(0, 15))
            viscosity(ElementUtils.getTotalDensity(mat, type).toInt().let { if (type.isGas(mat)) -it else it })
            viscosity(ElementUtils.getTotalDensity(mat, type).toInt())
        }
    }
}