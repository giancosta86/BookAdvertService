package info.gianlucacosta.bookadvertservice.bookadverts

import org.scalatest.{FlatSpec, Matchers}

class TestGenre extends FlatSpec with Matchers {
  "The available genre list" should "include at least 2 genres" in {
    Genre.values().length should be >= 2
  }

  "Retrieving a genre by name" should "work" in {
    Genre.valueOf("Fantasy") should be(Genre.Fantasy)
  }
}
