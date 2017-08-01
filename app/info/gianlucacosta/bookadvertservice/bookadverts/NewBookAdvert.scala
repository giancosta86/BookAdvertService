package info.gianlucacosta.bookadvertservice.bookadverts

import java.time.LocalDate
import java.util.UUID

/**
  * Advertisement for new books
  */
case class NewBookAdvert(
                          id: UUID,
                          title: String,
                          genre: Genre,
                          price: Int
                        ) extends BookAdvert {
  require(
    id != null,
    "The ID cannot be null"
  )

  require(
    title != null,
    "The title cannot be null"
  )

  require(
    title.trim != "",
    "The title cannot be empty"
  )

  require(
    genre != null,
    "The genre cannot be null"
  )

  require(
    price > 0,
    "The price must be > 0"
  )


  override def isNew: Boolean =
    true


  override def firstPrice: Int =
    0


  override def firstPurchaseDateOption: Option[LocalDate] =
    None
}
