package info.gianlucacosta.bookadvertservice.io.dynamo

import java.time.LocalDate
import java.util.UUID

import com.amazonaws.services.dynamodbv2.document.Item
import info.gianlucacosta.bookadvertservice.bookadverts._

import scala.language.implicitConversions


/**
  * Provides automatic conversions from/to DynamoDB's object model
  */
private object Includes {
  implicit def toItem(bookAdvert: BookAdvert): Item = {
    val basicItem =
      new Item()
        .withPrimaryKey(
          BookAdvertFields.id,
          bookAdvert.id.toString
        )
        .withString(
          BookAdvertFields.title,
          bookAdvert.title
        )
        .withInt(
          BookAdvertFields.genre,
          bookAdvert.genre.ordinal()
        )
        .withInt(
          BookAdvertFields.price,
          bookAdvert.price
        )
        .withBoolean(
          BookAdvertFields.isNew,
          bookAdvert.isNew
        )


    if (bookAdvert.isNew)
      basicItem
    else
      basicItem
        .withInt(
          BookAdvertFields.firstPrice,
          bookAdvert.firstPrice
        )
        .withString(
          BookAdvertFields.firstPurchaseDate,
          bookAdvert
            .firstPurchaseDateOption
            .get
            .toString
        )
  }


  implicit def toBookAdvert(item: Item): BookAdvert = {
    val id =
      UUID.fromString(
        item.getString(BookAdvertFields.id)
      )

    val title =
      item.getString(BookAdvertFields.title)

    val genre =
      Genre.values()(
        item.getInt(BookAdvertFields.genre)
      )

    val price =
      item.getInt(BookAdvertFields.price)

    val isNew =
      item.getBoolean(BookAdvertFields.isNew)


    if (isNew) {
      NewBookAdvert(
        id,
        title,
        genre,
        price
      )
    } else {
      val firstPrice =
        item.getInt(BookAdvertFields.firstPrice)

      val firstPurchaseDate =
        LocalDate.parse(
          item.getString(BookAdvertFields.firstPurchaseDate)
        )

      UsedBookAdvert(
        id,
        title,
        genre,
        price,
        firstPrice,
        firstPurchaseDate
      )
    }
  }
}
