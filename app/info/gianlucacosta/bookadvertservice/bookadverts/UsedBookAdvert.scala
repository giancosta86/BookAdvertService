package info.gianlucacosta.bookadvertservice.bookadverts

import java.time.LocalDate
import java.util.UUID


case class UsedBookAdvert(
                           id: UUID,
                           title: String,
                           genre: Genre,
                           price: Int,
                           firstPrice: Int,
                           firstPurchaseDate: LocalDate
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


  require(
    firstPrice > 0,
    "The first price must be > 0"
  )

  require(
    firstPurchaseDate != null,
    "The first purchase date cannot be null"
  )


  override def isNew: Boolean =
    false


  override def firstPurchaseDateOption: Option[LocalDate] =
    Some(firstPurchaseDate)
}
