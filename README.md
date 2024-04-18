# casing

A Clojure/Clojurescript lib and toolbox to easily compose your own
converion functions to convert between different type of casings (kebab,
snake, camel, pascal and similar).

## Why not use just use the camel-snake-kebab lib?

[camel-snake-kebab](https://github.com/clj-commons/camel-snake-kebab) is
an easy to use library, but if your use case doesn't match
the lib exactly, it quickly gets harder to use. It lacks support for
namespaced keywords and symbols and it's character classification does not
take unicode character classification into account, which stops some use
cases.

## Approach

Instead of providing a number of conversion functions from a to b (in
whatever casing-style a and b might be), this library tries to take a more
composable DIY approach.

Since there is no authority on what each casing is called (do you call
`:this-thing` kebab-case or lisp-case?), nor any exact rules for
a particular casing (is `:this-Thing` still kebab-case or something
else?), the approach we take is "build-your-own" using the `convert`
function.

`convert` takes a `split-fn`, a `map-fn` a `join-fn`, an optional
`coerce-fn` and a value to convert. To define your own thing, you
partially apply the `convert` function and give it a name.

## Examples

Let's say you want to convert from pascal-case to kebab-case, then you can
define that function like this:

```clojure
(require
  '[fooheads.casing :as casing]
  '[fooheads.casing.coerce :as coerce]
  '[fooheads.casing.string :as string])


(def pascal->kebab
  (partial casing/convert string/split-wordnums string/map-lower string/join-kebab))```

If you want to convert from snake-case to camel-case and always get
a keyword back, you can create a function like this:

```clojure
(def snake->camel-keyword
  (partial casing/convert string/split-snake string/map-camel string/join-blank coerce/keyword))```


What's important is that when you need to step out of the most common
cases, you just provide your own function for splitting,
mapping, joining or coercing, but still have the other parts available to
you. Let's say you want to convert something like `special_casing` into
`spEcIAl-cAsIng`, you provide your own function that downcase consonants
and upcase vowels:

```clojure
(def snake->vowel-kebab
  (partial casing/convert string/split-snake my-mixed-case string/join-dash))```


## The functions

### split-fn

`split-fn` takes a string and splits it into a seq of strings.

  * `split-dash` - splits on dashes
  * `split-underscore` - splits on underscore
  * `split-word` - splits a typical `camelCase` or `PascalCase` string
  * `split-wordnum` - splits a typical `camelCase` or `PascalCase` string, 
    but keeps trailing digits in the word


### map-fn

`map-fn` takes a seq of strings and maps them. The built-in functions are
unicode aware regarding uppercase and lowercase.

  * `map-camel` - lowercase first element, capitalize the rest.
  * `map-lower` - lowercase all elements
  * `map-pascal` - capitalize the elements
  * `map-upper` - uppercase all elements


### join-fn

`join-fn` simply joins strings. The built-in functions are just thin, 
named wrappers of `clojure.string/join`:

  * `join-blank` - joins without a separator
  * `join-dash` - joins with dash (kebab-style)
  * `join-underscore` - joins with underscore (snake_style)

### coerce-fn

`coerce-fn` takes a `namespace` string and a `name` string (since the
original value can be a qualified ident) and returns something new. 
It can choose to ignore either of the parts.

   * `keyword` - same as `clojure.core/keyword`
   * `symbol` - same as `clojure.core/symbol`
   * `simple-keyword` - only uses the `name` part
   * `simple-symbol` - only uses the `name` part
   * `string` - uses `name` if `namespace` is nil, otherwise joins with a slash

