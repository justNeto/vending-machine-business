(defproject vending-machine-business "0.1.0-SNAPSHOT"
  :description "Simulation of multiple vending machines using clojure."
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :profiles {:uberjar {:aot :all :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :main ^:skip-aot vending-machine-business.core
  :target-path "target/%s"
)
