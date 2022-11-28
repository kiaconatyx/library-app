package models


data class Library (var libraryId: Int = 0,
                 var libraryContents : String,
                 var isLibraryComplete: Boolean = false)