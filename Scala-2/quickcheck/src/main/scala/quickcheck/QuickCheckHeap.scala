package quickcheck

import org.scalacheck.*
import Arbitrary.*
import Gen.*
import Prop.forAll

import scala.annotation.tailrec
import scala.util.Try

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap :


  lazy val genHeap: Gen[H] = {
    oneOf(
      const(empty),
      for {
        value <- arbitrary[Int]
        heap <- oneOf(const(empty), genHeap)
      } yield insert(value, heap)
    )
  }

  given Arbitrary[H] = Arbitrary(genHeap)

  property("gen1") = forAll { (h: H) =>
    val m = if isEmpty(h) then 0 else findMin(h)
    findMin(insert(m, h)) == m
  }
  property("min1") = forAll { (a: Int) =>
    val h = insert(a, empty)
    findMin(h) == a
  }
  property("min2") = forAll { (a: Int, b: Int) =>
    val lesser = if (a < b) a else b
    findMin(insert(b, insert(a, empty))) == lesser
  }
  property("min3") = forAll { (h1: H, h2: H) =>
    val melded = meld(h1, h2)
    if isEmpty(melded) then Try(findMin(melded)).toOption.isEmpty
    else if isEmpty(h1) && !isEmpty(h2) then findMin(melded) == findMin(h2)
    else if !isEmpty(h1) && isEmpty(h2) then findMin(melded) == findMin(h1)
    else {
      val lesser = if findMin(h1) < findMin(h2) then findMin(h1) else findMin(h2)
      findMin(melded) == lesser
    }
  }

  property("del1") = forAll { (a: Int) =>
    isEmpty(deleteMin(insert(a, empty)))
  }
  property("del2") = forAll { (a: Int, b: Int, c: Int) =>
    val max = if a > b then if a > c then a else c else if b > c then b else c
    val h = insert(c, insert(b, insert(a, empty)))
    findMin(deleteMin(deleteMin(h))) == max

  }

  property("rec_check") = forAll { (h: H) =>
    @tailrec
    def rec_check(h: H): Boolean = if isEmpty(h) then Try(findMin(h)).toOption.isEmpty
    else if isEmpty(deleteMin(h)) then Try(findMin(deleteMin(h))).toOption.isEmpty
    else {
      val min = findMin(h)
      val rest = deleteMin(h)
      if min <= findMin(rest) then rec_check(rest) else false
    }

    rec_check(h)

  }