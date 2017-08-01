package info.gianlucacosta.bookadvertservice.bookadverts

/**
  * Book advertisement field names - dedicated to external storage
  * and handling
  */
object BookAdvertFields {
  val id =
    "id"

  val title =
    "title"

  val genre =
    "genre"

  val price =
    "price"

  val isNew =
    "isNew"

  val firstPrice =
    "firstPrice"

  val firstPurchaseDate =
    "firstPurchaseDate"


  val All = List(
    id,
    title,
    genre,
    price,
    isNew,
    firstPrice,
    firstPurchaseDate
  )
}
