(ns clojure-soundcloud.core
  (:require [cheshire [core :as ch]]
            [clojure-soundcloud [manifest :as m]])
  (:import [com.soundcloud.api ApiWrapper Env Request Endpoints Params$Track Http]
           [org.apache.http HttpStatus]
           [java.io File]))

(def SC-INFO (ch/parse-stream
              (clojure.java.io/reader m/CREDENTIALS)
              true))

(defn create-wrapper
  "Based on com.soundcloud.api.examples.CreateWrapper from the
   java-api-wrapper example code. This is a rather ugly method
   for persisting an authentication token, but we've ported it
   relatively verbatim."
  []
  (let [{:keys [id secret username password]} SC-INFO
        wrapper (ApiWrapper. id secret nil nil Env/LIVE)
        token (.login wrapper username password)]
    (.toFile wrapper (File. m/SERIALISED-WRAPPER))))

(defn tidy-up
  "Parse the JSON from the response, wrap everything into a
   convenient map. Status code should always be 201."
  [response]
  (let [status-line (.getStatusLine response)
        status-code (.getStatusCode status-line)]
    {:status-line (str status-line)
     :status-code status-code
     :ok (= status-code HttpStatus/SC_CREATED)
     :detail (ch/parse-string (str (Http/getJSON response)) true)}))

(defn upload-file
  "Based on com.soundcloud.api.examples.UploadFile.
   NOTE: requires serialised wrapper on disk via
   create-wrapper."
  [filename]
  (let [file (File. filename)
        wrapper (ApiWrapper/fromFile (File. m/SERIALISED-WRAPPER))
        request (-> (Request/to Endpoints/TRACKS (into-array nil))
                    (.add Params$Track/TITLE (.getName file))
                    (.add Params$Track/TAG_LIST "demo upload")
                    (.withFile Params$Track/ASSET_DATA file))
        response (try
                   (.post wrapper request)
                   (finally (.toFile wrapper (File. m/SERIALISED-WRAPPER))))]
    (tidy-up response)))
