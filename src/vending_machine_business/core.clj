(ns vending-machine-business.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as string])
  (:gen-class)
)

(def cli-options
  [
   ["-f" "--file NAME" "File names to read"
    :multi false ; use :update-fn to combine multiple instance of -f/--file
    :default []
    :update-fn conj]
   ["-h" "--help"]
  ]
)

(defn -main
  "Here I can provide a brief description of each function."
  [& args]
  (println "Second thing second... " args)
)
