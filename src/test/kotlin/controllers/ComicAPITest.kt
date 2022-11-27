package controllers

import models.Comic
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ComicAPITest {

    private var animeadventures: Comic? = null
    private var racetovictory: Comic? = null
    private var lovealittle: Comic? = null
    private var cookingmama: Comic? = null
    private var animalplanet: Comic? = null
    private var populatedComics: ComicAPI? = ComicAPI(XMLSerializer(File("comics.xml")))
    private var emptyComics: ComicAPI? = ComicAPI(XMLSerializer(File("comics.xml")))

    @BeforeEach
    fun setup() {
        animeadventures = Comic("Anime Adventures", 2, "Adventure", false)
        racetovictory = Comic("Race to Victory", 5, "Action", false)
        lovealittle = Comic("Love a Little", 2, "Romance", false)
        cookingmama = Comic("Cooking Mama", 5, "Cooking", false)
        animalplanet = Comic("Animal Planet", 3, "Educational", false)

        populatedComics!!.add(animeadventures!!)
        populatedComics!!.add(racetovictory!!)
        populatedComics!!.add(lovealittle!!)
        populatedComics!!.add(cookingmama!!)
        populatedComics!!.add(animalplanet!!)
    }

    @Nested
    inner class AddComics {
        @AfterEach
        fun tearDown() {
            animeadventures = null
            racetovictory = null
            lovealittle = null
            cookingmama = null
            animalplanet = null
            populatedComics = null
            emptyComics = null
        }

        @Test
        fun `adding a Comic to a populated list adds to ArrayList`() {
            val newComic = Comic("Vampire Slayer Part 1", 5, "fantasy", false)
            assertEquals(5, populatedComics!!.numberOfComics())
            assertTrue(populatedComics!!.add(newComic))
            assertEquals(6, populatedComics!!.numberOfComics())
            assertEquals(newComic, populatedComics!!.findComic(populatedComics!!.numberOfComics() - 1))
        }

        @Test
        fun `adding a Comic to an empty list adds to ArrayList`() {
            val newComic = Comic("Vampire Slayer Part 1", 5, "fantasy", false)
            assertEquals(0, emptyComics!!.numberOfComics())
            assertTrue(emptyComics!!.add(newComic))
            assertEquals(1, emptyComics!!.numberOfComics())
            assertEquals(newComic, emptyComics!!.findComic(emptyComics!!.numberOfComics() - 1))
        }
    }

    @Nested
    inner class DeleteComics {

        @Test
        fun `deleting a Comic that does not exist, returns null`() {
            assertNull(emptyComics!!.deleteComic(0))
            assertNull(populatedComics!!.deleteComic(-1))
            assertNull(populatedComics!!.deleteComic(5))
        }

        @Test
        fun `deleting a Comic that exists delete and returns deleted object`() {
            assertEquals(5, populatedComics!!.numberOfComics())
            assertEquals(animalplanet, populatedComics!!.deleteComic(4))
            assertEquals(4, populatedComics!!.numberOfComics())
            assertEquals(animeadventures, populatedComics!!.deleteComic(0))
            assertEquals(3, populatedComics!!.numberOfComics())
        }
    }

    @Nested
    inner class UpdateComics {
        @Test
        fun `updating a comic that does not exist returns false`(){
            assertFalse(populatedComics!!.updateComic(6, Comic("Updating Comic", 2, "Work", false)))
            assertFalse(populatedComics!!.updateComic(-1, Comic("Updating Comic", 2, "Work", false)))
            assertFalse(emptyComics!!.updateComic(0, Comic("Updating Comic", 2, "Work", false)))
        }

        @Test
        fun `updating a comic that exists returns true and updates`() {
            //check comic 5 exists and check the contents
            assertEquals(animeadventures, populatedComics!!.findComic(4))
            assertEquals("Anime Adventures", populatedComics!!.findComic(4)!!.comicTitle)
            assertEquals(3, populatedComics!!.findComic(4)!!.comicRating)
            assertEquals("Fantasy", populatedComics!!.findComic(4)!!.comicGenre)

            //update comic 5 with new information and ensure contents updated successfully
            assertTrue(populatedComics!!.updateComic(4, Comic("Updating Comic", 2, "Fantasy - Anime", false)))
            assertEquals("Updating Comic", populatedComics!!.findComic(4)!!.comicTitle)
            assertEquals(2, populatedComics!!.findComic(4)!!.comicRating)
            assertEquals("Fantasy", populatedComics!!.findComic(4)!!.comicGenre)
        }
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty comics.XML file.
            val storingComics = ComicAPI(XMLSerializer(File("comics.xml")))
            storingComics.store()

            //Loading the empty comics.xml file into a new object
            val loadedComics = ComicAPI(XMLSerializer(File("comics.xml")))
            loadedComics.load()

            //Comparing the source of the comics (storingComics) with the XML loaded comics (loadedComics)
            assertEquals(0, storingComics.numberOfComics())
            assertEquals(0, loadedComics.numberOfComics())
            assertEquals(storingComics.numberOfComics(), loadedComics.numberOfComics())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 comics to the comics.XML file.
            val storingComics = ComicAPI(XMLSerializer(File("comics.xml")))
            storingComics.add(animeadventures!!)
            storingComics.add(animalplanet!!)
            storingComics.add(cookingmama!!)
            storingComics.store()

            //Loading comics.xml into a different collection
            val loadedComics = ComicAPI(XMLSerializer(File("comics.xml")))
            loadedComics.load()

            //Comparing the source of the comics (storingComics) with the XML loaded comics (loadedComics)
            assertEquals(3, storingComics.numberOfComics())
            assertEquals(3, loadedComics.numberOfComics())
            assertEquals(storingComics.numberOfComics(), loadedComics.numberOfComics())
            assertEquals(storingComics.findComic(0), loadedComics.findComic(0))
            assertEquals(storingComics.findComic(1), loadedComics.findComic(1))
            assertEquals(storingComics.findComic(2), loadedComics.findComic(2))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty comics.json file.
            val storingComics = ComicAPI(JSONSerializer(File("comics.json")))
            storingComics.store()

            //Loading the empty comics.json file into a new object
            val loadedComics = ComicAPI(JSONSerializer(File("comics.json")))
            loadedComics.load()

            //Comparing the source of the comics (storingComics) with the json loaded comics (loadedComics)
            assertEquals(0, storingComics.numberOfComics())
            assertEquals(0, loadedComics.numberOfComics())
            assertEquals(storingComics.numberOfComics(), loadedComics.numberOfComics())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 comics to the comics.json file.
            val storingComics = ComicAPI(JSONSerializer(File("comics.json")))
            storingComics.add(animeadventures!!)
            storingComics.add(cookingmama!!)
            storingComics.add(animalplanet!!)
            storingComics.store()

            //Loading comics.json into a different collection
            val loadedComics = ComicAPI(JSONSerializer(File("comics.json")))
            loadedComics.load()

            //Comparing the source of the comics (storingComics) with the json loaded comics (loadedComics)
            assertEquals(3, storingComics.numberOfComics())
            assertEquals(3, loadedComics.numberOfComics())
            assertEquals(storingComics.numberOfComics(), loadedComics.numberOfComics())
            assertEquals(storingComics.findComic(0), loadedComics.findComic(0))
            assertEquals(storingComics.findComic(1), loadedComics.findComic(1))
            assertEquals(storingComics.findComic(2), loadedComics.findComic(2))
        }
    }

    @Nested
    inner class ArchiveComics {
        @Test
        fun `archiving a comic that does not exist returns false`(){
            assertFalse(populatedComics!!.archiveComic(6))
            assertFalse(populatedComics!!.archiveComic(-1))
            assertFalse(emptyComics!!.archiveComic(0))
        }

        @Test
        fun `archiving an already archived comic returns false`(){
            assertTrue(populatedComics!!.findComic(2)!!.isComicArchived)
            assertFalse(populatedComics!!.archiveComic(2))
        }

        @Test
        fun `archiving an active comic that exists returns true and archives`() {
            assertFalse(populatedComics!!.findComic(1)!!.isComicArchived)
            assertTrue(populatedComics!!.archiveComic(1))
            assertTrue(populatedComics!!.findComic(1)!!.isComicArchived)
        }
    }
    @Nested
    inner class ListComics {
        @Test
        fun `listAllComics returns No Comics Stored message when ArrayList is empty`() {
            assertEquals(0, emptyComics!!.numberOfComics())
            assertTrue(emptyComics!!.listAllComics().lowercase().contains("no comics to see"))
        }

        @Test
        fun `listAllComics returns Comics when ArrayList has comics stored`() {
            assertEquals(5, populatedComics!!.numberOfComics())
            val comicsString = populatedComics!!.listAllComics().lowercase()
            assertTrue(comicsString.contains("anime adventures"))
            assertTrue(comicsString.contains("race to victory"))
            assertTrue(comicsString.contains("love a little"))
            assertTrue(comicsString.contains("cooking mama"))
            assertTrue(comicsString.contains("animal planet"))
        }

        @Test
        fun `listActiveComics returns no active comics stored when ArrayList is empty`() {
            assertEquals(0, emptyComics!!.numberOfActiveComics())
            assertTrue(
                emptyComics!!.listActiveComics().lowercase().contains("no active comics")
            )
        }

        @Test
        fun `listActiveComics returns active comics when ArrayList has active comics stored`() {
            assertEquals(3, populatedComics!!.numberOfActiveComics())
            val activeComicsString = populatedComics!!.listActiveComics().lowercase()
            assertTrue(activeComicsString.contains("anime adventures"))
            assertFalse(activeComicsString.contains("race to victory"))
            assertTrue(activeComicsString.contains("love a little"))
            assertTrue(activeComicsString.contains("cooking mama"))
            assertFalse(activeComicsString.contains("animal planet"))
        }

        @Test
        fun `listArchivedComics returns no archived comics when ArrayList is empty`() {
            assertEquals(0, emptyComics!!.numberOfArchivedComics())
            assertTrue(
                emptyComics!!.listArchivedComics().lowercase().contains("no archived comics")
            )
        }

        @Test
        fun `listArchivedComics returns archived comics when ArrayList has archived comics stored`() {
            assertEquals(2, populatedComics!!.numberOfArchivedComics())
            val archivedComicsString = populatedComics!!.listArchivedComics().lowercase(Locale.getDefault())
            assertFalse(archivedComicsString.contains("anime adventures"))
            assertTrue(archivedComicsString.contains("race to victory"))
            assertFalse(archivedComicsString.contains("love a little"))
            assertFalse(archivedComicsString.contains("cooking mama"))
            assertTrue(archivedComicsString.contains("animal planet"))
        }

        @Test
        fun `listComicsBySelectedRating returns No Comics when ArrayList is empty`() {
            assertEquals(0, emptyComics!!.numberOfComics())
            assertTrue(emptyComics!!.listComicsBySelectedRating(1).lowercase().contains("no comics")
            )
        }

        @Test
        fun `listComicsBySelectedRating returns no comics when no comics of that rating exist`() {
            //Rating 1 (1 comic), 2 (none), 3 (1 comic). 4 (2 comics), 5 (1 comic)
            assertEquals(5, populatedComics!!.numberOfComics())
            val rating2String = populatedComics!!.listComicsBySelectedRating(2).lowercase()
            assertTrue(rating2String.contains("no comics"))
            assertTrue(rating2String.contains("2"))
        }

        @Test
        fun `listComicsBySelectedRating returns all comics that match that rating when comics of that rating exist`() {
            //Rating 1 (1 comic), 2 (none), 3 (1 comic). 4 (2 comics), 5 (1 comic)
            assertEquals(5, populatedComics!!.numberOfComics())
            val rating1String = populatedComics!!.listComicsBySelectedRating(1).lowercase()
            assertTrue(rating1String.contains("1 comic"))
            assertTrue(rating1String.contains("rating 1"))
            assertTrue(rating1String.contains("anime adventures"))
            assertFalse(rating1String.contains("race to victory"))
            assertFalse(rating1String.contains("love a little"))
            assertFalse(rating1String.contains("cooking mama"))
            assertFalse(rating1String.contains("animal planet"))


            val rating4String = populatedComics!!.listComicsBySelectedRating(4).lowercase(Locale.getDefault())
            assertTrue(rating4String.contains("2 comic"))
            assertTrue(rating4String.contains("rating 4"))
            assertFalse(rating4String.contains("race to victory"))
            assertTrue(rating4String.contains("cooking mama"))
            assertTrue(rating4String.contains("animal planet"))
            assertFalse(rating4String.contains("love a little"))
            assertFalse(rating4String.contains("anime adventures"))
        }

        @Test
        fun `listComicsBySelectedGenre returns No Comics when ArrayList is empty`() {
            Assertions.assertEquals(0, emptyComics!!.numberOfComics())
            assertTrue(emptyComics!!.listComicsBySelectedGenre("Work").lowercase().contains("no comics")
            )
        }

        @Test
        fun `listComicsBySelectedGenre returns no comics when no comics of that genre exist`() {
            //Genre 1 (1 comic), 2 (none), 3 (1 comic). 4 (2 comics), 5 (1 comic)
            Assertions.assertEquals(5, populatedComics!!.numberOfComics())
            val genre2String = populatedComics!!.listComicsBySelectedGenre("Work").lowercase()
            assertTrue(genre2String.contains("no comics"))
            assertTrue(genre2String.contains("2"))
        }
        @Test
        fun `listComicsBySelectedGenre returns all comics that match that genre when comics of that genre exist`() {
            //Genre 1 (1 comic), 2 (none), 3 (1 comic). 4 (2 comics), 5 (1 comic)
            Assertions.assertEquals(5, populatedComics!!.numberOfComics())
            val genre1String = populatedComics!!.listComicsBySelectedGenre("Hobby").lowercase()
            assertTrue(genre1String.contains("1 comic"))
            assertTrue(genre1String.contains("genre 1"))
            assertTrue(genre1String.contains("anime adventures"))
            assertFalse(genre1String.contains("animal planet"))
            assertFalse(genre1String.contains("cooking mama"))
            assertFalse(genre1String.contains("love a little"))
            assertFalse(genre1String.contains("race to victory"))

            val genre4String = populatedComics!!.listComicsBySelectedGenre("Work").lowercase()
            assertTrue(genre4String.contains("2 comic"))
            assertTrue(genre4String.contains("genre 4"))
            assertFalse(genre4String.contains("animal planet"))
            assertTrue(genre4String.contains("love a little"))
            assertTrue(genre4String.contains("race to victory"))
            assertFalse(genre4String.contains("cooking mama"))
            assertFalse(genre4String.contains("anime adventures"))
        }

    }

    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfComicsCalculatedCorrectly() {
            assertEquals(5, populatedComics!!.numberOfComics())
            assertEquals(0, emptyComics!!.numberOfComics())
        }

        @Test
        fun numberOfArchivedComicsCalculatedCorrectly() {
            assertEquals(2, populatedComics!!.numberOfArchivedComics())
            assertEquals(0, emptyComics!!.numberOfArchivedComics())
        }

        @Test
        fun numberOfActiveComicsCalculatedCorrectly() {
            assertEquals(3, populatedComics!!.numberOfActiveComics())
            assertEquals(0, emptyComics!!.numberOfActiveComics())
        }

        @Test
        fun numberOfComicsByRatingCalculatedCorrectly() {
            assertEquals(1, populatedComics!!.numberOfComicsByRating(1))
            assertEquals(0, populatedComics!!.numberOfComicsByRating(2))
            assertEquals(1, populatedComics!!.numberOfComicsByRating(3))
            assertEquals(2, populatedComics!!.numberOfComicsByRating(4))
            assertEquals(1, populatedComics!!.numberOfComicsByRating(5))
            assertEquals(0, emptyComics!!.numberOfComicsByRating(1))
        }
    }

}