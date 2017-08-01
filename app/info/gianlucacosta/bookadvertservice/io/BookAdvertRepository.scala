package info.gianlucacosta.bookadvertservice.io

import java.util.UUID

import info.gianlucacosta.bookadvertservice.bookadverts.BookAdvert

/**
  * Repository handling book advertisements
  */
trait BookAdvertRepository {
  def findById(id: UUID): Option[BookAdvert]

  def add(bookAdvert: BookAdvert): Unit

  def update(updatedBookAdvert: BookAdvert): Unit

  def delete(id: UUID): Unit

  def findAll(): Iterable[BookAdvert]

  def findAllSortedBy(sortingFieldName: String, ascending: Boolean): Iterable[BookAdvert]
}
