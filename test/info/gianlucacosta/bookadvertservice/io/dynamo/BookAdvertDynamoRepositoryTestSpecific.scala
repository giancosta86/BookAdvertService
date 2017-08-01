package info.gianlucacosta.bookadvertservice.io.dynamo

import info.gianlucacosta.bookadvertservice.bookadverts.{BookAdvert, BookAdvertFields}
import info.gianlucacosta.bookadvertservice.io.dynamo.Includes._

import scala.collection.JavaConversions._

/**
  * Base class for tests performed on a specific book advert subclass
  *
  * @param testAdvert The book advert employed by the tests
  * @tparam T The specific BookAdvert subclass being tested
  */
abstract class BookAdvertDynamoRepositoryTestSpecific[T <: BookAdvert](testAdvert: T) extends BookAdvertDynamoRepositoryTestBase {
  /**
    * Returns a copy of the given advert, having the given new title
    */
  protected def changeTitle(advert: T, newTitle: String): T


  "Adding a non-existing advert" should "work" in {
    bookAdvertRepository.add(testAdvert)

    val queryResult =
      bookAdvertTable.query(
        BookAdvertFields.id,
        testAdvert.id.toString
      )

    val retrievedBookAdvert: BookAdvert =
      queryResult.head

    retrievedBookAdvert should be(testAdvert)
  }


  "Adding the same advert twice" should "do nothing" in {
    bookAdvertRepository.add(testAdvert)
    bookAdvertRepository.add(testAdvert)

    val queryResult =
      bookAdvertTable.query(
        BookAdvertFields.id,
        testAdvert.id.toString
      )

    queryResult.size should be(1)

    val retrievedBookAdvert: BookAdvert =
      queryResult.head

    retrievedBookAdvert should be(testAdvert)
  }


  "Adding an updated copy of an advert" should "replace the previous one" in {
    bookAdvertRepository.add(testAdvert)

    val updatedAdvert =
      changeTitle(
        testAdvert,
        "Updated advert"
      )

    bookAdvertRepository.add(updatedAdvert)

    val retrievedAdvert =
      bookAdvertRepository.findById(testAdvert.id).get

    retrievedAdvert should be(updatedAdvert)
  }


  "Finding an existing advert" should "work" in {
    bookAdvertRepository.add(testAdvert)

    val retrievedAdvert =
      bookAdvertRepository.findById(testAdvert.id).get

    retrievedAdvert should be(testAdvert)
  }


  "Deleting an existing advert" should "work" in {
    bookAdvertRepository.add(testAdvert)

    bookAdvertRepository.findById(testAdvert.id) should not be None

    bookAdvertRepository.delete(testAdvert.id)

    bookAdvertRepository.findById(testAdvert.id) should be(None)
  }


  "Modifying an inexisting advert" should "add it" in {
    bookAdvertRepository.update(testAdvert)

    val retrievedAdvert =
      bookAdvertRepository.findById(testAdvert.id).get

    retrievedAdvert should be(testAdvert)
  }


  "Modifying an existing advert" should "work" in {
    bookAdvertRepository.add(testAdvert)

    val updatedAdvert =
      changeTitle(
        testAdvert,
        "Updated advert"
      )

    bookAdvertRepository.update(updatedAdvert)

    val retrievedAdverts =
      bookAdvertRepository.findAll().toSet

    retrievedAdverts should be(Set(
      updatedAdvert
    ))
  }
}
