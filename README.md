# BookAdvertService

*Play and Scala RESTful toy web service for book advertisements*


## Introduction

**BookAdvertService** is a simple *RESTful JSON web service*, based on [Play](http://playframework.com/) for [Scala](http://scala-lang.org/), dedicated to CRUD operations on book advertisements.

Book adverts may describe either new books or used books:

* all the adverts have a set of shared fields - *id*, *title*, *genre*, *price* and *isNew*

* used-book adverts also have additional fields (*first price*, *first purchase date*)


## Architectural highlights

* The model resides in the package **info.gianlucacosta.bookadvertservice**: its core consists in the **BookAdvert** trait and the 2 *case classes* implementing it - **NewBookAdvert** and **UsedBookAdvert**

* To decouple the overall architecture from the specific storage technology, the **Repository** pattern (from *Domain-Driven Design*) was introduced

* The web application employs standard **Play** routing and controller, with **Guice** dependency injection configurable via *application.conf* parameters

* Storage is provided by **DynamoDB**; in particular, tests rely on an in-memory instance *and* it is also possible to run it via a separated instance thanks to an **SBT** plugin referenced by the project

* Model and controller are extensively tested with **ScalaTest**

* Experimental (not tested) support for **CORS** calls has been enabled via Play 2.5 filters



## Testing the project

Testing the project requires just a standard sbt command:

> sbt test

In particular, several tests employ an **in-memory DynamoDB instance**, provided by a utility class referencing the required native libraries (**info.gianlucacosta.bookadvertservice.utils.NativeSqlite**)



## Playing with the webservice


1. Open a command-line window (Bash, DOS, ...) into the project directory

1. First of all, let's start the **local persistent copy of DynamoDB**:

    > sbt start-dynamodb-local

  Should you later need to stop it, you could run:

    > sbt stop-dynamodb-local

1. Start Play's console, by running:

    > sbt

1. Now, to start the web application:

    > run

Voilà! We can now access the web service: its GET methods are comfortably accessible via browser, whereas the other ones require a more advanced tool - such as **curl** or a browser plugin.

Please, keep in mind that you can also customize a few parameters - such as DynamoDB's endpoint - by editing *conf/application.conf*.


## Web service methods

BookAdvertService follows standard RESTful conventions:

* **GET /adverts**: returns all the advertisements, in JSON format, showing only the available fields for each advertisement.
It also supports 2 optional query string parameters:

    * **sortBy**: a string, denoting the name of the sorting field. Default: *id*

    * **ascending**: a boolean telling whether to sort ascending. Default: *true*

  If the query string parameters are not both missing, the items in the result are sorted.

* **GET /adverts/:id**: returns the JSON info about the advertisement having the given id (a UUID); if no such advert is found, *NotFound* is returned

* **PUT /adverts/:id**: receives the JSON description of the advert and adds it to the db, returning *Created*. The operation is idempotent

* **POST /adverts/:id**: actually behaves just like **PUT**, but returns **NoContent**

* **DELETE  /adverts/:id**: deletes the advert having the given id; if the advert does not exist, nothing happens. It always returns **NoContent** and is idempotent


## JSON format

```javascript
{
  "id": "string", //output only, do not provide for input
  "title": "string",
  "genre": Integer (0 = Fantasy, 1 = Software),
  "price": Integer,
  "isNew": Boolean,
  "firstPrice": Integer, //Only if isNew == true
  "firstPurchaseDate": "YYYY-MM-DD" //Only if isNew == true
}
```


## Future enhancements

* **findAll()** and **findAllSortedBy()** in **BookAdvertDynamoDbRepository** now load *everything*: it would be nice to introduce some storage-independent pagination utility, which could then be customized for DynamoDB

* likewise, **findAllSortedBy()** sorts *in-memory*: performance considerations will probably, sooner or later, suggest to create *secondary indexes* on DynamoDB

* **CORS** calls should be already enabled thanks to Play 2.5 and its dedicated filter - but the feature should somehow be tested

* it would be nice to extract **info.gianlucacosta.bookadvertservice** to a dedicated JAR artifact

* error handling might be further customized and enhanced


## See also

* [Play](http://playframework.com/)

* [Scala](http://scala-lang.org/)

* [DynamoDB](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide)

* [SBT](http://www.scala-sbt.org/)
