package info.gianlucacosta.bookadvertservice.bookadverts

import java.time.LocalDate
import java.util.UUID

import org.scalatest.{FlatSpec, Matchers}


class TestUsedBookAdvert extends FlatSpec with Matchers {
  private lazy val correctAdvert =
    UsedBookAdvert(
      UUID.randomUUID(),
      "Example",
      Genre.Fantasy,
      price = 40,
      firstPrice = 35,
      LocalDate.now()
    )


  "Valid parameters" should "successfully construct an advert" in {
    correctAdvert
  }


  "Copying a correct advert passing valid parameters" should "work" in {
    correctAdvert.copy(
      price = 300
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
        title = " \n     "
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
        price = -4
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


  "isNew" should "always be false" in {
    correctAdvert.isNew should be(false)
  }


  "A negative first price" should "NOT be accepted" in {
    intercept[IllegalArgumentException] {
      correctAdvert.copy(
        firstPrice = -45
      )
    }
  }


  "A zero first price" should "NOT be accepted" in {
    intercept[IllegalArgumentException] {
      correctAdvert.copy(
        firstPrice = 0
      )
    }
  }


  "A null first purchase date" should "NOT be accepted" in {
    intercept[IllegalArgumentException] {
      correctAdvert.copy(
        firstPurchaseDate = null
      )
    }
  }
}
