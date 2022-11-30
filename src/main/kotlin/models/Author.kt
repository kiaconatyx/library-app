package models

data class Author (var authorId: Int = 0, var authorContents : String, var isAuthorComplete: Boolean = false){

    override fun toString() =
        if (isAuthorComplete)
            "$authorId: $authorContents (Complete)"
        else
            "$authorId: $authorContents (TODO)"

}