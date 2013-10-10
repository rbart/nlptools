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

  def postagger: edu.knowitall.tool.postag.Postagger

  override lazy val tokens = postagger.postag(this.text)
}

