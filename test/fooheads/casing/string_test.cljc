(ns fooheads.casing.string-test
  (:require
    [clojure.test :refer [are deftest]]
    [fooheads.casing.string :as string]))


(deftest split-dash-test
  (are [expected s] (= expected (string/split-dash s))
    ["kebab" "case"]  "kebab-case"
    ["snake_case"]  "snake_case"
    ["lower" "UPPER" "Pascal" "123"]  "lower-UPPER-Pascal-123"
    ["foo" "bar"]  "---foo---bar---"
    ["two words"]  "two words"
    ["version2"]  "version2"
    ["version" "3"]  "version-3"))


(deftest split-underscore-test
  (are [expected s] (= expected (string/split-underscore s))
    ["snake" "case"]  "snake_case"
    ["kebab-case"]  "kebab-case"
    ["foo" "bar"]  "___foo___bar___"
    ["two words"]  "two words"
    ["lower" "UPPER" "Pascal" "123"]  "lower_UPPER_Pascal_123"
    ["version2"]  "version2"
    ["version" "3"]  "version_3"))


(deftest split-word-test
  (are [expected s] (= expected (string/split-word s))
    ;; typical cases
    ["Some" "Pascal" "Case" "String"]     "SomePascalCaseString"
    ["some" "Camel" "Case" "String"]      "someCamelCaseString"

    ;; numbers
    ["some" "100" "Songs"]                "some100Songs"
    ["version" "2"]                       "version2"

    ;; abbreviations
    ["some" "GDPR" "String"]              "someGDPRString"
    ["some" "ID"]                         "someID"
    ["GDPR" "String"]                     "GDPRString"
    ["GDPR"]                              "GDPR"

    ;; abbreviations with numbers
    ["some" "ID" "1984" "string"]            "someID1984string"
    ["some" "ID" "1984"]                     "someID1984"

    ;; non word chars
    ["with" "_" "some" "." "Separators"]  "with_some.Separators"
    ["two" " " "words"]                   "two words"
    ["version" "-" "3"]                   "version-3"

    ;; non-ascii
    ["Göteborg" "Öland"]                  "GöteborgÖland"
    ["Göteborg" "2" "Öland"]              "Göteborg2Öland"))


(deftest split-wordnum-test
  (are [expected s] (= expected (string/split-wordnum s))
    ;; typical cases
    ["Some" "Pascal" "Case" "String"]     "SomePascalCaseString"
    ["some" "Camel" "Case" "String"]      "someCamelCaseString"

    ;; numbers
    ["some100" "Songs"]                   "some100Songs"
    ["version2"]                          "version2"

    ;; abbreviations
    ["some" "GDPR" "String"]              "someGDPRString"
    ["some" "ID"]                         "someID"
    ["GDPR" "String"]                     "GDPRString"
    ["GDPR"]                              "GDPR"

    ;; abbreviations with numbers
    ["some" "ID1984" "string"]            "someID1984string"
    ["some" "ID1984"]                     "someID1984"

    ;; non word chars
    ["with" "_" "some" "." "Separators"]  "with_some.Separators"
    ["two" " " "words"]                   "two words"
    ["version" "-" "3"]                   "version-3"

    ;; non-ascii
    ["Göteborg" "Öland"]                  "GöteborgÖland"
    ["Göteborg2" "Öland"]                 "Göteborg2Öland"))


(deftest map-camel-test
  (are [expected ss] (= expected (string/map-camel ss))
    ["some" "Long" "Name2"]                 ["some" "long" "name2"]
    ["some" "Long" "Name2"]                 ["Some" "Long" "Name2"]
    ["some" "Long" "Name2"]                 ["SOME" "LONG" "NAME2"]))


(deftest map-lower-test
  (are [expected ss] (= expected (string/map-lower ss))
    ["some" "long" "name2"]                 ["some" "long" "name2"]
    ["some" "long" "name2"]                 ["Some" "Long" "Name2"]
    ["some" "long" "name2"]                 ["SOME" "LONG" "NAME2"]))


(deftest map-pascal-test
  (are [expected ss] (= expected (string/map-pascal ss))
    ["Some" "Long" "Name2"]                 ["some" "long" "name2"]
    ["Some" "Long" "Name2"]                 ["Some" "Long" "Name2"]
    ["Some" "Long" "Name2"]                 ["SOME" "LONG" "NAME2"]))


(deftest map-upper-test
  (are [expected ss] (= expected (string/map-upper ss))
    ["SOME" "LONG" "NAME2"]                 ["some" "long" "name2"]
    ["SOME" "LONG" "NAME2"]                 ["Some" "Long" "Name2"]
    ["SOME" "LONG" "NAME2"]                 ["SOME" "LONG" "NAME2"]))

