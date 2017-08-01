package info.gianlucacosta.bookadvertservice.io.dynamo

import java.time.LocalDate
import java.util.UUID

import info.gianlucacosta.bookadvertservice.bookadverts.{Genre, UsedBookAdvert}

object TestBookAdvertDynamoRepository_UsedBooks {
  val usedBookAdvert =
    UsedBookAdvert(
      UUID.randomUUID(),
      "Used-book Advert",
      Genre.Fantasy,
      8,
      92,
      LocalDate.of(1986, 4, 29)
    )
}

/**
  * Tests on used-book adverts
  */
class TestBookAdvertDynamoRepository_UsedBooks extends BookAdvertDynamoRepositoryTestSpecific[UsedBookAdvert](
  TestBookAdvertDynamoRepository_UsedBooks.usedBookAdvert
) {
  override protected def changeTitle(advert: UsedBookAdvert, newTitle: String): UsedBookAdvert =
    advert.copy(
      title = newTitle
    )
}
