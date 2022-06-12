; (ns vending-machine-business.helper
;   (:require [clojure.tools.cli :refer [parse-opts]]
;             [clojure.string :as string])
; )

; (def cli-options
;   [
;    ["-f" "--file NAME" "File names to read"
;     :multi false ; use :update-fn to combine multiple instance of -f/--file
;     :default []
;     :update-fn conj]
;    ["-h" "--help"]
;   ]
; )

; (defn usage [options-summary]
;   (->> ["Simulation of multiple vending machines' transactions concurrently."
;         ""
;         "Usage: vending-business [options] action"
;         ""
;         "Options:"
;         options-summary
;         ""
;         "Please refer to the manual page for more information."]
;        (string/join \newline)
;   )
; )


;(def coin-won
;  '((50 0)(20 0)(10 0)(5 0)(2 0)(1 0))
;)

;(def max-deposit ;; max amount of space for a coin of any denomination
;  50
;)

;;;
;;; helper.rkt defines the initial data for the simulation of the machine and helper functions to be used through the rest of the program
;;;


;(defn update-coin-won [index value]
;  ; (set! coin-won (reconstruct-deposit coin-won index value))
;)

;(defn reset-coin-won
;  ; (set! coin-won '((50 0)(20 0)(10 0)(5 0)(2 0)(1 0)))
;)

;(defn update-money [index value]
;  ; (set! deposit (reconstruct-deposit deposit index value))
;)

;(defn update-inventory [index value]
;  ; (set! inventory (reconstruct-inventory inventory index value))
;)

;(defn reconstruct-deposit [datos index value]
;    (cond
;     (nil? datos) '()
;     (= index (first (first datos))) (append (list (list (first (first datos)) (+ (first (rest (first datos))) value))) (rest datos))
;     :else (append (list (first datos)) (reconstruct-deposit (rest datos) index value))
;    )
;)

;(defn reconstruct-inventory [datos index value]
;      (cond
;       (nil? datos) '()
;       (= index (first (first datos))) (append (list (list (first (first datos)) (first (rest (first datos))) (+ (first (rest (rest (first datos)))) value))) (rest datos))
;       :else (append (list (first datos)) (reconstruct-inventory (rest datos) index value))
;      )
;  )

;(defn destroy-product-list []
;  ; (delete-file (build-path (current-directory) "db" "product-list"))
;)

;(defn destroy-money-deposit []
;  ; (delete-file (build-path (current-directory) "db" "money-deposit"))
;)

;(defn destroy-transaction-file []
;  ; (delete-file (build-path (current-directory) "db" "test-transaction"))
;)

;(defn destroy-transactions-file []
;  ; (delete-file (build-path (current-directory) "db" "test-transactions"))
;)

;;; Set and get data to use in the main code:
;;; Make copies of the inventory and deposit
;;; Update stuff
;;; Retrieve data is needed

;(defn bubble [lts] ;; bubble sort for change automata
;  (if (nil? (rest lts))
;      lts
;      (if (> (first (first lts)) (first (cadr lts)))
;          (cons (first lts)
;                (bubble (rest lts)))
;          (cons (cadr lts)
;                (bubble (cons (first lts) (rest (rest lts)))))
;      )
;  )
;)

;(defn bubble-sort [N lts]
;  (cond
;        (= N 1) (bubble lts)
;        :else (bubble-sort (- N 1) (bubble lts))
;  )
;)

;(defn set-bubble [lts]
;  (bubble-sort (length lts) lts)
;)

;;; Check data from the money deposit
;(defn get-space [index]
;  (get-coin index deposit)
;)

;(defn get-coin [index coin-list]
;  (cond
;    (= index (first (first coin-list))) (first (rest (first coin-list)))
;    :else (get-coin index (rest coin-list))
;  )
;)

;;; Check data from the inventory
;(defn get-available [index]
;  (get-product index inventory)
;)

;(defn get-product [index inv]
;  (cond
;    (= index (first (first inv))) (first (rest (rest (first inv))))
;    :else (get-product index (rest inv))
;  )
;)

;;; Print inventory info. If get-available < 10 then is almost empty
;(defn almost-empty-inventory [inv]
;  (cond
;    [(nil? inv) #t]
;    [(< (get-available (first (first inv))) 10) (println (first (first inv))) (println " is almost empty or empty") (almost-empty-inventory (rest inv)) ] ;; if almost full
;  )
;)

;;; Print deposit info. If get-space is less than 10, then almos empty. If bigger than 50, empty
;(defn almost-full-coin []
;  (cond
;    (> (get-space 1) (- max-deposit 10)) (println "The deposit for coins of 1 is almost full or full, with: ") (println (get-space 1))  ;; if almost full
;  )
;  (cond
;    (> (get-space 2) (- max-deposit 10)) (println "The deposit for coins of 2 is almost full or full, with: ") (println (get-space 2)) ;; if almost full
;  )
;  (cond
;    [(> (get-space 5) (- max-deposit 10)) (println "The deposit for coins of 5 is almost full or full, with: ") (println (get-space 5)) ] ;; if almost full
;  )
;  (cond
;    [(> (get-space 10) (- max-deposit 10)) (println "The deposit for coins of 10 is almost full or full, with: ") (println (get-space 10)) ] ;; if almost full
;  )
;  (cond
;    [(> (get-space 20) (- max-deposit 10)) (println "The deposit for coins of 20 is almost full or full, with: ") (println (get-space 20)) ] ;; if almost full
;  )
;  (cond
;    [(> (get-space 50) (- max-deposit 10)) (println "The deposit for coins of 50 is almost full or full, with: ") (println (get-space 50)) ] ;; if almost full
;  )
;)

;(defn almost-empty-coin []
;  (cond
;    (< (get-space 1) 10) (println "The deposit for coins of 1 is almost empty or empty, with: ") (println (get-space 1))  ;; if almost empty
;  )
;  (cond
;    (< (get-space 2) 10) (println "The deposit for coins of 2 is almost empty or empty, with: ") (println (get-space 2))  ;; if almost empty
;  )
;  (cond
;    (< (get-space 5) 10) (println "The deposit for coins of 5 is almost empty or empty, with: ") (println (get-space 5))  ;; if almost empty
;  )
;  (cond
;    (< (get-space 10) 10) (println "The deposit for coins of 10 is almost empty or empty, with: ") (println (get-space 10))  ;; if almost empty
;  )
;  (cond
;    (< (get-space 20) 10) (println "The deposit for coins of 20 is almost empty or empty, with: ") (println (get-space 20))  ;; if almost empty
;  )
;  (cond
;    (< (get-space 50) 10) (println "The deposit for coins of 50 is almost empty or empty, with: ") (println (get-space 50))  ;; if almost empty
;  )
;)

;;; Execution of setup: creates inventory
;(println "::- [ Set up initial data ] -::")

;(cond
;  [(= del-data #t) (println "::- [ DELETING VENDING MACHINE DATA ]") (exit)]
;  [(= set-simulation #t) (set-default-inventory) (set-default-deposit) (set-default-transaction) (set-default-transactions)]
;  [(= set-simulation #f) (println "::- [ Simulation not set. Using custom user data ]" )

;   (cond
;     [(= inv-set #t) (get-inventory(set-inventory))]
;     [:else (println "::- [ Inventory not set. Ending program ]") (exit)]
;   )


;   (cond
;     [(= dep-set #t) (get-deposit(set-deposit))]
;     [:else (println "::- [ Deposit not set. Ending program ]") (exit) ]
;   )


;   (cond
;     [(and (= trns-set #t) (= trn-set #t)) (get-transaction(set-transaction)) (get-transactions(set-transactions)) (println "::- [ Using both transaction and transactions ]")]
;     [(and (= trns-set #t) (= trn-set #f)) (get-transactions(set-transactions)) (println "::- [ Using only default transactions ]")]
;     [(and (= trns-set #f) (= trn-set #t)) (get-transaction(set-transaction)) (println "::- [ Using only default transaction ]")]
;     [:else (println "::- [ Neither transaction nor transactions set. Ending program ]") (exit)]
;   )
;  ] ;; expecting data

;)

; (println "::- [ End setting up data ] -::")
