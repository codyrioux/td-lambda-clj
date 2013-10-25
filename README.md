# td-lambda-clj
A clojure library implementing a vectorized version of TD(lambda).
The goals of this impelementation are:

- Easy to use
- Safe (type checked, tested) [TODO]
- Vectorized
- Simple, idiomatic clojure
- Actively maintained

## Usage

Sample usage can be seen in src/td-lambda/sample.clj

```clojure
(use 'td-lambda.sample)
(def pol (create-policy 0.5 0.001 0.1 100))
(pol 0)
;=> 4
(pol 4)
;=> 4
(pol 8)
;=> 10
(pol 10)
;=> nil
```

## License

Copyright © 2013 Cody Rioux

Distributed under the Eclipse Public License, the same as Clojure.
