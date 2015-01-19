# ring-curl [![Build Status](https://snap-ci.com/GentlemanHal/ring-curl/branch/master/build_image)](https://snap-ci.com/GentlemanHal/ring-curl/branch/master)

A Clojure library designed to convert [ring](https://github.com/ring-clojure/ring/wiki/Concepts) requests into [cURL](http://curl.haxx.se/docs/manpage.html) commands.

It's main purpose is in debugging applications, as it easily allows you to replay requests against services.

## Installation

`ring-curl` is available as a Maven artifact from [Clojars](http://clojars.org/ring-curl).

## Usage

```clojure
(ns your-app.core
  (:require [ring-curl.core :as ring-curl]))

(ring-curl/to-curl request)

; or with options

(ring-curl/to-curl request {:some-option "the-value"})
```

### Options

The following options can be used to modify the output, see the [curl man page](http://curl.haxx.se/docs/manpage.html) for more details about exactly what each option will do.

- `:verbose?`
  A `truthy` value adds the `-v` curl flag to the output.

- `:very-silent?`
  A `truthy` value adds the `-s` curl flag to the output.

- `:silent?`
  A `truthy` value adds the `-s` and `-S` curl flags to the output.

- `:no-proxy`
  A `sequence` of `strings` will add the `--noproxy "<vals>"` curl flag to the output.  

- `:progress-bar?`
  A `truthy` value adds the `-#` curl flag to the output.

- `:insecure?`
  A `truthy` value adds the `-k` curl flag to the output.

- `:connect-timeout`
  A `integer` value adds the `--connect-timeout <val>` curl flag to the output.

- `:max-time`
  A `integer` value adds the `-m <val>` curl flag to the output.

- `:no-buffer?`
  A `truthy` value adds the `-N` curl flag to the output.

- `:output`
  A `string` value adds the `-o "<val>"` and `--create-dirs` curl flags to the output.

- `:retry`
  A `interger` value adds the `--retry <val>` curl flag to the output.

- `:dump-headers`
  A `string` value adds the `-D "<val>"` curl flag to the output.

### clj-http

If you use [clj-http](https://github.com/dakrone/clj-http) you can use the `convert` function under the `ring-curl.clj-http` namespace to convert it to a ring request. This will allow it to be printed correctly as curl by the `core` namespace.

## Contributing

If you would like to add a feature/fix a bug for us please create a pull request. Be sure to include or update any tests
if you want your pull request accepted.

## License

Copyright © 2014 Christopher Martin

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
