package controllers

import info.gianlucacosta.bookadvertservice.bookadverts._
import play.api.libs.json._

private object Includes {
  implicit val bookAdvertToJson = new Writes[BookAdvert] {
    def writes(bookAdvert: BookAdvert): JsValue = {
      val basicResult =
        Json.obj(
          BookAdvertFields.id -> bookAdvert.id.toString,

          BookAdvertFields.title -> bookAdvert.title,

          BookAdvertFields.genre -> bookAdvert.genre.ordinal(),

          BookAdvertFields.price -> bookAdvert.price,

          BookAdvertFields.isNew -> bookAdvert.isNew
        )

      if (bookAdvert.isNew)
        basicResult
      else
        basicResult +
          (BookAdvertFields.firstPrice -> JsNumber(bookAdvert.firstPrice)) +
          (BookAdvertFields.firstPurchaseDate -> JsString(bookAdvert.firstPurchaseDateOption.get.toString))
    }
  }
}
