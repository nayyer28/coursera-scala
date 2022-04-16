import org.scalacheck.*
import Arbitrary.*
import Gen.*
import Prop.{forAll, iff}

for{
  i <- Arbitrary[Int]
} yield i