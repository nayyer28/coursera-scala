package calculator

enum Expr:
  case Literal(v: Double)
  case Ref(name: String)
  case Plus(a: Expr, b: Expr)
  case Minus(a: Expr, b: Expr)
  case Times(a: Expr, b: Expr)
  case Divide(a: Expr, b: Expr)

object Calculator extends CalculatorInterface:
 import Expr.*

  def computeValues(
      namedExpressions: Map[String, Signal[Expr]]): Map[String, Signal[Double]] =
    namedExpressions.map{
      r => r._1 -> Signal(eval(r._2())(using namedExpressions.filterNot(_._1 == r._1)))
    }

  def eval(expr: Expr)(using references: Map[String, Signal[Expr]])(using Signal.Caller): Double =
    expr match {
      case Literal(v) => v
      case Ref(name) => eval(getReferenceExpr(name, references))
      case Plus(a,b) => eval(a) + eval (b)
      case Minus(a,b) => eval(a) - eval(b)
      case Times(a,b) => eval(a) * eval(b)
      case Divide(a, b) => if eval(b) == 0 then throw new ArithmeticException("cannot divide by 0") else eval(a)/eval(b)
    }

  /** Get the Expr for a referenced variables.
   *  If the variable is not known, returns a literal NaN.
   */
  private def getReferenceExpr(name: String,
      references: Map[String, Signal[Expr]])(using Signal.Caller): Expr =
    references.get(name).fold[Expr] {
      Literal(Double.NaN)
    } { exprSignal =>
      exprSignal()
    }
