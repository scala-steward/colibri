package colibri

import colibri.effect._
import scala.concurrent.Future

trait ObservableLike[-F[_]] {
  def toObservable[A](source: F[A]): Observable[A]
}
object ObservableLike       {
  @inline def apply[F[_]](implicit like: ObservableLike[F]): ObservableLike[F] = like

  implicit def observableSource[H[_]: Source]: ObservableLike[H] = new ObservableLike[H] {
    def toObservable[A](source: H[A]): Observable[A] = Observable.lift(source)
  }

  implicit def observableSyncEffect[F[_]: RunEffect]: ObservableLike[F] = new ObservableLike[F] {
    def toObservable[A](effect: F[A]): Observable[A] = Observable.fromEffect(effect)
  }

  implicit val observableFuture: ObservableLike[Future] = new ObservableLike[Future] {
    def toObservable[A](future: Future[A]): Observable[A] = Observable.fromFuture(future)
  }
}
