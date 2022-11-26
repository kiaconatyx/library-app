package models

data class Book(var bookTitle: String,
                var bookRating: Int,
                var bookISBN: Int,
                var bookGenre: String,
                var isBookArchived :Boolean){
}