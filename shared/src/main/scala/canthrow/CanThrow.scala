package canthrow

import scala.util.{Failure, Success, Try}

case object HasThrown extends CanThrow
case object HasSucceeded extends CanThrow

sealed trait CanThrow {

  @inline
  def flatMap(fun: () => CanThrow): CanThrow = {
    this match {
      case HasSucceeded => try { fun() } catch { case _: Throwable => HasThrown }
      case HasThrown => HasThrown
    }
  }

  @inline
  def map(fun: () => Any): CanThrow = {
    this match {
      case HasSucceeded => CanThrow(fun())
      case HasThrown => HasThrown
    }
  }

  @inline
  def foreach(fun: () => Any): Unit = {
    if (this == HasSucceeded) {
      try {
        fun()
      } catch {
        case _: Throwable =>
      }
    }
  }

  @inline
  def and(other: CanThrow): CanThrow = {
    flatMap(() => other)
  }

  @inline
  def toOption: Option[Unit] = this match {
    case HasSucceeded => Some(())
    case HasThrown => None
  }

  @inline
  def toEither: Either[Unit, Unit] = this match {
    case HasSucceeded => Right(())
    case HasThrown => Left(())
  }

  @inline
  def hasSucceeded: Boolean = this == HasSucceeded

  @inline
  def hasThrown: Boolean = this == HasThrown

}

object CanThrow {

  @inline
  def apply(f: => Any): CanThrow = try {
    f; HasSucceeded
  } catch {
    case _: Throwable => HasThrown
  }

  @inline
  def from(t: Try[Any]): CanThrow = t match {
    case Success(_) => HasSucceeded
    case Failure(_) => HasThrown
  }

  @inline
  def from(o: Option[Any]): CanThrow = o match {
    case Some(_) => HasSucceeded
    case None => HasThrown
  }

  @inline
  def from(e: Either[Any, Any]): CanThrow = e match {
    case Right(_) => HasSucceeded
    case Left(_) => HasThrown
  }

  @inline
  def from(b: Boolean): CanThrow =
    if (b) HasSucceeded else HasThrown

}

