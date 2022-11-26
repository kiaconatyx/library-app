package models

data class Book(val bookTitle: String,
                val bookRating: Int,
                val bookISBN: Int,
                val bookGenre: String,
                val isBookArchived :Boolean){
}