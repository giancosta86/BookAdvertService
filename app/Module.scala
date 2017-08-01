import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.google.inject.AbstractModule
import info.gianlucacosta.bookadvertservice.io.BookAdvertRepository
import info.gianlucacosta.bookadvertservice.io.dynamo.BookAdvertDynamoRepository
import play.api.{Configuration, Environment}


class Module(environment: Environment,
             configuration: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    val dynamoClient =
      AmazonDynamoDBClientBuilder
        .standard()
        .withEndpointConfiguration(
          new AwsClientBuilder.EndpointConfiguration(
            configuration.getString("dynamo.endpoint").get,
            configuration.getString("dynamo.region").get

          )
        )
        .withCredentials(
          new AWSStaticCredentialsProvider(
            new BasicAWSCredentials(
              configuration.getString("dynamo.user").get,
              configuration.getString("dynamo.password").get
            )
          )
        )
        .build()


    val dynamoDB =
      new DynamoDB(dynamoClient)


    val dynamoCapacityUnits =
      configuration.getLong("dynamo.capacityUnits").get

    val provisionedThroughput =
      new ProvisionedThroughput(
        dynamoCapacityUnits,
        dynamoCapacityUnits
      )


    val bookAdvertDynamoRepository =
      new BookAdvertDynamoRepository(dynamoDB)


    bookAdvertDynamoRepository.ensureTable(provisionedThroughput)


    bind(
      classOf[BookAdvertRepository]
    )
      .toInstance(bookAdvertDynamoRepository)

  }
}
