package com.emosewapixel.pixellib.machines.properties.implementations.processing.handlers

import com.emosewapixel.pixellib.machines.properties.implementations.energy.TEEnergyInputProperty
import com.emosewapixel.pixellib.machines.properties.implementations.processing.IProcessingHandler
import com.emosewapixel.pixellib.machines.recipes.Recipe
import com.emosewapixel.pixellib.machines.recipes.components.energy.EnergyConsumptionComponent

class EnergyInputProcessingHandler(val property: TEEnergyInputProperty) : IProcessingHandler {
    override fun canStartProcessingStandard(recipe: Recipe): Boolean {
        val recipeEnergy = recipe[EnergyConsumptionComponent::class.java] ?: return true
        return property.value.energyStored >= recipeEnergy.value
    }

    override fun canStartProcessingAutomation(recipe: Recipe) = canStartProcessingStandard(recipe)

    override fun startProcessingStandard(recipe: Recipe) {
        val recipeEnergy = recipe[EnergyConsumptionComponent::class.java] ?: return
        property.value.extractEnergy(recipeEnergy.value, false)
    }

    override fun startProcessingAutomation(recipe: Recipe) = startProcessingStandard(recipe)

    override fun canContinueProcessing(recipe: Recipe): Boolean {
        val recipeEnergy = recipe[EnergyConsumptionComponent::class.java] ?: return true
        return property.value.energyStored >= recipeEnergy.value
    }

    override fun processTick(recipe: Recipe) {
        val recipeEnergy = recipe[EnergyConsumptionComponent::class.java] ?: return
        property.value.extractEnergy(recipeEnergy.value, false)
    }
}