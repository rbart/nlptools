package edu.knowitall.repr.sentence

import edu.knowitall.tool.stem._

trait Lemmatized {
  tokenized: Tokenized =>

  def lemmatizedTokens: Seq[edu.knowitall.tool.stem.Lemmatized[token]]
}

trait Lemmatizer extends Lemmatized {
  tokenized: Tokenized =>

  protected def lemmatize(t: token): edu.knowitall.tool.stem.Lemmatized[token]

  override lazy val lemmatizedTokens: Seq[edu.knowitall.tool.stem.Lemmatized[token]] =
    tokenized.tokens map lemmatize
}
