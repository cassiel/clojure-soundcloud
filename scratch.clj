(require '[cheshire [core :as ch]]
         '[clojure-soundcloud [core :as c]])
(import '[com.soundcloud.api ApiWrapper Env Request Params Params$Track Http])

; Do this first:
(c/create-wrapper)

(def res (c/upload-file "/Users/nick/Desktop/0001 TAKES.wav"))
(def res (c/upload-file "/Users/nick/Desktop/grooble.wav"))
