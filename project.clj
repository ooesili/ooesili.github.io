(defproject idempotency "0.0.0-SNAPSHOT"
  :description "Lunch and learn on idempotency"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-core "1.5.0"]
                 [hiccup "1.0.5"]]
  :main ^:skip-aot idempotency.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :ring {:handler idempotency.core/app
         :port 3000
         :auto-refresh? true})
