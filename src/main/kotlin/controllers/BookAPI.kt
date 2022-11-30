package controllers
import persistence.XMLSerializer
import models.Book
import models.Author
import persistence.Serializer


class BookAPI(serializerType: Serializer){

    private var serializer: Serializer = serializerType
    private var books = ArrayList<Book>()
    private var lastId = 0
    private fun getId() = lastId++

    fun add(book: Book): Boolean {
        book.bookId = getId()
        return books.add(book)
    }


    fun listAllBooks(): String =
        if (books.isEmpty())  "No books stored"
        else formatListString(books)



    fun findBook(bookId : Int) =  books.find{ book -> book.bookId == bookId }

    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }
    fun listActiveBooks(): String =
        if (numberOfActiveBooks() == 0) "No active books stored"
        else formatListString(books.filter{ book -> !book.isBookArchived })

    fun listArchivedBooks(): String =
        if (numberOfArchivedBooks() == 0) "No archived books stored"
        else formatListString(books.filter{ book -> book.isBookArchived })





    fun listBooksBySelectedRating(rating: Int): String =
        if (books.isEmpty()) "No books stored"
        else {
            val listOfBooks = formatListString(books.filter{ book -> book.bookRating == rating})
            if (listOfBooks.equals("")) "No books with rating: $rating"
            else "${numberOfBooksByRating(rating)} books with rating $rating: $listOfBooks"
        }


    fun listBooksBySelectedISBN(ISBN: Int): String =
        if (books.isEmpty()) "No books stored"
        else {
            val listOfBooks = formatListString(books.filter{ book -> book.bookISBN == ISBN})
            if (listOfBooks.equals("")) "No books with ISBN: $ISBN"
            else "${numberOfBooksByISBN(ISBN)} books with ISBN $ISBN: $listOfBooks"
        }


    fun archiveBook(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) {
            val bookToArchive = books[indexToArchive]
            if (!bookToArchive.isBookArchived) {
                bookToArchive.isBookArchived = true
                return true
            }
        }
        return false
    }
    fun listBooksBySelectedGenre(cat: String): String =
        if (books.isEmpty()) "No Books are Stored"
        else {
            val listOfBooks = formatListString(books.filter{ book -> book.bookGenre == cat})
            if (listOfBooks.equals("")) "No books with genre: $cat"
            else "${numberOfBooksByGenre(cat)} books with genre $cat: $listOfBooks"
        }

    fun numberOfBooks(): Int = books.size
    fun numberOfActiveBooks(): Int = books.count{book: Book -> !book.isBookArchived}
    fun numberOfArchivedBooks(): Int = books.count{book: Book -> book.isBookArchived}
    fun numberOfBooksByRating(rating: Int): Int = books.count { p: Book -> p.bookRating == rating }

    fun numberOfBooksByGenre(cat: String): Int = books.count { p: Book -> p.bookGenre == cat }

    fun numberOfBooksByISBN(ISBN: Int): Int = books.count { p: Book -> p.bookISBN == ISBN }

    fun deleteBook(indexToDelete: Int): Book? {
        return if (isValidListIndex(indexToDelete, books)) {
            books.removeAt(indexToDelete)
        } else null
    }
    fun searchByTitle(searchString : String) =
        formatListString(books.filter { book -> book.bookTitle.contains(searchString, ignoreCase = true)})
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

    fun searchAuthorByContents(searchString: String): String {
        return if (numberOfBooks() == 0) "No books stored"
        else {
            var listOfBooks = ""
            for (book in books) {
                for (author in book.libraries) {
                    if (author.authorContents.contains(searchString, ignoreCase = true)) {
                        listOfBooks += "${book.bookId}: ${book.bookTitle} \n\t${author}\n"
                    }
                }
            }
            if (listOfBooks == "") "No libraries found for: $searchString"
            else listOfBooks
        }
    }

    // ----------------------------------------------
    //  LISTING METHODS FOR ITEMS
    // ----------------------------------------------
    fun listTodoLibraries(): String =
        if (numberOfBooks() == 0) "No books stored"
        else {
            var listOfTodoLibraries = ""
            for (book in books) {
                for (author in book.libraries) {
                    if (!author.isAuthorComplete) {
                        listOfTodoLibraries += book.bookTitle + ": " + author.authorContents + "\n"
                    }
                }
            }
            listOfTodoLibraries
        }

    // ----------------------------------------------
    //  COUNTING METHODS FOR ITEMS
    // ----------------------------------------------
    fun numberOfToDoLibraries(): Int {
        var numberOfToDoLibraries = 0
        for (book in books) {
            for (author in book.libraries) {
                if (!author.isAuthorComplete) {
                    numberOfToDoLibraries++
                }
            }
        }
        return numberOfToDoLibraries
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

    private fun formatListString(booksToFormat : List<Book>) : String =
        booksToFormat
            .joinToString (separator = "\n") { book ->
                books.indexOf(book).toString() + ": " + book.toString() }
}


