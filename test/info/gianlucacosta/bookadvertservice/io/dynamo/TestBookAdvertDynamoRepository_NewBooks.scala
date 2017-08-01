package info.gianlucacosta.bookadvertservice.io.dynamo

import java.util.UUID

import info.gianlucacosta.bookadvertservice.bookadverts.{Genre, NewBookAdvert}


object TestBookAdvertDynamoRepository_NewBooks {
  val newBookAdvert =
    NewBookAdvert(
      UUID.randomUUID(),
      "New-book Advert",
      Genre.Software,
      74
    )
}


/**
  * Tests on new-book adverts
  */
class TestBookAdvertDynamoRepository_NewBooks extends BookAdvertDynamoRepositoryTestSpecific[NewBookAdvert](
  TestBookAdvertDynamoRepository_NewBooks.newBookAdvert
) {
  override protected def changeTitle(advert: NewBookAdvert, newTitle: String): NewBookAdvert =
    advert.copy(
      title = newTitle
    )
}
