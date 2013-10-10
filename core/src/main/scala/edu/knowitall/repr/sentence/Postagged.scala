package edu.knowitall.repr.sentence

import edu.knowitall.tool.postag._

trait Postagged extends Tokenized {
  this: Sentence =>

  type token <: PostaggedToken

  def postags: Seq[String] = tokens.map(_.postag)
}

trait Postagger extends Postagged {
  this: Sentence =>

  type token = PostaggedToken

  protected def postag(text: String): Seq[token]

  override lazy val tokens = postag(this.text)
}