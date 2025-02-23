package com.emosewapixel.pixellib.machines.gui

import com.emosewapixel.pixellib.extensions.alphaF
import com.emosewapixel.pixellib.extensions.blueF
import com.emosewapixel.pixellib.extensions.greenF
import com.emosewapixel.pixellib.extensions.redF
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.client.config.GuiUtils

@OnlyIn(Dist.CLIENT)
object GUiUtils {
    val mc = Minecraft.getInstance()

    @JvmStatic
    fun drawItemStack(stack: ItemStack, x: Int, y: Int, z: Int = 200, altText: String? = null) {
        val itemRenderer = mc.itemRenderer
        itemRenderer.zLevel = z.toFloat()
        val font = stack.item.getFontRenderer(stack) ?: mc.fontRenderer
        itemRenderer.renderItemAndEffectIntoGUI(stack, x, y)
        itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y, altText)
        itemRenderer.zLevel = 0f
    }

    @JvmStatic
    fun drawTransparentItemStack(stack: ItemStack, x: Int, y: Int, z: Int = 200) {
        val itemRenderer = mc.itemRenderer
        itemRenderer.zLevel = z.toFloat()
        itemRenderer.renderItemAndEffectIntoGUI(stack, x, y)
        itemRenderer.zLevel = 0f
        GlStateManager.colorMask(true, true, true, true)
        drawColoredRectangle(0x7f7f7f80, x, y, 16, 16)
    }

    @JvmStatic
    fun drawFluidStack(stack: FluidStack, x: Int, y: Int, width: Int, height: Int) {
        GlStateManager.enableBlend()
        GlStateManager.enableAlphaTest()

        val texture = mc.textureMap.getSprite(stack.fluid.attributes.stillTexture)
        val color = stack.fluid.attributes.color
        GlStateManager.color4f(color.redF, color.greenF, color.blueF, 1f)

        drawTexture(texture, x, y, width, height, z = 100)

        GlStateManager.color4f(1f, 1f, 1f, 1f)

        GlStateManager.disableAlphaTest()
        GlStateManager.disableBlend()

        if (stack.amount / 1000 > 0) {
            val string = (stack.amount / 1000).toString()
            val fontRenderer = mc.fontRenderer
            GlStateManager.disableDepthTest()
            fontRenderer.drawStringWithShadow(string, x + width + 1f - fontRenderer.getStringWidth(string), y + height - 7f, 16777215)
            GlStateManager.enableDepthTest()
        }
    }

    @JvmStatic
    fun drawTransparentFluidStack(stack: FluidStack, x: Int, y: Int, width: Int, height: Int) {
        GlStateManager.enableBlend()
        GlStateManager.enableAlphaTest()

        val texture = mc.textureMap.getSprite(stack.fluid.attributes.stillTexture)
        val color = stack.fluid.attributes.color
        GlStateManager.color4f(color.redF, color.greenF, color.blueF, 1f)

        drawTexture(texture, x, y, width, height, z = 100)
        GlStateManager.colorMask(true, true, true, false)
        drawColoredRectangle(0x7f434343, x, y, 16, 16)

        GlStateManager.color4f(1f, 1f, 1f, 1f)

        GlStateManager.disableAlphaTest()
        GlStateManager.disableBlend()
    }

    @JvmStatic
    fun drawTooltip(tooltips: List<String>, x: Double, y: Double) = GuiUtils.drawHoveringText(tooltips, x.toInt(), y.toInt(), mc.mainWindow.scaledWidth, mc.mainWindow.scaledHeight, -1, mc.fontRenderer)

    @JvmStatic
    fun drawTooltip(stack: ItemStack, x: Double, y: Double) {
        GuiUtils.preItemToolTip(stack)
        this.drawTooltip(this.getTooltipsFromItem(stack), x, y)
        GuiUtils.postItemToolTip()
    }

    @JvmStatic
    fun drawTexture(sprite: TextureAtlasSprite, x: Int, y: Int, width: Int, height: Int, z: Int = 0) =
            drawTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE, x, y, width, height, sprite.minU.toDouble(), sprite.minV.toDouble(), sprite.maxU.toDouble(), sprite.maxV.toDouble(), z)

    @JvmStatic
    @JvmOverloads
    fun drawTexture(location: ResourceLocation, x: Int, y: Int, width: Int, height: Int, uStart: Double = 0.0, vStart: Double = 0.0, uEnd: Double = 1.0, vEnd: Double = 1.0, z: Int = 0) {
        mc.textureManager.bindTexture(location)
        val tessellator = Tessellator.getInstance()
        val bufferbuilder = tessellator.buffer
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX)
        bufferbuilder.pos(x.toDouble(), (y + height).toDouble(), z.toDouble()).tex(uStart, vEnd).endVertex()
        bufferbuilder.pos((x + width).toDouble(), (y + height).toDouble(), z.toDouble()).tex(uEnd, vEnd).endVertex()
        bufferbuilder.pos((x + width).toDouble(), y.toDouble(), z.toDouble()).tex(uEnd, vStart).endVertex()
        bufferbuilder.pos(x.toDouble(), y.toDouble(), z.toDouble()).tex(uStart, vStart).endVertex()
        tessellator.draw()
    }

    fun drawColoredRectangle(color: Int, x: Int, y: Int, width: Int, height: Int, z: Int = 0) {
        GlStateManager.disableTexture()
        GlStateManager.disableDepthTest()
        GlStateManager.enableBlend()
        GlStateManager.color4f(color.redF, color.greenF, color.blueF, color.alphaF)

        val tessellator = Tessellator.getInstance()
        val bufferbuilder = tessellator.buffer
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION)
        bufferbuilder.pos(x.toDouble(), (y + height).toDouble(), z.toDouble()).endVertex()
        bufferbuilder.pos((x + width).toDouble(), (y + height).toDouble(), z.toDouble()).endVertex()
        bufferbuilder.pos((x + width).toDouble(), y.toDouble(), z.toDouble()).endVertex()
        bufferbuilder.pos(x.toDouble(), y.toDouble(), z.toDouble()).endVertex()
        tessellator.draw()

        GlStateManager.color4f(1f, 1f, 1f, 1f)
        GlStateManager.disableBlend()
        GlStateManager.enableDepthTest()
        GlStateManager.enableTexture()
    }

    @JvmStatic
    fun getTooltipsFromItem(stack: ItemStack) = stack.getTooltip(mc.player, if (mc.gameSettings.advancedItemTooltips) ITooltipFlag.TooltipFlags.ADVANCED else ITooltipFlag.TooltipFlags.NORMAL).map(ITextComponent::getFormattedText)
}