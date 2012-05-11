(require '[cheshire [core :as ch]]
         '[clojure-soundcloud [core :as c]])
(import '[com.soundcloud.api ApiWrapper Env Request Params Params$Track Http])

; Do this first:
(c/create-wrapper (c/read-sc-info "/Users/nick/Desktop/sc-credentials.json"))

(def res (c/upload-file "/Users/nick/Desktop/0001 TAKES.wav"))
(def res (c/upload-file "/Users/nick/Desktop/grooble.wav"))

res
(:status-line res)

clojure-soundcloud.core/temp-serial-file
