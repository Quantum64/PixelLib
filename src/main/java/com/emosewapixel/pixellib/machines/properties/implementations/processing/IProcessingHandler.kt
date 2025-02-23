package com.emosewapixel.pixellib.machines.properties.implementations.processing

import com.emosewapixel.pixellib.machines.recipes.Recipe

interface IProcessingHandler {
    fun canStartProcessingStandard(recipe: Recipe) = true

    fun canStartProcessingAutomation(recipe: Recipe) = true

    fun startProcessingStandard(recipe: Recipe) {}

    fun startProcessingAutomation(recipe: Recipe) {}

    fun isProcessing() = true

    fun canContinueProcessing(recipe: Recipe) = true

    fun processTick(recipe: Recipe) {}

    fun shouldFinishProcessing(recipe: Recipe) = true

    fun finishProcessingAutomation(recipe: Recipe) {}

    fun finishProcessingStandard(recipe: Recipe) {}
}