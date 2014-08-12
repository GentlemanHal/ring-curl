# ring-curl

A Clojure library designed to convert [ring](https://github.com/ring-clojure/ring/wiki/Concepts) requests into [cURL](http://curl.haxx.se/docs/manpage.html) commands.

## Usage

```clojure
(:require ring-curl.core :refer :all)

(to-curl request)
```

## License

Copyright © 2014 Christopher Martin

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
