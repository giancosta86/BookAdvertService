package info.gianlucacosta.bookadvertservice.bookadverts

import java.util.UUID

import org.scalatest.{FlatSpec, Matchers}

class TestNewBookAdvert extends FlatSpec with Matchers {
  private lazy val correctAdvert =
    NewBookAdvert(
      UUID.randomUUID(),
      "Example",
      Genre.Software,
      90
    )


  "Valid parameters" should "successfully construct an advert" in {
    correctAdvert
  }


  "Copying a correct advert passing valid parameters" should "work" in {
    correctAdvert.copy(
      price = 200
    )
  }


  "A null id" should "NOT be accepted" in {
    intercept[IllegalArgumentException] {
      correctAdvert.copy(
        id = null
      )
    }
  }


  "A null title" should "NOT be accepted" in {
    intercept[IllegalArgumentException] {
      correctAdvert.copy(
        title = null
      )
    }
  }


  "An empty title" should "NOT be accepted" in {
    intercept[IllegalArgumentException] {
      correctAdvert.copy(
        title = ""
      )
    }
  }


  "A title consisting of spaces" should "NOT be accepted" in {
    intercept[IllegalArgumentException] {
      correctAdvert.copy(
        title = "   \t    "
      )
    }
  }


  "A null genre" should "NOT be accepted" in {
    intercept[IllegalArgumentException] {
      correctAdvert.copy(
        genre = null
      )
    }
  }


  "A negative price" should "NOT be accepted" in {
    intercept[IllegalArgumentException] {
      correctAdvert.copy(
        price = -5
      )
    }
  }


  "A zero price" should "NOT be accepted" in {
    intercept[IllegalArgumentException] {
      correctAdvert.copy(
        price = 0
      )
    }
  }


  "isNew" should "always be true" in {
    correctAdvert.isNew should be(true)
  }


  "First price" should "always be 0" in {
    correctAdvert.firstPrice should be(0)
  }


  "First purchase date" should "always be None" in {
    correctAdvert.firstPurchaseDateOption should be(None)
  }
}
