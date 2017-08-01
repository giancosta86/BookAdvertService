package info.gianlucacosta.bookadvertservice.io.dynamo

/**
  * Provides actual access to FieldOrdering instances
  */
private object FieldOrdering {
  val Ascending =
    new FieldOrdering(true)

  val Descending =
    new FieldOrdering(false)

  def get(ascending: Boolean): FieldOrdering =
    if (ascending)
      Ascending
    else
      Descending
}

/**
  * Sorts the values of a field, in ascending or descending order.
  * As for the values of every pair checked by the sorting algorithm:
  *
  * <ul>
  * <li>if they are both Comparable, their natural ordering is used</li>
  * <li>if just one of them is NOT Comparable, it is assumed before the Comparable value</li>
  * <li>if they are both NOT Comparable, they are assumed to be equal</li>
  * </ul>
  */
private class FieldOrdering private(ascending: Boolean) extends Ordering[AnyRef] {
  override def compare(left: AnyRef, right: AnyRef): Int = {
    val ascendingComparisonResult = {
      val isLeftComparable =
        left.isInstanceOf[Comparable[_]]

      val isRightComparable =
        right.isInstanceOf[Comparable[_]]

      if (isLeftComparable && isRightComparable)
        left.asInstanceOf[Comparable[AnyRef]].compareTo(right)
      else if (isLeftComparable)
        1
      else if (isRightComparable)
        -1
      else
        0
    }

    if (ascending)
      ascendingComparisonResult
    else
      -ascendingComparisonResult
  }
}
