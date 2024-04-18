(ns fooheads.casing.coerce
  "Coerce functions that takes two strings, a namespace and a name,
  and turns it into something."
  (:refer-clojure :exclude [keyword name namespace symbol])
  (:require
    [clojure.core :as clojure]))


(def keyword
  "The same as clojure.core/keyword. Only included for completeness."
  clojure/keyword)


(def symbol
  "The same as clojure.core/symbol Only included for completeness."
  clojure/symbol)


(defn simple-symbol
  "Returns a simple symbol for the name, ignoring ns"
  [_ns name]
  (symbol name))


(defn simple-keyword
  "Returns a simple keyword for the name, ignoring ns"
  [_ns name]
  (keyword name))


(defn string
  "A string version of the symbol and keyword functions. Takes one or two
  strings and turns them into a string. When both ns and name
  is provided (and ns is not nil), the ns and name are joined with a /"
  ([ns name]
   (if ns
     (str ns "/" name)
     (str name))))


