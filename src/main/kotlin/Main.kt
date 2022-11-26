import controllers.BookAPI
import models.Book
import mu.KotlinLogging
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.lang.System.exit
private val logger = KotlinLogging.logger {}
private val bookAPI = BookAPI()

fun main(args: Array<String>) {
    runMenu()
}


fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |        Book Depo APP           |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a book                |
         > |   2) List all books            |
         > |   3) Update a book             |
         > |   4) Delete a book             |
         > ----------------------------------
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

fun listBooks(){
    println(bookAPI.listAllBooks())
}

fun updateBook(){
    logger.info { "updateBook() function invoked" }
}

fun deleteBook(){
    logger.info { "deleteBook() function invoked" }
}

fun exitApp(){
    println("Exiting...bye")
    exit(0)
}