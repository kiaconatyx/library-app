package models

data class Author (var authorId: Int = 0, val authorNumber: Int ,var authorName : String, var isAuthorActive: Boolean = false){

    override fun toString() =
        if (isAuthorActive)
            "$authorId: $authorName (Active Author)"
        else
            "$authorId: $authorName (Author Inactive)"

}