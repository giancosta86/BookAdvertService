package info.gianlucacosta.bookadvertservice.io.dynamo

import java.util.UUID

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.model._
import info.gianlucacosta.bookadvertservice.bookadverts.{BookAdvert, BookAdvertFields}
import info.gianlucacosta.bookadvertservice.io.BookAdvertRepository
import info.gianlucacosta.bookadvertservice.io.dynamo.Includes._

import scala.collection.JavaConversions._


object BookAdvertDynamoRepository {
  /**
    * The name of the underlying table
    */
  val tableName =
    "BookAdverts"
}

/**
  * Implementation of BookAdvertRepository working on DynamoDB
  *
  * @param db The underlying DynamoDB
  */
class BookAdvertDynamoRepository(db: DynamoDB) extends BookAdvertRepository {
  private lazy val table =
    db.getTable(BookAdvertDynamoRepository.tableName)


  /**
    * Looks for an advert having the given id
    *
    * @return Some(advert) if the advert was found, None otherwise
    */
  override def findById(id: UUID): Option[BookAdvert] = {
    table
      .query(
        BookAdvertFields.id,
        id.toString
      )
      .headOption
      .map(Includes.toBookAdvert)
  }


  /**
    * Adds a book advert. If an advert having the same id already exists, it gets replaced
    */
  override def add(bookAdvert: BookAdvert): Unit = {
    table.putItem(bookAdvert)
  }


  /**
    * Replaces a book advert with the updated copy - matching by id.
    * If no previous item exists, the updated copy gets added to the table.
    */
  override def update(updatedBookAdvert: BookAdvert): Unit = {
    table.putItem(updatedBookAdvert)
  }


  /**
    * Deletes the advert having the given id.
    * If no such advert exists, nothing happens.
    */
  override def delete(id: UUID): Unit = {
    table.deleteItem(
      BookAdvertFields.id,
      id.toString
    )
  }


  /**
    * Returns all the adverts.
    *
    * The current implementation performs a raw scan - therefore, the actual
    * number of returned items may depend on the pagination limits of the
    * underlying service (for example, 1 MB)
    */
  override def findAll(): Iterable[BookAdvert] = {
    table
      .scan()
      .toList
      .map(Includes.toBookAdvert)
  }


  /**
    * Returns all the adverts, sorted - ascending or descending - according to the given field.
    *
    * The current implementation performs a raw scan (see findAll() for further information).
    *
    * Additionally, sorting is now performed in memory; further efficiency can be achieved by relying
    * on secondary indexes on one or more fields in the underlying table.
    */
  override def findAllSortedBy(sortingFieldName: String, ascending: Boolean): Iterable[BookAdvert] = {
    require(
      BookAdvertFields.All.contains(sortingFieldName),
      s"Invalid sorting field: ${sortingFieldName}"
    )

    val fieldOrdering =
      FieldOrdering.get(ascending)

    table
      .scan()
      .toList
      .sortBy(_.get(sortingFieldName))(fieldOrdering)
      .map(Includes.toBookAdvert)
  }


  /**
    * Creates the underlying table and waits for it to be active
    *
    * @param provisionedThroughput The provisioned throughput - required by DynamoDB's API
    */
  def ensureTable(provisionedThroughput: ProvisionedThroughput): Unit = {
    var bookAdvertsTableRequest =
      new CreateTableRequest()
        .withTableName(
          BookAdvertDynamoRepository.tableName
        )
        .withAttributeDefinitions(
          new AttributeDefinition(
            BookAdvertFields.id,
            ScalarAttributeType.S
          )
        )
        .withKeySchema(
          new KeySchemaElement(
            BookAdvertFields.id,
            KeyType.HASH
          )
        )
        .withProvisionedThroughput(
          provisionedThroughput
        )

    try {
      val bookAdvertsTable =
        db.createTable(bookAdvertsTableRequest)

      bookAdvertsTable.waitForActive()
    } catch {
      case _: ResourceInUseException =>
      //Just do nothing
    }
  }
}
