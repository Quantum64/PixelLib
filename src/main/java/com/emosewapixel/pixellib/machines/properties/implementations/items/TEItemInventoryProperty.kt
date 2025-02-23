package com.emosewapixel.pixellib.machines.properties.implementations.items

import com.emosewapixel.pixellib.extensions.*
import com.emosewapixel.pixellib.machines.BaseTileEntity
import com.emosewapixel.pixellib.machines.capabilities.items.ItemCapabilityWrapper
import com.emosewapixel.pixellib.machines.gui.BaseContainer
import com.emosewapixel.pixellib.machines.packets.NetworkHandler
import com.emosewapixel.pixellib.machines.packets.handlers.UpdateSlotPacket
import com.emosewapixel.pixellib.machines.properties.ITEBoundProperty
import com.emosewapixel.pixellib.machines.properties.IValueProperty
import com.google.common.reflect.MutableTypeToInstanceMap
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.items.IItemHandlerModifiable
import net.minecraftforge.items.ItemStackHandler

open class TEItemInventoryProperty(override val value: ItemStackHandler, override val id: String, override val te: BaseTileEntity) : IValueProperty<IItemHandlerModifiable>, ITEBoundProperty {
    constructor(slots: Int, id: String, te: BaseTileEntity) : this(object : ItemStackHandler(slots) {
        override fun onContentsChanged(slot: Int) {
            te.markDirty()
            te.update()
        }
    }, id, te)

    override fun detectAndSendChanges(container: BaseContainer) {
        val clientHandler = (container.clientProperties[id] as TEItemInventoryProperty).value
        (0 until clientHandler.slots).forEach { index ->
            if (!clientHandler[index].equals(value[index], false)) {
                NetworkHandler.CHANNEL.sendTo(UpdateSlotPacket(id, index, value[index]), (container.playerInv.player as ServerPlayerEntity).connection.networkManager, NetworkDirection.PLAY_TO_CLIENT)
                clientHandler[index] = value[index].copy()
            }
        }
    }

    override fun clone(): TEItemInventoryProperty {
        val stackHandler = ItemStackHandler(value.slots)
        (0 until value.slots).forEach { stackHandler[it] = value[it].copy() }
        return TEItemInventoryProperty(stackHandler, id, te)
    }

    override fun deserializeNBT(nbt: CompoundNBT?) {
        if (nbt?.contains(id) == true) value.deserializeNBT(nbt.getCompound(id))
    }

    override fun serializeNBT() = nbt {
        id to value
    }

    override fun addCapability(map: MutableTypeToInstanceMap<ICapabilityProvider>) {
        map.getOrAddInstance(ItemCapabilityWrapper::class.java, ItemCapabilityWrapper()).addHandler(value)
    }

    override fun clear() {
        val spawnPos = te.pos + BlockPos(0.5, 0.5, 0.5)
        te.world?.let { world ->
            (0 until value.slots).forEach {
                world.spawnItemInWorld(spawnPos, value[it].copy())
                value[it] = ItemStack.EMPTY
            }
        }
    }
}