(ns vending-machine-business.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))


;; Actual code that will do something
(def cli-options

  ;; An option with a required argument
  [["-s" "--simulation" "Start simulation"
    :default 80
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ;; A non-idempotent option (:default is applied first)
   ["-v" nil "Verbosity level"
    :id :verbosity
    :default 0
    :update-fn inc]

  ["-m" "--machines MACHINES" ] ;; MACHINES is the path to a file with a list of machines in the format
                                ;; '((machine_name path_to_machine_transactions path_to_machine_deposit path_to_machine_inventory) [...] )


   ["-h" "--help"]]
)

(defn -main
  "Here I can provide a brief description of each main function."
  [& args]
  (println "Hello, world!")
)
