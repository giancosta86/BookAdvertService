package controllers

import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

import controllers.Includes._
import info.gianlucacosta.bookadvertservice.bookadverts._
import info.gianlucacosta.bookadvertservice.io.BookAdvertRepository
import play.api.libs.json._
import play.api.mvc._


object BookAdverts {

  object Defaults {
    val SortBy =
      BookAdvertFields.id

    val Ascending =
      true
  }

}


class BookAdverts @Inject()(bookAdvertRepository: BookAdvertRepository) extends Controller {
  def findAll(sortBy: Option[String], ascending: Option[Boolean]) = Action {
    request => {
      val retrievedAdverts: Iterable[BookAdvert] =
        if (sortBy.nonEmpty || ascending.nonEmpty) {
          val actualSortBy = {
            val tempSortBy =
              sortBy
                .getOrElse(BookAdverts.Defaults.SortBy)

            if (tempSortBy.nonEmpty)
              tempSortBy
            else
              BookAdverts.Defaults.SortBy
          }

          val actualAscending =
            ascending.getOrElse(BookAdverts.Defaults.Ascending)

          bookAdvertRepository.findAllSortedBy(actualSortBy, actualAscending)
        } else {
          bookAdvertRepository.findAll()
        }

      val jsonResult =
        Json.toJson(retrievedAdverts)

      Ok(jsonResult.toString)
    }
  }


  def findById(id: UUID) = Action {
    val retrievedAdvertOption =
      bookAdvertRepository.findById(id)

    retrievedAdvertOption match {
      case Some(retrievedAdvert) =>
        Ok(Json.toJson(retrievedAdvert))

      case None =>
        NotFound
    }
  }


  def add(id: UUID) = Action {
    request => {
      putAdvert(id, request)

      Created
    }
  }

  private def putAdvert(id: UUID, request: Request[AnyContent]): Unit = {
    val jsonSource =
      request.body.asJson.get

    val title =
      (jsonSource \ BookAdvertFields.title).get.as[String]

    val genreIndex =
      (jsonSource \ BookAdvertFields.genre).get.as[Int]

    val genre =
      Genre.values()(genreIndex)

    val price =
      (jsonSource \ BookAdvertFields.price).get.as[Int]

    val isNew =
      (jsonSource \ BookAdvertFields.isNew).get.as[Boolean]


    val bookAdvert: BookAdvert =
      if (isNew) {
        NewBookAdvert(
          id,
          title,
          genre,
          price
        )
      } else {
        val firstPrice =
          (jsonSource \ BookAdvertFields.firstPrice).get.as[Int]

        val firstPurchaseDateString =
          (jsonSource \ BookAdvertFields.firstPurchaseDate).get.as[String]

        val firstPurchaseDate =
          LocalDate.parse(firstPurchaseDateString)

        UsedBookAdvert(
          id,
          title,
          genre,
          price,
          firstPrice,
          firstPurchaseDate
        )
      }

    bookAdvertRepository.add(bookAdvert)
  }


  def update(id: UUID) = Action {
    request => {
      putAdvert(id, request)

      NoContent
    }
  }


  def delete(id: UUID) = Action {
    bookAdvertRepository.delete(id)

    NoContent
  }
}
