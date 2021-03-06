package gobblin.ingestion.google.webmaster;

import java.util.Collection;

import org.apache.commons.lang3.tuple.Pair;


public class UrlTrie {
  private final UrlTrieNode _root;
  private final String _prefix;

  /**
   * @param rootPage use the longest common prefix as your _root page.
   *                 e.g. if your pages are "www.linkedin.com/in/", "www.linkedin.com/jobs/", "www.linkedin.com/groups/"
   *                 The longest common prefix is "www.linkedin.com/", and it will be your _root page.
   *                 And the last "/" will be used as a TrieRoot.
   * @param pages
   */
  public UrlTrie(String rootPage, Collection<String> pages) {
    Pair<String, UrlTrieNode> defaults = getPrefixAndDefaultRoot(rootPage);
    _prefix = defaults.getLeft();
    _root = defaults.getRight();
    for (String page : pages) {
      add(page);
    }
  }

  /**
   * prefix is different from RootPage that the RootPage has an extra char in the end. And this last char will be used to construct the root node of the trie.
   */
  public UrlTrie(String rootPage, UrlTrieNode root) {
    Pair<String, UrlTrieNode> defaults = getPrefixAndDefaultRoot(rootPage);
    _prefix = defaults.getLeft();
    _root = root;
  }

  private Pair<String, UrlTrieNode> getPrefixAndDefaultRoot(String rootPage) {
    if (rootPage == null || rootPage.isEmpty()) {
      return Pair.of(null, new UrlTrieNode(null));
    } else {
      String prefix = rootPage.substring(0, rootPage.length() - 1);
      Character lastChar = rootPage.charAt(rootPage.length() - 1);
      return Pair.of(prefix, new UrlTrieNode(lastChar));
    }
  }

  public void add(String page) {
    if (_prefix == null || _prefix.isEmpty()) {
      _root.add(page);
    } else {
      if (!page.startsWith(_prefix)) {
        throw new IllegalArgumentException(
            String.format("Found a page '%s' not starting with the root page '%s'", page, _prefix));
      }
      _root.add(page.substring(_prefix.length() + 1)); //1 comes from the last char in root.
    }
  }

  public UrlTrieNode getChild(String path) {
    return _root.getChild(path);
  }

  public UrlTrieNode getRoot() {
    return _root;
  }

  public String getPrefix() {
    return _prefix;
  }
}

