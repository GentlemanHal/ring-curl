# ring-curl [![Build Status](https://snap-ci.com/GentlemanHal/ring-curl/branch/master/build_image)](https://snap-ci.com/GentlemanHal/ring-curl/branch/master)

A Clojure library designed to convert [ring](https://github.com/ring-clojure/ring/wiki/Concepts) requests into [cURL](http://curl.haxx.se/docs/manpage.html) commands.

It's main purpose is in debugging applications, as it easily allows you to replay requests against services.

## Installation

`ring-curl` is available as a Maven artifact from [Clojars](http://clojars.org/ring-curl).

## Usage

```clojure
(ns your-app.core
  (:require [ring-curl.core :as :ring-curl]))

(ring-curl/to-curl request)
```

### clj-http

If you use [clj-http](https://github.com/dakrone/clj-http) you can use the `convert` function under the `ring-curl.clj-http` namespace to convert it to a ring request. This will allow it to be printed correctly as curl by the `core` namespace.

## License

Copyright © 2014 Christopher Martin

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
