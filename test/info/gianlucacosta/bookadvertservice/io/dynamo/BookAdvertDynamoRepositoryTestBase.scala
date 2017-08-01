package info.gianlucacosta.bookadvertservice.io.dynamo

import com.amazonaws.services.dynamodbv2.document.Table


/**
  * Base class for all tests involving BookAdvertDynamoRepository
  */
abstract class BookAdvertDynamoRepositoryTestBase extends DynamoTestBase {
  protected var bookAdvertRepository: BookAdvertDynamoRepository = _

  protected var bookAdvertTable: Table = _

  override protected def initDb(): Unit = {
    bookAdvertRepository =
      new BookAdvertDynamoRepository(db)

    bookAdvertRepository.ensureTable(testProvisionedThroughput)

    bookAdvertTable =
      db.getTable(BookAdvertDynamoRepository.tableName)
  }
}
