package edu.knowitall.repr.sentence

import edu.knowitall.tool.postag._

trait PostaggedSupertrait extends TokenizedSupertrait {
  this: Sentence =>

  type token <: PostaggedToken

  def postags: Seq[String] = tokens.map(_.postag)
}

trait Postagged extends PostaggedSupertrait {
  this: Sentence =>

  type token = PostaggedToken
}

trait Postagger extends Postagged {
  this: Sentence =>

  def postag(text: String): Seq[PostaggedToken]

  override lazy val tokens = postag(this.text)
}

