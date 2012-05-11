# SoundCloud for Clojure

Soundcloud connection for Clojure, using the
[java-api-wrapper][api]. This is pretty much a straight port of the
example code. All we've done so far is port the upload procedure, but
the rest should be straightforward.

(NOTE: I'm going to overhaul this - the auth. token serialisation from
the example code is pretty horrid, and makes the code definitely not
thread-safe, although if we're doing nonce-based authentication then
we can't parallelise it anyway.)

## Usage

SoundCloud's Java API libraries are all available on the Maven global
repositories, so we just fetch everything in Leiningen. (See
`project.clj`.)

There's a tiny amount of local configuration: the `create-wrapper`
function takes a filename for a JSON file, like so:

        {
                "soundcloud" : {
                        "username" : "[username]",
                        "password" : "[password]",
                        "id" : "[application id]",
                        "secret" : "[application secret]"
                }
        }

(We've put everything under "soundcloud" so that the same file can
be used to hold additional parameters.)

Hack the paths in `scratch.clj`, and enjoy.

## License

Copyright © 2012 Nick Rothwell, nick@cassiel.com

Distributed under the Eclipse Public License, the same as Clojure.

[api]: https://github.com/soundcloud/java-api-wrapper
