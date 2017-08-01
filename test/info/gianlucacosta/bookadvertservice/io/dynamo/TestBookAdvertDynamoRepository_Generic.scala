package info.gianlucacosta.bookadvertservice.io.dynamo

import java.util.UUID

import info.gianlucacosta.bookadvertservice.bookadverts.BookAdvertFields

import scala.collection.JavaConversions._


/**
  * Generic tests on BookAdvertDynamoRepository
  */
class TestBookAdvertDynamoRepository_Generic extends BookAdvertDynamoRepositoryTestBase {
  private val newBookAdvert =
    TestBookAdvertDynamoRepository_NewBooks.newBookAdvert


  private val usedBookAdvert =
    TestBookAdvertDynamoRepository_UsedBooks.usedBookAdvert


  "The book advertisements table" should "be created" in {
    db.listTables().size should be(1)
  }


  "Creating the book advertisements table once more" should "do nothing" in {
    bookAdvertRepository.ensureTable(testProvisionedThroughput)
  }


  "Finding an inexisting id" should "return None" in {
    val retrievedAdvert =
      bookAdvertRepository.findById(UUID.randomUUID())

    retrievedAdvert should be(None)
  }


  "Deleting a non-existing advert" should "do nothing" in {
    bookAdvertRepository.delete(UUID.randomUUID())
  }


  "Finding all items when there are no adverts" should "return an empty sequence" in {
    bookAdvertRepository.findAll() should be(empty)
  }


  "Finding all items when there are different kinds of advert" should "work" in {
    bookAdvertRepository.add(newBookAdvert)
    bookAdvertRepository.add(usedBookAdvert)

    val retrievedAdverts =
      bookAdvertRepository
        .findAll()
        .toSet

    retrievedAdverts should be(Set(
      newBookAdvert,
      usedBookAdvert
    ))
  }


  "Sorting by title descending" should "work" in {
    val advertA =
      usedBookAdvert
        .copy(
          id = UUID.randomUUID(),
          title = "A"
        )

    val advertB =
      newBookAdvert
        .copy(
          id = UUID.randomUUID(),
          title = "B"
        )

    val advertC =
      usedBookAdvert
        .copy(
          id = UUID.randomUUID(),
          title = "C"
        )

    val advertD =
      usedBookAdvert
        .copy(
          id = UUID.randomUUID(),
          title = "D"
        )

    val advertE =
      newBookAdvert
        .copy(
          id = UUID.randomUUID(),
          title = "E"
        )

    val advertF =
      newBookAdvert
        .copy(
          id = UUID.randomUUID(),
          title = "F"
        )


    bookAdvertRepository.add(advertD)
    bookAdvertRepository.add(advertE)
    bookAdvertRepository.add(advertF)

    bookAdvertRepository.add(advertC)
    bookAdvertRepository.add(advertB)
    bookAdvertRepository.add(advertA)


    val retrievedAdverts =
      bookAdvertRepository.findAllSortedBy(
        BookAdvertFields.title,
        ascending = false
      ).toList


    retrievedAdverts should be(List(
      advertF,
      advertE,
      advertD,
      advertC,
      advertB,
      advertA
    ))
  }


  "Sorting by price ascending" should "work" in {
    val advert2 =
      usedBookAdvert
        .copy(
          id = UUID.randomUUID(),
          price = 2
        )

    val advert7 =
      newBookAdvert
        .copy(
          id = UUID.randomUUID(),
          price = 7
        )

    val advert44 =
      usedBookAdvert
        .copy(
          id = UUID.randomUUID(),
          price = 44
        )

    val advert56 =
      usedBookAdvert
        .copy(
          id = UUID.randomUUID(),
          price = 56
        )

    val advert198 =
      newBookAdvert
        .copy(
          id = UUID.randomUUID(),
          price = 198
        )

    val advert204 =
      newBookAdvert
        .copy(
          id = UUID.randomUUID(),
          price = 204
        )


    bookAdvertRepository.add(advert56)
    bookAdvertRepository.add(advert198)
    bookAdvertRepository.add(advert204)

    bookAdvertRepository.add(advert44)
    bookAdvertRepository.add(advert7)
    bookAdvertRepository.add(advert2)


    val retrievedAdverts =
      bookAdvertRepository.findAllSortedBy(
        BookAdvertFields.price,
        ascending = true
      ).toList


    retrievedAdverts should be(List(
      advert2,
      advert7,
      advert44,
      advert56,
      advert198,
      advert204
    ))
  }


  "Sorting by isNew ascending" should "work" in {
    val usedBookAdvert5 =
      usedBookAdvert
        .copy(
          id = UUID.randomUUID()
        )

    val userBookAdvert4 =
      usedBookAdvert
        .copy(
          id = UUID.randomUUID()
        )


    val usedBookAdvert3 =
      usedBookAdvert
        .copy(
          id = UUID.randomUUID()
        )


    val usedBookAdvert2 =
      usedBookAdvert
        .copy(
          id = UUID.randomUUID()
        )


    bookAdvertRepository.add(usedBookAdvert)
    bookAdvertRepository.add(usedBookAdvert2)
    bookAdvertRepository.add(usedBookAdvert3)

    bookAdvertRepository.add(newBookAdvert)
    bookAdvertRepository.add(userBookAdvert4)
    bookAdvertRepository.add(usedBookAdvert5)


    val lastAdvert =
      bookAdvertRepository
        .findAllSortedBy(
          BookAdvertFields.isNew,
          ascending = true
        )
        .last

    lastAdvert.isNew should be(true)
  }


  "Sorting by first price ascending" should "work and put new-book adverts before used-book adverts" in {
    val usedBookAdvert2 =
      usedBookAdvert.copy(
        id = UUID.randomUUID(),
        firstPrice = usedBookAdvert.firstPrice + 300
      )


    val usedBookAdvert3 =
      usedBookAdvert.copy(
        id = UUID.randomUUID(),
        firstPrice = usedBookAdvert.firstPrice + 400
      )


    bookAdvertRepository.add(usedBookAdvert2)
    bookAdvertRepository.add(newBookAdvert)
    bookAdvertRepository.add(usedBookAdvert3)
    bookAdvertRepository.add(usedBookAdvert)

    val retrievedAdverts =
      bookAdvertRepository.findAllSortedBy(
        BookAdvertFields.firstPrice,
        ascending = true
      )
        .toList

    retrievedAdverts should be(List(
      newBookAdvert,
      usedBookAdvert,
      usedBookAdvert2,
      usedBookAdvert3
    ))
  }


  "Sorting by first price ascending 2 new-book adverts" should "not fail" in {
    val newBookAdvert2 =
      newBookAdvert.copy(
        id = UUID.randomUUID()
      )

    bookAdvertRepository.add(newBookAdvert)
    bookAdvertRepository.add(newBookAdvert2)


    val retrievedAdverts =
      bookAdvertRepository.findAllSortedBy(
        BookAdvertFields.firstPrice,
        ascending = true
      )
        .toSet

    retrievedAdverts should be(Set(
      newBookAdvert,
      newBookAdvert2
    ))
  }


  "Sorting by first purchase date descending" should "work and put new-book adverts after used-book adverts" in {
    val usedBookAdvert2 =
      usedBookAdvert.copy(
        id = UUID.randomUUID(),
        firstPurchaseDate = usedBookAdvert.firstPurchaseDate.plusWeeks(5)
      )


    val usedBookAdvert3 =
      usedBookAdvert.copy(
        id = UUID.randomUUID(),
        firstPurchaseDate = usedBookAdvert.firstPurchaseDate.plusWeeks(14)
      )


    bookAdvertRepository.add(usedBookAdvert2)
    bookAdvertRepository.add(newBookAdvert)
    bookAdvertRepository.add(usedBookAdvert3)
    bookAdvertRepository.add(usedBookAdvert)

    val retrievedAdverts =
      bookAdvertRepository.findAllSortedBy(
        BookAdvertFields.firstPurchaseDate,
        ascending = false
      )
        .toList

    retrievedAdverts should be(List(
      usedBookAdvert3,
      usedBookAdvert2,
      usedBookAdvert,
      newBookAdvert
    ))
  }


  "Sorting by a missing field" should "fail" in {
    bookAdvertRepository.add(newBookAdvert)
    bookAdvertRepository.add(usedBookAdvert)

    intercept[IllegalArgumentException] {
      bookAdvertRepository.findAllSortedBy("INEXISTING", ascending = true)
    }
  }
}
