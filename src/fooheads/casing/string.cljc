(ns fooheads.casing.string
  "The core casing functions, operating only on strings. Some functions
  here are very trival and could be inlined elsewhere, but exists for
  completeness for the consuming code."
  (:refer-clojure :exclude [re-pattern])
  (:require
    [clojure.string :as str]))


(defn- re-pattern
  "In Clojurescipt, the default regular expressions does not support unicode,
  so we need to use strings and create a re-pattern function that works the
  'same' in both enviroments."
  [s]
  #?(:cljs (js/RegExp. s "u")
     :clj (clojure.core/re-pattern s)))


(def ^:private str-re-non-word             "[^\\p{L}]+")
(def ^:private str-re-lower-word           "\\p{Ll}+")
(def ^:private str-re-lower-wordnum        "[\\p{Ll}\\p{N}]+")
(def ^:private str-re-title-word           "\\p{Lu}\\p{Ll}+")
(def ^:private str-re-title-wordnum        "\\p{Lu}[\\p{Ll}\\p{N}]+")
(def ^:private str-re-abbr-before-non-word "\\p{Lu}+(?=[^\\p{L}])")
(def ^:private str-re-abbr-before-word     "\\p{Lu}+(?=\\p{Lu}\\p{Ll})")
(def ^:private str-re-abbr-ending-with-num "\\p{Lu}+\\p{N}+")
(def ^:private str-re-abbr-end             "\\p{Lu}+$")
(def ^:private str-re-punct                "\\p{P}+")
(def ^:private str-re-any                  ".+")


(defn- alt
  "Takes regexps and creates an alternating regexp from them. This function
  exists to make it easy to compose regexps without loosing your mind."
  [& regexps]
  (->> regexps (map str) (str/join "|") (re-pattern)))


(def ^:private re-word-groups
  "regex to group words, separating numbers from words.
  Example: version2 becomes 'version' and '2'"
  (alt
    str-re-punct
    str-re-non-word
    str-re-title-word
    str-re-lower-word
    str-re-abbr-end
    str-re-abbr-before-non-word
    str-re-abbr-before-word
    str-re-any))


(def ^:private re-wordnum-groups
  "regex to group words, keeping numbers after words in the words.
  Example: version2 becomes 'version2'"
  (alt
    str-re-punct
    str-re-non-word
    str-re-title-wordnum
    str-re-lower-wordnum
    str-re-abbr-end
    str-re-abbr-ending-with-num
    str-re-abbr-before-word
    str-re-any))


(defn split
  "Splits using a regexp and removes empty matches. The base for more
  specific split functions."
  [re s]
  (remove empty? (str/split s re)))


(defn group
  "Groups (re-seq) using a regexp and removes empty matches. The base for more
  specific split functions."
  [re s]
  (remove empty? (re-seq re s)))


(defn split-dash
  "Splits a string on dash and returns the parts. The - is not kept."
  [s]
  (split #"-+" s))


(defn split-underscore
  "Splits a string on underscore and returns the parts. The _ is not kept."
  [s]
  (split #"_+" s))


(defn split-word
  "Split a camel case or pascal case string into words. Keeps numbers and words
  separate, which means that version2 becomes 'version' and '2'"
  [s]
  (group re-word-groups s))


(defn split-wordnum
  "Split a camel case or pascal case string into wordnums. Keeps words and
  numbers together, which means that version2 becomes 'version2'"
  [s]
  (group re-wordnum-groups s))


(defn map-camel
  "Maps a list of strings into camel case. Downcase the first string
  and downcase + capitalize the rest"
  [strings]
  (let [strings (map str/lower-case strings)
        [x & xs] strings]
    (cons x (map str/capitalize xs))))


(defn map-lower
  "Maps a list of strings into lower case."
  [strings]
  (map str/lower-case strings))


(defn map-pascal
  "Maps a list of strings into pascal case. Downcase + capitalize each string."
  [strings]
  (map (comp str/capitalize str/lower-case) strings))


(defn map-upper
  "Maps a list of strings into upper case."
  [strings]
  (map str/upper-case strings))


(defn join-underscore
  "Joins strings with _ as a separator"
  [strings]
  (str/join "_" strings))


(defn join-dash
  "Joins strings with - as a separator"
  [strings]
  (str/join "-" strings))


(defn join-blank
  "Joins strings without a separator"
  [strings]
  (str/join "" strings))


(defn convert
  "Converts a string from one format to another. Splits, maps and joins using
  the provided functions."
  [split-fn map-fn join-fn s]
  (->> s (split-fn) (map-fn) (join-fn)))

