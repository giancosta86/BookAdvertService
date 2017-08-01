package info.gianlucacosta.bookadvertservice.io.dynamo

import org.scalatest.{FlatSpec, Matchers}

class TestFieldOrdering extends FlatSpec with Matchers {
  private val testStrings =
    List("B", "D", "A", "F", "C", "E")


  private val testInts =
    List(
      6, 90, 100, 1, 2, 4, 85
    )


  private val testBooleans =
    List(
      false, true, false, true, false
    )


  "Ascending sorting" should "work with strings" in {
    val sortedList =
      testStrings
        .sortBy(_.asInstanceOf[AnyRef])(FieldOrdering.Ascending)

    sortedList should be(List(
      "A", "B", "C", "D", "E", "F"
    ))
  }


  "Descending sorting" should "work with strings" in {
    val sortedList =
      testStrings
        .sortBy(_.asInstanceOf[AnyRef])(FieldOrdering.Descending)

    sortedList should be(List(
      "F", "E", "D", "C", "B", "A"
    ))
  }


  "Ascending sorting" should "work with integers" in {
    val sortedList =
      testInts
        .sortBy(_.asInstanceOf[AnyRef])(FieldOrdering.Ascending)

    sortedList should be(List(
      1, 2, 4, 6, 85, 90, 100
    ))
  }


  "Descending sorting" should "work with integers" in {
    val sortedList =
      testInts
        .sortBy(_.asInstanceOf[AnyRef])(FieldOrdering.Descending)

    sortedList should be(List(
      100, 90, 85, 6, 4, 2, 1
    ))
  }


  "Ascending sorting" should "work with booleans" in {
    val sortedList =
      testBooleans
        .sortBy(_.asInstanceOf[AnyRef])(FieldOrdering.Ascending)

    sortedList should be(List(
      false, false, false, true, true
    ))
  }


  "Descending sorting" should "work with booleans" in {
    val sortedList =
      testBooleans
        .sortBy(_.asInstanceOf[AnyRef])(FieldOrdering.Descending)

    sortedList should be(List(
      true, true, false, false, false
    ))
  }


  "Ascending order between null and a string" should "put null before the string" in {
    val sortedList =
      List(null, "Test")
        .sortBy(_.asInstanceOf[AnyRef])(FieldOrdering.Ascending)

    sortedList should be(List(
      null,
      "Test"
    ))
  }

  "Descending order between null and a string" should "put null after the string" in {
    val sortedList =
      List(null, "Test")
        .sortBy(_.asInstanceOf[AnyRef])(FieldOrdering.Descending)

    sortedList should be(List(
      "Test",
      null
    ))
  }


  "Ascending order between null and an integer" should "put null before the integer" in {
    val sortedList =
      List(null, 9)
        .sortBy(_.asInstanceOf[AnyRef])(FieldOrdering.Ascending)

    sortedList should be(List(
      null,
      9
    ))
  }

  "Descending order between null and an integer" should "put null after the integer" in {
    val sortedList =
      List(null, 9)
        .sortBy(_.asInstanceOf[AnyRef])(FieldOrdering.Descending)

    sortedList should be(List(
      9,
      null
    ))
  }


  "Ascending order between null and a boolean" should "put null before the boolean" in {
    val sortedList =
      List(null, false)
        .sortBy(_.asInstanceOf[AnyRef])(FieldOrdering.Ascending)

    sortedList should be(List(
      null,
      false
    ))
  }

  "Descending order between null and a boolean" should "put null after the boolean" in {
    val sortedList =
      List(null, true)
        .sortBy(_.asInstanceOf[AnyRef])(FieldOrdering.Descending)

    sortedList should be(List(
      true,
      null
    ))
  }


  "Ascending order between 2 nulls" should "leave the list intact" in {
    val sortedList =
      List(null, null)
        .sortBy(_.asInstanceOf[AnyRef])(FieldOrdering.Ascending)

    sortedList should be(List(
      null,
      null
    ))
  }


  "Descending order between 2 nulls" should "leave the list intact" in {
    val sortedList =
      List(null, null)
        .sortBy(_.asInstanceOf[AnyRef])(FieldOrdering.Descending)

    sortedList should be(List(
      null,
      null
    ))
  }
}
