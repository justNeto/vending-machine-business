(ns vending-machine-business.core
  (:gen-class)
)

(require '[vending-machine-business.helper :as hp])

;; Return the coint to user. I.E erase coin from deposit
(defn return-coin [coin]
  (hp/update-money coin -1)
  (hp/update-coin-won coin -1)
)

(defn fare-automata [deposit debt] ;; in this automata the deposit has to be checked to decide what to return
  ;; debt is input - cost what the machine owes to the user
  ;; debt = (- current-state final-state)

  (println)
  (println "Deposit in fare-automata " deposit)
  (println "Debt in fare-automata " debt)
  (println)

  (cond
    (= debt 0) true ;; [updates the money and inventory db]
                    ;; first(first money-deposit i)s coin value to update in db
                    ;; first (rest (first money-deposit i))s existance

    ;; If debt > 0 then select the first coin that is less or equal to deb
    ;; insert the coin
    (> debt 0)
     (cond
       (nil? deposit) false
       (or (and (< (first(first deposit)) debt) (> (first (rest (first deposit))) 0)) (and (= (first(first deposit)) debt) (> (first (rest (first deposit))) 0))) (do (return-coin (first(first deposit))) (fare-automata deposit (- debt (first(first deposit)))))
       :else (fare-automata (rest deposit) debt)
     )

    :else false ;; does not update and returns the security-data to the document
  )
)

(defn check-currency-in [transactions deposit inventory]
  ; (println "Transaction in check-currency-in: " transactions)
  (println "Transaction in check-currency-in:" (first transactions))
  (println "Inventory in check-currency-in :" inventory)

  (cond
    (nil? transactions) true
    (< (hp/get-space (first transactions) deposit) hp/max-deposit) (do (hp/update-coin-won (first transactions) 1 inventory deposit) (hp/update-money (first transactions) 1 inventory deposit) (check-currency-in (rest transactions) deposit inventory))
    ; (and (< (hp/get-space 1 deposit) hp/max-deposit) (= (first transactions) 1)) (do (hp/update-coin-won 1 1) (hp/update-money (first transactions) 1) (check-currency-in (rest transactions) deposit))
    ; (and (< (hp/get-space 2 deposit) hp/max-deposit) (= (first ) 2)) (do(hp/update-coin-won 2 1) (hp/update-money (first transactions) 1) (check-currency-in (rest transactions) deposit))
    ; (and (< (hp/get-space 5 deposit) hp/max-deposit) (= (first transactions) 5))  (do(hp/update-coin-won 5 1)(hp/update-money (first transactions) 1) (check-currency-in (rest transactions) deposit))
    ; (and (< (hp/get-space 10 deposit) hp/max-deposit) (= (first transactions) 10)) (do(hp/update-coin-won 10 1)(hp/update-money (first transactions) 1) (check-currency-in (rest transactions) deposit))
    ; (and (< (hp/get-space 20 deposit) hp/max-deposit) (= (first transactions) 20)) (do(hp/update-coin-won 20 1)(hp/update-money (first transactions) 1) (check-currency-in (rest transactions) deposit))
    ; (and (< (hp/get-space 50 deposit) hp/max-deposit) (= (first transactions) 50)) (do(hp/update-coin-won 50 1)(hp/update-money (first transactions) 1) (check-currency-in (rest transactions) deposit))

    :else false
  )
)

;; HERE DATA SHOULD BE UPDATED
;; current-state = money deposited by user
;; final-state = money that user should deposit
(defn validate [transactions final-state current-state deposit]

  (println)
  (println "Final-state in validate " final-state)
  (println "Current-state in validate " final-state)
  (println "Deposit in validate " deposit)
  (println "Transaction in validate " transactions)
  (println)

  (cond
    ;; If transactions not empty yet, then continue to add up the transactions inside the function
    (not(nil? transactions)) (validate (rest transactions) final-state (+ current-state (first transactions)) )

    ;; validated transactions without fare to return. update inv and money using transactions data
    (= final-state current-state) true

    ;; Change automata only needs to substract the expected value of the product to the value inputed by the user
    ;; For example, if a product costs 50 and user gave 55, then the machine must return 5 (input - cost)
    (> current-state final-state) (fare-automata deposit (- current-state final-state)) ;; starts change automata and updates stuff

    (< current-state final-state) false ;; transaction does not buy a product and db's not updated
  )
)

;; [compute-transaction] validates the transaction and updates de db
;; Getting money in
(defn compute-transaction [inventory transaction deposit]
  ;; (first (rest transaction)) is the list of transitions form '(1 1 2 5 10 20 50)
  ;; (first (rest inventory)) is the price form '("product-name" price quantity) : ("gansitos" 17 2)

  (println)
  (println "Inventory in compute-transaction: " inventory)
  (println "Deposit in compute-transaction: " deposit)
  (println "Transaction in compute-transaction: " transaction)
  (println)

  (cond
    (and (check-currency-in (first (rest transaction)) deposit inventory) (validate (first (rest transaction)) (first (rest inventory)) 0 deposit)) true
    :else false
  )
)

;; helper function of [start-transaction]
;; if product-name exist then return true
(defn product-exist? [inventory transaction deposit]

  (println "Inventory received in product-exist?: " inventory)
  (println "Deposit received in product-exist?: " deposit)
  (println "Transaction received in product-exist?: " transaction)

  (cond
    ;; if inventory null then all inventory checked, if compute-transaction is false then the transaction is not valid, so returns false
    (nil? inventory) false
    (and (and (= (first(first inventory)) (first transaction)) (> ( first (rest (rest (first inventory)))) 0)) (compute-transaction (first inventory) transaction deposit)) (do (hp/update-inventory (first(first inventory)) -1) true)
    :else (product-exist? (rest inventory) transaction deposit)
  )
)

(defn start-transactions [inventory deposit transactions]
  ;; get product in inventory and confirm it exist
  (println "Inventory received: " inventory)
  (println "Deposit received: " deposit)
  (println "Transactions received: " transactions)

  (cond
    (nil? transactions) true
    (product-exist? inventory (first transactions) deposit) (start-transactions inventory (rest transactions)) ;; executes the transaction
    :else false
  )
)

(defn start-transaction [machine]
  ;; get product in inventory and confirm it exist
  (cond
    (product-exist? inventory transaction deposit) true ;; executes the transaction
    :else false
  )
)


(defn final-stuff []
  (println "Final stuff")
)

(defn pass-list-to-automata [data] ;; this is the wrapper for the default data

  (println "Data received:" data)
  (println)

  (println "Machine name: " (first(first data)) )
  (println "Inventory: " (first (rest (first data))))
  (println "Deposit: " (first (rest (rest (first data)))))
  (println "Transaction: "(first (rest (rest (rest (first data))))))

  (cond
    (nil? data) (final-stuff) ;; if data is null then no more data found
    (= (start-transactions (first data) true) (println "Transaction completed");; inventory
  )
)

(defn pass-file-to-automata [data] ;; this is the wrapper for the specific data
  (println "Final stuff")
)

(defprotocol Format
  (fmt [this])
)
(extend-protocol Format
  clojure.lang.IPersistentList
    (fmt [this] (str "list"))
  clojure.lang.ArraySeq
    (fmt [this] (str "file"))
  clojure.lang.Atom
    (fmt [this] (str "atom"))
)

(defn run-machines [file]
  (println "Reaching this function")
  (cond
    (= (fmt file) "list") (pass-list-to-automata file)
    (= (fmt file) "file") (pass-file-to-automata file)
    :else (println "Cannot process the file.")
  )
)

;; -f / -s mutually exclusive.
;; -h / -v mutually exclusive
;; -m has to be run with -t

(defn wrapper-for-sim []
  (println "::- [ Business not specified. Creating default business ]")
  (hp/create-default-machine)
  (hp/create-coin-won)
  (run-machines (hp/read-business-data))
)


(defn validate-args [args]
  (println args) ;; access args data with first
  (cond
    (or (= (first args) "-f") (= (first args) "--file")) (run-machines (rest args))
    (or (= (first args) "-s") (= (first args) "--simulation")) (wrapper-for-sim)
    :else (println "Error. Ending the program.")
  )
)


(defn -main
  "Here I can provide a brief description of each function."
  [& args]
  (cond
    (nil? args) (hp/usage)
    :else (validate-args args)
  )
)
