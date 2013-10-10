package edu.knowitall.repr.sentence

import edu.knowitall.tool.tokenize._
import edu.knowitall.tool.postag._
import edu.knowitall.tool.chunk._
import edu.knowitall.tool.stem._

trait Tokenized {
  this: Sentence =>
  type token <: Token

  def tokens: Seq[token]

  def strings: Seq[String] = tokens.map(_.string)
}

trait Tokenizer extends Tokenized {
  this: Sentence =>

  type token = Token

  def tokenizer: edu.knowitall.tool.tokenize.Tokenizer

  override lazy val tokens = tokenizer.tokenize(text)
}