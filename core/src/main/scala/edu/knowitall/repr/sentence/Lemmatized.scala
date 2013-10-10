package edu.knowitall.repr.sentence

import edu.knowitall.tool.stem._

trait Lemmatized {
  tokenized: TokenizedSupertrait =>

  def lemmatizedTokens: Seq[edu.knowitall.tool.stem.Lemmatized[token]]
}

trait Lemmatizer extends Lemmatized {
  tokenized: TokenizedSupertrait =>

  def lemmatize(t: token): edu.knowitall.tool.stem.Lemmatized[token]

  override lazy val lemmatizedTokens: Seq[edu.knowitall.tool.stem.Lemmatized[token]] =
    tokenized.tokens map lemmatize
}
