package funsets

/**
 * This class is a test suite for the methods in object FunSets.
 *
 * To run this test suite, start "sbt" then run the "test" command.
 */
class FunSetSuite extends munit.FunSuite:

  import FunSets.*

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets:
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)

  /**
   * This test is currently disabled (by using @Ignore) because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", remove the
   * .ignore annotation.
   */
  test("singleton set one contains one") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets:
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), s"Singleton test failed. Not {1}")
      assert(contains(s2, 2), s"Singleton test failed. Not {2}")
      assert(contains(s3, 3), s"Singleton test failed. Not {3}")
  }

  test("union contains all elements of each set") {
    new TestSets:
      val s = union(s1, s2)
      assert(contains(s, 1), "1 not in union")
      assert(contains(s, 2), "2 not in union")
      assert(!contains(s, 3), "3 in union")
  }

  test("intersect contains the common elements of two sets"){
    new TestSets:
      val s = intersect(s1,s2)
      val t = intersect(union(s1,s2), s1)
      val u = intersect(union(s2,s3), s3)
      assert(!contains(s, 1), "1 in intersection of {1} and {2}")
      assert(contains(t, 1), "1 not in intersection of {1,2} and {1}")
      assert(contains(u, 3), "3 not in intersection of {2,3} and {3}")
  }
  test("first set deprived of second set"){
    new TestSets:
      val s = union(s3,union(s1,s2))
      val t = union(s1,s2)
      val u = diff(s,t)
      assert(!contains(u, 1), "1 in diff of {1,2,3} and {1,2}")
      assert(contains(u, 3), "3 not in diff of {1,2,3} and {1,2}")
  }
  test("first set filtered by predicate."){
    new TestSets:
      val s = union(s3,union(s1,s2))
      val p = (x:Int) => x>0 && x<3
      val u = filter(s,p)
      assert(contains(u, 1), "1 not in filtered subset of {1,2,3} by x>0 and x<3")
      assert(!contains(u, 3), "3 in filtered subset of {1,2,3} by x>0 and x<3")
  }
  test("all elements of a set satisfy a predicate."){
    new TestSets:
      val s = union(s3,union(s1,s2))
      val p = (x:Int) => x>0
      assert(forall(s, p), "all elements of set don't satisfy x>0")
      assert(!forall(x => !s(x), p), "all elements of set satisfy x>0")
  }
  test("at least one elements of a set satisfies a predicate."){
    new TestSets:
      val s = union(s3,union(s1,s2))
      val p = (x:Int) => x<2
      assert(exists(s, p), "none element of set satisfies  x<2")
      assert(!exists(x => !s(x), x => x==2), "at least one element of set satisfies x==2")
  }
  test("maps one set to another."){
    new TestSets:
      val s = union(s3,union(s1,s2))
      val m = map(s, x => x+10)
      assert(contains(m, 11), "no element of s was mapped to 11")
  }

  import scala.concurrent.duration.*
  override val munitTimeout = 10.seconds
