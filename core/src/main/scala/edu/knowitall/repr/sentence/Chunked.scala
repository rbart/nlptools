package edu.knowitall.repr.sentence

import edu.knowitall.collection.immutable.Interval
import edu.knowitall.tool.chunk._

trait ChunkedSupertrait extends PostaggedSupertrait {
  this: Sentence =>

  type token <: ChunkedToken

  def chunks: Seq[String] = tokens.map(_.chunk)
  def chunkIntervals: Seq[(String, Interval)] = Chunker.intervals(tokens)
}

trait Chunked extends ChunkedSupertrait {
  this: Sentence =>

  type token = ChunkedToken
}

trait Chunker extends Chunked {
  this: Sentence =>

  def chunk(text: String): Seq[ChunkedToken]

  override def tokens = chunk(this.text)
}
