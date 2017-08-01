package controllers

import java.time.LocalDate
import java.util.UUID

import info.gianlucacosta.bookadvertservice.bookadverts.{BookAdvertFields, Genre, NewBookAdvert, UsedBookAdvert}
import info.gianlucacosta.bookadvertservice.io.dynamo.BookAdvertDynamoRepositoryTestBase
import play.api.http.Status
import play.api.libs.json.{JsArray, JsNumber, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers._


class TestBookAdverts extends BookAdvertDynamoRepositoryTestBase {
  private var controller: BookAdverts = _

  override protected def initDb(): Unit = {
    super.initDb()

    controller =
      new BookAdverts(bookAdvertRepository)
  }


  private val newBookRequestJson =
    Json.obj(
      BookAdvertFields.isNew -> true,
      BookAdvertFields.title -> "Alpha",
      BookAdvertFields.genre -> 0,
      BookAdvertFields.price -> 90
    )


  private def createNewBookAdvert(advertId: UUID) =
    NewBookAdvert(
      advertId,
      "Alpha",
      Genre.values()(0),
      90
    )


  private val usedBookRequestJson =
    Json.obj(
      BookAdvertFields.isNew -> false,
      BookAdvertFields.title -> "Beta",
      BookAdvertFields.genre -> 1,
      BookAdvertFields.price -> 176,
      BookAdvertFields.firstPrice -> 4,
      BookAdvertFields.firstPurchaseDate -> "1986-04-29"
    )


  private def createUsedBookAdvert(advertId: UUID) =
    UsedBookAdvert(
      advertId,
      "Beta",
      Genre.values()(1),
      176,
      4,
      LocalDate.of(1986, 4, 29)
    )


  "Listing all the adverts when the db is empty" should "return an empty JSON list" in {
    val resultJson =
      contentAsJson(
        controller.findAll(
          None,
          None
        )(FakeRequest())
      )

    resultJson should be(JsArray())
  }


  "Listing all the adverts sorted by any field when the db is empty" should "return an empty JSON list" in {
    val resultJson =
      contentAsJson(
        controller.findAll(
          Some(BookAdvertFields.genre),
          Some(true)
        )(FakeRequest())
      )

    resultJson should be(JsArray())
  }


  "Adding a new-book advert" should "work" in {
    val advertId =
      UUID.randomUUID()

    val newBookRequest =
      FakeRequest()
        .withJsonBody(newBookRequestJson)

    status(
      controller.add(advertId)(newBookRequest)
    ) should be(Status.CREATED)


    bookAdvertRepository.findById(advertId).get should be(
      createNewBookAdvert(advertId)
    )
  }


  "Adding a used-book advert" should "work" in {
    val advertId =
      UUID.randomUUID()

    val usedBookRequest =
      FakeRequest()
        .withJsonBody(usedBookRequestJson)

    status(
      controller.add(advertId)(usedBookRequest)
    ) should be(Status.CREATED)


    bookAdvertRepository.findById(advertId).get should be(
      createUsedBookAdvert(advertId)
    )
  }


  "Replacing a new-book advert" should "work" in {
    val advertId =
      UUID.randomUUID()

    val advert =
      createNewBookAdvert(advertId)

    bookAdvertRepository.add(advert)


    val updatingJson =
      newBookRequestJson + (
        BookAdvertFields.price -> JsNumber(Int.MaxValue)
        )

    val updatingRequest =
      FakeRequest()
        .withJsonBody(updatingJson)

    status(
      controller.update(advertId)(updatingRequest)
    ) should be(Status.NO_CONTENT)


    bookAdvertRepository.findById(advertId).get should be(
      advert.copy(
        price = Int.MaxValue
      )
    )
  }


  "Replacing a used-book advert" should "work" in {
    val advertId =
      UUID.randomUUID()

    val advert =
      createUsedBookAdvert(advertId)

    bookAdvertRepository.add(advert)

    val updatingJson =
      usedBookRequestJson + (
        BookAdvertFields.price -> JsNumber(Int.MaxValue)
        )

    val updatingRequest =
      FakeRequest()
        .withJsonBody(updatingJson)

    status(
      controller.update(advertId)(updatingRequest)
    ) should be(Status.NO_CONTENT)


    bookAdvertRepository.findById(advertId).get should be(
      advert.copy(
        price = Int.MaxValue
      )
    )
  }


  "Listing different kinds of advert" should "work" in {
    bookAdvertRepository.add(
      createNewBookAdvert(UUID.randomUUID())
    )

    bookAdvertRepository.add(
      createUsedBookAdvert(UUID.randomUUID())
    )

    val resultJson =
      contentAsJson(
        controller.findAll(
          None,
          None
        )(FakeRequest())
      )

    resultJson.asInstanceOf[JsArray].value.size should be(2)
  }


  "Listing different kinds of advert sorted by first price descending" should "work" in {
    bookAdvertRepository.add(
      createNewBookAdvert(UUID.randomUUID())
    )

    bookAdvertRepository.add(
      createUsedBookAdvert(UUID.randomUUID())
    )

    val resultJson =
      contentAsJson(
        controller.findAll(
          Some(BookAdvertFields.firstPrice),
          Some(false)
        )(FakeRequest())
      )

    val resultValues =
      resultJson.asInstanceOf[JsArray].value

    resultValues.size should be(2)

    /*
    New-book adverts have no "firstPrice" attribute . so, in descending order by firstPrice,
    they are always after used-book adverts
     */
    (resultValues.head \ BookAdvertFields.isNew).get.as[Boolean] should be(false)
  }


  "Retrieving an existing new-book advert by id" should "only show new-book fields" in {
    val advertId =
      UUID.randomUUID()

    val advert =
      createNewBookAdvert(advertId)

    bookAdvertRepository.add(advert)

    val resultJson =
      contentAsJson(
        controller.findById(
          advertId
        )(FakeRequest())
      )

    (resultJson \ BookAdvertFields.id).get.as[UUID] should be(advert.id)
    (resultJson \ BookAdvertFields.title).get.as[String] should be(advert.title)
    (resultJson \ BookAdvertFields.genre).get.as[Int] should be(advert.genre.ordinal())
    (resultJson \ BookAdvertFields.price).get.as[Int] should be(advert.price)
    (resultJson \ BookAdvertFields.isNew).get.as[Boolean] should be(advert.isNew)
    (resultJson \ BookAdvertFields.firstPrice).toOption should be(empty)
    (resultJson \ BookAdvertFields.firstPurchaseDate).toOption should be(empty)
  }


  "Retrieving an existing used-book advert by id" should "work" in {
    val advertId =
      UUID.randomUUID()

    val advert =
      createUsedBookAdvert(advertId)

    bookAdvertRepository.add(advert)

    val resultJson =
      contentAsJson(
        controller.findById(
          advertId
        )(FakeRequest())
      )

    (resultJson \ BookAdvertFields.id).get.as[UUID] should be(advert.id)
    (resultJson \ BookAdvertFields.title).get.as[String] should be(advert.title)
    (resultJson \ BookAdvertFields.genre).get.as[Int] should be(advert.genre.ordinal())
    (resultJson \ BookAdvertFields.price).get.as[Int] should be(advert.price)
    (resultJson \ BookAdvertFields.isNew).get.as[Boolean] should be(advert.isNew)
    (resultJson \ BookAdvertFields.firstPrice).get.as[Int] should be(advert.firstPrice)
    (resultJson \ BookAdvertFields.firstPurchaseDate).get.as[LocalDate] should be(advert.firstPurchaseDate)
  }


  "Retrieving a missing advert by id" should "return NotFound" in {
    status(
      controller.findById(UUID.randomUUID())(FakeRequest())
    ) should be(Status.NOT_FOUND)
  }


  "Removing an advert" should "work" in {
    val newBookAdvertId =
      UUID.randomUUID()

    val newBookAdvert =
      createNewBookAdvert(newBookAdvertId)

    bookAdvertRepository.add(newBookAdvert)


    val usedBookAdvertId =
      UUID.randomUUID()

    val usedBookAdvert =
      createUsedBookAdvert(usedBookAdvertId)

    bookAdvertRepository.add(usedBookAdvert)


    status(
      controller.delete(newBookAdvertId)(FakeRequest())
    ) should be(Status.NO_CONTENT)


    bookAdvertRepository.findAll().toList should be(List(
      usedBookAdvert
    ))
  }
}
