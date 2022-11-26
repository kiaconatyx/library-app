package controllers

import models.Book

class BookAPI {
    private var books = ArrayList<Book>()

    fun add(book: Book): Boolean {
        return books.add(book)
    }
    fun listAllBooks(): String {
        return if (books.isEmpty()) {
            "No books stored"
        } else {
            var listOfBooks = ""
            for (i in books.indices) {
                listOfBooks += "${i}: ${books[i]} \n"
            }
            listOfBooks
        }
    }

    fun numberOfBooks(): Int {
        return books.size
    }

    fun findBook(index: Int): Book? {
        return if (isValidListIndex(index, books)) {
            books[index]
        } else null
    }

    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    fun listActiveBooks(): String {
        return if (numberOfActiveBooks() == 0) {
            "No active books seem to be stored here"
        } else {
            var listOfActiveBooks = ""
            for (book in books) {
                if (!book.isBookArchived) {
                    listOfActiveBooks += "${books.indexOf(book)}: $book \n"
                }
            }
            listOfActiveBooks
        }
    }

    fun listArchivedBooks(): String {
        return if (numberOfArchivedBooks() == 0) {
            "No archived books to see here"
        } else {
            var listOfArchivedBooks = ""
            for (book in books) {
                if (book.isBookArchived) {
                    listOfArchivedBooks += "${books.indexOf(book)}: $book \n"
                }
            }
            listOfArchivedBooks
        }
    }

    fun numberOfArchivedBooks(): Int {
        var counter = 0
        for (book in books) {
            if (book.isBookArchived) {
                counter++
            }
        }
        return counter
    }

    fun numberOfActiveBooks(): Int {
        var counter = 0
        for (book in books) {
            if (!book.isBookArchived) {
                counter++
            }
        }
        return counter
    }
}