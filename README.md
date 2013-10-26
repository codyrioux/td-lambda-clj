# td-lambda
A clojure library implementing a vectorized version of TD(lambda).

The goals of this impelementation are:

- Easy to use
- Safe (type checked, tested) [TODO - types]
- Vectorized
- Simple, idiomatic clojure
- Actively maintained

I plan to actively maintain this library indefinitely so please feel free
to submit feature requests, bug reports and the like and I'll attempt to
attend to them in a timely fashion. I'm also trying to hone my (nonexistent)
functional library design skills through some reading so bear with me.

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
