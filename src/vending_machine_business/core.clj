(ns vending-machine-business.core
  (:gen-class)
)

(require '[vending-machine-business.helper :as hp])

(defn start-transactions [inventory transactions]
  ;; get product in inventory and confirm it exist
  (cond
    (nil? transactions) true
    (product-exist? inventory (first transactions)) (start-transactions inventory (rest transactions)) ;; executes the transaction
    :else false
  )
)

(defn start-transaction [inventory transaction]
  ;; get product in inventory and confirm it exist
  (cond
    (product-exist? inventory transaction) true ;; executes the transaction
    :else false
  )
)

;; helper function of [start-transaction]
;; if product-name exist then return true
(defn product-exist? [inventory transaction]
  (cond
    ;; if inventory null then all inventory checked, if compute-transaction is false then the transaction is not valid, so returns false
    (nil? inventory) false
    (and (and (= (first(first inventory)) (first transaction)) (> ( first (rest (rest (first inventory)))) 0)) (compute-transaction (first inventory) transaction)) (update-inventory (first(first inventory)) -1) true
    :else (product-exist? (rest inventory) transaction)
  )
)

;; [compute-transaction] validates the transaction and updates de db
;; Getting money in
(defn compute-transaction [inventory transaction]
  ;; (first (rest transaction)) is the list of transitions form '(1 1 2 5 10 20 50)
  ;; (first (rest inventory)) is the price form '("product-name" price quantity) : ("gansitos" 17 2)

  (cond
    (and (check-currency-in (first (rest transaction))) (validate (first (rest transaction)) (first (rest inventory)) 0)) true
    :else false
  )
)

;; HERE DATA SHOULD BE UPDATED
;; current-state = money deposited by user
;; final-state = money that user should deposit
(defn validate [transactions final-state current-state]
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
(defn check-currency-in [transactions]
  (cond
    (nil? transactions) true
    (and (< (get-space 1) max-deposit) (= (first transactions) 1)) (update-coin-won 1 1)  (update-money (first transactions) 1) (check-currency-in (rest transactions))
    ;; if coin is added to the deposit, also add to a list to register the money won [(and (< (get-space 2) max-deposit) (= (first transactions) 2)) (update-coin-won 2 1) (update-money (first transactions) 1) (check-currency-in (rest transactions)) ] [(and (< (get-space 5) max-deposit) (= (first transactions) 5))  (update-coin-won 5 1)(update-money (first transactions) 1) (check-currency-in (rest transactions)) ]
    (and (< (get-space 10) max-deposit) (= (first transactions) 10)) (update-coin-won 10 1)(update-money (first transactions) 1) (check-currency-in (rest transactions))
    (and (< (get-space 20) max-deposit) (= (first transactions) 20)) (update-coin-won 20 1)(update-money (first transactions) 1) (check-currency-in (rest transactions))
    (and (< (get-space 50) max-deposit) (= (first transactions) 50)) (update-coin-won 50 1)(update-money (first transactions) 1) (check-currency-in (rest transactions))
    :else false
  )
)

(defn fare-automata [deposit debt] ;; in this automata the deposit has to be checked to decide what to return
  ;; debt is input - cost what the machine owes to the user
  ;; debt = (- current-state final-state)

  (cond
    (= debt 0) true ;; [updates the money and inventory db]
    ;; first(first money-deposit i)s coin value to update in db
    ;; cadar money-deposit is existance

    ;; If debt > 0 then select the first coin that is less or equal to deb
    ;; insert the coin
    (> debt 0)
     (cond
       (nil? deposit) false
       (or (and (< (first(first deposit)) debt) (> (cadar deposit) 0)) (and (= (first(first deposit)) debt) (> (cadar deposit) 0))) (return-coin (first(first deposit))) (fare-automata deposit (- debt (first(first deposit))))
       :else (fare-automata (rest deposit) debt)
     )

    :else false ;; does not update and returns the security-data to the document
  )
)

;; Return the coint to user. I.E erase coin from deposit
(defn return-coin [coin]
  (hp/update-money coin -1)
  (hp/update-coin-won coin -1)
)


(defn run-machines [file]
  (println file)
)

;; -f / -s mutually exclusive.
;; -h / -v mutually exclusive
;; -m has to be run with -t

(defn validate-args [args]
  ; (println args) ;; access args data with first
  (cond
    (or (= (first args) "-f") (= (first args) "--file")) (run-machines (rest args))
    (or (= (first args) "-s") (= (first args) "--simulation")) (run-machines hp/default-machine)
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
