package canthrow

import org.scalatest.{FunSuite, Matchers}

import scala.util.{Failure, Success}

class CanThrowTest extends FunSuite with Matchers {

  test("canthrow apply") {
    CanThrow(throw new Exception()) should be(HasThrown)
  }

  test("canthrow from try") {
    CanThrow.from(Success(())) should be(HasSucceeded)
    CanThrow.from(Failure(new Exception())) should be(HasThrown)
  }

  test("canthrow from boolean") {
    CanThrow.from(true) should be(HasSucceeded)
    CanThrow.from(false) should be(HasThrown)
  }

  test("canthrow from either") {
    CanThrow.from(Right(())) should be(HasSucceeded)
    CanThrow.from(Left(())) should be(HasThrown)
  }

  test("canthrow from option") {
    CanThrow.from(Some(())) should be(HasSucceeded)
    CanThrow.from(None) should be(HasThrown)
  }

  test("canthrow and") {
    HasSucceeded.and(HasThrown) should be(HasThrown)
    HasThrown.and(HasThrown) should be(HasThrown)
    HasThrown.and(HasSucceeded) should be(HasThrown)
    HasSucceeded.and(HasSucceeded) should be(HasSucceeded)
  }

  test("canthrow map") {
    HasSucceeded.map(() => ()) should be(HasSucceeded)
    HasThrown.map(() => ()) should be(HasThrown)
    HasSucceeded.map(() => throw new Exception()) should be(HasThrown)
    HasThrown.map(() => throw new Exception()) should be(HasThrown)
  }

  test("canthrow flatMap") {
    HasSucceeded.flatMap(() => HasThrown) should be(HasThrown)
    HasThrown.flatMap(() => HasThrown) should be(HasThrown)
    HasThrown.flatMap(() => HasSucceeded) should be(HasThrown)
    HasSucceeded.flatMap(() => HasSucceeded) should be(HasSucceeded)
    HasSucceeded.flatMap(() => throw new Exception()) should be(HasThrown)
    HasThrown.flatMap(() => throw new Exception()) should be(HasThrown)
  }

  test("canthrow foreach") {
    var set = Set[Int]()
    HasSucceeded.foreach(() => set += 0)
    HasThrown.foreach(() => set += 1)
    HasSucceeded.foreach(() => throw new Exception())
    set should be(Set(0))
  }

  test("canthrow hasSucceeded") {
    HasSucceeded.hasSucceeded should be(true)
    HasThrown.hasSucceeded should be(false)
  }

  test("canthrow hasFailed") {
    HasSucceeded.hasThrown should be(false)
    HasThrown.hasThrown should be(true)
  }

  test("canthrow toOption") {
    HasSucceeded.toOption should be(Some(()))
    HasThrown.toOption should be(None)
  }

  test("canthrow toEither") {
    HasSucceeded.toEither should be(Right(()))
    HasThrown.toEither should be(Left(()))
  }

}