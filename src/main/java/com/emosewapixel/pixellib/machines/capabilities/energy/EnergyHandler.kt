package com.emosewapixel.pixellib.machines.capabilities.energy

import com.emosewapixel.pixellib.extensions.nbt
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.common.util.INBTSerializable

open class EnergyHandler(private val max: Int) : IEnergyStorageModifiable, INBTSerializable<CompoundNBT> {
    override fun getMaxEnergyStored() = max

    private var stored = 0

    override fun getEnergyStored() = 0

    override fun setEnergyStored(amount: Int) {
        stored = amount
    }

    override fun canExtract() = true

    override fun extractEnergy(maxExtract: Int, simulate: Boolean): Int {
        val extracted = maxExtract.coerceAtMost(stored)
        if (!simulate) stored -= extracted
        return extracted
    }

    override fun canReceive() = true

    override fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int {
        val received = maxReceive.coerceAtMost(max - stored)
        if (!simulate) stored += received
        return received
    }

    override fun serializeNBT() = nbt {
        "energy" to stored
    }

    override fun deserializeNBT(nbt: CompoundNBT?) {
        stored = nbt?.getInt("energy") ?: 0
    }

    fun noLoad() {}
}