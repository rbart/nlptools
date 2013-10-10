package edu.knowitall.repr.sentence

import edu.knowitall.collection.immutable.Interval
import edu.knowitall.tool.chunk._

trait Chunked extends Postagged {
  this: Sentence =>

  type token <: ChunkedToken

  def chunks: Seq[String] = tokens.map(_.chunk)

  def chunkIntervals: Seq[(String, Interval)] = Chunker.intervals(tokens)
}

trait Chunker extends Chunked {
  this: Sentence =>

  type token = ChunkedToken

  protected def chunk(text: String): Seq[token]

  override lazy val tokens = chunk(this.text)
}
