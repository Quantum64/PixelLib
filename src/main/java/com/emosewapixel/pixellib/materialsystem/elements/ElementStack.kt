package com.emosewapixel.pixellib.materialsystem.elements

import com.blamejared.crafttweaker.api.annotations.ZenRegister
import com.emosewapixel.pixellib.materialsystem.addition.BaseElements
import org.openzen.zencode.java.ZenCodeType

//Elements Stacks are ways of getting an amount of a certain Element
@ZenRegister
@ZenCodeType.Name("pixellib.materialsystem.elements.ElementStack")
data class ElementStack @JvmOverloads constructor(val element: Element, var count: Int = 1) {
    val isEmpty: Boolean
        @ZenCodeType.Getter get() = this == EMPTY || element == BaseElements.NULL || count == 0

    companion object {
        val EMPTY = BaseElements.NULL * 0
    }
}