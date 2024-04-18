(ns fooheads.casing-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [fooheads.casing :as c]
    [fooheads.casing.coerce :as coerce]
    [fooheads.casing.string :as cs]))


;;
;; Some example functions that you would typically setup in your
;; own code base according to your situation and data shape.
;;

(def pascal->camel
  (partial c/convert cs/split-wordnum cs/map-camel cs/join-blank))


(def pascal->kebab
  (partial c/convert cs/split-wordnum cs/map-lower cs/join-dash))


(def pascal->kebab-keyword
  (partial c/convert cs/split-wordnum cs/map-lower cs/join-dash coerce/keyword))


(def snake->kebab
  (partial c/convert cs/split-underscore cs/map-lower cs/join-dash))


(def snake->pascal
  (partial c/convert cs/split-underscore cs/map-pascal cs/join-blank))


(deftest convert-test
  (testing "conversion"
    (is (= 'someNameSpace/someName12
           (pascal->camel 'someNameSpace/someName12)))

    (is (= :some-namespace/some-name12
           (snake->kebab :some_namespace/some_name12)))

    (is (= 'some-name-space/some-name12
           (pascal->kebab 'someNameSpace/someName12)))

    (is (= :some-name-space/some-name12
           (pascal->kebab-keyword 'someNameSpace/someName12)))

    (is (= "ΓειαΣουΚόσμε"
          (snake->pascal "γεια_σου_κόσμε"))))

  (testing "with coerce"
    (is (= :someNameSpace/someName12
           (pascal->camel coerce/keyword 'someNameSpace/someName12)))

    (is (= 'someName12
           (pascal->camel coerce/simple-symbol :someNameSpace/someName12)))))

