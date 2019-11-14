package com.emosewapixel.pixellib.machines.gui.layout

class GUIBook {
    val pages = mutableListOf<GUIPage>()

    operator fun get(index: Int) = pages[index]

    lateinit var current: GUIPage

    fun page(builder: GUIPage.() -> Unit) {
        pages += GUIPage(builder)
    }

    operator fun GUIPage.invoke(builder: GUIPage.() -> Unit) {
        this.builder()
        pages += this
    }

    fun GUIPage.add() {
        pages += this
    }
}

fun book(builder: GUIBook.() -> Unit): () -> GUIBook = {
    val book = GUIBook()
    book.builder()
    book.current = book[0]
    book
}