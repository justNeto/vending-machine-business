(ns vending-machine-business.helper)
(use 'clojure.java.io)

(defn usage []
  (println "Usage: vending-business [OPTIONS] ... [FILE] ...")
  (println)
  (println "Simulation of several vending machines. To run, pass a file with the vending machines.")
  (println "The format for the file will be ((nombre-maquina(('nombre-producto' precio cantidad) ...)((valor disponibilidad) ... )('nombre-producto' (moneda moneda moneda))...) ... )")
  (println)
  (println "Options")
  (println "-f, --file FILE                 specify file with database.")
  (println "-s, --simulation                will run a simulation with default data and will save it in db/ project folder.")
  (println "-m, --machine MACHINE           will select a machine to work with individually. ")
  (println "-t, --transaction TRANSACTION   will use a transaction file with format transaction::- (('nombre producto' (moneda ...)) ...) ")

  (println)
  (println "-h, --help                      shows this help menu and exit.")
  (println "-v, --version                   displays current version of script.\n")
)

(defn write-file [file-path data]
  (spit file-path
        (pr-str data)
  )
)

(defn read-file [file-path]
  (read-string (slurp file-path))
)

;; Define paths to save the data
(def business-path "/home/neto/Documents/Tec/4thSemester/IMC/EvidenciaTres/vending-machine-business/src/vending_machine_business/business-data")
(def coin-path "/home/neto/Documents/Tec/4thSemester/IMC/EvidenciaTres/vending-machine-business/src/vending_machine_business/coin-won")

;; Default machines
(def default-machine
  '(
    ("first-machine" (("coca" 15 10)("agua" 15 12)("manzanita" 7 10)) ((10 20)(5 30)(2 15)(1 20)) (("coca" (1 1 1 5 10))("chocoroles" (1 1 1 5 10))("agua" (1 1 1 5 10))))
    ("second-machine" (("gansito" 18 6) ("pinguinos" 15 17) ("coca" 20 20)) ((10 20)(5 22)(2 15)(1 50)) (("coca" (1 1 1 5 10))("chocoroles" (1 1 1 5 10))("agua" (1 1 1 5 10))))
    ("third-machine" (("manzanita" 20 20) ("agua" 15 20) ("chocoroles" 18 20)) ((10 20)(5 31)(2 11)(1 12)) (("coca" (1 1 1 5 10))("chocoroles" (1 1 1 5 10))("agua" (1 1 1 5 10))))
    ("fourth-machine" (("fresca" 18 17) ("fritos" 15 17) ("ruffles" 15 20)) ((10 30)(5 5)(2 5)(1 31)) (("coca" (1 1 1 5 10))("chocoroles" (1 1 1 5 10))("agua" (1 1 1 5 10))))
    ("fifth-machine" (("coca" 15 10)("fresca" 18 5)("manzanita" 10 23)) ((10 53)(5 20)(2 10)(1 35)) (("coca" (1 1 1 5 10))("chocoroles" (1 1 1 5 10))("agua" (1 1 1 5 10))))
    ("sixth-machine" (("coca" 15 15) ("pinguinos" 15 7) ("ruffles" 15 14)) ((20 23)(10 10)(5 10)(2 40)(1 26))(("coca" (1 1 1 5 10))("chocoroles" (1 1 1 5 10))("agua" (1 1 1 5 10))))
  )
)

(def coin-won ;; a unmutable data container to represent the information gain
  '(
    ("first-machine"((50 0)(20 0)(10 0)(5 0)(2 0)(1 0)))
    ("second-machine"((50 0)(20 0)(10 0)(5 0)(2 0)(1 0)))
    ("third-machine"((50 0)(20 0)(10 0)(5 0)(2 0)(1 0)))
    ("fourth-machine"((50 0)(20 0)(10 0)(5 0)(2 0)(1 0)))
    ("fifth-machine"((50 0)(20 0)(10 0)(5 0)(2 0)(1 0)))
    ("sixth-machine"((50 0)(20 0)(10 0)(5 0)(2 0)(1 0)))
  )
)

(defn create-coin-won []
  (write-file coin-path coin-won)
)

(defn create-default-machine []
  (write-file business-path default-machine)
)

(defn read-business-data []
  (read-file business-path)
)

(defn read-won-coin []
 (read-file coin-path)
)

(def max-deposit 50)

;;
;; helper.rkt defines the initial data for the simulation of the machine and helper functions to be used through the rest of the program
;;

(defn reconstruct-deposit [datos index value]
    (cond
     (nil? datos) '()
     (= index (first (first datos))) (concat (list (list (first (first datos)) (+ (first (rest (first datos))) value))) (rest datos))
     :else (concat (list (first datos)) (reconstruct-deposit (rest datos) index value))
    )
)

(defn reconstruct-inventory [datos index value]
      (cond
       (nil? datos) '()
       (= index (first (first datos))) (concat (list (list (first (first datos)) (first (rest (first datos))) (+ (first (rest (rest (first datos)))) value))) (rest datos))
       :else (concat (list (first datos)) (reconstruct-inventory (rest @datos) index value))
      )
)

 (defn update-coin-won [index value]
  (println "Updating coin won")
  () ;; open won coin
  (println coin-won)
  ;; save coin-won in a file to be opened next time
 )

(defn update-money [index value inventory]
  ; (set! deposit (reconstruct-deposit deposit index value))
  (println "Setting deposit")
)

(defn update-inventory [index value inventory]
  ; (set! inventory (reconstruct-inventory inventory index value))
  (println "Updating inventory")
)


;; Set and get data to use in the main code:
;; Make copies of the inventory and deposit
;; Update stuff
;; Retrieve data is needed

(defn bubble [lts] ;; bubble sort for change automata
  (if (nil? (rest lts))
      lts
      (if (> (first (first lts)) (first (first (rest lts))))
          (cons (first lts)
                (bubble (rest lts)))
          (cons (first (rest lts))
                (bubble (cons (first lts) (rest (rest lts)))))
      )
  )
)

(defn bubble-sort [N lts]
  (cond
        (= N 1) (bubble lts)
        :else (bubble-sort (- N 1) (bubble lts))
  )
)

(defn set-bubble [lts]
  (bubble-sort (.length lts) lts)
)

;; Check data from the money deposit
(defn get-coin [index coin-list]
  (cond
    (= index (first (first coin-list))) (first (rest (first coin-list)))
    :else (get-coin index (rest coin-list))
  )
)

(defn get-space [index deposit]
  (get-coin index deposit)
)


;; Check data from the inventory
(defn get-product [index inv]
  (cond
    (= index (first (first inv))) (first (rest (rest (first inv))))
    :else (get-product index (rest inv))
  )
)

(defn get-available [index inventory]
  (get-product index inventory)
)

; ;; Print inventory info. If get-available < 10 then is almost empty
; (defn almost-empty-inventory [inv]
;   (cond
;     (nil? inv) true
;     (< (get-available (first (first inv))) 10) (println (first (first inv))) (println " is almost empty or empty") (almost-empty-inventory (rest inv)) ;; if almost full
;   )
; )

; ;; Print deposit info. If get-space is less than 10, then almos empty. If bigger than 50, empty
; (defn almost-full-coin []
;   (cond
;     (> (get-space 1) (- max-deposit 10)) (println "The deposit for coins of 1 is almost full or full, with: ") (println (get-space 1))  ;; if almost full
;   )
;   (cond
;     (> (get-space 2) (- max-deposit 10)) (println "The deposit for coins of 2 is almost full or full, with: ") (println (get-space 2)) ;; if almost full
;   )
;   (cond
;     [(> (get-space 5) (- max-deposit 10)) (println "The deposit for coins of 5 is almost full or full, with: ") (println (get-space 5)) ] ;; if almost full
;   )
;   (cond
;     [(> (get-space 10) (- max-deposit 10)) (println "The deposit for coins of 10 is almost full or full, with: ") (println (get-space 10)) ] ;; if almost full
;   )
;   (cond
;     [(> (get-space 20) (- max-deposit 10)) (println "The deposit for coins of 20 is almost full or full, with: ") (println (get-space 20)) ] ;; if almost full
;   )
;   (cond
;     [(> (get-space 50) (- max-deposit 10)) (println "The deposit for coins of 50 is almost full or full, with: ") (println (get-space 50)) ] ;; if almost full
;   )
; )

; (defn almost-empty-coin []
;   (cond
;     (< (get-space 1) 10) (println "The deposit for coins of 1 is almost empty or empty, with: ") (println (get-space 1))  ;; if almost empty
;   )
;   (cond
;     (< (get-space 2) 10) (println "The deposit for coins of 2 is almost empty or empty, with: ") (println (get-space 2))  ;; if almost empty
;   )
;   (cond
;     (< (get-space 5) 10) (println "The deposit for coins of 5 is almost empty or empty, with: ") (println (get-space 5))  ;; if almost empty
;   )
;   (cond
;     (< (get-space 10) 10) (println "The deposit for coins of 10 is almost empty or empty, with: ") (println (get-space 10))  ;; if almost empty
;   )
;   (cond
;     (< (get-space 20) 10) (println "The deposit for coins of 20 is almost empty or empty, with: ") (println (get-space 20))  ;; if almost empty
;   )
;   (cond
;     (< (get-space 50) 10) (println "The deposit for coins of 50 is almost empty or empty, with: ") (println (get-space 50))  ;; if almost empty
;   )
; )
