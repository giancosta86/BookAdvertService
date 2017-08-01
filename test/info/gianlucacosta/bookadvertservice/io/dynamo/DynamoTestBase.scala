package info.gianlucacosta.bookadvertservice.io.dynamo

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import info.gianlucacosta.bookadvertservice.utils.NativeSqlite
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}


abstract class DynamoTestBase extends FlatSpec with Matchers with BeforeAndAfter {
  protected var db: DynamoDB = _

  protected val testProvisionedThroughput =
    new ProvisionedThroughput(10L, 10L)

  System.setProperty(
    "sqlite4java.library.path",
    NativeSqlite.libraryDirectory
  )

  before {
    val amazonDb =
      DynamoDBEmbedded
        .create()
        .amazonDynamoDB()


    db =
      new DynamoDB(amazonDb)


    initDb()
  }


  protected def initDb(): Unit


  after {
    db.shutdown()
  }
}
