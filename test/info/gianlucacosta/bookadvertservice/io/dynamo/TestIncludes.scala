package info.gianlucacosta.bookadvertservice.io.dynamo

import java.time.LocalDate
import java.util.UUID

import com.amazonaws.services.dynamodbv2.document.Item
import info.gianlucacosta.bookadvertservice.bookadverts._
import info.gianlucacosta.bookadvertservice.io.dynamo.Includes._
import org.scalatest.{FlatSpec, Matchers}


class TestIncludes extends FlatSpec with Matchers {
  private val newBookAdvert =
    NewBookAdvert(
      UUID.randomUUID(),
      "New-book Advert",
      Genre.Software,
      74
    )


  private val usedBookAdvert =
    UsedBookAdvert(
      UUID.randomUUID(),
      "Used-book Advert",
      Genre.Fantasy,
      8,
      92,
      LocalDate.of(1986, 4, 29)
    )


  "toItem applied to a new-book advert" should "work" in {
    val item: Item =
      newBookAdvert

    item.getString(BookAdvertFields.id) should be(newBookAdvert.id.toString)

    item.getString(BookAdvertFields.title) should be(newBookAdvert.title)

    item.getInt(BookAdvertFields.genre) should be(newBookAdvert.genre.ordinal())

    item.getInt(BookAdvertFields.price) should be(newBookAdvert.price)

    item.getBoolean(BookAdvertFields.isNew) should be(newBookAdvert.isNew)

    item.hasAttribute(BookAdvertFields.firstPrice) should be(false)

    item.hasAttribute(BookAdvertFields.firstPurchaseDate) should be(false)
  }


  "toItem applied to a used-book advert" should "work" in {
    val item: Item =
      usedBookAdvert

    item.getString(BookAdvertFields.id) should be(usedBookAdvert.id.toString)

    item.getString(BookAdvertFields.title) should be(usedBookAdvert.title)

    item.getInt(BookAdvertFields.genre) should be(usedBookAdvert.genre.ordinal())

    item.getInt(BookAdvertFields.price) should be(usedBookAdvert.price)

    item.getBoolean(BookAdvertFields.isNew) should be(usedBookAdvert.isNew)

    item.getInt(BookAdvertFields.firstPrice) should be(usedBookAdvert.firstPrice)

    item.getString(BookAdvertFields.firstPurchaseDate) should be(usedBookAdvert.firstPurchaseDate.toString)
  }


  "toBookAdvert" should "convert a new-book item to a new-book advert" in {
    val item: Item =
      newBookAdvert

    val resultAdvert: BookAdvert =
      item

    resultAdvert should be(newBookAdvert)
  }


  "toBookAdvert" should "convert a used-book item to a used-book advert" in {
    val item: Item =
      usedBookAdvert

    val resultAdvert: BookAdvert =
      item

    resultAdvert should be(usedBookAdvert)
  }
}
