package edu.knowitall
package tool
package parse

import java.util.zip.ZipInputStream

import scala.collection.JavaConverters.asScalaIteratorConverter

import com.googlecode.clearnlp.component.dep.CDEPPassParser
import com.googlecode.clearnlp.component.morph.CEnglishMPAnalyzer
import com.googlecode.clearnlp.dependency.DEPNode
import com.googlecode.clearnlp.dependency.DEPTree

import edu.knowitall.common.Resource.using
import edu.knowitall.tool.parse.graph.DependencyGraph
import edu.knowitall.tool.parse.graph.DependencyNode
import edu.knowitall.tool.postag.ClearPostagger
import edu.knowitall.tool.postag.PostaggedToken
import edu.knowitall.tool.postag.Postagger
import edu.knowitall.tool.tokenize.Token
import edu.knowitall.tool.tokenize.Tokenizer
import graph.Dependency

class ClearParser(val postagger: Postagger = new ClearPostagger()) extends DependencyParser {
  val clearMorphaUrl = this.getClass.getResource("/edu/knowitall/tool/tokenize/dictionary-1.2.0.zip")
  require(clearMorphaUrl != null, "cannot find clear dict model")
  val clearMorpha = using(clearMorphaUrl.openStream()) { input =>
    new CEnglishMPAnalyzer(new ZipInputStream(input))
  }

  val clearDepUrl = this.getClass.getResource("/knowitall/models/clear/ontonotes-en-dep-1.3.0.jar")
  require(clearDepUrl != null, "cannot find clear dep model")
  val clearDepParser = using (clearDepUrl.openStream()) { input =>
    new CDEPPassParser(new ZipInputStream(input))
  }

  override def dependencyGraph(string: String): DependencyGraph = {
    val tokens = postagger.postag(string)
    this.dependencyGraph(string, tokens)
  }

  def dependencyGraph(tokens: Seq[PostaggedToken]): DependencyGraph = {
    this.dependencyGraph(Tokenizer.originalText(tokens), tokens)
  }

  def dependencyGraph(string: String, tokens: Seq[PostaggedToken]) = {
    val tree = new DEPTree()
    tokens.zipWithIndex.foreach { case (token, i) =>
      val node = new DEPNode(i + 1, token.string)
      node.pos = token.postag
      tree.add(node)
    }

    clearMorpha.process(tree)
    clearDepParser.process(tree)

    ClearParser.graphFromTree(string, tree, tokens)
  }
}

object ClearParser {
  def graphFromTree(string: String, tree: DEPTree, tokens: Seq[Token]) = {
    val nodeMap = (for ((node, i) <- tree.iterator.asScala.zipWithIndex) yield {
      if (i == 0) node.id -> new DependencyNode(node.form, node.pos, -1, -1)
      else node.id -> new DependencyNode(node.form, node.pos, i - 1, tokens(i - 1).offset)
    }).toMap

    val deps = for {
      sourceNode <- tree.iterator.asScala.toList
      if sourceNode.hasHead
      if sourceNode.id != 0
      label = sourceNode.getLabel
      destNode = sourceNode.getHead
      if destNode.id != 0
    } yield {
      new Dependency(nodeMap(destNode.id), nodeMap(sourceNode.id), label)
    }

    new DependencyGraph(nodeMap.values filterNot (_.index == -1), deps)
  }
}

object ClearDependencyParserMain extends DependencyParserMain {
  lazy val dependencyParser = new ClearParser()
}
