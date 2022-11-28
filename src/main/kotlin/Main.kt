import controllers.BookAPI
import controllers.ComicAPI
import models.Book
import models.Comic
import models.Library
import mu.KotlinLogging
import persistence.JSONSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit
private val logger = KotlinLogging.logger {}


//private val bookAPI = BookAPI(XMLSerializer(File("books.xml")))
private val bookAPI = BookAPI(JSONSerializer(File("books.json")))
//private val comicAPI = ComicAPI(XMLSerializer(File("comics.xml")))
private val comicAPI = ComicAPI(JSONSerializer(File("comics.json")))

fun main(args: Array<String>) {
    runMenu()
}


fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |       LIBRARY APP              |
         > ----------------------------------
         > | BOOK MENU                      |
         > |   1) Add a book                |
         > |   2) List all books            |
         > |   3) Update a book             |
         > |   4) Delete a book             |
         > |   5) Archive a book            |
         > |   6) Search book(description)  |
         > ----------------------------------
         > | Comic MENU                     |
         > |   7) Add a Comic               |
         > |   8) List all Comics           |
         > |   9) Update a Comic            |
         > |   10) Delete a Comic           |
         > |   11) Archive a Comic          |
         > |   12) Search comic(description)|
         > ----------------------------------
         > ----------------------------------
         > |   20) Save                     |
         > |   21) Load                     |
         > |   0) Exit                      |
         > ----------------------------------
        > ==>> """.trimMargin(">"))
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addBook()
            2  -> listBooks()
            3  -> updateBook()
            4  -> deleteBook()
            5 -> archiveBook()
            6 -> searchBooks()
            7 -> addLibraryToBook()
            8 -> updateLibraryContentsInBook()
            9 -> deleteAnLibrary()
            10 -> markLibraryStatus()
            15 -> searchLibraries()
            16 -> listToDoLibraries()
            17  -> addComic()
            18  -> listComics()
            19  -> updateComic()
            20  -> deleteComic()
            21 -> archiveComic()
            22 -> searchComics()
            23 -> save()
            24 -> load()
            0  -> exitApp()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}

fun addBook(){
    val bookTitle = readNextLine("Enter the book's title: ")
    val bookRating = readNextInt("Enter a rating for the book (1-low, 2, 3, 4, 5-high): ")
    val bookISBN = readNextInt("Enter a unique ISBN for the book ")
    val bookGenre = readNextLine("Enter a suitable genre for the book ")
    val isAdded = bookAPI.add(Book(bookTitle, bookRating, bookISBN,bookGenre, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listBooks() {
    if (bookAPI.numberOfBooks() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL books          |
                  > |   2) View ACTIVE books       |
                  > |   3) View ARCHIVED books     |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllBooks();
            2 -> listActiveBooks();
            3 -> listArchivedBooks();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No books stored");
    }
}


fun listAllBooks() {
    println(bookAPI.listAllBooks())
}

fun listArchivedBooks() {
    println(bookAPI.listArchivedBooks())
}

fun updateBook(){
   // logger.info { "updateBook() function invoked" }
    listBooks()
    if (bookAPI.numberOfBooks() > 0) {
        //only ask the user to choose the book if books exist
        val indexToUpdate = readNextInt("Enter the index of the book to update: ")
        if (bookAPI.isValidIndex(indexToUpdate)) {
            val bookTitle = readNextLine("Enter a title for the book: ")
            val bookRating = readNextInt("Enter a rating (1-low, 2, 3, 4, 5-high): ")
            val bookISBN = readNextInt("Enter a Unique ISBN for the book ")
            val bookGenre = readNextLine("Enter a genre for the book: ")

            //pass the index of the book and the new book details to BookAPI for updating and check for success.
            if (bookAPI.updateBook(indexToUpdate, Book(bookTitle, bookRating, bookISBN,bookGenre, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no books for this index number")
        }
    }
}


fun deleteBook(){
    listBooks()
    if (bookAPI.numberOfBooks() > 0) {
        val indexToDelete = readNextInt("Enter the index of the book to delete: ")
        val bookToDelete = bookAPI.deleteBook(indexToDelete)
        if (bookToDelete != null) {
            println("Delete Successful! Deleted book: ${bookToDelete.bookTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun addComic(){
    val comicTitle = readNextLine("Enter the comics's title: ")
    val comicRating = readNextInt("Enter a rating for the comic (1-low, 2, 3, 4, 5-high): ")
    val comicGenre = readNextLine("Enter a suitable genre for the comic ")
    val isAdded = comicAPI.add(Comic(comicTitle, comicRating, comicGenre, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listComics() {
    if (comicAPI.numberOfComics() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL comics          |
                  > |   2) View ACTIVE comics       |
                  > |   3) View ARCHIVED comics     |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllComics();
            2 -> listActiveComics();
            3 -> listArchivedComics();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No comics stored");
    }
}

fun listAllComics() {
    println(comicAPI.listAllComics())
}

fun listArchivedComics() {
    println(comicAPI.listArchivedComics())
}
fun updateComic(){
    //logger.info { "updateComic() function invoked" }
    listComics()
    if (comicAPI.numberOfComics() > 0) {
        //only ask the user to choose the comic if comics exist
        val indexToUpdate = readNextInt("Enter the index of the comic to update: ")
        if (comicAPI.isValidIndex(indexToUpdate)) {
            val comicTitle = readNextLine("Enter a title for the comic: ")
            val comicRating = readNextInt("Enter a rating (1-low, 2, 3, 4, 5-high): ")
            val comicGenre = readNextLine("Enter a genre for the comic: ")

            //pass the index of the comic and the new comic details to ComicAPI for updating and check for success.
            if (comicAPI.updateComic(indexToUpdate, Comic(comicTitle, comicRating, comicGenre, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no comics for this index number")
        }
    }
}


fun deleteComic(){
    //logger.info { "deleteComic() function invoked" }
    listComics()
    if (comicAPI.numberOfComics() > 0) {
        val indexToDelete = readNextInt("Enter the index of the comic to delete: ")
        val comicToDelete = comicAPI.deleteComic(indexToDelete)
        if (comicToDelete != null) {
            println("Delete Successful! Deleted comic: ${comicToDelete.comicTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun listActiveBooks() {
    println(bookAPI.listActiveBooks())
}

fun archiveBook() {
    listActiveBooks()
    if (bookAPI.numberOfActiveBooks() > 0) {
        //only ask the user to choose the book to archive if active books exist
        val indexToArchive = readNextInt("Enter the index of the book to archive: ")
        //pass the index of the book to BookAPI for archiving and check for success.
        if (bookAPI.archiveBook(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

fun listActiveComics() {
    println(comicAPI.listActiveComics())
}

fun archiveComic() {
    listActiveComics()
    if (comicAPI.numberOfActiveComics() > 0) {
        //only ask the user to choose the comic to archive if active comics exist
        val indexToArchive = readNextInt("Enter the index of the comic to archive: ")
        //pass the index of the comic to ComicAPI for archiving and check for success.
        if (comicAPI.archiveComic(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}



fun searchBooks() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = bookAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No books found")
    } else {
        println(searchResults)
    }
}

fun searchComics() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = comicAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No comics found")
    } else {
        println(searchResults)
    }
}
fun save() {
    try {
        bookAPI.store()
        comicAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}



fun load() {
    try {
        bookAPI.load()
        comicAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}
fun exitApp(){
    println("Exiting...bye")
    exit(0)
}

//-------------------------------------------
//ITEM MENU (only available for active books)
//-------------------------------------------
private fun addLibraryToBook() {
    val book: Book? = askUserToChooseActiveBook()
    if (book != null) {
        if (book.addLibrary(Library(libraryContents = readNextLine("\t Library Contents: "))))
            println("Add Successful!")
        else println("Add NOT Successful")
    }
}

fun updateLibraryContentsInBook() {
    val book: Book? = askUserToChooseActiveBook()
    if (book != null) {
        val library: Library? = askUserToChooseLibrary(book)
        if (library != null) {
            val newContents = readNextLine("Enter new contents: ")
            if (book.update(library.libraryId, Library(libraryContents = newContents))) {
                println("Library contents updated")
            } else {
                println("Library contents NOT updated")
            }
        } else {
            println("Invalid Library Id")
        }
    }
}

fun deleteAnLibrary() {
    val book: Book? = askUserToChooseActiveBook()
    if (book != null) {
        val library: Library? = askUserToChooseLibrary(book)
        if (library != null) {
            val isDeleted = book.delete(library.libraryId)
            if (isDeleted) {
                println("Delete Successful!")
            } else {
                println("Delete NOT Successful")
            }
        }
    }
}

fun markLibraryStatus() {
    val book: Book? = askUserToChooseActiveBook()
    if (book != null) {
        val library: Library? = askUserToChooseLibrary(book)
        if (library != null) {
            var changeStatus = 'X'
            if (library.isLibraryComplete) {
                changeStatus =
                    ScannerInput.readNextChar("The library is currently complete...do you want to mark it as TODO?")
                if ((changeStatus == 'Y') ||  (changeStatus == 'y'))
                    library.isLibraryComplete = false
            }
            else {
                changeStatus =
                    ScannerInput.readNextChar("The library is currently TODO...do you want to mark it as Complete?")
                if ((changeStatus == 'Y') ||  (changeStatus == 'y'))
                    library.isLibraryComplete = true
            }
        }
    }
}

fun searchLibraries() {
    val searchContents = readNextLine("Enter the library contents to search by: ")
    val searchResults = bookAPI.searchLibraryByContents(searchContents)
    if (searchResults.isEmpty()) {
        println("No libraries found")
    } else {
        println(searchResults)
    }
}

fun listToDoLibraries(){
    if (bookAPI.numberOfToDoLibraries() > 0) {
        println("Total TODO Libraries: ${bookAPI.numberOfToDoLibraries()}")
    }
    println(bookAPI.listTodoLibraries())
}
private fun askUserToChooseActiveBook(): Book? {
    listActiveBooks()
    if (bookAPI.numberOfActiveBooks() > 0) {
        val book = bookAPI.findBook(readNextInt("\nEnter the id of the book: "))
        if (book != null) {
            if (book.isBookArchived) {
                println("Book is NOT Active, it is Archived")
            } else {
                return book //chosen book is active
            }
        } else {
            println("Book id is not valid")
        }
    }
    return null //selected book is not active
}

private fun askUserToChooseLibrary(book: Book): Library? {
    if (book.numberOfLibraries() > 0) {
        print(book.listLibraries())
        return book.findOne(readNextInt("\nEnter the id of the library: "))
    } else {
        println("No libraries for chosen book")
        return null
    }
}