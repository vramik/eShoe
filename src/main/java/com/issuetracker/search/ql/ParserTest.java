package com.issuetracker.search.ql;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

/**
 * //TODO: document this
 *
 * @author Jiří Holuša
 */
public class ParserTest {

    public static void main(String[] args) {
        new ParserTest().run();
    }

    private void run() {
        try {
            //String expression = "project=\"Infinispan 6.2.0\" AND version=\"2\"";
            //String expression = "project IN (\"Infinispan 6.2.0\")";
            String expression = "project ~ \"Infinispan 6.2.0\" AND version IN (\"2\", \"3\") AND author = \"Jirka\"";
            //lexer splits input into tokens
            ANTLRStringStream input = new ANTLRStringStream(expression);
            TokenStream tokens = new CommonTokenStream( new SearchQueryLanguageLexer( input ) );

            //parser generates abstract syntax tree
            SearchQueryLanguageParser parser = new SearchQueryLanguageParser(tokens);
            SearchQueryLanguageParser.query_return ret = parser.query();

            //acquire parse result
            CommonTree ast = (CommonTree) ret.tree;

            Tree leftChild = ast.getChild(0);
            Tree rightChild = ast.getChild(1);

            //System.out.println(leftChild.getText());
            //System.out.println(rightChild.getText());
            printTree(ast);
            //return ast;
        } catch (RecognitionException e) {
            throw new IllegalStateException("Recognition exception is never thrown, only declared.");
        }
    }

    private void printTree(CommonTree ast) {
        print(ast, 0);
    }

    private void print(CommonTree tree, int level) {
//indent level
        for (int i = 0; i < level; i++)
            System.out.print("--");

//print node description: type code followed by token text
        System.out.println(" " + tree.getType() + " " + tree.getText());

//print all children
        if (tree.getChildren() != null)
            for (Object ie : tree.getChildren()) {
                print((CommonTree) ie, level + 1);
            }
    }


}
