package scalashop

import java.util.concurrent.*
import scala.collection.*

class BlurSuite extends munit.FunSuite:
  // Put tests here
  test("boxBlurKernel for 3x4  image"){
    //val values = Array(1,2,1,2,1,2,1,2,1,2,1,2)
    val values = Array.fill(12)(1)
    val image = new Img(3,4, values)
    assert(boxBlurKernel(image,1,2,1) == 1)
  }
