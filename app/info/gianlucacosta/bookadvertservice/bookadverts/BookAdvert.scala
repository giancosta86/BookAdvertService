package info.gianlucacosta.bookadvertservice.bookadverts

import java.time.LocalDate
import java.util.UUID

/**
  * Generic book advertisement
  */
trait BookAdvert {
  def id: UUID

  def title: String

  def genre: Genre

  def price: Int

  def isNew: Boolean

  def firstPrice: Int

  def firstPurchaseDateOption: Option[LocalDate]
}
