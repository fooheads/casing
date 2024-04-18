(ns fooheads.casing
  "Casing functions, from/to camel, kebab, pascal and snake. An optional
  coersion function can be provided to change the end result into another type,
  (typically keyword, symbol or simliar).

  A lot of the code could have been generated, but keeing it explicit was an
  explicit choise to avoid macros and similar in this code."
  (:require
    [fooheads.casing.coerce :as coerce]
    [fooheads.casing.string :as string]))


(defn- as
  [to ns name]
  (let [f (cond
            (keyword? to) keyword
            (symbol? to)  symbol
            (string? to)  str
            :else to)]
    (if ns
      (f ns name)
      (f name))))


(defn convert
  "Converts a string or an ident, and either keeps the type or coerce it.

  Supports qualified idents. Uses the provided functions to split, map, join
  and coerce."
  ([split-fn map-fn join-fn x]
   (convert split-fn map-fn join-fn nil x))

  ([split-fn map-fn join-fn coerce-fn x]
   (let [[nspace namn]
         (cond
           (qualified-ident? x)
           [(namespace x) (name x)]

           :else
           [nil (name x)])

         nspace (when nspace (string/convert split-fn map-fn join-fn nspace))
         namn   (string/convert split-fn map-fn join-fn namn)

         coerce-fn (cond
                     coerce-fn coerce-fn
                     (keyword? x) coerce/keyword
                     (symbol? x) coerce/symbol
                     (string? x) coerce/string)]

     ;; TODO: how to handle nil nspace and coercion?
     (if coerce-fn
       (coerce-fn nspace namn)
       (as x nspace namn)))))

