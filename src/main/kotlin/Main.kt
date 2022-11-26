import utils.ScannerInput
import java.lang.System.exit


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
    println("You chose to add a new book")
}

fun listBooks(){
    println("You chose to list all books")
}

fun updateBook(){
    println("You chose to Update books")
}

fun deleteBook(){
    println("You chose to Delete a book")
}

fun exitApp(){
    println("Exiting...bye")
    exit(0)
}