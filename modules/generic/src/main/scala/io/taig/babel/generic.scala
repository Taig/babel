package io.taig.babel

object generic {
  object semiauto {
    def deriveEncoder[F[_], A](implicit encoder: DerivedEncoder[F[A], A]): Encoder[F, A] = new Encoder[F, A] {
      override def encode(value: F[A]): Segments[A] = encoder.encode(value)
    }

    def deriveDecoder[F[_], A](implicit decoder: DerivedDecoder[A, F[A]]): Decoder[F, A] = new Decoder[F, A] {
      override def decode(path: Path, values: Segments[A]): Either[Decoder.Error, F[A]] = decoder.decode(path, values)
    }
  }

  object auto {
    implicit def derivedEncoder[F[_], A](implicit encoder: DerivedEncoder[F[A], A]): Encoder[F, A] =
      semiauto.deriveEncoder(encoder)

    implicit def derivedDecoder[F[_], A](implicit decoder: DerivedDecoder[A, F[A]]): Decoder[F, A] =
      semiauto.deriveDecoder(decoder)
  }
}