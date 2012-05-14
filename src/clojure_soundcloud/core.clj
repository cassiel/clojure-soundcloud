;; -*- tab-width: 4; -*-

(ns clojure-soundcloud.core
  (:require [cheshire [core :as ch]])
  (:import [com.soundcloud.api ApiWrapper Env Request Endpoints Params$Track Http]
		   [org.apache.http HttpStatus]
		   [java.io File]))


(defprotocol CONFIGURATION
  "Provide one of these to tell us where to find the credentials."
  (sc-credentials-filename [this]))

(defprotocol SOUNDCLOUD
  "We return one of these with an uploader, having created the token."
  (upload-file [this filename]))

(def temp-serial-file
  ^{:private true
	:doc "Temporary file to store serialised SoundCloud authentication token."}
  (let [f (File/createTempFile "sc-ser" nil)]
	(.deleteOnExit f)
	f))

(defn read-sc-info
  "Read SoundCloud authentication information from a JSON file."
  ^{:private true}
  [filename]
  (:soundcloud (ch/parse-stream
				(clojure.java.io/reader filename)
				true)))

(defn create-wrapper
  "Based on com.soundcloud.api.examples.CreateWrapper from the
   java-api-wrapper example code. This is a rather ugly method
   for persisting an authentication token, but we've ported it
   relatively verbatim."
  ^{:private true}
  [sc-info]
  (let [{:keys [id secret username password]} sc-info
		wrapper (ApiWrapper. id secret nil nil Env/LIVE)
		token (.login wrapper username password)]
	(.toFile wrapper temp-serial-file)))

(defn tidy-up
  "Parse the JSON from the response, wrap everything into a
   convenient map. Status code should always be 201."
  ^{:private true}
  [response]
  (let [status-line (.getStatusLine response)
		status-code (.getStatusCode status-line)]
	{:status-line (str status-line)
	 :status-code status-code
	 :ok (= status-code HttpStatus/SC_CREATED)
	 :detail (ch/parse-string (str (Http/getJSON response)) true)}))

(defn upload
  "Based on com.soundcloud.api.examples.UploadFile.
   NOTE: requires serialised wrapper on disk via
   create-wrapper.
   TODO: obviously not thread-safe since it re-writes the
   auth. token. (This is SoundCloud's method - don't blame me.)"
  ^{:private true}
  [filename]
  (let [file (File. filename)
		wrapper (ApiWrapper/fromFile temp-serial-file)
		request (-> (Request/to Endpoints/TRACKS (into-array nil))
					(.add Params$Track/TITLE (.getName file))
					(.add Params$Track/TAG_LIST "demo upload")
					(.withFile Params$Track/ASSET_DATA file))
		response (try
				   (.post wrapper request)
				   (finally (.toFile wrapper temp-serial-file)))]
	(tidy-up response)))

;; The actual SoundCloud handler. Takes a CONFIGURATION to name the
;; auth/password file, creates the token on disk, returns an uploader.

(defn soundcloud-handler
  [cfg]
  (let [_ (create-wrapper (read-sc-info (sc-credentials-filename cfg)))]
	(reify SOUNDCLOUD
	  (upload-file [this filename]
		(upload filename)))))
