import org.scalacheck.*
import Arbitrary.*
import Gen.*
import Prop.{forAll, iff}

/*for{
  i <- Arbitrary[Int]
} yield i*/

val m = Map('first' -> 'Saahil', 'last'-> 'Nayyer')
m.map(_._1)
