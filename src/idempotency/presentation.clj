(ns idempotency.presentation
  (:require [hiccup.page :as page]))

(defn image [src]
  [:img {:src src}])

(def presentation
  (page/html5
    [:head
     [:title "Hey there"]
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"}]
     (page/include-css "/reveal/css/reveal.css"
                       "/reveal/css/theme/moon.css"
                       "/style.css")]
    [:body
     [:div.reveal
      [:div.slides
       [:section
        [:img.light-bulb {:src "/images/light-bulb.jpg"}]]
       [:section
        [:h1 "Idempotency"]
        [:q "\"...the property of certain operations in mathematics and computer science, that can be applied multiple times without changing the result beyond the initial application.\""]
        " - Some person on Wikipedia"]
       [:section
        [:h1 "Idempotency"]
        [:q "\"because it's easier to just try again, than to figure out what went wrong\""]
        " - Me"]
       [:section
        [:section
         [:h2 "So hot right now"]
         [:img {:src "/images/so-hot.jpg"}]]
        [:section
         [:h3 "Useful everywhere"]
         [:p "user interfaces"]
         [:p "databases"]
         [:p "configuration management"]
         [:p "HTTP/REST"]
         [:p "messaging"]
         [:p "orchestration/scheduling"]
         [:p "light switches"]]]
       [:section
        [:section
         [:h2 "User interfaces"]
         [:p "JavaScript ecosystem is embracing unidirectional data flow"]
         [:p "idempotent rendering"]]
        [:section
         [:h3 "Reactjs"]
         [:p "describes views declaratively instead of imperatively"]
         [:p "uses virtual-DOM diffing and reconciliation to achieve idempotency"]]]
       [:section
        [:section
         [:h2 "Database migrations"]
         [:p "safe to reapply"]
         [:p "allows apps to migrate on startup"]]
        [:section
         [:h3 "The migrations table"]
         [:p "keeps track of all currently applied migrations"]
         [:p "is a no-op when there are new migrations to apply"]]]
       [:section
        [:section
         [:h2 "Configuration management"]
         [:p "no danger in rerunning the system if something goes wrong"]
         [:p "can naturally even out inconsistencies"]]
        [:section
         [:h3 "Puppet"]
         [:p "puppet modules declare how system work"]
         [:p "agents blindly re-apply configuration every half hour"]]]
       [:section
        [:section
         [:h2 "HTTP/REST"]
         [:p "difference between PUT and POST"]]
        [:section
         [:h3 "Create/update"]
         [:p "creating a new resource is generally not idempotent"]
         [:code "POST /things"]
         [:p "updating an existing resource is"]
         [:code "PUT /things/1"]]
        [:section
         [:h3 "POST"]
         [:p "usually, no unique ID is sent during creating"]
         [:p "server can't figure out if resource has already been created"]]
        [:section
         [:h3 "PUT"]
         [:p "declarative updates to an existing resource"]
         [:p "server knows what resource is being referred to"]
         [:p "it's easy to tell if something has changed or not"]]]
       [:section
        [:section
         [:h2 "Messaging"]
         [:p "works great with at-least-once delivery guarantees"]]
        [:section
         [:h3 "Kafka and NSQ"]
         [:p "exactly-once semantics are expensive"]
         [:p "message bus can be more efficient when using at-least-once semantics"]
         [:p "idempotent messages can easily be retried in case of delivery failure"]]]
       [:section
        [:section
         [:h2 "Orchestration/scheduling"]
         [:p "works well with self-healing systems"]]
        [:section
         [:h2 "CF HM9000 and K8S replication controllers"]
         [:p "what apps are currently running?"]
         [:p "what apps do I expect to be running?"]
         [:p "what can I do to reconcile the differences?"]]]
       [:section
        [:section
         [:h2 "But, how?"]]
        [:section
         [:h3 "Say what you want"]
         [:p.fragment "instead of saying how to get there"]]
        [:section
         [:h3 "Be declarative"]
         [:p "find and use naturally idempotent operations"]
         [:p "intelligently reconcile declared and actual state"]]]
       [:section
        [:section
         [:h2 "Getting technical"]]
        [:section
         [:h3 "Purity"]
         [:p [:code "f(x) == f(x)"]]
         [:h3 "Idempotency"]
         [:p [:code "f(f(x)) == f(x)"]]]
        [:section
         [:h3 "Functional programming"]
         [:p "immutable data"]
         [:p "pure functions"]
         [:p "helpful but not necessary"]]]
       [:section
        [:h2 "Build robust, performant systems"]
        [:p "operations can be safely retried"]
        [:p "interruptions are recoverable"]
        [:p "aggressively cache results, since they are predictable"]]
       [:section
        [:h1 "Thank you!"]]]]]
    (page/include-js "/reveal/js/reveal.js"
                     "/app.js")))
