package controllers
import persistence.XMLSerializer
import models.Book
import persistence.Serializer


class BookAPI(serializerType: Serializer){

    private var serializer: Serializer = serializerType
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

    fun listBooksBySelectedRating(rating: Int): String {
        return if (books.isEmpty()) {
            "No books stored"
        } else {
            var listOfBooks = ""
            for (i in books.indices) {
                if (books[i].bookRating == rating) {
                    listOfBooks +=
                        """$i: ${books[i]}
                        """.trimIndent()
                }
            }
            if (listOfBooks.equals("")) {
                "No books with rating of: $rating"
            } else {
                "${numberOfBooksByRating(rating)} books with rating of $rating: $listOfBooks"
            }
        }
    }

    fun numberOfBooksByRating(rating: Int): Int {
        var counter = 0
        for (book in books) {
            if (book.bookRating == rating) {
                counter++
            }
        }
        return counter
    }

    fun listBooksBySelectedISBN(ISBN: Int): String {
        return if (books.isEmpty()) {
            "No books stored"
        } else {
            var listOfBooks = ""
            for (i in books.indices) {
                if (books[i].bookISBN == ISBN) {
                    listOfBooks +=
                        """$i: ${books[i]}
                        """.trimIndent()
                }
            }
            if (listOfBooks.equals("")) {
                "No books with ISBN of: $ISBN"
            } else {
                "${numberOfBooksByISBN(ISBN)} books with ISBN of $ISBN: $listOfBooks"
            }
        }
    }

    fun numberOfBooksByISBN(ISBN: Int): Int {
        var counter = 0
        for (book in books) {
            if (book.bookISBN == ISBN) {
                counter++
            }
        }
        return counter
    }

    fun listBooksBySelectedGenre(cat: String): String =
        if (books.isEmpty()) "No Books are Stored"
        else {
            val listOfBooks = formatListString(books.filter{ book -> book.bookGenre == cat})
            if (listOfBooks.equals("")) "No books with genre: $cat"
            else "${numberOfBooksByGenre(cat)} books with genre $cat: $listOfBooks"
        }

    fun numberOfBooksByGenre(cat: String): Int = books.count { p: Book -> p.bookGenre == cat }

    fun deleteBook(indexToDelete: Int): Book? {
        return if (isValidListIndex(indexToDelete, books)) {
            books.removeAt(indexToDelete)
        } else null
    }

    fun updateBook(indexToUpdate: Int, book: Book?): Boolean {
        //find the book object by the index number
        val foundBook = findBook(indexToUpdate)

        //if the book exists, use the book details passed as parameters to update the found book in the ArrayList.
        if ((foundBook != null) && (book != null)) {
            foundBook.bookTitle = book.bookTitle
            foundBook.bookRating = book.bookRating
            foundBook.bookISBN = book.bookISBN
            foundBook.bookGenre = book.bookGenre
            return true
        }

        //if the book was not found, return false, indicating that the update was not successful
        return false
    }

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, books);
    }

    @Throws(Exception::class)
    fun load() {
        books = serializer.read() as ArrayList<Book>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(books)
    }
}


