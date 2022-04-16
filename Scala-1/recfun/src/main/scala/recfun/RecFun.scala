package recfun

object RecFun extends RecFunInterface:

  def main(args: Array[String]): Unit =
    println("Pascal's Triangle")
    for row <- 0 to 10 do
      for col <- 0 to row do
        print(s"${pascal(col, row)} ")
      println()

    println(balance("just an) example".toList))
    println(countChange(4, List(1,2)))

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0) || (c == r) then 1
    else pascal(c-1, r-1) + pascal(c, r-1)
  }

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def countParans(chars:List[Char], count:Int=0): Boolean ={
      if chars.isEmpty then count == 0
      else if chars.head == '(' then  countParans(chars.tail, count+1)
      else if chars.head == ')' then count >0 && countParans(chars.tail, count-1)
      else countParans(chars.tail, count)
    }
    countParans(chars)
  }

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    if money == 0 then 1
    else if(money>0 && coins.nonEmpty) then
      countChange(money-coins.head, coins) + countChange(money, coins.tail)
    else 0
  }
